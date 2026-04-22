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

### Day 3: Toboggan Trajectory
**Link**: [https://adventofcode.com/2020/day/3](https://adventofcode.com/2020/day/3)

**Description**: Traverse a repeating map of open squares and trees, counting trees encountered for various slopes. Part 1: Right 3, down 1. Part 2: Multiply the number of trees encountered for five different slopes.

**Source Code**: [TobogganTrajectoryAOC2020Day3.java](day3/)

---

### Day 6: Custom Customs
**Link**: [https://adventofcode.com/2020/day/6](https://adventofcode.com/2020/day/6)

**Description**: For each group, count the number of questions to which anyone answered "yes" (Part 1) and the number to which everyone answered "yes" (Part 2). Sum these counts for all groups.

**Source Code**: [CustomCustomsAOC2020Day6.java](day6/)

**Algorithm**: For each group, Part 1 collects all unique answers using a set. Part 2 finds the intersection of all answers in the group. Sum the set sizes for all groups.

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

### Day 10: Adapter Array
**Link**: [https://adventofcode.com/2020/day/10](https://adventofcode.com/2020/day/10)

**Description**: Given a list of joltage adapters, find a chain that uses all adapters to connect the charging outlet to your device. Count the 1-jolt and 3-jolt differences between consecutive adapters, then multiply these counts.

**Source Code**: [AdapterArrayAOC2020Day10.java](day10/)

**Algorithm**: Sort the adapter list, count the differences between consecutive adapters, and account for the device's built-in adapter (+3 jolts).

---

### Day 11: Seating System
**Link**: [https://adventofcode.com/2020/day/11](https://adventofcode.com/2020/day/11)

**Description**: Simulate a seating system where seats become occupied or empty based on adjacent seats. Apply rules until the system stabilises, then count the number of occupied seats.

**Source Code**: [SeatingSystemAOC2020Day11.java](day11/)

---

### Day 19: Monster Messages
**Link**: [https://adventofcode.com/2020/day/19](https://adventofcode.com/2020/day/19)

**Description**: Validate messages against a set of rules that build upon each other. Rules can match literal characters or reference other rules, with support for alternatives (|). Part 1: Determine how many messages completely match rule 0. Part 2: Update rules 8 and 11 to introduce loops and count valid messages.

**Source Code**: [MonsterMessagesAOC2020Day19.java](day19/)

**Algorithm**: Part 1 builds a regular expression from the rules and matches messages against it. Part 2 handles the recursive rules by counting how many times rule 42 and rule 31 match sequentially, ensuring the pattern follows the required structure (more rule 42 matches than rule 31 matches).

---

### Day 21: Allergen Assessment
**Link**: [https://adventofcode.com/2020/day/21](https://adventofcode.com/2020/day/21)

**Description**: Determine which ingredients cannot possibly contain any of the allergens in your list and count their appearances. Then, resolve the canonical dangerous ingredient list by allergen.

**Source Code**: [AllergenAssessmentAOC2020Day21.java](day21/)

**Algorithm**: For each allergen, intersect ingredient sets across all foods listing that allergen. Remove resolved ingredients iteratively to map allergens uniquely. Count safe ingredient appearances for Part 1; sort dangerous ingredients by allergen for Part 2.

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
