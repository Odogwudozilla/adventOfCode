package odogwudozilla.year2020.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Advent of Code 2020 - Day 19: Monster Messages
 *
 * This puzzle involves validating messages against a set of rules that build upon each other.
 * Rules can match literal characters or reference other rules, with support for alternatives (|).
 * The goal is to determine how many messages completely match rule 0.
 *
 * Puzzle: https://adventofcode.com/2020/day/19
 */
public class MonsterMessagesAOC2020Day19 {

    private static final String INPUT_FILE = "src/main/resources/2020/day19/day19_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of(INPUT_FILE));

            // Parse input into rules and messages
            Map<Integer, String> rules = new HashMap<>();
            List<String> messages = new ArrayList<>();
            boolean readingMessages = false;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    readingMessages = true;
                    continue;
                }

                if (!readingMessages) {
                    // Parse rule
                    String[] parts = line.split(": ");
                    int ruleNumber = Integer.parseInt(parts[0]);
                    rules.put(ruleNumber, parts[1]);
                } else {
                    // Parse message
                    messages.add(line);
                }
            }

            // Part 1
            long part1 = solvePartOne(rules, messages);
            System.out.println("Part 1 - Messages matching rule 0: " + part1);

            // Part 2
            long part2 = solvePartTwo(rules, messages);
            System.out.println("Part 2 - Messages matching rule 0 (with updated rules): " + part2);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solves Part 1: Count how many messages completely match rule 0.
     * @param rules the rule definitions
     * @param messages the messages to validate
     * @return the number of valid messages
     */
    private static long solvePartOne(Map<Integer, String> rules, List<String> messages) {
        String regex = buildRegex(rules, 0);
        Pattern pattern = Pattern.compile("^" + regex + "$");

        return messages.stream()
                .filter(msg -> pattern.matcher(msg).matches())
                .count();
    }

    /**
     * Solves Part 2: Count valid messages after updating rules 8 and 11.
     * Rule 8: 42 | 42 8 (one or more of rule 42)
     * Rule 11: 42 31 | 42 11 31 (balanced: n times rule 42 followed by n times rule 31)
     * @param rules the rule definitions
     * @param messages the messages to validate
     * @return the number of valid messages
     */
    private static long solvePartTwo(Map<Integer, String> rules, List<String> messages) {
        Map<Integer, String> updatedRules = new HashMap<>(rules);

        // Update rule 8: 42 | 42 8 becomes 42+ (one or more)
        // Update rule 11: 42 31 | 42 11 31 becomes balanced pattern
        // Since we can't express arbitrary recursion in regex, we'll expand to a reasonable depth

        // Rule 8: 42 | 42 8 -> 42+ (one or more occurrences of 42)
        String rule42 = buildRegex(updatedRules, 42);
        String rule31 = buildRegex(updatedRules, 31);

        // For rule 8: just match one or more of rule 42
        // For rule 11: match n occurrences of 42 followed by n occurrences of 31 (n >= 1)
        // Since we need finite regex, we'll try up to a reasonable depth (e.g., 5)

        long count = 0;
        for (String message : messages) {
            if (matchesWithLoops(message, rule42, rule31)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Check if a message matches the pattern with updated rules 8 and 11.
     * Rule 0: 8 11
     * Rule 8: 42+ (one or more)
     * Rule 11: 42{n} 31{n} for some n >= 1
     * @param message the message to check
     * @param rule42Regex the regex for rule 42
     * @param rule31Regex the regex for rule 31
     * @return true if the message matches
     */
    private static boolean matchesWithLoops(String message, String rule42Regex, String rule31Regex) {
        Pattern p42 = Pattern.compile("^(" + rule42Regex + ")");
        Pattern p31 = Pattern.compile("^(" + rule31Regex + ")");

        // Count how many times rule 42 and rule 31 match from the start
        List<Integer> rule42Positions = new ArrayList<>();
        List<Integer> rule31Positions = new ArrayList<>();

        String remaining = message;
        int position = 0;

        // Match rule 42 as many times as possible
        while (true) {
            var matcher = p42.matcher(remaining);
            if (matcher.find()) {
                int matchLength = matcher.group(1).length();
                rule42Positions.add(position);
                position += matchLength;
                remaining = remaining.substring(matchLength);
            } else {
                break;
            }
        }

        // Match rule 31 as many times as possible
        while (true) {
            var matcher = p31.matcher(remaining);
            if (matcher.find()) {
                int matchLength = matcher.group(1).length();
                rule31Positions.add(position);
                position += matchLength;
                remaining = remaining.substring(matchLength);
            } else {
                break;
            }
        }

        // Check if we've consumed the entire message
        if (!remaining.isEmpty()) {
            return false;
        }

        int count42 = rule42Positions.size();
        int count31 = rule31Positions.size();

        // Rule 0: 8 11 where 8 = 42+ and 11 = 42{n} 31{n}
        // This means we need at least 2 occurrences of 42 (one for rule 8, one for rule 11)
        // and at least 1 occurrence of 31 (for rule 11)
        // Also: count42 > count31 and count31 >= 1
        return count42 > count31 && count31 >= 1;
    }

    /**
     * Build a regular expression from the rules starting at a given rule number.
     * @param rules the rule definitions
     * @param ruleNum the rule number to start from
     * @return the regular expression as a string
     */
    private static String buildRegex(Map<Integer, String> rules, int ruleNum) {
        String rule = rules.get(ruleNum);

        // If it's a literal character rule
        if (rule.startsWith("\"")) {
            return rule.substring(1, 2);
        }

        // If it contains alternatives (|)
        if (rule.contains("|")) {
            String[] alternatives = rule.split(" \\| ");
            StringBuilder sb = new StringBuilder("(?:");
            for (int i = 0; i < alternatives.length; i++) {
                if (i > 0) {
                    sb.append("|");
                }
                sb.append(buildRegexFromSequence(rules, alternatives[i]));
            }
            sb.append(")");
            return sb.toString();
        }

        // It's a sequence of rule references
        return buildRegexFromSequence(rules, rule);
    }

    /**
     * Build a regex from a sequence of rule numbers.
     * @param rules the rule definitions
     * @param sequence the sequence of rule numbers as a string
     * @return the regular expression as a string
     */
    private static String buildRegexFromSequence(Map<Integer, String> rules, String sequence) {
        String[] parts = sequence.trim().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            int refRuleNum = Integer.parseInt(part);
            sb.append(buildRegex(rules, refRuleNum));
        }
        return sb.toString();
    }
}

