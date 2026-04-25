package odogwudozilla.automation;

import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Verifies the result of an answer submission by inspecting the AoC response page.
 *
 * AoC returns a webpage after submission. This class reads the article text and
 * maps known signal strings to a {@link SubmissionResult}.
 */
public final class AcceptanceVerifier {

    private static final Logger LOGGER = Logger.getLogger(AcceptanceVerifier.class.getName());

    /**
     * Inspects the current state of the provided Playwright page (which should already
     * be on the post-submission response page) and returns the submission outcome.
     * @param page a Playwright Page positioned on the AoC submission response
     * @return the determined SubmissionResult
     */
    @NotNull
    public SubmissionResult verify(@NotNull Page page) {
        LOGGER.info("verify - reading submission response page");

        String responseText = "";
        try {
            responseText = page.locator(AutomationConfig.RESPONSE_ARTICLE_SELECTOR)
                    .first()
                    .textContent();
        } catch (Exception e) {
            LOGGER.warning("verify - could not read response article: " + e.getMessage());
        }

        if (responseText == null || responseText.isBlank()) {
            LOGGER.warning("verify - response text is empty, returning UNKNOWN");
            return SubmissionResult.UNKNOWN;
        }

        return classify(responseText);
    }

    /**
     * Classifies a response text string into a {@link SubmissionResult}.
     * @param responseText the text content of the AoC response article
     * @return the matching SubmissionResult
     */
    @NotNull
    private SubmissionResult classify(@NotNull String responseText) {
        if (responseText.contains(AutomationConfig.CORRECT_ANSWER_SIGNAL)) {
            LOGGER.info("verify - result: CORRECT");
            return SubmissionResult.CORRECT;
        }
        if (responseText.contains(AutomationConfig.TOO_SOON_SIGNAL)) {
            LOGGER.info("verify - result: TOO_SOON");
            return SubmissionResult.TOO_SOON;
        }
        if (responseText.contains(AutomationConfig.ALREADY_SOLVED_SIGNAL)) {
            LOGGER.info("verify - result: ALREADY_SOLVED");
            return SubmissionResult.ALREADY_SOLVED;
        }
        if (responseText.contains(AutomationConfig.WRONG_ANSWER_SIGNAL)) {
            LOGGER.info("verify - result: INCORRECT");
            return SubmissionResult.INCORRECT;
        }
        LOGGER.warning("verify - result: UNKNOWN (no signal matched). Response preview: "
                + responseText.substring(0, Math.min(200, responseText.length())));
        return SubmissionResult.UNKNOWN;
    }
}

