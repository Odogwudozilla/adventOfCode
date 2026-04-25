package odogwudozilla.year2022.day4;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * Camp Cleanup (Advent of Code 2022, Day 4)
 * Space needs to be cleared before the last supplies can be unloaded from the ships, and so several Elves have been assigned the job of cleaning up sections of the camp. Every section has a unique ID number, and each Elf is assigned a range of section IDs.
 * Some assignments overlap. The task is to count how many assignment pairs have one range fully containing the other.
 *
 * Official puzzle URL: https://adventofcode.com/2022/day/4
 */
public class CampCleanupAOC2022Day4 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/2022/day4/day4_puzzle_data.txt"));
        System.out.println("Part 1: " + solvePartOne(lines));
        System.out.println("Part 2: " + solvePartTwo(lines));
    }

    /**
     * Solves Part 1: Counts assignment pairs where one range fully contains the other.
     * @param lines the input lines
     * @return the count of fully contained assignment pairs
     */
    public static int solvePartOne(@NotNull List<String> lines) {
        int count = 0;
        for (String line : lines) {
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            int[] a = parseRange(parts[0]);
            int[] b = parseRange(parts[1]);
            if ((a[0] <= b[0] && a[1] >= b[1]) || (b[0] <= a[0] && b[1] >= a[1])) {
                count++;
            }
        }
        return count;
    }

    /**
     * Solves Part 2: Counts assignment pairs where the ranges overlap at all.
     * @param lines the input lines
     * @return the count of overlapping assignment pairs
     */
    public static int solvePartTwo(@NotNull List<String> lines) {
        int count = 0;
        for (String line : lines) {
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            int[] a = parseRange(parts[0]);
            int[] b = parseRange(parts[1]);
            if (a[1] >= b[0] && b[1] >= a[0]) {
                count++;
            }
        }
        return count;
    }

    /**
     * Parses a range string like "2-4" into an int array [2,4].
     * @param s the range string
     * @return the int array
     */
    private static int[] parseRange(@NotNull String s) {
        String[] nums = s.split("-");
        return new int[] { Integer.parseInt(nums[0]), Integer.parseInt(nums[1]) };
    }
}
