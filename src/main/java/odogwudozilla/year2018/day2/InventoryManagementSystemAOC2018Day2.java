package odogwudozilla.year2018.day2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Inventory Management System (Advent of Code 2018 Day 2)
 *
 * You scan box IDs to count how many contain exactly two of any letter and how many contain exactly three of any letter. Multiply these counts to get a checksum.
 * Official puzzle link: https://adventofcode.com/2018/day/2
 */
public class InventoryManagementSystemAOC2018Day2 {
    private static final String INPUT_PATH = "src/main/resources/2018/day2/day2_puzzle_data.txt";
    private static final Logger LOGGER = Logger.getLogger(InventoryManagementSystemAOC2018Day2.class.getName());

    /**
     * Entry point for solving Advent of Code 2018 Day 2 Part 1.
     * Reads box IDs from the input file and calculates the checksum.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        LOGGER.info("main - Starting Inventory Management System checksum calculation");
        try {
            List<String> boxIds = Files.readAllLines(Paths.get(INPUT_PATH));
            int checksum = solvePartOne(boxIds);
            LOGGER.info("main - Checksum result: " + checksum);
            System.out.println("Checksum for your list of box IDs: " + checksum);

            // Part 2: Find the two box IDs that differ by exactly one character
            LOGGER.info("main - Starting Part 2: Finding correct box IDs");
            String commonLetters = solvePartTwo(boxIds);
            LOGGER.info("main - Common letters between correct box IDs: " + commonLetters);
            System.out.println("Common letters between the correct box IDs: " + commonLetters);
        } catch (Exception e) {
            LOGGER.severe("main - Error reading input or calculating checksum: " + e.getMessage());
            // Log stack trace for debugging purposes
            for (StackTraceElement ste : e.getStackTrace()) {
                LOGGER.severe("main - " + ste.toString());
            }
        }
    }

    /**
     * Standardised method for Part 1.
     */
    private static int solvePartOne(List<String> boxIds) {
        int twoCount = 0;
        int threeCount = 0;
        for (String id : boxIds) {
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : id.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }
            boolean hasTwo = false;
            boolean hasThree = false;
            for (int count : freq.values()) {
                if (count == 2) {
                    hasTwo = true;
                }
                if (count == 3) {
                    hasThree = true;
                }
            }
            if (hasTwo) {
                twoCount++;
            }
            if (hasThree) {
                threeCount++;
            }
        }
        return twoCount * threeCount;
    }

    /**
     * Standardised method for Part 2.
     */
    private static String solvePartTwo(List<String> boxIds) {
        return findCommonLetters(boxIds);
    }

    /**
     * Finds the common letters between the two correct box IDs that differ by exactly one character.
     *
     * @param boxIds List of box IDs
     * @return Common letters between the two correct box IDs
     */
    private static String findCommonLetters(List<String> boxIds) {
        // Compare each pair of box IDs
        for (int i = 0; i < boxIds.size(); i++) {
            String id1 = boxIds.get(i);
            for (int j = i + 1; j < boxIds.size(); j++) {
                String id2 = boxIds.get(j);
                int diffCount = 0;
                int diffIndex = -1;
                for (int k = 0; k < id1.length(); k++) {
                    if (id1.charAt(k) != id2.charAt(k)) {
                        diffCount++;
                        diffIndex = k;
                        if (diffCount > 1) {
                            break;
                        }
                    }
                }
                if (diffCount == 1) {
                    // Return the common letters (excluding the differing character)
                    return id1.substring(0, diffIndex) + id1.substring(diffIndex + 1);
                }
            }
        }
        LOGGER.warning("findCommonLetters - No matching box IDs found");
        return "";
    }
}
