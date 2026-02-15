package odogwudozilla.year2016.day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Grid Computing (Advent of Code 2016 Day 22)
 *
 * You gain access to a massive storage cluster arranged in a grid; each storage node is only connected to the four nodes directly adjacent to it (three if the node is on an edge, two if it's in a corner).
 *
 * You can directly access data only on node /dev/grid/node-x0-y0, but you can perform some limited actions on the other nodes:
 *
 * - You can get the disk usage of all nodes (via df). The result of doing this is in your puzzle input.
 * - You can instruct a node to move (not copy) all of its data to an adjacent node (if the destination node has enough space to receive the data). The sending node is left empty after this operation.
 * - Nodes are named by their position: the node named node-x10-y10 is adjacent to nodes node-x9-y10, node-x11-y10, node-x10-y9, and node-x10-y11.
 *
 * Before you begin, you need to understand the arrangement of data on these nodes. Even though you can only move data between directly connected nodes, you're going to need to rearrange a lot of the data to get access to the data you need. Therefore, you need to work out how you might be able to shift data around.
 *
 * To do this, you'd like to count the number of viable pairs of nodes. A viable pair is any two nodes (A,B), regardless of whether they are directly connected, such that:
 *
 * - Node A is not empty (its Used is not zero).
 * - Nodes A and B are not the same node.
 * - The data on node A (its Used) would fit on node B (its Avail).
 *
 * Official puzzle link: https://adventofcode.com/2016/day/22
 */
public class GridComputingAOC2016Day22 {
    private static final String INPUT_PATH = "src/main/resources/2016/day22/day22_puzzle_data.txt";

    public static void main(String[] args) throws IOException {
        List<String> inputLines = Files.readAllLines(Paths.get(INPUT_PATH));
        System.out.println("solvePartOne - Number of viable pairs: " + solvePartOne(inputLines));
        System.out.println("solvePartTwo - Steps to move goal data: " + solvePartTwo(inputLines));
    }

    /**
     * Solves Part 1: Counts the number of viable pairs of nodes.
     * @param inputLines The input lines from the puzzle data file
     * @return The number of viable pairs
     */
    public static int solvePartOne(List<String> inputLines) {
        List<Node> nodes = parseNodes(inputLines);
        int viablePairs = 0;
        for (Node a : nodes) {
            if (a.used == 0) continue;
            for (Node b : nodes) {
                if (a == b) continue;
                if (a.used <= b.avail) {
                    viablePairs++;
                }
            }
        }
        return viablePairs;
    }

    /**
     * Solves Part 2: Computes the minimum steps to move the goal data to node-x0-y0.
     * @param inputLines The input lines from the puzzle data file
     * @return The number of steps to move the goal data
     */
    public static int solvePartTwo(List<String> inputLines) {
        List<Node> nodes = parseNodes(inputLines);
        // Find grid dimensions
        int maxX = 0, maxY = 0;
        for (Node n : nodes) {
            if (n.x > maxX) maxX = n.x;
            if (n.y > maxY) maxY = n.y;
        }
        // Build grid
        Node[][] grid = new Node[maxY + 1][maxX + 1];
        for (Node n : nodes) {
            grid[n.y][n.x] = n;
        }
        // Identify the empty node
        Node empty = null;
        for (Node n : nodes) {
            if (n.used == 0) {
                empty = n;
                break;
            }
        }
        // Identify walls (nodes too large to move data into)
        int wallThreshold = 0;
        for (Node n : nodes) {
            if (n.used > wallThreshold) wallThreshold = n.used;
        }
        wallThreshold = Math.max(wallThreshold / 2, 100); // Heuristic
        // Find the goal data position (top-right)
        int goalX = maxX, goalY = 0;
        // Calculate steps to move empty node next to goal data
        int steps = 0;
        // BFS to move empty node to (goalY, goalX-1)
        steps += bfs(empty.x, empty.y, goalX - 1, goalY, grid, wallThreshold);
        // Now, move the goal data left to (0,0): each move takes 5 steps (empty circles around goal)
        steps += (goalX - 1) * 5 + 1;
        return steps;
    }

    /**
     * Parses the node information from the input lines.
     * @param inputLines The input lines from the puzzle data file
     * @return List of Node objects
     */
    private static List<Node> parseNodes(List<String> inputLines) {
        List<Node> nodes = new ArrayList<>();
        for (String line : inputLines) {
            if (!line.startsWith("/dev/grid/")) continue;
            String[] parts = line.trim().split("\\s+");
            String[] pos = parts[0].split("-");
            int x = Integer.parseInt(pos[1].substring(1));
            int y = Integer.parseInt(pos[2].substring(1));
            int size = Integer.parseInt(parts[1].replace("T", ""));
            int used = Integer.parseInt(parts[2].replace("T", ""));
            int avail = Integer.parseInt(parts[3].replace("T", ""));
            nodes.add(new Node(x, y, size, used, avail));
        }
        return nodes;
    }

    /**
     * BFS to find shortest path for empty node to target, avoiding walls.
     */
    private static int bfs(int startX, int startY, int targetX, int targetY, Node[][] grid, int wallThreshold) {
        int maxY = grid.length;
        int maxX = grid[0].length;
        boolean[][] visited = new boolean[maxY][maxX];
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        queue.add(new int[]{startX, startY, 0});
        visited[startY][startX] = true;
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0], y = curr[1], dist = curr[2];
            if (x == targetX && y == targetY) return dist;
            for (int[] d : dirs) {
                int nx = x + d[0], ny = y + d[1];
                if (nx >= 0 && nx < maxX && ny >= 0 && ny < maxY && !visited[ny][nx]) {
                    Node n = grid[ny][nx];
                    if (n.used <= wallThreshold) {
                        visited[ny][nx] = true;
                        queue.add(new int[]{nx, ny, dist + 1});
                    }
                }
            }
        }
        return Integer.MAX_VALUE; // Should not happen
    }

    /**
         * Node class representing a storage node in the grid.
         */
        private record Node(int x, int y, int size, int used, int avail) {
    }
}
