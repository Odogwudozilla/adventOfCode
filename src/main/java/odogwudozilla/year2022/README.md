# Advent of Code 2022

This directory contains solutions for Advent of Code 2022.

## Overview

Advent of Code 2022 continues the tradition of daily programming puzzles throughout December. Each puzzle builds upon problem-solving skills and algorithmic thinking.

## Implemented Puzzles

### Day 1: Calorie Counting
**Link**: [Day 1: Calorie Counting](https://adventofcode.com/2022/day/1)

**Description**: The Elves are taking inventory of their food supplies by counting calories. Each Elf's inventory is separated by blank lines. Find the Elf carrying the most calories, and then find the sum of calories carried by the top three Elves.

**Source**: [CalorieCountingAOC2022Day1.java](day1/CalorieCountingAOC2022Day1.java)

**Approach**: Parse the input to group calories by Elf (separated by blank lines), calculate totals for each Elf, then find the maximum for Part 1 and sum the top three for Part 2 using sorting.

### Day 2: Rock Paper Scissors
**Link**: [Day 2: Rock Paper Scissors](https://adventofcode.com/2022/day/2)

**Description**: The Elves are running a Rock Paper Scissors tournament. You are given an encrypted strategy guide to calculate your score. Part 1 interprets the second column as your move; Part 2 interprets it as the desired outcome.

**Source**: [RockPaperScissorsAOC2022Day2.java](day2/RockPaperScissorsAOC2022Day2.java)

**Approach**: For Part 1, map the encrypted moves to Rock/Paper/Scissors and calculate scores based on the rules. For Part 2, determine the required move to achieve the desired outcome and sum the scores accordingly.

### Day 3: Rucksack Reorganization
**Link**: [Day 3: Rucksack Reorganization](https://adventofcode.com/2022/day/3)

**Description**: Find the sum of priorities for misplaced items in rucksacks and badge items for groups of Elves. Part 1 finds the item type that appears in both compartments of each rucksack. Part 2 finds the badge item type common to each group of three Elves.

**Source**: [RucksackReorganizationAOC2022Day3.java](day3/RucksackReorganizationAOC2022Day3.java)

**Approach**: For Part 1, split each rucksack into two compartments and find the common item type, summing its priority. For Part 2, group rucksacks in threes and find the badge item type common to all three, summing its priority.

### Day 8: Treetop Tree House
**Link**: [Day 8: Treetop Tree House](https://adventofcode.com/2022/day/8)

**Description**: Determine if there is enough tree cover for a tree house by counting visible trees in a grid. Part 1 counts trees visible from outside the grid (all trees between it and an edge are shorter). Part 2 finds the tree with the highest scenic score (product of viewing distances in all four directions).

**Source**: [TreetopTreeHouseAOC2022Day8.java](day8/TreetopTreeHouseAOC2022Day8.java)

**Approach**: For Part 1, iterate through each tree and check if it is visible from any of the four directions by examining trees until reaching an edge or a taller tree. For Part 2, calculate the scenic score for each tree by counting visible trees in all four directions, then find the maximum score.

### Day 16: Proboscidea Volcanium
**Link**: [Day 16: Proboscidea Volcanium](https://adventofcode.com/2022/day/16)

**Description**: The sensors have led you to the origin of the distress signal: yet another handheld device, just like the one the Elves gave you. However, you don't see any Elves around; instead, the device is surrounded by elephants! They must have gotten lost in these tunnels, and one of the elephants apparently figured out how to turn on the distress signal. You need to get the elephants out of here, quickly, before the volcano erupts. The puzzle involves a network of pipes and pressure-release valves, and you must determine the most pressure you can release in 30 minutes by opening valves in an optimal order. In Part 2, you and an elephant work together for 26 minutes to maximise the pressure released.

**Source**: [ProboscideaVolcaniumAOC2022Day16.java](day16/ProboscideaVolcaniumAOC2022Day16.java)

**Approach**: Parse the input into a graph of valves and tunnels, precompute shortest paths between all valves, and use depth-first search with memoisation to explore all possible orders of opening valves to maximise released pressure within the time limit. For Part 2, try all possible splits of the valves between the two agents and sum their best results.

---

## Navigation
- [Main README](../../../README.md)
- [All Solutions Database](../../../resources/solutions_database.json)
