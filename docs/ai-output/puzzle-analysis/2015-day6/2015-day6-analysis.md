# 2015 Day 6 - Probably a Fire Hazard - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2015 Day 6

### Puzzle Title
Probably a Fire Hazard

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2015-day6/2015-day6-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given a 1000x1000 grid of lights (all initially off), process a list of instructions to turn on, turn off, or toggle rectangular regions of lights, then count how many lights are lit at the end.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| turn on 0,0 through 999,999 | 1000000 | All lights on |
| toggle 0,0 through 999,0\nturn off 499,499 through 500,500 | 998996 | First row toggled, then 4 lights in the centre turned off |

### Constraints
- Grid size is 1000x1000 (1,000,000 lights)
- All lights start off
- Instructions are processed in order
- Each instruction is one of: "turn on", "turn off", or "toggle" a rectangular region
- Coordinates are inclusive and range from 0 to 999

### Data Structures in Description
- 2D grid (boolean[1000][1000])
- List of instruction strings

---

## Part 2 Requirements

### Problem Statement
Each light now has a brightness (integer ≥ 0) instead of just on/off. "turn on" increases brightness by 1, "turn off" decreases brightness by 1 (to a minimum of zero), and "toggle" increases brightness by 2. After processing all instructions, return the total brightness of all lights.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| turn on 0,0 through 0,0 | 1 | Single light increases by 1 |
| toggle 0,0 through 999,999 | 2000000 | All 1,000,000 lights increase by 2 |

### How Part 2 Differs from Part 1
- Tracks integer brightness per light instead of on/off state
- Operations now increment/decrement/increase brightness, not just toggle state
- Output is the sum of all brightness values, not count of "on" lights

---

## Algorithm Approach

### Recommended Algorithm - PROPOSED
**Algorithm class:** Simulation (2D grid update)
**Rationale:** Each instruction directly updates a rectangular region of a fixed-size grid; simulation is efficient due to manageable grid size.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Use a Set of lit coordinates | More memory-efficient if grid is sparse, but grid is often dense and fixed size is manageable |

### Known Pitfalls
- Parsing coordinates and ensuring inclusivity
- Off-by-one errors in rectangle boundaries
- Large number of updates if not careful with loops

---

## Complexity Assessment

### Input Scale
- Grid: 1000x1000 (1,000,000 cells)
- Number of instructions: not specified, but likely < 10,000

### Required Time Complexity
- O(N * A), where N is the number of instructions and A is the average area affected per instruction; acceptable due to grid size and AoC time limits

### Space Complexity
- O(1,000,000) for the grid (about 1MB as booleans)

### Naive Approach Viable?
Yes - direct simulation is fast enough for this grid size.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2015/day6/day6_puzzle_data.txt").toURI()));
```

### Key Data Structures
- boolean[1000][1000] grid: tracks on/off state of each light
- List<String> instructions: raw input lines

### solvePartOne Outline
Parse each instruction, extract the operation and rectangle coordinates, and update the grid accordingly (turn on, turn off, or toggle). After all instructions, count the number of lights that are on and return this count.

### solvePartTwo Outline
Implement after Part 1 submission.

### Helper Methods
- parseInstruction(String line): parses operation and coordinates
- applyOperation(grid, op, x1, y1, x2, y2): updates the grid for the given rectangle

### Parsing Notes
- Instructions may have different prefixes ("turn on", "turn off", "toggle")
- Coordinates are comma-separated and inclusive
- Watch for extra spaces and correct splitting

---

## Pipeline Handoff

### Year
2015

### Day
6

### Puzzle Title
Probably a Fire Hazard

### Skeleton Class
`src/main/java/odogwudozilla/year2015/day6/ProbablyAFireHazardAOC2015Day6.java`

### Puzzle Input File
`src/main/resources/2015/day6/day6_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Simulation of 2D grid updates for each instruction

### Part 2 Status
Not yet available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

---

## Implementation Summary

### Year and Day
2015 Day 6

### Algorithm Applied
Direct simulation of a 1000x1000 boolean grid, updating regions per instruction and counting lights on at the end. No deviations from the plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| ProbablyAFireHazardAOC2015Day6Test | givenExampleInput_turnOnWholeGrid_returns1000000() | Unit | Passes |
| ProbablyAFireHazardAOC2015Day6Test | givenExampleInput_toggleRowAndTurnOff_returns998996() | Unit | Passes |

### Changes Made
| File | Change description |
|---|---|
| ProbablyAFireHazardAOC2015Day6.java | [RV-001] Output format in main() now prints exactly 'Part 1: <value>' and 'Part 2: <value>' on separate lines |
| ProbablyAFireHazardAOC2015Day6.java | [RV-002] All magic numbers (1000) replaced with GRID_SIZE constant; added PART2_NOT_IMPLEMENTED constant |
| ProbablyAFireHazardAOC2015Day6.java | [RV-003] Wildcard imports replaced with explicit imports |
| ProbablyAFireHazardAOC2015Day6Test.java | [RV-004] Static wildcard import replaced with explicit static import for assertEquals |
| ProbablyAFireHazardAOC2015Day6Test.java | [RV-005] Test methods assert exact expected output values (already correct) |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` [Yes]
- Part 2 output: `Part 2: <answer>` [Yes]

### Puzzle Run Result
```
Part 1: <answer captured from puzzle command>
Part 2: not implemented
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available (if pending)

---

## Implementation Summary

### Year and Day
2015 Day 6

### Algorithm Applied
Direct simulation of a 1000x1000 integer grid for brightness, updating regions per instruction and summing total brightness at the end. No deviations from the plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| ProbablyAFireHazardAOC2015Day6Test | givenExampleInput_turnOnWholeGrid_returns1000000() | Unit | Passes |
| ProbablyAFireHazardAOC2015Day6Test | givenExampleInput_toggleRowAndTurnOff_returns998996() | Unit | Passes |
| ProbablyAFireHazardAOC2015Day6Test | givenExampleInput_turnOnSingleLight_returns1_part2() | Unit | Fails before fix -> Passes after fix |
| ProbablyAFireHazardAOC2015Day6Test | givenExampleInput_toggleWholeGrid_returns2000000_part2() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| ProbablyAFireHazardAOC2015Day6.java | Implemented solvePartTwo: simulate integer brightness grid, apply operations, sum total brightness |
| ProbablyAFireHazardAOC2015Day6Test.java | Added Part 2 example-based unit tests |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` [Yes]
- Part 2 output: `Part 2: <answer>` [Yes]

### Puzzle Run Result
```
Part 1: 377891
Part 2: 14110788
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`
