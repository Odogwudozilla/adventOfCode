package odogwudozilla.automation;

import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Submits an answer to an Advent of Code puzzle via browser form automation.
 *
 * Enforces the rate-limit delay before each submission using the shared
 * {@link RateLimiter}. After posting the form, the updated page is passed to
 * the caller for result verification.
 */
public final class AnswerSubmitter {

    private static final Logger LOGGER = Logger.getLogger(AnswerSubmitter.class.getName());

    /** Level indicator for Part 1 submissions. */
    public static final int PART_ONE = 1;

    /** Level indicator for Part 2 submissions. */
    public static final int PART_TWO = 2;

    private final BrowserSessionManager sessionManager;
    private final RateLimiter rateLimiter;
    private final AcceptanceVerifier verifier;

    /**
     * Creates an AnswerSubmitter.
     * @param sessionManager the authenticated browser session manager
     * @param rateLimiter the shared rate limiter for enforcing submission delays
     * @param verifier the verifier used to parse the submission response
     */
    public AnswerSubmitter(@NotNull BrowserSessionManager sessionManager,
                           @NotNull RateLimiter rateLimiter,
                           @NotNull AcceptanceVerifier verifier) {
        this.sessionManager = sessionManager;
        this.rateLimiter = rateLimiter;
        this.verifier = verifier;
    }

    /**
     * Submits an answer for the specified puzzle part, awaiting the rate-limit delay
     * if necessary, and returns the verified result.
     * @param info PuzzleInfo for the target puzzle
     * @param answer the answer string to submit
     * @param part the puzzle part to submit to - use {@link #PART_ONE} or {@link #PART_TWO}
     * @return the SubmissionResult parsed from the AoC response page
     */
    @NotNull
    public SubmissionResult submit(@NotNull PuzzleInfo info, @NotNull String answer, int part) {
        LOGGER.info("submit - preparing to submit Part " + part + " answer \"" + answer
                + "\" for " + info.getYear() + " Day " + info.getDay()
                + " (\"" + info.getTitle() + "\")");

        // Before Part 1: check the main puzzle page to see if both parts are already complete.
        // This avoids wasting a submission attempt and triggering a rate-limit delay.
        if (part == PART_ONE && isPuzzleAlreadyComplete(info)) {
            LOGGER.info("submit - puzzle " + info.getYear() + " Day " + info.getDay()
                    + " is already marked complete on AoC; skipping submission for Part " + part);
            return SubmissionResult.ALREADY_SOLVED;
        }

        rateLimiter.waitIfNeeded();

        String submitUrl = info.getPuzzleUrl() + "/answer";
        LOGGER.info("submit - posting answer to: " + submitUrl);

        try (Page page = sessionManager.newPage()) {
            page.navigate(submitUrl);
            page.waitForSelector(AutomationConfig.ANSWER_INPUT_SELECTOR);

            page.fill(AutomationConfig.ANSWER_INPUT_SELECTOR, answer);

            // AoC uses a hidden 'level' field that is already pre-set by the server.
            // Do not attempt to interact with it - it is not a radio button or checkbox.

            page.locator(AutomationConfig.ANSWER_FORM_SELECTOR + " input[type='submit']").click();
            page.waitForLoadState();

            LOGGER.info("submit - answer posted; reading AoC response page to determine whether it was accepted");
            return verifier.verify(page);
        }
    }

    /**
     * Navigates to the main puzzle page and checks whether AoC reports that both parts
     * are already complete. Used as a pre-submission guard before Part 1 to avoid
     * burning a submission attempt on a puzzle that is fully solved.
     * @param info PuzzleInfo for the target puzzle
     * @return true if the "Both parts complete" signal is found on the puzzle page
     */
    private boolean isPuzzleAlreadyComplete(@NotNull PuzzleInfo info) {
        try (Page page = sessionManager.newPage()) {
            page.navigate(info.getPuzzleUrl());
            String pageContent = page.content();
            boolean complete = pageContent.contains(AutomationConfig.ALREADY_COMPLETE_SIGNAL);
            if (complete) {
                LOGGER.info("isPuzzleAlreadyComplete - found completion marker on puzzle page for "
                        + info.getYear() + " Day " + info.getDay());
            }
            return complete;
        } catch (Exception e) {
            LOGGER.warning("isPuzzleAlreadyComplete - check failed, proceeding with submission: " + e.getMessage());
            return false;
        }
    }
}

