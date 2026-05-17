# 2023 Day 11 - Cosmic Expansion - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2023 Day 11

### Puzzle Title
Cosmic Expansion

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | All sections (Initial analysis) |

### Files Created
| File | Purpose |
|------|---------|
| 2023-day11-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given a grid containing galaxies (#) and empty space (.), expand the universe by doubling any rows or columns that contain no galaxies, then calculate the sum of shortest path distances between every pair of galaxies using Manhattan distance.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| 10×10 grid with 9 galaxies (shown in puzzle description) | 374 | After expansion, sum of all pairwise Manhattan distances = 374 |

**Sample galaxy pairs from example:**
- Galaxy 1 to Galaxy 7: 15
- Galaxy 3 to Galaxy 6: 17
- Galaxy 8 to Galaxy 9: 5
- Galaxy 5 to Galaxy 9: 9 (detailed path example shown)
- Total for all 36 pairs: 374

### Constraints
- Input is a rectangular grid of characters ('.' or '#')
- Empty rows/columns should be counted as taking up 2× space
- Only step up, down, left, right (4-directional, no diagonals)
- Shortest path between galaxies must account for expansion
- Order does not matter when counting pairs (count each pair once)

### Data Structures in Description
- 2D grid (character array or list of strings)
- List of galaxy coordinates (row, column)
- Set of empty row indices and empty column indices

---

## Part 2 Requirements

### Problem Statement
Not yet available in the current description. Part 2 will likely be released after Part 1 submission.

### Expected Scope
Based on typical AoC patterns, Part 2 will probably:
- Request a much larger expansion factor (e.g. 1,000,000 instead of 2)
- Keep the same algorithm but with a parameterised expansion multiplier
- Require efficient distance calculation without materialising the full expanded grid

### How Part 2 Differs from Part 1
Expected to be a scale variant: same problem with different expansion factor. The algorithm remains the same (sum of pairwise distances), but the scaling factor is parameterised.

---

## Algorithm Approach

### Recommended Algorithm - **CONFIRMED**
**Algorithm class:** Coordinate Geometry + Pairwise Distance Summation  
**Rationale:** This is a distance calculation problem where the expansion factor modifies coordinates logically without materialising the grid. Calculate distance for each pair of galaxies using Manhattan distance, accounting for expansions, then sum all distances.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Materialise the full expanded grid | Simple to understand, but wasteful of memory and slower for large expansion factors. Not scalable to Part 2. |
| BFS from each galaxy | Unnecessary; Manhattan distance is optimal in a rectilinear grid. Slower than direct calculation. |

### Known Pitfalls
- **Off-by-one errors in expansion tracking:** Must correctly identify which rows/columns are empty before any expansion.
- **Pairwise iteration:** With N galaxies, must iterate N×(N−1)/2 pairs only; avoid double-counting.
- **Coordinate transformation:** Empty rows/columns increase distance between galaxies; the distance calculation must account for how many empty rows/columns lie between the two galaxies, not the galaxies themselves.
- **Integer overflow:** If expansion factor becomes very large (Part 2), pairwise distances can grow; use `long` for distance sums.

---

## Complexity Assessment

### Input Scale
- Typical grid: ~140 rows × ~140 columns (10,000–20,000 cells)
- Typical galaxy count: 50–500
- Pairwise distances: ~1,250–125,000 calculations

### Required Time Complexity
**O(N²)** where N is the number of galaxies (typically 50–500).
- Parsing: O(R × C)
- Identifying empty rows/columns: O(R + C)
- Pairwise distance calculation: O(N²)
- **Total:** O(R × C + N²) ≈ O(N²) for typical inputs.

This is efficient enough for ~1 second execution even with 500 galaxies (~125,000 pairs).

### Space Complexity
O(N) for storing galaxy coordinate lists and empty row/column indices. No need to materialise the expanded grid.

### Naive Approach Viable?
**Yes.** Direct pairwise distance calculation is the intended solution. No further optimisation needed for Part 1. For Part 2 (if expansion factor becomes very large), the same O(N²) approach applies; only the distance calculation changes.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = readInput();
// Input is read via the scanner in the skeleton (INPUT_FILE = "/2023/day11/day11_puzzle_data.txt")
```

### Key Data Structures
- **`List<String> grid`** – the raw input lines (each row as a string)
- **`List<Pair<Integer, Integer>> galaxies`** – coordinates (row, col) of all galaxies
- **`Set<Integer> emptyRows`** – indices of rows with no galaxies
- **`Set<Integer> emptyColumns`** – indices of columns with no galaxies

### solvePartOne Outline
1. Parse the input grid and extract all galaxy coordinates into a list.
2. Identify empty rows (all '.') and empty columns (all '.').
3. Iterate through all pairs of galaxies; for each pair, calculate the Manhattan distance, accounting for how many empty rows and columns lie between them (each contributes an extra 1 to the distance, doubling the expansion).
4. Sum all pairwise distances and return.

### solvePartTwo Outline
Implement after Part 1 submission. Expected approach: parameterise the expansion factor (currently 2) and re-run pairwise distance calculation. If the factor is 1,000,000, use `long` for all distance calculations to avoid overflow.

### Helper Methods
- **`extractGalaxies(List<String> grid)`** – Returns list of (row, col) for all '#' characters.
- **`findEmptyRows(List<String> grid)`** – Returns set of row indices with all '.'.
- **`findEmptyColumns(List<String> grid)`** – Returns set of column indices with all '.'.
- **`calculateDistance(Pair<Integer, Integer> g1, Pair<Integer, Integer> g2, Set<Integer> emptyRows, Set<Integer> emptyColumns, int expansionFactor)`** – Returns Manhattan distance with expansion adjustment.

### Parsing Notes
- Input is a simple grid: newline-delimited strings, each character is either '.' or '#'.
- No blank lines or special delimiters.
- Grid is rectangular; all rows have the same length.
- Straightforward to parse with `List<String>`.

---

## Pipeline Handoff

### Year
2023

### Day
11

### Puzzle Title
Cosmic Expansion

### Skeleton Class
`src/main/java/odogwudozilla/year2023/day11/CosmicExpansionAOC2023Day11.java`

### Puzzle Input File
`src/main/resources/2023/day11/day11_puzzle_data.txt`

### Puzzle Description File
`src/main/resources/2023/day11/day11_puzzle_description.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Sum of Manhattan distances between all galaxy pairs, accounting for empty row/column expansions (expansion factor = 2 for Part 1).

### Part 2 Status
Not yet available in current description – expected to be released after Part 1 submission. Likely a scale variant with larger expansion factor.

### Section Anchors
- Part 1 requirements – `## Part 1 Requirements`
- Algorithm approach – `## Algorithm Approach`
- Complexity assessment – `## Complexity Assessment`
- Implementation plan – `## Implementation Plan`
- Pipeline handoff – `## Pipeline Handoff`

---

