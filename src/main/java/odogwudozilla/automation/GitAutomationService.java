package odogwudozilla.automation;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provides Git automation operations for staging and committing puzzle solution files.
 *
 * Uses {@link ProcessBuilder} to invoke the local Git installation. All commands
 * run from the project root directory.
 */
public final class GitAutomationService {

    private static final Logger LOGGER = Logger.getLogger(GitAutomationService.class.getName());

    private static final String GIT_COMMAND = "git";
    private static final String ADD_ALL_ARG = "add";
    private static final String ALL_FLAG = ".";
    private static final String COMMIT_ARG = "commit";
    private static final String MESSAGE_FLAG = "-m";

    /**
     * Stages all changes in the working directory ({@code git add .}).
     * @throws IOException if the Git process cannot be started
     * @throws InterruptedException if the process is interrupted
     */
    public void stageAll() throws IOException, InterruptedException {
        LOGGER.info("stageAll - staging all changes");
        runGitCommand(List.of(GIT_COMMAND, ADD_ALL_ARG, ALL_FLAG));
    }

    /**
     * Commits staged changes with the provided message ({@code git commit -m "<message>"}).
     * @param message the commit message
     * @throws IOException if the Git process cannot be started
     * @throws InterruptedException if the process is interrupted
     */
    public void commit(@NotNull String message) throws IOException, InterruptedException {
        LOGGER.info("commit - committing with message: " + message);
        runGitCommand(List.of(GIT_COMMAND, COMMIT_ARG, MESSAGE_FLAG, message));
    }

    /**
     * Builds and runs a Git command, logging all output lines.
     * @param command the full command token list
     * @throws IOException if the process cannot be started
     * @throws InterruptedException if the process is interrupted
     * @throws IllegalStateException if the Git command exits with a non-zero code
     */
    private void runGitCommand(@NotNull List<String> command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(Paths.get("").toAbsolutePath().toFile());
        builder.redirectErrorStream(true);

        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LOGGER.info("git - " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Git command failed with exit code " + exitCode
                    + ": " + String.join(" ", command));
        }
    }

    /**
     * Builds a descriptive commit message following the project convention.
     * Format: "Add AOC <Year> Day <Day> solution: <Title> - <approach>"
     * @param info PuzzleInfo with year, day, and title
     * @param approach a brief description of the algorithm or approach used
     * @return the formatted commit message string
     */
    @NotNull
    public String buildCommitMessage(@NotNull PuzzleInfo info, @NotNull String approach) {
        String title = info.getTitle() != null ? info.getTitle() : "Day " + info.getDay();
        return "Add AOC " + info.getYear() + " Day " + info.getDay()
                + " solution: " + title + " - " + approach;
    }
}

