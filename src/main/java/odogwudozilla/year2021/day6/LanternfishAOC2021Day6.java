package odogwudozilla.year2021.day6;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simulates lanternfish population growth as described in Advent of Code 2021 Day 6.
 * Each lanternfish creates a new lanternfish every 7 days, with new fish taking 2 extra days for their first cycle.
 * See the official puzzle description for details.
 * https://adventofcode.com/2021/day/6
 */
public class LanternfishAOC2021Day6 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/2021/day6/day6_puzzle_data.txt"));
        List<Integer> initialTimers = Arrays.stream(lines.get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        System.out.println("Part 1: " + solvePartOne(initialTimers));
        System.out.println("Part 2: " + solvePartTwo(initialTimers));
    }

    /**
     * Simulates lanternfish growth for 80 days.
     * @param initialTimers the initial state of lanternfish timers
     * @return the total number of lanternfish after 80 days
     */
    public static long solvePartOne(List<Integer> initialTimers) {
        long[] fish = new long[9];
        for (int timer : initialTimers) {
            fish[timer]++;
        }
        for (int day = 0; day < 80; day++) {
            long newFish = fish[0];
            System.arraycopy(fish, 1, fish, 0, 8);
            fish[6] += newFish;
            fish[8] = newFish;
        }
        return Arrays.stream(fish).sum();
    }

    /**
     * Simulates lanternfish growth for 256 days.
     * @param initialTimers the initial state of lanternfish timers
     * @return the total number of lanternfish after 256 days
     */
    public static long solvePartTwo(List<Integer> initialTimers) {
        long[] fish = new long[9];
        for (int timer : initialTimers) {
            fish[timer]++;
        }
        for (int day = 0; day < 256; day++) {
            long newFish = fish[0];
            System.arraycopy(fish, 1, fish, 0, 8);
            fish[6] += newFish;
            fish[8] = newFish;
        }
        return Arrays.stream(fish).sum();
    }
}
