package odogwudozilla.year2023.day2;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 2: Cube Conundrum ---
 * You're launched high into the atmosphere! The apex of your trajectory just barely reaches the surface of a large island floating in the sky. You gently land in a fluffy pile of leaves. It's quite cold, but you don't see much snow. An Elf runs over to greet you.
 *
 * The Elf explains that you've arrived at Snow Island and apologises for the lack of snow. He'll be happy to explain the situation, but it's a bit of a walk, so you have some time. They don't get many visitors up here; would you like to play a game in the meantime?
 *
 * As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue. Each time you play this game, he will hide a secret number of cubes of each colour in the bag, and your goal is to figure out information about the number of cubes.
 *
 * To get information, once a bag has been loaded with cubes, the Elf will reach into the bag, grab a handful of random cubes, show them to you, and then put them back in the bag. He'll do this a few times per game.
 *
 * You play several games and record the information from each game (your puzzle input). Each game is listed with its ID number followed by a semicolon-separated list of subsets of cubes that were revealed from the bag.
 *
 * Official puzzle URL: https://adventofcode.com/2023/day/2
 */
public class CubeConundrumAOC2023Day2 {
    private static final int MAX_RED = 12;
    private static final int MAX_GREEN = 13;
    private static final int MAX_BLUE = 14;
    private static final String INPUT_PATH = "src/main/resources/2023/day2/day2_puzzle_data.txt";

    /**
     * Entry point for the Cube Conundrum puzzle solution.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Path.of(INPUT_PATH));
            int partOneResult = solvePartOne(lines);
            System.out.println("Part 1: " + partOneResult);
            int partTwoResult = solvePartTwo(lines);
            System.out.println("Part 2: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of the Cube Conundrum puzzle.
     * @param lines the input lines from the puzzle data file
     * @return the sum of the IDs of possible games
     */
    public static int solvePartOne(@NotNull List<String> lines) {
        int sum = 0;
        Pattern gamePattern = Pattern.compile("Game (\\d+): (.*)");
        for (String line : lines) {
            Matcher matcher = gamePattern.matcher(line);
            if (!matcher.matches()) continue;
            int gameId = Integer.parseInt(matcher.group(1));
            String[] rounds = matcher.group(2).split("; ");
            boolean possible = true;
            for (String round : rounds) {
                int red = getCount(round, "red");
                int green = getCount(round, "green");
                int blue = getCount(round, "blue");
                if (red > MAX_RED || green > MAX_GREEN || blue > MAX_BLUE) {
                    possible = false;
                    break;
                }
            }
            if (possible) {
                sum += gameId;
            }
        }
        return sum;
    }

    /**
     * Helper to extract the count of a specific colour from a round string.
     * @param round the round string
     * @param colour the colour to extract (red, green, blue)
     * @return the count of the specified colour, or 0 if not present
     */
    private static int getCount(@NotNull String round, @NotNull String colour) {
        Pattern p = Pattern.compile("(\\d+) " + colour);
        Matcher m = p.matcher(round);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    /**
     * Solves Part 2 of the Cube Conundrum puzzle.
     * @param lines the input lines from the puzzle data file
     * @return the sum of the power of the minimum sets of cubes for all games
     */
    public static int solvePartTwo(@NotNull List<String> lines) {
        int sum = 0;
        Pattern gamePattern = Pattern.compile("Game (\\d+): (.*)");
        for (String line : lines) {
            Matcher matcher = gamePattern.matcher(line);
            if (!matcher.matches()) continue;
            String[] rounds = matcher.group(2).split("; ");
            int maxRed = 0, maxGreen = 0, maxBlue = 0;
            for (String round : rounds) {
                maxRed = Math.max(maxRed, getCount(round, "red"));
                maxGreen = Math.max(maxGreen, getCount(round, "green"));
                maxBlue = Math.max(maxBlue, getCount(round, "blue"));
            }
            int power = maxRed * maxGreen * maxBlue;
            sum += power;
        }
        return sum;
    }
}
