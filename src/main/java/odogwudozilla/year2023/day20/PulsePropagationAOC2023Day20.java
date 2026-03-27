// Java solution skeleton for Advent of Code 2023 Day 20: Pulse Propagation
package odogwudozilla.year2023.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Day 20: Pulse Propagation
 * With your help, the Elves manage to find the right parts and fix all of the machines. Now, they just need to send the
 * command to boot up the machines and get the sand flowing again.
 * The puzzle involves simulating a network of modules (flip-flops, conjunctions, broadcaster) that communicate using
 * high and low pulses. The goal is to determine, after pushing the button 1000 times, how many low and high pulses are
 * sent, and to multiply these totals.
 * Official puzzle link: https://adventofcode.com/2023/day/20
 */
public class PulsePropagationAOC2023Day20 {
    private static final int BUTTON_PUSHES = 1000;
    private static final String INPUT_PATH = "src/main/resources/2023/day20/day20_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            PulseNetwork network = PulseNetwork.parse(lines);
            long result = network.solvePartOne(BUTTON_PUSHES);
            System.out.println("Part 1: " + result);
            // Part 2
            network = PulseNetwork.parse(lines); // reset state
            long part2 = network.solvePartTwo();
            System.out.println("Part 2: " + part2);
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    /**
     * Represents the pulse network and provides methods to solve the puzzle.
     */
    public static class PulseNetwork {
        private final Map<String, Module> modules;
        private final String broadcaster;

        private PulseNetwork(Map<String, Module> modules, String broadcaster) {
            this.modules = modules;
            this.broadcaster = broadcaster;
        }

        public static PulseNetwork parse(List<String> lines) {
            Map<String, Module> modules = new HashMap<>();
            String broadcaster = null;
            // First pass: create modules
            for (String line : lines) {
                String[] parts = line.split(" -> ");
                String left = parts[0].trim();
                String[] dests = parts[1].split(", ");
                char type = left.charAt(0);
                String name = (type == '%' || type == '&') ? left.substring(1) : left;
                List<String> destinations = Arrays.asList(dests);
                Module m;
                if (type == '%') {
                    m = new FlipFlopModule(name, destinations);
                } else if (type == '&') {
                    m = new ConjunctionModule(name, destinations);
                } else {
                    broadcaster = name;
                    m = new BroadcasterModule(name, destinations);
                }
                modules.put(name, m);
            }
            // Second pass: set up conjunction inputs
            for (Module m : modules.values()) {
                if (m instanceof ConjunctionModule) {
                    ((ConjunctionModule) m).initInputs(modules);
                }
            }
            return new PulseNetwork(modules, broadcaster);
        }

        /**
         * Simulates the pulse network for the given number of button pushes and returns the product of low and high pulses sent.
         * @param pushes number of button pushes
         * @return product of low and high pulses sent
         */
        public long solvePartOne(int pushes) {
            long lowCount = 0;
            long highCount = 0;
            for (int i = 0; i < pushes; i++) {
                Queue<Pulse> queue = new ArrayDeque<>();
                queue.add(new Pulse("button", broadcaster, false)); // false = low
                while (!queue.isEmpty()) {
                    Pulse pulse = queue.poll();
                    if (pulse.isHigh) highCount++;
                    else lowCount++;
                    Module dest = modules.get(pulse.to);
                    if (dest != null) {
                        dest.receive(pulse, queue);
                    }
                }
            }
            return lowCount * highCount;
        }

        /**
         * Solves Part 2 by finding the fewest button presses needed to deliver a single low pulse to 'rx'.
         * <p>
         * 'rx' receives input only from the conjunction module 'gq'. A conjunction module sends a low pulse
         * only when all its inputs are currently high. So we need all inputs of 'gq' to send a high pulse
         * in the same button press. Each input of 'gq' operates on its own independent cycle, so we find
         * the first button press at which each input sends a high pulse to 'gq', then compute the LCM of
         * all those cycle lengths.
         * @return the fewest number of button presses required to deliver a low pulse to 'rx'
         */
        public long solvePartTwo() {
            // Find which module feeds directly into rx - it must be a conjunction module
            String rxFeeder = findRxFeeder();
            if (rxFeeder == null) {
                throw new IllegalStateException("No module feeds into rx");
            }
            // Find all inputs to the rx-feeder conjunction module
            Set<String> feederInputs = findInputsOf(rxFeeder);
            // Track the first button press at which each feeder input sends a high pulse to the feeder
            Map<String, Long> cycleMap = new HashMap<>();
            long presses = 0;
            while (cycleMap.size() < feederInputs.size()) {
                presses++;
                Queue<Pulse> queue = new ArrayDeque<>();
                queue.add(new Pulse("button", broadcaster, false));
                while (!queue.isEmpty()) {
                    Pulse pulse = queue.poll();
                    if (rxFeeder.equals(pulse.to) && pulse.isHigh && feederInputs.contains(pulse.from)) {
                        cycleMap.putIfAbsent(pulse.from, presses);
                    }
                    Module dest = modules.get(pulse.to);
                    if (dest != null) {
                        dest.receive(pulse, queue);
                    }
                }
            }
            // The answer is the LCM of all cycle lengths
            return cycleMap.values().stream().reduce(1L, this::lcm);
        }

        /**
         * Finds the name of the single module that sends pulses directly to 'rx'.
         * @return the name of the module feeding rx, or null if none is found
         */
        private String findRxFeeder() {
            for (Module m : modules.values()) {
                if (m.destinations.contains("rx")) {
                    return m.name;
                }
            }
            return null;
        }

        /**
         * Finds all module names whose destination list includes the given target module.
         * @param target the name of the target module
         * @return the set of module names that send pulses to the target
         */
        private Set<String> findInputsOf(String target) {
            Set<String> inputs = new HashSet<>();
            for (Module m : modules.values()) {
                if (m.destinations.contains(target)) {
                    inputs.add(m.name);
                }
            }
            return inputs;
        }
        private long lcm(long a, long b) {
            return a * b / gcd(a, b);
        }
        private long gcd(long a, long b) {
            while (b != 0) {
                long t = b;
                b = a % b;
                a = t;
            }
            return a;
        }
    }

    public record Pulse(String from, String to, boolean isHigh) {
    }

    public static abstract class Module {
        protected final String name;
        protected final List<String> destinations;
        public Module(String name, List<String> destinations) {
            this.name = name;
            this.destinations = destinations;
        }
        public abstract void receive(Pulse pulse, Queue<Pulse> queue);
    }

    public static class BroadcasterModule extends Module {
        public BroadcasterModule(String name, List<String> destinations) {
            super(name, destinations);
        }
        @Override
        public void receive(Pulse pulse, Queue<Pulse> queue) {
            for (String dest : destinations) {
                queue.add(new Pulse(name, dest, pulse.isHigh));
            }
        }
    }

    public static class FlipFlopModule extends Module {
        private boolean isOn = false;
        public FlipFlopModule(String name, List<String> destinations) {
            super(name, destinations);
        }
        @Override
        public void receive(Pulse pulse, Queue<Pulse> queue) {
            if (pulse.isHigh) return;
            isOn = !isOn;
            for (String dest : destinations) {
                queue.add(new Pulse(name, dest, isOn));
            }
        }
    }

    public static class ConjunctionModule extends Module {
        private final Map<String, Boolean> memory = new HashMap<>();
        public ConjunctionModule(String name, List<String> destinations) {
            super(name, destinations);
        }
        public void initInputs(Map<String, Module> modules) {
            for (Module m : modules.values()) {
                if (m.destinations.contains(name)) {
                    memory.put(m.name, false); // default to low
                }
            }
        }
        @Override
        public void receive(Pulse pulse, Queue<Pulse> queue) {
            memory.put(pulse.from, pulse.isHigh);
            boolean allHigh = memory.values().stream().allMatch(Boolean::booleanValue);
            boolean outPulse = !allHigh; // send low if all high, else high
            for (String dest : destinations) {
                queue.add(new Pulse(name, dest, outPulse));
            }
        }
    }
}
