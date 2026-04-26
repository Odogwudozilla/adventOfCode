package odogwudozilla.automation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Compiles and runs the puzzle solution class for a given year and day using Gradle,
 * then parses the captured stdout to extract Part 1 and Part 2 answers.
 *
 * Answers are expected to appear on stdout as lines starting with "Part 1:" or "Part 2:".
 */
public final class SolverRunner {

    private static final Logger LOGGER = Logger.getLogger(SolverRunner.class.getName());

    private static final String OS_NAME = System.getProperty("os.name", "").toLowerCase();

    /**
     * Builds the project and runs the puzzle for the given year and day.
     * Captures stdout and returns a {@link SolveOutput} with the extracted answers.
     * @param info PuzzleInfo containing year and day
     * @return SolveOutput with Part 1 and Part 2 answers (null if not found in output)
     * @throws IOException if the Gradle process cannot be started
     * @throws InterruptedException if the process is interrupted
     */
    @NotNull
    public SolveOutput run(@NotNull PuzzleInfo info) throws IOException, InterruptedException {
        String gradleWrapper = OS_NAME.contains("win")
                ? AutomationConfig.GRADLE_WRAPPER_WINDOWS
                : AutomationConfig.GRADLE_WRAPPER_UNIX;

        String puzzleArgs = info.getYear() + " day" + info.getDay();
        LOGGER.info("run - compiling and executing solution for " + info.getYear() + " Day " + info.getDay()
                + " (\"" + info.getTitle() + "\") via Gradle: " + gradleWrapper + " run --args=" + puzzleArgs);

        ProcessBuilder builder = new ProcessBuilder(gradleWrapper, "run", "--args=" + puzzleArgs);
        builder.directory(Paths.get("").toAbsolutePath().toFile());
        builder.redirectErrorStream(true);

        Process process = builder.start();

        List<String> outputLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                outputLines.add(line);
                LOGGER.info("run [gradle output] - " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            LOGGER.warning("run - Gradle process exited with non-zero code " + exitCode
                    + "; solution may have failed to execute");
        }

        return parseAnswers(outputLines, info);
    }

    /**
     * Parses output lines for "Part 1:" and "Part 2:" answer prefixes.
     * @param lines list of stdout lines from the puzzle run
     * @param info PuzzleInfo used for contextual logging
     * @return SolveOutput containing extracted Part 1 and Part 2 answers
     */
    @NotNull
    private SolveOutput parseAnswers(@NotNull List<String> lines, @NotNull PuzzleInfo info) {
        String partOne = null;
        String partTwo = null;

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith(AutomationConfig.PART_ONE_OUTPUT_PREFIX)) {
                partOne = trimmed.substring(AutomationConfig.PART_ONE_OUTPUT_PREFIX.length()).trim();
            } else if (trimmed.startsWith(AutomationConfig.PART_TWO_OUTPUT_PREFIX)) {
                partTwo = trimmed.substring(AutomationConfig.PART_TWO_OUTPUT_PREFIX.length()).trim();
            }
        }

        LOGGER.info("run - solution output for " + info.getYear() + " Day " + info.getDay()
                + " -> Part 1: \"" + partOne + "\" | Part 2: \"" + partTwo + "\"");
        return new SolveOutput(partOne, partTwo);
    }

    /**
     * Holds the captured Part 1 and Part 2 answers from a puzzle run.
     */
    public static final class SolveOutput {

        @Nullable
        private final String partOneAnswer;

        @Nullable
        private final String partTwoAnswer;

        /**
         * Constructs a SolveOutput.
         * @param partOneAnswer the parsed Part 1 answer, or null if not found
         * @param partTwoAnswer the parsed Part 2 answer, or null if not found
         */
        public SolveOutput(@Nullable String partOneAnswer, @Nullable String partTwoAnswer) {
            this.partOneAnswer = partOneAnswer;
            this.partTwoAnswer = partTwoAnswer;
        }

        @Nullable
        public String getPartOneAnswer() {
            return partOneAnswer;
        }

        @Nullable
        public String getPartTwoAnswer() {
            return partTwoAnswer;
        }

        /**
         * Returns true if at least Part 1 was captured from the output.
         * @return true if Part 1 answer is non-null and non-blank
         */
        public boolean hasPartOne() {
            return partOneAnswer != null && !partOneAnswer.isBlank();
        }

        /**
         * Returns true if Part 2 was captured from the output.
         * @return true if Part 2 answer is non-null and non-blank
         */
        public boolean hasPartTwo() {
            return partTwoAnswer != null && !partTwoAnswer.isBlank();
        }
    }
}

