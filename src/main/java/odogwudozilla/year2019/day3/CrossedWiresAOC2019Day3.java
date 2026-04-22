/**
 * Advent of Code 2019 Day 3: Crossed Wires
 * https://adventofcode.com/2019/day/3
 *
 * The gravity assist was successful, and you're well on your way to the Venus refuelling station. During the rush back on Earth, the fuel management system wasn't completely installed, so that's next on the priority list.
 *
 * Opening the front panel reveals a jumble of wires. Specifically, two wires are connected to a central port and extend outward on a grid. You trace the path each wire takes as it leaves the central port, one wire per line of text (your puzzle input).
 *
 * The wires twist and turn, but the two wires occasionally cross paths. To fix the circuit, you need to find the intersection point closest to the central port. Because the wires are on a grid, use the Manhattan distance for this measurement. While the wires do technically cross right at the central port where they both start, this point does not count, nor does a wire count as crossing with itself.
 *
 * What is the Manhattan distance from the central port to the closest intersection?
 */
package odogwudozilla.year2019.day3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

public class CrossedWiresAOC2019Day3 {
    private static final String INPUT_PATH = "src/main/resources/2019/day3/day3_puzzle_data.txt";

    public static void main(String[] args) {
        List<String> inputLines;
        try {
            inputLines = Files.readAllLines(Paths.get(INPUT_PATH));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }
        int partOneResult = solvePartOne(inputLines);
        System.out.println("Part 1 (Manhattan distance to closest intersection): " + partOneResult);
        int partTwoResult = solvePartTwo(inputLines);
        System.out.println("Part 2 (Fewest combined steps to an intersection): " + partTwoResult);
    }

    /**
     * Solves Part 1: Finds the Manhattan distance from the central port to the closest intersection.
     * @param inputLines the puzzle input lines
     * @return the Manhattan distance to the closest intersection
     */
    public static int solvePartOne(List<String> inputLines) {
        Set<Point> wire1Points = getWirePoints(inputLines.get(0));
        Set<Point> wire2Points = getWirePoints(inputLines.get(1));
        wire1Points.retainAll(wire2Points);
        int minDistance = Integer.MAX_VALUE;
        for (Point p : wire1Points) {
            int dist = Math.abs(p.x) + Math.abs(p.y);
            if (dist != 0 && dist < minDistance) {
                minDistance = dist;
            }
        }
        return minDistance;
    }

    /**
     * Solves Part 2: Finds the fewest combined steps the wires must take to reach an intersection.
     * @param inputLines the puzzle input lines
     * @return the fewest combined steps to an intersection
     */
    public static int solvePartTwo(List<String> inputLines) {
        Map<Point, Integer> wire1Steps = getWireSteps(inputLines.get(0));
        Map<Point, Integer> wire2Steps = getWireSteps(inputLines.get(1));
        int minSteps = Integer.MAX_VALUE;
        for (Point p : wire1Steps.keySet()) {
            if (wire2Steps.containsKey(p)) {
                int steps = wire1Steps.get(p) + wire2Steps.get(p);
                if (steps < minSteps && (p.x != 0 || p.y != 0)) {
                    minSteps = steps;
                }
            }
        }
        return minSteps;
    }

    private static Set<Point> getWirePoints(String wire) {
        Set<Point> points = new HashSet<>();
        int x = 0, y = 0;
        String[] moves = wire.split(",");
        for (String move : moves) {
            char dir = move.charAt(0);
            int len = Integer.parseInt(move.substring(1));
            for (int i = 0; i < len; i++) {
                switch (dir) {
                    case 'U': y++; break;
                    case 'D': y--; break;
                    case 'L': x--; break;
                    case 'R': x++; break;
                }
                points.add(new Point(x, y));
            }
        }
        return points;
    }

    private static Map<Point, Integer> getWireSteps(String wire) {
        Map<Point, Integer> stepsMap = new HashMap<>();
        int x = 0, y = 0, steps = 0;
        String[] moves = wire.split(",");
        for (String move : moves) {
            char dir = move.charAt(0);
            int len = Integer.parseInt(move.substring(1));
            for (int i = 0; i < len; i++) {
                switch (dir) {
                    case 'U': y++; break;
                    case 'D': y--; break;
                    case 'L': x--; break;
                    case 'R': x++; break;
                }
                steps++;
                Point p = new Point(x, y);
                stepsMap.putIfAbsent(p, steps);
            }
        }
        return stepsMap;
    }

    private static class Point {
        final int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
        @Override
        public int hashCode() { return Objects.hash(x, y); }
    }
}
