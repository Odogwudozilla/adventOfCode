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

### Day 5: Print Queue
**Link**: [https://adventofcode.com/2024/day/5](https://adventofcode.com/2024/day/5)

**Description**: Given a set of page ordering rules and a list of updates (page sequences), determine which updates are already in the correct order. For each correctly-ordered update, find the middle page number and sum these values.

**Algorithm**: For each update, check all applicable ordering rules (only those involving present pages). Use index mapping to verify order. Sum the middle page numbers of valid updates.

**Source Code**: [PrintQueueAOC2024Day5.java](day5/)

### Day 18: RAM Run
**Link**: [https://adventofcode.com/2024/day/18](https://adventofcode.com/2024/day/18)

**Description**: Navigate through corrupted computer memory space using BFS pathfinding whilst avoiding falling bytes that corrupt memory locations.

**Algorithm**: Breadth-First Search (BFS) for shortest path, binary search for Part 2

**Source Code**: [RAMRunAOC2024Day18.java](day18/)

### Day 22: Monkey Market
**Link**: [https://adventofcode.com/2024/day/22](https://adventofcode.com/2024/day/22)

**Description**: Simulate buyers' pseudorandom secret number evolution to predict their prices. For each buyer, generate 2000 new secret numbers and sum the 2000th secret number for all buyers.

**Part 2**: Find the best sequence of four price changes (from buyers' price history) that maximises the sum of prices at the first occurrence for each buyer.

**Source Code**: [MonkeyMarketAOC2024Day22.java](day22/)

---

[← Back to Main README](../../../../../README.md) | [View All Years](../../../../../README.md#-years)
