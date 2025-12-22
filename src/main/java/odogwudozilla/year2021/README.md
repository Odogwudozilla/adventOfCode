# Advent of Code 2021 Solutions

[← Back to Main README](../../../../../README.md)

## Year Overview

Advent of Code 2021 features a submarine adventure attempting to recover lost sleigh keys from the ocean. The puzzles involve analyzing sonar data, calculating depths, and working with sliding windows.

---

## Implemented Puzzles

### Day 1: Sonar Sweep
**Link**: [https://adventofcode.com/2021/day/1](https://adventofcode.com/2021/day/1)

**Description**: Analyze sonar sweep measurements to count depth increases. Part 1 counts individual measurement increases. Part 2 analyzes three-measurement sliding windows.

**Source Code**: [SonarSweepAOC2021Day1.java](day1/)

**Algorithm/Approach**: Stream-based iteration through depth measurements, comparing each with the previous value. For Part 2, calculate window sums using sliding window technique and apply the same logic.

---

### Day 2: Dive!
**Link**: [https://adventofcode.com/2021/day/2](https://adventofcode.com/2021/day/2)

**Description**: Follow a series of movement commands to calculate the submarine's final horizontal position and depth. Part 1 multiplies these for the answer. Part 2 introduces 'aim' for more complex movement.

**Source Code**: [DiveAOC2021Day2.java](day2/)

**Algorithm/Approach**: Iterate through commands, updating horizontal and depth. For Part 2, track aim and update depth accordingly when moving forward.

---
