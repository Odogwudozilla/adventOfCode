package odogwudozilla.year2016.day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Security Through Obscurity (Advent of Code 2016, Day 4)
 * <p>
 * Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted and full of decoy data, but the instructions to decode the list are barely hidden nearby. Better remove the decoy data first.
 * <p>
 * Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID, and a checksum in square brackets.
 * <p>
 * A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order, with ties broken by alphabetisation. For example:
 * aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
 * a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each), the first five are listed alphabetically.
 * not-a-real-room-404[oarel] is a real room.
 * totally-real-room-200[decoy] is not.
 * Of the real rooms from the list above, the sum of their sector IDs is 1514.
 * <p>
 * Official puzzle link: https://adventofcode.com/2016/day/4
 */
public class SecurityThroughObscurityAOC2016Day4 {
    private static final String INPUT_PATH = "src/main/resources/2016/day4/day4_puzzle_data.txt";

    /**
     * Main entry point for the solution.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            int partOneResult = solvePartOne(lines);
            System.out.println("Part 1: Sum of sector IDs of real rooms = " + partOneResult);
            int partTwoResult = solvePartTwo(lines);
            System.out.println("Part 2: Sector ID of North Pole objects room = " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: Sums the sector IDs of all real rooms.
     * @param lines The input lines from the puzzle data file
     * @return The sum of sector IDs for real rooms
     */
    public static int solvePartOne(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            Room room = Room.parse(line);
            if (room != null && room.isReal()) {
                sum += room.sectorId;
            }
        }
        return sum;
    }

    /**
     * Solves Part 2: Finds the sector ID of the room where North Pole objects are stored.
     * @param lines The input lines from the puzzle data file
     * @return The sector ID of the North Pole objects room
     */
    public static int solvePartTwo(List<String> lines) {
        final String TARGET = "northpole object storage";
        for (String line : lines) {
            Room room = Room.parse(line);
            if (room != null && room.isReal()) {
                String decrypted = room.decryptName();
                if (TARGET.equals(decrypted)) {
                    return room.sectorId;
                }
            }
        }
        return -1; // Not found
    }

    /**
     * Represents a room entry from the input.
     */
    private static class Room {
        final String encryptedName;
        final int sectorId;
        final String checksum;

        private Room(String encryptedName, int sectorId, String checksum) {
            this.encryptedName = encryptedName;
            this.sectorId = sectorId;
            this.checksum = checksum;
        }

        /**
         * Parses a line into a Room object.
         * @param line The input line
         * @return Room object or null if parsing fails
         */
        static Room parse(String line) {
            // Example: aaaaa-bbb-z-y-x-123[abxyz]
            int lastDash = line.lastIndexOf('-');
            int bracketOpen = line.indexOf('[', lastDash);
            int bracketClose = line.indexOf(']', bracketOpen);
            if (lastDash < 0 || bracketOpen < 0 || bracketClose < 0) return null;
            String name = line.substring(0, lastDash);
            int sectorId;
            try {
                sectorId = Integer.parseInt(line.substring(lastDash + 1, bracketOpen));
            } catch (NumberFormatException e) {
                return null;
            }
            String checksum = line.substring(bracketOpen + 1, bracketClose);
            return new Room(name, sectorId, checksum);
        }

        /**
         * Determines if the room is real by validating the checksum.
         * @return true if the room is real, false otherwise
         */
        boolean isReal() {
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : encryptedName.toCharArray()) {
                if (c != '-') {
                    freq.put(c, freq.getOrDefault(c, 0) + 1);
                }
            }
            List<Character> chars = new ArrayList<>(freq.keySet());
            chars.sort((a, b) -> {
                int cmp = Integer.compare(freq.get(b), freq.get(a));
                if (cmp != 0) return cmp;
                return Character.compare(a, b);
            });
            StringBuilder expected = new StringBuilder();
            for (int i = 0; i < 5 && i < chars.size(); i++) {
                expected.append(chars.get(i));
            }
            return expected.toString().equals(checksum);
        }

        /**
         * Decrypts the room name using a Caesar cipher with the sector ID as the shift.
         * Dashes become spaces.
         * @return The decrypted room name
         */
        String decryptName() {
            StringBuilder sb = new StringBuilder();
            for (char c : encryptedName.toCharArray()) {
                if (c == '-') {
                    sb.append(' ');
                } else if (c >= 'a' && c <= 'z') {
                    int shifted = ((c - 'a' + sectorId) % 26) + 'a';
                    sb.append((char) shifted);
                }
            }
            return sb.toString();
        }
    }
}
