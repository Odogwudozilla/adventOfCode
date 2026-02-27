package odogwudozilla.year2016.day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Advent of Code 2016 - Day 17: Two Steps Forward
 *
 * This puzzle involves navigating through a 4x4 grid of rooms where doors are opened or closed
 * based on MD5 hashes of a passcode combined with the path taken. The goal is to find paths
 * from the top-left room to the bottom-right vault room.
 *
 * Part 1: Find the shortest path to reach the vault.
 * Part 2: Find the length of the longest path to reach the vault.
 *
 * Puzzle URL: https://adventofcode.com/2016/day/17
 */
public class TwoStepsForwardAOC2016Day17 {

    private static final int GRID_SIZE = 4;
    private static final int TARGET_X = 3;
    private static final int TARGET_Y = 3;

    // Direction mappings: Up, Down, Left, Right
    private static final char[] DIRECTIONS = {'U', 'D', 'L', 'R'};
    private static final int[] DX = {0, 0, -1, 1};
    private static final int[] DY = {-1, 1, 0, 0};

    public static void main(String[] args) {
        try {
            String passcode = readPasscode();

            System.out.println("=== Advent of Code 2016 - Day 17: Two Steps Forward ===\n");
            System.out.println("Passcode: " + passcode);

            String shortestPath = solvePartOne(passcode);
            System.out.println("\n--- Part 1 ---");
            System.out.println("Shortest path to vault: " + shortestPath);

            int longestPathLength = solvePartTwo(passcode);
            System.out.println("\n--- Part 2 ---");
            System.out.println("Length of longest path: " + longestPathLength);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solves Part 1: Finds the shortest path from top-left to bottom-right vault.
     * Uses BFS to explore all valid paths and returns the first path that reaches the vault.
     * @param passcode the vault passcode
     * @return the shortest path as a string of directions (U, D, L, R)
     */
    private static String solvePartOne(String passcode) {
        Queue<State> queue = new ArrayDeque<>();
        queue.offer(new State(0, 0, ""));

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Check if we've reached the vault
            if (current.x == TARGET_X && current.y == TARGET_Y) {
                return current.path;
            }

            // Get open doors for current position
            String hash = getMD5Hash(passcode + current.path);

            // Try each direction
            for (int i = 0; i < DIRECTIONS.length; i++) {
                if (isDoorOpen(hash.charAt(i))) {
                    int newX = current.x + DX[i];
                    int newY = current.y + DY[i];

                    // Check if new position is within bounds
                    if (isInBounds(newX, newY)) {
                        String newPath = current.path + DIRECTIONS[i];
                        queue.offer(new State(newX, newY, newPath));
                    }
                }
            }
        }

        return "No path found";
    }

    /**
     * Solves Part 2: Finds the length of the longest path to the vault.
     * Uses BFS to explore all possible paths and tracks the maximum length.
     * @param passcode the vault passcode
     * @return the length of the longest path
     */
    private static int solvePartTwo(String passcode) {
        Queue<State> queue = new ArrayDeque<>();
        queue.offer(new State(0, 0, ""));
        int maxLength = 0;

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Check if we've reached the vault
            if (current.x == TARGET_X && current.y == TARGET_Y) {
                maxLength = Math.max(maxLength, current.path.length());
                continue; // Don't explore further from vault
            }

            // Get open doors for current position
            String hash = getMD5Hash(passcode + current.path);

            // Try each direction
            for (int i = 0; i < DIRECTIONS.length; i++) {
                if (isDoorOpen(hash.charAt(i))) {
                    int newX = current.x + DX[i];
                    int newY = current.y + DY[i];

                    // Check if new position is within bounds
                    if (isInBounds(newX, newY)) {
                        String newPath = current.path + DIRECTIONS[i];
                        queue.offer(new State(newX, newY, newPath));
                    }
                }
            }
        }

        return maxLength;
    }

    /**
     * Checks if a door is open based on the hash character.
     * Doors are open if the character is b, c, d, e, or f.
     * @param hashChar the hash character representing the door
     * @return true if the door is open, false otherwise
     */
    private static boolean isDoorOpen(char hashChar) {
        return hashChar >= 'b' && hashChar <= 'f';
    }

    /**
     * Checks if coordinates are within the grid bounds.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if coordinates are within bounds, false otherwise
     */
    private static boolean isInBounds(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }

    /**
     * Computes the MD5 hash of the input string.
     * @param input the string to hash
     * @return the hexadecimal MD5 hash
     */
    private static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * Reads the passcode from the puzzle data file.
     * @return the passcode as a string
     * @throws IOException if file reading fails
     */
    private static String readPasscode() throws IOException {
        Path filePath = Path.of("src/main/resources/2016/day17/day17_puzzle_data.txt");
        return Files.readString(filePath).trim();
    }

    /**
     * Represents a state in the search space: current position and path taken.
     */
    private static class State {
        int x;
        int y;
        String path;

        State(int x, int y, String path) {
            this.x = x;
            this.y = y;
            this.path = path;
        }
    }
}

