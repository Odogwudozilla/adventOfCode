# 2019 Day 17 - Set and Forget - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2019 Day 17

### Puzzle Title
Set and Forget

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @puzzle-analyser | Refreshed Part 2 analysis and implementation handoff sections |
| 5 | @solution-implementer | Updated ## Implementation Summary for Part 2 implementation |
| 6 | @solution-reviewer | Updated ## Review for Phase B Part 2 validation |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2019-day17/2019-day17-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Run the Intcode ASCII camera program to render the scaffold map, detect scaffold intersections, compute each alignment parameter (`x * y`), and return the sum.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| ```
..#..........
..#..........
#######...###
#.#...#...#.#
#############
..#...#...#..
..#####...^..
``` | 76 | Intersections at `(2,2)`, `(2,4)`, `(6,4)`, `(10,4)` with parameters `4,8,24,40`. |

### Constraints
- ASCII output: `35 -> #`, `46 -> .`, `10 -> newline`.
- Robot symbols (`^`, `v`, `<`, `>`) indicate robot orientation on scaffold.
- Intersections require scaffold at centre plus all four orthogonal neighbours.
- Puzzle input is Intcode program text; map must be obtained by execution.

### Data Structures in Description
- 2D character grid.
- Coordinate points (`x`, `y`).
- Intcode output stream interpreted as ASCII.

---

## Part 2 Requirements

### Problem Statement
Wake the robot by changing Intcode address `0` from `1` to `2`, provide a valid movement script (main routine plus functions `A/B/C`) that visits all scaffold, and return the final non-ASCII dust amount output.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| ```
#######...#####
#.....#...#...#
#.....#...#...#
......#...#...#
......#...###.#
......#.....#.#
^########...#.#
......#.#...#.#
......#########
........#...#..
....#########..
....#...#......
....#...#......
....#...#......
....#####......
``` | `R,8,R,8,R,4,R,4,R,8,L,6,L,2,R,4,R,4,R,8,R,8,R,8,L,6,L,2` | Example full route covering scaffold. |
| Full route above | Main routine `A,B,C,B,A,C` with `A=R,8,R,8`, `B=R,4,R,4,R,8`, `C=L,6,L,2` | Valid function decomposition shown in puzzle text. |
| Main routine + function lines + `n`/`y` video line | Final output is one large non-ASCII integer | This final value is the Part 2 answer (no sample numeric value given). |

### How Part 2 Differs from Part 1
Part 2 changes from analysing a static map to controlling the robot: generate a complete route, compress it under strict routine length limits, feed ASCII commands to Intcode input, and read the dust report.

---

## Algorithm Approach

### Recommended Algorithm - **PROPOSED**
**Algorithm class:** Simulation + deterministic path construction + constrained backtracking.
**Rationale:** Intcode still drives map/rendering, then path traversal is deterministic on scaffold, while routine compression is a small bounded search over token prefixes constrained by the 20-character rule.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Manual hard-coded routines | Quick for one input but brittle and not reusable. |
| Unpruned brute force routine search | Can become combinatorially expensive. |
| Full graph-route optimisation | Unnecessary complexity for this puzzle structure. |

### Known Pitfalls
- Forgetting to set memory address `0` to `2` for movement mode.
- Not treating robot glyphs as scaffold during traversal/path generation.
- Exceeding 20-character maximum (excluding newline) for main routine or any function.
- Feeding commands without newline terminators.
- Returning ASCII frame output instead of the final large non-ASCII value.

---

## Complexity Assessment

### Input Scale
- Intcode program: typically a few thousand values.
- Rendered map: usually tens-by-tens cells.
- Movement token list: usually dozens of tokens.

### Required Time Complexity
`O(P + W*H + S)` where `P` is Intcode runtime, `W*H` is grid processing, and `S` is bounded routine-search work; this is suitable for AoC limits.

### Space Complexity
`O(W*H + T + M)` for map, movement tokens/routines, and Intcode memory.

### Naive Approach Viable?
Part 1 yes; Part 2 needs bounded/pruned decomposition rather than naive unbounded brute force.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2019/day17/day17_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `List<Long>` for Intcode program and outputs.
- `Map<Long, Long>` (or expandable array) for Intcode memory.
- `List<String>` / `char[][]` for camera map.
- `record Point(int x, int y)` and direction enum/int.
- `List<String>` for movement tokens (`L`, `R`, and numbers).
- `Map<Character, List<String>>` for function bodies (`A`, `B`, `C`).

### solvePartOne Outline
Run Intcode with the original program to collect ASCII camera output. Build the map rows, scan interior cells, and detect intersections via four-neighbour scaffold checks. Sum `x * y` across intersections and return as string.

### solvePartTwo Outline
Run Intcode (view mode) to reconstruct the scaffold and robot start, then generate the full movement token path that walks scaffold coverage. Compress tokens into up to three reusable functions and a main routine using constrained backtracking with 20-character checks. Re-run Intcode in movement mode (address `0 = 2`), feed main routine/functions/video choice as ASCII lines, and return the last output value greater than `255`.

### Helper Methods
- `parseProgram(String line)` - parse comma-separated integers.
- `buildGrid(List<Long> output)` - convert ASCII codes to rows.
- `findRobot(List<String> grid)` - locate start position and heading.
- `buildFullPathTokens(List<String> grid, RobotState start)` - derive movement sequence.
- `compressToRoutines(List<String> tokens)` - produce main routine and `A/B/C` definitions.
- `encodeAsciiScript(CompressionPlan plan, boolean video)` - build newline-terminated ASCII input.
- `runIntcodeWithInput(List<Long> program, List<Long> input)` - execute interactive VM and capture outputs.

### Parsing Notes
- Input file is Intcode CSV, not literal map rows.
- Keep newline (`10`) boundaries exact when reconstructing map/script.
- Character limits include commas and digits/letters, but not trailing newline.
- Final answer for Part 2 is the final non-ASCII output value.

---

## Implementation Summary

### Year and Day
2019 Day 17

### Algorithm Applied
Simulation + deterministic path construction + constrained backtracking compression (`A/B/C`) + movement-mode Intcode execution with ASCII script input.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| SetAndForgetAOC2019Day17Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| SetAndForgetAOC2019Day17Test | givenPartTwoExampleMap_buildFullPathTokens_returnsExpectedRouteTokens() | Unit | Fails before fix -> Passes after fix |
| SetAndForgetAOC2019Day17Test | givenPartTwoExampleRoute_compressToRoutines_returnsExpectedPlan() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| SetAndForgetAOC2019Day17.java | Preserved `solvePartOne` behaviour |
| SetAndForgetAOC2019Day17.java | Implemented `solvePartTwo`: map reconstruction, robot path token generation, routine compression, script encoding, wake-up execution, dust extraction |
| SetAndForgetAOC2019Day17.java | Extended Intcode runtime to support interactive input for Part 2 |
| SetAndForgetAOC2019Day17Test.java | Added example-based Part 2 unit tests for route generation and routine compression |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```
Part 1: 7404
Part 2: 929045
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [x] Part 1 submitted.
- [x] Implement Part 2 routine compression and dust collection execution.
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`.

---

## Review

### Review Cycle
1 (Phase B)

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| - | - | - | - | No issues found in reviewed Part 2 scope (`solvePartTwo` and related tests). |

### Fix Packets
None.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review (Part 1 scope) | 0 issues queued | - |
| 1 (Phase B) | Part 2 implementation review | 0 issues queued | 0 |

### Commit Message
Add AOC 2019 Day 17 solution: Set and Forget - scaffold path compression with Intcode movement execution

This implementation runs the Intcode camera program to build the scaffold map, derives the full robot movement token path, and compresses it into valid `A/B/C` routines via bounded recursive search under the 20-character constraints. It then re-runs Intcode in wake-up mode with encoded ASCII movement input and returns the final non-ASCII dust output, while preserving the required `Part 1:`/`Part 2:` console format.

---

## Pipeline Handoff

### Year
2019

### Day
17

### Puzzle Title
Set and Forget

### Skeleton Class
`src/main/java/odogwudozilla/year2019/day17/SetAndForgetAOC2019Day17.java`

### Puzzle Input File
`src/main/resources/2019/day17/day17_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Run Intcode to reconstruct scaffold, generate full movement tokens, compress into bounded `A/B/C` routines via pruned backtracking, then execute wake-up mode with ASCII input and read final dust output.

### Part 2 Status
Available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Part 2 requirements - `## Part 2 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`



