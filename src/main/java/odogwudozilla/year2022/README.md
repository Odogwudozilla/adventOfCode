# Advent of Code 2022

This directory contains solutions for Advent of Code 2022.

## Overview

Advent of Code 2022 continues the tradition of daily programming puzzles throughout December. Each puzzle builds upon problem-solving skills and algorithmic thinking.

## Implemented Puzzles

### Day 1: Calorie Counting
**Link**: [Day 1: Calorie Counting](https://adventofcode.com/2022/day/1)

**Description**: The Elves are taking inventory of their food supplies by counting calories. Each Elf's inventory is separated by blank lines. Find the Elf carrying the most calories, and then find the sum of calories carried by the top three Elves.

**Source**: [CalorieCountingAOC2022Day1.java](day1/CalorieCountingAOC2022Day1.java)

**Solutions**:
- Part 1: `67016`
- Part 2: `200116`

**Approach**: Parse the input to group calories by Elf (separated by blank lines), calculate totals for each Elf, then find the maximum for Part 1 and sum the top three for Part 2 using sorting.

### Day 2: Rock Paper Scissors
**Link**: [Day 2: Rock Paper Scissors](https://adventofcode.com/2022/day/2)

**Description**: The Elves are running a Rock Paper Scissors tournament. You are given an encrypted strategy guide to calculate your score. Part 1 interprets the second column as your move; Part 2 interprets it as the desired outcome.

**Source**: [RockPaperScissorsAOC2022Day2.java](day2/RockPaperScissorsAOC2022Day2.java)

**Approach**: For Part 1, map the encrypted moves to Rock/Paper/Scissors and calculate scores based on the rules. For Part 2, determine the required move to achieve the desired outcome and sum the scores accordingly.

---

## Navigation
- [Main README](../../../README.md)
- [All Solutions Database](../../../resources/solutions_database.json)
