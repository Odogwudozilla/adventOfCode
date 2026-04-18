package odogwudozilla.year2021.day5;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

/**
 * Hydrothermal Venture (Advent of Code 2021 Day 5)
 * <p>
 * You come across a field of hydrothermal vents on the ocean floor! These vents constantly produce large, opaque clouds, so it would be best to avoid them if possible.
 * <p>
 * They tend to form in lines; the submarine helpfully produces a list of nearby lines of vents (your puzzle input) for you to review.
 * <p>
 * Each line of vents is given as a line segment in the format x1,y1 -> x2,y2 where x1,y1 are the coordinates of one end the line segment and x2,y2 are the coordinates of the other end. These line segments include the points at both ends.
 * <p>
 * For now, only consider horizontal and vertical lines: lines where either x1 = x2 or y1 = y2.
 * <p>
 * Official puzzle URL: https://adventofcode.com/2021/day/5
 */
public class HydrothermalVentureAOC2021Day5 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("src/main/resources/2021/day5/day5_puzzle_data.txt"));
        int part1 = solvePartOne(input);
        System.out.println("Part 1: " + part1);
        int part2 = solvePartTwo(input);
        System.out.println("Part 2: " + part2);
    }

    /**
     * Solves Part 1: Counts the number of points where at least two horizontal or vertical lines overlap.
     * @param input the list of vent line definitions
     * @return the number of overlapping points
     */
    public static int solvePartOne(@NotNull List<String> input) {
        Map<String, Integer> grid = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split(" -> ");
            String[] start = parts[0].split(",");
            String[] end = parts[1].split(",");
            int x1 = Integer.parseInt(start[0]);
            int y1 = Integer.parseInt(start[1]);
            int x2 = Integer.parseInt(end[0]);
            int y2 = Integer.parseInt(end[1]);
            if (x1 == x2) {
                int minY = Math.min(y1, y2);
                int maxY = Math.max(y1, y2);
                for (int y = minY; y <= maxY; y++) {
                    String key = x1 + "," + y;
                    grid.put(key, grid.getOrDefault(key, 0) + 1);
                }
            } else if (y1 == y2) {
                int minX = Math.min(x1, x2);
                int maxX = Math.max(x1, x2);
                for (int x = minX; x <= maxX; x++) {
                    String key = x + "," + y1;
                    grid.put(key, grid.getOrDefault(key, 0) + 1);
                }
            }
        }
        int count = 0;
        for (int v : grid.values()) {
            if (v >= 2) count++;
        }
        return count;
    }

    /**
     * Solves Part 2: Counts the number of points where at least two lines (horizontal, vertical, or 45-degree diagonal) overlap.
     * @param input the list of vent line definitions
     * @return the number of overlapping points
     */
    public static int solvePartTwo(@NotNull List<String> input) {
        Map<String, Integer> grid = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split(" -> ");
            String[] start = parts[0].split(",");
            String[] end = parts[1].split(",");
            int x1 = Integer.parseInt(start[0]);
            int y1 = Integer.parseInt(start[1]);
            int x2 = Integer.parseInt(end[0]);
            int y2 = Integer.parseInt(end[1]);
            int dx = Integer.compare(x2, x1);
            int dy = Integer.compare(y2, y1);
            int x = x1, y = y1;
            while (true) {
                String key = x + "," + y;
                grid.put(key, grid.getOrDefault(key, 0) + 1);
                if (x == x2 && y == y2) break;
                x += dx;
                y += dy;
            }
        }
        int count = 0;
        for (int v : grid.values()) {
            if (v >= 2) count++;
        }
        return count;
    }
}
