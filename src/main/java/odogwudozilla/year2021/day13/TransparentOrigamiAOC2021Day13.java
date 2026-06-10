package odogwudozilla.year2021.day13;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Advent of Code 2021 - Day 13: Transparent Origami
 * <p>
 * Puzzle URL: https://adventofcode.com/2021/day/13
 */
public final class TransparentOrigamiAOC2021Day13 {

    private static final String INPUT_FILE = "/2021/day13/day13_puzzle_data.txt";
    private static final int LETTER_HEIGHT = 6;
    private static final Map<String, String> LETTER_PATTERNS = createLetterPatterns();

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    private static String solvePartOne(List<String> input) {
        ParsedInput parsedInput = parseInput(input);
        FoldInstruction firstFold = parsedInput.folds().get(0);
        Set<Point> folded = applyFold(parsedInput.dots(), firstFold);
        return String.valueOf(folded.size());
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        ParsedInput parsedInput = parseInput(input);
        Set<Point> dots = new HashSet<>(parsedInput.dots());
        for (FoldInstruction fold : parsedInput.folds()) {
            dots = applyFold(dots, fold);
        }
        return decodeMessage(dots);
    }

    private static ParsedInput parseInput(List<String> input) {
        Set<Point> dots = new HashSet<>();
        List<FoldInstruction> folds = new ArrayList<>();
        boolean readingDots = true;

        for (String line : input) {
            if (line.isBlank()) {
                readingDots = false;
                continue;
            }

            if (readingDots) {
                String[] coordinates = line.split(",");
                dots.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
                continue;
            }

            String[] parts = line.substring("fold along ".length()).split("=");
            folds.add(new FoldInstruction(parts[0].charAt(0), Integer.parseInt(parts[1])));
        }

        return new ParsedInput(dots, folds);
    }

    private static Set<Point> applyFold(Set<Point> dots, FoldInstruction fold) {
        Set<Point> folded = new HashSet<>();
        for (Point dot : dots) {
            if (fold.axis() == 'x' && dot.x() > fold.position()) {
                int foldedX = fold.position() - (dot.x() - fold.position());
                folded.add(new Point(foldedX, dot.y()));
            } else if (fold.axis() == 'y' && dot.y() > fold.position()) {
                int foldedY = fold.position() - (dot.y() - fold.position());
                folded.add(new Point(dot.x(), foldedY));
            } else {
                folded.add(dot);
            }
        }
        return folded;
    }

    private static String decodeMessage(Set<Point> dots) {
        int maxX = dots.stream().mapToInt(Point::x).max().orElse(0);
        int width = maxX + 1;
        char[][] grid = new char[LETTER_HEIGHT][width];
        for (int y = 0; y < LETTER_HEIGHT; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = dots.contains(new Point(x, y)) ? '#' : '.';
            }
        }

        StringBuilder decoded = new StringBuilder();
        int x = 0;
        while (x < width) {
            while (x < width && isEmptyColumn(grid, x)) {
                x++;
            }
            if (x >= width) {
                break;
            }

            int letterStart = x;
            while (x < width && !isEmptyColumn(grid, x)) {
                x++;
            }
            int letterEnd = x - 1;

            String letterKey = buildLetterKey(grid, letterStart, letterEnd);
            decoded.append(LETTER_PATTERNS.getOrDefault(letterKey, "?"));
        }

        return decoded.toString();
    }

    private static boolean isEmptyColumn(char[][] grid, int column) {
        for (int y = 0; y < LETTER_HEIGHT; y++) {
            if (grid[y][column] == '#') {
                return false;
            }
        }
        return true;
    }

    private static String buildLetterKey(char[][] grid, int startX, int endX) {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < LETTER_HEIGHT; y++) {
            for (int x = startX; x <= endX; x++) {
                builder.append(grid[y][x]);
            }
            if (y < LETTER_HEIGHT - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    private static Map<String, String> createLetterPatterns() {
        Map<String, String> patterns = new HashMap<>();
        patterns.put(".##.\n#..#\n#..#\n####\n#..#\n#..#", "A");
        patterns.put("###.\n#..#\n###.\n#..#\n#..#\n###.", "B");
        patterns.put(".##.\n#..#\n#...\n#...\n#..#\n.##.", "C");
        patterns.put("###.\n#..#\n#..#\n#..#\n#..#\n###.", "D");
        patterns.put("####\n#...\n###.\n#...\n#...\n####", "E");
        patterns.put("####\n#...\n###.\n#...\n#...\n#...", "F");
        patterns.put(".##.\n#..#\n#...\n#.##\n#..#\n.###", "G");
        patterns.put("#..#\n#..#\n####\n#..#\n#..#\n#..#", "H");
        patterns.put("###\n.#.\n.#.\n.#.\n.#.\n###", "I");
        patterns.put("..##\n...#\n...#\n...#\n#..#\n.##.", "J");
        patterns.put("#..#\n#.#.\n##..\n#.#.\n#.#.\n#..#", "K");
        patterns.put("#...\n#...\n#...\n#...\n#...\n####", "L");
        patterns.put("#..#\n##.#\n#.##\n#..#\n#..#\n#..#", "N");
        patterns.put(".##.\n#..#\n#..#\n#..#\n#..#\n.##.", "O");
        patterns.put("###.\n#..#\n#..#\n###.\n#...\n#...", "P");
        patterns.put("###.\n#..#\n#..#\n###.\n#.#.\n#..#", "R");
        patterns.put(".###\n#...\n#...\n.##.\n...#\n###.", "S");
        patterns.put("####\n.#..\n.#..\n.#..\n.#..\n.#..", "T");
        patterns.put("#..#\n#..#\n#..#\n#..#\n#..#\n.##.", "U");
        patterns.put("#..#\n#..#\n.##.\n.##.\n#..#\n#..#", "X");
        patterns.put("####\n...#\n..#.\n.#..\n#...\n####", "Z");
        return patterns;
    }

    private record Point(int x, int y) {
    }

    private record FoldInstruction(char axis, int position) {
    }

    private record ParsedInput(Set<Point> dots, List<FoldInstruction> folds) {
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = TransparentOrigamiAOC2021Day13.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            java.util.List<String> lines = new java.util.ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
