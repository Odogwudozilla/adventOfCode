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
     * Inspects the provided Playwright page (already on the post-submission response page)
     * and returns the submission outcome by matching known AoC response signal strings.
     * @param page a Playwright Page positioned on the AoC submission response
     * @return the determined SubmissionResult
     */
    @NotNull
    public SubmissionResult verify(@NotNull Page page) {
        LOGGER.info("verify - parsing AoC response page for known outcome signal strings");

        String responseText = "";
        try {
            responseText = page.locator(AutomationConfig.RESPONSE_ARTICLE_SELECTOR)
                    .first()
                    .textContent();
        } catch (Exception e) {
            LOGGER.warning("verify - could not read response article element: " + e.getMessage());
        }

        if (responseText == null || responseText.isBlank()) {
            LOGGER.warning("verify - response article was empty; unable to determine outcome");
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
            LOGGER.info("verify - outcome: CORRECT - AoC confirmed the answer is right");
            return SubmissionResult.CORRECT;
        }
        if (responseText.contains(AutomationConfig.ALREADY_COMPLETE_SIGNAL)) {
            LOGGER.info("verify - outcome: ALREADY_SOLVED - AoC reports both parts of this puzzle are already complete");
            return SubmissionResult.ALREADY_SOLVED;
        }
        if (responseText.contains(AutomationConfig.TOO_SOON_SIGNAL)) {
            LOGGER.warning("verify - outcome: TOO_SOON - a submission was made too recently; rate limit was not respected");
            return SubmissionResult.TOO_SOON;
        }
        if (responseText.contains(AutomationConfig.ALREADY_SOLVED_SIGNAL)) {
            LOGGER.info("verify - outcome: ALREADY_SOLVED - this level was previously completed");
            return SubmissionResult.ALREADY_SOLVED;
        }
        if (responseText.contains(AutomationConfig.WRONG_ANSWER_SIGNAL)) {
            // Log the full first sentence of the response so hints like "too high" / "too low" are visible
            String firstSentence = extractFirstSentence(responseText);
            LOGGER.warning("verify - outcome: INCORRECT - AoC rejected the answer. AoC said: \"" + firstSentence + "\"");
            return SubmissionResult.INCORRECT;
        }
        // No known signal matched - log enough of the response for manual diagnosis
        int previewLength = Math.min(500, responseText.length());
        LOGGER.warning("verify - outcome: UNKNOWN - no recognised signal found in AoC response. "
                + "Response preview (" + previewLength + " chars): " + responseText.substring(0, previewLength));
        return SubmissionResult.UNKNOWN;
    }

    /**
     * Extracts the first complete sentence (up to the first full stop followed by a space or end)
     * from the response text, capped at 300 characters so logs remain readable.
     * @param text the full response text
     * @return the first sentence, or a truncated prefix if no sentence boundary is found
     */
    @NotNull
    private String extractFirstSentence(@NotNull String text) {
        int cap = Math.min(300, text.length());
        String truncated = text.substring(0, cap).trim();
        int dot = truncated.indexOf(". ");
        return dot != -1 ? truncated.substring(0, dot + 1) : truncated;
    }
}
