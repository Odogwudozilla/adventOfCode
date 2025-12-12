# Advent of Code 2025 Solutions

[← Back to Main README](../../../../../README.md)

## Year Overview

Advent of Code 2025 features a journey through various rooms of the North Pole headquarters, each presenting unique algorithmic challenges.

**Note**: 2025 has 12 days available (as of December 12, 2025).

---

## Implemented Puzzles

### Day 1: Secret Entrance
**Link**: [https://adventofcode.com/2025/day/1](https://adventofcode.com/2025/day/1)

**Description**: Navigate the secret entrance using list comparisons and similarity calculations.

**Source Code**: [SecretEntranceAOC2025Day1.java](day1/)

---

### Day 2: Gift Shop
**Link**: [https://adventofcode.com/2025/day/2](https://adventofcode.com/2025/day/2)

**Description**: Solve gift shop inventory and pricing problems.

**Source Code**: [GiftShopAOC2025Day2.java](day2/)

---

### Day 3: Lobby
**Link**: [https://adventofcode.com/2025/day/3](https://adventofcode.com/2025/day/3)

**Description**: Parse and process lobby instructions with conditional logic.

**Source Code**: [LobbyAOC2025Day3.java](day3/)

---

### Day 4: Printing Department
**Link**: [https://adventofcode.com/2025/day/4](https://adventofcode.com/2025/day/4)

**Description**: Work through printing department word search and pattern matching challenges.

**Source Code**: [PrintingDepartmentAOC2025Day4.java](day4/)

---

### Day 5: Cafeteria
**Link**: [https://adventofcode.com/2025/day/5](https://adventofcode.com/2025/day/5)

**Description**: Solve cafeteria ordering and dependency problems.

**Source Code**: [CafeteriaAOC2025Day5.java](day5/)

---

### Day 6: Trash Compactor
**Link**: [https://adventofcode.com/2025/day/6](https://adventofcode.com/2025/day/6)

**Description**: Navigate and optimize trash compactor operations.

**Source Code**: [TrashCompactorAOC2025Day6.java](day6/)

---

### Day 7: Laboratories
**Link**: [https://adventofcode.com/2025/day/7](https://adventofcode.com/2025/day/7)

**Description**: Solve laboratory equipment calibration and equation validation problems.

**Source Code**: [LaboratoriesAOC2025Day7.java](day7/)

---

### Day 8: Playground
**Link**: [https://adventofcode.com/2025/day/8](https://adventofcode.com/2025/day/8)

**Description**: Work through playground grid-based antenna and signal problems.

**Source Code**: [PlaygroundAOC2025Day8.java](day8/)

---

### Day 9: Movie Theater
**Link**: [https://adventofcode.com/2025/day/9](https://adventofcode.com/2025/day/9)

**Description**: Optimize movie theater disk fragmentation and file system operations.

**Source Code**: [MovieTheaterAOC2025Day9.java](day9/)

---

### Day 10: Factory
**Link**: [https://adventofcode.com/2025/day/10](https://adventofcode.com/2025/day/10)

**Description**: Navigate factory floor topographic maps and trail calculations.

**Source Code**: [FactoryAOC2025Day10.java](day10/)

---

### Day 11: Reactor
**Link**: [https://adventofcode.com/2025/day/11](https://adventofcode.com/2025/day/11)

**Description**: Count all paths through a directed graph of devices from start to end, with Part 2 requiring paths to pass through specific intermediate nodes. Uses dynamic programming with memoization for efficient path counting in a DAG structure.

**Source Code**: [ReactorAOC2025Day11.java](day11/)

**Algorithm**: DFS with backtracking for Part 1, state-space DP with memoization (node, hasReq1, hasReq2) for Part 2.

---

### Day 12: Christmas Tree Farm
**Link**: [https://adventofcode.com/2025/day/12](https://adventofcode.com/2025/day/12)

**Description**: Determine how many rectangular regions can fit all required presents with irregular shapes. Presents can be rotated and flipped, must not overlap, and must align to a grid. A classic 2D polyomino packing problem solved using constraint satisfaction with backtracking.

**Source Code**: [ChristmasTreeFarmAOC2025Day12.java](day12/)

**Algorithm**: Backtracking with pruning. For each present, tries all orientations (rotations and flips) at all valid grid positions. Uses early termination when total area exceeds available space. Part 2 automatically awarded upon Part 1 completion.

---

[← Back to Main README](../../../../../README.md) | [View All Years](../../../../../README.md#-years)

