package odogwudozilla.year2023.day17;

/**
 * Clumsy Crucible (Advent of Code 2023 Day 17)
 * <p>
 * The goal is to direct a top-heavy crucible from the lava pool (top-left) to the machine parts factory (bottom-right) on a city grid, minimizing heat loss. Each block has a heat loss value, and the crucible can move at most three blocks in a straight line before turning left or right (no reversing). The input is a grid of digits representing heat loss per block. The solution must find the path with the least heat loss, following the movement constraints.
 * <p>
 * Official puzzle URL: https://adventofcode.com/2023/day/17
 */
public class ClumsyCrucibleAOC2023Day17 {
    /**
     * Reads the puzzle input from the resource file and returns it as a 2D int array.
     *
     * @return the heat loss grid
     */
    private static int[][] readInput() {
        java.util.List<String> lines;
        try {
            lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get("src/main/resources/2023/day17/day17_puzzle_data.txt"));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read input file", e);
        }
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = lines.get(i).charAt(j) - '0';
            }
        }
        return grid;
    }

    /**
     * Solves Part 1: Finds the least heat loss path from top-left to bottom-right, moving at most 3 blocks straight before turning.
     *
     * @param grid the heat loss grid
     * @return the minimum heat loss
     */
    public static int solvePartOne(int[][] grid) {
        // Implementation based on Dijkstra's algorithm with direction and straight-move constraints
        record State(int x, int y, int dir, int straight, int cost) {
        }
        int rows = grid.length, cols = grid[0].length;
        int[] dx = {0, 1, 0, -1}; // right, down, left, up
        int[] dy = {1, 0, -1, 0};
        java.util.PriorityQueue<State> pq = new java.util.PriorityQueue<>(java.util.Comparator.comparingInt(s -> s.cost));
        boolean[][][][] visited = new boolean[rows][cols][4][4];
        for (int d = 0; d < 4; d++) {
            pq.add(new State(0, 0, d, 0, 0));
        }
        while (!pq.isEmpty()) {
            State s = pq.poll();
            if (s.x == rows - 1 && s.y == cols - 1) return s.cost;
            if (visited[s.x][s.y][s.dir][s.straight]) continue;
            visited[s.x][s.y][s.dir][s.straight] = true;
            // Move straight
            if (s.straight < 3) {
                int nx = s.x + dx[s.dir], ny = s.y + dy[s.dir];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                    pq.add(new State(nx, ny, s.dir, s.straight + 1, s.cost + grid[nx][ny]));
                }
            }
            // Turn left or right
            for (int turn : new int[]{-1, 1}) {
                int ndir = (s.dir + turn + 4) % 4;
                int nx = s.x + dx[ndir], ny = s.y + dy[ndir];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                    pq.add(new State(nx, ny, ndir, 1, s.cost + grid[nx][ny]));
                }
            }
        }
        throw new IllegalStateException("No path found");
    }

    /**
     * Solves Part 2: Finds the least heat loss path for the ultra crucible, which must move at least 4 and at most 10 blocks straight before turning.
     *
     * @param grid the heat loss grid
     * @return the minimum heat loss for the ultra crucible
     */
    public static int solvePartTwo(int[][] grid) {
        // Dijkstra's algorithm with minStraight=4, maxStraight=10
        record State(int x, int y, int dir, int straight, int cost) {
        }
        int rows = grid.length, cols = grid[0].length;
        int[] dx = {0, 1, 0, -1}; // right, down, left, up
        int[] dy = {1, 0, -1, 0};
        java.util.PriorityQueue<State> pq = new java.util.PriorityQueue<>(java.util.Comparator.comparingInt(s -> s.cost));
        boolean[][][][] visited = new boolean[rows][cols][4][11];
        for (int d = 0; d < 4; d++) {
            pq.add(new State(0, 0, d, 0, 0));
        }
        while (!pq.isEmpty()) {
            State s = pq.poll();
            if (s.x == rows - 1 && s.y == cols - 1 && s.straight >= 4) return s.cost;
            if (visited[s.x][s.y][s.dir][s.straight]) continue;
            visited[s.x][s.y][s.dir][s.straight] = true;
            // Move straight if not exceeding maxStraight
            if (s.straight < 10) {
                int nx = s.x + dx[s.dir], ny = s.y + dy[s.dir];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                    pq.add(new State(nx, ny, s.dir, s.straight + 1, s.cost + grid[nx][ny]));
                }
            }
            // Only turn if at least 4 straight moves
            if (s.straight >= 4) {
                for (int turn : new int[]{-1, 1}) {
                    int ndir = (s.dir + turn + 4) % 4;
                    int nx = s.x + dx[ndir], ny = s.y + dy[ndir];
                    if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                        pq.add(new State(nx, ny, ndir, 1, s.cost + grid[nx][ny]));
                    }
                }
            }
        }
        throw new IllegalStateException("No path found");
    }

    public static void main(String[] args) {
        int[][] grid = readInput();
        int part1 = solvePartOne(grid);
        System.out.println("Part 1: " + part1);
        int part2 = solvePartTwo(grid);
        System.out.println("Part 2: " + part2);
    }
    // TODO: Implement solvePartTwo method
}
