# 2023 Day 12 - Hot Springs - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2023 Day 12

### Puzzle Title
Hot Springs

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Problem Summary – ## Pipeline Handoff |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review (Cycle 1) |
| 4 | @solution-implementer | ## Implementation Summary (Review Fix – Cycle 1) |
| 5 | @solution-reviewer | ## Review (Cycle 2) — ALL_VERIFIED |
| 6 | @puzzle-analyser | ## Part 2 Requirements – ## Part 2 Algorithm Approach – ## Part 2 Test Cases |
| 7 | @solution-implementer | ## Implementation Summary (Part 2) |
| 8 | @solution-reviewer | ## Review (Part 2) — ALL_VERIFIED |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2023-day12/2023-day12-analysis.md | Consolidated analysis lifecycle file |

---

## Problem Summary

For each row of a spring condition record — a string of `.` (operational), `#` (damaged),
and `?` (unknown) characters paired with a comma-separated list of contiguous damaged-group
sizes — count how many distinct assignments of `?` characters (each becoming either `.` or
`#`) produce exactly the listed contiguous groups, in order.  
Sum those counts across all rows.

---

## Part 1 Requirements

### Problem Statement
Each line of input is a spring pattern string and a list of damaged-group sizes.
Count valid arrangements of `?` → `.`/`#` per row, then return the grand total across all
rows.

### Example Test Cases
| # | Example Input (row) | Expected Arrangements | Notes |
|---|---|---|---|
| 1 | `???.### 1,1,3` | 1 | Only one valid placement of groups |
| 2 | `.??..??...?##. 1,1,3` | 4 | Each `??` pair can be `#.` or `.#` |
| 3 | `?#?#?#?#?#?#?#? 1,3,1,6` | 1 | Highly constrained pattern |
| 4 | `????.#...#... 4,1,1` | 1 | First four chars must all be `#` |
| 5 | `????.######..#####. 1,6,5` | 4 | Only the leading `????` varies |
| 6 | `?###???????? 3,2,1` | 10 | Many valid placements of groups 2 and 1 |
| **Total** | All six rows | **21** | Sum of all individual arrangement counts |

### Constraints
- Each row independently contributes to the total.
- Groups are always separated by **at least one** operational spring.
- Every `#` in the final arrangement must be accounted for by exactly one group.
- The group list accounts for **all** damaged springs; no extra `#` may appear.
- Input contains no blank lines between records.

### Data Structures in Description
- Sequence of characters (string) representing spring states: `.`, `#`, `?`
- Ordered list of integers representing contiguous damaged-group sizes

---

## Part 2 Requirements

### Problem Statement
Each row of the input is **unfolded** before solving: the pattern string is repeated five
times joined by `?`, and the group-size list is repeated five times joined by `,`.  
Count valid arrangements for every unfolded row and return the grand total — as for Part 1
but on dramatically larger inputs.

### Unfolding Rule (from description)
- Pattern: `<original>?<original>?<original>?<original>?<original>` (×5, separator `?`)  
- Groups: `<original>,<original>,<original>,<original>,<original>` (×5, separator `,`)  
- Example: `.# 1` → `.#?.#?.#?.#?.# 1,1,1,1,1`

### Example Test Cases — Unfolded ×5
| # | Original Row | Expected Arrangements (unfolded) | Notes |
|---|---|---|---|
| 1 | `???.### 1,1,3` | **1** | Rigidly constrained; still only 1 valid arrangement at any scale |
| 2 | `.??..??...?##. 1,1,3` | **16384** | 4 arrangements per copy; 4⁷ = 16 384 (overlapping join adds multiplier) |
| 3 | `?#?#?#?#?#?#?#? 1,3,1,6` | **1** | Fully determined; unfolding does not open new paths |
| 4 | `????.#...#... 4,1,1` | **16** | 16 arrangements across the five joined copies |
| 5 | `????.######..#####. 1,6,5` | **2500** | 2 500 arrangements |
| 6 | `?###???????? 3,2,1` | **506250** | 506 250 arrangements |
| **Total** | All six rows unfolded | **525152** | Sum of the six unfolded counts |

### Constraints (additional for Part 2)
- Pattern length grows from ~10–30 chars to ~50–150 chars after ×5 unfolding.
- Group list grows from ~3–8 entries to ~15–40 entries.
- Arrangement counts can reach the hundreds of thousands per row; use `long` throughout.
- The memoisation cache must now cover the full unfolded pattern + group list dimensions.

### How Part 2 Differs from Part 1
**Scale-up via unfolding.** The core algorithm is identical — memoised recursion over
`(patternIndex, groupIndex)` — but the input to that algorithm is 5× larger in both
dimensions. The brute-force O(2^N) approach that was marginally viable for Part 1 is
completely infeasible here (worst-case `2^150`). The memoised DP solution scales to
`O(L₅ × G₅)` per row where `L₅ ≈ 5L + 4` and `G₅ = 5G`, which remains within budget.

---

## Part 2 Algorithm Approach

### Recommended Algorithm — CONFIRMED
**Algorithm class:** Dynamic Programming with Memoisation (top-down recursive) — **same
algorithm as Part 1**  
**Rationale:** The Part 1 memoised recursion over `(patternIndex, groupIndex)` applies
directly to the unfolded input; only the input construction changes. The state space per
row is `O(L₅ × G₅)` ≈ `O(5L × 5G)` = `O(25 × L × G)`, still entirely tractable.

### Unfolding Implementation Notes
- Build the unfolded pattern string: `String.join("?", Collections.nCopies(5, original))`.
- Build the unfolded group array: repeat the parsed `int[]` five times into a new `int[5G]`.
- Pass the unfolded pattern and groups to the **same** `countArrangements` helper used in
  Part 1 with a **fresh memoisation cache** (the state space is row-specific).

### Known Pitfalls (Part 2 specific)
- **Cache key capacity:** If the memo key is packed as `patternIndex << 8 | groupIndex`,
  the bit shift of 8 supports up to 256 groups. With 5G ≤ 40 this is safe, but verify
  the shift width covers the actual maximum unfolded group count.
- **String construction overhead:** Joining five copies with `String.join` is negligible
  for ~1 000 rows; no concern.
- **Separate memo per row:** The cache must be cleared or re-created between rows, as in
  Part 1. Reusing a stale cache across rows would corrupt results.
- **Long overflow:** Row counts up to ~506 250 are safe as `long`; the running total
  across ~1 000 rows remains well within `Long.MAX_VALUE`.

---

## Part 2 Test Cases

The table below lists the six example rows from the puzzle description, showing the
original input, the unfolded pattern, and the expected arrangement count.

| # | Original Pattern | Original Groups | Unfolded Pattern (abbreviated) | Unfolded Groups | Expected Count |
|---|---|---|---|---|---|
| 1 | `???.###` | `1,1,3` | `???.###????.###????.###????.###????.###` | `1,1,3,1,1,3,1,1,3,1,1,3,1,1,3` | **1** |
| 2 | `.??..??...?##.` | `1,1,3` | `.??..??...?##.?.??..??...?##.?…` | `1,1,3,1,1,3,1,1,3,1,1,3,1,1,3` | **16384** |
| 3 | `?#?#?#?#?#?#?#?` | `1,3,1,6` | `?#?#?#?#?#?#?#???#?#?#?#?#?#?#?…` | `1,3,1,6,1,3,1,6,1,3,1,6,1,3,1,6,1,3,1,6` | **1** |
| 4 | `????.#...#...` | `4,1,1` | `????.#...#...?????.#...#...?…` | `4,1,1,4,1,1,4,1,1,4,1,1,4,1,1` | **16** |
| 5 | `????.######..#####.` | `1,6,5` | `????.######..#####.?????.######..#####.?…` | `1,6,5,1,6,5,1,6,5,1,6,5,1,6,5` | **2500** |
| 6 | `?###????????` | `3,2,1` | `?###??????????###??????????###????????…` | `3,2,1,3,2,1,3,2,1,3,2,1,3,2,1` | **506250** |
| **Total** | — | — | — | — | **525152** |

---

## Algorithm Approach

### Recommended Algorithm — PROPOSED
**Algorithm class:** Dynamic Programming with Memoisation (top-down recursive)  
**Rationale:** The problem is a classic constraint-satisfaction count over a sequence with
unknown positions; the subproblem — "how many ways can the remaining groups fit into the
remaining suffix of the pattern?" — overlaps heavily, making memoised recursion the
natural and efficient choice.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Brute-force enumeration | Replace each `?` with `.` or `#`, validate against group list. Simple to implement but O(2^N) per row — feasible only for short rows; likely too slow for large real inputs. |
| Bottom-up DP (table) | Equivalent time complexity to top-down memoisation; slightly harder to express the 2-D state cleanly in iterative form but avoids recursion stack overhead. |
| Regex / NFA matching | Translate group list into a regex such as `\.*(#{g1})\.+(#{g2})…\.*`; enumerate matches. Elegant but slower due to regex overhead and harder to count distinct solutions directly. |

**Recommended:** Top-down recursion with memoisation (HashMap keyed on `(patternIndex, groupIndex)`).

### Known Pitfalls
- **Off-by-one on group boundaries:** After placing a group of size `k`, the character
  immediately following must be `.` or `?` (forced to `.`) — or the end of the string.
  Forgetting this boundary separator causes overcounting.
- **Trailing `#` validation:** After all groups are placed, no unaccounted `#` may remain
  in the rest of the pattern; a residual scan or recursion guard is required.
- **Parsing delimiter:** Each line splits on a single space; the group list then splits on
  `,`. Mixed whitespace is not expected but should be trimmed defensively.
- **`?` acts as wildcard:** At each `?` position the algorithm must branch into both the
  `.` (skip) and `#` (start group) cases.
- **Integer overflow:** Individual row counts can be large; use `long` throughout, not
  `int`.

---

## Complexity Assessment

### Input Scale
Typical AoC Day 12 real inputs contain ~1 000 rows. Each pattern string is approximately
10–30 characters long. Group lists contain roughly 3–8 entries.

### Required Time Complexity
`O(R × L × G)` per part, where `R` = number of rows, `L` = pattern length, `G` = number
of groups. With memoisation the state space per row is `L × G` and each state is computed
once — well within the ~1 s limit for Part 1 at this scale.

### Space Complexity
`O(L × G)` per row for the memoisation cache (cleared between rows). No global memory
concern for Part 1.

### Naive Approach Viable?
**For Part 1 — Yes, marginally**, given the short pattern lengths (~10–30 chars). Brute
force (`2^N` enumeration) would be at most `2^30` ≈ 10⁹ in the worst case, which is
borderline. Memoised DP is strongly preferred as it handles worst-case inputs safely and
will be essential for Part 2 (expected scale-up).

---

## Implementation Plan

### Input Reading
```java
List<String> lines = readInput(); // already provided via InputStream/Scanner in skeleton
```
The skeleton class already reads lines from `/2023/day12/day12_puzzle_data.txt` via
`InputStream`; no change to `readInput()` is needed.

### Key Data Structures
- `char[]` — the pattern for a single row (efficient character-level indexing)
- `int[]` — the group sizes for a single row
- `Map<Long, Long>` — memoisation cache keyed on a packed `(patternIndex << 6 | groupIndex)` long, or equivalently `Map<String, Long>` keyed on `"pi,gi"` for clarity
- `long` — accumulator for the total arrangement count (to prevent overflow)

### solvePartOne Outline
1. For each line, split on `" "` to obtain the pattern string and the group-size string;
   parse the group sizes into an `int[]`.
2. Call a recursive helper `countArrangements(char[] pattern, int[] groups, int pi, int gi,
   Map<Long,Long> memo)` starting at indices `(0, 0)`.
3. At each position, branch on whether the current character forces a `.` (skip), forces a
   `#` (must start a group), or is `?` (try both).  When starting a group, verify the next
   `groups[gi]` characters can all be `#`/`?` and are followed by `.`/`?`/end-of-string.
4. Accumulate and return the sum of all row counts as a `String`.

### solvePartTwo Outline
1. For each line, split and parse identically to Part 1.
2. Unfold the pattern: `String.join("?", Collections.nCopies(5, patternStr))`.
3. Unfold the groups: copy the parsed `int[G]` five times into a new `int[5G]`.
4. Call the **same** `countArrangements` helper with the unfolded pattern and groups
   (and a fresh memo cache per row); accumulate and return the sum as a `String`.

### Helper Methods
- `countArrangements(char[] pattern, int[] groups, int pi, int gi, Map<Long,Long> memo)` —
  core memoised recursive counter; returns `long`
- `parseGroups(String groupStr)` — splits `"1,3,1,6"` into `int[]`
- `packKey(int pi, int gi)` — encodes two indices into a single `long` for memo key

### Parsing Notes
- Split each line on `" "` (single space); trim is defensive but not strictly necessary.
- Group sizes are comma-separated integers with no spaces: `"1,3,1,6"`.
- No blank lines between data rows expected; skip/guard if encountered.
- Pattern characters are exclusively `.`, `#`, and `?` — no other characters.

---

## Edge Cases

- **No unknowns:** Pattern is fully determined; answer is 1 if it matches the groups,
  else 0. The recursion handles this naturally.
- **No groups:** If the group list is empty, the answer is 1 only if no `#` remain in the
  pattern suffix; else 0.
- **Pattern exhausted before groups:** Answer is 0.
- **Groups exhausted before pattern:** Answer is 1 only if no `#` remains in the rest of
  the pattern.
- **Consecutive `#` longer than current group:** Must return 0 immediately — the group
  placement would be invalid.
- **Single-character pattern:** Edge case for boundary checks when verifying the separator
  character after a group.

---

## Pipeline Handoff

### Year
2023

### Day
12

### Puzzle Title
Hot Springs

### Skeleton Class
`src/main/java/odogwudozilla/year2023/day12/HotSpringsAOC2023Day12.java`

### Puzzle Input File
`src/main/resources/2023/day12/day12_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Top-down memoised recursion over `(patternIndex, groupIndex)` state space — O(L × G) per
row. For Part 2, apply identically to the ×5-unfolded input (same algorithm, larger input).

### Part 2 Status
Available — description fully scraped. Part 1 accepted (answer: 7633).

### Section Anchors
- Problem summary — `## Problem Summary`
- Part 1 requirements — `## Part 1 Requirements`
- Part 2 requirements — `## Part 2 Requirements`
- Part 2 algorithm — `## Part 2 Algorithm Approach`
- Part 2 test cases — `## Part 2 Test Cases`
- Algorithm approach — `## Algorithm Approach`
- Edge cases — `## Edge Cases`
- Implementation plan — `## Implementation Plan`

---

## Implementation Summary

### Year and Day
2023 Day 12

### Algorithm Applied
Top-down memoised recursion over `(patternIndex, groupIndex)` state space, as recommended.  
The memo key is a packed `long` (`patternIndex << 8 | groupIndex`). Each `?` branches into both
`.` (skip) and `#` (attempt group placement). After placing a group, the mandatory separator
character is consumed before recursing. No deviations from the analysis plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| HotSpringsAOC2023Day12Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix → Passes after fix |
| HotSpringsAOC2023Day12Test | givenRow1_solvePartOne_returns1() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow2_solvePartOne_returns4() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow3_solvePartOne_returns1() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow4_solvePartOne_returns1() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow5_solvePartOne_returns4() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow6_solvePartOne_returns10() | Unit | Passes |

### Changes Made
| File | Change description |
|---|---|
| HotSpringsAOC2023Day12.java | Implemented `solvePartOne`: top-down memoised recursion counting valid `?` arrangements per row, summed across all rows |
| HotSpringsAOC2023Day12.java | `solvePartTwo` left as stub returning `"not implemented"` |
| HotSpringsAOC2023Day12Test.java | Created with 7 example-based unit tests covering all 6 example rows and their combined total |

### Output Format Verified
- Part 1 output: `Part 1: 7633` — Yes
- Part 2 output: `Part 2: not implemented` — Yes (stub)

### Puzzle Run Result
```
Part 1: 7633
Part 2: not implemented
```

### Deviations from Analysis
- The constraint specified `com.quinity.qfa.junit.BaseTest` and `@Tag(AxonTestTags.UNIT_TEST)` for the test class, but neither exists in the project. The existing project test classes use plain JUnit 5 with no base class. The established project pattern was followed instead.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available

---

## Implementation Summary (Part 2)

### Year and Day
2023 Day 12

### Algorithm Applied
Same top-down memoised recursion over `(patternIndex, groupIndex)` as Part 1.  
Each row is unfolded ×5 before solving: pattern repeated five times joined by `?`
(using `UNFOLD_SEPARATOR`), groups array repeated five times (using `UNFOLD_FACTOR`).
A fresh `HashMap` memo cache is created per row. No deviations from the analysis plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| HotSpringsAOC2023Day12Test | givenExampleInput_solvePartTwo_returns525152() | Unit | Fails before fix → Passes after fix |
| HotSpringsAOC2023Day12Test | givenRow1Unfolded_solvePartTwo_returns1() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow2Unfolded_solvePartTwo_returns16384() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow3Unfolded_solvePartTwo_returns1() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow4Unfolded_solvePartTwo_returns16() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow5Unfolded_solvePartTwo_returns2500() | Unit | Passes |
| HotSpringsAOC2023Day12Test | givenRow6Unfolded_solvePartTwo_returns506250() | Unit | Passes |

### Changes Made
| File | Change description |
|---|---|
| HotSpringsAOC2023Day12.java | Added `UNFOLD_FACTOR = 5` and `UNFOLD_SEPARATOR = '?'` named constants |
| HotSpringsAOC2023Day12.java | Implemented `solvePartTwo`: unfolds each row ×5 then calls `countArrangements` with fresh memo |
| HotSpringsAOC2023Day12Test.java | Added 7 Part 2 example-based unit tests (6 individual rows + combined total) |

### Output Format Verified
- Part 1 output: `Part 1: 7633` — Yes
- Part 2 output: `Part 2: 23903579139437` — Yes

### Puzzle Run Result
```
Part 1: 7633
Part 2: 23903579139437
```

### Deviations from Analysis
None. The analysis specified exactly this approach: same `countArrangements` helper,
`String.join`-style unfolding (implemented via `StringBuilder` with `UNFOLD_SEPARATOR`),
group array repeated with `System.arraycopy`, fresh memo per row.

### Recommended Follow-up
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`

---

## Review (Part 2)

### Review Cycle
3 (Part 2 review)

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| — | — | — | — | No issues found. All checks passed. |

### Checks Performed
| Check | Result |
|---|---|
| `solvePartTwo` unfolding — pattern ×5 joined by `UNFOLD_SEPARATOR` | ✅ |
| `solvePartTwo` unfolding — groups ×5 via `System.arraycopy` | ✅ |
| Fresh `HashMap` memo per row | ✅ |
| Same `countArrangements` helper reused | ✅ |
| `UNFOLD_FACTOR = 5` constant (no magic number `5`) | ✅ |
| `UNFOLD_SEPARATOR = '?'` constant (no raw `'?'` in unfold loop) | ✅ |
| `OPERATIONAL`, `DAMAGED`, `UNKNOWN` constants used in `countArrangements` | ✅ |
| Pack key capacity: `GROUP_INDEX_BITS = 8` supports up to 256 groups; max unfolded ≈ 40 | ✅ |
| Output format `Part 1: 7633` / `Part 2: 23903579139437` | ✅ |
| Input from classpath via `INPUT_FILE` constant, no hardcoded path | ✅ |
| Class name `HotSpringsAOC2023Day12`, package `odogwudozilla.year2023.day12` | ✅ |
| Method names `solvePartOne`, `solvePartTwo` | ✅ |
| No wildcard imports (regular or static) | ✅ explicit `assertEquals` only |
| No magic numbers / raw character literals | ✅ |
| Meaningful variable names | ✅ |
| Method length / single responsibility | ✅ |
| Algorithm efficiency O(L×G) per row with memoisation | ✅ |
| Part 2 tests: all 6 individual rows + combined total (7 tests) | ✅ |
| Test values match puzzle description expected counts | ✅ |
| Test names clearly describe scenario and expected outcome | ✅ |
| British English in comments and Javadoc ("memoised" consistently) | ✅ |

### Commit Message
Add AOC 2023 Day 12 solution: Hot Springs - top-down memoised recursion with ×5 unfolding for Part 2

Count valid spring arrangements per row using top-down memoised recursion over a
(patternIndex, groupIndex) state space, reducing per-row complexity to O(L × G) from
the brute-force O(2^N). Part 1 solves each row directly; Part 2 unfolds each row ×5
by joining the pattern string with '?' and repeating the groups array, then passes the
enlarged input to the same countArrangements helper with a fresh memoisation cache per
row. Named constants (OPERATIONAL, DAMAGED, UNKNOWN, UNFOLD_FACTOR, UNFOLD_SEPARATOR,
GROUP_INDEX_BITS) replace all raw character literals and magic numbers. The memo key is
packed as a single long (patternIndex shifted by GROUP_INDEX_BITS = 8 | groupIndex),
comfortably covering the maximum unfolded group count of ~40. Real answers: Part 1 = 7633,
Part 2 = 23903579139437.

---

## Review

### Review Cycle
1

### Status
ISSUES_FOUND

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-001 | CodingConvention | Minor | HotSpringsAOC2023Day12.java | `readInput()` uses fully qualified `java.util.List` and `java.util.ArrayList` on lines 191–192 despite `java.util.List` already being imported; `ArrayList` must be added as a proper import and both types used with their simple names |
| RV-002 | CodingConvention | Minor | HotSpringsAOC2023Day12.java | Spring-state character literals `'.'`, `'#'`, and `'?'` appear five times as raw literals in `countArrangements`; they must be extracted as `private static final char` constants (e.g. `OPERATIONAL`, `DAMAGED`, `UNKNOWN`) |

### Fix Packets

#### Fix Packet - Cycle 1
**Issues in this pass:** RV-001, RV-002

##### RV-001 - CodingConvention - Minor
**File:** HotSpringsAOC2023Day12.java  
**Issue:** Inside `readInput()` (lines 191–192), `java.util.List<String>` and `new java.util.ArrayList<>()` are referenced by their fully-qualified names. `java.util.List` is already imported at line 7; `java.util.ArrayList` is not imported at all.  
**Resolution guidance:** Add `import java.util.ArrayList;` to the import block and replace the fully-qualified usages in `readInput()` with `List<String>` and `new ArrayList<>()` respectively.

##### RV-002 - CodingConvention - Minor
**File:** HotSpringsAOC2023Day12.java  
**Issue:** The character literals `'.'`, `'#'`, and `'?'` are used as raw magic characters in five places inside `countArrangements`, making intent less obvious and violating the no-magic-numbers/literals convention.  
**Resolution guidance:** Declare three `private static final char` constants at the top of the class — e.g. `OPERATIONAL = '.'`, `DAMAGED = '#'`, `UNKNOWN = '?'` — and replace every occurrence of the raw character literals in `countArrangements` with the named constants.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | — | 2 issues queued (RV-001, RV-002) |
| 2 | Fix applied by @solution-implementer | RV-001, RV-002 | 0 |
| 2 | Re-review by @solution-reviewer | ALL_VERIFIED — 0 remaining | — |

### Fixes Applied (Cycle 1)
| ID | Resolution |
|---|---|
| RV-001 | Added `import java.util.ArrayList;`; replaced `java.util.List<String>` and `new java.util.ArrayList<>()` FQNs in `readInput()` with simple names. |
| RV-002 | Declared `private static final char OPERATIONAL = '.'`, `DAMAGED = '#'`, `UNKNOWN = '?'`; replaced all five raw character literals in `countArrangements` with the named constants. |

### Post-Fix Verification
- `Part 1: 7633` — confirmed ✔
- All Day 12 tests pass ✔

---

## Review — Cycle 2

### Review Cycle
2

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| — | — | — | — | No new issues found. All Cycle 1 fixes verified. |

### Verification of Cycle 1 Fix Packet

#### RV-001 — CodingConvention — Minor — VERIFIED
`import java.util.ArrayList;` present at line 6. `readInput()` uses `List<String>` and `new ArrayList<>()` with simple names throughout. ✅

#### RV-002 — CodingConvention — Minor — VERIFIED
`private static final char OPERATIONAL = '.'`, `DAMAGED = '#'`, `UNKNOWN = '?'` declared at lines 25–29. All five raw character literals in `countArrangements` replaced with the named constants. ✅

### Additional Checks Performed
| Check | Result |
|---|---|
| Output format (`Part 1:` / `Part 2:`) | ✅ |
| Resource reading from classpath | ✅ |
| Class name convention | ✅ |
| Package convention | ✅ |
| Method names `solvePartOne` / `solvePartTwo` | ✅ |
| Wildcard imports (including `import static`) | ✅ None |
| Magic numbers / literals | ✅ All named |
| Meaningful variable names | ✅ |
| `.equals()` constant-on-left | ✅ Only `char ==` comparisons used |
| Method length / single responsibility | ✅ |
| Algorithm efficiency O(L×G) memoised | ✅ |
| Test uses example inputs with expected values | ✅ All 7 tests |
| Test wildcard static imports | ✅ None |
| British English in comments / Javadoc | ✅ "memoised" used consistently |

### Commit Message
Add AOC 2023 Day 12 solution: Hot Springs - top-down memoised recursion over (patternIndex, groupIndex)

Count the valid arrangements of damaged and operational spring sequences per row by
recursively exploring each position in the pattern: operational characters (or unknowns
treated as operational) advance the pattern index, while damaged characters (or unknowns
treated as damaged) trigger a group-placement attempt that verifies all required characters
can be '#' or '?' and that a mandatory separator follows. The memoisation cache, keyed on
a packed long of the two indices, reduces per-row complexity to O(L × G) — well within
the one-second budget — and positions the solution for the expected Part 2 scale-up.













