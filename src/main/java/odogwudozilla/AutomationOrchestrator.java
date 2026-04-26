package odogwudozilla;

import odogwudozilla.automation.AcceptanceVerifier;
import odogwudozilla.automation.AnswerSubmitter;
import odogwudozilla.automation.AutomationConfig;
import odogwudozilla.automation.BrowserSessionManager;
import odogwudozilla.automation.DocumentationUpdater;
import odogwudozilla.automation.GitAutomationService;
import odogwudozilla.automation.InputFetcher;
import odogwudozilla.automation.PuzzleInfo;
import odogwudozilla.automation.PuzzleScraper;
import odogwudozilla.automation.RateLimiter;
import odogwudozilla.automation.SolutionSkeletonGenerator;
import odogwudozilla.automation.SolverRunner;
import odogwudozilla.automation.SubmissionResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Main entry point for the fully automated Advent of Code puzzle pipeline.
 *
 * <p>Supported run modes (pass as {@code --args} to the Gradle task):</p>
 * <ul>
 *   <li><b>(no args)</b> - full pipeline with an interactive pause at Stage 5 for the
 *       user to implement the solve methods before submission.</li>
 *   <li><b>{@code --setup}</b> - Stages 1-4 only: select puzzle, scrape description,
 *       fetch input, generate solution skeleton. Exits after generating the skeleton,
 *       leaving the implementation to the user or Copilot.</li>
 *   <li><b>{@code --auto}</b> - full pipeline but skips the Stage 5 pause. Use this
 *       after the solve methods have already been implemented (e.g., by Copilot in chat).</li>
 *   <li><b>{@code --submit YEAR DAY}</b> - Stages 6-12 only for a specific puzzle:
 *       runs the solver, submits answers, updates docs, and commits. Use this when
 *       the solution is already written and you only need to submit.</li>
 *   <li><b>{@code --watch}</b> - may be combined with any mode flag. Opens a visible
 *       browser window with slow-motion actions so a human observer can follow what
 *       is happening in real time. Example: {@code --auto --watch}</li>
 * </ul>
 *
 * <p>Typical Copilot-driven workflow (chat):</p>
 * <ol>
 *   <li>Run: {@code ./gradlew autoSolve --args="--setup"}</li>
 *   <li>Copilot reads the generated description file and implements solve methods</li>
 *   <li>Run: {@code ./gradlew autoSolve --args="--auto"} (or {@code --submit YEAR DAY})</li>
 * </ol>
 *
 * <p>Run via: {@code ./gradlew autoSolve [--args="--setup|--auto|--submit YEAR DAY"]}</p>
 */
public final class AutomationOrchestrator {

    private static final Logger LOGGER = Logger.getLogger(AutomationOrchestrator.class.getName());

    private static final int MAX_SUBMIT_RETRIES = 3;
    private static final String FLAG_SETUP = "--setup";
    private static final String FLAG_AUTO = "--auto";
    private static final String FLAG_SUBMIT = "--submit";

    /** File used to persist the pending puzzle between runs. */
    private static final String STATE_FILE = ".aoc-state";

    private final BufferedReader stdinReader;
    private final boolean watchMode;

    /**
     * Creates an AutomationOrchestrator with a shared stdin reader.
     * Watch mode is off by default.
     */
    public AutomationOrchestrator() {
        this.stdinReader = new BufferedReader(new InputStreamReader(System.in));
        this.watchMode = false;
    }

    /**
     * Creates an AutomationOrchestrator with explicit watch mode setting.
     * @param watchMode {@code true} to open a visible browser with slow-motion actions
     */
    public AutomationOrchestrator(boolean watchMode) {
        this.stdinReader = new BufferedReader(new InputStreamReader(System.in));
        this.watchMode = watchMode;
    }

    /**
     * Entry point for the automation pipeline.
     * @param args optional run-mode flags; may include any of:
     *             {@code --setup}, {@code --auto}, {@code --submit YEAR DAY}, {@code --watch}
     */
    public static void main(String[] args) {
        boolean watchMode = containsFlag(args, AutomationConfig.FLAG_WATCH);
        AutomationOrchestrator orchestrator = new AutomationOrchestrator(watchMode);
        try {
            orchestrator.dispatch(args);
        } catch (Exception e) {
            LOGGER.severe("main - pipeline failed: " + e.getMessage());
            System.out.println("\nPipeline failed: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Returns true if the given flag appears anywhere in the args array.
     * @param args the command line arguments array
     * @param flag the flag to search for
     * @return true if the flag is present
     */
    private static boolean containsFlag(@NotNull String[] args, @NotNull String flag) {
        for (String arg : args) {
            if (flag.equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dispatches to the correct pipeline mode based on the provided arguments.
     * The {@code --watch} flag may appear in any position and is ignored when
     * determining the mode.
     * @param args command line arguments from main()
     * @throws Exception if any pipeline stage fails unrecoverably
     */
    private void dispatch(String[] args) throws Exception {
        // Find the primary mode flag, ignoring --watch
        String modeFlag = "";
        for (String arg : args) {
            if (!AutomationConfig.FLAG_WATCH.equalsIgnoreCase(arg)) {
                modeFlag = arg;
                break;
            }
        }

        if (FLAG_SETUP.equalsIgnoreCase(modeFlag)) {
            runSetupOnly();
        } else if (FLAG_AUTO.equalsIgnoreCase(modeFlag)) {
            runFull(true);
        } else if (FLAG_SUBMIT.equalsIgnoreCase(modeFlag)) {
            runSubmitOnly(args);
        } else {
            runFull(false);
        }
    }

    // -------------------------------------------------------------------------
    // Mode: --setup  (Stages 1-4)
    // -------------------------------------------------------------------------

    /**
     * Runs Stages 1-4 only: selects a puzzle, scrapes description, fetches input,
     * and generates the solution skeleton. Prints the skeleton path and exits.
     * @throws Exception if any stage fails
     */
    private void runSetupOnly() throws Exception {
        PuzzleInfo info = selectPuzzle();
        if (info == null) {
            return;
        }

        SolutionSkeletonGenerator skeletonGenerator = new SolutionSkeletonGenerator();

        try (BrowserSessionManager session = newBrowserSession()) {
            info = scrapeAndFetch(info, session, new RateLimiter());
            skeletonGenerator.generate(info);
        }

        System.out.println("\n[Setup complete] Skeleton generated for: " + info);
        System.out.println("  Description file: src/main/resources/" + info.getYear()
                + "/day" + info.getDay() + "/day" + info.getDay() + "_puzzle_description.txt");
        System.out.println("  Source file:      src/main/java/odogwudozilla/year"
                + info.getYear() + "/day" + info.getDay() + "/");

        savePendingState(info);

        System.out.println("\nImplement solvePartOne and solvePartTwo, then run:");
        System.out.println("  ./gradlew autoSolve --args=\"--auto\"");
        System.out.println("  (or: ./gradlew autoSolve --args=\"--submit "
                + info.getYear() + " " + info.getDay() + "\")");
    }

    // -------------------------------------------------------------------------
    // Mode: (default) or --auto  (full pipeline)
    // -------------------------------------------------------------------------

    /**
     * Runs the full pipeline: select, scrape, fetch, generate, (optionally pause),
     * solve, submit, document, commit.
     * @param skipPause if true the Stage 5 user-confirmation pause is skipped
     * @throws Exception if any stage fails
     */
    private void runFull(boolean skipPause) throws Exception {
        PuzzleInfo info = selectPuzzle();
        if (info == null) {
            return;
        }

        RateLimiter rateLimiter = new RateLimiter();
        AcceptanceVerifier verifier = new AcceptanceVerifier();
        DocumentationUpdater docUpdater = new DocumentationUpdater();
        GitAutomationService git = new GitAutomationService();
        SolverRunner solver = new SolverRunner();
        SolutionSkeletonGenerator skeletonGenerator = new SolutionSkeletonGenerator();

        try (BrowserSessionManager session = newBrowserSession()) {
            AnswerSubmitter submitter = new AnswerSubmitter(session, rateLimiter, verifier);

            info = scrapeAndFetch(info, session, rateLimiter);
            skeletonGenerator.generate(info);

            if (!skipPause) {
                printImplementationPrompt(info);
                awaitUserConfirmation();
            } else {
                System.out.println("\n[Stage 5] Skipping pause (--auto mode) - assuming solution is implemented.");
            }

            info = solveAndSubmit(info, solver, submitter, session, skeletonGenerator, docUpdater, git, rateLimiter);
        }
    }

    // -------------------------------------------------------------------------
    // Mode: --submit YEAR DAY  (Stages 6-12 only)
    // -------------------------------------------------------------------------

    /**
     * Runs Stages 6-12 for a specific year and day: builds, runs, submits, documents, commits.
     * Does not scrape or re-generate the skeleton - assumes files already exist.
     * Year and day are extracted from the non-flag tokens in args, so {@code --watch}
     * may appear in any position without affecting parsing.
     * @param args the full args array, e.g. {@code ["--submit", "2025", "1"]} or
     *             {@code ["--submit", "--watch", "2025", "1"]}
     * @throws Exception if any stage fails
     */
    private void runSubmitOnly(String[] args) throws Exception {
        // Extract year and day - skip all flag tokens (those starting with --)
        List<String> numericArgs = new ArrayList<>();
        for (String arg : args) {
            if (!arg.startsWith("--")) {
                numericArgs.add(arg);
            }
        }

        if (numericArgs.size() < 2) {
            throw new IllegalArgumentException(
                    "Usage: --submit YEAR DAY  (e.g., --submit 2025 1)");
        }

        int year = Integer.parseInt(numericArgs.get(0));
        int day  = Integer.parseInt(numericArgs.get(1));

        System.out.println("\n[Submit mode] Year " + year + " Day " + day);
        PuzzleInfo info = PuzzleInfo.minimal(year, day);
        info = enrichFromDescriptionFile(info);

        RateLimiter rateLimiter = new RateLimiter();
        AcceptanceVerifier verifier = new AcceptanceVerifier();
        DocumentationUpdater docUpdater = new DocumentationUpdater();
        GitAutomationService git = new GitAutomationService();
        SolverRunner solver = new SolverRunner();
        SolutionSkeletonGenerator skeletonGenerator = new SolutionSkeletonGenerator();

        try (BrowserSessionManager session = newBrowserSession()) {
            AnswerSubmitter submitter = new AnswerSubmitter(session, rateLimiter, verifier);
            solveAndSubmit(info, solver, submitter, session, skeletonGenerator, docUpdater, git, rateLimiter);
        }
    }

    // -------------------------------------------------------------------------
    // Shared pipeline stages
    // -------------------------------------------------------------------------

    /**
     * Selects a puzzle to work on. If a pending state file exists from a
     * previous {@code --setup} run, that puzzle is resumed instead of
     * selecting a new random one.
     * @return a minimal PuzzleInfo, or null if no unsolved puzzles remain
     * @throws IOException if the config or solutions database cannot be read
     */
    @Nullable
    private PuzzleInfo selectPuzzle() throws IOException {
        // Check for a pending puzzle left over from a previous --setup run
        PuzzleInfo pending = loadPendingState();
        if (pending != null) {
            System.out.println("\n[Stage 1] Resuming pending puzzle: Year " + pending.getYear()
                    + " Day " + pending.getDay()
                    + "  ->  " + pending.getPuzzleUrl());
            return pending;
        }

        System.out.println("\n[Stage 1] Selecting random unsolved puzzle...");
        PuzzleRandomizer.PuzzleSelection selection = PuzzleRandomizer.selectRandomUnsolvedPuzzle();
        if (selection == null) {
            System.out.println("No unsolved puzzles available. All done!");
            return null;
        }
        System.out.println("  Selected: Year " + selection.year + " Day " + selection.day
                + "  ->  https://adventofcode.com/" + selection.year + "/day/" + selection.day);
        return PuzzleInfo.minimal(selection.year, selection.day);
    }

    /**
     * Runs Stages 2 and 3: scrapes the puzzle description and fetches the input.
     * @param info minimal PuzzleInfo
     * @param session authenticated browser session
     * @param rateLimiter the shared rate limiter
     * @return updated PuzzleInfo with title, description, and input saved
     * @throws IOException if the input file cannot be written
     */
    @NotNull
    private PuzzleInfo scrapeAndFetch(@NotNull PuzzleInfo info,
                                      @NotNull BrowserSessionManager session,
                                      @NotNull RateLimiter rateLimiter) throws IOException {
        System.out.println("\n[Stage 2] Scraping puzzle description...");
        PuzzleScraper scraper = new PuzzleScraper(session);
        info = scraper.scrapePartOne(info);
        LOGGER.info("scrapeAndFetch - scraped title: " + info.getTitle());

        System.out.println("[Stage 3] Fetching puzzle input...");
        new InputFetcher(session).fetchAndSave(info);
        rateLimiter.recordRequest();
        return info;
    }

    /**
     * Runs Stages 6-12: compiles and runs the solver, submits answers, updates
     * documentation, and commits.
     * @return the final PuzzleInfo (may have Part 2 description added)
     * @throws Exception if any stage fails
     */
    @NotNull
    private PuzzleInfo solveAndSubmit(@NotNull PuzzleInfo info,
                                      @NotNull SolverRunner solver,
                                      @NotNull AnswerSubmitter submitter,
                                      @NotNull BrowserSessionManager session,
                                      @NotNull SolutionSkeletonGenerator skeletonGenerator,
                                      @NotNull DocumentationUpdater docUpdater,
                                      @NotNull GitAutomationService git,
                                      @NotNull RateLimiter rateLimiter) throws Exception {
        // Stage 6: run solver
        System.out.println("\n[Stage 6] Running solution...");
        SolverRunner.SolveOutput output = solver.run(info);

        if (!output.hasPartOne()) {
            throw new IllegalStateException(
                    "Part 1 answer not captured. Ensure solvePartOne prints: 'Part 1: <answer>'");
        }

        String partOneAnswer = output.getPartOneAnswer();
        assert partOneAnswer != null : "partOneAnswer is null after hasPartOne() check";

        if (AutomationConfig.STUB_ANSWER_VALUE.equalsIgnoreCase(partOneAnswer)) {
            System.out.println("\n[Stage 6] Part 1 solve method is still a stub.");
            System.out.println("  Implement solvePartOne in:");
            System.out.println("    src/main/java/odogwudozilla/year" + info.getYear()
                    + "/day" + info.getDay() + "/");
            System.out.println("  Then re-run:  ./gradlew autoSolve --args=\"--auto\"");
            System.out.println("  (The pending puzzle Year " + info.getYear()
                    + " Day " + info.getDay() + " has been saved and will be resumed.)");
            savePendingState(info);
            return info;
        }

        System.out.println("  Part 1 answer: " + partOneAnswer);

        // Stage 7-8: submit and verify Part 1
        System.out.println("\n[Stage 7] Submitting Part 1 (rate limit enforced)...");
        SubmissionResult part1Result = submitWithRetry(submitter, info, partOneAnswer, AnswerSubmitter.PART_ONE);

        if (!isAccepted(part1Result)) {
            System.out.println("\nPart 1 not accepted (" + part1Result + "). Aborting.");
            return info;
        }
        System.out.println("  Part 1 result: " + part1Result);
        docUpdater.updateSolutionsDatabase(info, partOneAnswer, null);

        // Stage 9: scrape Part 2 if not already present
        if (info.getPartTwoDescription() == null) {
            System.out.println("\n[Stage 9] Scraping Part 2 description...");
            info = new PuzzleScraper(session).scrapePartTwo(info);
            if (info.getPartTwoDescription() != null) {
                skeletonGenerator.generate(info);
            }
        }

        if (info.getPartTwoDescription() == null) {
            System.out.println("  Part 2 not yet available - finishing after Part 1.");
            finaliseDocumentation(info, partOneAnswer, null, docUpdater, git);
            return info;
        }

        // Stage 6b: run solver again for Part 2
        System.out.println("\n[Stage 6b] Running solution for Part 2...");
        output = solver.run(info);

        if (!output.hasPartTwo()) {
            System.out.println("  Part 2 not captured yet - implement solvePartTwo, then re-run with --submit "
                    + info.getYear() + " " + info.getDay());
            return info;
        }

        String partTwoAnswer = output.getPartTwoAnswer();
        assert partTwoAnswer != null : "partTwoAnswer is null after hasPartTwo() check";

        if (AutomationConfig.STUB_ANSWER_VALUE.equalsIgnoreCase(partTwoAnswer)) {
            System.out.println("  Part 2 solve method is still a stub - implement solvePartTwo, then re-run with:");
            System.out.println("    ./gradlew autoSolve --args=\"--submit " + info.getYear() + " " + info.getDay() + "\"");
            return info;
        }

        System.out.println("  Part 2 answer: " + partTwoAnswer);

        // Stage 10: submit and verify Part 2
        System.out.println("\n[Stage 10] Submitting Part 2 (rate limit enforced)...");
        SubmissionResult part2Result = submitWithRetry(submitter, info, partTwoAnswer, AnswerSubmitter.PART_TWO);
        System.out.println("  Part 2 result: " + part2Result);

        if (!isAccepted(part2Result)) {
            // Critical guard - never commit when a submitted answer was not accepted.
            System.out.println("\n[ERROR] Part 2 answer \"" + partTwoAnswer
                    + "\" was NOT accepted by AoC (result: " + part2Result + ").");
            System.out.println("  Commit aborted to prevent recording an incorrect solution.");
            System.out.println("  Check the logs above for the AoC response (e.g. 'too high' / 'too low').");
            System.out.println("  Fix solvePartTwo and re-run: ./gradlew autoSolve --args=\"--submit "
                    + info.getYear() + " " + info.getDay() + " --watch\"");
            return info;
        }

        // Stages 11-12: both parts accepted - document and commit
        finaliseDocumentation(info, partOneAnswer, partTwoAnswer, docUpdater, git);
        return info;
    }

    /**
     * Submits an answer with retry logic, waiting for the rate limit between attempts.
     * In non-interactive situations (e.g., stdin closed) the loop will not block.
     * @param submitter the AnswerSubmitter instance
     * @param info the puzzle info
     * @param answer the answer string to submit
     * @param part the puzzle part (1 or 2)
     * @return the final SubmissionResult after all attempts
     */
    @NotNull
    private SubmissionResult submitWithRetry(@NotNull AnswerSubmitter submitter,
                                             @NotNull PuzzleInfo info,
                                             @NotNull String answer,
                                             int part) {
        SubmissionResult result = SubmissionResult.UNKNOWN;

        for (int attempt = 1; attempt <= MAX_SUBMIT_RETRIES; attempt++) {
            LOGGER.info("submitWithRetry - attempt " + attempt + " of " + MAX_SUBMIT_RETRIES
                    + " for part " + part);
            result = submitter.submit(info, answer, part);

            if (isAccepted(result) || SubmissionResult.ALREADY_SOLVED.equals(result)) {
                return result;
            }

            boolean isLastAttempt = attempt == MAX_SUBMIT_RETRIES;
            if (SubmissionResult.INCORRECT.equals(result) && !isLastAttempt) {
                System.out.println("  Attempt " + attempt + " incorrect. Update the answer and type 'retry', or 'skip' to abort:");
                String input = readLine();
                if ("skip".equalsIgnoreCase(input)) {
                    return result;
                }
            } else {
                break;
            }
        }

        return result;
    }

    /**
     * Updates the solutions database, README files, and commits all changes.
     * @param info PuzzleInfo for the solved puzzle
     * @param partOneAnswer the Part 1 solution
     * @param partTwoAnswer the Part 2 solution, or null if not solved
     * @param docUpdater the documentation updater
     * @param git the git automation service
     * @throws IOException if documentation files cannot be written
     * @throws InterruptedException if the git process is interrupted
     */
    private void finaliseDocumentation(@NotNull PuzzleInfo info,
                                       @NotNull String partOneAnswer,
                                       @Nullable String partTwoAnswer,
                                       @NotNull DocumentationUpdater docUpdater,
                                       @NotNull GitAutomationService git) throws IOException, InterruptedException {
        System.out.println("\n[Stage 11] Updating documentation...");
        docUpdater.updateSolutionsDatabase(info, partOneAnswer, partTwoAnswer);
        docUpdater.updateYearReadme(info);
        boolean mainReadmeUpdated = docUpdater.updateMainReadmeIfNewYear(info);

        System.out.println("[Stage 12] Committing changes (only files created/updated in this run)...");
        List<String> affectedFiles = collectAffectedFiles(info, mainReadmeUpdated);
        git.stageFiles(affectedFiles);
        String commitMessage = git.buildCommitMessage(info, "automated solution");
        git.commit(commitMessage);

        clearPendingState();
        System.out.println("\nDone! Committed: " + commitMessage);
    }

    /**
     * Attempts to read the puzzle title from the existing description file so that
     * --submit mode can populate PuzzleInfo without re-scraping.
     * @param info minimal PuzzleInfo with year/day/url already set
     * @return enriched PuzzleInfo with title set if the file was found and parseable
     */
    @NotNull
    private PuzzleInfo enrichFromDescriptionFile(@NotNull PuzzleInfo info) {
        Path descFile = Paths.get("src/main/resources")
                .resolve(String.valueOf(info.getYear()))
                .resolve("day" + info.getDay())
                .resolve("day" + info.getDay() + "_puzzle_description.txt")
                .toAbsolutePath();

        if (!Files.exists(descFile)) {
            LOGGER.info("enrichFromDescriptionFile - description file not found, using minimal info");
            return info;
        }

        try {
            String firstLine = Files.readAllLines(descFile).stream()
                    .filter(l -> !l.isBlank())
                    .findFirst()
                    .orElse("");
            // First line format: "Advent of Code YYYY - Day D: Title"
            int colonIndex = firstLine.indexOf(':');
            if (colonIndex != -1 && colonIndex < firstLine.length() - 1) {
                String title = firstLine.substring(colonIndex + 1).trim();
                LOGGER.info("enrichFromDescriptionFile - resolved title: " + title);
                return info.withPartOne(title, "");
            }
        } catch (IOException e) {
            LOGGER.warning("enrichFromDescriptionFile - could not read file: " + e.getMessage());
        }

        return info;
    }

    /**
     * Prints the implementation prompt shown at Stage 5.
     * @param info PuzzleInfo for the generated puzzle
     */
    private void printImplementationPrompt(@NotNull PuzzleInfo info) {
        System.out.println("\n[Stage 5] PAUSED - implement the solve methods in:");
        System.out.println("  src/main/java/odogwudozilla/year" + info.getYear()
                + "/day" + info.getDay() + "/");
        System.out.println("\nDescription file (read this to understand the puzzle):");
        System.out.println("  src/main/resources/" + info.getYear()
                + "/day" + info.getDay() + "/day" + info.getDay() + "_puzzle_description.txt");
        System.out.println("\nWhen ready, type 'continue' and press Enter:");
    }

    /**
     * Returns true for results that count as a successful submission.
     * @param result the SubmissionResult to check
     * @return true if CORRECT or ALREADY_SOLVED
     */
    private boolean isAccepted(@NotNull SubmissionResult result) {
        return SubmissionResult.CORRECT.equals(result) || SubmissionResult.ALREADY_SOLVED.equals(result);
    }

    /**
     * Blocks until the user types "continue" (case-insensitive) on stdin.
     */
    private void awaitUserConfirmation() {
        System.out.println("  (Type 'continue' and press Enter when ready)");
        String input;
        do {
            input = readLine();
        } while (!"continue".equalsIgnoreCase(input));
    }

    /**
     * Reads a single trimmed line from the shared stdin reader.
     * @return the trimmed input line, or an empty string on error or EOF
     */
    @NotNull
    private String readLine() {
        try {
            String line = stdinReader.readLine();
            return line != null ? line.trim() : "";
        } catch (IOException e) {
            LOGGER.warning("readLine - failed to read from stdin: " + e.getMessage());
            return "";
        }
    }

    // -------------------------------------------------------------------------
    // Browser session factory
    // -------------------------------------------------------------------------

    /**
     * Creates a {@link BrowserSessionManager} configured according to the current
     * watch-mode setting. In watch mode the browser window is visible and each
     * Playwright action is slowed down so a human observer can follow.
     * @return an authenticated browser session ready for use
     */
    @NotNull
    private BrowserSessionManager newBrowserSession() {
        if (watchMode) {
            System.out.println("  [Watch mode] Browser will be visible with "
                    + AutomationConfig.WATCH_SLOW_MO_MILLIS + " ms slow-motion per action");
            return new BrowserSessionManager(false, AutomationConfig.WATCH_SLOW_MO_MILLIS);
        }
        return new BrowserSessionManager();
    }

    // -------------------------------------------------------------------------
    // File tracking - builds the list of files this run created/modified
    // -------------------------------------------------------------------------

    /**
     * Builds the list of file paths that this pipeline run created or modified.
     * Only these files will be staged - no unrelated working-tree changes are touched.
     * @param info PuzzleInfo for the completed puzzle
     * @param includeMainReadme whether the main README was updated in this run
     * @return list of relative file path strings to pass to git add
     */
    @NotNull
    private List<String> collectAffectedFiles(@NotNull PuzzleInfo info, boolean includeMainReadme) {
        List<String> files = new ArrayList<>();

        // Java solution class
        String className = SolutionSkeletonGenerator.resolveClassName(info);
        files.add(AutomationConfig.JAVA_BASE_PATH + "/"
                + AutomationConfig.PACKAGE_BASE.replace('.', '/') + "/year" + info.getYear()
                + "/day" + info.getDay() + "/" + className + ".java");

        // Resource files
        String resourceDir = AutomationConfig.RESOURCES_BASE_PATH + "/" + info.getYear()
                + "/day" + info.getDay();
        files.add(resourceDir + "/day" + info.getDay() + "_puzzle_data.txt");
        files.add(resourceDir + "/day" + info.getDay() + "_puzzle_description.txt");

        // Solutions database and year README
        files.add(AutomationConfig.SOLUTIONS_DB_PATH);
        files.add(AutomationConfig.JAVA_BASE_PATH + "/" + AutomationConfig.PACKAGE_BASE.replace('.', '/')
                + "/year" + info.getYear() + "/README.md");

        if (includeMainReadme) {
            files.add("README.md");
        }

        LOGGER.info("collectAffectedFiles - " + files.size() + " file(s) will be staged for commit");
        return files;
    }

    // -------------------------------------------------------------------------
    // State persistence - saves pending puzzle between runs
    // -------------------------------------------------------------------------

    /**
     * Persists the pending puzzle year and day to the state file so that a
     * subsequent run can resume from the same puzzle rather than selecting a new one.
     * @param info PuzzleInfo for the pending puzzle
     */
    private void savePendingState(@NotNull PuzzleInfo info) {
        try {
            String content = info.getYear() + "," + info.getDay();
            Files.writeString(Paths.get(STATE_FILE), content);
            LOGGER.info("savePendingState - saved pending state: " + content);
        } catch (IOException e) {
            LOGGER.warning("savePendingState - could not save state: " + e.getMessage());
        }
    }

    /**
     * Loads the pending puzzle from the state file if one exists.
     * @return minimal PuzzleInfo for the pending puzzle, or null if no state file exists
     */
    @Nullable
    private PuzzleInfo loadPendingState() {
        Path statePath = Paths.get(STATE_FILE);
        if (!Files.exists(statePath)) {
            return null;
        }
        try {
            String content = Files.readString(statePath).trim();
            String[] parts = content.split(",");
            if (parts.length != 2) {
                LOGGER.warning("loadPendingState - invalid state file content: " + content);
                return null;
            }
            int year = Integer.parseInt(parts[0].trim());
            int day = Integer.parseInt(parts[1].trim());
            LOGGER.info("loadPendingState - loaded pending state: Year " + year + " Day " + day);
            return PuzzleInfo.minimal(year, day);
        } catch (IOException | NumberFormatException e) {
            LOGGER.warning("loadPendingState - could not read state file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Removes the state file after a puzzle has been successfully committed.
     */
    private void clearPendingState() {
        try {
            Files.deleteIfExists(Paths.get(STATE_FILE));
            LOGGER.info("clearPendingState - state file cleared");
        } catch (IOException e) {
            LOGGER.warning("clearPendingState - could not clear state file: " + e.getMessage());
        }
    }
}

