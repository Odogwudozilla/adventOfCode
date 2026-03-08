/**
 * Grove Positioning System (Advent of Code 2022 Day 20)
 *
 * It's finally time to meet back up with the Elves. When you try to contact them, however, you get no reply. Perhaps you're out of range?
 *
 * You know they're headed to the grove where the star fruit grows, so if you can figure out where that is, you should be able to meet back up with them.
 *
 * Fortunately, your handheld device has a file (your puzzle input) that contains the grove's coordinates! Unfortunately, the file is encrypted - just in case the device were to fall into the wrong hands.
 *
 * Maybe you can decrypt it?
 *
 * When you were still back at the camp, you overheard some Elves talking about coordinate file encryption. The main operation involved in decrypting the file is called mixing.
 *
 * The encrypted file is a list of numbers. To mix the file, move each number forward or backward in the file a number of positions equal to the value of the number being moved. The list is circular, so moving a number off one end of the list wraps back around to the other end as if the ends were connected.
 *
 * Official puzzle URL: https://adventofcode.com/2022/day/20
 */
package odogwudozilla.year2022.day20;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GrovePositioningSystemAOC2022Day20 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/2022/day20/day20_puzzle_data.txt"));
        List<Long> numbers = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                numbers.add(Long.parseLong(line.trim()));
            }
        }
        long partOneResult = solvePartOne(numbers);
        System.out.println("Part 1: " + partOneResult);
        long partTwoResult = solvePartTwo(numbers);
        System.out.println("Part 2: " + partTwoResult);
    }

    /**
     * Solves Part 1 of the Grove Positioning System puzzle.
     * @param numbers The encrypted list of numbers from the input file.
     * @return The sum of the three numbers that form the grove coordinates.
     */
    public static long solvePartOne(List<Long> numbers) {
        // Create a list of indices to track original positions
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            indices.add(i);
        }
        int size = numbers.size();
        for (int i = 0; i < size; i++) {
            long value = numbers.get(i);
            int currentIndex = indices.indexOf(i);
            indices.remove(currentIndex);
            int newIndex = (int) ((currentIndex + value) % (size - 1));
            if (newIndex < 0) {
                newIndex += (size - 1);
            }
            indices.add(newIndex, i);
        }
        // Find the index of 0 in the mixed list
        int zeroIndex = -1;
        for (int i = 0; i < size; i++) {
            if (numbers.get(indices.get(i)) == 0) {
                zeroIndex = i;
                break;
            }
        }
        long sum = 0;
        int[] offsets = {1000, 2000, 3000};
        for (int offset : offsets) {
            int idx = (zeroIndex + offset) % size;
            sum += numbers.get(indices.get(idx));
        }
        return sum;
    }

    /**
     * Solves Part 2 of the Grove Positioning System puzzle.
     * @param numbers The encrypted list of numbers from the input file.
     * @return The sum of the three numbers that form the grove coordinates after decryption and 10 rounds of mixing.
     */
    public static long solvePartTwo(List<Long> numbers) {
        final long DECRYPTION_KEY = 811589153L;
        int size = numbers.size();
        List<Long> decrypted = new ArrayList<>(size);
        for (Long n : numbers) {
            decrypted.add(n * DECRYPTION_KEY);
        }
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indices.add(i);
        }
        for (int round = 0; round < 10; round++) {
            for (int i = 0; i < size; i++) {
                long value = decrypted.get(i);
                int currentIndex = indices.indexOf(i);
                indices.remove(currentIndex);
                int newIndex = (int) ((currentIndex + value) % (size - 1));
                if (newIndex < 0) {
                    newIndex += (size - 1);
                }
                indices.add(newIndex, i);
            }
        }
        int zeroIndex = -1;
        for (int i = 0; i < size; i++) {
            if (decrypted.get(indices.get(i)) == 0) {
                zeroIndex = i;
                break;
            }
        }
        long sum = 0;
        int[] offsets = {1000, 2000, 3000};
        for (int offset : offsets) {
            int idx = (zeroIndex + offset) % size;
            sum += decrypted.get(indices.get(idx));
        }
        return sum;
    }
}
