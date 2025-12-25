package odogwudozilla.year2022.day2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * Solves Advent of Code 2022 Day 2: Rock Paper Scissors
 * <p>
 * The Elves are running a Rock Paper Scissors tournament. You are given an encrypted strategy guide to calculate your score.
 * <p>
 * Part 1: The second column is your move (X=Rock, Y=Paper, Z=Scissors).
 * Part 2: The second column is the desired outcome (X=lose, Y=draw, Z=win).
 * <p>
 * Official puzzle link: https://adventofcode.com/2022/day/2
 *
 * @param args Command line arguments
 * @return None
 */
public class RockPaperScissorsAOC2022Day2 {
    private static final String INPUT_PATH = "src/main/resources/2022/day2/day2_puzzle_data.txt";
    private static final int SCORE_ROCK = 1;
    private static final int SCORE_PAPER = 2;
    private static final int SCORE_SCISSORS = 3;
    private static final int SCORE_LOSE = 0;
    private static final int SCORE_DRAW = 3;
    private static final int SCORE_WIN = 6;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
        System.out.println("main - Part 1 valid password count: " + solvePart1(lines));
        System.out.println("main - Part 2 valid password count: " + solvePart2(lines));
    }

    /**
     * Calculates the total score for Part 1.
     * @param lines The strategy guide input
     * @return Total score
     */
    private static int solvePart1(List<String> lines) {
        int totalScore = 0;
        for (String line : lines) {
            String[] parts = line.split(" ");
            if (parts.length != 2) continue;
            char opponent = parts[0].charAt(0);
            char player = parts[1].charAt(0);
            int shapeScore = 0;
            int outcomeScore = 0;
            switch (player) {
                case 'X': shapeScore = SCORE_ROCK; break;
                case 'Y': shapeScore = SCORE_PAPER; break;
                case 'Z': shapeScore = SCORE_SCISSORS; break;
            }
            switch (opponent) {
                case 'A': // Rock
                    if (player == 'X') outcomeScore = SCORE_DRAW;
                    else if (player == 'Y') outcomeScore = SCORE_WIN;
                    else outcomeScore = SCORE_LOSE;
                    break;
                case 'B': // Paper
                    if (player == 'X') outcomeScore = SCORE_LOSE;
                    else if (player == 'Y') outcomeScore = SCORE_DRAW;
                    else outcomeScore = SCORE_WIN;
                    break;
                case 'C': // Scissors
                    if (player == 'X') outcomeScore = SCORE_WIN;
                    else if (player == 'Y') outcomeScore = SCORE_LOSE;
                    else outcomeScore = SCORE_DRAW;
                    break;
            }
            totalScore += shapeScore + outcomeScore;
        }
        return totalScore;
    }

    /**
     * Calculates the total score for Part 2.
     * @param lines The strategy guide input
     * @return Total score
     */
    private static int solvePart2(List<String> lines) {
        int totalScore = 0;
        for (String line : lines) {
            String[] parts = line.split(" ");
            if (parts.length != 2) continue;
            char opponent = parts[0].charAt(0);
            char outcome = parts[1].charAt(0);
            int shapeScore = 0;
            int outcomeScore = 0;
            switch (outcome) {
                case 'X': outcomeScore = SCORE_LOSE; break;
                case 'Y': outcomeScore = SCORE_DRAW; break;
                case 'Z': outcomeScore = SCORE_WIN; break;
            }
            // Determine what to play
            char player = 'X';
            switch (opponent) {
                case 'A': // Rock
                    if (outcome == 'X') player = 'Z'; // lose: play Scissors
                    else if (outcome == 'Y') player = 'X'; // draw: play Rock
                    else player = 'Y'; // win: play Paper
                    break;
                case 'B': // Paper
                    if (outcome == 'X') player = 'X'; // lose: play Rock
                    else if (outcome == 'Y') player = 'Y'; // draw: play Paper
                    else player = 'Z'; // win: play Scissors
                    break;
                case 'C': // Scissors
                    if (outcome == 'X') player = 'Y'; // lose: play Paper
                    else if (outcome == 'Y') player = 'Z'; // draw: play Scissors
                    else player = 'X'; // win: play Rock
                    break;
            }
            switch (player) {
                case 'X': shapeScore = SCORE_ROCK; break;
                case 'Y': shapeScore = SCORE_PAPER; break;
                case 'Z': shapeScore = SCORE_SCISSORS; break;
            }
            totalScore += shapeScore + outcomeScore;
        }
        return totalScore;
    }
}

