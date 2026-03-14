package odogwudozilla.year2021.day12;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Passage Pathing (Advent of Code 2021 Day 12)
 *
 * With your submarine's subterranean subsystems subsisting suboptimally, the only way you're getting out of this cave anytime soon is by finding a path yourself. Not just a path - the only way to know if you've found the best path is to find all of them.
 *
 * Your goal is to find the number of distinct paths that start at start, end at end, and don't visit small caves more than once. There are two types of caves: big caves (written in uppercase, like A) and small caves (written in lowercase, like b). All paths you find should visit small caves at most once, and can visit big caves any number of times.
 *
 * Official puzzle URL: https://adventofcode.com/2021/day/12
 */
public class PassagePathingAOC2021Day12 {
    private static final String INPUT_PATH = "src/main/resources/2021/day12/day12_puzzle_data.txt";

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
        Map<String, List<String>> graph = buildGraph(lines);
        int part1 = solvePartOne(graph);
        System.out.println("Part 1: " + part1);
        int part2 = solvePartTwo(graph);
        System.out.println("Part 2: " + part2);
    }

    /**
     * Builds the cave graph from the input edge list.
     * @param lines List of edge strings
     * @return adjacency list graph
     */
    private static Map<String, List<String>> buildGraph(List<String> lines) {
        Map<String, List<String>> graph = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split("-");
            graph.computeIfAbsent(parts[0], k -> new ArrayList<>()).add(parts[1]);
            graph.computeIfAbsent(parts[1], k -> new ArrayList<>()).add(parts[0]);
        }
        return graph;
    }

    /**
     * Solves Part 1: Counts all distinct paths from 'start' to 'end' visiting small caves at most once.
     * @param graph The cave system graph
     * @return number of valid paths
     */
    public static int solvePartOne(Map<String, List<String>> graph) {
        return dfs("start", graph, new HashSet<>(), false);
    }

    /**
     * Depth-first search for all valid paths.
     * @param node Current cave
     * @param graph Cave system
     * @param visited Set of visited small caves
     * @param allowTwice Not used for Part 1
     * @return number of valid paths
     */
    private static int dfs(String node, Map<String, List<String>> graph, Set<String> visited, boolean allowTwice) {
        if ("end".equals(node)) return 1;
        boolean isSmall = node.equals(node.toLowerCase());
        if (isSmall && visited.contains(node)) return 0;
        if (isSmall) visited.add(node);
        int paths = 0;
        for (String next : graph.get(node)) {
            if (!"start".equals(next)) {
                paths += dfs(next, graph, new HashSet<>(visited), allowTwice);
            }
        }
        return paths;
    }

    /**
     * Solves Part 2: Counts all distinct paths from 'start' to 'end' where one small cave can be visited twice.
     * @param graph The cave system graph
     * @return number of valid paths
     */
    public static int solvePartTwo(Map<String, List<String>> graph) {
        return dfsPartTwo("start", graph, new HashSet<>(), false);
    }

    /**
     * DFS for Part 2: allows one small cave to be visited twice.
     * @param node Current cave
     * @param graph Cave system
     * @param visited Set of visited small caves
     * @param usedTwice True if a small cave has already been visited twice
     * @return number of valid paths
     */
    private static int dfsPartTwo(String node, Map<String, List<String>> graph, Set<String> visited, boolean usedTwice) {
        if ("end".equals(node)) return 1;
        boolean isSmall = node.equals(node.toLowerCase());
        boolean alreadyVisited = visited.contains(node);
        if (isSmall && alreadyVisited && usedTwice) return 0;
        if (isSmall && alreadyVisited && ("start".equals(node) || "end".equals(node))) return 0;
        if (isSmall && alreadyVisited) usedTwice = true;
        if (isSmall) visited.add(node);
        int paths = 0;
        for (String next : graph.get(node)) {
            if (!"start".equals(next)) {
                paths += dfsPartTwo(next, graph, new HashSet<>(visited), usedTwice);
            }
        }
        return paths;
    }
}
