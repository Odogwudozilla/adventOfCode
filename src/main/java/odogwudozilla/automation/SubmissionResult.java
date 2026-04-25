package odogwudozilla.automation;

/**
 * Represents the outcome of an answer submission to Advent of Code.
 */
public enum SubmissionResult {

    /** The answer was accepted as correct. */
    CORRECT,

    /** The answer was rejected as incorrect. */
    INCORRECT,

    /** The submission was rejected because a previous submission was too recent. */
    TOO_SOON,

    /** The level was already completed - no new submission needed. */
    ALREADY_SOLVED,

    /** The result could not be determined from the response page. */
    UNKNOWN
}

