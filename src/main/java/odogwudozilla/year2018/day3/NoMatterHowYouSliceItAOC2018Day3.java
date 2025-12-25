package odogwudozilla.year2018.day3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Advent of Code 2018 Day 3 - No Matter How You Slice It
 *
 * The Elves are working with a large piece of fabric and need to determine which areas
 * have overlapping claims. Each claim specifies a rectangular area by its ID, left offset,
 * top offset, width, and height. The puzzle asks us to find how many square inches of
 * fabric are covered by two or more claims (Part 1) and which claim doesn't overlap with
 * any other claim (Part 2).
 *
 * Official Puzzle: https://adventofcode.com/2018/day/3
 */
public class NoMatterHowYouSliceItAOC2018Day3 {

    private static final String PUZZLE_INPUT_PATH = "src/main/resources/2018/day3/day3_puzzle_data.txt";
    private static final Pattern CLAIM_PATTERN = Pattern.compile(
        "#(\\d+)\\s@\\s(\\d+),(\\d+):\\s(\\d+)x(\\d+)"
    );

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(PUZZLE_INPUT_PATH));
        Map<Integer, Claim> claims = new HashMap<>();
        for (String line : lines) {
            Claim claim = parseClaim(line);
            if (claim != null) {
                claims.put(claim.id, claim);
            }
        }
        long overlapCount = solvePartOne(claims);
        System.out.println("Part 1 - Square inches with two or more claims: " + overlapCount);
        int nonOverlappingClaimId = solvePartTwo(claims);
        System.out.println("Part 2 - Claim with no overlaps: #" + nonOverlappingClaimId);
    }

    /**
     * Standardised method for Part 1.
     */
    private static long solvePartOne(Map<Integer, Claim> claims) {
        Map<String, Integer> fabricCoverage = new HashMap<>();
        for (Claim claim : claims.values()) {
            for (int x = claim.left; x < claim.left + claim.width; x++) {
                for (int y = claim.top; y < claim.top + claim.height; y++) {
                    String coordinate = x + "," + y;
                    fabricCoverage.put(coordinate, fabricCoverage.getOrDefault(coordinate, 0) + 1);
                }
            }
        }
        return fabricCoverage.values().stream().filter(count -> count >= 2).count();
    }

    /**
     * Standardised method for Part 2.
     */
    private static int solvePartTwo(Map<Integer, Claim> claims) {
        Map<String, Integer> fabricCoverage = new HashMap<>();
        for (Claim claim : claims.values()) {
            for (int x = claim.left; x < claim.left + claim.width; x++) {
                for (int y = claim.top; y < claim.top + claim.height; y++) {
                    String coordinate = x + "," + y;
                    fabricCoverage.put(coordinate, fabricCoverage.getOrDefault(coordinate, 0) + 1);
                }
            }
        }
        Set<Integer> overlappingClaimIds = new HashSet<>();
        for (Map.Entry<String, Integer> entry : fabricCoverage.entrySet()) {
            if (entry.getValue() >= 2) {
                String[] coords = entry.getKey().split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                for (Claim claim : claims.values()) {
                    if (claim.covers(x, y)) {
                        overlappingClaimIds.add(claim.id);
                    }
                }
            }
        }
        for (Claim claim : claims.values()) {
            if (!overlappingClaimIds.contains(claim.id)) {
                return claim.id;
            }
        }
        return -1;
    }

    private static Claim parseClaim(String line) {
        Matcher matcher = CLAIM_PATTERN.matcher(line);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            int left = Integer.parseInt(matcher.group(2));
            int top = Integer.parseInt(matcher.group(3));
            int width = Integer.parseInt(matcher.group(4));
            int height = Integer.parseInt(matcher.group(5));
            return new Claim(id, left, top, width, height);
        }
        return null;
    }

    static class Claim {
        int id;
        int left;
        int top;
        int width;
        int height;

        Claim(int id, int left, int top, int width, int height) {
            this.id = id;
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
        }

        boolean covers(int x, int y) {
            return x >= left && x < left + width && y >= top && y < top + height;
        }
    }
}
