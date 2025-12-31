# Advent of Code 2024 Solutions

[← Back to Main README](../../../../../README.md)

## Year Overview

Advent of Code 2024 brought fresh algorithmic challenges with themes of computer systems and pathfinding.

---

## Implemented Puzzles

### Day 4: Ceres Search
**Link**: [https://adventofcode.com/2024/day/4](https://adventofcode.com/2024/day/4)

**Description**: Find all occurrences of the word 'XMAS' in a word search grid, in any direction (horizontal, vertical, diagonal, backwards, overlapping).

**Part 2**: Find all X-MAS patterns, which are two MAS (or SAM) sequences crossing in an X shape, each MAS can be forwards or backwards.

**Algorithm**: Iterates through each cell and checks all 8 directions for the target word (Part 1). For Part 2, checks all possible X centres for crossing MAS/SAM diagonals.


**Source Code**: [CeresSearchAOC2024Day4.java](day4/)

### Day 18: RAM Run
**Link**: [https://adventofcode.com/2024/day/18](https://adventofcode.com/2024/day/18)

**Description**: Navigate through corrupted computer memory space using BFS pathfinding whilst avoiding falling bytes that corrupt memory locations.

**Algorithm**: Breadth-First Search (BFS) for shortest path, binary search for Part 2

**Source Code**: [RAMRunAOC2024Day18.java](day18/)

---

[← Back to Main README](../../../../../README.md) | [View All Years](../../../../../README.md#-years)
