package odogwudozilla.year2021.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Advent of Code 2021 - Day 21: Dirac Dice
 * <p>
 * This puzzle involves simulating a dice game between two players on a circular board with 10 spaces.
 * Part 1 uses a deterministic 100-sided die that rolls in sequence (1, 2, 3, ..., 100, 1, 2, ...).
 * Part 2 uses a quantum three-sided die that splits the universe on each roll.
 * Players take turns rolling three times, moving forward, and adding their position to their score.
 * <p>
 * Puzzle URL: <a href="https://adventofcode.com/2021/day/21">https://adventofcode.com/2021/day/21</a>
 */
public class DiracDiceAOC2021Day21 {

    private static final int BOARD_SIZE = 10;
    private static final int PART_ONE_WIN_SCORE = 1000;
    private static final int PART_TWO_WIN_SCORE = 21;
    private static final int DIE_SIZE = 100;

    // Frequency of sums when rolling a 3-sided die three times
    // Sum 3 (1+1+1): 1 way
    // Sum 4 (1+1+2, 1+2+1, 2+1+1): 3 ways
    // Sum 5 (1+1+3, 1+3+1, 3+1+1, 1+2+2, 2+1+2, 2+2+1): 6 ways
    // Sum 6 (1+2+3, 1+3+2, 2+1+3, 2+3+1, 3+1+2, 3+2+1, 2+2+2): 7 ways
    // Sum 7 (2+2+3, 2+3+2, 3+2+2, 1+3+3, 3+1+3, 3+3+1): 6 ways
    // Sum 8 (2+3+3, 3+2+3, 3+3+2): 3 ways
    // Sum 9 (3+3+3): 1 way
    private static final Map<Integer, Long> ROLL_FREQUENCIES = Map.of(
        3, 1L, 4, 3L, 5, 6L, 6, 7L, 7, 6L, 8, 3L, 9, 1L
    );

    private static Map<String, long[]> memo;

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/2021/day21/day21_puzzle_data.txt")
            );

            int player1Start = parseStartingPosition(lines.get(0));
            int player2Start = parseStartingPosition(lines.get(1));

            long partOneResult = solvePartOne(player1Start, player2Start);
            System.out.println("Part 1 - Result: " + partOneResult);

            long partTwoResult = solvePartTwo(player1Start, player2Start);
            System.out.println("Part 2 - Result: " + partTwoResult);

        } catch (IOException e) {
            System.err.println("Error reading puzzle input: " + e.getMessage());
        }
    }

    /**
     * Parses the starting position from input line.
     * @param line the input line containing "Player X starting position: Y"
     * @return the starting position
     */
    private static int parseStartingPosition(String line) {
        return Integer.parseInt(line.split(": ")[1]);
    }

    /**
     * Solves Part 1 using a deterministic die.
     * @param player1Start starting position for player 1
     * @param player2Start starting position for player 2
     * @return the losing player's score multiplied by the number of die rolls
     */
    private static long solvePartOne(int player1Start, int player2Start) {
        int pos1 = player1Start;
        int pos2 = player2Start;
        int score1 = 0;
        int score2 = 0;
        int dieValue = 1;
        int rollCount = 0;
        boolean player1Turn = true;

        while (score1 < PART_ONE_WIN_SCORE && score2 < PART_ONE_WIN_SCORE) {
            // Roll die three times and calculate total movement
            int movement = 0;
            for (int i = 0; i < 3; i++) {
                movement += dieValue;
                dieValue++;
                if (dieValue > DIE_SIZE) {
                    dieValue = 1;
                }
                rollCount++;
            }

            if (player1Turn) {
                pos1 = movePosition(pos1, movement);
                score1 += pos1;
            } else {
                pos2 = movePosition(pos2, movement);
                score2 += pos2;
            }

            player1Turn = !player1Turn;
        }

        int losingScore = Math.min(score1, score2);
        return (long) losingScore * rollCount;
    }

    /**
     * Solves Part 2 using quantum dice (3-sided die that splits universes).
     * @param player1Start starting position for player 1
     * @param player2Start starting position for player 2
     * @return the number of universes in which the winning player wins
     */
    private static long solvePartTwo(int player1Start, int player2Start) {
        memo = new HashMap<>();
        long[] wins = countWins(player1Start, 0, player2Start, 0, true);
        return Math.max(wins[0], wins[1]);
    }

    /**
     * Recursively counts wins for both players across all quantum universes.
     * @param pos1 current position of player 1
     * @param score1 current score of player 1
     * @param pos2 current position of player 2
     * @param score2 current score of player 2
     * @param player1Turn whether it's player 1's turn
     * @return array of [player1Wins, player2Wins] across all universes from this state
     */
    private static long[] countWins(int pos1, int score1, int pos2, int score2, boolean player1Turn) {
        // Check if either player has already won
        if (score1 >= PART_TWO_WIN_SCORE) {
            return new long[]{1, 0};
        }
        if (score2 >= PART_TWO_WIN_SCORE) {
            return new long[]{0, 1};
        }

        // Create state key for memoization
        String state = pos1 + "," + score1 + "," + pos2 + "," + score2 + "," + player1Turn;
        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        long[] totalWins = new long[]{0, 0};

        // For each possible sum of three die rolls
        for (Map.Entry<Integer, Long> entry : ROLL_FREQUENCIES.entrySet()) {
            int rollSum = entry.getKey();
            long frequency = entry.getValue();

            if (player1Turn) {
                int newPos1 = movePosition(pos1, rollSum);
                int newScore1 = score1 + newPos1;
                long[] wins = countWins(newPos1, newScore1, pos2, score2, false);
                totalWins[0] += wins[0] * frequency;
                totalWins[1] += wins[1] * frequency;
            } else {
                int newPos2 = movePosition(pos2, rollSum);
                int newScore2 = score2 + newPos2;
                long[] wins = countWins(pos1, score1, newPos2, newScore2, true);
                totalWins[0] += wins[0] * frequency;
                totalWins[1] += wins[1] * frequency;
            }
        }

        memo.put(state, totalWins);
        return totalWins;
    }

    /**
     * Moves a position forward on the circular board.
     * @param currentPos current position (1-10)
     * @param steps number of steps to move forward
     * @return new position (1-10)
     */
    private static int movePosition(int currentPos, int steps) {
        return ((currentPos - 1 + steps) % BOARD_SIZE) + 1;
    }
}







