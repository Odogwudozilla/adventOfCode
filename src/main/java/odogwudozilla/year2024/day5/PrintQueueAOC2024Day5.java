package odogwudozilla.year2024.day5;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

/**
 * Print Queue (Advent of Code 2024 Day 5)
 * <p>
 * Satisfied with their search on Ceres, the squadron of scholars suggests subsequently scanning the stationery stacks of sub-basement 17.
 * <p>
 * The North Pole printing department is busier than ever this close to Christmas, and while The Historians continue their search of this historically significant facility, an Elf operating a very familiar printer beckons you over.
 * <p>
 * The Elf must recognise you, because they waste no time explaining that the new sleigh launch safety manual updates won't print correctly. Failure to update the safety manuals would be dire indeed, so you offer your services.
 * <p>
 * Safety protocols clearly indicate that new pages for the safety manuals must be printed in a very specific order. The notation X|Y means that if both page number X and page number Y are to be produced as part of an update, page number X must be printed at some point before page number Y.
 * <p>
 * Official puzzle link: https://adventofcode.com/2024/day/5
 *
 * @author GitHub Copilot
 */
public class PrintQueueAOC2024Day5 {
    private static final String PUZZLE_INPUT_PATH = "src/main/resources/2024/day5/day5_puzzle_data.txt";

    /**
     * Entry point for the solution.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(PUZZLE_INPUT_PATH));
        } catch (IOException e) {
            System.err.println("main - Failed to read puzzle input: " + e.getMessage());
            return;
        }
        // Split rules and updates
        List<String> rules = new ArrayList<>();
        List<String> updates = new ArrayList<>();
        boolean readingRules = true;
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (readingRules && line.contains(",")) {
                readingRules = false;
            }
            if (readingRules) {
                rules.add(line);
            } else {
                updates.add(line);
            }
        }
        int part1Result = solvePartOne(rules, updates);
        System.out.println("main - Part 1 result: " + part1Result);
        int part2Result = solvePartTwo(rules, updates);
        System.out.println("main - Part 2 result: " + part2Result);
    }

    /**
     * Solves Part 1: Sums the middle page numbers of correctly-ordered updates.
     * @param rules List of ordering rules (e.g., "47|53")
     * @param updates List of updates (e.g., "75,47,61,53,29")
     * @return The sum of the middle page numbers of correctly-ordered updates
     */
    public static int solvePartOne(List<String> rules, List<String> updates) {
        // Parse rules into pairs
        List<int[]> rulePairs = new ArrayList<>();
        for (String rule : rules) {
            String[] parts = rule.split("\\|");
            if (parts.length != 2) continue;
            int left = Integer.parseInt(parts[0]);
            int right = Integer.parseInt(parts[1]);
            rulePairs.add(new int[]{left, right});
        }
        int sum = 0;
        for (String update : updates) {
            String[] pageStrs = update.split(",");
            List<Integer> pages = new ArrayList<>();
            for (String p : pageStrs) {
                pages.add(Integer.parseInt(p.trim()));
            }
            if (isCorrectOrder(pages, rulePairs)) {
                int midIdx = pages.size() / 2;
                sum += pages.get(midIdx);
            }
        }
        return sum;
    }

    /**
     * Checks if the given update is in the correct order according to the rules.
     * @param pages List of page numbers in the update
     * @param rulePairs List of ordering rules as pairs
     * @return True if the update is correctly ordered, false otherwise
     */
    private static boolean isCorrectOrder(List<Integer> pages, List<int[]> rulePairs) {
        Map<Integer, Integer> pageToIndex = new HashMap<>();
        for (int i = 0; i < pages.size(); i++) {
            pageToIndex.put(pages.get(i), i);
        }
        for (int[] rule : rulePairs) {
            int left = rule[0];
            int right = rule[1];
            if (pageToIndex.containsKey(left) && pageToIndex.containsKey(right)) {
                if (pageToIndex.get(left) >= pageToIndex.get(right)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Solves Part 2: Sums the middle page numbers of incorrectly-ordered updates after reordering them correctly.
     * @param rules List of ordering rules (e.g., "47|53")
     * @param updates List of updates (e.g., "75,47,61,53,29")
     * @return The sum of the middle page numbers of reordered updates
     */
    public static int solvePartTwo(List<String> rules, List<String> updates) {
        List<int[]> rulePairs = new ArrayList<>();
        for (String rule : rules) {
            String[] parts = rule.split("\\|");
            if (parts.length != 2) continue;
            int left = Integer.parseInt(parts[0]);
            int right = Integer.parseInt(parts[1]);
            rulePairs.add(new int[]{left, right});
        }
        int sum = 0;
        for (String update : updates) {
            String[] pageStrs = update.split(",");
            List<Integer> pages = new ArrayList<>();
            for (String p : pageStrs) {
                pages.add(Integer.parseInt(p.trim()));
            }
            if (!isCorrectOrder(pages, rulePairs)) {
                List<Integer> sorted = topologicalSort(pages, rulePairs);
                if (sorted != null && !sorted.isEmpty()) {
                    int midIdx = sorted.size() / 2;
                    sum += sorted.get(midIdx);
                }
            }
        }
        return sum;
    }

    /**
     * Performs a topological sort of the pages according to the rules.
     * @param pages List of page numbers in the update
     * @param rulePairs List of ordering rules as pairs
     * @return Sorted list of page numbers, or null if impossible
     */
    private static List<Integer> topologicalSort(List<Integer> pages, List<int[]> rulePairs) {
        // Build graph for only present pages
        Set<Integer> present = new HashSet<>(pages);
        Map<Integer, List<Integer>> adj = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int page : pages) {
            adj.put(page, new ArrayList<>());
            inDegree.put(page, 0);
        }
        for (int[] rule : rulePairs) {
            int left = rule[0];
            int right = rule[1];
            if (present.contains(left) && present.contains(right)) {
                adj.get(left).add(right);
                inDegree.put(right, inDegree.get(right) + 1);
            }
        }
        // Kahn's algorithm
        Queue<Integer> q = new ArrayDeque<>();
        for (int page : pages) {
            if (inDegree.get(page) == 0) {
                q.add(page);
            }
        }
        List<Integer> result = new ArrayList<>();
        Set<Integer> used = new HashSet<>();
        while (!q.isEmpty()) {
            // To match original order as much as possible, pick the first unused page in input order
            Integer next = null;
            for (int page : pages) {
                if (q.contains(page) && !used.contains(page)) {
                    next = page;
                    break;
                }
            }
            if (next == null) {
                next = q.poll();
            } else {
                q.remove(next);
            }
            result.add(next);
            used.add(next);
            for (int neighbour : adj.get(next)) {
                inDegree.put(neighbour, inDegree.get(neighbour) - 1);
                if (inDegree.get(neighbour) == 0 && !used.contains(neighbour)) {
                    q.add(neighbour);
                }
            }
        }
        if (result.size() != pages.size()) {
            return null; // Cycle detected or impossible
        }
        return result;
    }
}
