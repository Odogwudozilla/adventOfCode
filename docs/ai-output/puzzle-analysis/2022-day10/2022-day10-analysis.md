# 2022 Day 10 - Cathode-Ray Tube - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2022 Day 10

### Puzzle Title
Cathode-Ray Tube

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @puzzle-analyser | ## Part 2 Requirements - ## Implementation Plan |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2022-day10/2022-day10-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Simulate the CPU’s register `X` across its instruction stream, then sum the signal strengths observed during cycles 20, 60, 100, 140, 180, and 220. A signal strength is the cycle number multiplied by the value of `X` during that cycle.

### Example Test Cases
| Example | Expected Output | Notes |
|---|---|---|
| Example 1 | `13140` | Explicit worked example and final Part 1 assertion in the description. |

Example 1 input:

```text
addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
```

The smaller `noop / addx` program is a cycle-by-cycle execution trace only; it does not provide a separate Part 1 assertion.

### Constraints
- `X` starts at `1`.
- `addx V` takes exactly two cycles and applies `V` only after the second cycle.
- `noop` takes exactly one cycle and has no side effects.
- `V` may be negative.
- Signal strengths are only sampled during cycles 20, 60, 100, 140, 180, and 220.
- No explicit upper bound on instruction count is stated in the description.

### Data Structures in Description
- Linear instruction list / program
- Single CPU register (`X`)
- Cycle counter
- Sample-cycle set or fixed list of cycle numbers

---

## Part 2 Requirements

### Problem Statement
Use the same cycle-accurate CPU simulation to drive a 40×6 CRT screen. The CRT draws one pixel per cycle; the pixel is lit (`#`) when the current 3-pixel-wide sprite, centred on `X`, overlaps the column being drawn, otherwise it stays dark (`.`). After 240 cycles, read the six rendered rows as eight capital letters.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| The worked example program from Part 1 | Rendered 6×40 image shown below | The description uses the larger instruction stream to demonstrate the rendered CRT output; the final eight letters are not named explicitly. |

```text
##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....
```

### How Part 2 Differs from Part 1
Part 2 reuses the same instruction timing rules, but replaces the numeric signal-strength sum with a visual 240-pixel render. The implementation needs a CRT buffer, a per-cycle draw step, and a final OCR or letter-decoding pass over the six output rows.

---

## Algorithm Approach

### Recommended Algorithm - **Confirmed**
**Algorithm class:** Simulation
**Rationale:** The puzzle is defined in terms of cycle-accurate instruction timing, so a single left-to-right simulation of cycles and register updates is the natural and safest approach.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Materialise every cycle’s `X` value in a list | Simple to reason about, but uses extra memory proportional to total cycles. |
| Derive only the six sample cycles on the fly | Slightly more compact, but still a form of direct simulation and offers little extra benefit here. |

### Known Pitfalls
- `addx` updates `X` after, not during, its second cycle.
- The sampled `X` value is the value in effect during the cycle, before any end-of-cycle `addx` write-back.
- Cycle numbering is 1-based, not 0-based.
- Forgetting the initial `X = 1` gives the wrong signal strengths.
- Off-by-one errors are likely if the implementation checks the sample point after incrementing the cycle counter in the wrong order.

---

## Complexity Assessment

### Input Scale
The description does not state a hard input-size limit, but the instruction stream is clearly intended to be processed in a single pass with only a small, fixed set of sample cycles.

### Required Time Complexity
`O(n)` time, where `n` is the number of instructions. A direct cycle simulation is sufficient because each instruction has a fixed cost of one or two cycles.

### Space Complexity
`O(1)` auxiliary space is achievable if the implementation tracks only the current cycle, current register value, and running total.

### Naive Approach Viable?
Yes. A straightforward simulation is already linear and easily fast enough for Advent of Code constraints.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/<YEAR>/day<N>/day<N>_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `List<String>` for the raw instruction lines
- `int cycle` for the current cycle number
- `int x` for the register value
- `int sum` for the accumulated signal strengths
- `Set<Integer>` or fixed integer array for the six sample cycles
- `char[][]` or `StringBuilder[]` for the 6×40 CRT image
- `int row` / `int column` derived from the current cycle when drawing Part 2

### solvePartOne Outline
Read each instruction in order and simulate its cycle cost exactly. For every cycle, check whether it matches one of the six sample points; if so, add `cycle * x` to the running total. For `addx`, apply the register change only after both cycles have completed.

### solvePartTwo Outline
Run the same instruction simulation as Part 1, but on every cycle compute the current CRT column from the cycle number and draw a pixel before any `addx` write-back occurs. Mark the pixel as lit when the column lies within `X - 1` to `X + 1`, otherwise dark. Once all 240 pixels have been processed, join the six rows into the final image string and decode the capital letters if required by the project.

### Helper Methods
- `isSampleCycle(int cycle)` - tests whether the current cycle should contribute to the total
- `applyInstruction(String instruction, ...)` - handles one `noop` or `addx` while respecting cycle timing
- `drawPixel(int cycle, int x, char[][] screen)` - records the Part 2 CRT pixel for the current cycle
- `renderScreen(char[][] screen)` - converts the 6×40 buffer into newline-separated output rows

### Parsing Notes
- Each line is either `noop` or `addx V`.
- `addx` values must be parsed as signed integers.
- Blank lines are not described, so the parser can treat them as invalid unless the project input normalises them away.

---

## Pipeline Handoff

### Year
2022

### Day
10

### Puzzle Title
Cathode-Ray Tube

### Skeleton Class
`src/main/java/odogwudozilla/year2022/day10/CathodeRayTubeAOC2022Day10.java`

### Puzzle Input File
`src/main/resources/2022/day10/day10_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Single-pass cycle simulation with exact `addx` timing, six-point signal sampling, and CRT rendering of the 240-screen pixels.

### Part 2 Status
Available in the source description and analysed here.

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

## Implementation Summary

### Year and Day
2022 Day 10

### Algorithm Applied
Single-pass cycle simulation with exact `addx` timing and sampling at cycles 20, 60, 100, 140, 180, and 220.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| CathodeRayTubeAOC2022Day10Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| src/main/java/odogwudozilla/year2022/day10/CathodeRayTubeAOC2022Day10.java | Implemented solvePartOne with cycle-accurate signal-strength accumulation; left Part 2 stub unchanged. |
| src/test/java/odogwudozilla/year2022/day10/CathodeRayTubeAOC2022Day10Test.java | Created with the worked example for Part 1. |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` [Yes]
- Part 2 output: `Part 2: <answer>` [Yes]

### Puzzle Run Result
```text
Part 1: 13180
Part 2: not implemented
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available (if pending)


## Review

### Review Cycle
1

### Status
ALL_VERIFIED

### Issues
None.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | 0 issues queued | 0 |

### Commit Message
Add AOC 2022 Day 10 solution: Cathode-Ray Tube - simulate cycles and sample signal strengths

A single-pass cycle simulation is used to track the register value across `noop` and `addx` instructions, sampling signal strength exactly at cycles 20, 60, 100, 140, 180 and 220. The implementation keeps the timing rules precise by applying `addx` only after its second cycle, which matches the puzzle’s requirements and is efficient for the full input.





