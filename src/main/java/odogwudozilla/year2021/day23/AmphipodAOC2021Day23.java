package odogwudozilla.year2021.day23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Amphipod (Advent of Code 2021 Day 23)
 *
 * Organise amphipods into their destination rooms using the least energy possible.
 *
 * Puzzle link: https://adventofcode.com/2021/day/23
 */
public class AmphipodAOC2021Day23 {
    private static final String INPUT_PATH = "src/main/resources/2021/day23/day23_puzzle_data.txt";
    private static final int ENERGY_AMBER = 1;
    private static final int ENERGY_BRONZE = 10;
    private static final int ENERGY_COPPER = 100;
    private static final int ENERGY_DESERT = 1000;

    public static void main(String[] args) {
        try {
            List<String> inputLines = Files.readAllLines(Paths.get(INPUT_PATH));
            int minEnergyPart1 = solvePartOne(inputLines);
            System.out.println("solvePartOne - Minimum energy required: " + minEnergyPart1);

            int minEnergyPart2 = solvePartTwo(inputLines);
            System.out.println("solvePartTwo - Minimum energy required: " + minEnergyPart2);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    private static class BurrowState {
        char[] hallway;
        char[][] rooms;
        int energy;
        int roomDepth;
        static final int[] ROOM_ENTRANCES = {2, 4, 6, 8};
        static final char[] ROOM_TYPES = {'A', 'B', 'C', 'D'};

        BurrowState(char[] hallway, char[][] rooms, int energy, int roomDepth) {
            this.hallway = hallway.clone();
            this.roomDepth = roomDepth;
            this.rooms = new char[4][roomDepth];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < roomDepth; j++) {
                    this.rooms[i][j] = rooms[i][j];
                }
            }
            this.energy = energy;
        }

        boolean isSolved() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < roomDepth; j++) {
                    if (rooms[i][j] != ROOM_TYPES[i]) {
                        return false;
                    }
                }
            }
            return true;
        }

        String key() {
            StringBuilder sb = new StringBuilder();
            sb.append(hallway);
            for (char[] room : rooms) {
                sb.append(room);
            }
            return sb.toString();
        }
    }

    public static int solvePartOne(List<String> inputLines) {
        char[] hallway = new char[11];
        Arrays.fill(hallway, '.');
        char[][] rooms = new char[4][2];
        for (int i = 0; i < 4; i++) {
            rooms[i][0] = inputLines.get(2).charAt(3 + i * 2);
            rooms[i][1] = inputLines.get(3).charAt(3 + i * 2);
        }
        BurrowState start = new BurrowState(hallway, rooms, 0, 2);

        PriorityQueue<BurrowState> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.energy));
        Map<String, Integer> visited = new HashMap<>();
        queue.add(start);
        visited.put(start.key(), 0);

        while (!queue.isEmpty()) {
            BurrowState state = queue.poll();
            if (state.isSolved()) {
                return state.energy;
            }

            String key = state.key();
            if (visited.getOrDefault(key, Integer.MAX_VALUE) < state.energy) {
                continue;
            }

            for (BurrowState next : generateMoves(state)) {
                String nextKey = next.key();
                int prevEnergy = visited.getOrDefault(nextKey, Integer.MAX_VALUE);
                if (next.energy < prevEnergy) {
                    visited.put(nextKey, next.energy);
                    queue.add(next);
                }
            }
        }
        return -1;
    }

    private static List<BurrowState> generateMoves(BurrowState state) {
        List<BurrowState> nextStates = new ArrayList<>();

        // Move from rooms to hallway
        for (int roomIdx = 0; roomIdx < 4; roomIdx++) {
            int entrance = BurrowState.ROOM_ENTRANCES[roomIdx];
            int depth = -1;
            for (int d = 0; d < state.roomDepth; d++) {
                if (state.rooms[roomIdx][d] != '.') {
                    depth = d;
                    break;
                }
            }
            if (depth == -1) continue;

            char amphipod = state.rooms[roomIdx][depth];
            boolean shouldStay = BurrowState.ROOM_TYPES[roomIdx] == amphipod;
            if (shouldStay) {
                boolean allBelowCorrect = true;
                for (int d = depth + 1; d < state.roomDepth; d++) {
                    if (state.rooms[roomIdx][d] != amphipod) {
                        allBelowCorrect = false;
                        break;
                    }
                }
                if (allBelowCorrect) continue;
            }

            for (int pos = 0; pos < 11; pos++) {
                if (state.hallway[pos] != '.') continue;
                if (pos == 2 || pos == 4 || pos == 6 || pos == 8) continue;

                int start = Math.min(pos, entrance);
                int end = Math.max(pos, entrance);
                boolean pathClear = true;
                for (int i = start; i <= end; i++) {
                    if (i == entrance) continue;
                    if (state.hallway[i] != '.') {
                        pathClear = false;
                        break;
                    }
                }
                if (!pathClear) continue;

                char[] newHallway = state.hallway.clone();
                char[][] newRooms = new char[4][state.roomDepth];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < state.roomDepth; j++) {
                        newRooms[i][j] = state.rooms[i][j];
                    }
                }
                newHallway[pos] = amphipod;
                newRooms[roomIdx][depth] = '.';
                int steps = Math.abs(pos - entrance) + depth + 1;
                int energy = state.energy + steps * getEnergy(amphipod);
                nextStates.add(new BurrowState(newHallway, newRooms, energy, state.roomDepth));
            }
        }

        // Move from hallway to rooms
        for (int pos = 0; pos < 11; pos++) {
            char amphipod = state.hallway[pos];
            if (amphipod == '.') continue;

            int destRoom = -1;
            for (int i = 0; i < 4; i++) {
                if (BurrowState.ROOM_TYPES[i] == amphipod) {
                    destRoom = i;
                    break;
                }
            }
            int entrance = BurrowState.ROOM_ENTRANCES[destRoom];

            int start = Math.min(pos, entrance);
            int end = Math.max(pos, entrance);
            boolean pathClear = true;
            for (int i = start; i <= end; i++) {
                if (i == pos) continue;
                if (state.hallway[i] != '.') {
                    pathClear = false;
                    break;
                }
            }
            if (!pathClear) continue;

            boolean canEnter = true;
            int depth = -1;
            for (int d = state.roomDepth - 1; d >= 0; d--) {
                if (state.rooms[destRoom][d] == '.') {
                    depth = d;
                } else if (state.rooms[destRoom][d] != amphipod) {
                    canEnter = false;
                    break;
                }
            }
            if (!canEnter || depth == -1) continue;

            char[] newHallway = state.hallway.clone();
            char[][] newRooms = new char[4][state.roomDepth];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < state.roomDepth; j++) {
                    newRooms[i][j] = state.rooms[i][j];
                }
            }
            newHallway[pos] = '.';
            newRooms[destRoom][depth] = amphipod;
            int steps = Math.abs(pos - entrance) + depth + 1;
            int energy = state.energy + steps * getEnergy(amphipod);
            nextStates.add(new BurrowState(newHallway, newRooms, energy, state.roomDepth));
        }

        return nextStates;
    }

    public static int solvePartTwo(List<String> inputLines) {
        char[] hallway = new char[11];
        Arrays.fill(hallway, '.');
        char[][] rooms = new char[4][4];
        rooms[0][0] = inputLines.get(2).charAt(3);
        rooms[1][0] = inputLines.get(2).charAt(5);
        rooms[2][0] = inputLines.get(2).charAt(7);
        rooms[3][0] = inputLines.get(2).charAt(9);

        rooms[0][1] = 'D';
        rooms[1][1] = 'C';
        rooms[2][1] = 'B';
        rooms[3][1] = 'A';

        rooms[0][2] = 'D';
        rooms[1][2] = 'B';
        rooms[2][2] = 'A';
        rooms[3][2] = 'C';

        rooms[0][3] = inputLines.get(3).charAt(3);
        rooms[1][3] = inputLines.get(3).charAt(5);
        rooms[2][3] = inputLines.get(3).charAt(7);
        rooms[3][3] = inputLines.get(3).charAt(9);

        BurrowState start = new BurrowState(hallway, rooms, 0, 4);

        PriorityQueue<BurrowState> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.energy));
        Map<String, Integer> visited = new HashMap<>();
        queue.add(start);
        visited.put(start.key(), 0);

        while (!queue.isEmpty()) {
            BurrowState state = queue.poll();
            if (state.isSolved()) {
                return state.energy;
            }

            String key = state.key();
            if (visited.getOrDefault(key, Integer.MAX_VALUE) < state.energy) {
                continue;
            }

            for (BurrowState next : generateMoves(state)) {
                String nextKey = next.key();
                int prevEnergy = visited.getOrDefault(nextKey, Integer.MAX_VALUE);
                if (next.energy < prevEnergy) {
                    visited.put(nextKey, next.energy);
                    queue.add(next);
                }
            }
        }
        return -1;
    }

    private static int getEnergy(char amphipod) {
        switch (amphipod) {
            case 'A': return ENERGY_AMBER;
            case 'B': return ENERGY_BRONZE;
            case 'C': return ENERGY_COPPER;
            case 'D': return ENERGY_DESERT;
            default: return 0;
        }
    }
}

