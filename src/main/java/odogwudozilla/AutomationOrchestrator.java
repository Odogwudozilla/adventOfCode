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

    /**
     * Creates an AutomationOrchestrator with a shared stdin reader.
     */
    public AutomationOrchestrator() {
        this.stdinReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Entry point for the automation pipeline.
     * @param args optional run-mode flags: --setup, --auto, or --submit YEAR DAY
     */
    public static void main(String[] args) {
        AutomationOrchestrator orchestrator = new AutomationOrchestrator();
        try {
            orchestrator.dispatch(args);
        } catch (Exception e) {
            LOGGER.severe("main - pipeline failed: " + e.getMessage());
            System.out.println("\nPipeline failed: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Dispatches to the correct pipeline mode based on the provided arguments.
     * @param args command line arguments from main()
     * @throws Exception if any pipeline stage fails unrecoverably
     */
    private void dispatch(String[] args) throws Exception {
        if (args.length > 0 && FLAG_SETUP.equalsIgnoreCase(args[0])) {
            runSetupOnly();
        } else if (args.length > 0 && FLAG_AUTO.equalsIgnoreCase(args[0])) {
            runFull(true);
        } else if (args.length > 0 && FLAG_SUBMIT.equalsIgnoreCase(args[0])) {
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

        try (BrowserSessionManager session = new BrowserSessionManager()) {
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

        try (BrowserSessionManager session = new BrowserSessionManager()) {
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
     * @param args the full args array; expected: flags[0]="--submit", args[1]=YEAR, args[2]=DAY
     * @throws Exception if any stage fails
     */
    private void runSubmitOnly(String[] args) throws Exception {
        if (args.length < 3) {
            throw new IllegalArgumentException(
                    "Usage: --submit YEAR DAY  (e.g., --submit 2025 1)");
        }

        int year = Integer.parseInt(args[1]);
        int day = Integer.parseInt(args[2]);

        System.out.println("\n[Submit mode] Year " + year + " Day " + day);
        PuzzleInfo info = PuzzleInfo.minimal(year, day);
        info = enrichFromDescriptionFile(info);

        RateLimiter rateLimiter = new RateLimiter();
        AcceptanceVerifier verifier = new AcceptanceVerifier();
        DocumentationUpdater docUpdater = new DocumentationUpdater();
        GitAutomationService git = new GitAutomationService();
        SolverRunner solver = new SolverRunner();
        SolutionSkeletonGenerator skeletonGenerator = new SolutionSkeletonGenerator();

        try (BrowserSessionManager session = new BrowserSessionManager()) {
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

        // Stages 11-12: document and commit
        String part2Stored = isAccepted(part2Result) ? partTwoAnswer : null;
        finaliseDocumentation(info, partOneAnswer, part2Stored, docUpdater, git);
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
        docUpdater.updateMainReadmeIfNewYear(info);

        System.out.println("[Stage 12] Committing changes...");
        git.stageAll();
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

