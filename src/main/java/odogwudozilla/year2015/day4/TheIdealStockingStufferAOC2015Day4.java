package odogwudozilla.year2015.day4;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.List;

/**
 * The Ideal Stocking Stuffer (Advent of Code 2015, Day 4)
 * Santa needs help mining AdventCoins by finding the lowest positive number that, when appended to the secret key,
 * produces an MD5 hash starting with five zeroes.
 * Official puzzle URL: https://adventofcode.com/2015/day/4
 * @author Advent of Code
 */
public class TheIdealStockingStufferAOC2015Day4 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/2015/day4/day4_puzzle_data.txt"));
        String secretKey = lines.get(0).trim();
        System.out.println("Part 1: " + solvePartOne(secretKey));
        System.out.println("Part 2: " + solvePartTwo(secretKey));
    }

    /**
     * Finds the lowest positive number that produces an MD5 hash starting with five zeroes when appended to the secret key.
     * @param secretKey the puzzle input
     * @return the lowest positive number
     */
    public static int solvePartOne(@NotNull String secretKey) throws Exception {
        final String prefix = "00000";
        MessageDigest md = MessageDigest.getInstance("MD5");
        int number = 1;
        while (true) {
            String input = secretKey + number;
            byte[] hash = md.digest(input.getBytes());
            String hex = bytesToHex(hash);
            if (hex.startsWith(prefix)) {
                return number;
            }
            number++;
            md.reset();
        }
    }

    /**
     * Optimised: Finds the lowest positive number that produces an MD5 hash starting with six zeroes when appended to
     * the secret key, using parallel streams.
     * @param secretKey the puzzle input
     * @return the lowest positive number for part two
     */
    public static int solvePartTwo(@NotNull String secretKey) throws Exception {
        final String prefix = "000000";
        return java.util.stream.IntStream.iterate(1, i -> i + 1)
                .parallel()
                .filter(number -> {
                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        String input = secretKey + number;
                        byte[] hash = md.digest(input.getBytes());
                        String hex = bytesToHex(hash);
                        return hex.startsWith(prefix);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst()
                .orElseThrow();
    }

    /**
     * Converts a byte array to a hexadecimal string.
     * @param bytes the byte array
     * @return the hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
