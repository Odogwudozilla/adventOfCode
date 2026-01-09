package odogwudozilla.year2016.day20;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

/**
 * Firewall Rules (Advent of Code 2016 Day 20)
 * <p>
 * You'd like to set up a small hidden computer here so you can use it to get back into the network later. However, the corporate firewall only allows communication with certain external IP addresses.
 * You've retrieved the list of blocked IPs from the firewall, but the list seems to be messy and poorly maintained, and it's not clear which IPs are allowed. Also, rather than being written in dot-decimal notation, they are written as plain 32-bit integers, which can have any value from 0 through 4294967295, inclusive.
 * <p>
 * Official puzzle link: https://adventofcode.com/2016/day/20
 *
 * @author Advent of Code workflow
 */
public class FirewallRulesAOC2016Day20 {
    private static final long MAX_IP = 4294967295L;

    /**
     * Main entry point for the solution.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        String inputPath = "src/main/resources/2016/day20/day20_puzzle_data.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputPath));
            List<long[]> ranges = parseRanges(lines);
            long partOneResult = solvePartOne(ranges);
            System.out.println("solvePartOne - Lowest-valued allowed IP: " + partOneResult);
            long partTwoResult = solvePartTwo(ranges);
            System.out.println("solvePartTwo - Total number of allowed IPs: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Parses the input lines into a list of blocked IP ranges.
     * @param lines The input lines
     * @return List of [start, end] pairs
     */
    private static List<long[]> parseRanges(List<String> lines) {
        List<long[]> ranges = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0].trim());
            long end = Long.parseLong(parts[1].trim());
            ranges.add(new long[]{start, end});
        }
        ranges.sort(Comparator.comparingLong(a -> a[0]));
        return ranges;
    }

    /**
     * Finds the lowest-valued IP that is not blocked.
     * @param ranges List of blocked ranges
     * @return The lowest allowed IP
     */
    public static long solvePartOne(List<long[]> ranges) {
        long ip = 0;
        for (long[] range : ranges) {
            if (ip < range[0]) {
                // Found an allowed IP
                return ip;
            }
            ip = Math.max(ip, range[1] + 1);
        }
        return ip <= MAX_IP ? ip : -1;
    }

    /**
     * Counts the total number of allowed IPs.
     * @param ranges List of blocked ranges
     * @return Number of allowed IPs
     */
    public static long solvePartTwo(List<long[]> ranges) {
        long allowed = 0;
        long ip = 0;
        for (long[] range : ranges) {
            if (ip < range[0]) {
                allowed += range[0] - ip;
            }
            ip = Math.max(ip, range[1] + 1);
        }
        if (ip <= MAX_IP) {
            allowed += MAX_IP - ip + 1;
        }
        return allowed;
    }
}

