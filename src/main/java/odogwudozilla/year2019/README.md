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

[Back to main README](../../../README.md)
