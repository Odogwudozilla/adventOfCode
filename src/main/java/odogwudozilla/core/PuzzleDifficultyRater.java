package odogwudozilla.core;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rates puzzle difficulty based on various metrics.
 * Difficulty is determined by code complexity, execution time, and memory usage.
 */
public class PuzzleDifficultyRater {

    private static final int TIME_THRESHOLD_EASY = 100;     // milliseconds
    private static final int TIME_THRESHOLD_MEDIUM = 500;
    private static final int TIME_THRESHOLD_HARD = 2000;

    private static final long MEMORY_THRESHOLD_EASY = 1024 * 100;        // 100 KB
    private static final long MEMORY_THRESHOLD_MEDIUM = 1024 * 500;      // 500 KB
    private static final long MEMORY_THRESHOLD_HARD = 1024 * 1024 * 10;  // 10 MB

    private final Map<String, Integer> difficultyScores;
    private final Map<String, String> difficultyRatings;

    public PuzzleDifficultyRater() {
        this.difficultyScores = new LinkedHashMap<>();
        this.difficultyRatings = new LinkedHashMap<>();
    }

    /**
     * Rates the difficulty of a puzzle part based on execution metrics.
     * @param partName the name of the part (e.g., "Part 1", "Part 2")
     * @param executionTime execution time in milliseconds
     * @param memoryUsage memory usage in bytes
     * @param linesOfCode number of lines of code
     * @return difficulty rating (1-10 scale)
     */
    public int rateDifficulty(String partName, long executionTime, long memoryUsage, int linesOfCode) {
        int score = 0;

        // Time-based difficulty
        if (executionTime > TIME_THRESHOLD_HARD) {
            score += 3;
        } else if (executionTime > TIME_THRESHOLD_MEDIUM) {
            score += 2;
        } else if (executionTime > TIME_THRESHOLD_EASY) {
            score += 1;
        }

        // Memory-based difficulty
        if (memoryUsage > MEMORY_THRESHOLD_HARD) {
            score += 3;
        } else if (memoryUsage > MEMORY_THRESHOLD_MEDIUM) {
            score += 2;
        } else if (memoryUsage > MEMORY_THRESHOLD_EASY) {
            score += 1;
        }

        // Code complexity (lines of code as proxy)
        if (linesOfCode > 200) {
            score += 3;
        } else if (linesOfCode > 100) {
            score += 2;
        } else if (linesOfCode > 50) {
            score += 1;
        }

        // Normalize score to 1-10 scale
        int normalizedScore = Math.min(10, Math.max(1, score));
        difficultyScores.put(partName, normalizedScore);
        difficultyRatings.put(partName, getDifficultyLabel(normalizedScore));

        return normalizedScore;
    }

    /**
     * Gets the difficulty label for a numeric score.
     * @param score the difficulty score (1-10)
     * @return the difficulty label (Very Easy, Easy, Medium, Hard, Very Hard)
     */
    private String getDifficultyLabel(int score) {
        if (score <= 2) {
            return "Very Easy";
        } else if (score <= 4) {
            return "Easy";
        } else if (score <= 6) {
            return "Medium";
        } else if (score <= 8) {
            return "Hard";
        } else {
            return "Very Hard";
        }
    }

    /**
     * Gets the difficulty rating for a specific part.
     * @param partName the name of the part
     * @return the difficulty score (1-10)
     */
    public int getDifficultyScore(String partName) {
        return difficultyScores.getOrDefault(partName, -1);
    }

    /**
     * Gets the difficulty label for a specific part.
     * @param partName the name of the part
     * @return the difficulty label
     */
    public String getDifficultyLabel(String partName) {
        return difficultyRatings.getOrDefault(partName, "Unknown");
    }

    /**
     * Prints a difficulty report.
     */
    public void printDifficultyReport() {
        System.out.println("\n" + "=".repeat(59));
        System.out.println("  Difficulty Assessment Report");
        System.out.println("=".repeat(59));

        difficultyScores.forEach((part, score) -> {
            String label = difficultyRatings.getOrDefault(part, "Unknown");
            System.out.printf("  %s: %d/10 - %s%n", part, score, label);
        });

        System.out.println("=".repeat(59) + "\n");
    }

    /**
     * Gets all difficulty scores as a map.
     * @return map of difficulty scores
     */
    public Map<String, Integer> getDifficultyScores() {
        return new LinkedHashMap<>(difficultyScores);
    }

    /**
     * Calculates average difficulty across all rated parts.
     * @return average difficulty score
     */
    public double getAverageDifficulty() {
        if (difficultyScores.isEmpty()) {
            return 0.0;
        }
        double sum = difficultyScores.values().stream().mapToInt(Integer::intValue).sum();
        return sum / difficultyScores.size();
    }

    /**
     * Resets all difficulty data.
     */
    public void reset() {
        difficultyScores.clear();
        difficultyRatings.clear();
    }
}

