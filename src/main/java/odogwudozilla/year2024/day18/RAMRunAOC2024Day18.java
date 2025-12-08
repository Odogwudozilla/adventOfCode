package odogwudozilla.year2024.day18;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Advent of Code 2024 - Day 18: RAM Run
 *
 * You're inside a computer at the North Pole, and bytes are falling into memory!
 * Navigate from (0,0) to (70,70) while avoiding corrupted memory locations.
 *
 * Part 1: Find the minimum number of steps to reach the exit after the first 1024 bytes fall.
 * Part 2: Find the coordinates of the first byte that blocks the path to the exit.
 *
 * Puzzle link: https://adventofcode.com/2024/day/18
 */
public class RAMRunAOC2024Day18 {

    private static final int GRID_SIZE = 71;
    private static final int BYTES_TO_SIMULATE = 1024;
    private static final String INPUT_FILE = "/2024/day18/day18_puzzle_data.txt";

    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public static void main(String[] args) {
        try {
            List<Coordinate> fallingBytes = readInput();

            // Part 1: Simulate first 1024 bytes and find shortest path
            int partOneResult = solvePartOne(fallingBytes);
            System.out.println("Part 1 - Minimum steps to reach exit: " + partOneResult);

            // Part 2: Find the first byte that blocks the path
            Coordinate partTwoResult = solvePartTwo(fallingBytes);
            System.out.println("Part 2 - First blocking byte: " + partTwoResult.x + "," + partTwoResult.y);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solves Part 1 by simulating the first 1024 bytes and finding the shortest path.
     * @param fallingBytes list of all falling byte coordinates
     * @return minimum number of steps to reach the exit
     */
    private static int solvePartOne(@NotNull List<Coordinate> fallingBytes) {
        Set<Coordinate> corrupted = new HashSet<>();

        // Simulate first 1024 bytes falling
        for (int i = 0; i < Math.min(BYTES_TO_SIMULATE, fallingBytes.size()); i++) {
            corrupted.add(fallingBytes.get(i));
        }

        return findShortestPath(corrupted);
    }

    /**
     * Solves Part 2 by finding the first byte that blocks the path to the exit.
     * Uses binary search to efficiently find the blocking byte.
     * @param fallingBytes list of all falling byte coordinates
     * @return the coordinate of the first byte that blocks the path
     */
    @NotNull
    private static Coordinate solvePartTwo(@NotNull List<Coordinate> fallingBytes) {
        int left = BYTES_TO_SIMULATE;
        int right = fallingBytes.size() - 1;

        // Binary search to find the first blocking byte
        while (left < right) {
            int mid = left + (right - left) / 2;

            Set<Coordinate> corrupted = new HashSet<>();
            for (int i = 0; i <= mid; i++) {
                corrupted.add(fallingBytes.get(i));
            }

            if (findShortestPath(corrupted) == -1) {
                // Path is blocked at mid, search left half
                right = mid;
            } else {
                // Path exists at mid, search right half
                left = mid + 1;
            }
        }

        return fallingBytes.get(left);
    }

    /**
     * Finds the shortest path from (0,0) to (70,70) using BFS.
     * @param corrupted set of corrupted coordinates to avoid
     * @return length of shortest path, or -1 if no path exists
     */
    private static int findShortestPath(@NotNull Set<Coordinate> corrupted) {
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(GRID_SIZE - 1, GRID_SIZE - 1);

        if (corrupted.contains(start) || corrupted.contains(end)) {
            return -1;
        }

        Queue<State> queue = new ArrayDeque<>();
        Set<Coordinate> visited = new HashSet<>();

        queue.offer(new State(start, 0));
        visited.add(start);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.position.equals(end)) {
                return current.steps;
            }

            // Try all four directions
            for (int[] direction : DIRECTIONS) {
                int newX = current.position.x + direction[0];
                int newY = current.position.y + direction[1];

                // Check bounds
                if (newX < 0 || newX >= GRID_SIZE || newY < 0 || newY >= GRID_SIZE) {
                    continue;
                }

                Coordinate nextPos = new Coordinate(newX, newY);

                // Check if not corrupted and not visited
                if (!corrupted.contains(nextPos) && !visited.contains(nextPos)) {
                    visited.add(nextPos);
                    queue.offer(new State(nextPos, current.steps + 1));
                }
            }
        }

        return -1; // No path found
    }

    /**
     * Reads the input file and parses falling byte coordinates.
     * @return list of coordinates where bytes will fall
     */
    @NotNull
    private static List<Coordinate> readInput() throws IOException {
        List<Coordinate> coordinates = new ArrayList<>();

        try (InputStream inputStream = RAMRunAOC2024Day18.class.getResourceAsStream(INPUT_FILE)) {
            if (inputStream == null) {
                throw new IOException("Input file not found: " + INPUT_FILE);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split(",");
                        int x = Integer.parseInt(parts[0].trim());
                        int y = Integer.parseInt(parts[1].trim());
                        coordinates.add(new Coordinate(x, y));
                    }
                }
            }
        }

        return coordinates;
    }

    /**
     * Represents a coordinate in the memory space.
     */
    private static class Coordinate {
        final int x;
        final int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x && y == that.y;
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

    /**
     * Represents a state in the BFS search.
     */
    private static class State {
        final Coordinate position;
        final int steps;

        State(Coordinate position, int steps) {
            this.position = position;
            this.steps = steps;
        }
    }
}

