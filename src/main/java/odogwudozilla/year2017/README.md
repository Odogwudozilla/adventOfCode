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
