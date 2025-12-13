package odogwudozilla.core;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Monitors and records performance metrics for puzzle execution.
 * Tracks execution time, memory usage, and other performance data.
 */
public class PuzzlePerformanceMonitor {

    private final String puzzleIdentifier;
    private final Map<String, Long> executionTimes;
    private final Map<String, Long> memoryUsage;
    private Instant startTime;
    private long startMemory;

    public PuzzlePerformanceMonitor(String year, String day) {
        this.puzzleIdentifier = year + " " + day;
        this.executionTimes = new LinkedHashMap<>();
        this.memoryUsage = new LinkedHashMap<>();
    }

    /**
     * Starts timing for a specific puzzle part.
     * @param partName the name of the part (e.g., "Part 1", "Part 2")
     */
    public void startTiming(String partName) {
        this.startTime = Instant.now();
        this.startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * Ends timing for a specific puzzle part and records the duration.
     * @param partName the name of the part being timed
     */
    public void stopTiming(String partName) {
        if (startTime == null) {
            System.err.println("Warning: stopTiming called without startTiming for " + partName);
            return;
        }

        long duration = Duration.between(startTime, Instant.now()).toMillis();
        executionTimes.put(partName, duration);

        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoryDelta = endMemory - startMemory;
        memoryUsage.put(partName, memoryDelta);

        startTime = null;
    }

    /**
     * Gets the execution time for a specific part.
     * @param partName the name of the part
     * @return execution time in milliseconds
     */
    public long getExecutionTime(String partName) {
        return executionTimes.getOrDefault(partName, -1L);
    }

    /**
     * Gets the memory usage delta for a specific part.
     * @param partName the name of the part
     * @return memory delta in bytes
     */
    public long getMemoryUsage(String partName) {
        return memoryUsage.getOrDefault(partName, -1L);
    }

    /**
     * Gets the total execution time across all parts.
     * @return total time in milliseconds
     */
    public long getTotalExecutionTime() {
        return executionTimes.values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Prints a formatted performance report.
     */
    public void printPerformanceReport() {
        System.out.println("\n" + "=".repeat(59));
        System.out.println("  Performance Report: Advent of Code " + puzzleIdentifier);
        System.out.println("=".repeat(59));

        executionTimes.forEach((part, time) -> {
            System.out.printf("  %s Execution Time: %,d ms%n", part, time);
            long memory = memoryUsage.getOrDefault(part, 0L);
            System.out.printf("  %s Memory Used: %,d bytes (%.2f KB)%n", part, memory, memory / 1024.0);
        });

        System.out.printf("  Total Execution Time: %,d ms%n", getTotalExecutionTime());
        System.out.println("=".repeat(59) + "\n");
    }

    /**
     * Returns a map of all execution times.
     * @return map of part names to execution times in milliseconds
     */
    public Map<String, Long> getExecutionTimesMap() {
        return new LinkedHashMap<>(executionTimes);
    }

    /**
     * Returns a map of all memory usage data.
     * @return map of part names to memory delta in bytes
     */
    public Map<String, Long> getMemoryUsageMap() {
        return new LinkedHashMap<>(memoryUsage);
    }
}

