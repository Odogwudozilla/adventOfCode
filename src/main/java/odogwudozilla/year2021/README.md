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

### Day 4: Giant Squid
**Link**: [https://adventofcode.com/2021/day/4](https://adventofcode.com/2021/day/4)

**Description**: Play bingo with a set of boards and a sequence of drawn numbers. The first board to complete a row or column wins. The score is calculated by multiplying the sum of unmarked numbers by the last drawn number. Part 2 finds the score of the last board to win.

**Source Code**: [GiantSquidAOC2021Day4.java](day4/)

**Algorithm/Approach**: Parse the drawn numbers and boards, simulate marking numbers, and check for winning conditions after each draw. For Part 2, continue until all boards have won and return the score for the last winning board.

---

### Day 23: Amphipod
**Link**: [https://adventofcode.com/2021/day/23](https://adventofcode.com/2021/day/23)

**Description**: Organise amphipods into their destination rooms in a burrow using the least energy possible. Each amphipod type (Amber, Bronze, Copper, Desert) has a different energy cost per step. Part 1 has rooms with 2 positions. Part 2 expands rooms to 4 positions.

**Source Code**: [AmphipodAOC2021Day23.java](day23/)

**Algorithm/Approach**: Use Dijkstra's algorithm with state-space search to find the minimum energy configuration. Model the burrow state including hallway and room positions. Generate all valid moves based on movement rules and explore states using a priority queue ordered by energy cost. Support variable room depths to handle both Part 1 (depth 2) and Part 2 (depth 4).

---

