package odogwudozilla.year2023.day5;

import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Advent of Code 2023 - Day 5: If You Give A Seed A Fertilizer
 *
 * You take the boat and find the gardener right where you were told he would be: managing a giant "garden" that looks more to you like a farm.
 * ... (see puzzle description for full details)
 *
 * Puzzle link: https://adventofcode.com/2023/day/5
 */
public class IfYouGiveASeedAFertilizerAOC2023Day5 {
    private static final String INPUT_FILE = "/2023/day5/day5_puzzle_data.txt";

    public static void main(String[] args) {
        List<String> inputLines = readInputFile(INPUT_FILE);
        Almanac almanac = Almanac.parse(inputLines);
        long partOneResult = solvePartOne(almanac);
        System.out.println("Part 1: Lowest location number for any seed: " + partOneResult);
        long partTwoResult = solvePartTwo(almanac);
        System.out.println("Part 2: Lowest location number for any seed in range: " + partTwoResult);
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param almanac the parsed Almanac input
     * @return the lowest location number for any seed
     */
    public static long solvePartOne(@NotNull Almanac almanac) {
        long minLocation = Long.MAX_VALUE;
        for (long seed : almanac.seeds) {
            long location = almanac.mapAll(seed);
            if (location < minLocation) {
                minLocation = location;
            }
        }
        return minLocation;
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param almanac the parsed Almanac input
     * @return the lowest location number for any seed in any range
     */
    public static long solvePartTwo(@NotNull Almanac almanac) {
        List<long[]> intervals = new ArrayList<>();
        for (int i = 0; i < almanac.seeds.length; i += 2) {
            long start = almanac.seeds[i];
            long len = almanac.seeds[i + 1];
            intervals.add(new long[]{start, start + len});
        }
        for (RangeMap map : almanac.maps) {
            List<long[]> next = new ArrayList<>();
            while (!intervals.isEmpty()) {
                long[] interval = intervals.remove(intervals.size() - 1);
                boolean mapped = false;
                for (Range r : map.ranges) {
                    long overlapStart = Math.max(interval[0], r.srcStart);
                    long overlapEnd = Math.min(interval[1], r.srcStart + r.len);
                    if (overlapStart < overlapEnd) {
                        // Map the overlapping part
                        next.add(new long[]{r.destStart + (overlapStart - r.srcStart), r.destStart + (overlapEnd - r.srcStart)});
                        // Add left non-overlapping part
                        if (interval[0] < overlapStart) intervals.add(new long[]{interval[0], overlapStart});
                        // Add right non-overlapping part
                        if (overlapEnd < interval[1]) intervals.add(new long[]{overlapEnd, interval[1]});
                        mapped = true;
                        break;
                    }
                }
                if (!mapped) next.add(interval);
            }
            intervals = next;
        }
        long minLocationPart2 = Long.MAX_VALUE;
        for (long[] interval : intervals) {
            if (interval[0] < minLocationPart2) minLocationPart2 = interval[0];
        }
        return minLocationPart2;
    }

    @NotNull
    private static List<String> readInputFile(String resourcePath) {
        List<String> lines = new ArrayList<>();
        try (InputStream is = IfYouGiveASeedAFertilizerAOC2023Day5.class.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to read input file: " + resourcePath, e);
        }
        return lines;
    }

    /**
         * Almanac holds all mapping tables and seeds.
         */
        private record Almanac(long[] seeds, List<RangeMap> maps) {

        static Almanac parse(List<String> lines) {
                List<RangeMap> maps = new ArrayList<>();
                long[] seeds = null;
                Iterator<String> it = lines.iterator();
                while (it.hasNext()) {
                    String line = it.next().trim();
                    if (line.startsWith("seeds:")) {
                        String[] parts = line.substring(7).trim().split(" ");
                        seeds = Arrays.stream(parts).filter(s -> !s.isEmpty()).mapToLong(Long::parseLong).toArray();
                    } else if (line.endsWith("map:")) {
                        List<Range> ranges = new ArrayList<>();
                        while (it.hasNext()) {
                            String l = it.next().trim();
                            if (l.isEmpty()) break;
                            String[] nums = l.split(" ");
                            if (nums.length != 3) continue;
                            long dest = Long.parseLong(nums[0]);
                            long src = Long.parseLong(nums[1]);
                            long len = Long.parseLong(nums[2]);
                            ranges.add(new Range(src, dest, len));
                        }
                        maps.add(new RangeMap(ranges));
                    }
                }
                if (seeds == null) throw new IllegalArgumentException("No seeds found");
                return new Almanac(seeds, maps);
            }

            long mapAll(long value) {
                long v = value;
                for (RangeMap map : maps) {
                    v = map.map(v);
                }
                return v;
            }
        }

    /**
         * RangeMap maps values using a list of ranges.
         */
        private record RangeMap(List<Range> ranges) {
        long map(long value) {
                for (Range r : ranges) {
                    if (r.inSource(value)) return r.map(value);
                }
                return value;
            }
        }

    /**
         * Range represents a mapping from a source range to a destination range.
         */
        private record Range(long srcStart, long destStart, long len) {
        boolean inSource(long v) {
                return v >= srcStart && v < srcStart + len;
            }

        long map(long v) {
                return destStart + (v - srcStart);
            }
        }
}
