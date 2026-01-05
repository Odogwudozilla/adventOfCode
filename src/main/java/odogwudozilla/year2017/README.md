# Advent of Code 2017 Solutions

[← Back to Main README](../../../../../README.md)

## Year Overview

Advent of Code 2017 presents algorithmic challenges ranging from string manipulation and circular lists to complex graph problems and assembly simulations.

**Note**: 2017 has 25 days available (complete year).

---

## Implemented Puzzles

### Day 1: Inverse Captcha
**Link**: [https://adventofcode.com/2017/day/1](https://adventofcode.com/2017/day/1)

**Description**: Review a sequence of digits and find the sum of all digits that match the next digit in a circular list.

**Source Code**: [InverseCaptchaAOC2017Day1.java](day1/)

**Algorithm**: Iterate through digits, comparing each with the next (or opposite) digit using modulo for circular indexing.

---

### Day 2: Corruption Checksum
**Link**: [https://adventofcode.com/2017/day/2](https://adventofcode.com/2017/day/2)

**Description**: Calculate the spreadsheet's checksum by summing the difference between the largest and smallest value in each row.

**Source Code**: [CorruptionChecksumAOC2017Day2.java](day2/)

**Algorithm**: For each row, parse numbers, find min and max, sum their differences for the checksum.

---

### Day 5: A Maze of Twisty Trampolines, All Alike
**Link**: [https://adventofcode.com/2017/day/5](https://adventofcode.com/2017/day/5)

**Description**: Traverse a list of jump offsets, incrementing each instruction after use, to determine the number of steps required to exit the maze.

**Source Code**: [AmazeOfTwistyTrampolinesAOC2017Day5.java](day5/)

**Algorithm**: Simulate jumps using an array, incrementing each instruction after jumping, and count steps until the pointer leaves the array bounds.

---

### Day 21: Fractal Art
**Link**: [https://adventofcode.com/2017/day/21](https://adventofcode.com/2017/day/21)

**Description**: Enhance a pixel grid using transformation rules, splitting into blocks and applying rotations/flips to match rules. Count pixels on after a set number of iterations (5 for Part 1, 18 for Part 2).

**Source Code**: [FractalArtAOC2017Day21.java](day21/)

**Algorithm**: Parse enhancement rules, repeatedly split the grid into blocks, match each block (with all rotations/flips) to rules, stitch enhanced blocks, and count on pixels after iterations. For Part 2, run the process for 18 iterations.

---
