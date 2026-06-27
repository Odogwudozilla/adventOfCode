# 2015 Day 9 - All in a Single Night - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2015 Day 9

### Puzzle Title
All in a Single Night

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-implementer | ## Implementation Summary (Part 2) |
| 4 | @solution-reviewer | ## Review |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2015-day9/2015-day9-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given pairwise distances between locations, find the shortest possible route that visits every location exactly once. The route may start and finish at any two different locations.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| ```London to Dublin = 464\nLondon to Belfast = 518\nDublin to Belfast = 141``` | `605` | The description enumerates all six permutations and confirms the minimum route is `London -> Dublin -> Belfast = 605`. |

### Constraints
- Distances are provided between each pair of locations (an undirected, weighted complete graph in practice).
- Each location must be visited exactly once.
- Start and end locations are unrestricted, but must be different.
- Objective is the minimum total route length.

### Data Structures in Description
- Weighted graph (locations as nodes, distances as undirected edges).
- Route ordering/permutation of locations.

---

## Part 2 Requirements

### Problem Statement
Find the longest possible route that visits every location exactly once. The route may start and finish at any two different locations.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| ```London to Dublin = 464\nLondon to Belfast = 518\nDublin to Belfast = 141``` | `982` | The longest route is via Dublin -> London -> Belfast. |

### How Part 2 Differs from Part 1
Instead of finding the shortest route, the problem asks for the longest route. The same permutation-based search can be reused by simply tracking the maximum distance rather than the minimum.

---

## Algorithm Approach

### Recommended Algorithm - PROPOSED
**Algorithm class:** Graph traversal via permutation-based brute force (Travelling Salesperson path variant).
**Rationale:** Part 1 asks for a Hamiltonian path minimum over a small set of cities, so evaluating all city orders and taking the minimum total distance is direct, reliable, and simple.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Held-Karp dynamic programming (`O(N^2 * 2^N)`) | More scalable for larger `N`, but unnecessary complexity for typical AoC Day 9 input sizes. |
| Branch-and-bound over permutations | Can prune search, but introduces extra implementation complexity and little benefit at small `N`. |

### Known Pitfalls
- Parse lines carefully with the format `A to B = D` (spaces are significant separators).
- Distances are effectively bidirectional; store both directions or normalise lookups.
- Ensure route cost sums only adjacent pairs in the permutation.
- Avoid assuming a fixed starting city; all permutations are candidates.

---

## Complexity Assessment

### Input Scale
AoC 2015 Day 9 typically has a small city count (commonly around 8 locations, inferred from standard puzzle scale), with one line per pairwise distance.

### Required Time Complexity
`O(N! * N)` is acceptable for Part 1 at this scale, as factorial growth remains tractable for single-digit `N`.

### Space Complexity
`O(N^2)` for distance storage plus `O(N)` for the current permutation/route accumulator.

### Naive Approach Viable?
Yes. A full permutation search is viable for the expected input size and should run comfortably within AoC timing expectations.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2015/day9/day9_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `Map<String, Integer> cityIndex` - maps city names to compact integer indices.
- `int[][] dist` - adjacency matrix of pairwise distances.
- `int[] order` / `List<Integer>` - current city permutation for route evaluation.
- `int best` - running minimum route distance.

### solvePartOne Outline
Parse each input line into `(from, to, distance)`, assigning indices for unseen cities. Build an undirected adjacency matrix (or nested map) for constant-time edge lookup. Generate all permutations of city indices and compute each route total by summing adjacent edge distances. Track and return the minimum route total as a string.

### solvePartTwo Outline
Re-use the permutation logic from Part 1. Traverse all permutations of the city indices, compute the distance for each permutation, and track the maximum route total instead of the minimum. Return the maximum route total as a string.

### Helper Methods
- `parseEdge(String line)` - splits one line into source, destination, and distance.
- `permuteAndScore(...)` - recursively or iteratively enumerates city orders and updates minimum.
- `routeDistance(int[] order, int[][] dist)` - sums path length for one permutation.

### Parsing Notes
- Ignore blank lines if present.
- City names in this puzzle are single tokens in the example, but parsing should rely on delimiters (`" to "`, `" = "`) rather than token count assumptions.

---

## Pipeline Handoff

### Year
2015

### Day
9

### Puzzle Title
All in a Single Night

### Skeleton Class
`src/main/java/odogwudozilla/year2015/day9/AllInASingleNightAOC2015Day9.java`

### Puzzle Input File
`src/main/resources/2015/day9/day9_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Enumerate all permutations of locations, compute each route length from an undirected distance matrix, and return the minimum total distance.

### Part 2 Status
Available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

## Implementation Summary

### Year and Day
2015 Day 9

### Algorithm Applied
Enumerated all permutations of city visit order, scored each route from an undirected adjacency matrix, and returned the minimum total distance. No deviation from the analysed plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| AllInASingleNightAOC2015Day9Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| AllInASingleNightAOC2015Day9.java | Implemented `solvePartOne` using parsed edges, adjacency matrix, and full permutation scoring for shortest route |
| AllInASingleNightAOC2015Day9.java | Kept `solvePartTwo` as stub returning `not implemented` (pipeline-compatible Part 2 pending) |
| AllInASingleNightAOC2015Day9Test.java | Created example-based Part 1 unit test from puzzle description |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```text
Part 1: 251
Part 2: not implemented
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available

## Implementation Summary (Part 2)

### Year and Day
2015 Day 9

### Algorithm Applied
Reused the permutation-based brute force algorithm from Part 1. Extended to optionally track the maximum route length instead of the minimum. Both minimum and maximum route conditions evaluate the same permutations using a unified `solve` and `permuteAndScore` method.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| AllInASingleNightAOC2015Day9Test | givenExampleInput_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| AllInASingleNightAOC2015Day9.java | Implemented `solvePartTwo` by reusing logic in a generic `solve(..., boolean findMax)` method. |
| AllInASingleNightAOC2015Day9Test.java | Added unit test for Part 2 with the example target length of 982. |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```
Part 1: 251
Part 2: 898
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`

---

## Review

### Review Cycle
1

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| None | - | - | - | No issues found in reviewed scope. |

### Fix Packets
No fixes required in this cycle.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | 0 issues queued | - |

### Commit Message
Add AOC 2015 Day 9 solution: All in a Single Night - brute-force permutation search for shortest route

The Part 1 solution parses pairwise distances into an undirected adjacency matrix and evaluates every city permutation to compute route totals, selecting the minimum Hamiltonian path length. This approach is appropriate for the small AoC input size, keeps the implementation clear, reads input from the classpath resource, and preserves the required output format for both parts.

## Review (Part 2)

### Review Cycle
2

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| None | - | - | - | No issues found in reviewed scope. |

### Fix Packets
No fixes required in this cycle.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 2 | Part 2 review | 0 issues queued | - |

### Commit Message
Add AOC 2015 Day 9 solution: All in a Single Night - brute-force permutation search for longest route

The Part 2 solution extends the original permutation-based brute force approach to also evaluate and track the maximum route length. The unified search algorithm correctly calculates both the shortest and longest Hamiltonian paths across the undirected graph by traversing every possible permutation of the relatively small set of cities.
