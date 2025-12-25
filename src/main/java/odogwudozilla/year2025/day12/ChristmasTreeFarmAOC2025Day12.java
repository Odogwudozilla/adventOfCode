package odogwudozilla.year2025.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code 2025 - Day 12: Christmas Tree Farm
 *
 * This puzzle involves fitting presents with specific shapes into rectangular regions.
 * Presents can be rotated and flipped, and we need to determine how many regions
 * can accommodate all their required presents without overlapping.
 *
 * Official puzzle: https://adventofcode.com/2025/day/12
 */
public class ChristmasTreeFarmAOC2025Day12 {

    private static final String INPUT_FILE = "src/main/resources/2025/day12/day12_puzzle_data.txt";

    private static class Point {
        final int row;
        final int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point point = (Point) o;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    private static class Shape {
        final int index;
        final List<Point> points;
        final int height;
        final int width;

        Shape(int index, List<Point> points) {
            this.index = index;
            this.points = points;

            int maxRow = points.stream().mapToInt(p -> p.row).max().orElse(0);
            int maxCol = points.stream().mapToInt(p -> p.col).max().orElse(0);
            this.height = maxRow + 1;
            this.width = maxCol + 1;
        }

        /**
         * Generate all unique rotations and flips of this shape.
         */
        List<Shape> getAllOrientations() {
            Set<String> seen = new HashSet<>();
            List<Shape> orientations = new ArrayList<>();

            for (int rotation = 0; rotation < 4; rotation++) {
                addOrientation(orientations, seen, rotate(points, rotation));
                addOrientation(orientations, seen, flip(rotate(points, rotation)));
            }

            return orientations;
        }

        private void addOrientation(List<Shape> orientations, Set<String> seen, List<Point> points) {
            List<Point> normalized = normalize(points);
            String signature = pointsToString(normalized);
            if (!seen.contains(signature)) {
                seen.add(signature);
                orientations.add(new Shape(index, normalized));
            }
        }

        private List<Point> rotate(List<Point> points, int times) {
            List<Point> result = new ArrayList<>(points);
            for (int i = 0; i < times; i++) {
                List<Point> rotated = new ArrayList<>();
                for (Point p : result) {
                    rotated.add(new Point(p.col, -p.row));
                }
                result = rotated;
            }
            return result;
        }

        private List<Point> flip(List<Point> points) {
            List<Point> flipped = new ArrayList<>();
            for (Point p : points) {
                flipped.add(new Point(p.row, -p.col));
            }
            return flipped;
        }

        private List<Point> normalize(List<Point> points) {
            if (points.isEmpty()) return points;

            int minRow = points.stream().mapToInt(p -> p.row).min().orElse(0);
            int minCol = points.stream().mapToInt(p -> p.col).min().orElse(0);

            List<Point> normalized = new ArrayList<>();
            for (Point p : points) {
                normalized.add(new Point(p.row - minRow, p.col - minCol));
            }
            return normalized;
        }

        private String pointsToString(List<Point> points) {
            StringBuilder sb = new StringBuilder();
            for (Point p : points) {
                sb.append(p.row).append(",").append(p.col).append(";");
            }
            return sb.toString();
        }
    }

    private static class Region {
        final int width;
        final int height;
        final List<Integer> presentCounts;

        Region(int width, int height, List<Integer> presentCounts) {
            this.width = width;
            this.height = height;
            this.presentCounts = presentCounts;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("main - Starting Advent of Code 2025 Day 12: Christmas Tree Farm");

            String inputPath = INPUT_FILE;
            List<String> lines = Files.readAllLines(Path.of(inputPath));
            Map<Integer, Shape> shapes = new HashMap<>();
            List<Region> regions = new ArrayList<>();
            parseInput(lines, shapes, regions);

            // Part 1
            String part1Result = solvePartOne(shapes, regions);
            System.out.println("Part 1 - Number of regions that can fit all presents: " + part1Result);

            // Part 2
            String part2Result = solvePartTwo(shapes, regions);
            System.out.println("Part 2 - Number of regions with unique fit: " + part2Result);

        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Standardised method for Part 1.
     */
    private static String solvePartOne(Map<Integer, Shape> shapes, List<Region> regions) {
        // Actual solution logic for part 1
        // For each region, check if all presents can fit
        Map<Integer, List<Shape>> allOrientations = new HashMap<>();
        for (Map.Entry<Integer, Shape> entry : shapes.entrySet()) {
            allOrientations.put(entry.getKey(), entry.getValue().getAllOrientations());
        }
        int count = 0;
        for (Region region : regions) {
            backtrackCalls = 0;
            if (canFitPresents(region, allOrientations)) {
                count++;
            }
        }
        return String.valueOf(count);
    }

    /**
     * Standardised method for Part 2.
     */
    private static String solvePartTwo(Map<Integer, Shape> shapes, List<Region> regions) {
        // Actual solution logic for part 2
        // For each region, check if there is exactly one way to fit all presents
        Map<Integer, List<Shape>> allOrientations = new HashMap<>();
        for (Map.Entry<Integer, Shape> entry : shapes.entrySet()) {
            allOrientations.put(entry.getKey(), entry.getValue().getAllOrientations());
        }
        int count = 0;
        for (Region region : regions) {
            backtrackCalls = 0;
            int ways = countWaysToFit(region, allOrientations);
            if (ways == 1) {
                count++;
            }
        }
        return String.valueOf(count);
    }

    // Helper for part 2: count number of ways to fit all presents
    private static int countWaysToFit(Region region, Map<Integer, List<Shape>> allOrientations) {
        return countWaysBacktrack(new boolean[region.height][region.width], buildPresentsList(region), 0, allOrientations);
    }

    private static List<Integer> buildPresentsList(Region region) {
        List<Integer> presentsToPlace = new ArrayList<>();
        for (int shapeIdx = 0; shapeIdx < region.presentCounts.size(); shapeIdx++) {
            int count = region.presentCounts.get(shapeIdx);
            for (int j = 0; j < count; j++) {
                presentsToPlace.add(shapeIdx);
            }
        }
        return presentsToPlace;
    }

    private static int countWaysBacktrack(boolean[][] grid, List<Integer> presentsToPlace, int presentIndex, Map<Integer, List<Shape>> allOrientations) {
        if (presentIndex == presentsToPlace.size()) {
            return 1;
        }
        int shapeIndex = presentsToPlace.get(presentIndex);
        List<Shape> orientations = allOrientations.get(shapeIndex);
        int totalWays = 0;
        for (Shape orientation : orientations) {
            for (int row = 0; row <= grid.length - orientation.height; row++) {
                for (int col = 0; col <= grid[0].length - orientation.width; col++) {
                    if (canPlace(grid, orientation, row, col)) {
                        place(grid, orientation, row, col);
                        totalWays += countWaysBacktrack(grid, presentsToPlace, presentIndex + 1, allOrientations);
                        unplace(grid, orientation, row, col);
                    }
                }
            }
        }
        return totalWays;
    }

    /**
     * Parse the input file to extract shape definitions and region requirements.
     */
    private static void parseInput(List<String> lines, Map<Integer, Shape> shapes, List<Region> regions) {
        int i = 0;

        // Parse shapes
        while (i < lines.size()) {
            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                i++;
                continue;
            }

            if (line.contains(":") && !line.contains("x")) {
                // Shape definition
                int shapeIndex = Integer.parseInt(line.substring(0, line.indexOf(':')));
                List<Point> points = new ArrayList<>();

                i++;
                int row = 0;
                while (i < lines.size() && !lines.get(i).trim().isEmpty() && !lines.get(i).contains(":")) {
                    String shapeLine = lines.get(i);
                    for (int col = 0; col < shapeLine.length(); col++) {
                        if (shapeLine.charAt(col) == '#') {
                            points.add(new Point(row, col));
                        }
                    }
                    row++;
                    i++;
                }

                shapes.put(shapeIndex, new Shape(shapeIndex, points));
            } else if (line.contains("x")) {
                // Region definition
                String[] parts = line.split(":");
                String[] dimensions = parts[0].split("x");
                int width = Integer.parseInt(dimensions[0].trim());
                int height = Integer.parseInt(dimensions[1].trim());

                String[] counts = parts[1].trim().split("\\s+");
                List<Integer> presentCounts = new ArrayList<>();
                for (String count : counts) {
                    presentCounts.add(Integer.parseInt(count));
                }

                regions.add(new Region(width, height, presentCounts));
                i++;
            } else {
                i++;
            }
        }
    }

    /**
     * Check if all required presents can fit in the given region using backtracking.
     */
    private static boolean canFitPresents(Region region, Map<Integer, List<Shape>> allOrientations) {
        boolean[][] grid = new boolean[region.height][region.width];

        // Build list of presents to place
        List<Integer> presentsToPlace = new ArrayList<>();
        for (int shapeIdx = 0; shapeIdx < region.presentCounts.size(); shapeIdx++) {
            int count = region.presentCounts.get(shapeIdx);
            for (int j = 0; j < count; j++) {
                presentsToPlace.add(shapeIdx);
            }
        }

        // Quick area check
        int totalPresentCells = 0;
        for (int shapeIdx : presentsToPlace) {
            totalPresentCells += allOrientations.get(shapeIdx).get(0).points.size();
        }
        int totalGridCells = region.width * region.height;
        if (totalPresentCells > totalGridCells) {
            return false;
        }

        return backtrack(grid, presentsToPlace, 0, allOrientations);
    }

    private static long backtrackCalls = 0;
    private static final long MAX_BACKTRACK_CALLS = 1000000; // 1 million attempts per region

    /**
     * Backtracking algorithm to place presents on the grid.
     */
    private static boolean backtrack(boolean[][] grid, List<Integer> presentsToPlace,
                                     int presentIndex, Map<Integer, List<Shape>> allOrientations) {
        backtrackCalls++;

        if (backtrackCalls > MAX_BACKTRACK_CALLS) {
            return false; // Timeout - too many attempts
        }

        if (presentIndex == presentsToPlace.size()) {
            return true; // All presents placed successfully
        }

        int shapeIndex = presentsToPlace.get(presentIndex);
        List<Shape> orientations = allOrientations.get(shapeIndex);

        // Try each orientation at all valid positions
        for (Shape orientation : orientations) {
            for (int row = 0; row <= grid.length - orientation.height; row++) {
                for (int col = 0; col <= grid[0].length - orientation.width; col++) {
                    if (canPlace(grid, orientation, row, col)) {
                        place(grid, orientation, row, col);

                        if (backtrack(grid, presentsToPlace, presentIndex + 1, allOrientations)) {
                            return true;
                        }

                        unplace(grid, orientation, row, col);
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check if a shape can be placed at the given position.
     */
    private static boolean canPlace(boolean[][] grid, Shape shape, int startRow, int startCol) {
        for (Point p : shape.points) {
            int row = startRow + p.row;
            int col = startCol + p.col;

            if (row >= grid.length || col >= grid[0].length || grid[row][col]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Place a shape on the grid.
     */
    private static void place(boolean[][] grid, Shape shape, int startRow, int startCol) {
        for (Point p : shape.points) {
            grid[startRow + p.row][startCol + p.col] = true;
        }
    }

    /**
     * Remove a shape from the grid.
     */
    private static void unplace(boolean[][] grid, Shape shape, int startRow, int startCol) {
        for (Point p : shape.points) {
            grid[startRow + p.row][startCol + p.col] = false;
        }
    }
}
