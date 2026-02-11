package odogwudozilla.year2017.day14;

/**
 * Advent of Code 2017 Day 14: Disk Defragmentation
 *
 * Summary: The puzzle involves a 128x128 grid representing a disk, where each square is either free or used. The state of the grid is determined by the bits in a sequence of knot hashes, each generated from the puzzle input key string and a row number. The task is to determine how many squares are used across the entire grid.
 *
 * Official puzzle URL: https://adventofcode.com/2017/day/14
 */
public class DiskDefragmentationAOC2017Day14 {
    public static void main(String[] args) throws Exception {
        // Read the puzzle input key string from the resource file
        String keyString = java.nio.file.Files.readAllLines(
            java.nio.file.Paths.get("src/main/resources/2017/day14/day14_puzzle_data.txt")
        ).get(0).trim();

        int usedSquares = solvePartOne(keyString);
        System.out.println("Part 1: Used squares = " + usedSquares);

        int regions = solvePartTwo(keyString);
        System.out.println("Part 2: Number of regions = " + regions);
    }

    /**
     * Solves Part 1: Counts the number of used squares in the 128x128 grid.
     * @param keyString The puzzle input key string.
     * @return The number of used squares.
     */
    public static int solvePartOne(String keyString) {
        final int GRID_SIZE = 128;
        int usedCount = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            String hashInput = keyString + "-" + row;
            String knotHash = knotHash(hashInput);
            StringBuilder binary = new StringBuilder();
            for (char c : knotHash.toCharArray()) {
                int value = Integer.parseInt(String.valueOf(c), 16);
                String bin = String.format("%4s", Integer.toBinaryString(value)).replace(' ', '0');
                binary.append(bin);
            }
            for (char bit : binary.toString().toCharArray()) {
                if (bit == '1') usedCount++;
            }
        }
        return usedCount;
    }

    /**
     * Computes the Knot Hash for a given input string.
     * @param input The input string.
     * @return The 32-character hexadecimal Knot Hash.
     */
    public static String knotHash(String input) {
        final int LIST_SIZE = 256;
        int[] list = new int[LIST_SIZE];
        for (int i = 0; i < LIST_SIZE; i++) list[i] = i;
        int[] suffix = {17, 31, 73, 47, 23};
        java.util.List<Integer> lengths = new java.util.ArrayList<>();
        for (char c : input.toCharArray()) lengths.add((int) c);
        for (int s : suffix) lengths.add(s);
        int pos = 0, skip = 0;
        for (int round = 0; round < 64; round++) {
            for (int len : lengths) {
                for (int i = 0; i < len / 2; i++) {
                    int a = (pos + i) % LIST_SIZE;
                    int b = (pos + len - 1 - i) % LIST_SIZE;
                    int tmp = list[a];
                    list[a] = list[b];
                    list[b] = tmp;
                }
                pos = (pos + len + skip) % LIST_SIZE;
                skip++;
            }
        }
        int[] dense = new int[16];
        for (int i = 0; i < 16; i++) {
            int x = list[i * 16];
            for (int j = 1; j < 16; j++) x ^= list[i * 16 + j];
            dense[i] = x;
        }
        StringBuilder sb = new StringBuilder();
        for (int d : dense) sb.append(String.format("%02x", d));
        return sb.toString();
    }

    /**
     * Solves Part 2: Counts the number of regions of adjacent used squares (not including diagonals).
     * @param keyString The puzzle input key string.
     * @return The number of regions.
     */
    public static int solvePartTwo(String keyString) {
        final int GRID_SIZE = 128;
        boolean[][] grid = new boolean[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            String hashInput = keyString + "-" + row;
            String knotHash = knotHash(hashInput);
            StringBuilder binary = new StringBuilder();
            for (char c : knotHash.toCharArray()) {
                int value = Integer.parseInt(String.valueOf(c), 16);
                String bin = String.format("%4s", Integer.toBinaryString(value)).replace(' ', '0');
                binary.append(bin);
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = binary.charAt(col) == '1';
            }
        }
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
        int regions = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] && !visited[row][col]) {
                    markRegion(grid, visited, row, col);
                    regions++;
                }
            }
        }
        return regions;
    }

    /**
     * Marks all squares in a region as visited using DFS.
     * @param grid The grid of used squares.
     * @param visited The visited marker grid.
     * @param row The starting row.
     * @param col The starting column.
     */
    private static void markRegion(boolean[][] grid, boolean[][] visited, int row, int col) {
        final int GRID_SIZE = 128;
        java.util.Deque<int[]> stack = new java.util.ArrayDeque<>();
        stack.push(new int[]{row, col});
        while (!stack.isEmpty()) {
            int[] pos = stack.pop();
            int r = pos[0], c = pos[1];
            if (r < 0 || r >= GRID_SIZE || c < 0 || c >= GRID_SIZE) continue;
            if (!grid[r][c] || visited[r][c]) continue;
            visited[r][c] = true;
            stack.push(new int[]{r - 1, c}); // up
            stack.push(new int[]{r + 1, c}); // down
            stack.push(new int[]{r, c - 1}); // left
            stack.push(new int[]{r, c + 1}); // right
        }
    }
}
