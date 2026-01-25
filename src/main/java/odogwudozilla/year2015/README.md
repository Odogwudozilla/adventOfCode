# Advent of Code 2015 Solutions

[← Back to Main README](../../../../../README.md)

## Year Overview

Advent of Code 2015 was the inaugural year of this annual coding challenge. These classic puzzles set the foundation for all future years.

---

## Implemented Puzzles

### Day 1: Not Quite Lisp
**Link**: [https://adventofcode.com/2015/day/1](https://adventofcode.com/2015/day/1)

**Description**: Navigate through floors using parentheses instructions to find Santa's destination.

**Source Code**: [NotQuiteLispAOC2015Day1.java](day1/)

---

### Day 2: I Was Told There Would Be No Math
**Link**: [https://adventofcode.com/2015/day/2](https://adventofcode.com/2015/day/2)

**Description**: Calculate wrapping paper and ribbon needed for presents based on box dimensions.

**Source Code**: [IWasToldThereWouldBeNoMathAOC2015Day2.java](day2/)

---

### Day 3: Perfectly Spherical Houses in a Vacuum
**Link**: [https://adventofcode.com/2015/day/3](https://adventofcode.com/2015/day/3)

**Description**: Santa delivers presents to houses on an infinite two-dimensional grid, moving according to instructions. The goal is to determine how many houses receive at least one present. Part 2 introduces Robo-Santa, who alternates moves with Santa.

**Source Code**: [PerfectlySphericalHousesAOC2015Day3.java](day3/)

**Algorithm/Approach**: Use a HashSet to track unique house coordinates. For Part 1, iterate through the directions and update Santa's position. For Part 2, alternate moves between Santa and Robo-Santa, updating their positions and the set of visited houses.

---

### Day 10: Elves Look, Elves Say
**Link**: [https://adventofcode.com/2015/day/10](https://adventofcode.com/2015/day/10)

**Description**: Apply the look-and-say sequence transformation (also known as the Morris number sequence) to a string of digits. Each iteration describes the previous result by counting consecutive runs of digits.

**Source Code**: [ElvesLookElvesSayAOC2015Day10.java](day10/)

**Algorithm/Approach**: Iterate through the string counting consecutive occurrences of each digit, then build the result by appending the count followed by the digit. Part 1 applies this 40 times, Part 2 applies it 50 times. The result grows exponentially with each iteration.

---

### Day 15: Science for Hungry People
**Link**: [https://adventofcode.com/2015/day/15](https://adventofcode.com/2015/day/15)

**Description**: Optimise a cookie recipe by finding the best balance of ingredients that maximises the score based on capacity, durability, flavour, and texture.

**Source Code**: [ScienceForHungryPeopleAOC2015Day15.java](day15/)

**Algorithm/Approach**: Recursive brute-force search through all possible ingredient combinations (100 teaspoons total), evaluating each combination's score. Part 2 adds a constraint: only combinations totalling exactly 500 calories are considered.

---

[← Back to Main README](../../../../../README.md) | [View All Years](../../../../../README.md#-years)

