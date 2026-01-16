# Advent of Code 2023

This directory contains solutions for [Advent of Code 2023](https://adventofcode.com/2023).

## Implemented Puzzles

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

