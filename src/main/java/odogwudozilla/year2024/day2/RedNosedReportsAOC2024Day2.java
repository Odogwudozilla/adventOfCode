package odogwudozilla.year2024.day2;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code 2024 Day 2: Red-Nosed Reports
 * Fortunately, the first location The Historians want to search isn't a long walk from the Chief Historian's office.
 * While the Red-Nosed Reindeer nuclear fusion/fission plant appears to contain no sign of the Chief Historian, the engineers there run up to you as soon as they see you. Apparently, they still talk about the time Rudolph was saved through molecular synthesis from a single electron.
 * They're quick to add that - since you're already here - they'd really appreciate your help analyzing some unusual data from the Red-Nosed reactor. You turn to check if The Historians are waiting for you, but they seem to have already divided into groups that are currently searching every corner of the facility. You offer to help with the unusual data.
 * The unusual data (your puzzle input) consists of many reports, one report per line. Each report is a list of numbers called levels that are separated by spaces.
 * The engineers are trying to figure out which reports are safe. The Red-Nosed reactor safety systems can only tolerate levels that are either gradually increasing or gradually decreasing. So, a report only counts as safe if both of the following are true:
 * 1. The levels are either all increasing or all decreasing.
 * 2. Any two adjacent levels differ by at least one and at most three.
 * Analyze the unusual data from the engineers. How many reports are safe?
 *
 * Official puzzle URL: https://adventofcode.com/2024/day/2
 */
public class RedNosedReportsAOC2024Day2 {
    public static void main(String[] args) {
        List<String> lines = readInput("src/main/resources/2024/day2/day2_puzzle_data.txt");
        int safeCount = solvePartOne(lines);
        System.out.println("Part 1 - Safe reports: " + safeCount);
        int safeCountPartTwo = solvePartTwo(lines);
        System.out.println("Part 2 - Safe reports with Problem Dampener: " + safeCountPartTwo);
    }

    @NotNull
    private static List<String> readInput(@NotNull String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input file: " + path, e);
        }
    }

    /**
     * Solves Part 1: Counts the number of safe reports.
     * @param lines the input lines
     * @return the number of safe reports
     */
    public static int solvePartOne(@NotNull List<String> lines) {
        int safeCount = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] tokens = line.trim().split("\\s+");
            List<Integer> levels = new ArrayList<>();
            for (String token : tokens) {
                levels.add(Integer.parseInt(token));
            }
            if (isSafeReport(levels)) {
                safeCount++;
            }
        }
        return safeCount;
    }

    /**
     * Checks if a report is safe according to the puzzle rules.
     * @param levels the list of levels
     * @return true if the report is safe, false otherwise
     */
    private static boolean isSafeReport(@NotNull List<Integer> levels) {
        if (levels.size() < 2) return false;
        boolean increasing = true;
        boolean decreasing = true;
        for (int i = 1; i < levels.size(); i++) {
            int diff = levels.get(i) - levels.get(i - 1);
            if (diff < 1 || diff > 3) increasing = false;
            if (diff > -1 || diff < -3) decreasing = false;
            if (levels.get(i).equals(levels.get(i - 1))) return false;
        }
        return increasing || decreasing;
    }

    /**
     * Solves Part 2: Counts the number of safe reports with the Problem Dampener.
     * @param lines the input lines
     * @return the number of safe reports (with at most one level removed)
     */
    public static int solvePartTwo(@NotNull List<String> lines) {
        int safeCount = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] tokens = line.trim().split("\\s+");
            List<Integer> levels = new ArrayList<>();
            for (String token : tokens) {
                levels.add(Integer.parseInt(token));
            }
            if (isSafeReport(levels)) {
                safeCount++;
            } else if (canBeSafeByRemovingOne(levels)) {
                safeCount++;
            }
        }
        return safeCount;
    }

    /**
     * Checks if a report can be made safe by removing a single level.
     * @param levels the list of levels
     * @return true if the report can be made safe by removing one level
     */
    private static boolean canBeSafeByRemovingOne(@NotNull List<Integer> levels) {
        for (int i = 0; i < levels.size(); i++) {
            List<Integer> copy = new ArrayList<>(levels);
            copy.remove(i);
            if (isSafeReport(copy)) {
                return true;
            }
        }
        return false;
    }
}
