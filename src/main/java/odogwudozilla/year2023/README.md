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

### Day 5: If You Give A Seed A Fertilizer
**Link**: [Day 5 - If You Give A Seed A Fertilizer](https://adventofcode.com/2023/day/5)

**Description**:
- Part 1: Find the lowest location number for any seed by mapping each seed through a series of category conversions using the Almanac.
- Part 2: Seeds are given as ranges; efficiently map all ranges through the Almanac to find the lowest possible location number.

**Source**: [IfYouGiveASeedAFertilizerAOC2023Day5.java](day5/IfYouGiveASeedAFertilizerAOC2023Day5.java)

**Algorithm/Approach**:
- Part 1: Parse all mapping tables and apply each to every seed, tracking the minimum location.
- Part 2: Treat seed input as intervals, propagate intervals through all mapping tables using interval splitting and merging for efficiency.

### Day 6: Wait For It
**Link**: [Day 6 - Wait For It](https://adventofcode.com/2023/day/6)

**Description**: 
- Part 1: Calculate the number of ways to beat boat race records across multiple races. The boat's speed increases by 1 mm/ms for each millisecond the button is held. Multiply the number of winning strategies for each race.
- Part 2: Treat all race times and distances as a single large race by removing spaces between numbers, then calculate ways to win.

**Source**: [WaitForItAOC2023Day6.java](day6/WaitForItAOC2023Day6.java)

**Algorithm/Approach**:
- Part 1: For each race, iterate through all possible hold times and count how many result in distances that beat the record
- Part 2: Parse numbers by concatenating digits (ignoring spaces), then apply the same algorithm to the single large race

### Day 7: Camel Cards
**Link**: [Day 7 - Camel Cards](https://adventofcode.com/2023/day/7)

**Description**:
- Part 1: Order hands by strength (five of a kind, four of a kind, full house, etc.), breaking ties by card order. Multiply each hand's bid by its rank and sum for total winnings.
- Part 2: J cards are jokers (wildcards, weakest card). Evaluate hands with jokers as wildcards for type, but break ties treating J as J. Multiply each hand's bid by its rank and sum for new total winnings.

**Source**: [CamelCardsAOC2023Day7.java](day7/CamelCardsAOC2023Day7.java)

**Algorithm/Approach**:
- Parse each hand and bid, sort hands by type and card order (with/without jokers), assign ranks, and sum bid * rank for all hands. For Part 2, treat J as wildcard for hand type, but as lowest card for tie-breaking.

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

### Day 17: Clumsy Crucible
**Link**: [Day 17 - Clumsy Crucible](https://adventofcode.com/2023/day/17)

**Description**:
- Part 1: Find the least heat loss path for a crucible from the lava pool (top-left) to the machine parts factory (bottom-right) on a city grid, moving at most three blocks straight before turning left or right (no reversing).
- Part 2: For the ultra crucible, each straight segment must be at least four and at most ten blocks before turning. Find the minimum heat loss path under these constraints.

**Source**: [ClumsyCrucibleAOC2023Day17.java](day17/ClumsyCrucibleAOC2023Day17.java)

**Algorithm/Approach**:
- Dijkstra's algorithm with direction and straight-move constraints to track position, direction, and consecutive straight moves. For Part 2, enforce the minimum and maximum straight segment rules.

### Day 20: Pulse Propagation
**Link**: [Day 20 - Pulse Propagation](https://adventofcode.com/2023/day/20)

**Description**:
- Simulate a network of modules (flip-flops, conjunctions, broadcaster) that communicate using high and low pulses. After pushing the button 1000 times, count the total low and high pulses sent and multiply these totals.

**Source**: [PulsePropagationAOC2023Day20.java](day20/PulsePropagationAOC2023Day20.java)

**Algorithm/Approach**:
- Parse the module network from input, simulate pulse propagation for each button press, and track pulse counts. Use a queue to process pulses in order and module classes to encapsulate behaviour.

### Day 21: Step Counter
**Link**: [Day 21 - Step Counter](https://adventofcode.com/2023/day/21)

**Description**: 
- Part 1: Determine which garden plots the Elf can reach with exactly 64 steps from the starting position 'S', navigating through garden plots (.) and avoiding rocks (#).
- Part 2: Calculate reachable plots on an infinitely repeating grid after 26,501,365 steps using quadratic extrapolation.

**Source**: [StepCounterAOC2023Day21.java](day21/StepCounterAOC2023Day21.java)

**Algorithm/Approach**:
- Part 1: Breadth-First Search (BFS) with parity tracking to count positions reachable in exactly 64 steps
- Part 2: Quadratic extrapolation using finite differences after sampling at strategic intervals (65, 196, 327 steps)

### Day 24: Never Tell Me The Odds
**Link**: [Day 24 - Never Tell Me The Odds](https://adventofcode.com/2023/day/24)

**Description**:
- Part 1: Count the number of pairs of hailstones whose paths intersect within the test area (ignoring the Z axis). Each hailstone moves in a straight line; check all pairs for intersection in the future and within the specified bounds.
- Part 2: Find the unique integer initial position (x, y, z) and velocity (vx, vy, vz) for a rock thrown at time 0 so that it collides with every hailstone in integer nanoseconds. Return the sum of the initial position coordinates.

**Source**: [NeverTellMeTheOddsAOC2023Day24.java](day24/NeverTellMeTheOddsAOC2023Day24.java)

**Algorithm/Approach**:
- Part 1: Parse each hailstone's position and velocity from input. For each pair, solve the linear equations for intersection in the XY plane. Count only those intersections that occur in the future and within the test area bounds.
- Part 2: Set up a system of six linear equations using three hailstones and their pairwise differences, eliminating the collision time variable. Solve for the rock's initial position and velocity using Gaussian elimination. Return the sum of the initial position coordinates.

---

[← Back to Main README](../../../../../../README.md)
