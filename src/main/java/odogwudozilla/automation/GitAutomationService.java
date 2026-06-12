package odogwudozilla.automation;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provides Git automation operations for staging and committing puzzle solution files.
 * Only the files explicitly passed to {@link #stageFiles} are staged - this prevents
 * unrelated working-tree changes from being included in the automated commit.
 */
public final class GitAutomationService {

    private static final Logger LOGGER = Logger.getLogger(GitAutomationService.class.getName());

    private static final String GIT_COMMAND = "git";
    private static final String ADD_ARG = "add";
    private static final String COMMIT_ARG = "commit";
    private static final String MESSAGE_FLAG = "-m";

    /**
     * Stages a specific list of files ({@code git add <file1> <file2> ...}).
     * Only the provided paths are staged; nothing else in the working tree is touched.
     * @param filePaths relative or absolute paths to the files to stage
     * @throws IOException if the Git process cannot be started
     * @throws InterruptedException if the process is interrupted
     */
    public void stageFiles(@NotNull List<String> filePaths) throws IOException, InterruptedException {
        if (filePaths.isEmpty()) {
            LOGGER.warning("stageFiles - no files provided; nothing to stage");
            return;
        }
        LOGGER.info("stageFiles - staging " + filePaths.size() + " file(s):");
        filePaths.forEach(f -> LOGGER.info("stageFiles -   " + f));

        List<String> command = new ArrayList<>();
        command.add(GIT_COMMAND);
        command.add(ADD_ARG);
        command.addAll(filePaths);
        runGitCommand(command);
    }

    /**
     * Commits staged changes with the provided message ({@code git commit -m "<message>"}).
     * @param message the commit message
     * @throws IOException if the Git process cannot be started
     * @throws InterruptedException if the process is interrupted
     */
    public void commit(@NotNull String message) throws IOException, InterruptedException {
        LOGGER.info("commit - creating commit: \"" + message + "\"");
        runGitCommand(List.of(GIT_COMMAND, COMMIT_ARG, MESSAGE_FLAG, message));
    }

    /**
     * Builds a descriptive commit message following the project convention.
     * Format: {@code Add AOC <Year> Day <Day> solution: <Title> - <approach>}
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

    /**
     * Runs a Git command, logging every output line and failing fast on non-zero exit.
     * The "nothing to commit" exit code is treated as a success so that a re-run after
     * a partial failure (docs written but commit not yet made) does not crash the pipeline.
     * @param command the full command token list
     * @throws IOException if the process cannot be started
     * @throws InterruptedException if the process is interrupted
     * @throws IllegalStateException if Git exits with a non-zero code for any reason
     *                               other than "nothing to commit"
     */
    private void runGitCommand(@NotNull List<String> command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(Paths.get("").toAbsolutePath().toFile());
        builder.redirectErrorStream(true);

        Process process = builder.start();

        StringBuilder outputCapture = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LOGGER.info("git - " + line);
                outputCapture.append(line).append('\n');
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            String output = outputCapture.toString();
            // "nothing to commit" is not an error - the files were already committed on a
            // previous run that cleared the state file but failed before printing "Done!".
            if (output.contains("nothing to commit") || output.contains("nothing added to commit")) {
                LOGGER.info("commit - nothing to commit (files already committed); treating as success");
                return;
            }
            throw new IllegalStateException("Git command failed with exit code " + exitCode
                    + ": " + String.join(" ", command));
        }
    }
}
