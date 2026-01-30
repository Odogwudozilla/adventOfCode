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
- [Day 16: Flawed Frequency Transmission](https://adventofcode.com/2019/day/16)
  - **Description:** Clean up a noisy signal using the Flawed Frequency Transmission (FFT) algorithm. Each phase applies a repeating pattern to the input digits, producing a new list. After 100 phases, report the first eight digits of the output.
  - **Source:** [FlawedFrequencyTransmissionAOC2019Day16.java](./day16/FlawedFrequencyTransmissionAOC2019Day16.java)
  - **Algorithm:** For each phase, generate output digits by summing products of input digits and a position-dependent pattern, keeping only the ones digit. Repeat for 100 phases.
- [Day 24: Planet of Discord](https://adventofcode.com/2019/day/24)
  - **Description:** Simulate a 5x5 grid of bugs and empty spaces, updating each minute according to adjacency rules. Find the first repeated layout and calculate its biodiversity rating.
  - **Source:** [PlanetOfDiscordAOC2019Day24.java](./day24/PlanetOfDiscordAOC2019Day24.java)
  - **Algorithm:** Track layouts using a set, update the grid per rules, and compute biodiversity when a repeat is found.

[Back to main README](../../../README.md)
