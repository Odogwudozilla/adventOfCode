package odogwudozilla.core;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Collects and analyzes solution statistics across puzzles.
 * Tracks metrics like time taken, code complexity, and success rates.
 */
public class SolutionStatisticsCollector {

    private final Map<String, SolutionStats> statsMap;

    public SolutionStatisticsCollector() {
        this.statsMap = new LinkedHashMap<>();
    }

    /**
     * Records statistics for a solved puzzle part.
     * @param puzzleId the puzzle identifier (e.g., "2025-day1")
     * @param partName the part name (e.g., "Part 1")
     * @param executionTime execution time in milliseconds
     * @param memoryUsage memory usage in bytes
     * @param linesOfCode number of lines of code in solution
     * @param algorithmType the algorithm/approach used
     */
    public void recordStatistics(String puzzleId, String partName, long executionTime,
                                  long memoryUsage, int linesOfCode, String algorithmType) {
        String key = puzzleId + "-" + partName;
        SolutionStats stats = new SolutionStats(puzzleId, partName, executionTime,
                                                 memoryUsage, linesOfCode, algorithmType);
        statsMap.put(key, stats);
    }

    /**
     * Gets statistics for a specific puzzle part.
     * @param puzzleId the puzzle identifier
     * @param partName the part name
     * @return the solution statistics, or null if not found
     */
    public SolutionStats getStatistics(String puzzleId, String partName) {
        return statsMap.get(puzzleId + "-" + partName);
    }

    /**
     * Calculates average execution time across all solutions.
     * @return average execution time in milliseconds
     */
    public double getAverageExecutionTime() {
        if (statsMap.isEmpty()) {
            return 0.0;
        }
        long totalTime = statsMap.values().stream()
                .mapToLong(s -> s.executionTime)
                .sum();
        return (double) totalTime / statsMap.size();
    }

    /**
     * Calculates average memory usage across all solutions.
     * @return average memory usage in bytes
     */
    public double getAverageMemoryUsage() {
        if (statsMap.isEmpty()) {
            return 0.0;
        }
        long totalMemory = statsMap.values().stream()
                .mapToLong(s -> s.memoryUsage)
                .sum();
        return (double) totalMemory / statsMap.size();
    }

    /**
     * Calculates average code size across all solutions.
     * @return average number of lines of code
     */
    public double getAverageCodeSize() {
        if (statsMap.isEmpty()) {
            return 0.0;
        }
        long totalLines = statsMap.values().stream()
                .mapToLong(s -> s.linesOfCode)
                .sum();
        return (double) totalLines / statsMap.size();
    }

    /**
     * Gets the fastest solution.
     * @return the SolutionStats for the fastest solution, or null if no stats
     */
    public SolutionStats getFastestSolution() {
        return statsMap.values().stream()
                .min((s1, s2) -> Long.compare(s1.executionTime, s2.executionTime))
                .orElse(null);
    }

    /**
     * Gets the most memory-efficient solution.
     * @return the SolutionStats for the most efficient solution, or null if no stats
     */
    public SolutionStats getMostEfficientSolution() {
        return statsMap.values().stream()
                .min((s1, s2) -> Long.compare(s1.memoryUsage, s2.memoryUsage))
                .orElse(null);
    }

    /**
     * Gets the most concise solution.
     * @return the SolutionStats for the most concise solution, or null if no stats
     */
    public SolutionStats getMostConciseSolution() {
        return statsMap.values().stream()
                .min((s1, s2) -> Integer.compare(s1.linesOfCode, s2.linesOfCode))
                .orElse(null);
    }

    /**
     * Prints a comprehensive statistics report.
     */
    public void printStatisticsReport() {
        System.out.println("\n" + "=".repeat(59));
        System.out.println("  Solution Statistics Report");
        System.out.println("=".repeat(59));

        System.out.printf("  Total Solutions: %d%n", statsMap.size());
        System.out.printf("  Average Execution Time: %.2f ms%n", getAverageExecutionTime());
        System.out.printf("  Average Memory Usage: %.2f KB%n", getAverageMemoryUsage() / 1024.0);
        System.out.printf("  Average Code Size: %.2f lines%n", getAverageCodeSize());

        System.out.println("\n  Performance Leaders:");

        SolutionStats fastest = getFastestSolution();
        if (fastest != null) {
            System.out.printf("    Fastest: %s (%d ms)%n", fastest.puzzleId + " " + fastest.partName,
                             fastest.executionTime);
        }

        SolutionStats mostEfficient = getMostEfficientSolution();
        if (mostEfficient != null) {
            System.out.printf("    Most Efficient: %s (%.2f KB)%n",
                             mostEfficient.puzzleId + " " + mostEfficient.partName,
                             mostEfficient.memoryUsage / 1024.0);
        }

        SolutionStats mostConcise = getMostConciseSolution();
        if (mostConcise != null) {
            System.out.printf("    Most Concise: %s (%d lines)%n",
                             mostConcise.puzzleId + " " + mostConcise.partName,
                             mostConcise.linesOfCode);
        }

        System.out.println("=".repeat(59) + "\n");
    }

    /**
     * Gets all collected statistics.
     * @return map of all statistics
     */
    public Map<String, SolutionStats> getAllStatistics() {
        return new LinkedHashMap<>(statsMap);
    }

    /**
     * Resets all statistics.
     */
    public void reset() {
        statsMap.clear();
    }

    /**
     * Inner class to represent solution statistics.
     */
    public static class SolutionStats {
        public final String puzzleId;
        public final String partName;
        public final long executionTime;
        public final long memoryUsage;
        public final int linesOfCode;
        public final String algorithmType;

        public SolutionStats(String puzzleId, String partName, long executionTime,
                           long memoryUsage, int linesOfCode, String algorithmType) {
            this.puzzleId = puzzleId;
            this.partName = partName;
            this.executionTime = executionTime;
            this.memoryUsage = memoryUsage;
            this.linesOfCode = linesOfCode;
            this.algorithmType = algorithmType;
        }
    }
}

