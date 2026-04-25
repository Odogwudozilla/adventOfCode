package odogwudozilla.year2020.day4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Advent of Code 2020 - Day 4: Passport Processing
 * <p>
 * Validates batch passport files by checking required fields (Part 1) and
 * additionally validating field values against strict rules (Part 2).
 * Puzzle URL: https://adventofcode.com/2020/day/4
 */
public final class PassportProcessingAOC2020Day4 {

    private static final String INPUT_FILE = "/2020/day4/day4_puzzle_data.txt";

    private static final Set<String> REQUIRED_FIELDS = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

    private static final int BYR_MIN = 1920;
    private static final int BYR_MAX = 2002;
    private static final int IYR_MIN = 2010;
    private static final int IYR_MAX = 2020;
    private static final int EYR_MIN = 2020;
    private static final int EYR_MAX = 2030;
    private static final int HGT_CM_MIN = 150;
    private static final int HGT_CM_MAX = 193;
    private static final int HGT_IN_MIN = 59;
    private static final int HGT_IN_MAX = 76;

    private static final Pattern HCL_PATTERN = Pattern.compile("^#[0-9a-f]{6}$");
    private static final Pattern PID_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Set<String> VALID_ECL_VALUES = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Counts passports that contain all required fields (cid is optional).
     * @param input list of input lines
     * @return the number of valid passports
     */
    private static String solvePartOne(List<String> input) {
        List<Map<String, String>> passports = parsePassports(input);
        long count = passports.stream()
                .filter(passport -> passport.keySet().containsAll(REQUIRED_FIELDS))
                .count();
        return String.valueOf(count);
    }

    /**
     * Counts passports that contain all required fields AND have valid field values.
     * @param input list of input lines
     * @return the number of strictly valid passports
     */
    private static String solvePartTwo(List<String> input) {
        List<Map<String, String>> passports = parsePassports(input);
        long count = passports.stream()
                .filter(passport -> passport.keySet().containsAll(REQUIRED_FIELDS))
                .filter(PassportProcessingAOC2020Day4::areFieldValuesValid)
                .count();
        return String.valueOf(count);
    }

    /**
     * Validates all required field values for a given passport.
     * @param passport map of field names to values
     * @return true if all field values satisfy the rules
     */
    private static boolean areFieldValuesValid(Map<String, String> passport) {
        return isYearInRange(passport.get("byr"), BYR_MIN, BYR_MAX)
                && isYearInRange(passport.get("iyr"), IYR_MIN, IYR_MAX)
                && isYearInRange(passport.get("eyr"), EYR_MIN, EYR_MAX)
                && isHeightValid(passport.get("hgt"))
                && HCL_PATTERN.matcher(passport.get("hcl")).matches()
                && VALID_ECL_VALUES.contains(passport.get("ecl"))
                && PID_PATTERN.matcher(passport.get("pid")).matches();
    }

    /**
     * Checks whether a year string is a four-digit number within the given range.
     * @param value the year value as a string
     * @param min   the minimum valid year (inclusive)
     * @param max   the maximum valid year (inclusive)
     * @return true if the year is valid
     */
    private static boolean isYearInRange(String value, int min, int max) {
        if (value == null || value.length() != 4) {
            return false;
        }
        try {
            int year = Integer.parseInt(value);
            return year >= min && year <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates the height field: a number followed by "cm" or "in" within allowed ranges.
     * @param value the height value as a string
     * @return true if the height is valid
     */
    private static boolean isHeightValid(String value) {
        if (value == null) {
            return false;
        }
        if (value.endsWith("cm")) {
            try {
                int height = Integer.parseInt(value.substring(0, value.length() - 2));
                return height >= HGT_CM_MIN && height <= HGT_CM_MAX;
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (value.endsWith("in")) {
            try {
                int height = Integer.parseInt(value.substring(0, value.length() - 2));
                return height >= HGT_IN_MIN && height <= HGT_IN_MAX;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Parses the input lines into a list of passports, each represented as a map of field names to values.
     * Passports are separated by blank lines.
     * @param lines the raw input lines
     * @return list of passport field maps
     */
    private static List<Map<String, String>> parsePassports(List<String> lines) {
        List<Map<String, String>> passports = new ArrayList<>();
        Map<String, String> current = new HashMap<>();

        for (String line : lines) {
            if (line.isBlank()) {
                if (!current.isEmpty()) {
                    passports.add(current);
                    current = new HashMap<>();
                }
            } else {
                for (String token : line.split(" ")) {
                    String[] parts = token.split(":");
                    current.put(parts[0], parts[1]);
                }
            }
        }

        // Add the last passport if the file does not end with a blank line
        if (!current.isEmpty()) {
            passports.add(current);
        }

        return passports;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = PassportProcessingAOC2020Day4.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
