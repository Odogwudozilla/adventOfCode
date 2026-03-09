package odogwudozilla.year2019.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * --- Day 20: Donut Maze ---
 * You notice a strange pattern on the surface of Pluto and land nearby to get a closer look. Upon closer inspection, you realize you've come across one of the famous space-warping mazes of the long-lost Pluto civilization!
 *
 * This maze is shaped like a donut. Portals along the inner and outer edge of the donut can instantly teleport you from one side to the other. Every maze on Pluto has a start (the open tile next to AA) and an end (the open tile next to ZZ). Mazes on Pluto also have portals; when on an open tile next to one of these labels, a single step can take you to the other tile with the same label. (You can only walk on . tiles; labels and empty space are not traversable.)
 *
 * Official puzzle URL: https://adventofcode.com/2019/day/20
 */
public class DonutMazeAOC2019Day20 {
    private static final String INPUT_PATH = "src/main/resources/2019/day20/day20_puzzle_data.txt";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
        int partOne = solvePartOne(lines);
        System.out.println("Part 1: " + partOne);
        int partTwo = solvePartTwo(lines);
        System.out.println("Part 2: " + partTwo);
    }

    /**
     * Solves Part 1 of the Donut Maze puzzle.
     * @param lines The maze input as a list of strings
     * @return The minimum number of steps from AA to ZZ
     */
    public static int solvePartOne(List<String> lines) {
        // Parse the maze and portals
        Maze maze = Maze.parse(lines);
        return maze.shortestPath();
    }

    /**
     * Solves Part 2 of the Donut Maze puzzle (recursive maze).
     * @param lines The maze input as a list of strings
     * @return The minimum number of steps from AA to ZZ at the outermost layer
     */
    public static int solvePartTwo(List<String> lines) {
        Maze maze = Maze.parse(lines);
        return maze.shortestPathRecursive();
    }

    // Helper classes for maze parsing and BFS
    static class Maze {
        private final char[][] grid;
        private final Map<String, List<Point>> portals = new HashMap<>();
        private Point start;
        private Point end;

        private Maze(char[][] grid) {
            this.grid = grid;
        }

        static Maze parse(List<String> lines) {
            int height = lines.size();
            int width = lines.stream().mapToInt(String::length).max().orElse(0);
            char[][] grid = new char[height][width];
            for (int y = 0; y < height; y++) {
                String line = lines.get(y);
                for (int x = 0; x < width; x++) {
                    grid[y][x] = x < line.length() ? line.charAt(x) : ' ';
                }
            }
            Maze maze = new Maze(grid);
            maze.findPortals();
            return maze;
        }

        private void findPortals() {
            int height = grid.length;
            int width = grid[0].length;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (Character.isUpperCase(grid[y][x])) {
                        // Check right and down for portal labels
                        if (x + 1 < width && Character.isUpperCase(grid[y][x + 1])) {
                            String label = "" + grid[y][x] + grid[y][x + 1];
                            Point p = getPortalPoint(x, y, x + 2, y, x - 1, y);
                            if (p != null) addPortal(label, p);
                        }
                        if (y + 1 < height && Character.isUpperCase(grid[y + 1][x])) {
                            String label = "" + grid[y][x] + grid[y + 1][x];
                            Point p = getPortalPoint(x, y, x, y + 2, x, y - 1);
                            if (p != null) addPortal(label, p);
                        }
                    }
                }
            }
        }

        private Point getPortalPoint(int x1, int y1, int x2, int y2, int x3, int y3) {
            // Check after or before the label for an open tile
            if (inBounds(x2, y2) && grid[y2][x2] == '.') return new Point(x2, y2);
            if (inBounds(x3, y3) && grid[y3][x3] == '.') return new Point(x3, y3);
            return null;
        }

        private boolean inBounds(int x, int y) {
            return y >= 0 && y < grid.length && x >= 0 && x < grid[0].length;
        }

        private void addPortal(String label, Point p) {
            if ("AA".equals(label)) {
                start = p;
            } else if ("ZZ".equals(label)) {
                end = p;
            } else {
                portals.computeIfAbsent(label, k -> new ArrayList<>()).add(p);
            }
        }

        int shortestPath() {
            Queue<State> queue = new ArrayDeque<>();
            Set<Point> visited = new HashSet<>();
            queue.add(new State(start, 0));
            visited.add(start);
            while (!queue.isEmpty()) {
                State curr = queue.poll();
                if (curr.p.equals(end)) return curr.steps;
                for (Point next : getNeighbours(curr.p)) {
                    if (visited.add(next)) {
                        queue.add(new State(next, curr.steps + 1));
                    }
                }
            }
            return -1;
        }

        int shortestPathRecursive() {
            // BFS with (Point, level)
            Queue<RecursiveState> queue = new ArrayDeque<>();
            Set<RecursiveState> visited = new HashSet<>();
            queue.add(new RecursiveState(start, 0, 0));
            visited.add(new RecursiveState(start, 0, 0));
            while (!queue.isEmpty()) {
                RecursiveState curr = queue.poll();
                if (curr.p.equals(end) && curr.level == 0) return curr.steps;
                for (RecursiveState next : getNeighboursRecursive(curr)) {
                    if (visited.add(next)) {
                        queue.add(next);
                    }
                }
            }
            return -1;
        }

        private List<Point> getNeighbours(Point p) {
            List<Point> result = new ArrayList<>();
            int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
            for (int[] d : dirs) {
                int nx = p.x + d[0], ny = p.y + d[1];
                if (inBounds(nx, ny) && grid[ny][nx] == '.') {
                    result.add(new Point(nx, ny));
                }
            }
            // Check for portal
            for (Map.Entry<String, List<Point>> entry : portals.entrySet()) {
                List<Point> pts = entry.getValue();
                if (pts.size() == 2 && pts.contains(p)) {
                    Point other = pts.get(0).equals(p) ? pts.get(1) : pts.get(0);
                    result.add(other);
                }
            }
            return result;
        }

        private List<RecursiveState> getNeighboursRecursive(RecursiveState state) {
            List<RecursiveState> result = new ArrayList<>();
            int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
            for (int[] d : dirs) {
                int nx = state.p.x + d[0], ny = state.p.y + d[1];
                if (inBounds(nx, ny) && grid[ny][nx] == '.') {
                    result.add(new RecursiveState(new Point(nx, ny), state.level, state.steps + 1));
                }
            }
            // Check for portal
            for (Map.Entry<String, List<Point>> entry : portals.entrySet()) {
                List<Point> pts = entry.getValue();
                if (pts.size() == 2 && pts.contains(state.p)) {
                    String label = entry.getKey();
                    boolean isOuter = isOuterPortal(state.p);
                    if (isOuter && state.level == 0) continue;
                    if ("AA".equals(label) || "ZZ".equals(label)) continue;
                    Point other = pts.get(0).equals(state.p) ? pts.get(1) : pts.get(0);
                    int nextLevel = isOuter ? state.level - 1 : state.level + 1;
                    result.add(new RecursiveState(other, nextLevel, state.steps + 1));
                }
            }
            return result;
        }

        private boolean isOuterPortal(Point p) {
            int x = p.x, y = p.y;
            return x <= 2 || y <= 2 || x >= grid[0].length - 3 || y >= grid.length - 3;
        }
    }

    static class State {
        final Point p;
        final int steps;
        State(Point p, int steps) {
            this.p = p;
            this.steps = steps;
        }
    }

    static class RecursiveState {
        final Point p;
        final int level;
        final int steps;
        RecursiveState(Point p, int level, int steps) {
            this.p = p;
            this.level = level;
            this.steps = steps;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RecursiveState)) return false;
            RecursiveState that = (RecursiveState) o;
            return level == that.level && p.equals(that.p);
        }
        @Override
        public int hashCode() {
            return Objects.hash(p, level);
        }
    }

    static class Point {
        final int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
