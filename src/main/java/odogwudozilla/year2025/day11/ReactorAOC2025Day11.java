package odogwudozilla.year2025.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Advent of Code 2025 - Day 11: Reactor
 *
 * Part 1: Find all possible paths through a network of devices from "you" to "out".
 * Part 2: Find all paths from "svr" to "out" that pass through both "dac" and "fft".
 *
 * The devices are connected in a directed graph where data flows only forward
 * through the outputs.
 *
 * Puzzle URL: https://adventofcode.com/2025/day/11
 */
public class ReactorAOC2025Day11 {

    private static final String PART1_START = "you";
    private static final String PART2_START = "svr";
    private static final String END_DEVICE = "out";
    private static final String REQUIRED_DEVICE_1 = "dac";
    private static final String REQUIRED_DEVICE_2 = "fft";

    public static void main(String[] args) {
        try {
            String inputPath = "src/main/resources/2025/day11/day11_puzzle_data.txt";
            List<String> lines = Files.readAllLines(Paths.get(inputPath));

            // Parse the input to build the graph
            Map<String, List<String>> graph = parseGraph(lines);

            // Part 1: Find all paths from "you" to "out"
            int part1Count = countAllPaths(graph, PART1_START, END_DEVICE);
            System.out.println("Part 1 - Number of different paths from 'you' to 'out': " + part1Count);

            // Part 2: Find all paths from "svr" to "out" that visit both "dac" and "fft"
            long part2Count = countPathsWithRequiredDevices(graph, PART2_START, END_DEVICE,
                                                           REQUIRED_DEVICE_1, REQUIRED_DEVICE_2);
            System.out.println("Part 2 - Number of paths from 'svr' to 'out' visiting both 'dac' and 'fft': " + part2Count);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses the input lines and builds a directed graph represented as an adjacency list.
     * @param lines the input lines containing device connections
     * @return a map where each key is a device name and the value is a list of connected devices
     */
    private static Map<String, List<String>> parseGraph(List<String> lines) {
        Map<String, List<String>> graph = new HashMap<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Parse line format: "device: output1 output2 output3"
            String[] parts = line.split(":");
            String device = parts[0].trim();

            List<String> outputs = new ArrayList<>();
            if (parts.length > 1) {
                String[] outputDevices = parts[1].trim().split("\\s+");
                outputs.addAll(Arrays.asList(outputDevices));
            }

            graph.put(device, outputs);
        }

        return graph;
    }

    /**
     * Counts all possible paths from the start device to the end device using depth-first search.
     * @param graph the directed graph of device connections
     * @param start the starting device name
     * @param end the ending device name
     * @return the total number of distinct paths from start to end
     */
    private static int countAllPaths(Map<String, List<String>> graph, String start, String end) {
        Set<String> visited = new HashSet<>();
        return dfs(graph, start, end, visited);
    }

    /**
     * Performs depth-first search to count all paths from current device to the target device.
     * @param graph the directed graph of device connections
     * @param current the current device being visited
     * @param target the target device to reach
     * @param visited set of devices already visited in the current path to avoid cycles
     * @return the count of paths from current to target
     */
    private static int dfs(Map<String, List<String>> graph, String current, String target, Set<String> visited) {
        // Base case: reached the target device
        if (current.equals(target)) {
            return 1;
        }

        // If current device has no outputs or is not in the graph, no path exists
        if (!graph.containsKey(current)) {
            return 0;
        }

        // Mark current device as visited to prevent cycles
        visited.add(current);

        int totalPaths = 0;
        List<String> outputs = graph.get(current);

        // Explore all output devices
        for (String nextDevice : outputs) {
            // Only visit if not already in the current path
            if (!visited.contains(nextDevice)) {
                totalPaths += dfs(graph, nextDevice, target, visited);
            }
        }

        // Backtrack: remove current device from visited set
        visited.remove(current);

        return totalPaths;
    }

    /**
     * Counts all paths from start to end that pass through both required devices (in any order).
     * Uses dynamic programming with proper memoization.
     * @param graph the directed graph of device connections
     * @param start the starting device name
     * @param end the ending device name
     * @param required1 the first required device that must be visited
     * @param required2 the second required device that must be visited
     * @return the count of paths that visit both required devices
     */
    private static long countPathsWithRequiredDevices(Map<String, List<String>> graph, String start,
                                                       String end, String required1, String required2) {
        // State space: (current_node, has_visited_req1, has_visited_req2)
        // We compute number of paths from each state to the end
        Map<StateKey, Long> memo = new HashMap<>();

        return countPathsMemo(graph, start, end, required1, required2, false, false, memo);
    }

    /**
     * State key for memoization combining node position and which required devices have been visited.
     */
    private static class StateKey {
        final String node;
        final boolean hasReq1;
        final boolean hasReq2;

        StateKey(String node, boolean hasReq1, boolean hasReq2) {
            this.node = node;
            this.hasReq1 = hasReq1;
            this.hasReq2 = hasReq2;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof StateKey)) {
                return false;
            }
            StateKey other = (StateKey) obj;
            return node.equals(other.node) && hasReq1 == other.hasReq1 && hasReq2 == other.hasReq2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, hasReq1, hasReq2);
        }
    }

    /**
     * Counts paths using memoization. The key insight is that if the graph is a DAG,
     * we can safely memoize based on (node, hasReq1, hasReq2) without tracking visited nodes.
     * @param graph the directed graph
     * @param current current node
     * @param target target node
     * @param req1 first required device
     * @param req2 second required device
     * @param hasReq1 whether req1 has been visited
     * @param hasReq2 whether req2 has been visited
     * @param memo memoization cache
     * @return number of valid paths
     */
    private static long countPathsMemo(Map<String, List<String>> graph, String current, String target,
                                        String req1, String req2, boolean hasReq1, boolean hasReq2,
                                        Map<StateKey, Long> memo) {
        // Update required device flags
        if (current.equals(req1)) {
            hasReq1 = true;
        }
        if (current.equals(req2)) {
            hasReq2 = true;
        }

        // Base case: reached target
        if (current.equals(target)) {
            return (hasReq1 && hasReq2) ? 1L : 0L;
        }

        // Check memoization
        StateKey key = new StateKey(current, hasReq1, hasReq2);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // No outgoing edges
        if (!graph.containsKey(current)) {
            memo.put(key, 0L);
            return 0L;
        }

        // Sum paths through all neighbors
        long totalPaths = 0L;
        for (String next : graph.get(current)) {
            totalPaths += countPathsMemo(graph, next, target, req1, req2, hasReq1, hasReq2, memo);
        }

        memo.put(key, totalPaths);
        return totalPaths;
    }
}

