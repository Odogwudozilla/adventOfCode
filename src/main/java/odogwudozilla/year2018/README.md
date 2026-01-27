# Advent of Code 2018

This README tracks all implemented solutions for Advent of Code 2018.

## Implemented Puzzles

| Day | Title                | Link                                      | Description                                                                 | Source Code Link                                                                 |
|-----|----------------------|-------------------------------------------|-----------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| 1   | Chronal Calibration  | https://adventofcode.com/2018/day/1       | Part 1: Sum all frequency changes.<br>Part 2: Find the first frequency reached twice. | [ChronalCalibrationAOC2018Day1.java](./day1/ChronalCalibrationAOC2018Day1.java)  |
| 2   | Inventory Management System | https://adventofcode.com/2018/day/2 | Part 1: Count box IDs with exactly two and three of any letter, multiply for checksum.<br>Part 2: Find the box IDs that differ by one character and output the common letters. | [InventoryManagementSystemAOC2018Day2.java](./day2/InventoryManagementSystemAOC2018Day2.java) |
| 3   | No Matter How You Slice It | https://adventofcode.com/2018/day/3 | Find overlapping fabric squares and the only non-overlapping claim.          | [NoMatterHowYouSliceItAOC2018Day3.java](./day3/NoMatterHowYouSliceItAOC2018Day3.java) |
| 4   | Repose Record | https://adventofcode.com/2018/day/4 | Analyse guard sleep records to find the guard most frequently asleep and the minute they are most often asleep (Part 1), and the guard most frequently asleep on the same minute (Part 2). | [ReposeRecordAOC2018Day4.java](./day4/ReposeRecordAOC2018Day4.java) |
| 11  | Chronal Charge | https://adventofcode.com/2018/day/11 | Find the fuel cell square with the largest total power in a 300x300 grid.<br>Part 1: Find 3x3 square.<br>Part 2: Find any-sized square (1x1 to 300x300). | [ChronalChargeAOC2018Day11.java](./day11/ChronalChargeAOC2018Day11.java) |
| 15  | Beverage Bandits | https://adventofcode.com/2018/day/15 | Combat simulation between Elves and Goblins with turn-based movement and attacks.<br>Part 1: Standard combat outcome.<br>Part 2: Find minimum elf attack power with no elf casualties. | [BeverageBanditsAOC2018Day15.java](./day15/BeverageBanditsAOC2018Day15.java) |
| 16  | Chronal Classification | https://adventofcode.com/2018/day/16 | Reverse-engineer opcodes for a device with four registers.<br>Part 1: Count samples behaving like 3+ opcodes.<br>Part 2: Deduce opcode mappings and execute test program. | [ChronalClassificationAOC2018Day16.java](./day16/ChronalClassificationAOC2018Day16.java) |

## Navigation
- [Back to main README](../../../README.md)

---

### Notes
- Algorithm: For Day 1, sum all changes for Part 1; for Part 2, track frequencies until a repeat is found.
- Algorithm: For Day 2, use a frequency map for each box ID to count letters, then tally IDs with exactly two and three of any letter for the checksum. For Part 2, compare all pairs to find IDs differing by one character and output the common letters.
- Algorithm: For Day 3, map all fabric claims onto a grid, count overlaps for Part 1, and identify the only claim with no overlap for Part 2.
- Algorithm: For Day 4, sort records, build a sleep map for each guard, and analyse sleep patterns to solve both strategies.
- Algorithm: For Day 11, calculate power levels using the formula (rack ID * Y + serial) * rack ID, extract hundreds digit, and subtract 5. Part 1 brute-forces all 3x3 squares. Part 2 uses a Summed Area Table (SAT) for O(1) range queries, enabling efficient evaluation of all square sizes (1x1 to 300x300).
- Algorithm: For Day 15, implement turn-based combat simulation with BFS pathfinding for movement, reading-order tie-breaking, and HP tracking. Part 2 uses binary search to find minimum elf attack power ensuring no elf deaths.
- Algorithm: For Day 16, implement all 16 opcodes (arithmetic, bitwise, comparison, assignment). Part 1 tests each sample against all opcodes. Part 2 uses constraint satisfaction and propagation to deduce opcode mappings, then executes the test program.
