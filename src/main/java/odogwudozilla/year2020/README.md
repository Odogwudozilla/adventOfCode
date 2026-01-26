# Advent of Code 2020 Solutions

[← Back to Main README](../../../../../README.md)

## Year Overview

Advent of Code 2020 featured puzzles themed around tropical vacation adventures and cryptographic challenges.

---

## Implemented Puzzles

### Day 1: Report Repair
**Link**: [https://adventofcode.com/2020/day/1](https://adventofcode.com/2020/day/1)

**Description**: Find the two entries in your expense report that sum to 2020 and multiply them together. Part 2: Find three entries that sum to 2020 and multiply them together.

**Source Code**: [ReportRepairAOC2020Day1.java](day1/)

---

### Day 2: Password Philosophy
**Link**: [https://adventofcode.com/2020/day/2](https://adventofcode.com/2020/day/2)

**Description**: Validate passwords against two different policies. Part 1 checks if a letter appears within a specified range. Part 2 checks if a letter appears in exactly one of two specified positions.

**Source Code**: [PasswordPhilosophyAOC2020Day2.java](day2/)

---

### Day 8: Handheld Halting
**Link**: [https://adventofcode.com/2020/day/8](https://adventofcode.com/2020/day/8)

**Description**: Fix a handheld game console with an infinite loop in its boot code. The console runs instructions (acc, jmp, nop) and you need to detect the infinite loop. Part 1: Find the accumulator value immediately before any instruction is executed a second time. Part 2: Fix the code by changing exactly one jmp to nop or vice versa to make the program terminate normally.

**Source Code**: [HandheldHaltingAOC2020Day8.java](day8/)

**Algorithm**: Part 1 detects infinite loops by tracking visited instruction indices. Part 2 systematically tries flipping each jmp/nop instruction until the program terminates successfully.

---

### Day 9: Encoding Error
**Link**: [https://adventofcode.com/2020/day/9](https://adventofcode.com/2020/day/9)

**Description**: Find the first number in a list that is not the sum of two of the 25 previous numbers (XMAS encryption anomaly). Part 2: Find a contiguous set of numbers that sum to the invalid number, then return the sum of the smallest and largest numbers in that set.

**Source Code**: [EncodingErrorAOC2020Day9.java](day9/)

**Algorithm**: Part 1 uses a preamble window approach checking all pairs for valid sums. Part 2 uses a nested loop to find contiguous subsequences and their sums.

---

### Day 23: Crab Cups
**Link**: [https://adventofcode.com/2020/day/23](https://adventofcode.com/2020/day/23)

**Description**: Simulate a game where cups are moved in a circle according to specific rules. Part 1: After 100 moves, what is the order of cups after cup 1? Part 2: After 10 million moves with 1 million cups, what is the product of the two cups immediately clockwise of cup 1?

**Source Code**: [CrabCupsAOC2020Day23.java](day23/)

**Algorithm**: Part 1 uses a LinkedList to simulate cup moves. Part 2 uses an array-based linked list for efficient simulation of 10 million moves with 1 million cups.

---

### Day 25: Combo Breaker
**Link**: [https://adventofcode.com/2020/day/25](https://adventofcode.com/2020/day/25)

**Description**: Break the encryption used by the door lock at the resort by finding the encryption key through cryptographic loop calculations.

**Source Code**: [ComboBreakerAOC2020Day25.java](day25/)

**Note**: This is the final day puzzle. Part 2 cannot be completed until all previous days (1-24) are solved.

---

[← Back to Main README](../../../../../README.md) | [View All Years](../../../../../README.md#-years)
