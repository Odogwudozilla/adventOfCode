# Advent of Code 2018

This README tracks all implemented solutions for Advent of Code 2018.

## Implemented Puzzles

| Day | Title                | Link                                      | Description                                                                 | Source Code Link                                                                 |
|-----|----------------------|-------------------------------------------|-----------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| 1   | Chronal Calibration  | https://adventofcode.com/2018/day/1       | Part 1: Sum all frequency changes.<br>Part 2: Find the first frequency reached twice. | [ChronalCalibrationAOC2018Day1.java](./day1/ChronalCalibrationAOC2018Day1.java)  |
| 2   | Inventory Management System | https://adventofcode.com/2018/day/2 | Part 1: Count box IDs with exactly two and three of any letter, multiply for checksum.<br>Part 2: Find the box IDs that differ by one character and output the common letters. Result: qysdtrkloagnfozuwujmhrbvx | [InventoryManagementSystemAOC2018Day2.java](./day2/InventoryManagementSystemAOC2018Day2.java) |
| 3   | No Matter How You Slice It | https://adventofcode.com/2018/day/3 | Find overlapping fabric squares and the only non-overlapping claim.          | [NoMatterHowYouSliceItAOC2018Day3.java](./day3/NoMatterHowYouSliceItAOC2018Day3.java) |

## Navigation
- [Back to main README](../../../README.md)

---

### Notes
- Algorithm: For Day 1, sum all changes for Part 1; for Part 2, track frequencies until a repeat is found.
- Algorithm: For Day 2, use a frequency map for each box ID to count letters, then tally IDs with exactly two and three of any letter for the checksum. For Part 2, compare all pairs to find IDs differing by one character and output the common letters.
