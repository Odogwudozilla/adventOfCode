# 2024 Day 12 - Garden Groups - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2024 Day 12

### Puzzle Title
Garden Groups

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @solution-implementer | ## Part 1 Requirements - ## Pipeline Handoff |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2024-day12/2024-day12-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given a rectangular garden map of plant letters, treat each maximal orthogonally connected set of equal letters as one region. For each region, compute `area * perimeter`, then sum those prices across all regions.

### Example Test Cases
| Example Input | Expected Output |
|---|---|
| `AAAA\nBBCD\nBBCC\nEEEC` | `140` |
| `OOOOO\nOXOXO\nOOOOO\nOXOXO\nOOOOO` | `772` |
| Larger example from the description | `1930` |

---

## Part 2 Requirements

### Problem Statement
Use the same regions, but replace perimeter with the number of straight fence sides. Each maximal straight fence run counts as one side regardless of length, and inside and outside fence runs must be counted separately where regions touch diagonally.

### Example Test Cases
| Example Input | Expected Output |
|---|---|
| `AAAA\nBBCD\nBBCC\nEEEC` | `80` |
| `OOOOO\nOXOXO\nOOOOO\nOXOXO\nOOOOO` | `436` |
| `EEEEE\nEXXXX\nEEEEE\nEXXXX\nEEEEE` | `236` |
| `AAAAAA\nAAABBA\nAAABBA\nABBAAA\nABBAAA\nAAAAAA` | `368` |
| Larger example from the description | `1206` |

### Key Pitfall
Fence segments on the same grid line do not always belong to the same side. When the region lies on opposite sides of those segments, the runs must stay separate.

---

## Algorithm Approach

### Confirmed Algorithm
1. Parse the input into a `char[][]` garden grid.
2. Flood-fill each unvisited region with a queue-based BFS.
3. For every cell in the region:
   - increment area
   - add to perimeter whenever a neighbouring cell is out of bounds or a different plant type
   - record boundary edges separately as top, bottom, left, or right edge runs
4. Count Part 2 sides by sorting edge positions within each directional line map and counting contiguous runs.
5. Add `area * perimeter` to Part 1 and `area * sides` to Part 2.

### Why This Works
- BFS guarantees that each plot belongs to exactly one region.
- Perimeter is the count of exposed unit edges.
- Counting contiguous boundary runs per direction preserves the inside/outside distinction required by Part 2.

### Complexity
- Time: `O(R * C)` plus edge-run sorting bounded by the number of boundary edges
- Space: `O(R * C)` for visited tracking and the largest region queue

---

## Implementation Summary

### Files Changed
| File | Change |
|---|---|
| `src/main/java/odogwudozilla/year2024/day12/GardenGroupsAOC2024Day12.java` | Implemented both puzzle parts with shared region analysis and corrected Part 2 side counting. |
| `src/test/java/odogwudozilla/year2024/day12/GardenGroupsAOC2024Day12Test.java` | Added example-based regression coverage for Part 1 and Part 2. |

### Real Input Results
- Part 1: `1363682`
- Part 2: `787680`

### Validation Performed
- `./gradlew.bat run --args="2024 day12"` -> produced `Part 1: 1363682` and `Part 2: 787680`
- `./gradlew.bat test --tests "odogwudozilla.year2024.day12.GardenGroupsAOC2024Day12Test"` is currently blocked by an unrelated pre-existing compile failure in `src/test/java/odogwudozilla/year2017/day10/KnotHashAOC2017Day10Test.java`

---

## Review

### Findings
- The final Part 2 implementation correctly handles the diagonal-touching interior boundary case described in the puzzle.
- The solver reads all input from `src/main/resources/2024/day12/day12_puzzle_data.txt` and does not hardcode puzzle data.
- Output remains in the required format for the automation runner:
  - `Part 1: <answer>`
  - `Part 2: <answer>`

### Residual Risk
- Repository-wide test execution remains affected by the unrelated `KnotHashAOC2017Day10Test` access modifier issue.

---

## Pipeline Handoff

- Year: `2024`
- Day: `12`
- Workflow Stage: `Solved, submitted, pending final commit rerun`
- Recommended Outputs: `Commit all generated puzzle artefacts for 2024 Day 12`
- Constraints: `Do not edit dashboard/index.html; preserve automation output format; keep analysis in this file only`
- Reference Sections:
  - `## Part 1 Requirements`
  - `## Part 2 Requirements`
  - `## Algorithm Approach`
  - `## Implementation Summary`
  - `## Review`

