package odogwudozilla.year2018.day6;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Chronal Coordinates (Advent of Code 2018 Day 6)
 * <p>
 * The device on your wrist beeps several times, and once again you feel like you're falling.
 * "Situation critical," the device announces. "Destination indeterminate. Chronal interference detected. Please specify new target coordinates."
 * The device then produces a list of coordinates (your puzzle input). Are they places it thinks are safe or dangerous? It recommends you check manual page 729. The Elves did not give you a manual.
 * If they're dangerous, maybe you can minimize the danger by finding the coordinate that gives the largest distance from the other points.
 * Using only the Manhattan distance, determine the area around each coordinate by counting the number of integer X,Y locations that are closest to that coordinate (and aren't tied in distance to any other coordinate).
 * Your goal is to find the size of the largest area that isn't infinite.
 *
 * Official puzzle URL: https://adventofcode.com/2018/day/6
 */
public class ChronalCoordinatesAOC2018Day6 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("src/main/resources/2018/day6/day6_puzzle_data.txt"));
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1 of the puzzle: Find the size of the largest area that isn't infinite.
     * @param input the list of coordinate strings
     * @return the size of the largest finite area
     */
    public static int solvePartOne(@NotNull List<String> input) {
        class Point {
            final int x, y;
            Point(int x, int y) { this.x = x; this.y = y; }
            @Override public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Point point = (Point) o;
                return x == point.x && y == point.y;
            }
            @Override public int hashCode() { return Objects.hash(x, y); }
        }

        List<Point> coords = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(",");
            coords.add(new Point(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())));
        }

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Point p : coords) {
            if (p.x < minX) minX = p.x;
            if (p.x > maxX) maxX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        Map<Point, Integer> area = new HashMap<>();
        Set<Point> infinite = new HashSet<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Point curr = new Point(x, y);
                int minDist = Integer.MAX_VALUE;
                Point closest = null;
                boolean tie = false;
                for (Point c : coords) {
                    int dist = Math.abs(c.x - x) + Math.abs(c.y - y);
                    if (dist < minDist) {
                        minDist = dist;
                        closest = c;
                        tie = false;
                    } else if (dist == minDist) {
                        tie = true;
                    }
                }
                if (!tie && closest != null) {
                    area.put(closest, area.getOrDefault(closest, 0) + 1);
                    if (x == minX || x == maxX || y == minY || y == maxY) {
                        infinite.add(closest);
                    }
                }
            }
        }

        int maxArea = 0;
        for (Map.Entry<Point, Integer> entry : area.entrySet()) {
            if (!infinite.contains(entry.getKey())) {
                if (entry.getValue() > maxArea) {
                    maxArea = entry.getValue();
                }
            }
        }
        return maxArea;
    }

    /**
     * Solves Part 2 of the puzzle: Find the size of the region containing all locations
     * which have a total distance to all given coordinates of less than 10000.
     * @param input the list of coordinate strings
     * @return the size of the region
     */
    public static int solvePartTwo(@NotNull List<String> input) {
        final int DISTANCE_LIMIT = 10000;
        class Point {
            final int x, y;
            Point(int x, int y) { this.x = x; this.y = y; }
        }
        List<Point> coords = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(",");
            coords.add(new Point(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())));
        }
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Point p : coords) {
            if (p.x < minX) minX = p.x;
            if (p.x > maxX) maxX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }
        int regionSize = 0;
        for (int x = minX - 100; x <= maxX + 100; x++) {
            for (int y = minY - 100; y <= maxY + 100; y++) {
                int totalDist = 0;
                for (Point c : coords) {
                    totalDist += Math.abs(c.x - x) + Math.abs(c.y - y);
                    if (totalDist >= DISTANCE_LIMIT) break;
                }
                if (totalDist < DISTANCE_LIMIT) {
                    regionSize++;
                }
            }
        }
        return regionSize;
    }
}

