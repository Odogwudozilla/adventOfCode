# 2019 Day 15 - Oxygen System - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2019 Day 15

### Puzzle Title
Oxygen System

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2019-day15/2019-day15-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given an unknown maze controlled by an Intcode program, use movement commands to remotely control a repair droid. Find the minimum number of movement commands required to reach the oxygen system from the starting position, using only feedback from the droid (wall, moved, or found oxygen system).

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| (Intcode program, see description) | 2 | Example in description: oxygen system is 2 moves away |

### Constraints
- The map is unknown and must be explored by interacting with the Intcode program.
- Only four movement commands: north (1), south (2), west (3), east (4).
- Droid reports: 0 (wall), 1 (moved), 2 (moved and found oxygen system).
- The droid starts at a fixed position; the oxygen system is somewhere in the maze.
- The maze may contain dead ends and cycles.

### Data Structures in Description
- Grid (implicit, as a map of explored positions)
- Queue (for BFS traversal)
- Map/Set (to track visited positions)

---

## Part 2 Requirements

### Problem Statement
Not yet available.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|

### How Part 2 Differs from Part 1
Not yet available - will be scraped after Part 1 is submitted.

---

## Algorithm Approach

### Recommended Algorithm - PROPOSED
**Algorithm class:** Breadth-First Search (BFS) with simulation
**Rationale:** The shortest path in an unknown maze is best found by BFS, and the droid's movement/status feedback allows simulation of exploration.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| DFS | May not find shortest path; more complex backtracking |
| Random walk | Inefficient, not guaranteed to find shortest path |

### Known Pitfalls
- Correctly simulating the Intcode program for each movement (may need to clone state for BFS branches)
- Avoiding revisiting positions (track visited states)
- Handling the Intcode program's stateful nature (may require copying program state per BFS node)
- Parsing the Intcode program and movement feedback

---

## Complexity Assessment

### Input Scale
- The maze size is not specified, but AoC mazes are typically up to ~100x100.
- The Intcode program input is a single line of comma-separated integers.

### Required Time Complexity
- O(N) where N is the number of reachable positions; BFS ensures shortest path and is efficient for this scale.

### Space Complexity
- O(N) for storing visited positions and program states per BFS node.

### Naive Approach Viable?
- No; naive DFS or random walk may be too slow or not guarantee shortest path. BFS with program state tracking is required.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2019/day15/day15_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `Map<Point, TileType>`: to represent the explored maze
- `Queue<State>`: for BFS, where State includes position and Intcode program state
- `Set<Point>`: to track visited positions
- `IntcodeComputer` class: to simulate the droid's program

### solvePartOne Outline
Parse the Intcode program from input. Use BFS to explore the maze, simulating the droid's movement by running the Intcode program for each possible move. For each BFS node, track the droid's position and a copy of the Intcode program state. When the oxygen system is found (status 2), return the number of steps taken.

### solvePartTwo Outline
Implement after Part 1 submission.

### Helper Methods
- `parseIntcode(String line)`: parse the Intcode program
- `move(State, direction)`: simulate a move and return new State
- `isOxygen(State)`: check if current state is at the oxygen system

### Parsing Notes
- Input is a single line of comma-separated integers (the Intcode program)
- No blank lines or mixed formats

---

## Pipeline Handoff

### Year
2019

### Day
15

### Puzzle Title
Oxygen System

### Skeleton Class
`src/main/java/odogwudozilla/year2019/day15/OxygenSystemAOC2019Day15.java`

### Puzzle Input File
`src/main/resources/2019/day15/day15_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
BFS with Intcode simulation per node to find shortest path to oxygen system

### Part 2 Status
Not yet available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

---

## Implementation Summary

### Year and Day
2019 Day 15

### Algorithm Applied
Breadth-First Search (BFS) with Intcode simulation per node, as described in the analysis. The Intcode computer is implemented as an inner class and cloned for each BFS branch. No deviations from the plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| OxygenSystemAOC2019Day15Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| OxygenSystemAOC2019Day15.java | Implemented Intcode computer, BFS maze exploration for Part 1 and Part 2, and all required helpers. |
| OxygenSystemAOC2019Day15Test.java | Created with example-based unit test for Part 1. |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```
Part 1: 234
Part 2: 292
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [x] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [x] Implement Part 2 (done)
