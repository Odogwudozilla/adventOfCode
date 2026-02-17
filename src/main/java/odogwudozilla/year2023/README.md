# Advent of Code 2023

This directory contains solutions for [Advent of Code 2023](https://adventofcode.com/2023).

## Implemented Puzzles

### Day 3: Gear Ratios
**Link**: [Day 3 - Gear Ratios](https://adventofcode.com/2023/day/3)

**Description**: 
- Part 1: Sum all part numbers in the engine schematic. A part number is any number adjacent to a symbol (including diagonally), where periods (.) do not count as symbols.
- Part 2: Find all gears (asterisks with exactly two adjacent numbers) and sum the products of their two adjacent numbers.

**Source**: [GearRatiosAOC2023Day3.java](day3/GearRatiosAOC2023Day3.java)

**Algorithm/Approach**:
- Part 1: Iterate through the schematic, extract all numbers, and check if they are adjacent to any non-digit, non-period character
- Part 2: Locate all asterisks, find all adjacent numbers for each asterisk, and sum the products of pairs with exactly two numbers

### Day 4: Scratchcards
**Link**: [Day 4 - Scratchcards](https://adventofcode.com/2023/day/4)

**Description**: 
- Part 1: Calculate total points from scratchcards. Each card has winning numbers and your numbers. The first match is worth 1 point, then each subsequent match doubles the points (2, 4, 8, 16, etc.).
- Part 2: Calculate the total number of scratchcards including copies. Each match on a card causes you to win copies of subsequent cards.

**Source**: [ScratchcardsAOC2023Day4.java](day4/ScratchcardsAOC2023Day4.java)

**Algorithm/Approach**:
- Part 1: Parse each card to extract winning numbers and your numbers, count matches using a Set lookup, calculate points using 2^(matchCount-1) formula
- Part 2: Use an array to track copies of each card, iterate through cards and increment copy counts for subsequent cards based on match count

### Day 6: Wait For It
**Link**: [Day 6 - Wait For It](https://adventofcode.com/2023/day/6)

**Description**: 
- Part 1: Calculate the number of ways to beat boat race records across multiple races. The boat's speed increases by 1 mm/ms for each millisecond the button is held. Multiply the number of winning strategies for each race.
- Part 2: Treat all race times and distances as a single large race by removing spaces between numbers, then calculate ways to win.

**Source**: [WaitForItAOC2023Day6.java](day6/WaitForItAOC2023Day6.java)

**Algorithm/Approach**:
- Part 1: For each race, iterate through all possible hold times and count how many result in distances that beat the record
- Part 2: Parse numbers by concatenating digits (ignoring spaces), then apply the same algorithm to the single large race

### Day 9: Mirage Maintenance
**Link**: [Day 9 - Mirage Maintenance](https://adventofcode.com/2023/day/9)

**Description**:
- Part 1: Extrapolate the next value for each sequence using difference sequences until all zeroes, then sum the extrapolated values.

**Source**: [MirageMaintenanceAOC2023Day9.java](day9/MirageMaintenanceAOC2023Day9.java)

**Algorithm/Approach**:
- For each line, repeatedly compute difference sequences until all values are zero, then extrapolate the next value by summing up from the bottom difference sequence.

### Day 10: Pipe Maze
**Link**: [Day 10 - Pipe Maze](https://adventofcode.com/2023/day/10)

**Description**: 
- Part 1: Navigate through a two-dimensional grid of pipes to find a continuous loop. Determine the farthest point from the starting position 'S' along the loop.
- Part 2: Calculate how many tiles are enclosed within the pipe loop using scan-line algorithm.

**Source**: [PipeMazeAOC2023Day10.java](day10/PipeMazeAOC2023Day10.java)

**Algorithm/Approach**:
- Part 1: Breadth-First Search (BFS) to traverse the pipe loop and track distances from the starting position
- Part 2: Scan-line algorithm with ray-casting to determine which tiles are inside the loop boundary

### Day 13: Point of Incidence
**Link**: [Day 13 - Point of Incidence](https://adventofcode.com/2023/day/13)

**Description**: 
- Part 1: Find perfect lines of reflection (horizontal or vertical) in patterns of ash (.) and rocks (#). Calculate the sum of columns left of vertical reflections plus 100 times rows above horizontal reflections.
- Part 2: Find new lines of reflection after fixing exactly one smudge (one character difference) in each pattern.

**Source**: [PointOfIncidenceAOC2023Day13.java](day13/PointOfIncidenceAOC2023Day13.java)

**Algorithm/Approach**:
- Part 1: Test each possible reflection line by comparing mirrored rows/columns for exact matches
- Part 2: Find reflections with exactly one character difference, which represents the fixed smudge

### Day 21: Step Counter
**Link**: [Day 21 - Step Counter](https://adventofcode.com/2023/day/21)

**Description**: 
- Part 1: Determine which garden plots the Elf can reach with exactly 64 steps from the starting position 'S', navigating through garden plots (.) and avoiding rocks (#).
- Part 2: Calculate reachable plots on an infinitely repeating grid after 26,501,365 steps using quadratic extrapolation.

**Source**: [StepCounterAOC2023Day21.java](day21/StepCounterAOC2023Day21.java)

**Algorithm/Approach**:
- Part 1: Breadth-First Search (BFS) with parity tracking to count positions reachable in exactly 64 steps
- Part 2: Quadratic extrapolation using finite differences after sampling at strategic intervals (65, 196, 327 steps)

**Solutions**:
- Part 1: `3737`
- Part 2: `625382480005896`

---

[← Back to Main README](../../../../../../README.md)
