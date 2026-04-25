package odogwudozilla.year2018.day7;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Advent of Code 2018 - Day 7: The Sum of Its Parts
 * <p>
 * Given a directed acyclic graph of assembly steps with prerequisite constraints:
 * Part 1 - Find the single-worker topological order, breaking ties alphabetically.
 * Part 2 - Find the total time with {@link #NUM_WORKERS} parallel workers, where
 * each step takes a base duration plus its alphabetical position.
 * <p>
 * Puzzle URL: https://adventofcode.com/2018/day/7
 */
public final class TheSumOfItsPartsAOC2018Day7 {

    private static final String INPUT_FILE = "/2018/day7/day7_puzzle_data.txt";

    /** Number of workers available for parallel execution in Part 2. */
    private static final int NUM_WORKERS = 5;

    /** Base duration in seconds added to each step's alphabetical position in Part 2. */
    private static final int BASE_STEP_DURATION = 60;

    /** Index in the input line where the prerequisite step letter appears. */
    private static final int PREREQUISITE_CHAR_INDEX = 5;

    /** Index in the input line where the dependent step letter appears. */
    private static final int DEPENDENT_CHAR_INDEX = 36;

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
     * Determines the single-worker step order by performing a topological sort,
     * always choosing the alphabetically first available step.
     * @param input list of prerequisite constraint lines
     * @return the step order as a string, e.g. "CABDFE"
     */
    private static String solvePartOne(List<String> input) {
        Map<Character, Set<Character>> prerequisites = buildPrerequisiteMap(input);
        Set<Character> allSteps = new TreeSet<>(prerequisites.keySet());
        Set<Character> completed = new HashSet<>();
        StringBuilder order = new StringBuilder();

        while (order.length() < allSteps.size()) {
            // Pick the alphabetically first step whose all prerequisites are done
            char next = allSteps.stream()
                    .filter(step -> !completed.contains(step))
                    .filter(step -> completed.containsAll(prerequisites.get(step)))
                    .min(Character::compareTo)
                    .orElseThrow(() -> new IllegalStateException("Cycle detected in step graph"));

            completed.add(next);
            order.append(next);
        }

        return order.toString();
    }

    /**
     * Simulates parallel execution with {@link #NUM_WORKERS} workers.
     * Each step takes {@link #BASE_STEP_DURATION} + (alphabetical position) seconds.
     * @param input list of prerequisite constraint lines
     * @return total elapsed seconds as a string
     */
    private static String solvePartTwo(List<String> input) {
        Map<Character, Set<Character>> prerequisites = buildPrerequisiteMap(input);
        Set<Character> allSteps = new TreeSet<>(prerequisites.keySet());
        Set<Character> completed = new HashSet<>();
        Set<Character> inProgress = new HashSet<>();
        // Tracks which step each active worker finishes and at what time
        Map<Character, Integer> workerFinishTimes = new HashMap<>();
        int elapsedTime = 0;

        while (completed.size() < allSteps.size()) {
            // Release workers whose tasks finished at or before the current time
            List<Character> justFinished = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : workerFinishTimes.entrySet()) {
                if (entry.getValue() <= elapsedTime) {
                    justFinished.add(entry.getKey());
                }
            }
            for (char step : justFinished) {
                completed.add(step);
                inProgress.remove(step);
                workerFinishTimes.remove(step);
            }

            // Assign available steps to free workers
            int freeWorkers = NUM_WORKERS - workerFinishTimes.size();
            List<Character> available = allSteps.stream()
                    .filter(step -> !completed.contains(step))
                    .filter(step -> !inProgress.contains(step))
                    .filter(step -> completed.containsAll(prerequisites.get(step)))
                    .limit(freeWorkers)
                    .collect(Collectors.toList());

            for (char step : available) {
                int duration = BASE_STEP_DURATION + (step - 'A' + 1);
                workerFinishTimes.put(step, elapsedTime + duration);
                inProgress.add(step);
            }

            if (workerFinishTimes.isEmpty()) {
                break;
            }

            // Jump to the next moment a worker finishes
            elapsedTime = workerFinishTimes.values().stream()
                    .min(Integer::compareTo)
                    .orElse(elapsedTime + 1);
        }

        return String.valueOf(elapsedTime);
    }

    /**
     * Parses the input lines and builds a map from each step to its set of prerequisite steps.
     * Every step that appears in the input is guaranteed to have an entry in the map,
     * even if it has no prerequisites (in which case its set is empty).
     * @param input list of constraint lines in format "Step X must be finished before step Y can begin."
     * @return map of step -> set of prerequisites
     */
    private static Map<Character, Set<Character>> buildPrerequisiteMap(List<String> input) {
        Map<Character, Set<Character>> prerequisites = new HashMap<>();

        for (String line : input) {
            if (line.isBlank()) {
                continue;
            }
            char prerequisite = line.charAt(PREREQUISITE_CHAR_INDEX);
            char dependent = line.charAt(DEPENDENT_CHAR_INDEX);

            // Ensure both steps have an entry
            prerequisites.computeIfAbsent(prerequisite, k -> new HashSet<>());
            prerequisites.computeIfAbsent(dependent, k -> new HashSet<>()).add(prerequisite);
        }

        return prerequisites;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = TheSumOfItsPartsAOC2018Day7.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
