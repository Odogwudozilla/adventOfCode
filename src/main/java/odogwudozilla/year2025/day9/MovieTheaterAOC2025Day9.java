package odogwudozilla.year2025.day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code 2025 - Day 9: Movie Theater
 *
 * Part 1: Find the largest rectangle that uses red tiles for two of its opposite corners.
 *
 * Part 2: The rectangle can only include red or green tiles. Green tiles form a path
 * connecting consecutive red tiles in the input list (wrapping around), and also fill
 * the interior of the loop formed by the red tiles. Find the largest valid rectangle.
 *
 * Puzzle URL: https://adventofcode.com/2025/day/9
 */
public class MovieTheaterAOC2025Day9 {

    private static final String INPUT_FILE_PATH = "src/main/resources/2025/day9/day9_puzzle_data.txt";

    /**
     * Represents a coordinate point with x and y values.
     */
    private static class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE_PATH));
            List<Point> redTilesList = parseRedTilesList(lines);
            Set<Point> redTilesSet = new HashSet<>(redTilesList);

            long part1Result = solvePart1(redTilesSet);
            System.out.println("Part 1 - Largest rectangle area: " + part1Result);

            long part2Result = solvePart2(redTilesList, redTilesSet);
            System.out.println("Part 2 - Largest rectangle area (red/green only): " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses the input lines to extract red tile coordinates in order.
     * @param lines the input lines containing coordinates in "x,y" format
     * @return a list of Point objects representing red tile locations in order
     */
    private static List<Point> parseRedTilesList(List<String> lines) {
        List<Point> redTiles = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            redTiles.add(new Point(x, y));
        }
        return redTiles;
    }

    /**
     * Solves Part 1 by finding the largest rectangle area formed by any two red tiles as opposite corners.
     * @param redTiles set of all red tile coordinates
     * @return the largest rectangle area
     */
    private static long solvePart1(Set<Point> redTiles) {
        final String methodName = "solvePart1";
        System.out.println(methodName + " - Starting calculation with " + redTiles.size() + " red tiles");

        long maxArea = 0;
        Point[] tileArray = redTiles.toArray(new Point[0]);

        // Try all pairs of red tiles as potential opposite corners
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = i + 1; j < tileArray.length; j++) {
                Point tile1 = tileArray[i];
                Point tile2 = tileArray[j];

                // Calculate the area of the rectangle formed by these two tiles as opposite corners
                // The rectangle includes all tiles from one corner to the other, so we add 1 to each dimension
                long width = Math.abs(tile1.x - tile2.x) + 1;
                long height = Math.abs(tile1.y - tile2.y) + 1;
                long area = width * height;

                if (area > maxArea) {
                    maxArea = area;
                    System.out.println(methodName + " - New max area: " + maxArea +
                                     " between " + tile1 + " and " + tile2);
                }
            }
        }

        return maxArea;
    }

    /**
     * Solves Part 2 by finding the largest rectangle that only contains red or green tiles.
     * @param redTilesList ordered list of red tile coordinates
     * @param redTilesSet set of red tile coordinates for quick lookup
     * @return the largest rectangle area using only red/green tiles
     */
    private static long solvePart2(List<Point> redTilesList, Set<Point> redTilesSet) {
        final String methodName = "solvePart2";
        long startTime = System.currentTimeMillis();

        System.out.println(methodName + " - Step 1: Coordinate compression");

        // Extract all unique X and Y coordinates from red tiles ONLY
        // We don't need every coordinate along paths - just the red tile positions
        Set<Integer> xCoords = new HashSet<>();
        Set<Integer> yCoords = new HashSet<>();
        for (Point p : redTilesList) {
            xCoords.add(p.x);
            yCoords.add(p.y);
        }

        // Create sorted arrays and mapping
        List<Integer> xList = new ArrayList<>(xCoords);
        List<Integer> yList = new ArrayList<>(yCoords);
        xList.sort(Integer::compareTo);
        yList.sort(Integer::compareTo);

        Map<Integer, Integer> xToCompressed = new HashMap<>();
        Map<Integer, Integer> yToCompressed = new HashMap<>();

        for (int i = 0; i < xList.size(); i++) {
            xToCompressed.put(xList.get(i), i);
        }
        for (int i = 0; i < yList.size(); i++) {
            yToCompressed.put(yList.get(i), i);
        }

        System.out.println(methodName + " - Compressed from " + redTilesList.size() +
                         " red tiles to grid: " + xList.size() + "x" + yList.size());

        // Step 2: Build valid tiles in compressed coordinates
        System.out.println(methodName + " - Step 2: Building valid tile set");
        boolean[][] validGrid = new boolean[xList.size()][yList.size()];

        // Mark red tiles
        for (Point p : redTilesList) {
            int cx = xToCompressed.get(p.x);
            int cy = yToCompressed.get(p.y);
            validGrid[cx][cy] = true;
        }

        // Mark path tiles - in compressed space, we mark the entire rectangle between consecutive points
        for (int i = 0; i < redTilesList.size(); i++) {
            Point current = redTilesList.get(i);
            Point next = redTilesList.get((i + 1) % redTilesList.size());

            int cx1 = xToCompressed.get(current.x);
            int cy1 = yToCompressed.get(current.y);
            int cx2 = xToCompressed.get(next.x);
            int cy2 = yToCompressed.get(next.y);

            if (current.x == next.x) {
                // Vertical line in original space - mark entire compressed y range
                int minCy = Math.min(cy1, cy2);
                int maxCy = Math.max(cy1, cy2);
                for (int cy = minCy; cy <= maxCy; cy++) {
                    validGrid[cx1][cy] = true;
                }
            } else if (current.y == next.y) {
                // Horizontal line in original space - mark entire compressed x range
                int minCx = Math.min(cx1, cx2);
                int maxCx = Math.max(cx1, cx2);
                for (int cx = minCx; cx <= maxCx; cx++) {
                    validGrid[cx][cy1] = true;
                }
            }
        }

        // Mark interior tiles using ray casting in compressed space
        System.out.println(methodName + " - Step 3: Filling polygon interior");
        for (int cx = 0; cx < xList.size(); cx++) {
            for (int cy = 0; cy < yList.size(); cy++) {
                if (!validGrid[cx][cy]) {
                    // Check if this point is inside the polygon
                    Point testPoint = new Point(xList.get(cx), yList.get(cy));
                    if (isPointInsidePolygon(testPoint, redTilesList)) {
                        validGrid[cx][cy] = true;
                    }
                }
            }
        }

        System.out.println(methodName + " - Step 4: Finding largest rectangle");

        // Convert red tiles to compressed coordinates
        List<Point> compressedRedTiles = new ArrayList<>();
        for (Point p : redTilesList) {
            compressedRedTiles.add(new Point(xToCompressed.get(p.x), yToCompressed.get(p.y)));
        }

        long maxArea = 0;
        Point bestCorner1 = null;
        Point bestCorner2 = null;

        // Try all pairs of red tiles
        for (int i = 0; i < compressedRedTiles.size(); i++) {
            if (i % 50 == 0) {
                System.out.println(methodName + " - Progress: checking red tile " + i + "/" + compressedRedTiles.size());
            }

            for (int j = i + 1; j < compressedRedTiles.size(); j++) {
                Point c1 = compressedRedTiles.get(i);
                Point c2 = compressedRedTiles.get(j);

                int minCx = Math.min(c1.x, c2.x);
                int maxCx = Math.max(c1.x, c2.x);
                int minCy = Math.min(c1.y, c2.y);
                int maxCy = Math.max(c1.y, c2.y);

                // Check if all tiles in this rectangle are valid
                boolean allValid = true;
                for (int cx = minCx; cx <= maxCx && allValid; cx++) {
                    for (int cy = minCy; cy <= maxCy && allValid; cy++) {
                        if (!validGrid[cx][cy]) {
                            allValid = false;
                        }
                    }
                }

                if (allValid) {
                    // Calculate actual area using original coordinates
                    Point orig1 = redTilesList.get(i);
                    Point orig2 = redTilesList.get(j);
                    long width = Math.abs(orig1.x - orig2.x) + 1;
                    long height = Math.abs(orig1.y - orig2.y) + 1;
                    long area = width * height;

                    if (area > maxArea) {
                        maxArea = area;
                        bestCorner1 = orig1;
                        bestCorner2 = orig2;
                        System.out.println(methodName + " - New max area: " + maxArea +
                                         " between " + bestCorner1 + " and " + bestCorner2);
                    }
                }
            }
        }

        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println(methodName + " - Completed in " + elapsed + " seconds");

        return maxArea;
    }

    /**
     * Helper class to store rectangle candidates with their area.
     */
    private static class RectangleCandidate {
        final Point corner1;
        final Point corner2;
        final long area;

        RectangleCandidate(Point corner1, Point corner2, long area) {
            this.corner1 = corner1;
            this.corner2 = corner2;
            this.area = area;
        }
    }

    /**
     * Builds the set of all green tiles (paths between consecutive red tiles and interior tiles).
     * @param redTilesList ordered list of red tile coordinates
     * @return set of all green tile coordinates
     */
    private static Set<Point> buildGreenTiles(List<Point> redTilesList) {
        final String methodName = "buildGreenTiles";
        Set<Point> greenTiles = new HashSet<>();

        // Add path tiles between consecutive red tiles (including wrap-around)
        System.out.println(methodName + " - Adding path tiles between consecutive red tiles");
        for (int i = 0; i < redTilesList.size(); i++) {
            Point current = redTilesList.get(i);
            Point next = redTilesList.get((i + 1) % redTilesList.size());

            // Add all tiles on the path between current and next (excluding endpoints)
            addPathTiles(current, next, greenTiles);
        }
        System.out.println(methodName + " - Added " + greenTiles.size() + " path tiles");

        // Compute interior tiles efficiently using scanline approach
        System.out.println(methodName + " - Computing interior tiles using optimized scanline...");
        Set<Point> interiorTiles = findInteriorTilesOptimized(redTilesList, greenTiles);
        greenTiles.addAll(interiorTiles);
        System.out.println(methodName + " - Added " + interiorTiles.size() + " interior tiles");
        System.out.println(methodName + " - Total green tiles: " + greenTiles.size());

        return greenTiles;
    }

    /**
     * Efficiently finds interior tiles using a scanline approach - only scan rows that contain edges.
     * @param redTilesList ordered list of red tile coordinates forming a polygon
     * @param boundaryTiles set of tiles on the boundary (green path tiles)
     * @return set of interior tile coordinates
     */
    private static Set<Point> findInteriorTilesOptimized(List<Point> redTilesList, Set<Point> boundaryTiles) {
        Set<Point> interiorTiles = new HashSet<>();

        // Find bounding box
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : redTilesList) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }

        // Scanline approach: for each row, find interior points
        for (int y = minY; y <= maxY; y++) {
            // For this row, check each x coordinate
            for (int x = minX; x <= maxX; x++) {
                Point testPoint = new Point(x, y);

                // Skip if already on boundary or is a red tile
                if (boundaryTiles.contains(testPoint) || redTilesList.contains(testPoint)) {
                    continue;
                }

                // Use ray casting to determine if point is inside polygon
                if (isPointInsidePolygon(testPoint, redTilesList)) {
                    interiorTiles.add(testPoint);
                }
            }

            // Progress reporting for long computations
            if ((y - minY) % 1000 == 0) {
                int progress = ((y - minY) * 100) / (maxY - minY + 1);
                System.out.println("findInteriorTilesOptimized - Progress: " + progress + "% (row " + y + "/" + maxY + ")");
            }
        }

        return interiorTiles;
    }

    /**
     * Adds all tiles on the straight path between two points (excluding endpoints).
     * @param from starting point
     * @param to ending point
     * @param tiles set to add path tiles to
     */
    private static void addPathTiles(Point from, Point to, Set<Point> tiles) {
        if (from.x == to.x) {
            // Vertical line
            int minY = Math.min(from.y, to.y);
            int maxY = Math.max(from.y, to.y);
            for (int y = minY + 1; y < maxY; y++) {
                tiles.add(new Point(from.x, y));
            }
        } else if (from.y == to.y) {
            // Horizontal line
            int minX = Math.min(from.x, to.x);
            int maxX = Math.max(from.x, to.x);
            for (int x = minX + 1; x < maxX; x++) {
                tiles.add(new Point(x, from.y));
            }
        }
    }

    /**
     * Finds all interior tiles of the polygon formed by red tiles using ray casting algorithm.
     * @param redTilesList ordered list of red tile coordinates forming a polygon
     * @param boundaryTiles set of tiles on the boundary (green path tiles)
     * @return set of interior tile coordinates
     */
    private static Set<Point> findInteriorTiles(List<Point> redTilesList, Set<Point> boundaryTiles) {
        Set<Point> interiorTiles = new HashSet<>();

        // Find bounding box
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : redTilesList) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }

        // Check each point in the bounding box
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Point testPoint = new Point(x, y);

                // Skip if already on boundary or is a red tile
                if (boundaryTiles.contains(testPoint) || redTilesList.contains(testPoint)) {
                    continue;
                }

                // Use ray casting to determine if point is inside polygon
                if (isPointInsidePolygon(testPoint, redTilesList)) {
                    interiorTiles.add(testPoint);
                }
            }
        }

        return interiorTiles;
    }

    /**
     * Determines if a point is inside a polygon using ray casting algorithm.
     * @param point the point to test
     * @param polygon ordered list of vertices forming the polygon
     * @return true if the point is inside the polygon
     */
    private static boolean isPointInsidePolygon(Point point, List<Point> polygon) {
        int intersections = 0;
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);

            // Check if horizontal ray from point intersects edge (p1, p2)
            if (((p1.y > point.y) != (p2.y > point.y)) &&
                (point.x < (p2.x - p1.x) * (point.y - p1.y) / (double)(p2.y - p1.y) + p1.x)) {
                intersections++;
            }
        }

        return (intersections % 2) == 1;
    }

    /**
     * Checks if all tiles within a rectangle are in the valid tiles set.
     * @param corner1 first corner of the rectangle
     * @param corner2 opposite corner of the rectangle
     * @param validTiles set of valid tile coordinates (red or green)
     * @return true if all tiles in the rectangle are valid
     */
    private static boolean isRectangleValid(Point corner1, Point corner2, Set<Point> validTiles) {
        int minX = Math.min(corner1.x, corner2.x);
        int maxX = Math.max(corner1.x, corner2.x);
        int minY = Math.min(corner1.y, corner2.y);
        int maxY = Math.max(corner1.y, corner2.y);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (!validTiles.contains(new Point(x, y))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if all tiles within a rectangle are red, green path tiles, or inside the polygon.
     * @param corner1 first corner of the rectangle
     * @param corner2 opposite corner of the rectangle
     * @param redTiles set of red tile coordinates
     * @param greenPathTiles set of green path tile coordinates
     * @param polygon ordered list of red tiles forming the polygon
     * @return true if all tiles in the rectangle are valid
     */
    private static boolean isRectangleValidWithPolygon(Point corner1, Point corner2,
                                                        Set<Point> redTiles,
                                                        Set<Point> greenPathTiles,
                                                        List<Point> polygon) {
        int minX = Math.min(corner1.x, corner2.x);
        int maxX = Math.max(corner1.x, corner2.x);
        int minY = Math.min(corner1.y, corner2.y);
        int maxY = Math.max(corner1.y, corner2.y);

        // Check edge tiles first - if any edge is invalid, the whole rectangle is invalid
        // This provides early exit for large rectangles
        for (int x = minX; x <= maxX; x++) {
            if (!isPointValid(new Point(x, minY), redTiles, greenPathTiles, polygon)) {
                return false;
            }
            if (!isPointValid(new Point(x, maxY), redTiles, greenPathTiles, polygon)) {
                return false;
            }
        }
        for (int y = minY + 1; y < maxY; y++) {
            if (!isPointValid(new Point(minX, y), redTiles, greenPathTiles, polygon)) {
                return false;
            }
            if (!isPointValid(new Point(maxX, y), redTiles, greenPathTiles, polygon)) {
                return false;
            }
        }

        // Check interior tiles
        for (int x = minX + 1; x < maxX; x++) {
            for (int y = minY + 1; y < maxY; y++) {
                if (!isPointValid(new Point(x, y), redTiles, greenPathTiles, polygon)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if a single point is valid (red, green path, or inside polygon).
     * @param point the point to check
     * @param redTiles set of red tile coordinates
     * @param greenPathTiles set of green path tile coordinates
     * @param polygon ordered list of red tiles forming the polygon
     * @return true if the point is valid
     */
    private static boolean isPointValid(Point point, Set<Point> redTiles,
                                       Set<Point> greenPathTiles, List<Point> polygon) {
        return redTiles.contains(point) ||
               greenPathTiles.contains(point) ||
               isPointInsidePolygon(point, polygon);
    }
}

