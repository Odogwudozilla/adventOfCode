package odogwudozilla.year2015.day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code 2015 - Day 2: I Was Told There Would Be No Math
 * <p>
 * The elves need to order wrapping paper for presents. Each present is a box with dimensions (length, width, height).
 * The required wrapping paper is the surface area (2*l*w + 2*w*h + 2*h*l) plus the area of the smallest side as slack.
 * <p>
 * Puzzle: <a href="https://adventofcode.com/2015/day/2">https://adventofcode.com/2015/day/2</a>
 */
public class IWasToldThereWouldBeNoMathAOC2015Day2 {

    private static final String INPUT_FILE = "src/main/resources/2015/day2/day2_puzzle_data.txt";

    public static void main(String[] args) {
        System.out.println("main - Starting Advent of Code 2015 Day 2: I Was Told There Would Be No Math");

        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
            int totalWrappingPaper = solvePartOne(lines);
            int totalRibbon = solvePartTwo(lines);

            System.out.println("main - Part 1: Total square feet of wrapping paper needed: " + totalWrappingPaper);
            System.out.println("main - Part 2: Total feet of ribbon needed: " + totalRibbon);

        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Standardised method for Part 1.
     */
    private static int solvePartOne(List<String> dimensions) {
        return calculateTotalWrappingPaper(dimensions);
    }

    /**
     * Standardised method for Part 2.
     */
    private static int solvePartTwo(List<String> dimensions) {
        return calculateTotalRibbon(dimensions);
    }

    /**
     * Calculate the total wrapping paper needed for all presents.
     * @param dimensions list of dimension strings in format "LxWxH"
     * @return total square feet of wrapping paper needed
     */
    private static int calculateTotalWrappingPaper(List<String> dimensions) {
        System.out.println("calculateTotalWrappingPaper - Processing " + dimensions.size() + " presents");

        int total = 0;
        for (String dimension : dimensions) {
            total += calculateWrappingPaperForBox(dimension);
        }

        return total;
    }

    /**
     * Calculate wrapping paper needed for a single box.
     * @param dimension dimension string in format "LxWxH"
     * @return square feet of wrapping paper needed
     */
    private static int calculateWrappingPaperForBox(String dimension) {
        String[] parts = dimension.split("x");
        int length = Integer.parseInt(parts[0]);
        int width = Integer.parseInt(parts[1]);
        int height = Integer.parseInt(parts[2]);

        // Calculate the three side areas
        int side1 = length * width;
        int side2 = width * height;
        int side3 = height * length;

        // Surface area = 2*l*w + 2*w*h + 2*h*l
        int surfaceArea = 2 * side1 + 2 * side2 + 2 * side3;

        // Find the smallest side for slack
        int smallestSide = Math.min(side1, Math.min(side2, side3));

        return surfaceArea + smallestSide;
    }

    /**
     * Calculate the total ribbon needed for all presents.
     * @param dimensions list of dimension strings in format "LxWxH"
     * @return total feet of ribbon needed
     */
    private static int calculateTotalRibbon(List<String> dimensions) {
        System.out.println("calculateTotalRibbon - Processing " + dimensions.size() + " presents");

        int total = 0;
        for (String dimension : dimensions) {
            total += calculateRibbonForBox(dimension);
        }

        return total;
    }

    /**
     * Calculate ribbon needed for a single box.
     * Ribbon = smallest perimeter + volume (for bow)
     * @param dimension dimension string in format "LxWxH"
     * @return feet of ribbon needed
     */
    private static int calculateRibbonForBox(String dimension) {
        String[] parts = dimension.split("x");
        int length = Integer.parseInt(parts[0]);
        int width = Integer.parseInt(parts[1]);
        int height = Integer.parseInt(parts[2]);

        // Find the two smallest dimensions for the perimeter
        int max = Math.max(length, Math.max(width, height));
        int perimeterRibbon;

        if (max == length) {
            perimeterRibbon = 2 * (width + height);
        } else if (max == width) {
            perimeterRibbon = 2 * (length + height);
        } else {
            perimeterRibbon = 2 * (length + width);
        }

        // Calculate volume for the bow
        int bowRibbon = length * width * height;

        return perimeterRibbon + bowRibbon;
    }
}
