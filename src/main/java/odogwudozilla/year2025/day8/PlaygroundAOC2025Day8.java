package odogwudozilla.year2025.day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Advent of Code 2025 Day 8: Playground
 * <p>
 * The Elves are setting up a Christmas decoration project with suspended electrical junction boxes.
 * They want to connect junction boxes with strings of lights, focusing on the closest pairs first.
 * <p>
 * Part 1: After making 1000 connections, find the product of the sizes of the three largest circuits.
 * <p>
 * Part 2: Continue connecting until all junction boxes form a single circuit. Find the last connection
 * that unifies everything and multiply the X coordinates of those two junction boxes.
 * <p>
 * This puzzle uses a Union-Find (Disjoint Set Union) data structure to efficiently track which
 * junction boxes are connected in the same circuit as we make connections based on Euclidean distance.
 * <p>
 * URL: https://adventofcode.com/2025/day/8
 */
public class PlaygroundAOC2025Day8 {

    private static final String INPUT_FILE = "src/main/resources/2025/day8/day8_puzzle_data.txt";
    private static final int CONNECTIONS_TO_MAKE = 1000;

    /**
     * Represents a junction box with 3D coordinates.
     */
    static class JunctionBox {
        final int x;
        final int y;
        final int z;
        final int index;

        JunctionBox(int x, int y, int z, int index) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.index = index;
        }

        /**
         * Calculates the squared Euclidean distance to another junction box.
         * We use squared distance to avoid floating point arithmetic.
         * @param other the other junction box
         * @return the squared distance
         */
        double distanceSquared(JunctionBox other) {
            long dx = (long) this.x - other.x;
            long dy = (long) this.y - other.y;
            long dz = (long) this.z - other.z;
            return dx * dx + dy * dy + dz * dz;
        }
    }

    /**
     * Represents a potential connection between two junction boxes.
     */
    static class Connection implements Comparable<Connection> {
        final int box1;
        final int box2;
        final double distanceSquared;

        Connection(int box1, int box2, double distanceSquared) {
            this.box1 = box1;
            this.box2 = box2;
            this.distanceSquared = distanceSquared;
        }

        @Override
        public int compareTo(Connection other) {
            return Double.compare(this.distanceSquared, other.distanceSquared);
        }
    }

    /**
     * Union-Find data structure to track connected components (circuits).
     */
    static class UnionFind {
        private final int[] parent;
        private final int[] size;

        UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        /**
         * Finds the root of the set containing element x with path compression.
         * @param x the element
         * @return the root of the set
         */
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        /**
         * Unions two sets containing elements x and y.
         * @param x first element
         * @param y second element
         * @return true if a union was performed, false if they were already connected
         */
        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false;
            }

            // Union by size
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
            return true;
        }

        /**
         * Gets all circuit sizes.
         * @return list of circuit sizes
         */
        List<Integer> getCircuitSizes() {
            Map<Integer, Integer> circuits = new HashMap<>();
            for (int i = 0; i < parent.length; i++) {
                int root = find(i);
                circuits.put(root, size[root]);
            }
            return new ArrayList<>(circuits.values());
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
            List<JunctionBox> junctionBoxes = parseJunctionBoxes(lines);

            System.out.println("Loaded " + junctionBoxes.size() + " junction boxes");

            // Part 1: Make 1000 connections and find product of three largest circuits
            long part1Result = solvePart1(junctionBoxes);
            System.out.println("Part 1 - Product of three largest circuits: " + part1Result);

            System.out.println();

            // Part 2: Continue until all boxes form one circuit
            long part2Result = solvePart2(junctionBoxes);
            System.out.println("Part 2 - Product of X coordinates of final connection: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Parses the input lines into a list of JunctionBox objects.
     * @param lines the input lines
     * @return list of junction boxes
     */
    private static List<JunctionBox> parseJunctionBoxes(List<String> lines) {
        List<JunctionBox> boxes = new ArrayList<>();
        int index = 0;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                int z = Integer.parseInt(parts[2].trim());
                boxes.add(new JunctionBox(x, y, z, index++));
            }
        }
        return boxes;
    }

    /**
     * Solves Part 1: Makes 1000 connections and calculates the product of the three largest circuits.
     * @param junctionBoxes the list of junction boxes
     * @return the product of the three largest circuit sizes
     */
    private static long solvePart1(List<JunctionBox> junctionBoxes) {
        int numBoxes = junctionBoxes.size();
        UnionFind uf = new UnionFind(numBoxes);

        // Generate all possible connections and sort by distance
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < numBoxes; i++) {
            for (int j = i + 1; j < numBoxes; j++) {
                double distSq = junctionBoxes.get(i).distanceSquared(junctionBoxes.get(j));
                connections.add(new Connection(i, j, distSq));
            }
        }

        // Sort connections by distance
        Collections.sort(connections);

        // Make the first 1000 connection attempts
        int connectionsAttempted = 0;
        int successfulConnections = 0;
        for (Connection conn : connections) {
            if (connectionsAttempted >= CONNECTIONS_TO_MAKE) {
                break;
            }
            connectionsAttempted++;
            // Track successful unions (not already connected)
            if (uf.union(conn.box1, conn.box2)) {
                successfulConnections++;
            }
        }

        System.out.println("Attempted " + connectionsAttempted + " connections, "
                + successfulConnections + " were successful");

        // Get all circuit sizes and sort to find the three largest
        List<Integer> circuitSizes = uf.getCircuitSizes();
        circuitSizes.sort(Collections.reverseOrder());

        System.out.println("Number of circuits: " + circuitSizes.size());

        if (circuitSizes.size() < 3) {
            System.err.println("Error: Less than 3 circuits found. Cannot calculate product.");
            return 0;
        }

        System.out.println("Three largest circuits: " + circuitSizes.get(0) + ", "
                + circuitSizes.get(1) + ", " + circuitSizes.get(2));

        // Calculate product of three largest circuits
        long product = (long) circuitSizes.get(0) * circuitSizes.get(1) * circuitSizes.get(2);
        return product;
    }

    /**
     * Solves Part 2: Continues connecting until all boxes form one circuit.
     * Finds the last connection that unifies everything into a single circuit.
     * @param junctionBoxes the list of junction boxes
     * @return the product of the X coordinates of the final two junction boxes connected
     */
    private static long solvePart2(List<JunctionBox> junctionBoxes) {
        int numBoxes = junctionBoxes.size();
        UnionFind uf = new UnionFind(numBoxes);

        // Generate all possible connections and sort by distance
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < numBoxes; i++) {
            for (int j = i + 1; j < numBoxes; j++) {
                double distSq = junctionBoxes.get(i).distanceSquared(junctionBoxes.get(j));
                connections.add(new Connection(i, j, distSq));
            }
        }

        // Sort connections by distance
        Collections.sort(connections);

        // Keep connecting until all boxes form a single circuit
        int connectionsAttempted = 0;
        int successfulConnections = 0;
        Connection finalConnection = null;
        int numberOfCircuits = numBoxes;

        for (Connection conn : connections) {
            connectionsAttempted++;

            // Check if they're already connected
            if (uf.find(conn.box1) != uf.find(conn.box2)) {
                uf.union(conn.box1, conn.box2);
                successfulConnections++;
                numberOfCircuits--;
                finalConnection = conn;

                // Check if we've unified everything into one circuit
                if (numberOfCircuits == 1) {
                    break;
                }
            }
        }

        System.out.println("Attempted " + connectionsAttempted + " connections, "
                + successfulConnections + " were successful");
        System.out.println("All junction boxes now form a single circuit");

        if (finalConnection == null) {
            System.err.println("Error: No final connection found");
            return 0;
        }

        JunctionBox box1 = junctionBoxes.get(finalConnection.box1);
        JunctionBox box2 = junctionBoxes.get(finalConnection.box2);

        System.out.println("Final connection between: (" + box1.x + "," + box1.y + "," + box1.z
                + ") and (" + box2.x + "," + box2.y + "," + box2.z + ")");
        System.out.println("X coordinates: " + box1.x + " and " + box2.x);

        long product = (long) box1.x * box2.x;
        return product;
    }
}

