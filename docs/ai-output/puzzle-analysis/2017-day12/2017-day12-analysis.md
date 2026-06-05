# 2017 Day 12 - Digital Plumber - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2017 Day 12

### Puzzle Title
Digital Plumber

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @solution-implementer | ## Implementation Summary (Resume Update) |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2017-day12/2017-day12-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
The input describes a bidirectional communication network between programs, where each line lists one program ID and all directly connected program IDs. Part 1 asks for the total number of programs in the connected component that contains program ID `0`.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| ```text
0 <-> 2
1 <-> 1
2 <-> 0, 3, 4
3 <-> 2, 4
4 <-> 2, 3, 6
5 <-> 6
6 <-> 4, 5
``` | `6` | Programs reachable from `0` are `{0,2,3,4,5,6}`; program `1` is isolated except for a self-loop. |

### Constraints
- Each program can directly communicate with one or more listed programs.
- Pipes are bidirectional, so connections are undirected.
- IDs are integers in the input text format (`A <-> B, C, ...`).
- The required output is the size of the group containing program ID `0`.

### Data Structures in Description
- Undirected graph (program IDs as nodes, pipes as edges).
- Adjacency list style input representation per line.

---

## Part 2 Requirements

### Problem Statement
Not analysed by request (Part 1 only).

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| N/A | N/A | Part 2 intentionally excluded. |

### How Part 2 Differs from Part 1
N/A (Part 1 only analysis requested).

---

## Algorithm Approach

### Recommended Algorithm - CONFIRMED
**Algorithm class:** Graph traversal (BFS or DFS)
**Rationale:** Part 1 is exactly a connected-component reachability count from a fixed start node (`0`), which is directly solved by one traversal over the undirected graph.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Union-Find (Disjoint Set Union) | Also valid for connected components, but slightly more setup than a single BFS/DFS when only one component size is required. |
| Repeated transitive expansion with list scanning | Simpler conceptually but less efficient due to repeated full input passes. |

### Known Pitfalls
- Parsing the delimiter sequence correctly (`" <-> "` then `", "`).
- Ensuring undirected semantics are respected (input appears symmetric but logic should not rely on perfect symmetry).
- Counting unique visited nodes only once, especially with self-loops like `1 <-> 1`.

---

## Complexity Assessment

### Input Scale
The description does not give explicit bounds; typical AoC input is a few hundred to a few thousand lines/nodes.

### Required Time Complexity
`O(V + E)` is appropriate and safely within AoC runtime expectations (~1 second per part).

### Space Complexity
`O(V + E)` for adjacency storage plus `O(V)` for visited tracking and traversal queue/stack.

### Naive Approach Viable?
Partially. A naive repeated-scan reachability method may pass on small inputs but is unnecessary and can degrade towards `O(V * E)`; a single BFS/DFS is the robust approach.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2017/day12/day12_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `Map<Integer, List<Integer>> graph` - adjacency list of program connections.
- `Set<Integer> visited` - tracks programs already reached from `0`.
- `ArrayDeque<Integer>` (BFS) or stack equivalent (DFS) - traversal frontier.

### solvePartOne Outline
Parse each line into `source` and neighbour IDs, populate an adjacency list for the undirected graph, then traverse from node `0` using BFS or DFS. Mark each reached node in `visited` and expand through all neighbours. Return `visited.size()` as a string.

### solvePartTwo Outline
Not in scope for this analysis (Part 1 only).

### Helper Methods
- `parseGraph(List<String> input)` - converts text lines into adjacency list.
- `countReachableFromZero(Map<Integer, List<Integer>> graph)` - BFS/DFS traversal and size count.
- `parseNeighbours(String rhs)` - parses comma-separated neighbour IDs.

### Parsing Notes
- Trim whitespace around tokens when splitting.
- Accept potential blank lines defensively by skipping empty input rows.
- Keep integer parsing strict to surface malformed input early.

---

## Pipeline Handoff

### Year
2017

### Day
12

### Puzzle Title
Digital Plumber

### Skeleton Class
`src/main/java/odogwudozilla/year2017/day12/DigitalPlumberAOC2017Day12.java`

### Puzzle Input File
`src/main/resources/2017/day12/day12_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Build an undirected adjacency list and run one BFS/DFS from node `0`, returning the visited-node count.

### Part 2 Status
Not analysed (user requested Part 1 only).

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

## Implementation Summary

### Year and Day
2017 Day 12

### Algorithm Applied
Built an undirected adjacency list from `A <-> B, C, ...` lines and ran a BFS from program `0` to count unique reachable nodes. No deviation from the analysed approach.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| DigitalPlumberAOC2017Day12Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix (`expected: <6> but was: <not implemented>`) -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| `src/main/java/odogwudozilla/year2017/day12/DigitalPlumberAOC2017Day12.java` | Implemented `solvePartOne` using graph parsing + BFS traversal; exposed solve methods for testability. |
| `src/main/java/odogwudozilla/year2017/day12/DigitalPlumberAOC2017Day12.java` | Left `solvePartTwo` as skeleton stub returning `"not implemented"`. |
| `src/test/java/odogwudozilla/year2017/day12/DigitalPlumberAOC2017Day12Test.java` | Added example-based Part 1 unit test from the puzzle analysis. |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes (`Part 2: not implemented`)

### Puzzle Run Result
```text
Part 1: 130
Part 2: not implemented
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available

## Review

### Review Cycle
1

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| - | - | - | - | No issues found in Part 1 implementation scope. |

### Fix Packets

#### Fix Packet - Cycle 1
**Issues in this pass:** None

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | 0 issues queued | 0 |

### Commit Message
Add AOC 2017 Day 12 solution: Digital Plumber - breadth-first search over an undirected graph

Implemented Part 1 by parsing the program pipe list into an undirected adjacency structure and running a single BFS from program `0` to count all reachable nodes. This matches the connected-component requirement directly, handles cycles and self-links safely via a visited set, and runs in linear time relative to nodes plus edges for typical Advent of Code input sizes.




## Part 2 Analysis Update (Post-Part-1 Submission)

### Context
Part 2 description is now available in `src/main/resources/2017/day12/day12_puzzle_description.txt`, and Part 1 has already been submitted.

### Part 2 Requirements

#### Problem Statement
Given the same undirected program-pipe graph, count how many disconnected communication groups (connected components) exist in total, not just the one containing program `0`.

#### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| ```text
0 <-> 2
1 <-> 1
2 <-> 0, 3, 4
3 <-> 2, 4
4 <-> 2, 3, 6
5 <-> 6
6 <-> 4, 5
``` | `2` | Groups are `{0,2,3,4,5,6}` and `{1}`. |

#### How Part 2 Differs from Part 1
Part 1 asks for one component size (reachable from `0`), while Part 2 asks for the total number of components across all nodes. This is a direct extension of the same graph model.

### Algorithm Guidance for Implementation Handoff

#### Recommended Algorithm - CONFIRMED
**Algorithm class:** Graph traversal for connected components (repeated BFS/DFS)

**Rationale:** Reuse the parsed adjacency list, then iterate through all program IDs; whenever an unvisited node is found, run BFS/DFS to mark that whole component and increment a `groupCount`.

#### Implementation Notes
- Reuse `parseGraph(List<String>)` from Part 1 unchanged.
- Maintain one global `Set<Integer> visited` for Part 2 traversal.
- Ensure all IDs appearing either on the left-hand side or in neighbour lists are included in the node set before component counting.
- Self-loops (for example `1 <-> 1`) still represent a valid single-node component if not connected elsewhere.

#### Suggested `solvePartTwo` Outline
1. Parse input into `Map<Integer, List<Integer>> graph`.
2. Build/derive the complete node set from graph keys (and defensively from neighbour lists).
3. For each node not in `visited`, run BFS/DFS and increment `groupCount`.
4. Return `String.valueOf(groupCount)`.

#### Complexity Impact
- **Time:** `O(V + E)` total, because each node/edge is visited at most once across all traversals.
- **Space:** `O(V + E)` for graph storage and visited/frontier structures.
- **Naive warning:** Re-running a full traversal from every node without global visited causes avoidable repeated work.

### Refreshed Pipeline Handoff (Part 2)
- **Puzzle:** 2017 Day 12 - Digital Plumber
- **Skeleton class:** `src/main/java/odogwudozilla/year2017/day12/DigitalPlumberAOC2017Day12.java`
- **Part 2 available:** Yes
- **Recommended implementation:** Count connected components via repeated BFS/DFS over unvisited nodes.
- **Primary example assertion:** sample graph => `2` groups.

## Implementation Summary (Resume Update)

### Year and Day
2017 Day 12

### Algorithm Applied
Used the analysed graph-traversal approach throughout: parse the undirected pipe graph into an adjacency list, run one BFS-style component traversal from program `0` for Part 1, and repeat the same traversal from each unvisited node to count connected components for Part 2.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| DigitalPlumberAOC2017Day12Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Existing example assertion retained; verified by direct harness output (`6`) |
| DigitalPlumberAOC2017Day12Test | givenExampleInput_solvePartTwo_returnsExpectedValue() | Unit | Existing example assertion retained; verified by direct harness output (`2`) |
| DigitalPlumberAOC2017Day12Test | givenInputWithBlankLines_solvePartOne_ignoresEmptyRows() | Unit | Added regression coverage for defensive blank-line parsing |
| DigitalPlumberAOC2017Day12Test | givenInputWithBlankLines_solvePartTwo_ignoresEmptyRows() | Unit | Added regression coverage for defensive blank-line parsing |

### Changes Made
| File | Change description |
|---|---|
| `src/main/java/odogwudozilla/year2017/day12/DigitalPlumberAOC2017Day12.java` | Kept both solve methods on the analysed BFS/connected-component approach, extracted a shared traversal helper, and aligned input loading with `Files.readAllLines(...)` from the resource file. |
| `src/test/java/odogwudozilla/year2017/day12/DigitalPlumberAOC2017Day12Test.java` | Retained the example assertions for both parts and added blank-line regression coverage. |

### Output Format Verified
- Part 1 output: `Part 1: 130` Yes
- Part 2 output: `Part 2: 189` Yes

### Puzzle Run Result
```text
Part 1: 130
Part 2: 189
```

### Validation Notes
- `get_errors` reported no issues in the Day 12 source or test file after the changes.
- `./gradlew compileJava` completed successfully.
- `java -cp "build/classes/java/main;build/resources/main" odogwudozilla.Main 2017 day12` ran successfully.
- A temporary direct Java harness produced the documented example outputs: `Example Part 1: 6` and `Example Part 2: 2`.
- `./gradlew test --tests odogwudozilla.year2017.day12.DigitalPlumberAOC2017Day12Test` could not be completed because compilation is currently blocked by an unrelated failure in `src/test/java/odogwudozilla/year2017/day10/KnotHashAOC2017Day10Test.java` (`solvePartTwo(List<String>) has private access`).

### Deviations from Analysis
None in algorithm terms. The resume pass only refactored the existing implementation to share traversal logic and to use the project-preferred resource loading style.

### Recommended Follow-up
- [ ] Fix the unrelated Day 10 test compilation failure so targeted Gradle test execution can run normally again.
- [ ] Ready for @solution-reviewer.
