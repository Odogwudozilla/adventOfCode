package odogwudozilla.year2025.day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code 2025 - Day 6: Trash Compactor
 *
 * The math worksheet consists of a list of problems arranged horizontally.
 * Each problem's numbers are arranged vertically with the operation symbol at the bottom.
 * Problems are separated by full columns of spaces.
 * This solution parses the vertical problems, evaluates each one, and calculates the grand total.
 *
 * https://adventofcode.com/2025/day/6
 */
public class TrashCompactorAOC2025Day6 {

    private static final String INPUT_FILE = "src/main/resources/2025/day6/day6_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));

            // Part 1: Calculate grand total (left-to-right reading)
            long grandTotal = solvePartOne(lines);
            System.out.println("Part 1 - Grand Total: " + grandTotal);

            // Part 2: Calculate grand total (right-to-left reading, column-by-column)
            long grandTotalPart2 = solvePartTwo(lines);
            System.out.println("Part 2 - Grand Total: " + grandTotalPart2);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Calculate the grand total by parsing all vertical math problems.
     * @param lines the input lines containing the math worksheet
     * @return the sum of all problem answers
     */
    private static long calculateGrandTotal(List<String> lines) {
        if (lines.isEmpty()) {
            return 0;
        }

        // Parse the worksheet into columns
        List<List<String>> columns = parseIntoColumns(lines);

        // Group columns into individual problems (separated by empty columns)
        List<List<List<String>>> problems = groupIntoProblems(columns);

        // Calculate each problem and sum the results
        long grandTotal = 0;
        for (List<List<String>> problem : problems) {
            long result = solveProblem(problem);
            grandTotal += result;
        }

        return grandTotal;
    }

    /**
     * Parse the input lines into columns of values.
     * @param lines the input lines
     * @return list of columns, where each column is a list of values
     */
    private static List<List<String>> parseIntoColumns(List<String> lines) {
        if (lines.isEmpty()) {
            return new ArrayList<>();
        }

        // Find the maximum line length to determine number of columns
        int maxLength = lines.stream().mapToInt(String::length).max().orElse(0);

        List<List<String>> columns = new ArrayList<>();
        for (int col = 0; col < maxLength; col++) {
            List<String> column = new ArrayList<>();
            for (String line : lines) {
                if (col < line.length()) {
                    column.add(String.valueOf(line.charAt(col)));
                } else {
                    column.add(" ");
                }
            }
            columns.add(column);
        }

        return columns;
    }

    /**
     * Group columns into individual problems.
     * Problems are separated by columns containing only spaces.
     * @param columns the parsed columns
     * @return list of problems, where each problem is a list of columns
     */
    private static List<List<List<String>>> groupIntoProblems(List<List<String>> columns) {
        List<List<List<String>>> problems = new ArrayList<>();
        List<List<String>> currentProblem = new ArrayList<>();

        for (List<String> column : columns) {
            boolean isEmptyColumn = column.stream().allMatch(s -> s.trim().isEmpty());

            if (isEmptyColumn) {
                if (!currentProblem.isEmpty()) {
                    problems.add(new ArrayList<>(currentProblem));
                    currentProblem.clear();
                }
            } else {
                currentProblem.add(column);
            }
        }

        // Add the last problem if it exists
        if (!currentProblem.isEmpty()) {
            problems.add(currentProblem);
        }

        return problems;
    }

    /**
     * Solve a single math problem.
     * @param problem the problem columns
     * @return the result of the calculation
     */
    private static long solveProblem(List<List<String>> problem) {
        if (problem.isEmpty()) {
            return 0;
        }

        // Extract the operation from the last row
        String operation = extractOperation(problem);

        // Extract all numbers from the problem (excluding the operation row)
        List<Long> numbers = extractNumbers(problem);

        // Calculate based on the operation
        if (numbers.isEmpty()) {
            return 0;
        }

        long result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            if ("+".equals(operation)) {
                result += numbers.get(i);
            } else if ("*".equals(operation)) {
                result *= numbers.get(i);
            }
        }

        return result;
    }

    /**
     * Extract the operation symbol from the last row of the problem.
     * @param problem the problem columns
     * @return the operation symbol ("+" or "*")
     */
    private static String extractOperation(List<List<String>> problem) {
        // The operation is in the last row
        for (List<String> column : problem) {
            int lastRowIndex = column.size() - 1;
            String value = column.get(lastRowIndex).trim();
            if ("+".equals(value) || "*".equals(value)) {
                return value;
            }
        }
        return "+"; // default to addition
    }

    /**
     * Extract all numbers from the problem (excluding the operation row).
     * @param problem the problem columns
     * @return list of numbers in the problem
     */
    private static List<Long> extractNumbers(List<List<String>> problem) {
        List<Long> numbers = new ArrayList<>();

        // Each row (except the last) may contain parts of a number
        int numRows = problem.get(0).size() - 1; // exclude operation row

        for (int row = 0; row < numRows; row++) {
            StringBuilder numberBuilder = new StringBuilder();

            // Collect digits across all columns for this row
            for (List<String> column : problem) {
                String value = column.get(row);
                if (!value.trim().isEmpty() && Character.isDigit(value.charAt(0))) {
                    numberBuilder.append(value);
                }
            }

            String numberStr = numberBuilder.toString().trim();
            if (!numberStr.isEmpty()) {
                try {
                    numbers.add(Long.parseLong(numberStr));
                } catch (NumberFormatException e) {
                    // Skip invalid numbers
                }
            }
        }

        return numbers;
    }

    /**
     * Calculate the grand total for Part 2 using right-to-left column-by-column reading.
     * @param lines the input lines containing the math worksheet
     * @return the sum of all problem answers
     */
    private static long calculateGrandTotalPart2(List<String> lines) {
        if (lines.isEmpty()) {
            return 0;
        }

        // Parse the worksheet into columns
        List<List<String>> columns = parseIntoColumns(lines);

        // Group columns into individual problems (separated by empty columns)
        List<List<List<String>>> problems = groupIntoProblems(columns);

        // Calculate each problem using right-to-left reading and sum the results
        long grandTotal = 0;
        for (List<List<String>> problem : problems) {
            long result = solveProblemPart2(problem);
            grandTotal += result;
        }

        return grandTotal;
    }

    /**
     * Solve a single math problem for Part 2 (right-to-left, column-by-column reading).
     * @param problem the problem columns
     * @return the result of the calculation
     */
    private static long solveProblemPart2(List<List<String>> problem) {
        if (problem.isEmpty()) {
            return 0;
        }

        // Extract the operation from the last row
        String operation = extractOperation(problem);

        // Extract numbers reading right-to-left, column-by-column
        List<Long> numbers = extractNumbersPart2(problem);

        // Calculate based on the operation
        if (numbers.isEmpty()) {
            return 0;
        }

        long result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            if ("+".equals(operation)) {
                result += numbers.get(i);
            } else if ("*".equals(operation)) {
                result *= numbers.get(i);
            }
        }

        return result;
    }

    /**
     * Extract numbers for Part 2: each column within a problem forms one complete number.
     * Within each column, digits are read top-to-bottom (most significant digit at top).
     * Numbers are extracted right-to-left across columns.
     * @param problem the problem columns
     * @return list of numbers in the problem
     */
    private static List<Long> extractNumbersPart2(List<List<String>> problem) {
        List<Long> numbers = new ArrayList<>();

        int numRows = problem.get(0).size() - 1; // exclude operation row

        // Process columns from right to left
        for (int col = problem.size() - 1; col >= 0; col--) {
            StringBuilder numberBuilder = new StringBuilder();

            // Read this column top-to-bottom to form a number
            for (int row = 0; row < numRows; row++) {
                String value = problem.get(col).get(row);
                if (!value.trim().isEmpty() && Character.isDigit(value.charAt(0))) {
                    numberBuilder.append(value);
                }
            }

            String numberStr = numberBuilder.toString().trim();
            if (!numberStr.isEmpty()) {
                try {
                    numbers.add(Long.parseLong(numberStr));
                } catch (NumberFormatException e) {
                    // Skip invalid numbers
                }
            }
        }

        return numbers;
    }

    /**
     * Solve Part 1: Calculate grand total (left-to-right reading)
     * @param lines the input lines containing the math worksheet
     * @return the sum of all problem answers
     */
    private static long solvePartOne(List<String> lines) {
        return calculateGrandTotal(lines);
    }

    /**
     * Solve Part 2: Calculate grand total (right-to-left reading, column-by-column)
     * @param lines the input lines containing the math worksheet
     * @return the sum of all problem answers
     */
    private static long solvePartTwo(List<String> lines) {
        return calculateGrandTotalPart2(lines);
    }
}
