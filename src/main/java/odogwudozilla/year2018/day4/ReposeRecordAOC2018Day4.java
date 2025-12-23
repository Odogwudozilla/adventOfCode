package odogwudozilla.year2018.day4;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

/**
 * Repose Record (Advent of Code 2018 Day 4)
 *
 * Analyse guard sleep records to find the guard most frequently asleep and the minute they are most often asleep (Part 1), and the guard most frequently asleep on the same minute (Part 2).
 * Official puzzle link: https://adventofcode.com/2018/day/4
 */
public class ReposeRecordAOC2018Day4 {
    private static final String INPUT_PATH = "src/main/resources/2018/day4/day4_puzzle_data.txt";
    private static final Logger LOGGER = Logger.getLogger(ReposeRecordAOC2018Day4.class.getName());

    /**
     * Entry point for solving Advent of Code 2018 Day 4.
     * Calls partOne and partTwo methods and prints/logs their results.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        LOGGER.info("main - Starting Repose Record analysis");
        try {
            List<String> records = Files.readAllLines(Paths.get(INPUT_PATH));
            int resultPart1 = partOne(records);
            LOGGER.info("main - Part 1 result (guard ID * minute): " + resultPart1);
            System.out.println("Part 1 result (guard ID * minute): " + resultPart1);

            int resultPart2 = partTwo(records);
            LOGGER.info("main - Part 2 result (guard ID * minute): " + resultPart2);
            System.out.println("Part 2 result (guard ID * minute): " + resultPart2);
        } catch (Exception e) {
            LOGGER.severe("main - Error reading input or analysing records: " + e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                LOGGER.severe("main - " + ste.toString());
            }
        }
    }

    /**
     * Solves Part 1: Find the guard with the most minutes asleep and the minute they are most frequently asleep.
     *
     * @param records List of sleep records
     * @return Product of guard ID and minute
     */
    private static int partOne(List<String> records) {
        Map<Integer, int[]> sleepMap = buildSleepMap(records);
        int maxSleepGuard = -1;
        int maxSleep = 0;
        for (Map.Entry<Integer, int[]> entry : sleepMap.entrySet()) {
            int totalSleep = Arrays.stream(entry.getValue()).sum();
            if (totalSleep > maxSleep) {
                maxSleep = totalSleep;
                maxSleepGuard = entry.getKey();
            }
        }
        int[] minutes = sleepMap.get(maxSleepGuard);
        int maxMinute = 0;
        for (int i = 1; i < minutes.length; i++) {
            if (minutes[i] > minutes[maxMinute]) {
                maxMinute = i;
            }
        }
        return maxSleepGuard * maxMinute;
    }

    /**
     * Solves Part 2: Find the guard most frequently asleep on the same minute.
     *
     * @param records List of sleep records
     * @return Product of guard ID and minute
     */
    private static int partTwo(List<String> records) {
        Map<Integer, int[]> sleepMap = buildSleepMap(records);
        int maxGuard = -1;
        int maxMinute = -1;
        int maxCount = 0;
        for (Map.Entry<Integer, int[]> entry : sleepMap.entrySet()) {
            int[] minutes = entry.getValue();
            for (int i = 0; i < minutes.length; i++) {
                if (minutes[i] > maxCount) {
                    maxCount = minutes[i];
                    maxGuard = entry.getKey();
                    maxMinute = i;
                }
            }
        }
        return maxGuard * maxMinute;
    }

    /**
     * Builds a map of guard IDs to an array of minutes asleep.
     *
     * @param records List of sleep records
     * @return Map of guard ID to minutes asleep array
     */
    private static Map<Integer, int[]> buildSleepMap(List<String> records) {
        List<String> sortedRecords = new ArrayList<>(records);
        Collections.sort(sortedRecords);
        Map<Integer, int[]> sleepMap = new HashMap<>();
        int guardId = -1;
        int sleepStart = -1;
        for (String record : sortedRecords) {
            if (record.contains("Guard #")) {
                int idx = record.indexOf('#') + 1;
                int endIdx = record.indexOf(' ', idx);
                guardId = Integer.parseInt(record.substring(idx, endIdx));
                sleepMap.putIfAbsent(guardId, new int[60]);
            } else if (record.contains("falls asleep")) {
                sleepStart = Integer.parseInt(record.substring(15, 17));
            } else if (record.contains("wakes up")) {
                int sleepEnd = Integer.parseInt(record.substring(15, 17));
                int[] minutes = sleepMap.get(guardId);
                for (int i = sleepStart; i < sleepEnd; i++) {
                    minutes[i]++;
                }
            }
        }
        return sleepMap;
    }
}
