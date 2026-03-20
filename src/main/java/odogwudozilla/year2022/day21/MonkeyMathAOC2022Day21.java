package odogwudozilla.year2022.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Advent of Code 2022 Day 21: Monkey Math
 * https://adventofcode.com/2022/day/21
 *
 * The monkeys are back! Each monkey is given a job: either to yell a specific number or to yell the result of a math operation. Your job is to work out the number the monkey named root will yell before the monkeys figure it out themselves.
 *
 * Example jobs:
 *   root: pppw + sjmn
 *   dbpl: 5
 *   sjmn: drzm * dbpl
 *
 * See the puzzle description for full details.
 */
public class MonkeyMathAOC2022Day21 {
    private static final String INPUT_PATH = "src/main/resources/2022/day21/day21_puzzle_data.txt";

    public static void main(String[] args) throws IOException {
        Map<String, String> jobs = new HashMap<>();
        for (String line : Files.readAllLines(Path.of(INPUT_PATH))) {
            if (line.isBlank()) continue;
            String[] parts = line.split(": ", 2);
            jobs.put(parts[0], parts[1]);
        }
        long partOneResult = solvePartOne(jobs);
        System.out.println("Part 1: " + partOneResult);
        long partTwoResult = solvePartTwo(new HashMap<>(jobs));
        System.out.println("Part 2: " + partTwoResult);
    }

    /**
     * Solves Part 1: What number will the monkey named root yell?
     * @param jobs the map of monkey names to their jobs
     * @return the number root yells
     */
    public static long solvePartOne(Map<String, String> jobs) {
        Map<String, Long> memo = new HashMap<>();
        return yell("root", jobs, memo);
    }

    public static long solvePartTwo(Map<String, String> jobs) {
        String rootJob = jobs.get("root");
        String[] rootTokens = rootJob.split(" ");
        String leftMonkey = rootTokens[0];
        String rightMonkey = rootTokens[2];
        jobs.remove("humn");
        // Evaluate left and right at two points to determine monotonicity
        long test1 = 0L;
        long test2 = 1L;
        Map<String, Long> memo1 = new HashMap<>();
        memo1.put("humn", test1);
        long left1 = yell(leftMonkey, jobs, memo1);
        long right1 = yell(rightMonkey, jobs, memo1);
        Map<String, Long> memo2 = new HashMap<>();
        memo2.put("humn", test2);
        long left2 = yell(leftMonkey, jobs, memo2);
        long right2 = yell(rightMonkey, jobs, memo2);
        boolean increasing = (left2 - right2) > (left1 - right1);
        long low = 0;
        long high = 1_000_000_000_000_000L;
        long answer = -1;
        while (low <= high) {
            long mid = low + (high - low) / 2;
            Map<String, Long> memo = new HashMap<>();
            memo.put("humn", mid);
            long left = yell(leftMonkey, jobs, memo);
            long right = yell(rightMonkey, jobs, memo);
            if (left == right) {
                answer = mid;
                high = mid - 1;
            } else if ((left - right > 0) == increasing) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        if (answer != -1) return answer;
        throw new IllegalStateException("No solution found for humn");
    }

    private static long yell(String monkey, Map<String, String> jobs, Map<String, Long> memo) {
        if (memo.containsKey(monkey)) return memo.get(monkey);
        String job = jobs.get(monkey);
        String[] tokens = job.split(" ");
        long result;
        if (tokens.length == 1) {
            result = Long.parseLong(tokens[0]);
        } else {
            long left = yell(tokens[0], jobs, memo);
            long right = yell(tokens[2], jobs, memo);
            switch (tokens[1]) {
                case "+": result = left + right; break;
                case "-": result = left - right; break;
                case "*": result = left * right; break;
                case "/": result = left / right; break;
                default: throw new IllegalArgumentException("Unknown operator: " + tokens[1]);
            }
        }
        memo.put(monkey, result);
        return result;
    }
}

