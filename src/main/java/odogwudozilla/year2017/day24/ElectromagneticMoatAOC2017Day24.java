package odogwudozilla.year2017.day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Electromagnetic Moat (Advent of Code 2017, Day 24)
 * <p>
 * The goal is to build the strongest possible bridge using magnetic components, each with two ports. The bridge must start with a port of type 0, and each component can only be used once. The strength of a bridge is the sum of the port types in each component. This solution finds the strength of the strongest bridge that can be built from the available components.
 * @see <a href="https://adventofcode.com/2017/day/24">https://adventofcode.com/2017/day/24</a>
 */
public class ElectromagneticMoatAOC2017Day24 {
    private static final String INPUT_PATH = "src/main/resources/2017/day24/day24_puzzle_data.txt";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
        List<Component> components = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.trim().split("/");
            if (parts.length == 2) {
                int a = Integer.parseInt(parts[0]);
                int b = Integer.parseInt(parts[1]);
                components.add(new Component(a, b));
            }
        }
        int strongest = solvePartOne(components);
        System.out.println("Part 1: Strength of the strongest bridge: " + strongest);
        int longestStrongest = solvePartTwo(components);
        System.out.println("Part 2: Strength of the longest bridge (strongest if tie): " + longestStrongest);
    }

    /**
     * Finds the strength of the strongest bridge that can be built from the given components.
     * @param components List of available components
     * @return Strength of the strongest bridge
     */
    public static int solvePartOne(List<?> components) {
        @SuppressWarnings("unchecked")
        List<Component> typedComponents = (List<Component>) components;
        return findStrongest(0, typedComponents, new boolean[typedComponents.size()]);
    }

    // Recursive DFS to find the strongest bridge
    private static int findStrongest(int port, List<Component> components, boolean[] used) {
        int maxStrength = 0;
        for (int i = 0; i < components.size(); i++) {
            if (!used[i] && components.get(i).matches(port)) {
                used[i] = true;
                int nextPort = components.get(i).other(port);
                int strength = components.get(i).strength() + findStrongest(nextPort, components, used);
                maxStrength = Math.max(maxStrength, strength);
                used[i] = false;
            }
        }
        return maxStrength;
    }

    /**
     * Finds the strength of the longest bridge (strongest if tie) that can be built from the given components.
     * @param components List of available components
     * @return Strength of the longest (and strongest if tie) bridge
     */
    public static int solvePartTwo(List<?> components) {
        @SuppressWarnings("unchecked")
        List<Component> typedComponents = (List<Component>) components;
        Result result = findLongestStrongest(0, typedComponents, new boolean[typedComponents.size()], 0);
        return result.strength;
    }

    // Helper class to track both length and strength
        private record Result(int length, int strength) {
    }

    // Recursive DFS to find the longest (and strongest if tie) bridge
    private static Result findLongestStrongest(int port, List<Component> components, boolean[] used, int length) {
        Result best = new Result(length, 0);
        for (int i = 0; i < components.size(); i++) {
            if (!used[i] && components.get(i).matches(port)) {
                used[i] = true;
                int nextPort = components.get(i).other(port);
                Result candidate = findLongestStrongest(nextPort, components, used, length + 1);
                int candidateStrength = components.get(i).strength() + candidate.strength;
                if (candidate.length > best.length || (candidate.length == best.length && candidateStrength > best.strength)) {
                    best = new Result(candidate.length, candidateStrength);
                }
                used[i] = false;
            }
        }
        return best;
    }

    /**
         * Represents a magnetic component with two ports.
         */
        private record Component(int a, int b) {
        boolean matches(int port) {
                return a == port || b == port;
            }

        int other(int port) {
                return a == port ? b : a;
            }

        int strength() {
                return a + b;
            }
        }
}
