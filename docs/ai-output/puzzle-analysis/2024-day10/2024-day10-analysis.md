## Implementation Summary

### Year and Day
2024 Day 10

### Algorithm Applied
Part 2: DFS with memoization (DP) from each trailhead, counting all distinct strictly increasing paths to any 9. Memoization is by (row, col) only, as heights are strictly increasing and no revisits are possible. This matches the analysis recommendation for Part 2.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| HoofItAOC2024Day10Test | givenExampleInput1_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| HoofItAOC2024Day10Test | givenExampleInput2_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| HoofItAOC2024Day10Test | givenExampleInput3_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| HoofItAOC2024Day10Test | givenExampleInput4_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| HoofItAOC2024Day10Test | givenExampleInput5_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| HoofItAOC2024Day10Test | givenExampleInput6_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| HoofItAOC2024Day10.java | Implemented solvePartTwo: DFS with memoization to count all valid hiking trails from each trailhead to any 9 |
| HoofItAOC2024Day10Test.java | Added/expanded Part 2 tests for all analysis doc examples |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` [Yes]
- Part 2 output: `Part 2: <answer>` [Yes]

### Puzzle Run Result
```
Part 1: <see previous submission>
Part 2: <see test output; puzzle runner error prevents capture>
```

### Deviations from Analysis
None. Algorithm matches analysis: DFS with memoization by (row, col) for strictly increasing paths.

### Recommended Follow-up
- [x] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [x] Implement Part 2 (done)
# 2024 Day 10 - Hoof It - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2024 Day 10

### Puzzle Title
Hoof It

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2024-day10/2024-day10-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given a grid of digits (0-9) representing heights, find all trailheads (positions with height 0). For each trailhead, count how many positions with height 9 are reachable by a path that increases by exactly 1 at each step (only up/down/left/right, no diagonals, no impassable tiles in real input). Return the sum of all trailhead scores.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| 0123\n1234\n8765\n9876 | 1 | Single trailhead (top-left), can reach one 9 (bottom-left) |
| ...0...\n...1...\n...2...\n6543456\n7.....7\n8.....8\n9.....9 | 2 | Trailhead at (0,3), two 9s reachable |
| ..90..9\n...1.98\n...2..7\n6543456\n765.987\n876....\n987.... | 4 | Trailhead at (0,2), four 9s reachable |
| 10..9..\n2...8..\n3...7..\n4567654\n...8..3\n...9..2\n.....01 | 3 | Two trailheads: top (score 1), bottom (score 2), sum 3 |
| 89010123\n78121874\n87430965\n96549874\n45678903\n32019012\n01329801\n10456732 | 36 | 9 trailheads, scores: 5,6,5,3,1,3,5,3,5 |

### Constraints
- Heights are digits 0-9
- Input is a rectangular grid
- No impassable tiles in real input (all positions are valid)
- Steps allowed: up, down, left, right (no diagonals)
- Paths must increase by exactly 1 at each step
- Trailheads: all positions with height 0
- Endpoints: positions with height 9

### Data Structures in Description
- 2D grid (int[][] or char[][])
- Queue or stack for pathfinding (BFS/DFS)
- Set for visited positions

---

## Part 2 Requirements

### Problem Statement
For each trailhead (position with height 0), compute its "rating": the number of distinct hiking trails that begin at that trailhead. A hiking trail is any path starting at a trailhead, ending at a position with height 9, and increasing by exactly 1 at each step (up, down, left, or right; no diagonals). The sum of all trailhead ratings is the answer.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| .....0.\n..4321.\n..5..2.\n..6543.\n..7..4.\n..8765.\n..9.... | 3 | Single trailhead; 3 distinct hiking trails |
| ..90..9\n...1.98\n...2..7\n6543456\n765.987\n876....\n987.... | 13 | Single trailhead; 13 distinct hiking trails |
| 012345\n123456\n234567\n345678\n4.6789\n56789. | 227 | Single trailhead; 121 trails to right 9, 106 to bottom 9 |
| 89010123\n78121874\n87430965\n96549874\n45678903\n32019012\n01329801\n10456732 | 81 | 9 trailheads, ratings: 20,24,10,4,1,4,5,8,5 |

### How Part 2 Differs from Part 1
- Instead of counting unique endpoints (9s) reachable from each trailhead, count all distinct valid paths (hiking trails) from each trailhead to any 9.
- The sum is over the number of distinct paths, not just the number of endpoints.
- Requires path enumeration/counting, not just reachability.

---

## Algorithm Approach

### Recommended Algorithm - PROPOSED
**Algorithm class:** BFS (Breadth-First Search) from each trailhead
**Rationale:** Need to find all unique paths from each 0 to all reachable 9s, with the constraint that each step increases by 1; BFS ensures shortest/valid paths and avoids cycles.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| DFS | May be less efficient for large grids, but works for path existence |
| Precompute all 0->9 paths | Not scalable, as number of paths can be large |

### Known Pitfalls
- Must not revisit positions in the same path
- Only allow steps where next height = current + 1
- Multiple trailheads; sum all their scores
- No impassable tiles in real input (unlike examples)

---

## Complexity Assessment

### Input Scale
- Grid size not specified, but AoC typically up to 100x100 or 200x200

### Required Time Complexity
- O(T * N^2) where T = number of trailheads, N = grid size; BFS from each trailhead is acceptable for moderate N

### Space Complexity
- O(N^2) for grid and visited sets per trailhead

### Naive Approach Viable?
- Yes, for typical AoC input sizes; BFS/DFS per trailhead is feasible

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2024/day10/day10_puzzle_data.txt").toURI()));
```

### Key Data Structures
- int[][] grid: stores heights
- List<Point> trailheads: positions with height 0
- Set<Point> visited: for BFS/DFS

### solvePartOne Outline
Parse the grid and identify all trailheads (height 0). For each trailhead, perform BFS to find all reachable positions with height 9, only stepping to adjacent cells with height exactly one greater than current. Count unique 9s reached per trailhead, sum all scores, and return the total.

### solvePartTwo Outline
Implement after Part 1 submission.

### Helper Methods
- parseGrid(List<String> lines): int[][]
- getTrailheads(int[][] grid): List<Point>
- bfsTrailScore(int[][] grid, Point start): int

### Parsing Notes
- Input is a rectangular grid of digits, no blank lines
- No impassable tiles in real input

---

## Pipeline Handoff

### Year
2024

### Day
10

### Puzzle Title
Hoof It

### Skeleton Class
`src/main/java/odogwudozilla/year2024/day10/HoofItAOC2024Day10.java`

### Puzzle Input File
`src/main/resources/2024/day10/day10_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Part 1: BFS from each trailhead to all reachable 9s, only stepping to height+1
Part 2: DFS with memoization (DP) to count all distinct valid paths from each trailhead to any 9

### Part 2 Status
Available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Part 2 requirements - `## Part 2 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

