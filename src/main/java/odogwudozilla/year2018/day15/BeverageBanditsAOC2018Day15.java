package odogwudozilla.year2018.day15;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Advent of Code 2018 - Day 15: Beverage Bandits
 *
 * This puzzle simulates combat between Elves and Goblins on a grid map.
 * Units take turns in reading order, moving towards enemies and attacking adjacent targets.
 * Combat ends when one side is eliminated.
 *
 * Puzzle: https://adventofcode.com/2018/day/15
 */
public class BeverageBanditsAOC2018Day15 {

    private static final int DEFAULT_ATTACK_POWER = 3;
    private static final int DEFAULT_HIT_POINTS = 200;
    private static final char WALL = '#';
    private static final char OPEN = '.';
    private static final char ELF = 'E';
    private static final char GOBLIN = 'G';

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(
                    Paths.get("src/main/resources/2018/day15/day15_puzzle_data.txt")
            );

            // Part 1: Standard combat with attack power 3
            long partOneResult = solvePartOne(lines);
            System.out.println("Part 1 - Combat outcome: " + partOneResult);

            // Part 2: Find minimum elf attack power for no elf deaths
            long partTwoResult = solvePartTwo(lines);
            System.out.println("Part 2 - Combat outcome with boosted elves: " + partTwoResult);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solve Part 1: Simulate combat with default attack power (3) for all units.
     * @param lines the map lines
     * @return the outcome (full rounds * remaining HP sum)
     */
    private static long solvePartOne(@NotNull List<String> lines) {
        GameState game = new GameState(lines, DEFAULT_ATTACK_POWER, DEFAULT_ATTACK_POWER);
        return game.simulateCombat();
    }

    /**
     * Solve Part 2: Find minimum elf attack power where no elves die.
     * @param lines the map lines
     * @return the outcome with the minimum elf attack power needed
     */
    private static long solvePartTwo(@NotNull List<String> lines) {
        int elfAttackPower = DEFAULT_ATTACK_POWER + 1;

        while (true) {
            GameState game = new GameState(lines, elfAttackPower, DEFAULT_ATTACK_POWER);
            int initialElfCount = game.countElves();
            long outcome = game.simulateCombat();
            int finalElfCount = game.countElves();

            if (finalElfCount == initialElfCount) {
                return outcome;
            }
            elfAttackPower++;
        }
    }

    private static class GameState {
        private final char[][] map;
        private final List<Unit> units;
        private final int elfAttackPower;
        private final int goblinAttackPower;

        public GameState(@NotNull List<String> lines, int elfAttackPower, int goblinAttackPower) {
            this.elfAttackPower = elfAttackPower;
            this.goblinAttackPower = goblinAttackPower;

            int height = lines.size();
            int width = lines.get(0).length();
            this.map = new char[height][width];
            this.units = new ArrayList<>();

            for (int y = 0; y < height; y++) {
                String line = lines.get(y);
                for (int x = 0; x < width; x++) {
                    char c = line.charAt(x);
                    map[y][x] = c;
                    if (c == ELF || c == GOBLIN) {
                        int attackPower = c == ELF ? elfAttackPower : goblinAttackPower;
                        units.add(new Unit(c, x, y, attackPower));
                        map[y][x] = OPEN;
                    }
                }
            }
        }

        public long simulateCombat() {
            int fullRounds = 0;

            while (true) {
                units.sort(Comparator.comparingInt((Unit u) -> u.y).thenComparingInt(u -> u.x));

                for (int i = 0; i < units.size(); i++) {
                    Unit unit = units.get(i);
                    if (!unit.isAlive()) {
                        continue;
                    }

                    List<Unit> targets = getTargets(unit);
                    if (targets.isEmpty()) {
                        return fullRounds * getSumOfHitPoints();
                    }

                    if (!isInRange(unit, targets)) {
                        move(unit, targets);
                    }

                    if (isInRange(unit, targets)) {
                        attack(unit, targets);
                    }
                }

                units.removeIf(u -> !u.isAlive());
                fullRounds++;
            }
        }

        private void move(@NotNull Unit unit, @NotNull List<Unit> targets) {
            List<Position> inRangePositions = new ArrayList<>();
            for (Unit target : targets) {
                for (Position adj : getAdjacentPositions(target.x, target.y)) {
                    if (isOpen(adj.x, adj.y)) {
                        inRangePositions.add(adj);
                    }
                }
            }

            if (inRangePositions.isEmpty()) {
                return;
            }

            Map<Position, Integer> distances = new HashMap<>();
            Queue<Position> queue = new LinkedList<>();
            Position start = new Position(unit.x, unit.y);
            queue.add(start);
            distances.put(start, 0);

            while (!queue.isEmpty()) {
                Position current = queue.poll();
                int dist = distances.get(current);

                for (Position adj : getAdjacentPositions(current.x, current.y)) {
                    if (isOpen(adj.x, adj.y) && !distances.containsKey(adj)) {
                        distances.put(adj, dist + 1);
                        queue.add(adj);
                    }
                }
            }

            Position bestTarget = null;
            int minDist = Integer.MAX_VALUE;

            for (Position pos : inRangePositions) {
                Integer dist = distances.get(pos);
                if (dist != null && dist < minDist) {
                    minDist = dist;
                    bestTarget = pos;
                } else if (dist != null && dist == minDist && isEarlierInReadingOrder(pos, bestTarget)) {
                    bestTarget = pos;
                }
            }

            if (bestTarget == null) {
                return;
            }

            Position nextStep = findNextStep(unit.x, unit.y, bestTarget.x, bestTarget.y);
            if (nextStep != null) {
                unit.x = nextStep.x;
                unit.y = nextStep.y;
            }
        }

        private Position findNextStep(int fromX, int fromY, int toX, int toY) {
            Map<Position, Integer> distances = new HashMap<>();
            Queue<Position> queue = new LinkedList<>();
            Position target = new Position(toX, toY);
            queue.add(target);
            distances.put(target, 0);

            while (!queue.isEmpty()) {
                Position current = queue.poll();
                int dist = distances.get(current);

                for (Position adj : getAdjacentPositions(current.x, current.y)) {
                    if ((isOpen(adj.x, adj.y) || (adj.x == fromX && adj.y == fromY)) && !distances.containsKey(adj)) {
                        distances.put(adj, dist + 1);
                        queue.add(adj);
                    }
                }
            }

            Position bestStep = null;
            int minDist = Integer.MAX_VALUE;

            for (Position adj : getAdjacentPositions(fromX, fromY)) {
                Integer dist = distances.get(adj);
                if (dist != null && dist < minDist) {
                    minDist = dist;
                    bestStep = adj;
                } else if (dist != null && dist == minDist && isEarlierInReadingOrder(adj, bestStep)) {
                    bestStep = adj;
                }
            }

            return bestStep;
        }

        private void attack(@NotNull Unit attacker, @NotNull List<Unit> targets) {
            List<Unit> adjacentTargets = new ArrayList<>();
            for (Unit target : targets) {
                if (Math.abs(target.x - attacker.x) + Math.abs(target.y - attacker.y) == 1) {
                    adjacentTargets.add(target);
                }
            }

            if (adjacentTargets.isEmpty()) {
                return;
            }

            adjacentTargets.sort(Comparator.comparingInt((Unit u) -> u.hitPoints)
                    .thenComparingInt(u -> u.y)
                    .thenComparingInt(u -> u.x));

            Unit target = adjacentTargets.get(0);
            target.hitPoints -= attacker.attackPower;
        }

        private boolean isInRange(@NotNull Unit unit, @NotNull List<Unit> targets) {
            for (Unit target : targets) {
                if (Math.abs(target.x - unit.x) + Math.abs(target.y - unit.y) == 1) {
                    return true;
                }
            }
            return false;
        }

        @NotNull
        private List<Unit> getTargets(@NotNull Unit unit) {
            List<Unit> targets = new ArrayList<>();
            char enemyType = unit.type == ELF ? GOBLIN : ELF;
            for (Unit other : units) {
                if (other.type == enemyType && other.isAlive()) {
                    targets.add(other);
                }
            }
            return targets;
        }

        private boolean isOpen(int x, int y) {
            if (y < 0 || y >= map.length || x < 0 || x >= map[0].length) {
                return false;
            }
            if (map[y][x] != OPEN) {
                return false;
            }
            for (Unit unit : units) {
                if (unit.isAlive() && unit.x == x && unit.y == y) {
                    return false;
                }
            }
            return true;
        }

        @NotNull
        private List<Position> getAdjacentPositions(int x, int y) {
            return Arrays.asList(
                    new Position(x, y - 1),
                    new Position(x - 1, y),
                    new Position(x + 1, y),
                    new Position(x, y + 1)
            );
        }

        private boolean isEarlierInReadingOrder(Position a, Position b) {
            if (b == null) {
                return true;
            }
            if (a.y != b.y) {
                return a.y < b.y;
            }
            return a.x < b.x;
        }

        private int getSumOfHitPoints() {
            return units.stream().filter(Unit::isAlive).mapToInt(u -> u.hitPoints).sum();
        }

        public int countElves() {
            return (int) units.stream().filter(u -> u.type == ELF && u.isAlive()).count();
        }
    }

    private static class Unit {
        char type;
        int x;
        int y;
        int hitPoints;
        int attackPower;

        public Unit(char type, int x, int y, int attackPower) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.hitPoints = DEFAULT_HIT_POINTS;
            this.attackPower = attackPower;
        }

        public boolean isAlive() {
            return hitPoints > 0;
        }
    }

    private static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}

