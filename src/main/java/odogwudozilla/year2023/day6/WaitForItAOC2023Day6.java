package odogwudozilla.year2023.day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code 2023 - Day 6: Wait For It
 *
 * This puzzle involves calculating the number of ways to beat boat race records.
 * The boat's speed increases by 1 mm/ms for each millisecond the button is held.
 * The distance travelled is: holdTime * (totalTime - holdTime).
 * We need to find all hold times where distance > record distance.
 *
 * Puzzle URL: https://adventofcode.com/2023/day/6
 */
public class WaitForItAOC2023Day6 {

    private static final String RESOURCE_PATH = "src/main/resources/2023/day6/day6_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = readInputFile();

            // Part 1
            long partOneResult = solvePartOne(lines);
            System.out.println("Part 1 - Product of ways to win: " + partOneResult);

            // Part 2
            long partTwoResult = solvePartTwo(lines);
            System.out.println("Part 2 - Ways to win single race: " + partTwoResult);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 by parsing multiple races and calculating the product of ways to win each race.
     * @param lines the input lines containing time and distance data
     * @return the product of the number of ways to win each race
     */
    private static long solvePartOne(List<String> lines) {
        List<Race> races = parseRaces(lines);

        long product = 1;
        for (Race race : races) {
            long waysToWin = calculateWaysToWin(race.time, race.distance);
            product *= waysToWin;
        }

        return product;
    }

    /**
     * Solves Part 2 by treating all numbers as a single large race (ignoring spaces).
     * @param lines the input lines containing time and distance data
     * @return the number of ways to win the single large race
     */
    private static long solvePartTwo(List<String> lines) {
        Race singleRace = parseSingleRace(lines);
        return calculateWaysToWin(singleRace.time, singleRace.distance);
    }

    /**
     * Parses the input lines into multiple Race objects.
     * @param lines the input lines
     * @return list of Race objects
     */
    private static List<Race> parseRaces(List<String> lines) {
        String timeLine = lines.get(0).substring(lines.get(0).indexOf(':') + 1).trim();
        String distanceLine = lines.get(1).substring(lines.get(1).indexOf(':') + 1).trim();

        String[] times = timeLine.split("\\s+");
        String[] distances = distanceLine.split("\\s+");

        List<Race> races = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            long time = Long.parseLong(times[i]);
            long distance = Long.parseLong(distances[i]);
            races.add(new Race(time, distance));
        }

        return races;
    }

    /**
     * Parses the input as a single race by concatenating all numbers (ignoring spaces).
     * @param lines the input lines
     * @return a single Race object
     */
    private static Race parseSingleRace(List<String> lines) {
        String timeLine = lines.get(0).substring(lines.get(0).indexOf(':') + 1).replaceAll("\\s+", "");
        String distanceLine = lines.get(1).substring(lines.get(1).indexOf(':') + 1).replaceAll("\\s+", "");

        long time = Long.parseLong(timeLine);
        long distance = Long.parseLong(distanceLine);

        return new Race(time, distance);
    }

    /**
     * Calculates the number of ways to beat the record distance for a given race time.
     * The distance formula is: holdTime * (totalTime - holdTime).
     * We need to find all holdTime values where distance > recordDistance.
     * @param totalTime the total race time
     * @param recordDistance the record distance to beat
     * @return the number of ways to win
     */
    private static long calculateWaysToWin(long totalTime, long recordDistance) {
        long waysToWin = 0;

        // For each possible hold time, calculate if we can beat the record
        for (long holdTime = 1; holdTime < totalTime; holdTime++) {
            long travelTime = totalTime - holdTime;
            long distance = holdTime * travelTime;

            if (distance > recordDistance) {
                waysToWin++;
            }
        }

        return waysToWin;
    }

    /**
     * Reads the input file and returns its lines.
     * @return list of lines from the input file
     * @throws IOException if file cannot be read
     */
    private static List<String> readInputFile() throws IOException {
        Path path = Paths.get(RESOURCE_PATH);
        return Files.readAllLines(path);
    }

    /**
     * Represents a boat race with a total time and a record distance.
     */
    private static class Race {
        final long time;
        final long distance;

        Race(long time, long distance) {
            this.time = time;
            this.distance = distance;
        }
    }
}
