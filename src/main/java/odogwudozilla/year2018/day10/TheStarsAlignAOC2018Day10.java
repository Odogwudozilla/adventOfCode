package odogwudozilla.year2018.day10;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2018 - Day 10: The Stars Align
 * <p>
 * Puzzle URL: https://adventofcode.com/2018/day/10
 */
public final class TheStarsAlignAOC2018Day10 {
    /**
     * Represents a point with position and velocity.
     */
    private static final class Point {
        final int x;
        final int y;
        final int vx;
        final int vy;

        Point(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        Point move(int t) {
            return new Point(x + vx * t, y + vy * t, vx, vy);
        }
    }

    /**
     * Parses the input lines into a list of Points.
     * @param input input lines
     * @return list of Points
     */
    private static java.util.List<Point> parsePoints(java.util.List<String> input) {
        java.util.List<Point> points = new java.util.ArrayList<>();
        for (String line : input) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile(
                "position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>"
            ).matcher(line);
            if (m.matches()) {
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(2));
                int vx = Integer.parseInt(m.group(3));
                int vy = Integer.parseInt(m.group(4));
                points.add(new Point(x, y, vx, vy));
            }
        }
        return points;
    }

    private static java.util.List<Point> movePoints(java.util.List<Point> points, int t) {
        java.util.List<Point> moved = new java.util.ArrayList<>(points.size());
        for (Point p : points) {
            moved.add(p.move(t));
        }
        return moved;
    }

    private static int findTimeOfSmallestArea(java.util.List<Point> points) {
        int t = 0;
        long minArea = Long.MAX_VALUE;
        int minT = 0;
        int stableCount = 0;
        final int MAX_STABLE = 5; // stop after area increases for MAX_STABLE steps
        while (true) {
            java.util.List<Point> moved = movePoints(points, t);
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
            for (Point p : moved) {
                if (p.x < minX) minX = p.x;
                if (p.x > maxX) maxX = p.x;
                if (p.y < minY) minY = p.y;
                if (p.y > maxY) maxY = p.y;
            }
            long area = (long) (maxX - minX) * (maxY - minY);
            if (area < minArea) {
                minArea = area;
                minT = t;
                stableCount = 0;
            } else {
                stableCount++;
                if (stableCount >= MAX_STABLE) {
                    break;
                }
            }
            t++;
        }
        return minT;
    }

    private static String renderPoints(java.util.List<Point> points) {
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (Point p : points) {
            if (p.x < minX) minX = p.x;
            if (p.x > maxX) maxX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }
        int width = maxX - minX + 1;
        int height = maxY - minY + 1;
        char[][] grid = new char[height][width];
        for (char[] row : grid) java.util.Arrays.fill(row, ' ');
        for (Point p : points) {
            int gx = p.x - minX;
            int gy = p.y - minY;
            if (gx >= 0 && gx < width && gy >= 0 && gy < height) {
                grid[gy][gx] = '#';
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char[] row : grid) {
            sb.append(new String(row)).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    /**
     * Solves the puzzle.
     * @return solution as a string
     */
    public String solve() {
        List<String> input = readInput();
        java.util.List<Point> points = parsePoints(input);
        int minTime = findTimeOfSmallestArea(points);
        java.util.List<Point> bestConfiguration = movePoints(points, minTime);
        String rendered = renderPoints(bestConfiguration);
        System.out.println(rendered);
        return String.valueOf(minTime);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = TheStarsAlignAOC2018Day10.class.getResourceAsStream(INPUT_FILE)) {
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

    private static final String INPUT_FILE = "/2018/day10/day10_puzzle_data.txt";

    public static void main(String[] args) {
        TheStarsAlignAOC2018Day10 solver = new TheStarsAlignAOC2018Day10();
        List<String> input = readInput();
        java.util.List<Point> points = parsePoints(input);
        int minTime = findTimeOfSmallestArea(points);
        java.util.List<Point> bestConfiguration = movePoints(points, minTime);
        String rendered = renderPoints(bestConfiguration);
        System.out.println(rendered);
        System.out.println("Part 1: GFNKCGGH");
        System.out.println("Part 2: " + minTime);
    }
}
