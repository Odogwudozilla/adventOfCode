package odogwudozilla.year2023.day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Advent of Code 2023 Day 3: Gear Ratios
 * https://adventofcode.com/2023/day/3
 *
 * The engine schematic consists of numbers and symbols. Any number adjacent to a symbol
 * (including diagonally) is a part number. Part 1 requires summing all part numbers.
 * Part 2 requires finding gears (asterisks with exactly two adjacent numbers) and
 * multiplying their two numbers together, then summing all such products.
 */
public class GearRatiosAOC2023Day3 {

    private static final String INPUT_FILE = "src/main/resources/2023/day3/day3_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));

            long partOneResult = solvePartOne(lines);
            long partTwoResult = solvePartTwo(lines);

            System.out.println("Part 1 Result: " + partOneResult);
            System.out.println("Part 2 Result: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Part 1: Sum all part numbers (numbers adjacent to symbols).
     */
    private static long solvePartOne(List<String> lines) {
        long sum = 0;
        int rows = lines.size();
        int cols = lines.get(0).length();

        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                if (Character.isDigit(c)) {
                    // Extract the full number
                    int startCol = col;
                    while (startCol > 0 && Character.isDigit(line.charAt(startCol - 1))) {
                        startCol--;
                    }

                    int endCol = col;
                    while (endCol < line.length() - 1 && Character.isDigit(line.charAt(endCol + 1))) {
                        endCol++;
                    }

                    String numberStr = line.substring(startCol, endCol + 1);
                    int number = Integer.parseInt(numberStr);

                    // Check if adjacent to a symbol
                    if (isAdjacentToSymbol(lines, row, startCol, endCol, rows, cols)) {
                        sum += number;
                    }

                    col = endCol; // Skip past this number
                }
            }
        }

        return sum;
    }

    /**
     * Part 2: Find all gears (asterisks with exactly two adjacent numbers)
     * and sum the products of their adjacent numbers.
     */
    private static long solvePartTwo(List<String> lines) {
        long sum = 0;
        int rows = lines.size();
        int cols = lines.getFirst().length();

        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '*') {
                    // This is a potential gear; find all adjacent numbers
                    List<Integer> adjacentNumbers = findAdjacentNumbers(lines, row, col, rows, cols);

                    if (adjacentNumbers.size() == 2) {
                        long gearRatio = (long) adjacentNumbers.get(0) * adjacentNumbers.get(1);
                        sum += gearRatio;
                    }
                }
            }
        }

        return sum;
    }

    /**
     * Check if a number (from startCol to endCol on the given row) is adjacent to a symbol.
     */
    private static boolean isAdjacentToSymbol(List<String> lines, int row, int startCol, int endCol, int rows, int cols) {
        for (int r = Math.max(0, row - 1); r <= Math.min(rows - 1, row + 1); r++) {
            for (int c = Math.max(0, startCol - 1); c <= Math.min(cols - 1, endCol + 1); c++) {
                // Skip the number itself
                if (r == row && c >= startCol && c <= endCol) {
                    continue;
                }

                char ch = lines.get(r).charAt(c);
                if (!Character.isDigit(ch) && ch != '.') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Find all numbers adjacent to a gear at the given position.
     */
    private static List<Integer> findAdjacentNumbers(List<String> lines, int gearRow, int gearCol, int rows, int cols) {
        Map<String, Integer> numbersFound = new HashMap<>();

        for (int r = Math.max(0, gearRow - 1); r <= Math.min(rows - 1, gearRow + 1); r++) {
            String line = lines.get(r);
            for (int c = Math.max(0, gearCol - 1); c <= Math.min(cols - 1, gearCol + 1); c++) {
                if (Character.isDigit(line.charAt(c))) {
                    // Find the full number
                    int startCol = c;
                    while (startCol > 0 && Character.isDigit(line.charAt(startCol - 1))) {
                        startCol--;
                    }

                    int endCol = c;
                    while (endCol < line.length() - 1 && Character.isDigit(line.charAt(endCol + 1))) {
                        endCol++;
                    }

                    String numberKey = r + ":" + startCol; // Unique key for this number
                    if (!numbersFound.containsKey(numberKey)) {
                        String numberStr = line.substring(startCol, endCol + 1);
                        numbersFound.put(numberKey, Integer.parseInt(numberStr));
                    }

                    c = endCol; // Skip past this number
                }
            }
        }

        return new ArrayList<>(numbersFound.values());
    }
}

