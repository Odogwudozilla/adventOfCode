package odogwudozilla.year2017.day7;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2017 - Day 7: Recursive Circus
 * <p>
 * Puzzle URL: https://adventofcode.com/2017/day/7
 */
public final class RecursiveCircusAOC2017Day7 {

    private static final String INPUT_FILE = "/2017/day7/day7_puzzle_data.txt";

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    private static String solvePartOne(List<String> input) {
        // Find the bottom program (the root of the tower)
        java.util.Set<String> allPrograms = new java.util.HashSet<>();
        java.util.Set<String> childPrograms = new java.util.HashSet<>();
        for (String line : input) {
            String[] parts = line.split(" -> ");
            String name = parts[0].split(" ")[0];
            allPrograms.add(name);
            if (parts.length > 1) {
                String[] children = parts[1].split(", ");
                for (String child : children) {
                    childPrograms.add(child.trim());
                }
            }
        }
        // The bottom program is the one that is not a child of any other
        allPrograms.removeAll(childPrograms);
        if (allPrograms.size() == 1) {
            return allPrograms.iterator().next();
        }
        return "not found";
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Parse the input into a tree structure
        java.util.Map<String, Node> nodes = new java.util.HashMap<>();
        for (String line : input) {
            String[] parts = line.split(" -> ");
            String[] nameWeight = parts[0].split(" ");
            String name = nameWeight[0];
            int weight = Integer.parseInt(nameWeight[1].replaceAll("[()]", ""));
            Node node = nodes.computeIfAbsent(name, Node::new);
            node.weight = weight;
            if (parts.length > 1) {
                String[] children = parts[1].split(", ");
                for (String childName : children) {
                    Node child = nodes.computeIfAbsent(childName.trim(), Node::new);
                    node.children.add(child);
                    child.parent = node;
                }
            }
        }
        // Find the root
        Node root = null;
        for (Node n : nodes.values()) {
            if (n.parent == null) {
                root = n;
                break;
            }
        }
        // Find the incorrect weight
        Result result = findUnbalanced(root);
        return result != null ? String.valueOf(result.correctedWeight) : "not found";
    }

    private static Result findUnbalanced(Node node) {
        java.util.Map<Integer, java.util.List<Node>> weightToChildren = new java.util.HashMap<>();
        for (Node child : node.children) {
            Result r = findUnbalanced(child);
            if (r != null) return r;
            int total = child.totalWeight();
            weightToChildren.computeIfAbsent(total, k -> new java.util.ArrayList<>()).add(child);
        }
        if (weightToChildren.size() > 1) {
            // There is an imbalance
            int correctWeight = 0, wrongWeight = 0;
            Node wrongNode = null;
            for (java.util.Map.Entry<Integer, java.util.List<Node>> entry : weightToChildren.entrySet()) {
                if (entry.getValue().size() == 1) {
                    wrongWeight = entry.getKey();
                    wrongNode = entry.getValue().get(0);
                } else {
                    correctWeight = entry.getKey();
                }
            }
            int diff = correctWeight - wrongWeight;
            return new Result(wrongNode.weight + diff);
        }
        return null;
    }

    private static class Node {
        final String name;
        int weight;
        Node parent;
        java.util.List<Node> children = new java.util.ArrayList<>();
        Node(String name) { this.name = name; }
        int totalWeight() {
            int sum = weight;
            for (Node c : children) sum += c.totalWeight();
            return sum;
        }
    }

    private static class Result {
        final int correctedWeight;
        Result(int correctedWeight) { this.correctedWeight = correctedWeight; }
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = RecursiveCircusAOC2017Day7.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            java.util.List<String> lines = new java.util.ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
