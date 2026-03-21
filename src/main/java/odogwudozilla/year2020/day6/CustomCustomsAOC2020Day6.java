/**
 * Advent of Code 2020 Day 6: Custom Customs
 * As your flight approaches the regional airport where you'll switch to a much larger plane, customs declaration forms are distributed to the passengers.
 * The form asks a series of 26 yes-or-no questions marked a through z. All you need to do is identify the questions for which anyone in your group answers "yes".
 * For each group, count the number of questions to which anyone answered "yes". What is the sum of those counts?
 *
 * Puzzle link: https://adventofcode.com/2020/day/6
 */
package odogwudozilla.year2020.day6;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.IOException;

public class CustomCustomsAOC2020Day6 {
    private static final String INPUT_PATH = "src/main/resources/2020/day6/day6_puzzle_data.txt";

    /**
     * Solves Part 1 of the puzzle: sum of counts of questions anyone answered "yes" per group.
     * @param inputLines the input lines from the puzzle data file
     * @return the sum of counts
     */
    public static int solvePartOne(List<String> inputLines) {
        int sum = 0;
        Set<Character> groupAnswers = new HashSet<>();
        for (String line : inputLines) {
            if (line.trim().isEmpty()) {
                sum += groupAnswers.size();
                groupAnswers.clear();
            } else {
                for (char c : line.toCharArray()) {
                    groupAnswers.add(c);
                }
            }
        }
        sum += groupAnswers.size(); // Add last group
        return sum;
    }

    /**
     * Solves Part 2 of the puzzle: sum of counts of questions everyone answered "yes" per group.
     * @param inputLines the input lines from the puzzle data file
     * @return the sum of counts
     */
    public static int solvePartTwo(List<String> inputLines) {
        int sum = 0;
        Set<Character> groupAnswers = null;
        for (String line : inputLines) {
            if (line.trim().isEmpty()) {
                if (groupAnswers != null) {
                    sum += groupAnswers.size();
                }
                groupAnswers = null;
            } else {
                Set<Character> personAnswers = new HashSet<>();
                for (char c : line.toCharArray()) {
                    personAnswers.add(c);
                }
                if (groupAnswers == null) {
                    groupAnswers = personAnswers;
                } else {
                    groupAnswers.retainAll(personAnswers);
                }
            }
        }
        if (groupAnswers != null) {
            sum += groupAnswers.size();
        }
        return sum;
    }

    public static void main(String[] args) throws IOException {
        List<String> inputLines = Files.readAllLines(Paths.get(INPUT_PATH));
        int partOneResult = solvePartOne(inputLines);
        System.out.println("Part 1: " + partOneResult);
        int partTwoResult = solvePartTwo(inputLines);
        System.out.println("Part 2: " + partTwoResult);
    }
}
