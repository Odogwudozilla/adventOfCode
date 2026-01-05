package odogwudozilla.year2017.day21;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

/**
 * Advent of Code 2017 Day 21: Fractal Art
 *
 * You find a program trying to generate some art. It uses a strange process that involves repeatedly enhancing the detail of an image through a set of rules.
 *
 * The image consists of a two-dimensional square grid of pixels that are either on (#) or off (.). The program always begins with this pattern:
 * .#.
 * ..#
 * ###
 *
 * The program repeats the following process:
 * If the size is evenly divisible by 2, break the pixels up into 2x2 squares, and convert each 2x2 square into a 3x3 square by following the corresponding enhancement rule.
 * Otherwise, the size is evenly divisible by 3; break the pixels up into 3x3 squares, and convert each 3x3 square into a 4x4 square by following the corresponding enhancement rule.
 * Rules may require rotation or flipping of the input pattern to match.
 *
 * Official puzzle link: https://adventofcode.com/2017/day/21
 *
 * @param args Command line arguments
 * @return None
 */
public class FractalArtAOC2017Day21 {
    private static final String INPUT_PATH = "src/main/resources/2017/day21/day21_puzzle_data.txt";
    private static final int PART_ONE_ITERATIONS = 5;
    private static final int PART_TWO_ITERATIONS = 18;
    private static final String ON_PIXEL = "#";
    private static final String OFF_PIXEL = ".";

    public static void main(String[] args) {
        try {
            List<String> inputLines = Files.readAllLines(Paths.get(INPUT_PATH));
            int partOneResult = solvePartOne(inputLines);
            System.out.println("solvePartOne - Pixels on after 5 iterations: " + partOneResult);
            int partTwoResult = solvePartTwo(inputLines);
            System.out.println("solvePartTwo - Pixels on after 18 iterations: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: How many pixels are on after 5 iterations?
     * @param inputLines The enhancement rules from the puzzle input
     * @return Number of pixels on after 5 iterations
     */
    public static int solvePartOne(List<String> inputLines) {
        Map<String, String> rules = parseRules(inputLines);
        List<String> grid = Arrays.asList(
                ".#.",
                "..#",
                "###"
        );
        for (int i = 0; i < PART_ONE_ITERATIONS; i++) {
            grid = enhance(grid, rules);
        }
        return countOnPixels(grid);
    }

    /**
     * Solves Part 2: How many pixels are on after 18 iterations?
     * @param inputLines The enhancement rules from the puzzle input
     * @return Number of pixels on after 18 iterations
     */
    public static int solvePartTwo(List<String> inputLines) {
        Map<String, String> rules = parseRules(inputLines);
        List<String> grid = Arrays.asList(
                ".#.",
                "..#",
                "###"
        );
        for (int i = 0; i < PART_TWO_ITERATIONS; i++) {
            grid = enhance(grid, rules);
        }
        return countOnPixels(grid);
    }

    // Utility methods for parsing rules, enhancing grid, rotating/flipping, and counting pixels
    private static Map<String, String> parseRules(List<String> inputLines) {
        Map<String, String> rules = new HashMap<>();
        for (String line : inputLines) {
            String[] parts = line.split(" => ");
            if (parts.length == 2) {
                rules.put(parts[0], parts[1]);
            }
        }
        return rules;
    }

    private static List<String> enhance(List<String> grid, Map<String, String> rules) {
        int size = grid.size();
        int blockSize = (size % 2 == 0) ? 2 : 3;
        int newBlockSize = blockSize + 1;
        int blocksPerRow = size / blockSize;
        List<List<List<String>>> newBlocks = new ArrayList<>();
        for (int by = 0; by < blocksPerRow; by++) {
            List<List<String>> rowBlocks = new ArrayList<>();
            for (int bx = 0; bx < blocksPerRow; bx++) {
                List<String> block = new ArrayList<>();
                for (int y = 0; y < blockSize; y++) {
                    block.add(grid.get(by * blockSize + y).substring(bx * blockSize, bx * blockSize + blockSize));
                }
                String matched = matchBlock(block, rules);
                rowBlocks.add(Arrays.asList(matched.split("/")));
            }
            newBlocks.add(rowBlocks);
        }
        // Stitch blocks together
        List<String> newGrid = new ArrayList<>();
        for (int by = 0; by < blocksPerRow; by++) {
            for (int y = 0; y < newBlockSize; y++) {
                StringBuilder sb = new StringBuilder();
                for (int bx = 0; bx < blocksPerRow; bx++) {
                    sb.append(newBlocks.get(by).get(bx).get(y));
                }
                newGrid.add(sb.toString());
            }
        }
        return newGrid;
    }

    private static String matchBlock(List<String> block, Map<String, String> rules) {
        List<List<String>> variants = generateVariants(block);
        for (List<String> variant : variants) {
            String key = String.join("/", variant);
            if (rules.containsKey(key)) {
                return rules.get(key);
            }
        }
        throw new IllegalArgumentException("matchBlock - No matching rule for block: " + block);
    }

    private static List<List<String>> generateVariants(List<String> block) {
        List<List<String>> variants = new ArrayList<>();
        List<String> current = new ArrayList<>(block);
        for (int i = 0; i < 4; i++) {
            variants.add(new ArrayList<>(current));
            variants.add(flipHorizontal(current));
            variants.add(flipVertical(current));
            current = rotate(current);
        }
        return variants;
    }

    private static List<String> rotate(List<String> block) {
        int n = block.size();
        List<String> rotated = new ArrayList<>();
        for (int x = 0; x < n; x++) {
            StringBuilder sb = new StringBuilder();
            for (int y = n - 1; y >= 0; y--) {
                sb.append(block.get(y).charAt(x));
            }
            rotated.add(sb.toString());
        }
        return rotated;
    }

    private static List<String> flipHorizontal(List<String> block) {
        List<String> flipped = new ArrayList<>();
        for (String row : block) {
            flipped.add(new StringBuilder(row).reverse().toString());
        }
        return flipped;
    }

    private static List<String> flipVertical(List<String> block) {
        List<String> flipped = new ArrayList<>(block);
        Collections.reverse(flipped);
        return flipped;
    }

    private static int countOnPixels(List<String> grid) {
        int count = 0;
        for (String row : grid) {
            for (char c : row.toCharArray()) {
                if (ON_PIXEL.equals(String.valueOf(c))) {
                    count++;
                }
            }
        }
        return count;
    }
}
