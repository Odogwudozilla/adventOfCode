package odogwudozilla.examples;

import odogwudozilla.core.*;
import odogwudozilla.dashboard.WebDashboardGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example usage of all enhancement features implemented in the Advent of Code project.
 * This class demonstrates how to integrate and use the new testing, monitoring, and
 * statistics features in puzzle solution code.
 */
public class EnhancementFeaturesExample {
    private static final Logger LOGGER = Logger.getLogger(EnhancementFeaturesExample.class.getName());
    private static final String SOLUTIONS_DB_RESOURCE = "solutions_database.json";

    private static class PuzzleRef {
        final String year;
        final String day;
        final String partOne;
        final String partTwo;
        PuzzleRef(String year, String day, String partOne, String partTwo) {
            this.year = year;
            this.day = day;
            this.partOne = partOne;
            this.partTwo = partTwo;
        }
    }

    private static class PuzzleMetrics {
        int execTimePart1;
        int execTimePart2;
        long memUsagePart1;
        long memUsagePart2;
        int codeSize;
        boolean realExecTime;
        boolean realMemUsage;
        boolean realCodeSize;
    }

    private static PuzzleMetrics measureRealMetrics(PuzzleRef puzzle) {
        PuzzleMetrics metrics = new PuzzleMetrics();
        metrics.realExecTime = false;
        metrics.realMemUsage = false;
        metrics.realCodeSize = false;
        try {
            // Find solution class file path
            String dayNum = puzzle.day.replace("day", "");
            String expectedPrefix = puzzle.year + "/day" + dayNum + "/";
            String expectedSuffix = "AOC" + puzzle.year + "Day" + dayNum + ".java";
            java.nio.file.Path srcRoot = java.nio.file.Paths.get("src/main/java/odogwudozilla/");
            try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.walk(srcRoot)) {
                stream.filter(p -> p.toString().endsWith(expectedSuffix) && p.toString().contains(expectedPrefix))
                    .findFirst()
                    .ifPresent(p -> {
                        try (java.util.stream.Stream<String> linesStream = java.nio.file.Files.lines(p)) {
                            metrics.codeSize = (int) linesStream.count();
                            metrics.realCodeSize = true;
                        } catch (Exception e) { metrics.codeSize = 0; }
                    });
            }
            // Dynamic solution logic for performance and memory
            String year = puzzle.year;
            String className;
            String title = null;
            // Try to get the class name and title from the solutions database
            try {
                ObjectMapper mapper = new ObjectMapper();
                InputStream inputStream = EnhancementFeaturesExample.class.getClassLoader().getResourceAsStream(SOLUTIONS_DB_RESOURCE);
                JsonNode rootNode = mapper.readTree(inputStream);
                JsonNode solutionsNode = rootNode.get("adventOfCodeSolutions");
                JsonNode yearNode = solutionsNode.get(year);
                if (yearNode != null) {
                    for (JsonNode puzzleNode : yearNode) {
                        if (puzzleNode.get("day").asInt() == Integer.parseInt(dayNum)) {
                            title = puzzleNode.get("title").asText();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // fallback: title unknown
            }
            if (title != null && !title.isEmpty()) {
                String classTitle = title.replaceAll("[^A-Za-z0-9]", "");
                className = "odogwudozilla.year" + year + ".day" + dayNum + "." + classTitle + "AOC" + year + "Day" + dayNum;
            } else {
                className = "odogwudozilla.year" + year + ".day" + dayNum + ".AOC" + year + "Day" + dayNum;
            }
            String inputPath = "src/main/resources/" + year + "/day" + dayNum + "/day" + dayNum + "_puzzle_data.txt";
            Object inputArg = null;
            Class<?> paramType = null;
            try {
                inputArg = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(inputPath));
                paramType = java.util.List.class;
            } catch (Exception e) {
                try {
                    inputArg = java.nio.file.Files.readString(java.nio.file.Paths.get(inputPath)).trim();
                    paramType = String.class;
                } catch (Exception ex) {
                    // fallback: no input
                }
            }
            // Only use standardised method names
            try {
                Class<?> clazz = Class.forName(className);
                java.lang.reflect.Method partOneMethod = clazz.getDeclaredMethod("solvePartOne", paramType);
                partOneMethod.setAccessible(true);
                long memBefore1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                long start1 = System.currentTimeMillis();
                partOneMethod.invoke(null, inputArg);
                long end1 = System.currentTimeMillis();
                long memAfter1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                metrics.execTimePart1 = (int) (end1 - start1);
                metrics.memUsagePart1 = Math.max(0, memAfter1 - memBefore1);
                metrics.realExecTime = true;
                metrics.realMemUsage = true;
                java.lang.reflect.Method partTwoMethod = clazz.getDeclaredMethod("solvePartTwo", paramType);
                partTwoMethod.setAccessible(true);
                long memBefore2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                long start2 = System.currentTimeMillis();
                partTwoMethod.invoke(null, inputArg);
                long end2 = System.currentTimeMillis();
                long memAfter2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                metrics.execTimePart2 = (int) (end2 - start2);
                metrics.memUsagePart2 = Math.max(0, memAfter2 - memBefore2);
            } catch (Exception e) {
                // fallback: synthetic metrics
                metrics.execTimePart1 = 45;
                metrics.execTimePart2 = 52;
                metrics.memUsagePart1 = 51200;
                metrics.memUsagePart2 = 58400;
            }
        } catch (Exception e) {
            // Fallback to synthetic values
            metrics.execTimePart1 = 45;
            metrics.execTimePart2 = 52;
            metrics.memUsagePart1 = 51200;
            metrics.memUsagePart2 = 58400;
            metrics.codeSize = 78;
        }
        return metrics;
    }

    private static PuzzleRef pickRandomSolvedPuzzle() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = EnhancementFeaturesExample.class.getClassLoader().getResourceAsStream(SOLUTIONS_DB_RESOURCE);
            if (inputStream == null) {
                throw new RuntimeException("pickRandomSolvedPuzzle - Could not find solutions_database.json on the classpath.");
            }
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode solutionsNode = rootNode.get("adventOfCodeSolutions");
            java.util.List<PuzzleRef> solvedPuzzles = new java.util.ArrayList<>();
            Iterator<Map.Entry<String, JsonNode>> yearIterator = solutionsNode.fields();
            while (yearIterator.hasNext()) {
                Map.Entry<String, JsonNode> yearEntry = yearIterator.next();
                String year = yearEntry.getKey();
                JsonNode puzzlesArray = yearEntry.getValue();
                for (JsonNode puzzleNode : puzzlesArray) {
                    int day = puzzleNode.get("day").asInt();
                    JsonNode solutionsObj = puzzleNode.get("solutions");
                    String partOne = solutionsObj.has("partOne") && solutionsObj.get("partOne") != null ? solutionsObj.get("partOne").asText() : "";
                    String partTwo = solutionsObj.has("partTwo") && solutionsObj.get("partTwo") != null ? solutionsObj.get("partTwo").asText() : "";
                    if (partOne != null && !partOne.isEmpty() && partTwo != null && !partTwo.isEmpty()) {
                        solvedPuzzles.add(new PuzzleRef(year, "day" + day, partOne, partTwo));
                    }
                }
            }
            if (solvedPuzzles.isEmpty()) {
                throw new RuntimeException("No solved puzzles with both partOne and partTwo found in solutions_database.json.");
            }
            int idx = new java.util.Random(System.currentTimeMillis()).nextInt(solvedPuzzles.size());
            return solvedPuzzles.get(idx);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "pickRandomSolvedPuzzle - Error selecting random puzzle: " + e.getMessage(), e);
            throw new RuntimeException("pickRandomSolvedPuzzle - Error selecting random puzzle: " + e.getMessage(), e);
        }
    }

    /**
     * Demonstrates the usage of all enhancement features.
     * This is a reference example for integrating these features into actual puzzle solutions.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(59));
        System.out.println("  Advent of Code Enhancement Features - Example Usage");
        System.out.println("=".repeat(59));

        PuzzleRef puzzle = pickRandomSolvedPuzzle();
        System.out.println("Selected puzzle: Year " + puzzle.year + ", " + puzzle.day);
        PuzzleMetrics metrics = measureRealMetrics(puzzle);
        demonstratePerformanceMonitoring(puzzle, metrics);
        demonstrateTestHarness(puzzle);
        demonstratePuzzleCaching(puzzle);
        demonstrateDifficultyRating(puzzle, metrics);
        demonstrateSolutionStatistics(puzzle, metrics);
        demonstrateWebDashboard();
        demonstrateIntegrationPattern(puzzle);
    }

    /**
     * Demonstrates the PuzzlePerformanceMonitor feature.
     * Shows how to track execution time and memory usage for puzzle parts.
     */
    @SuppressWarnings("unused")
    private static void demonstratePerformanceMonitoring(PuzzleRef puzzle, PuzzleMetrics metrics) {
        System.out.println("\n1. Performance Monitoring Example");
        System.out.println("-".repeat(32));
        System.out.println("Performance Report: Advent of Code " + puzzle.year + " " + puzzle.day);
        System.out.println("  Part 1 Execution Time: " + metrics.execTimePart1 + " ms");
        System.out.println("  Part 1 Memory Used: " + metrics.memUsagePart1 + " bytes (" + String.format("%.2f", metrics.memUsagePart1 / 1024.0) + " KB)");
        System.out.println("  Part 2 Execution Time: " + metrics.execTimePart2 + " ms");
        System.out.println("  Part 2 Memory Used: " + metrics.memUsagePart2 + " bytes (" + String.format("%.2f", metrics.memUsagePart2 / 1024.0) + " KB)");
        System.out.println("  Total Execution Time: " + (metrics.execTimePart1 + metrics.execTimePart2) + " ms");
        if (!metrics.realExecTime || !metrics.realMemUsage) {
            System.out.println("  Note: Synthetic values used for performance or memory.");
        }
        System.out.println("=".repeat(59));
    }

    /**
     * Demonstrates the PuzzleTestHarness feature.
     * Shows how to validate puzzle solutions against expected results.
     */
    @SuppressWarnings("unused")
    private static void demonstrateTestHarness(PuzzleRef puzzle) {
        System.out.println("\n2. Test Harness Example");
        System.out.println("-".repeat(24));
        PuzzleTestHarness harness = new PuzzleTestHarness(puzzle.year, puzzle.day);
        harness.setExpectedResult("Part 1", puzzle.partOne);
        harness.setExpectedResult("Part 2", puzzle.partTwo);
        harness.recordActualResult("Part 1", puzzle.partOne);
        harness.recordActualResult("Part 2", puzzle.partTwo);
        harness.printTestReport();
    }

    /**
     * Demonstrates the PuzzleCacheManager feature.
     * Shows how to cache and retrieve puzzle results.
     */
    @SuppressWarnings("unused")
    private static void demonstratePuzzleCaching(PuzzleRef puzzle) {
        System.out.println("\n3. Puzzle Caching Example");
        System.out.println("-".repeat(25));
        PuzzleCacheManager cache = new PuzzleCacheManager();
        System.out.println("Caching puzzle results...");
        cache.cachePuzzleResult(puzzle.year, puzzle.day, 1, puzzle.partOne, 45);
        cache.cachePuzzleResult(puzzle.year, puzzle.day, 2, puzzle.partTwo, 52);
        if (cache.isCached(puzzle.year, puzzle.day, 1)) {
            String cachedResult = cache.getCachedResult(puzzle.year, puzzle.day, 1);
            System.out.println("Retrieved cached result for " + puzzle.year + " " + puzzle.day + " part 1: " + cachedResult);
        }
        boolean matches = cache.compareWithCache(puzzle.year, puzzle.day, 1, puzzle.partOne);
        System.out.println("Current result matches cache: " + matches);
        System.out.println("Cache operations completed successfully.");
    }

    /**
     * Demonstrates the PuzzleDifficultyRater feature.
     * Shows how to assess puzzle difficulty based on multiple metrics.
     *
     * Synthetic demo values are used for performance, memory, and code size.
     * Real performance data is not available in the cache.
     */
    @SuppressWarnings("unused")
    private static void demonstrateDifficultyRating(PuzzleRef puzzle, PuzzleMetrics metrics) {
        System.out.println("\n4. Difficulty Rating Example");
        System.out.println("-".repeat(28));
        PuzzleDifficultyRater rater = new PuzzleDifficultyRater();
        int difficulty1 = rater.rateDifficulty("Part 1", metrics.execTimePart1, (int)metrics.memUsagePart1, metrics.codeSize);
        int difficulty2 = rater.rateDifficulty("Part 2", metrics.execTimePart2, (int)metrics.memUsagePart2, metrics.codeSize);
        double average = rater.getAverageDifficulty();
        System.out.println("Part 1 Difficulty: " + difficulty1 + "/10 - " + rater.getDifficultyLabel("Part 1"));
        System.out.println("Part 2 Difficulty: " + difficulty2 + "/10 - " + rater.getDifficultyLabel("Part 2"));
        System.out.println("Average Difficulty: " + String.format("%.2f", average) + "/10");
        System.out.println("\n===========================================================");
        System.out.println("  Difficulty Assessment Report");
        System.out.println("===========================================================");
        System.out.println("  Part 1: " + difficulty1 + "/10 - " + rater.getDifficultyLabel("Part 1"));
        System.out.println("  Part 2: " + difficulty2 + "/10 - " + rater.getDifficultyLabel("Part 2"));
        if (!metrics.realExecTime || !metrics.realMemUsage || !metrics.realCodeSize) {
            System.out.println("\n  Note: Synthetic values used for one or more metrics.");
        } else {
            System.out.println("\n  Note: All metrics are real measurements.");
        }
        System.out.println("===========================================================\n");
    }

    /**
     * Demonstrates the SolutionStatisticsCollector feature.
     * Shows how to collect and analyse solution statistics.
     *
     * Synthetic demo statistics are used for illustration.
     * Real statistics are not available in the cache.
     */
    @SuppressWarnings("unused")
    private static void demonstrateSolutionStatistics(PuzzleRef puzzle, PuzzleMetrics metrics) {
        System.out.println("\n5. Solution Statistics Example");
        System.out.println("-".repeat(30));
        SolutionStatisticsCollector stats = new SolutionStatisticsCollector();
        stats.recordStatistics(puzzle.year + "-" + puzzle.day, "Part 1", metrics.execTimePart1, (int)metrics.memUsagePart1, metrics.codeSize, "Algorithm notes unavailable");
        stats.recordStatistics(puzzle.year + "-" + puzzle.day, "Part 2", metrics.execTimePart2, (int)metrics.memUsagePart2, metrics.codeSize, "Algorithm notes unavailable");
        System.out.println("Average Execution Time: " + String.format("%.2f", stats.getAverageExecutionTime()) + " ms");
        System.out.println("Average Memory Usage: " + String.format("%.2f", stats.getAverageMemoryUsage() / 1024.0) + " KB");
        System.out.println("Average Code Size: " + String.format("%.2f", stats.getAverageCodeSize()) + " lines");
        SolutionStatisticsCollector.SolutionStats fastest = stats.getFastestSolution();
        if (fastest != null) {
            System.out.println("Fastest Solution: " + fastest.puzzleId + " " + fastest.partName + " (" + fastest.executionTime + " ms)");
        }
        System.out.println("\n===========================================================");
        System.out.println("  Solution Statistics Report");
        System.out.println("===========================================================");
        System.out.println("  Average Execution Time: " + String.format("%.2f", stats.getAverageExecutionTime()) + " ms");
        System.out.println("  Average Memory Usage: " + String.format("%.2f", stats.getAverageMemoryUsage() / 1024.0) + " KB");
        System.out.println("  Average Code Size: " + String.format("%.2f", stats.getAverageCodeSize()) + " lines");
        if (fastest != null) {
            System.out.println("\n  Performance Leaders:");
            System.out.println("    Fastest: " + fastest.puzzleId + " " + fastest.partName + " (" + fastest.executionTime + " ms)");
            System.out.println("    Most Efficient: " + fastest.puzzleId + " " + fastest.partName + " (" + String.format("%.2f", fastest.memoryUsage / 1024.0) + " KB)");
        }
        if (!metrics.realExecTime || !metrics.realMemUsage || !metrics.realCodeSize) {
            System.out.println("\n  Note: Synthetic values used for one or more metrics.");
        } else {
            System.out.println("\n  Note: All metrics are real measurements.");
        }
        System.out.println("===========================================================\n");
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
            InputStream inputStream = EnhancementFeaturesExample.class.getClassLoader().getResourceAsStream(SOLUTIONS_DB_RESOURCE);
            if (inputStream == null) {
                throw new RuntimeException("demonstrateWebDashboard - Could not find solutions_database.json on the classpath.");
            }
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
                    String partOne = solutionsObj.has("partOne") && solutionsObj.get("partOne") != null ? solutionsObj.get("partOne").asText() : "";
                    String partTwo = solutionsObj.has("partTwo") && solutionsObj.get("partTwo") != null ? solutionsObj.get("partTwo").asText() : "";
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
            LOGGER.log(Level.SEVERE, "demonstrateWebDashboard - Error loading solutions database: " + e.getMessage(), e);
        }
    }

    /**
     * Integration pattern: Complete puzzle solution with all features.
     * This shows how to use all features together in a real puzzle solution.
     */
    private static void demonstrateIntegrationPattern(PuzzleRef puzzle) {
        System.out.println("\n7. Integration Pattern Example");
        System.out.println("-".repeat(30));
        System.out.println("This is how to use all features together in a real puzzle solution:\n");
        System.out.println("public static void main(String[] args) {");
        System.out.println("    // Initialise all tools");
        System.out.println("    PuzzlePerformanceMonitor monitor = new PuzzlePerformanceMonitor(\"" + puzzle.year + "\", \"" + puzzle.day + "\");");
        System.out.println("    PuzzleTestHarness harness = new PuzzleTestHarness(\"" + puzzle.year + "\", \"" + puzzle.day + "\");");
        System.out.println("    PuzzleCacheManager cache = new PuzzleCacheManager();");
        System.out.println("    ");
        System.out.println("    try {");
        System.out.println("        // Part 1");
        System.out.println("        monitor.startTiming(\"Part 1\");");
        System.out.println("        String result1 = solvePartOne();");
        System.out.println("        monitor.stopTiming(\"Part 1\");");
        System.out.println("        ");
        System.out.println("        cache.cachePuzzleResult(\"" + puzzle.year + "\", \"" + puzzle.day + "\", 1, result1,");
        System.out.println("                               monitor.getExecutionTime(\"Part 1\"));");
        System.out.println("        harness.recordActualResult(\"Part 1\", result1);");
        System.out.println("        ");
        System.out.println("        // Part 2");
        System.out.println("        monitor.startTiming(\"Part 2\");");
        System.out.println("        String result2 = solvePartTwo();");
        System.out.println("        monitor.stopTiming(\"Part 2\");");
        System.out.println("        ");
        System.out.println("        cache.cachePuzzleResult(\"" + puzzle.year + "\", \"" + puzzle.day + "\", 2, result2,");
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

    /**
     * Uses reflection to invoke a (possibly private) static method on a class.
     * @param className Fully qualified class name
     * @param methodName Method name
     * @param paramTypes Array of parameter types
     * @param args Arguments to pass
     * @return Result of method invocation
     */
    private static Object invokeSolutionMethod(String className, String methodName, Class<?>[] paramTypes, Object[] args) {
        try {
            Class<?> clazz = Class.forName(className);
            java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException("invokeSolutionMethod - Error invoking " + className + "." + methodName + ": " + e.getMessage(), e);
        }
    }
}
