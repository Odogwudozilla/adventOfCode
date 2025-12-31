package odogwudozilla.year2021.day4;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Giant Squid (Advent of Code 2021 Day 4)
 *
 * You're already almost 1.5km (almost a mile) below the surface of the ocean, already so deep that you can't see any sunlight. What you can see, however, is a giant squid that has attached itself to the outside of your submarine.
 *
 * Maybe it wants to play bingo?
 *
 * Bingo is played on a set of boards each consisting of a 5x5 grid of numbers. Numbers are chosen at random, and the chosen number is marked on all boards on which it appears. (Numbers may not appear on all boards.) If all numbers in any row or any column of a board are marked, that board wins. (Diagonals don't count.)
 *
 * The submarine has a bingo subsystem to help passengers (currently, you and the giant squid) pass the time. It automatically generates a random order in which to draw numbers and a random set of boards (your puzzle input).
 *
 * Official puzzle link: https://adventofcode.com/2021/day/4
 */
public class GiantSquidAOC2021Day4 {
    private static final String INPUT_PATH = "src/main/resources/2021/day4/day4_puzzle_data.txt";
    private static final int BOARD_SIZE = 5;

    public static void main(String[] args) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(INPUT_PATH));
        } catch (Exception e) {
            System.err.println("main - Failed to read input file: " + e.getMessage());
            return;
        }
        int partOneResult = solvePartOne(lines);
        System.out.println("Part 1: " + partOneResult);
        int partTwoResult = solvePartTwo(lines);
        System.out.println("Part 2: " + partTwoResult);
    }

    /**
     * Solves Part 1: Find the score of the first winning bingo board.
     * @param lines The puzzle input lines
     * @return The final score of the first winning board
     */
    public static int solvePartOne(List<String> lines) {
        // Parse drawn numbers
        String[] drawNumbers = lines.get(0).split(",");
        List<Integer> draws = Stream.of(drawNumbers).map(Integer::parseInt).collect(Collectors.toList());

        // Parse boards
        List<BingoBoard> boards = new ArrayList<>();
        List<String> boardLines = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                if (!boardLines.isEmpty()) {
                    boards.add(new BingoBoard(boardLines));
                    boardLines.clear();
                }
            } else {
                boardLines.add(line);
            }
        }
        if (!boardLines.isEmpty()) {
            boards.add(new BingoBoard(boardLines));
        }

        // Play bingo
        for (int draw : draws) {
            for (BingoBoard board : boards) {
                board.mark(draw);
                if (board.isWinner()) {
                    int unmarkedSum = board.sumUnmarked();
                    return unmarkedSum * draw;
                }
            }
        }
        return -1; // No winner found
    }

    /**
     * Solves Part 2: Find the score of the last winning bingo board.
     * @param lines The puzzle input lines
     * @return The final score of the last winning board
     */
    public static int solvePartTwo(List<String> lines) {
        String[] drawNumbers = lines.get(0).split(",");
        List<Integer> draws = Stream.of(drawNumbers).map(Integer::parseInt).collect(Collectors.toList());

        List<BingoBoard> boards = new ArrayList<>();
        List<String> boardLines = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                if (!boardLines.isEmpty()) {
                    boards.add(new BingoBoard(boardLines));
                    boardLines.clear();
                }
            } else {
                boardLines.add(line);
            }
        }
        if (!boardLines.isEmpty()) {
            boards.add(new BingoBoard(boardLines));
        }

        List<BingoBoard> remainingBoards = new ArrayList<>(boards);
        int lastScore = -1;
        for (int draw : draws) {
            List<BingoBoard> justWon = new ArrayList<>();
            for (BingoBoard board : remainingBoards) {
                board.mark(draw);
                if (board.isWinner()) {
                    int unmarkedSum = board.sumUnmarked();
                    lastScore = unmarkedSum * draw;
                    justWon.add(board);
                }
            }
            remainingBoards.removeAll(justWon);
            if (remainingBoards.isEmpty()) {
                break;
            }
        }
        return lastScore;
    }

    /**
     * Represents a bingo board and its state.
     */
    static class BingoBoard {
        private final int[][] numbers;
        private final boolean[][] marked;

        /**
         * Constructs a BingoBoard from a list of strings.
         * @param lines The board lines
         */
        public BingoBoard(List<String> lines) {
            numbers = new int[BOARD_SIZE][BOARD_SIZE];
            marked = new boolean[BOARD_SIZE][BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                String[] tokens = lines.get(i).trim().split("\\s+");
                for (int j = 0; j < BOARD_SIZE; j++) {
                    numbers[i][j] = Integer.parseInt(tokens[j]);
                    marked[i][j] = false;
                }
            }
        }

        /**
         * Marks the given number on the board if present.
         * @param number The number to mark
         */
        public void mark(int number) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (numbers[i][j] == number) {
                        marked[i][j] = true;
                    }
                }
            }
        }

        /**
         * Checks if the board is a winner (any row or column fully marked).
         * @return True if winner, false otherwise
         */
        public boolean isWinner() {
            // Check rows
            for (int i = 0; i < BOARD_SIZE; i++) {
                boolean rowWin = true;
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (!marked[i][j]) {
                        rowWin = false;
                        break;
                    }
                }
                if (rowWin) return true;
            }
            // Check columns
            for (int j = 0; j < BOARD_SIZE; j++) {
                boolean colWin = true;
                for (int i = 0; i < BOARD_SIZE; i++) {
                    if (!marked[i][j]) {
                        colWin = false;
                        break;
                    }
                }
                if (colWin) return true;
            }
            return false;
        }

        /**
         * Sums all unmarked numbers on the board.
         * @return The sum of unmarked numbers
         */
        public int sumUnmarked() {
            int sum = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (!marked[i][j]) {
                        sum += numbers[i][j];
                    }
                }
            }
            return sum;
        }
    }
}
