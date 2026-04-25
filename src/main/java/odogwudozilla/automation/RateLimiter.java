package odogwudozilla.automation;

import java.util.logging.Logger;

/**
 * Enforces a minimum delay between rate-limited operations (e.g., AoC answer submissions).
 * Tracks the timestamp of the last recorded request and blocks until the minimum interval
 * has elapsed before allowing the next one.
 */
public final class RateLimiter {

    private static final Logger LOGGER = Logger.getLogger(RateLimiter.class.getName());

    private long lastRequestTimestampMillis;

    /**
     * Creates a new RateLimiter with no prior request history.
     */
    public RateLimiter() {
        this.lastRequestTimestampMillis = 0L;
    }

    /**
     * Blocks the current thread until the minimum rate-limit interval has passed since
     * the last recorded request. Records the current time as the new last-request timestamp.
     */
    public void waitIfNeeded() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRequestTimestampMillis;

        if (elapsed < AutomationConfig.RATE_LIMIT_MILLIS) {
            long remaining = AutomationConfig.RATE_LIMIT_MILLIS - elapsed;
            long remainingSeconds = remaining / 1000;
            LOGGER.info("waitIfNeeded - rate limit active; waiting " + remainingSeconds + " seconds");
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warning("waitIfNeeded - rate limit sleep interrupted");
            }
        }

        lastRequestTimestampMillis = System.currentTimeMillis();
    }

    /**
     * Records the current timestamp as a request, without blocking.
     * Use this to mark a request that has already been made outside this class.
     */
    public void recordRequest() {
        lastRequestTimestampMillis = System.currentTimeMillis();
    }
}

