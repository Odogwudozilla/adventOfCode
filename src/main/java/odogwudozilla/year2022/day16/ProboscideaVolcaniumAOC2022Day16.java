package odogwudozilla.year2022.day16;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

/**
 * Proboscidea Volcanium (Advent of Code 2022 Day 16)
 *
 * The sensors have led you to the origin of the distress signal: yet another handheld device, just like the one the Elves gave you. However, you don't see any Elves around; instead, the device is surrounded by elephants! They must have gotten lost in these tunnels, and one of the elephants apparently figured out how to turn on the distress signal.
 *
 * The ground rumbles again, much stronger this time. What kind of cave is this, exactly? You scan the cave with your handheld device; it reports mostly igneous rock, some ash, pockets of pressurised gas, magma... this isn't just a cave, it's a volcano!
 *
 * You need to get the elephants out of here, quickly. Your device estimates that you have 30 minutes before the volcano erupts, so you don't have time to go back out the way you came in.
 *
 * You scan the cave for other options and discover a network of pipes and pressure-release valves. You aren't sure how such a system got into a volcano, but you don't have time to complain; your device produces a report (your puzzle input) of each valve's flow rate if it were opened (in pressure per minute) and the tunnels you could use to move between the valves.
 *
 * There's even a valve in the room you and the elephants are currently standing in labelled AA. You estimate it will take you one minute to open a single valve and one minute to follow any tunnel from one valve to another. What is the most pressure you could release?
 *
 * Official puzzle link: https://adventofcode.com/2022/day/16
 *
 * @param args Command line arguments
 */
public class ProboscideaVolcaniumAOC2022Day16 {
    private static final String INPUT_PATH = "src/main/resources/2022/day16/day16_puzzle_data.txt";
    private static final int TOTAL_MINUTES = 30;

    public static void main(String[] args) throws IOException {
        List<String> inputLines = Files.readAllLines(Paths.get(INPUT_PATH));
        System.out.println("Part 1: " + solvePartOne(inputLines));
        System.out.println("Part 2: " + solvePartTwo(inputLines));
    }

    /**
     * Solves Part 1 of the puzzle: Find the maximum pressure that can be released in 30 minutes.
     * @param inputLines The puzzle input lines
     * @return The maximum pressure released
     */
    public static int solvePartOne(List<String> inputLines) {
        // Parse the input into a graph structure
        Map<String, Valve> valves = new HashMap<>();
        for (String line : inputLines) {
            String[] parts = line.split("[=;]");
            String name = line.substring(6, 8);
            int flow = Integer.parseInt(parts[1].trim());
            List<String> leadsTo = new ArrayList<>();
            if (line.contains("valves ")) {
                String[] tunnels = line.split("valves ")[1].split(", ");
                leadsTo.addAll(Arrays.asList(tunnels));
            } else if (line.contains("valve ")) {
                String[] tunnels = line.split("valve ")[1].split(", ");
                leadsTo.addAll(Arrays.asList(tunnels));
            }
            valves.put(name, new Valve(name, flow, leadsTo));
        }
        // Precompute shortest paths between all valves using Floyd-Warshall
        Map<String, Map<String, Integer>> shortestPaths = computeAllPairsShortestPaths(valves);
        // Only consider valves with positive flow
        List<String> usefulValves = new ArrayList<>();
        for (Valve v : valves.values()) {
            if (v.flowRate > 0) usefulValves.add(v.name);
        }
        // DFS with memoisation
        return dfs("AA", TOTAL_MINUTES, 0, usefulValves, valves, shortestPaths, new HashMap<>());
    }

    /**
     * Solves Part 2 of the puzzle: Find the maximum pressure that can be released in 26 minutes with two agents.
     * @param inputLines The puzzle input lines
     * @return The maximum pressure released with two agents
     */
    public static int solvePartTwo(List<String> inputLines) {
        Map<String, Valve> valves = new HashMap<>();
        for (String line : inputLines) {
            String[] parts = line.split("[=;]");
            String name = line.substring(6, 8);
            int flow = Integer.parseInt(parts[1].trim());
            List<String> leadsTo = new ArrayList<>();
            if (line.contains("valves ")) {
                String[] tunnels = line.split("valves ")[1].split(", ");
                leadsTo.addAll(Arrays.asList(tunnels));
            } else if (line.contains("valve ")) {
                String[] tunnels = line.split("valve ")[1].split(", ");
                leadsTo.addAll(Arrays.asList(tunnels));
            }
            valves.put(name, new Valve(name, flow, leadsTo));
        }
        Map<String, Map<String, Integer>> shortestPaths = computeAllPairsShortestPaths(valves);
        List<String> usefulValves = new ArrayList<>();
        for (Valve v : valves.values()) {
            if (v.flowRate > 0) usefulValves.add(v.name);
        }
        int n = usefulValves.size();
        int max = 0;
        Map<String, Integer> memo = new HashMap<>();
        // Try all possible splits of the valves between the two agents
        for (int mask = 0; mask < (1 << n); mask++) {
            int you = dfs("AA", 26, mask, usefulValves, valves, shortestPaths, memo);
            int elephant = dfs("AA", 26, ((1 << n) - 1) ^ mask, usefulValves, valves, shortestPaths, memo);
            max = Math.max(max, you + elephant);
        }
        return max;
    }

    private static int dfs(String current, int timeLeft, int openedMask, List<String> usefulValves, Map<String, Valve> valves, Map<String, Map<String, Integer>> shortestPaths, Map<String, Integer> memo) {
        String key = current + "," + timeLeft + "," + openedMask;
        if (memo.containsKey(key)) return memo.get(key);
        int max = 0;
        for (int i = 0; i < usefulValves.size(); i++) {
            if ((openedMask & (1 << i)) != 0) continue;
            String next = usefulValves.get(i);
            int dist = shortestPaths.get(current).get(next);
            int timeAfterOpen = timeLeft - dist - 1;
            if (timeAfterOpen <= 0) continue;
            int released = valves.get(next).flowRate * timeAfterOpen;
            int total = released + dfs(next, timeAfterOpen, openedMask | (1 << i), usefulValves, valves, shortestPaths, memo);
            if (total > max) max = total;
        }
        memo.put(key, max);
        return max;
    }

    private static Map<String, Map<String, Integer>> computeAllPairsShortestPaths(Map<String, Valve> valves) {
        Map<String, Map<String, Integer>> dist = new HashMap<>();
        for (String v : valves.keySet()) {
            dist.put(v, new HashMap<>());
            for (String w : valves.keySet()) {
                dist.get(v).put(w, v.equals(w) ? 0 : 1000);
            }
        }
        for (Valve v : valves.values()) {
            for (String w : v.leadsTo) {
                dist.get(v.name).put(w, 1);
            }
        }
        for (String k : valves.keySet()) {
            for (String i : valves.keySet()) {
                for (String j : valves.keySet()) {
                    int alt = dist.get(i).get(k) + dist.get(k).get(j);
                    if (alt < dist.get(i).get(j)) {
                        dist.get(i).put(j, alt);
                    }
                }
            }
        }
        return dist;
    }

    private static class Valve {
        String name;
        int flowRate;
        List<String> leadsTo;
        Valve(String name, int flowRate, List<String> leadsTo) {
            this.name = name;
            this.flowRate = flowRate;
            this.leadsTo = leadsTo;
        }
    }
}
