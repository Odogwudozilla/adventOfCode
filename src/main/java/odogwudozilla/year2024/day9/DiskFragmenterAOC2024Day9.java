package odogwudozilla.year2024.day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code 2024 - Day 9: Disk Fragmenter
 * <p>
 * The amphipod is trying to compact files on disk by moving file blocks one at a time
 * from the end of the disk to the leftmost free space block. The disk map uses a dense
 * format where digits alternate between file length and free space length.
 * <p>
 * Official puzzle: https://adventofcode.com/2024/day/9
 */
public class DiskFragmenterAOC2024Day9 {

    private static final String INPUT_FILE = "src/main/resources/2024/day9/day9_puzzle_data.txt";
    private static final int FREE_SPACE = -1;

    public static void main(String[] args) {
        try {
            String diskMap = readInput();

            long part1Result = solvePartOne(diskMap);
            System.out.println("Part 1 - Filesystem checksum after compaction: " + part1Result);

            long part2Result = solvePartTwo(diskMap);
            System.out.println("Part 2 - Filesystem checksum after whole file compaction: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solves Part 1: Compact the disk by moving individual file blocks from the end
     * to the leftmost free space, then calculate the filesystem checksum.
     * @param diskMap the dense disk map string
     * @return the filesystem checksum
     */
    private static long solvePartOne(String diskMap) {
        List<Integer> blocks = expandDiskMap(diskMap);
        compactDisk(blocks);
        return calculateChecksum(blocks);
    }

    /**
     * Solves Part 2: Compact the disk by moving whole files to the leftmost
     * contiguous free space that can fit them, in order of decreasing file ID.
     * @param diskMap the dense disk map string
     * @return the filesystem checksum
     */
    private static long solvePartTwo(String diskMap) {
        List<Integer> blocks = expandDiskMap(diskMap);
        compactWholeFiles(blocks);
        return calculateChecksum(blocks);
    }

    /**
     * Expands the dense disk map format into individual blocks.
     * Alternates between file blocks (with ID) and free space blocks.
     * @param diskMap the dense disk map
     * @return list of blocks where each element is a file ID or FREE_SPACE
     */
    private static List<Integer> expandDiskMap(String diskMap) {
        List<Integer> blocks = new ArrayList<>();
        int fileId = 0;
        boolean isFile = true;

        for (char c : diskMap.toCharArray()) {
            int length = c - '0';

            if (isFile) {
                // Add file blocks with current file ID
                for (int i = 0; i < length; i++) {
                    blocks.add(fileId);
                }
                fileId++;
            } else {
                // Add free space blocks
                for (int i = 0; i < length; i++) {
                    blocks.add(FREE_SPACE);
                }
            }

            isFile = !isFile;
        }

        return blocks;
    }

    /**
     * Compacts the disk by moving file blocks from the end to the leftmost free space.
     * Continues until there are no gaps between file blocks.
     * @param blocks the list of disk blocks to compact in-place
     */
    private static void compactDisk(List<Integer> blocks) {
        int leftPointer = 0;
        int rightPointer = blocks.size() - 1;

        while (leftPointer < rightPointer) {
            // Find the next free space from the left
            while (leftPointer < rightPointer && blocks.get(leftPointer) != FREE_SPACE) {
                leftPointer++;
            }

            // Find the next file block from the right
            while (leftPointer < rightPointer && blocks.get(rightPointer) == FREE_SPACE) {
                rightPointer--;
            }

            // Move the file block to the free space
            if (leftPointer < rightPointer) {
                blocks.set(leftPointer, blocks.get(rightPointer));
                blocks.set(rightPointer, FREE_SPACE);
            }
        }
    }

    /**
     * Compacts the disk by moving whole files to the leftmost contiguous free space
     * that can fit them. Files are moved in order of decreasing file ID.
     * @param blocks the list of disk blocks to compact in-place
     */
    private static void compactWholeFiles(List<Integer> blocks) {
        // Find the maximum file ID
        int maxFileId = 0;
        for (int block : blocks) {
            if (block != FREE_SPACE && block > maxFileId) {
                maxFileId = block;
            }
        }

        // Process files in decreasing order of file ID
        for (int fileId = maxFileId; fileId >= 0; fileId--) {
            moveWholeFile(blocks, fileId);
        }
    }

    /**
     * Attempts to move a whole file to the leftmost contiguous free space that can fit it.
     * @param blocks the list of disk blocks
     * @param fileId the ID of the file to move
     */
    private static void moveWholeFile(List<Integer> blocks, int fileId) {
        // Find the file's current position and size
        int fileStart = -1;
        int fileSize = 0;

        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == fileId) {
                if (fileStart == -1) {
                    fileStart = i;
                }
                fileSize++;
            }
        }

        if (fileStart == -1 || fileSize == 0) {
            return; // File not found
        }

        // Find the leftmost contiguous free space that can fit the file
        int targetStart = findLeftmostFreeSpace(blocks, fileSize, fileStart);

        if (targetStart != -1) {
            // Move the file to the target position
            for (int i = 0; i < fileSize; i++) {
                blocks.set(targetStart + i, fileId);
            }

            // Clear the old position
            for (int i = fileStart; i < fileStart + fileSize; i++) {
                blocks.set(i, FREE_SPACE);
            }
        }
    }

    /**
     * Finds the leftmost contiguous free space that can fit the specified size.
     * Only searches to the left of the current file position.
     * @param blocks the list of disk blocks
     * @param requiredSize the size of space needed
     * @param beforePosition only search before this position
     * @return the starting position of the free space, or -1 if not found
     */
    private static int findLeftmostFreeSpace(List<Integer> blocks, int requiredSize, int beforePosition) {
        int consecutiveFree = 0;
        int startPos = -1;

        for (int i = 0; i < beforePosition; i++) {
            if (blocks.get(i) == FREE_SPACE) {
                if (consecutiveFree == 0) {
                    startPos = i;
                }
                consecutiveFree++;

                if (consecutiveFree >= requiredSize) {
                    return startPos;
                }
            } else {
                consecutiveFree = 0;
                startPos = -1;
            }
        }

        return -1; // No suitable space found
    }

    /**
     * Calculates the filesystem checksum by multiplying each block's position
     * with its file ID number and summing the results.
     * @param blocks the compacted disk blocks
     * @return the filesystem checksum
     */
    private static long calculateChecksum(List<Integer> blocks) {
        long checksum = 0;

        for (int position = 0; position < blocks.size(); position++) {
            int fileId = blocks.get(position);
            if (fileId != FREE_SPACE) {
                checksum += (long) position * fileId;
            }
        }

        return checksum;
    }

    /**
     * Reads the puzzle input from the file.
     * @return the disk map as a single line string
     * @throws IOException if the file cannot be read
     */
    private static String readInput() throws IOException {
        return Files.readString(Paths.get(INPUT_FILE)).trim();
    }
}




