package odogwudozilla.year2015.day9;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2015 - Day 9: All in a Single Night
 * <p>
 * Puzzle URL: https://adventofcode.com/2015/day/9
 */
public final class AllInASingleNightAOC2015Day9 {

    private static final String INPUT_FILE = "/2015/day9/day9_puzzle_data.txt";
    private static final String TO_DELIMITER = " to ";
    private static final String DISTANCE_DELIMITER = " = ";
    private static final int UNREACHABLE_DISTANCE = -1;

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
    public static String solvePartOne(List<String> input) {
        return solve(input, false);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public static String solvePartTwo(List<String> input) {
        return solve(input, true);
    }

    private static String solve(List<String> input, boolean findMax) {
        Map<String, Integer> cityIndex = new HashMap<>();
        List<RouteEdge> edges = new ArrayList<>();

        for (String rawLine : input) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }
            RouteEdge edge = parseEdge(line);
            cityIndex.computeIfAbsent(edge.from(), ignored -> cityIndex.size());
            cityIndex.computeIfAbsent(edge.to(), ignored -> cityIndex.size());
            edges.add(edge);
        }

        int cityCount = cityIndex.size();
        if (cityCount <= 1) {
            return "0";
        }

        int[][] distances = new int[cityCount][cityCount];
        for (int[] row : distances) {
            Arrays.fill(row, UNREACHABLE_DISTANCE);
        }

        for (RouteEdge edge : edges) {
            int fromIndex = cityIndex.get(edge.from());
            int toIndex = cityIndex.get(edge.to());
            distances[fromIndex][toIndex] = edge.distance();
            distances[toIndex][fromIndex] = edge.distance();
        }

        int[] routeOrder = new int[cityCount];
        for (int city = 0; city < cityCount; city++) {
            routeOrder[city] = city;
        }

        int[] bestDistance = {findMax ? Integer.MIN_VALUE : Integer.MAX_VALUE};
        permuteAndScore(routeOrder, 0, distances, bestDistance, findMax);
        return String.valueOf(bestDistance[0]);
    }

    /**
     * Parses one route line formatted as {@code A to B = D}.
     *
     * @param line input route line
     * @return parsed route edge
     */
    private static RouteEdge parseEdge(String line) {
        String[] routeAndDistance = line.split(DISTANCE_DELIMITER);
        String[] fromAndTo = routeAndDistance[0].split(TO_DELIMITER);
        int distance = Integer.parseInt(routeAndDistance[1]);
        return new RouteEdge(fromAndTo[0], fromAndTo[1], distance);
    }

    /**
     * Enumerates all city orders and updates the best route distance.
     *
     * @param order current permutation container
     * @param startIndex current permutation index
     * @param distances adjacency matrix of pairwise distances
     * @param bestDistance running best distance
     * @param findMax whether to find maximum distance instead of minimum
     */
    private static void permuteAndScore(int[] order, int startIndex, int[][] distances, int[] bestDistance, boolean findMax) {
        if (startIndex == order.length) {
            int currentDistance = routeDistance(order, distances);
            if (currentDistance == Integer.MAX_VALUE) {
                return;
            }
            if (findMax) {
                if (currentDistance > bestDistance[0]) {
                    bestDistance[0] = currentDistance;
                }
            } else {
                if (currentDistance < bestDistance[0]) {
                    bestDistance[0] = currentDistance;
                }
            }
            return;
        }

        for (int index = startIndex; index < order.length; index++) {
            swap(order, startIndex, index);
            permuteAndScore(order, startIndex + 1, distances, bestDistance, findMax);
            swap(order, startIndex, index);
        }
    }

    /**
     * Computes total route distance for one city visit order.
     *
     * @param order city permutation
     * @param distances adjacency matrix
     * @return total distance, or {@link Integer#MAX_VALUE} if a segment is missing
     */
    private static int routeDistance(int[] order, int[][] distances) {
        int totalDistance = 0;
        for (int index = 0; index < order.length - 1; index++) {
            int from = order[index];
            int to = order[index + 1];
            int segmentDistance = distances[from][to];
            if (segmentDistance == UNREACHABLE_DISTANCE) {
                return Integer.MAX_VALUE;
            }
            totalDistance += segmentDistance;
        }
        return totalDistance;
    }

    private static void swap(int[] values, int leftIndex, int rightIndex) {
        int temporary = values[leftIndex];
        values[leftIndex] = values[rightIndex];
        values[rightIndex] = temporary;
    }

    private record RouteEdge(String from, String to, int distance) {
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = AllInASingleNightAOC2015Day9.class.getResourceAsStream(INPUT_FILE)) {
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
