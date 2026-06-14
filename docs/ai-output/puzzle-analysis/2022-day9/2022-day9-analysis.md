## Run Metadata
- Year: 2022
- Day: 9
- Puzzle: Rope Bridge
- Source: https://adventofcode.com/2022/day/9
- Workflow Stage: Auto-solve submission support

## Puzzle Summary
Simulate rope movement on a 2D grid from step-by-step motion instructions.
Part 1 tracks a rope with two knots (head and tail).
Part 2 tracks a rope with ten knots and counts unique positions visited by the last knot.

## Algorithm Approach
Use arrays for knot coordinates and apply each movement one step at a time.
After moving the head, update each following knot so it moves at most one step per axis towards the previous knot whenever they are no longer touching.
Store visited tail positions in a set and return the set size.

## Review
- The implementation reads input from `src/main/resources/2022/day9/day9_puzzle_data.txt` and does not hardcode puzzle data.
- Direction handling validates unexpected input by throwing an exception.
- Sample-based tests are provided in `src/test/java/odogwudozilla/year2022/day9/RopeBridgeAOC2022Day9Test.java`.

## Pipeline Handoff
- Year: 2022
- Day: 9
- Recommended Output: committed puzzle artefacts for Day 9
- Constraints: preserve auto-generated documentation updates and commit only intended Day 9 artefacts

