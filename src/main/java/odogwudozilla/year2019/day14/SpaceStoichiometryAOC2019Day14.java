package odogwudozilla.year2019.day14;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2019 - Day 14: Space Stoichiometry
 * <p>
 * Puzzle URL: https://adventofcode.com/2019/day/14
 */
public final class SpaceStoichiometryAOC2019Day14 {

    private static final String INPUT_FILE = "/2019/day14/day14_puzzle_data.txt";

    /**
     * Represents a chemical reaction with inputs and output.
     */
    static class Reaction {
        final Map<String, Long> inputs;
        final String output;
        final long outputQuantity;

        Reaction(Map<String, Long> inputs, String output, long outputQuantity) {
            this.inputs = inputs;
            this.output = output;
            this.outputQuantity = outputQuantity;
        }
    }

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
    public static String solvePartOne(List<String> input) {
        Map<String, Reaction> reactionMap = new HashMap<>();
        for (String line : input) {
            parseReaction(line, reactionMap);
        }

        Map<String, Long> inventory = new HashMap<>();
        long oreNeeded = calculateOreNeeded("FUEL", 1, reactionMap, inventory);
        return String.valueOf(oreNeeded);
    }

    /**
     * Solves Part 2 of the puzzle.
     * Finds the maximum amount of FUEL that can be produced with 1 trillion ORE.
     * Uses binary search to efficiently find the answer.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public static String solvePartTwo(List<String> input) {
        Map<String, Reaction> reactionMap = new HashMap<>();
        for (String line : input) {
            parseReaction(line, reactionMap);
        }

        final long ORE_BUDGET = 1_000_000_000_000L;

        // Calculate the cost of producing 1 FUEL to estimate the upper bound
        Map<String, Long> tempInventory = new HashMap<>();
        long orePerFuel = calculateOreNeeded("FUEL", 1, reactionMap, tempInventory);

        // Use binary search for the maximum FUEL that can be produced with ORE_BUDGET
        long left = 1;
        long right = ORE_BUDGET / orePerFuel * 2;  // Heuristic: use 2x the rough estimate to be safe

        while (left < right) {
            long mid = left + (right - left + 1) / 2;  // Use ceiling to avoid infinite loop

            // Test if we can produce mid FUEL with the ORE budget
            Map<String, Long> inventory = new HashMap<>();
            long oreNeeded = calculateOreNeeded("FUEL", mid, reactionMap, inventory);

            if (oreNeeded <= ORE_BUDGET) {
                // We can produce this much FUEL; try for more
                left = mid;
            } else {
                // We can't produce this much FUEL; try for less
                right = mid - 1;
            }
        }

        return String.valueOf(left);
    }

    /**
     * Parses a reaction line and adds it to the reaction map.
     * Format: "input1 CHEM1, input2 CHEM2 => outputQty OUTPUT"
     */
    private static void parseReaction(String line, Map<String, Reaction> reactionMap) {
        String[] parts = line.split(" => ");
        String inputPart = parts[0];
        String outputPart = parts[1].trim();

        // Parse output
        String[] outputTokens = outputPart.split(" ");
        long outputQty = Long.parseLong(outputTokens[0]);
        String outputChem = outputTokens[1];

        // Parse inputs
        Map<String, Long> inputs = new HashMap<>();
        String[] inputChems = inputPart.split(", ");
        for (String input : inputChems) {
            String[] tokens = input.trim().split(" ");
            long qty = Long.parseLong(tokens[0]);
            String chem = tokens[1];
            inputs.put(chem, qty);
        }

        reactionMap.put(outputChem, new Reaction(inputs, outputChem, outputQty));
    }

    /**
     * Recursively calculates the minimum ORE needed to produce the given quantity of a chemical.
     * Uses inventory to track and reuse leftovers.
     */
    private static long calculateOreNeeded(
            String chemical,
            long quantityNeeded,
            Map<String, Reaction> reactionMap,
            Map<String, Long> inventory) {

        // Base case: ORE doesn't need any ore to produce (it is ore)
        if ("ORE".equals(chemical)) {
            return quantityNeeded;
        }

        // Check inventory first
        long availableInInventory = inventory.getOrDefault(chemical, 0L);
        if (availableInInventory >= quantityNeeded) {
            inventory.put(chemical, availableInInventory - quantityNeeded);
            return 0;
        }

        // Need to produce more
        long stillNeeded = quantityNeeded - availableInInventory;
        inventory.put(chemical, 0L);

        // Get the reaction for this chemical
        Reaction reaction = reactionMap.get(chemical);
        if (reaction == null) {
            throw new IllegalStateException("No reaction found for chemical: " + chemical);
        }

        // Calculate how many times we need to run the reaction (ceiling division)
        long timesToRun = (stillNeeded + reaction.outputQuantity - 1) / reaction.outputQuantity;

        // Add leftovers to inventory
        long produced = timesToRun * reaction.outputQuantity;
        long leftover = produced - stillNeeded;
        inventory.put(chemical, leftover);

        // Recursively calculate ore needed for all inputs
        long oreConsumed = 0;
        for (Map.Entry<String, Long> input : reaction.inputs.entrySet()) {
            String inputChem = input.getKey();
            long inputQtyPerReaction = input.getValue();
            long totalInputNeeded = timesToRun * inputQtyPerReaction;
            oreConsumed += calculateOreNeeded(inputChem, totalInputNeeded, reactionMap, inventory);
        }

        return oreConsumed;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SpaceStoichiometryAOC2019Day14.class.getResourceAsStream(INPUT_FILE)) {
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




