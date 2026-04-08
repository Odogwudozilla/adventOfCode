package odogwudozilla.year2017.day3;

/**
 * Spiral Memory (Advent of Code 2017 Day 3)
 *
 * You come across an experimental new kind of memory stored on an infinite two-dimensional grid.
 * Each square on the grid is allocated in a spiral pattern starting at a location marked 1 and then counting up while spiraling outward.
 * The shortest path from any square to the access port (square 1) is the Manhattan Distance.
 *
 * Puzzle link: https://adventofcode.com/2017/day/3
 */
public class SpiralMemoryAOC2017Day3 {
    public static void main(String[] args) {
        // Read the puzzle input from the resource file
        String inputPath = "src/main/resources/2017/day3/day3_puzzle_data.txt";
        int puzzleInput = 0;
        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(inputPath));
            puzzleInput = Integer.parseInt(lines.get(0).trim());
        } catch (Exception e) {
            System.err.println("Error reading puzzle input: " + e.getMessage());
            return;
        }
        int part1 = solvePartOne(puzzleInput);
        System.out.println("Part 1: " + part1);
        int part2 = solvePartTwo(puzzleInput);
        System.out.println("Part 2: " + part2);
    }

    /**
     * Solves Part 1: Computes the Manhattan Distance from the input square to the access port (square 1).
     * @param n the square number
     * @return the Manhattan Distance
     */
    public static int solvePartOne(int n) {
        if (n == 1) return 0;
        int layer = 0;
        while ((2 * layer + 1) * (2 * layer + 1) < n) {
            layer++;
        }
        int sideLen = 2 * layer + 1;
        int maxVal = sideLen * sideLen;
        // The axis positions are maxVal - layer - k*(sideLen-1) for k=0..3
        int minDist = Integer.MAX_VALUE;
        for (int k = 0; k < 4; k++) {
            int axis = maxVal - layer - k * (sideLen - 1);
            int dist = Math.abs(n - axis);
            if (dist < minDist) minDist = dist;
        }
        return layer + minDist;
    }

    /**
     * Solves Part 2: Finds the first value written that is larger than the puzzle input.
     * @param input the puzzle input
     * @return the first value larger than input
     */
    public static int solvePartTwo(int input) {
        java.util.Map<String, Integer> grid = new java.util.HashMap<>();
        int x = 0, y = 0;
        grid.put("0,0", 1);
        int[][] directions = { {1,0}, {0,1}, {-1,0}, {0,-1} };
        int steps = 1;
        int dir = 0;
        while (true) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < steps; j++) {
                    x += directions[dir][0];
                    y += directions[dir][1];
                    int sum = 0;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) continue;
                            sum += grid.getOrDefault((x + dx) + "," + (y + dy), 0);
                        }
                    }
                    if (sum > input) return sum;
                    grid.put(x + "," + y, sum);
                }
                dir = (dir + 1) % 4;
            }
            steps++;
        }
    }
}
