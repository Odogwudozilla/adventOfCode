package odogwudozilla.core;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Harness for testing and validating puzzle outputs.
 * Allows comparison of actual results against expected results.
 */
public class PuzzleTestHarness {

    private final String puzzleIdentifier;
    private final Map<String, String> expectedResults;
    private final Map<String, String> actualResults;
    private final Map<String, Boolean> testResults;

    public PuzzleTestHarness(String year, String day) {
        this.puzzleIdentifier = year + " " + day;
        this.expectedResults = new LinkedHashMap<>();
        this.actualResults = new LinkedHashMap<>();
        this.testResults = new LinkedHashMap<>();
    }

    /**
     * Sets the expected result for a puzzle part.
     * @param partName the name of the part (e.g., "Part 1", "Part 2")
     * @param expectedResult the expected solution result
     */
    public void setExpectedResult(String partName, String expectedResult) {
        expectedResults.put(partName, expectedResult);
    }

    /**
     * Records the actual result for a puzzle part and compares with expected.
     * @param partName the name of the part (e.g., "Part 1", "Part 2")
     * @param actualResult the actual solution result
     * @return true if actual matches expected, false otherwise
     */
    public boolean recordActualResult(String partName, String actualResult) {
        actualResults.put(partName, actualResult);

        String expected = expectedResults.get(partName);
        boolean matches = expected != null && expected.equals(actualResult);
        testResults.put(partName, matches);

        return matches;
    }

    /**
     * Validates all recorded results against expected results.
     * @return true if all tests pass, false otherwise
     */
    public boolean validateAll() {
        return testResults.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * Gets the test result for a specific part.
     * @param partName the name of the part
     * @return true if test passed, false if failed, null if not tested
     */
    public Boolean getTestResult(String partName) {
        return testResults.get(partName);
    }

    /**
     * Prints a test report with results.
     */
    public void printTestReport() {
        System.out.println("\n" + "=".repeat(59));
        System.out.println("  Test Report: Advent of Code " + puzzleIdentifier);
        System.out.println("=".repeat(59));

        testResults.forEach((part, passed) -> {
            String status = passed ? "PASS" : "FAIL";
            String expected = expectedResults.getOrDefault(part, "Not set");
            String actual = actualResults.getOrDefault(part, "Not recorded");

            System.out.printf("  %s: %s%n", part, status);
            System.out.printf("    Expected: %s%n", expected);
            System.out.printf("    Actual:   %s%n", actual);

            if (!passed && expectedResults.containsKey(part)) {
                System.out.printf("    Match: %s%n", expected.equals(actual) ? "Yes" : "No");
            }
        });

        long passCount = testResults.values().stream().filter(Boolean::booleanValue).count();
        System.out.printf("  Total: %d/%d tests passed%n", passCount, testResults.size());
        System.out.println("=".repeat(59) + "\n");
    }

    /**
     * Gets the number of passing tests.
     * @return number of passing tests
     */
    public long getPassCount() {
        return testResults.values().stream().filter(Boolean::booleanValue).count();
    }

    /**
     * Gets the total number of tests.
     * @return total number of tests
     */
    public int getTotalTests() {
        return testResults.size();
    }

    /**
     * Gets all test results as a map.
     * @return map of test results
     */
    public Map<String, Boolean> getTestResults() {
        return new LinkedHashMap<>(testResults);
    }

    /**
     * Resets all test data.
     */
    public void reset() {
        expectedResults.clear();
        actualResults.clear();
        testResults.clear();
    }
}

