package odogwudozilla.examples;

import odogwudozilla.core.*;
import odogwudozilla.dashboard.WebDashboardGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Example usage of all enhancement features implemented in the Advent of Code project.
 * This class demonstrates how to integrate and use the new testing, monitoring, and
 * statistics features in puzzle solution code.
 */
public class EnhancementFeaturesExample {

    /**
     * Demonstrates the usage of all enhancement features.
     * This is a reference example for integrating these features into actual puzzle solutions.
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(59));
        System.out.println("  Advent of Code Enhancement Features - Example Usage");
        System.out.println("=".repeat(59));

        // Example 1: Performance Monitoring
        demonstratePerformanceMonitoring();

        // Example 2: Test Harness
        demonstrateTestHarness();

        // Example 3: Puzzle Caching
        demonstratePuzzleCaching();

        // Example 4: Difficulty Rating
        demonstrateDifficultyRating();

        // Example 5: Solution Statistics
        demonstrateSolutionStatistics();

        // Example 6: Web Dashboard
        demonstrateWebDashboard();

        // Example 7: Integration Pattern
        demonstrateIntegrationPattern();
    }

    /**
     * Demonstrates the PuzzlePerformanceMonitor feature.
     * Shows how to track execution time and memory usage for puzzle parts.
     */
    private static void demonstratePerformanceMonitoring() {
        System.out.println("\n1. Performance Monitoring Example");
        System.out.println("-".repeat(32));

        PuzzlePerformanceMonitor monitor = new PuzzlePerformanceMonitor("2025", "day1");

        // Simulate Part 1 execution
        monitor.startTiming("Part 1");
        try {
            Thread.sleep(45);  // Simulate computation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        monitor.stopTiming("Part 1");

        // Simulate Part 2 execution
        monitor.startTiming("Part 2");
        try {
            Thread.sleep(52);  // Simulate computation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        monitor.stopTiming("Part 2");

        monitor.printPerformanceReport();
    }

    /**
     * Demonstrates the PuzzleTestHarness feature.
     * Shows how to validate puzzle solutions against expected results.
     */
    private static void demonstrateTestHarness() {
        System.out.println("\n2. Test Harness Example");
        System.out.println("-".repeat(24));

        PuzzleTestHarness harness = new PuzzleTestHarness("2025", "day1");

        // Set expected results
        harness.setExpectedResult("Part 1", "138");
        harness.setExpectedResult("Part 2", "1771");

        // Record actual results
        harness.recordActualResult("Part 1", "138");   // Correct
        harness.recordActualResult("Part 2", "1770");  // Incorrect (for demo)

        harness.printTestReport();
    }

    /**
     * Demonstrates the PuzzleCacheManager feature.
     * Shows how to cache and retrieve puzzle results.
     */
    private static void demonstratePuzzleCaching() {
        System.out.println("\n3. Puzzle Caching Example");
        System.out.println("-".repeat(25));

        PuzzleCacheManager cache = new PuzzleCacheManager();

        // Cache results
        System.out.println("Caching puzzle results...");
        cache.cachePuzzleResult("2025", "day1", 1, "138", 45);
        cache.cachePuzzleResult("2025", "day1", 2, "1771", 52);

        // Check if cached
        if (cache.isCached("2025", "day1", 1)) {
            String cachedResult = cache.getCachedResult("2025", "day1", 1);
            System.out.println("Retrieved cached result for 2025 day1 part 1: " + cachedResult);
        }

        // Compare with current result
        boolean matches = cache.compareWithCache("2025", "day1", 1, "138");
        System.out.println("Current result matches cache: " + matches);

        System.out.println("Cache operations completed successfully.");
    }

    /**
     * Demonstrates the PuzzleDifficultyRater feature.
     * Shows how to assess puzzle difficulty based on multiple metrics.
     */
    private static void demonstrateDifficultyRating() {
        System.out.println("\n4. Difficulty Rating Example");
        System.out.println("-".repeat(28));

        PuzzleDifficultyRater rater = new PuzzleDifficultyRater();

        // Rate Part 1: Fast, efficient, concise code
        int difficulty1 = rater.rateDifficulty("Part 1", 45, 51200, 78);
        System.out.println("Part 1 Difficulty: " + difficulty1 + "/10 - " +
                          rater.getDifficultyLabel("Part 1"));

        // Rate Part 2: Slower, more memory, more code
        int difficulty2 = rater.rateDifficulty("Part 2", 250, 512000, 156);
        System.out.println("Part 2 Difficulty: " + difficulty2 + "/10 - " +
                          rater.getDifficultyLabel("Part 2"));

        // Calculate average
        double average = rater.getAverageDifficulty();
        System.out.println("Average Difficulty: " + String.format("%.2f", average) + "/10");

        rater.printDifficultyReport();
    }

    /**
     * Demonstrates the SolutionStatisticsCollector feature.
     * Shows how to collect and analyze solution statistics.
     */
    private static void demonstrateSolutionStatistics() {
        System.out.println("\n5. Solution Statistics Example");
        System.out.println("-".repeat(30));

        SolutionStatisticsCollector stats = new SolutionStatisticsCollector();

        // Record statistics for multiple puzzles
        stats.recordStatistics("2025-day1", "Part 1", 45, 51200, 78,
                              "Linear scan with counter");
        stats.recordStatistics("2025-day1", "Part 2", 52, 58400, 95,
                              "Hash map aggregation");
        stats.recordStatistics("2025-day2", "Part 1", 38, 42000, 65,
                              "Conditional logic");
        stats.recordStatistics("2025-day2", "Part 2", 60, 75000, 120,
                              "Dynamic programming");

        System.out.println("Average Execution Time: " +
                          String.format("%.2f", stats.getAverageExecutionTime()) + " ms");
        System.out.println("Average Memory Usage: " +
                          String.format("%.2f", stats.getAverageMemoryUsage() / 1024.0) + " KB");
        System.out.println("Average Code Size: " +
                          String.format("%.2f", stats.getAverageCodeSize()) + " lines");

        SolutionStatisticsCollector.SolutionStats fastest = stats.getFastestSolution();
        if (fastest != null) {
            System.out.println("Fastest Solution: " + fastest.puzzleId + " " +
                              fastest.partName + " (" + fastest.executionTime + " ms)");
        }

        stats.printStatisticsReport();
    }

    /**
     * Demonstrates the WebDashboardGenerator feature.
     * Shows how to create an interactive progress dashboard.
     * Loads actual solution data from solutions_database.json.
     */
    private static void demonstrateWebDashboard() {
        System.out.println("\n6. Web Dashboard Example");
        System.out.println("-".repeat(24));

        WebDashboardGenerator dashboard = new WebDashboardGenerator();

        try {
            System.out.println("Loading solutions from database...");

            // Load solutions database
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = EnhancementFeaturesExample.class.getResourceAsStream("/solutions_database.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode solutionsNode = rootNode.get("adventOfCodeSolutions");

            // Process each year
            Iterator<Map.Entry<String, JsonNode>> yearIterator = solutionsNode.fields();
            int totalPuzzles = 0;
            int completedPuzzles = 0;

            while (yearIterator.hasNext()) {
                Map.Entry<String, JsonNode> yearEntry = yearIterator.next();
                String year = yearEntry.getKey();
                JsonNode puzzlesArray = yearEntry.getValue();

                // Process each puzzle in the year
                for (JsonNode puzzleNode : puzzlesArray) {
                    int day = puzzleNode.get("day").asInt();
                    String title = puzzleNode.get("title").asText();
                    JsonNode solutionsObj = puzzleNode.get("solutions");

                    // Check if parts are solved
                    String partOne = solutionsObj.get("partOne").asText();
                    String partTwo = solutionsObj.get("partTwo").asText();
                    boolean hasPartOne = partOne != null && !partOne.isEmpty();
                    boolean hasPartTwo = partTwo != null && !partTwo.isEmpty();

                    // Add to dashboard
                    dashboard.addPuzzleData(year, String.valueOf(day), title, hasPartOne, hasPartTwo, 0);

                    // Track statistics
                    totalPuzzles++;
                    if (hasPartOne && hasPartTwo) {
                        completedPuzzles++;
                    }
                }
            }

            System.out.println("Found " + totalPuzzles + " total puzzles");
            System.out.println("Completed " + completedPuzzles + " puzzles");
            System.out.println("Building dashboard with real solution data...");

            // Generate dashboard
            dashboard.generateDashboard();
            System.out.println("Dashboard generated at: " + dashboard.getDashboardPath());
            System.out.println("Open the HTML file in your browser to view the interactive dashboard.");

        } catch (Exception e) {
            System.err.println("Error loading solutions database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Integration pattern: Complete puzzle solution with all features.
     * This shows how to use all features together in a real puzzle solution.
     */
    private static void demonstrateIntegrationPattern() {
        System.out.println("\n7. Integration Pattern Example");
        System.out.println("-".repeat(30));
        System.out.println("This is how to use all features together in a real puzzle solution:\n");

        System.out.println("public static void main(String[] args) {");
        System.out.println("    // Initialize all tools");
        System.out.println("    PuzzlePerformanceMonitor monitor = new PuzzlePerformanceMonitor(\"2025\", \"day1\");");
        System.out.println("    PuzzleTestHarness harness = new PuzzleTestHarness(\"2025\", \"day1\");");
        System.out.println("    PuzzleCacheManager cache = new PuzzleCacheManager();");
        System.out.println("    ");
        System.out.println("    try {");
        System.out.println("        // Part 1");
        System.out.println("        monitor.startTiming(\"Part 1\");");
        System.out.println("        String result1 = solvePart1();");
        System.out.println("        monitor.stopTiming(\"Part 1\");");
        System.out.println("        ");
        System.out.println("        cache.cachePuzzleResult(\"2025\", \"day1\", 1, result1,");
        System.out.println("                               monitor.getExecutionTime(\"Part 1\"));");
        System.out.println("        harness.recordActualResult(\"Part 1\", result1);");
        System.out.println("        ");
        System.out.println("        // Part 2");
        System.out.println("        monitor.startTiming(\"Part 2\");");
        System.out.println("        String result2 = solvePart2();");
        System.out.println("        monitor.stopTiming(\"Part 2\");");
        System.out.println("        ");
        System.out.println("        cache.cachePuzzleResult(\"2025\", \"day1\", 2, result2,");
        System.out.println("                               monitor.getExecutionTime(\"Part 2\"));");
        System.out.println("        harness.recordActualResult(\"Part 2\", result2);");
        System.out.println("        ");
        System.out.println("        // Display results");
        System.out.println("        monitor.printPerformanceReport();");
        System.out.println("        harness.printTestReport();");
        System.out.println("        ");
        System.out.println("    } catch (IOException e) {");
        System.out.println("        System.err.println(\"Error: \" + e.getMessage());");
        System.out.println("    }");
        System.out.println("}");
    }
}

