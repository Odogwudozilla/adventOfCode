package odogwudozilla.year2017.day20;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Advent of Code 2017, Day 20: Particle Swarm
 * <p>
 * The GPU needs help simulating particles. Each particle has position (p), velocity (v), and
 * acceleration (a) in 3D space. We need to find which particle stays closest to the origin
 * <0,0,0> in the long term using Manhattan distance.
 * <p>
 * Part 1: Find the particle that stays closest to the origin.
 * Part 2: Simulate particle collisions - particles at the same position destroy each other.
 *
 * @see <a href="https://adventofcode.com/2017/day/20">Advent of Code 2017 Day 20</a>
 */
public class ParticleSwarmAOC2017Day20 {
    private static final String INPUT_FILE = "src/main/resources/2017/day20/day20_puzzle_data.txt";
    private static final Pattern PARTICLE_PATTERN = Pattern.compile(
            "p=<(-?\\d+),(-?\\d+),(-?\\d+)>, v=<(-?\\d+),(-?\\d+),(-?\\d+)>, a=<(-?\\d+),(-?\\d+),(-?\\d+)>"
    );

    static class Particle {
        int id;
        long px, py, pz;
        long vx, vy, vz;
        long ax, ay, az;

        Particle(int id, long px, long py, long pz, long vx, long vy, long vz, long ax, long ay, long az) {
            this.id = id;
            this.px = px;
            this.py = py;
            this.pz = pz;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.ax = ax;
            this.ay = ay;
            this.az = az;
        }

        void update() {
            vx += ax;
            vy += ay;
            vz += az;
            px += vx;
            py += vy;
            pz += vz;
        }

        long manhattanDistance() {
            return Math.abs(px) + Math.abs(py) + Math.abs(pz);
        }

        @Override
        public String toString() {
            return String.format("id=%d, p=<%d,%d,%d>, v=<%d,%d,%d>, a=<%d,%d,%d>",
                    id, px, py, pz, vx, vy, vz, ax, ay, az);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
        List<Particle> particles = parseParticles(lines);

        System.out.println("Part 1: " + solvePartOne(particles));
        System.out.println("Part 2: " + solvePartTwo(particles));
    }

    static List<Particle> parseParticles(List<String> lines) {
        List<Particle> particles = new ArrayList<>();
        int id = 0;
        for (String line : lines) {
            Matcher matcher = PARTICLE_PATTERN.matcher(line);
            if (matcher.find()) {
                long px = Long.parseLong(matcher.group(1));
                long py = Long.parseLong(matcher.group(2));
                long pz = Long.parseLong(matcher.group(3));
                long vx = Long.parseLong(matcher.group(4));
                long vy = Long.parseLong(matcher.group(5));
                long vz = Long.parseLong(matcher.group(6));
                long ax = Long.parseLong(matcher.group(7));
                long ay = Long.parseLong(matcher.group(8));
                long az = Long.parseLong(matcher.group(9));
                particles.add(new Particle(id++, px, py, pz, vx, vy, vz, ax, ay, az));
            }
        }
        return particles;
    }

    static long solvePartOne(List<Particle> particles) {
        List<Particle> copy = deepCopyParticles(particles);

        long closestId = 0;
        long minDistance = Long.MAX_VALUE;
        int stableCount = 0;
        final int STABLE_THRESHOLD = 1000;

        for (int tick = 0; tick < 10000; tick++) {
            for (Particle p : copy) {
                p.update();
            }

            long currentMin = Long.MAX_VALUE;
            long currentClosestId = 0;
            for (Particle p : copy) {
                long dist = p.manhattanDistance();
                if (dist < currentMin) {
                    currentMin = dist;
                    currentClosestId = p.id;
                }
            }

            if (currentClosestId == closestId) {
                stableCount++;
            } else {
                closestId = currentClosestId;
                minDistance = currentMin;
                stableCount = 0;
            }

            if (stableCount >= STABLE_THRESHOLD) {
                break;
            }
        }

        return closestId;
    }

    static long solvePartTwo(List<Particle> particles) {
        List<Particle> copy = deepCopyParticles(particles);

        for (int tick = 0; tick < 10000; tick++) {
            for (Particle p : copy) {
                p.update();
            }

            Map<String, List<Particle>> positionMap = new HashMap<>();
            for (Particle p : copy) {
                String pos = p.px + "," + p.py + "," + p.pz;
                positionMap.computeIfAbsent(pos, k -> new ArrayList<>()).add(p);
            }

            Set<Integer> toRemove = new HashSet<>();
            for (List<Particle> particlesAtPos : positionMap.values()) {
                if (particlesAtPos.size() > 1) {
                    for (Particle p : particlesAtPos) {
                        toRemove.add(p.id);
                    }
                }
            }

            copy.removeIf(p -> toRemove.contains(p.id));

            if (copy.size() == 1) {
                break;
            }
        }

        return copy.size();
    }

    static List<Particle> deepCopyParticles(List<Particle> particles) {
        List<Particle> copy = new ArrayList<>();
        for (Particle p : particles) {
            copy.add(new Particle(p.id, p.px, p.py, p.pz, p.vx, p.vy, p.vz, p.ax, p.ay, p.az));
        }
        return copy;
    }
}

