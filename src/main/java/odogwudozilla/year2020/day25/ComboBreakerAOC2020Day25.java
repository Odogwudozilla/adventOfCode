package odogwudozilla.year2020.day25;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code 2020 - Day 25: Combo Breaker
 * <p>
 * This puzzle involves reverse-engineering a cryptographic handshake between a card and door.
 * We need to find the loop sizes for both devices and calculate the encryption key they establish.
 * The transformation process involves multiplying by a subject number and taking modulo 20201227.
 * <p>
 * Puzzle URL: https://adventofcode.com/2020/day/25
 */
public class ComboBreakerAOC2020Day25 {

    private static final long MODULUS = 20201227L;
    private static final long INITIAL_SUBJECT_NUMBER = 7L;

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/2020/day25/day25_puzzle_data.txt"));

            long cardPublicKey = Long.parseLong(lines.get(0).trim());
            long doorPublicKey = Long.parseLong(lines.get(1).trim());

            System.out.println("Card Public Key: " + cardPublicKey);
            System.out.println("Door Public Key: " + doorPublicKey);

            // Part 1: Find the encryption key
            long encryptionKey = solvePart1(cardPublicKey, doorPublicKey);
            System.out.println("\nPart 1 - Encryption Key: " + encryptionKey);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 by finding the loop size for one device and using it to calculate the encryption key.
     * @param cardPublicKey the card's public key
     * @param doorPublicKey the door's public key
     * @return the encryption key
     */
    private static long solvePart1(long cardPublicKey, long doorPublicKey) {
        // Find the card's loop size by brute force
        int cardLoopSize = findLoopSize(cardPublicKey);
        System.out.println("Card Loop Size: " + cardLoopSize);

        // Calculate the encryption key using the card's loop size and door's public key
        long encryptionKey = transform(doorPublicKey, cardLoopSize);

        return encryptionKey;
    }

    /**
     * Finds the loop size that produces the given public key when transforming the initial subject number.
     * @param publicKey the public key to match
     * @return the loop size
     */
    private static int findLoopSize(long publicKey) {
        long value = 1L;
        int loopSize = 0;

        while (value != publicKey) {
            value = (value * INITIAL_SUBJECT_NUMBER) % MODULUS;
            loopSize++;
        }

        return loopSize;
    }

    /**
     * Transforms a subject number by applying the cryptographic operation a specified number of times.
     * @param subjectNumber the subject number to transform
     * @param loopSize the number of times to apply the transformation
     * @return the transformed value
     */
    private static long transform(long subjectNumber, int loopSize) {
        long value = 1L;

        for (int i = 0; i < loopSize; i++) {
            value = (value * subjectNumber) % MODULUS;
        }

        return value;
    }
}

