# Advent of Code 2019

## Year Overview

This directory contains solutions for Advent of Code 2019 puzzles.

### Implemented Puzzles

- [Day 1: The Tyranny of the Rocket Equation](https://adventofcode.com/2019/day/1)
  - **Description:** Calculate the total fuel requirements for a list of module masses using the formula: (mass / 3) - 2, summed for all modules. For Part 2, recursively add fuel for the fuel itself until no more is needed.
  - **Source:** [TheTyrannyOfTheRocketEquationAOC2019Day1.java](./day1/TheTyrannyOfTheRocketEquationAOC2019Day1.java)
  - **Algorithm:** Read all module masses, apply the formula, and sum the results. For Part 2, keep adding fuel for the fuel recursively.
- [Day 2: 1202 Program Alarm](https://adventofcode.com/2019/day/2)
  - **Description:** Simulate an Intcode computer. For Part 1, restore the gravity assist program by setting position 1 to 12 and position 2 to 2, then run the program and report the value at position 0. For Part 2, find the noun and verb that produce the output 19690720 and report 100 * noun + verb.
  - **Source:** [ProgramAlarmAOC2019Day2.java](./day2/ProgramAlarmAOC2019Day2.java)
  - **Algorithm:** Implement Intcode interpreter, brute-force noun/verb search for Part 2.
- [Day 3: Crossed Wires](https://adventofcode.com/2019/day/3)
  - **Description:** Trace two wires on a grid, find the closest intersection by Manhattan distance (Part 1), and the intersection with the fewest combined steps (Part 2).
  - **Source:** [CrossedWiresAOC2019Day3.java](./day3/CrossedWiresAOC2019Day3.java)
  - **Algorithm:** Track all visited points for each wire, find intersections, compute Manhattan distances and step counts for each intersection.
- [Day 7: Amplification Circuit](https://adventofcode.com/2019/day/7)
  - **Description:** Configure five amplifiers in series to maximise the thruster signal by testing all permutations of phase settings. Part 1 uses phase settings 0-4 with linear signal flow. Part 2 uses phase settings 5-9 with feedback loop mode where the output of the last amplifier feeds back to the first.
  - **Source:** [AmplificationCircuitAOC2019Day7.java](./day7/AmplificationCircuitAOC2019Day7.java)
  - **Algorithm:** Implement Intcode interpreter with input/output handling. Generate all permutations of phase settings and run the amplifier chain for each, tracking the maximum output signal.
- [Day 9: Sensor Boost](https://adventofcode.com/2019/day/9)
  - **Description:** Implement an Intcode computer with support for relative mode and large memory. Run the BOOST program in test mode (input = 1) to obtain the BOOST keycode.
  - **Source:** [SensorBoostAOC2019Day9.java](./day9/SensorBoostAOC2019Day9.java)
  - **Algorithm:** Intcode interpreter with position, immediate, and relative modes; dynamic memory extension; input/output handling for BOOST program.
- [Day 11: Space Police](https://adventofcode.com/2019/day/11)
  - **Description:** Build a robot that moves on a grid, painting panels black or white according to an Intcode program. Part 1 counts the number of panels painted at least once. Part 2 renders the registration identifier painted by the robot.
  - **Source:** [SpacePoliceAOC2019Day11.java](./day11/SpacePoliceAOC2019Day11.java)
  - **Algorithm:** Simulate robot movement and painting using a custom Intcode interpreter. Track painted panels and render the output grid for Part 2.
- [Day 13: Care Package](https://adventofcode.com/2019/day/13)
  - **Description:** The arcade cabinet draws tiles to the screen using Intcode output. Count the number of block tiles (tile id 2) on the screen when the game exits.
  - **Source:** [CarePackageAOC2019Day13.java](./day13/CarePackageAOC2019Day13.java)
  - **Algorithm:** Run Intcode interpreter, parse output in triplets, and count block tiles.
- [Day 16: Flawed Frequency Transmission](https://adventofcode.com/2019/day/16)
  - **Description:** Clean up a noisy signal using the Flawed Frequency Transmission (FFT) algorithm. Each phase applies a repeating pattern to the input digits, producing a new list. After 100 phases, report the first eight digits of the output.
  - **Source:** [FlawedFrequencyTransmissionAOC2019Day16.java](./day16/FlawedFrequencyTransmissionAOC2019Day16.java)
  - **Algorithm:** For each phase, generate output digits by summing products of input digits and a position-dependent pattern, keeping only the ones digit. Repeat for 100 phases.
- [Day 22: Slam Shuffle](https://adventofcode.com/2019/day/22)
  - **Description:** Simulate a deck of cards and apply a sequence of shuffling techniques (deal into new stack, cut N, deal with increment N). Part 1 finds the position of card 2019 in a deck of 10007 cards. Part 2 finds which card is at position 2020 after shuffling a deck of 119315717514047 cards 101741582076661 times.
  - **Source:** [SlamShuffleAOC2019Day22.java](./day22/SlamShuffleAOC2019Day22.java)
  - **Algorithm:** Part 1 - simulate the deck as a list and return the index of the target card. Part 2 - model the entire shuffle sequence as a single affine transformation f(x) = ax + b (mod deckSize), then use modular exponentiation to compose n repetitions, and invert the result to find the card at the queried position. Overflow-safe multiplication is achieved using BigInteger for intermediate products.
- [Day 24: Planet of Discord](https://adventofcode.com/2019/day/24)
  - **Description:** Simulate a 5x5 grid of bugs and empty spaces, updating each minute according to adjacency rules. Find the first repeated layout and calculate its biodiversity rating.
  - **Source:** [PlanetOfDiscordAOC2019Day24.java](./day24/PlanetOfDiscordAOC2019Day24.java)
  - **Algorithm:** Track layouts using a set, update the grid per rules, and compute biodiversity when a repeat is found.
- [Day 21: Springdroid Adventure](https://adventofcode.com/2019/day/21)
  - **Description:** Program a springdroid using springscript to jump over holes in the hull. Part 1 uses sensors A-D and the WALK command. Part 2 enables extended sensors E-I and uses the RUN command to survey the hull.
  - **Source:** [SpringdroidAdventureAOC2019Day21.java](./day21/SpringdroidAdventureAOC2019Day21.java)
  - **Algorithm:** Implement a minimal Intcode interpreter. For Part 1, jump if any of A, B, or C is a hole and D is ground. For Part 2, also require that H or E is ground before jumping, using extended sensors and RUN mode.

[Back to main README](../../../README.md)
