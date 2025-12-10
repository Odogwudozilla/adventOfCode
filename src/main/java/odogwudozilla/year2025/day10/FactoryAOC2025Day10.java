package odogwudozilla.year2025.day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Advent of Code 2025 - Day 10: Factory
 *
 * Puzzle: Determine the minimum number of button presses required to configure
 * indicator lights on factory machines. Each button toggles specific lights,
 * and we need to find the optimal combination to reach the target state.
 *
 * ===========================================================================================
 * IMPORTANT: Part 2 is solved using a separate Python script with Z3 solver
 * ===========================================================================================
 *
 * PART 1: Solved using Gaussian elimination over GF(2) in Java
 * - Algorithm: Gaussian elimination in modulo 2 arithmetic
 * - Answer: 459
 * - Implementation: This Java file
 *
 * PART 2: Solved using Z3 Integer Linear Programming solver in Python
 * - Algorithm: ILP optimization using Z3 theorem prover
 * - Answer: 18687
 * - Python script: solve_part2_z3.py (in this same directory)
 * - Requirements: Python 3.x with z3-solver package (pip install z3-solver)
 * - Usage: python solve_part2_z3.py
 *
 * This Java implementation uses a greedy approximation for Part 2, which is NOT optimal
 * and produces an answer that is too high (~70,000+). For the correct Part 2 answer (18687),
 * you MUST run the Python script.
 *
 * See README.md in this directory for detailed instructions on running the Python solver.
 *
 * Official puzzle: https://adventofcode.com/2025/day/10
 */
public class FactoryAOC2025Day10 {

    private static final String INPUT_FILE = "src/main/resources/2025/day10/day10_puzzle_data.txt";

    private static class Machine {
        boolean[] targetState;
        List<boolean[]> buttons;
        int[] joltageRequirements;
        List<int[]> buttonJoltageEffects;

        public Machine(boolean[] targetState, List<boolean[]> buttons, int[] joltageRequirements, List<int[]> buttonJoltageEffects) {
            this.targetState = targetState;
            this.buttons = buttons;
            this.joltageRequirements = joltageRequirements;
            this.buttonJoltageEffects = buttonJoltageEffects;
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
            List<Machine> machines = parseMachines(lines);

            int totalPresses = solvePart1(machines);
            System.out.println("Part 1 - Minimum button presses required: " + totalPresses);

            long totalPart2 = solvePart2(machines);
            System.out.println("Part 2 - Minimum button presses for joltage configuration: " + totalPart2);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solve Part 1: Find the minimum number of button presses for all machines.
     * @param machines the list of machines to configure
     * @return the total minimum button presses required
     */
    private static int solvePart1(List<Machine> machines) {
        int totalPresses = 0;

        for (Machine machine : machines) {
            int presses = solveGaussianEliminationGF2(machine);
            totalPresses += presses;
        }

        return totalPresses;
    }

    /**
     * Solve Part 2: Find the minimum number of button presses for joltage configuration.
     * @param machines the list of machines to configure
     * @return the total minimum button presses required
     */
    private static long solvePart2(List<Machine> machines) {
        long totalPresses = 0;

        for (Machine machine : machines) {
            long presses = solveJoltageConfiguration(machine);
            totalPresses += presses;
        }

        return totalPresses;
    }

    /**
     * Solve joltage configuration problem using greedy algorithm.
     *
     * NOTE: This problem is an integer linear programming (ILP) problem that requires
     * a proper solver like Z3, PuLP, or OR-Tools for optimal solutions.
     *
     * The greedy algorithm provides an approximation but is NOT optimal.
     * For the correct answer, see solve_part2_z3.py which uses Z3 solver.
     *
     * Correct answer for Part 2: 18687 (obtained via Z3)
     * Greedy approximation: ~70405 (too high)
     *
     * @param machine the machine to solve
     * @return an approximation of the minimum number of button presses
     */
    private static long solveJoltageConfiguration(Machine machine) {
        return calculateGreedyUpperBound(machine);
    }

    /**
     * Calculate a greedy upper bound for the joltage configuration problem.
     * Uses a smarter greedy strategy that considers actual progress toward goals.
     * @param machine the machine to solve
     * @return an upper bound on the minimum number of presses
     */
    private static int calculateGreedyUpperBound(Machine machine) {
        int numCounters = machine.joltageRequirements.length;
        int numButtons = machine.buttonJoltageEffects.size();

        // Try multiple greedy strategies and pick the best
        int bestGreedy = Integer.MAX_VALUE;

        // Strategy 1: Simple greedy - pick button affecting most counters that need work
        bestGreedy = Math.min(bestGreedy, greedyStrategy1(machine));

        // Strategy 2: Pick button with best progress ratio
        bestGreedy = Math.min(bestGreedy, greedyStrategy2(machine));

        // Strategy 3: Prioritize buttons that maximize progress per press
        bestGreedy = Math.min(bestGreedy, greedyStrategy3(machine));

        // Strategy 4: Focus on filling the highest requirements first
        bestGreedy = Math.min(bestGreedy, greedyStrategy4(machine));

        return bestGreedy == Integer.MAX_VALUE ? 500 : bestGreedy;
    }

    private static int greedyStrategy1(Machine machine) {
        int numCounters = machine.joltageRequirements.length;
        int numButtons = machine.buttonJoltageEffects.size();
        int[] current = new int[numCounters];
        int[] target = machine.joltageRequirements.clone();
        int totalPresses = 0;

        while (!java.util.Arrays.equals(current, target)) {
            int bestButton = -1;
            int bestScore = -1;

            for (int btn = 0; btn < numButtons; btn++) {
                int progress = 0;
                boolean valid = true;

                for (int i = 0; i < numCounters; i++) {
                    if (machine.buttonJoltageEffects.get(btn)[i] > 0) {
                        int remaining = target[i] - current[i];
                        if (remaining > 0) {
                            progress++;
                        } else if (remaining == 0) {
                            valid = false;
                            break;
                        }
                    }
                }

                if (valid && progress > bestScore) {
                    bestScore = progress;
                    bestButton = btn;
                }
            }

            if (bestButton == -1) return Integer.MAX_VALUE;

            for (int i = 0; i < numCounters; i++) {
                current[i] += machine.buttonJoltageEffects.get(bestButton)[i];
            }
            totalPresses++;

            if (totalPresses > 500) return 500;
        }

        return totalPresses;
    }

    private static int greedyStrategy2(Machine machine) {
        int numCounters = machine.joltageRequirements.length;
        int numButtons = machine.buttonJoltageEffects.size();
        int[] current = new int[numCounters];
        int[] target = machine.joltageRequirements.clone();
        int totalPresses = 0;

        while (!java.util.Arrays.equals(current, target)) {
            int bestButton = -1;
            double bestRatio = -1;

            for (int btn = 0; btn < numButtons; btn++) {
                double progress = 0;
                int affected = 0;
                boolean valid = true;

                for (int i = 0; i < numCounters; i++) {
                    if (machine.buttonJoltageEffects.get(btn)[i] > 0) {
                        int remaining = target[i] - current[i];
                        if (remaining > 0) {
                            progress += Math.min(remaining, machine.buttonJoltageEffects.get(btn)[i]);
                            affected++;
                        } else if (remaining == 0) {
                            valid = false;
                            break;
                        }
                    }
                }

                if (valid && affected > 0) {
                    double ratio = progress / affected;
                    if (ratio > bestRatio) {
                        bestRatio = ratio;
                        bestButton = btn;
                    }
                }
            }

            if (bestButton == -1) return Integer.MAX_VALUE;

            for (int i = 0; i < numCounters; i++) {
                current[i] += machine.buttonJoltageEffects.get(bestButton)[i];
            }
            totalPresses++;

            if (totalPresses > 500) return 500;
        }

        return totalPresses;
    }

    private static int greedyStrategy3(Machine machine) {
        int numCounters = machine.joltageRequirements.length;
        int numButtons = machine.buttonJoltageEffects.size();
        int[] current = new int[numCounters];
        int[] target = machine.joltageRequirements.clone();
        int totalPresses = 0;

        while (!java.util.Arrays.equals(current, target)) {
            int bestButton = -1;
            double bestScore = -1;

            for (int btn = 0; btn < numButtons; btn++) {
                double totalProgress = 0;
                boolean valid = true;

                for (int i = 0; i < numCounters; i++) {
                    if (machine.buttonJoltageEffects.get(btn)[i] > 0) {
                        int remaining = target[i] - current[i];
                        if (remaining > 0) {
                            totalProgress += machine.buttonJoltageEffects.get(btn)[i];
                        } else if (remaining == 0) {
                            valid = false;
                            break;
                        }
                    }
                }

                if (valid && totalProgress > bestScore) {
                    bestScore = totalProgress;
                    bestButton = btn;
                }
            }

            if (bestButton == -1) return Integer.MAX_VALUE;

            for (int i = 0; i < numCounters; i++) {
                current[i] += machine.buttonJoltageEffects.get(bestButton)[i];
            }
            totalPresses++;

            if (totalPresses > 500) return 500;
        }

        return totalPresses;
    }

    private static int greedyStrategy4(Machine machine) {
        int numCounters = machine.joltageRequirements.length;
        int numButtons = machine.buttonJoltageEffects.size();
        int[] current = new int[numCounters];
        int[] target = machine.joltageRequirements.clone();
        int totalPresses = 0;

        while (!java.util.Arrays.equals(current, target)) {
            int bestButton = -1;
            double bestScore = -1;

            // Find counter with largest remaining value
            int maxRemaining = 0;
            int maxCounter = -1;
            for (int i = 0; i < numCounters; i++) {
                int remaining = target[i] - current[i];
                if (remaining > maxRemaining) {
                    maxRemaining = remaining;
                    maxCounter = i;
                }
            }

            if (maxCounter == -1) break;

            // Prefer buttons that affect this counter
            for (int btn = 0; btn < numButtons; btn++) {
                boolean valid = true;
                double score = 0;

                for (int i = 0; i < numCounters; i++) {
                    if (machine.buttonJoltageEffects.get(btn)[i] > 0) {
                        int remaining = target[i] - current[i];
                        if (remaining > 0) {
                            if (i == maxCounter) {
                                score += 10; // Bonus for affecting max counter
                            }
                            score += 1;
                        } else if (remaining == 0) {
                            valid = false;
                            break;
                        }
                    }
                }

                if (valid && score > bestScore) {
                    bestScore = score;
                    bestButton = btn;
                }
            }

            if (bestButton == -1) return Integer.MAX_VALUE;

            for (int i = 0; i < numCounters; i++) {
                current[i] += machine.buttonJoltageEffects.get(bestButton)[i];
            }
            totalPresses++;

            if (totalPresses > 500) return 500;
        }

        return totalPresses;
    }

    /**
     * Solve a single machine using Gaussian elimination over GF(2).
     * This finds the minimum number of button presses (in mod 2 arithmetic).
     * @param machine the machine to solve
     * @return the minimum number of button presses
     */
    private static int solveGaussianEliminationGF2(Machine machine) {
        int numLights = machine.targetState.length;
        int numButtons = machine.buttons.size();

        // Create augmented matrix [A|b] where A is button effects and b is target state
        boolean[][] matrix = new boolean[numLights][numButtons + 1];

        // Fill in the matrix: each column represents a button, each row represents a light
        for (int light = 0; light < numLights; light++) {
            for (int button = 0; button < numButtons; button++) {
                matrix[light][button] = machine.buttons.get(button)[light];
            }
            matrix[light][numButtons] = machine.targetState[light];
        }

        // Gaussian elimination in GF(2) to get reduced row echelon form
        int[] pivotColumn = new int[numLights];
        for (int i = 0; i < numLights; i++) {
            pivotColumn[i] = -1;
        }

        int currentRow = 0;
        for (int col = 0; col < numButtons && currentRow < numLights; col++) {
            // Find pivot
            int pivotRow = -1;
            for (int row = currentRow; row < numLights; row++) {
                if (matrix[row][col]) {
                    pivotRow = row;
                    break;
                }
            }

            if (pivotRow == -1) {
                continue; // No pivot in this column
            }

            // Swap rows
            if (pivotRow != currentRow) {
                boolean[] temp = matrix[currentRow];
                matrix[currentRow] = matrix[pivotRow];
                matrix[pivotRow] = temp;
            }

            pivotColumn[currentRow] = col;

            // Eliminate other rows (to get RREF)
            for (int row = 0; row < numLights; row++) {
                if (row != currentRow && matrix[row][col]) {
                    // XOR this row with current row
                    for (int c = 0; c <= numButtons; c++) {
                        matrix[row][c] ^= matrix[currentRow][c];
                    }
                }
            }

            currentRow++;
        }

        // Check for inconsistency
        for (int row = currentRow; row < numLights; row++) {
            if (matrix[row][numButtons]) {
                return Integer.MAX_VALUE; // No solution
            }
        }

        // Identify pivot and free variables
        boolean[] isPivot = new boolean[numButtons];
        for (int row = 0; row < currentRow; row++) {
            if (pivotColumn[row] != -1) {
                isPivot[pivotColumn[row]] = true;
            }
        }

        // Collect free variables
        List<Integer> freeVars = new ArrayList<>();
        for (int col = 0; col < numButtons; col++) {
            if (!isPivot[col]) {
                freeVars.add(col);
            }
        }

        // Try all combinations of free variables to find minimum button presses
        int minPresses = Integer.MAX_VALUE;
        int numFreeVars = freeVars.size();

        // If too many free variables, limit the search
        if (numFreeVars > 20) {
            // Just use the basic solution with free vars = 0
            numFreeVars = 0;
        }

        int totalCombinations = 1 << numFreeVars; // 2^numFreeVars

        for (int combination = 0; combination < totalCombinations; combination++) {
            boolean[] solution = new boolean[numButtons];

            // Set free variables according to combination
            for (int i = 0; i < numFreeVars; i++) {
                solution[freeVars.get(i)] = ((combination >> i) & 1) == 1;
            }

            // Calculate pivot variables using back substitution
            for (int row = 0; row < currentRow; row++) {
                int pivotCol = pivotColumn[row];
                if (pivotCol == -1) continue;

                boolean value = matrix[row][numButtons];
                // Subtract contributions from non-pivot columns
                for (int col = 0; col < numButtons; col++) {
                    if (col != pivotCol && matrix[row][col] && solution[col]) {
                        value ^= true;
                    }
                }
                solution[pivotCol] = value;
            }

            // Count button presses
            int presses = 0;
            for (boolean press : solution) {
                if (press) presses++;
            }

            minPresses = Math.min(minPresses, presses);
        }

        return minPresses;
    }

    /**
     * Parse the input file into a list of machines.
     * @param lines the input lines
     * @return the list of machines
     */
    private static List<Machine> parseMachines(List<String> lines) {
        List<Machine> machines = new ArrayList<>();

        Pattern indicatorPattern = Pattern.compile("\\[([.#]+)\\]");
        Pattern buttonPattern = Pattern.compile("\\(([0-9,]+)\\)");
        Pattern joltagePattern = Pattern.compile("\\{([0-9,]+)\\}");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Parse indicator lights
            Matcher indicatorMatcher = indicatorPattern.matcher(line);
            if (!indicatorMatcher.find()) {
                continue;
            }

            String indicatorStr = indicatorMatcher.group(1);
            int numLights = indicatorStr.length();
            boolean[] targetState = new boolean[numLights];
            for (int i = 0; i < numLights; i++) {
                targetState[i] = indicatorStr.charAt(i) == '#';
            }

            // Parse buttons
            List<boolean[]> buttons = new ArrayList<>();
            List<int[]> buttonJoltageEffects = new ArrayList<>();
            Matcher buttonMatcher = buttonPattern.matcher(line);
            while (buttonMatcher.find()) {
                String buttonStr = buttonMatcher.group(1);
                boolean[] buttonEffect = new boolean[numLights];

                String[] indices = buttonStr.split(",");
                for (String indexStr : indices) {
                    int index = Integer.parseInt(indexStr.trim());
                    buttonEffect[index] = true;
                }

                buttons.add(buttonEffect);
            }

            // Parse joltage requirements
            Matcher joltageMatcher = joltagePattern.matcher(line);
            int[] joltageRequirements = new int[0];
            if (joltageMatcher.find()) {
                String joltageStr = joltageMatcher.group(1);
                String[] values = joltageStr.split(",");
                joltageRequirements = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    joltageRequirements[i] = Integer.parseInt(values[i].trim());
                }

                // Create button joltage effects based on button indices
                buttonMatcher = buttonPattern.matcher(line);
                while (buttonMatcher.find()) {
                    String buttonStr = buttonMatcher.group(1);
                    int[] joltageEffect = new int[joltageRequirements.length];

                    String[] indices = buttonStr.split(",");
                    for (String indexStr : indices) {
                        int index = Integer.parseInt(indexStr.trim());
                        if (index < joltageRequirements.length) {
                            joltageEffect[index] = 1;
                        }
                    }

                    buttonJoltageEffects.add(joltageEffect);
                }
            }

            machines.add(new Machine(targetState, buttons, joltageRequirements, buttonJoltageEffects));
        }

        return machines;
    }
}

