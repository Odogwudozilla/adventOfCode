# 2021 Day 8 - Seven Segment Search - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2021 Day 8

### Puzzle Title
Seven Segment Search

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Puzzle Summary – ## Pipeline Handoff |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review (Cycle 1) |
| 4 | @solution-implementer | ## Fix Implementation Summary (Cycle 1) |
| 5 | @solution-reviewer | ## Review (Cycle 2) |
| 6 | @solution-implementer | ## Fix Implementation Summary (Cycle 2) |
| 7 | @solution-reviewer | ## Review (Cycle 3) |
| 8 | @solution-implementer | ## Fix Implementation Summary (Cycle 3) |
| 9 | @solution-reviewer | ## Review (Cycle 4) — ALL_VERIFIED |
| 10 | @puzzle-analyser | ## Part 2 Requirements – ## Part 2 Algorithm Approach – ## Part 2 Edge Cases – ## Pipeline Handoff (updated) |
| 11 | @solution-implementer | ## Part 2 Implementation Summary |
| 12 | @solution-reviewer | ## Review (Part 2) — Cycle 1 |
| 13 | @solution-implementer | ## Fix Implementation Summary (Part 2 Cycle 1) |
| 14 | @solution-reviewer | ## Review (Part 2) — Cycle 2 |
| 15 | @solution-implementer | ## Fix Implementation Summary (Part 2 Cycle 2) |
| 16 | @solution-reviewer | ## Review (Part 2) — Cycle 3 — ALL_VERIFIED |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2021-day8/2021-day8-analysis.md | Consolidated analysis lifecycle file |

---

## Puzzle Summary

Each entry in the puzzle input contains **ten unique signal patterns** (the scrambled wiring for all ten digits on a single display), a `|` delimiter, and a **four-digit output value**.
The wiring between signal wires (`a`–`g`) and physical display segments is randomly shuffled independently for every display.

Part 1 asks a deliberately narrow question: using only the **segment-count heuristic**, count how many times the digits **1, 4, 7, or 8** appear across all output values (the right-hand side of `|`).  
These four digits are uniquely identifiable because they are the only digits that use exactly **2, 4, 3, and 7 segments** respectively — no other digit shares that count.

---

## Part 1 Requirements

### Problem Statement
In the four-digit output values (after the `|` delimiter on each input line), count the total number of digit occurrences whose segment count uniquely identifies them as **1** (2 segments), **4** (4 segments), **7** (3 segments), or **8** (7 segments).

### Unique Segment Counts (key fact)
| Digit | Segment count | Unique? |
|-------|---------------|---------|
| 0     | 6             | No (shared with 6, 9) |
| 1     | 2             | **Yes** |
| 2     | 5             | No (shared with 3, 5) |
| 3     | 5             | No (shared with 2, 5) |
| 4     | 4             | **Yes** |
| 5     | 5             | No (shared with 2, 3) |
| 6     | 6             | No (shared with 0, 9) |
| 7     | 3             | **Yes** |
| 8     | 7             | **Yes** |
| 9     | 6             | No (shared with 0, 6) |

### Example Test Cases

**Single-entry example (lines 43–44 of description):**
```
acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
cdfeb fcadb cdfeb cdbaf
```
Output tokens: `cdfeb` (5 segs), `fcadb` (5 segs), `cdfeb` (5 segs), `cdbaf` (5 segs) → **0** easy-digit occurrences (all five-segment).

**Larger example (lines 54–73 of description — 10 entries):**

| Line (output side only) | Easy digits found |
|-------------------------|-------------------|
| `fdgacbe cefdb cefbgd gcbe` | `fdgacbe`=7 segs→8, `gcbe`=4 segs→4 → **2** |
| `fcgedb cgb dgebacf gc` | `dgebacf`=7→8, `gc`=2→1 → **2** |
| `cg cg fdcagb cbg` | `cg`=2→1, `cg`=2→1, `cbg`=3→7 → **3** |
| `efabcd cedba gadfec cb` | `cb`=2→1 → **1** |
| `gecf egdcabf bgf bfgea` | `gecf`=4→4, `egdcabf`=7→8, `bgf`=3→7 → **3** |
| `gebdcfa ecba ca fadegcb` | `gebdcfa`=7→8, `ecba`=4→4, `ca`=2→1, `fadegcb`=7→8 → **4** |
| `cefg dcbef fcge gbcadfe` | `cefg`=4→4, `fcge`=4→4, `gbcadfe`=7→8 → **3** |
| `ed bcgafe cdgba cbgef` | `ed`=2→1 → **1** |
| `gbdfcae bgc cg cgb` | `gbdfcae`=7→8, `cg`=2→1, `cgb`=3→7 → **3** |
| `fgae cfgab fg bagce` | `fgae`=4→4, `fg`=2→1 → **2** |
| **Total** | **26** |

**Expected output for Part 1 on the larger example: `26`.**

### Constraints
- Each input line contains exactly **10 signal patterns** followed by `|` followed by **4 output tokens**.
- All tokens consist solely of lowercase letters `a`–`g`.
- Segment letters within a token may appear in **any order** (e.g. `gcbe` and `bcge` refer to the same set of segments).
- Every display uses a **distinct, independent** wire scramble; patterns from one line cannot be used to decode another.
- The ten signal patterns on each line represent all ten distinct digits (0–9) exactly once — but Part 1 does not require this property.
- Real puzzle input is typically **200 lines** (200 individual display entries).

### Data Structures in Description
- Each line splits into two halves on `|`: a list of 10 signal-pattern strings and a list of 4 output-value strings.
- Tokens are space-delimited strings of 2–7 characters each.

---

## Algorithm Approach

### Recommended Algorithm — CONFIRMED

**Algorithm class:** Simple Frequency Count / String Parsing

**Rationale:** Part 1 requires no cryptanalysis of the scrambled wiring whatsoever — it is purely a **segment-count filter**. For every entry, split on `|`, tokenise the right-hand side on whitespace, and increment a counter for each token whose `length()` is one of `{2, 3, 4, 7}`. Accumulate across all lines and return the total.

No mapping, set operations, or logical deduction is needed for Part 1. The algorithm is O(N) in the number of input lines, where N ≈ 200 — trivially fast.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Full wiring deduction (constraint-solving) | Solves both parts but is dramatically over-engineered for Part 1; save for Part 2. |
| Regex matching on token length | Functionally equivalent to `String.length()` check; no benefit, slightly less readable. |

### Known Pitfalls
- **Trailing/leading whitespace** around the `|` delimiter — use `trim()` or `strip()` after splitting.
- **Token ordering is arbitrary** — segment letters within each token can appear in any order; Part 1 does not need to sort them, but Part 2 will.
- **The single-entry example** deliberately produces 0 easy digits (all output tokens have 5 segments) — a naïve test that expects 1 would give a false positive.
- Do not confuse the **ten signal patterns** (left of `|`) with the **four output digits** (right of `|`); Part 1 counts only the output side.
- Java `String.split("\\s+")` on a string that starts with whitespace can produce an empty leading token — use `strip()` before splitting.

---

## Edge Cases

- **Zero easy digits in output** (as in the single-entry example): the counter must not be initialised per-entry; a global accumulator is correct.
- **Duplicate output tokens**: the same token appearing twice (e.g. `cdfeb cdfeb`) counts twice — this is expected and correct behaviour.
- **Mixed-case or extra characters**: the description states only lowercase `a`–`g`; no special handling needed, but defensive `toLowerCase()` is harmless.
- **Blank input lines**: depending on puzzle input formatting, guard with `line.isBlank()` or `line.isEmpty()` checks.
- **The `|` delimiter may have surrounding spaces**: always trim both halves after splitting on `\\|`.

---

## Complexity Assessment

### Input Scale
Approximately **200 lines** of puzzle input, each with 10 signal patterns + 4 output tokens. Total tokens processed: ~200 × 4 = **800 output tokens**.

### Required Time Complexity
**O(N)** where N is the number of input lines — a single linear pass is entirely sufficient. No algorithmic concern at this scale.

### Space Complexity
**O(1)** working space beyond the input list — only a running integer counter is needed.

### Naive Approach Viable?
**Yes** — the single-pass `length()`-filter approach *is* the optimal approach. No optimisation is needed for Part 1.

---

## Implementation Plan

### Input Reading
```java
// Input is already read in readInput() and passed as List<String> lines
// via the existing skeleton's Scanner-based reader.
// Alternatively, using NIO:
List<String> lines = Files.readAllLines(Paths.get(
    SevenSegmentSearchAOC2021Day8.class.getResource(
        "/2021/day8/day8_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `List<String> lines` — raw puzzle input, one entry per element.
- `int count` — running total of easy-digit occurrences across all output values.
- `Set<Integer> uniqueLengths = Set.of(2, 3, 4, 7)` — the four uniquely-identifying segment counts (optional but readable).

### solvePartOne Outline
1. Initialise an integer counter to zero.
2. For each line, split on `"\\|"` to isolate the output-value half (index `[1]`).
3. Strip and split the output half on `"\\s+"` to obtain the four output tokens.
4. For each token, if its `length()` is `2`, `3`, `4`, or `7`, increment the counter.
5. Return the final counter as a `String`.

### solvePartTwo Outline
Implement after Part 1 submission. Part 2 requires full wiring deduction for each display to produce a decoded four-digit number, then summing all 200 decoded values. A constraint-propagation / set-intersection approach is expected.

### Helper Methods
- `parseOutputTokens(String line)` — splits a line on `|` and returns the four output tokens as a `String[]`.
- Optionally `isUniqueSegmentCount(int len)` — returns `true` for lengths `{2, 3, 4, 7}`.

### Parsing Notes
- Split each line on `"\\|"` (escaped pipe) — Java's `split` requires this.
- Always call `.strip()` on both halves before further splitting; puzzle input commonly includes a trailing space before `|`.
- `String.split("\\s+")` is preferred over `split(" ")` to handle any multi-space gaps.
- The ten signal patterns (left of `|`) are **not needed** for Part 1 and can be discarded.

---

## Implementation Summary

### Year and Day
2021 Day 8

### Algorithm Applied
Linear scan of the output side (right of `|`) on each input line. For every whitespace-delimited token, check whether its `String.length()` is a member of the named constant set `UNIQUE_SEGMENT_LENGTHS = {2, 3, 4, 7}`. Accumulate a single integer counter across all 200 lines and return it as a `String`. No wiring deduction is required for Part 1.

No deviations from the recommended algorithm.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| SevenSegmentSearchAOC2021Day8Test | givenLargerExampleInput_solvePartOne_returns26() | Unit | Fails before fix (private access compile error) → Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| SevenSegmentSearchAOC2021Day8.java | Implemented `solvePartOne`: segment-count filter on output tokens; widened method access from `private` to package-private; added `UNIQUE_SEGMENT_LENGTHS` named constant; full JavaDoc |
| SevenSegmentSearchAOC2021Day8.java | `solvePartTwo` left as original stub — returns `"not implemented"` (prints `Part 2: not implemented`) |
| SevenSegmentSearchAOC2021Day8Test.java | Created with 10-entry example-based unit test asserting result `"26"` |

### Output Format Verified
- Part 1 output: `Part 1: 452` ✓
- Part 2 output: `Part 2: not implemented` ✓ (stub as required)

### Puzzle Run Result
```
Part 1: 452
Part 2: not implemented
```

### Deviations from Analysis
None. The implementation follows the recommended algorithm exactly: split on `\\|`, strip and split the right-hand side on `\\s+`, count tokens whose length is in `{2, 3, 4, 7}`.

Note: the analysis document's per-line count table contains a minor discrepancy for line 9 (`gbdfcae bgc cg cgb` — both `bgc` and `cgb` are length 3 and should each be counted, giving 4 easy digits on that line, not 3 as stated in the table). The declared total of **26** is still correct and the implementation produces that result.  
`BaseTest` and `AxonTestTags` are not present in this project; the test was written as a plain JUnit 5 class, matching the established project convention.

### Recommended Follow-up
- [x] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"` — answer 452 accepted
- [x] Implement Part 2 — completed; answer 1096964

---

## Part 2 Implementation Summary

### Year and Day
2021 Day 8

### Algorithm Applied
Constraint-propagation / set-intersection deduction per display line:
1. Anchor digits 1, 4, 7, 8 from unique segment counts (`len` 2, 4, 3, 7).
2. Resolve six-segment group: digit 6 (does not `containsAll` of digit 1's wires); digit 9 (`containsAll` of digit 4's wires); digit 0 (remainder).
3. Resolve five-segment group: digit 3 (`containsAll` of digit 1's wires); digit 5 (`six.containsAll(candidate)`); digit 2 (remainder).
4. Build `Map<String, Integer>` keyed on alphabetically-sorted pattern characters; decode four output tokens; accumulate sum.

No deviations from the recommended algorithm.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| SevenSegmentSearchAOC2021Day8Test | givenLargerExampleInput_solvePartTwo_returns61229() | Unit | Fails before fix (private access compile error) → Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| SevenSegmentSearchAOC2021Day8.java | Implemented `solvePartTwo`: full constraint-propagation deduction; widened access from `private` to package-private; added named constants `LEN_DIGIT_ONE/SEVEN/FOUR/EIGHT`, `LEN_FIVE_SEGMENT`, `LEN_SIX_SEGMENT`, `MULTIPLIER_THOUSANDS/HUNDREDS/TENS`, `OUTPUT_TOKEN_COUNT`; added helpers `toCharSet`, `sortChars(Set)`, `sortChars(String)`; added imports `Arrays`, `HashMap`, `HashSet`, `Map` |
| SevenSegmentSearchAOC2021Day8Test.java | Added `givenLargerExampleInput_solvePartTwo_returns61229()` using shared `TEN_ENTRY_EXAMPLE` constant |

### Output Format Verified
- Part 1 output: `Part 1: 452` ✓
- Part 2 output: `Part 2: 1096964` ✓

### Puzzle Run Result
```
Part 1: 452
Part 2: 1096964
```

### Deviations from Analysis
None. Deduction order, `containsAll` direction, character-sort normalisation, and blank-line guard all implemented exactly as specified in the analysis.

### Recommended Follow-up
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`

---

## Review

### Review Cycle
4

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-001 | Convention | Minor | SevenSegmentSearchAOC2021Day8.java | Blank line between multi-paragraph description and `@param` tag in `solvePartOne` JavaDoc — violates the "no blank line between description and tags" rule |
| RV-002 | Convention | Major | SevenSegmentSearchAOC2021Day8.java | Missing `@NotNull` annotation on the `input` parameter of `solvePartOne`; `org.jetbrains.annotations.NotNull` import is also absent — project convention (per reference solutions such as `MemoryManeuverAOC2018Day8`) requires `@NotNull` on non-null `List<String>` parameters |
| RV-003 | Convention | Minor | SevenSegmentSearchAOC2021Day8.java | Line 89 uses `new java.util.ArrayList<>()` as a fully-qualified inline class reference instead of a named import — all concrete class references should appear as explicit `import` declarations at the top of the file, not as inline FQNs |
| RV-004 | Convention | Minor | SevenSegmentSearchAOC2021Day8.java | `solvePartTwo(List<String> input)` does not annotate its `input` parameter with `@NotNull`. The project convention established in RV-002 requires every non-null `List<String>` parameter to carry `@NotNull`; this applies to `solvePartTwo` as well. The import is already present. |

### Fix Packets

#### Fix Packet - Cycle 1
**Issues in this pass:** RV-001, RV-002

##### [RV-001] - Convention - Minor
**File:** SevenSegmentSearchAOC2021Day8.java  
**Issue:** The `solvePartOne` JavaDoc contains a blank comment line (`*`) between the closing sentence of the `<p>` paragraph and the `@param` tag. The project convention is "no blank line between description and tags".  
**Resolution guidance:** Remove the blank `*` line immediately before `@param input` in the `solvePartOne` Javadoc so the last description line and the first tag are adjacent.

##### [RV-002] - Convention - Major
**File:** SevenSegmentSearchAOC2021Day8.java  
**Issue:** `solvePartOne(List<String> input)` does not annotate its `input` parameter with `@NotNull`. The established project convention — demonstrated by `MemoryManeuverAOC2018Day8.parseInput`, `sumMetadata`, and `nodeValue` — is to mark every non-null method parameter with `@NotNull`.  The `org.jetbrains.annotations.NotNull` import statement is also missing.  
**Resolution guidance:** Add `import org.jetbrains.annotations.NotNull;` to the import block, then annotate the `input` parameter of `solvePartOne` with `@NotNull`.

#### Fix Packet - Cycle 2
**Issues in this pass:** RV-003

##### [RV-003] - Convention - Minor
**File:** SevenSegmentSearchAOC2021Day8.java  
**Issue:** Inside `readInput()`, line 89 instantiates `ArrayList` using its fully-qualified name: `new java.util.ArrayList<>()`. Project convention is to declare all class references via explicit named `import` statements at the top of the file and use simple names in the method body.  
**Resolution guidance:** Add `import java.util.ArrayList;` to the import block (alongside the other `java.util.*` imports) and replace `new java.util.ArrayList<>()` with `new ArrayList<>()` in the method body.

#### Fix Packet - Cycle 3
**Issues in this pass:** RV-004

##### [RV-004] - Convention - Minor
**File:** SevenSegmentSearchAOC2021Day8.java  
**Issue:** `solvePartTwo(List<String> input)` does not annotate its `input` parameter with `@NotNull`. The convention established in Cycle 1 (RV-002) requires every non-null `List<String>` parameter to carry `@NotNull`, regardless of whether the method is a stub or whether the parameter is currently used. The `org.jetbrains.annotations.NotNull` import is already present at line 10.  
**Resolution guidance:** Add `@NotNull` before `List<String> input` in the `solvePartTwo` method signature. No import change is needed.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | 0 | 2 issues queued (RV-001, RV-002) |
| 1 | Fix applied (RV-001, RV-002) | 2 | 0 |
| 2 | Re-review after Cycle 1 fixes | RV-001 ✓, RV-002 ✓ | 1 new issue queued (RV-003) |
| 2 | Fix applied (RV-003) | 1 | 0 |
| 3 | Re-review after Cycle 2 fixes | RV-003 ✓ | 1 new issue queued (RV-004) |
| 3 | Fix applied (RV-004) | 1 | 0 |
| 4 | Re-review after Cycle 3 fixes | RV-004 ✓ — all 12 criteria passed | 0 |

### Commit Message
Add AOC 2021 Day 8 solution: Seven Segment Search - segment-count frequency filter

For Part 1, each input line is split on the pipe delimiter and only the output side
(four tokens) is examined. Any token whose character length falls in the named constant
set UNIQUE_SEGMENT_LENGTHS {2, 3, 4, 7} unambiguously corresponds to digit 1, 7, 4, or 8
respectively, since no other digit shares those segment counts. A single linear pass
accumulates a running counter, making the solution O(N) with trivial constant factors
for the ~200-line real input. Part 2 is stubbed pending the full wiring-deduction phase.

---

## Fix Implementation Summary

### Fix Cycle
Cycle 1

### Fixes Applied
| ID | Severity | Resolution |
|---|---|---|
| RV-001 | Minor | Removed the blank `*` line immediately before `@param input` in the `solvePartOne` JavaDoc; the last description line and the first `@param` tag are now adjacent with no intervening blank line. |
| RV-002 | Major | Added `import org.jetbrains.annotations.NotNull;` to the import block (after `java.util.Set`, before the blank line); annotated the `input` parameter of `solvePartOne` with `@NotNull`. |

### Changes Made
| File | Change description |
|---|---|
| SevenSegmentSearchAOC2021Day8.java | RV-001: Removed blank `*` line between `<p>` description and `@param` in `solvePartOne` JavaDoc |
| SevenSegmentSearchAOC2021Day8.java | RV-002: Added `import org.jetbrains.annotations.NotNull;` and `@NotNull` annotation on `input` parameter of `solvePartOne` |

### Verification
- Tests (`odogwudozilla.year2021.day8.*`): **BUILD SUCCESSFUL** — all tests pass.
- Puzzle run: `Part 1: 452` ✓ — answer unchanged.
- No compilation errors.

### Deviations from Fix Packet
None. Both fixes applied exactly as specified.

---

## Fix Implementation Summary

### Fix Cycle
Cycle 2

### Fixes Applied
| ID | Severity | Resolution |
|---|---|---|
| RV-003 | Minor | Added `import java.util.ArrayList;` to the import block (line 6, alphabetically before `java.util.List`); replaced `new java.util.ArrayList<>()` with `new ArrayList<>()` in the `readInput()` method body. |

### Changes Made
| File | Change description |
|---|---|
| SevenSegmentSearchAOC2021Day8.java | RV-003: Added `import java.util.ArrayList;` (alphabetically before `List` in the `java.util` import block) and replaced the fully-qualified inline reference `new java.util.ArrayList<>()` with the simple name `new ArrayList<>()` in `readInput()` |

### Verification
- Tests (`odogwudozilla.year2021.day8.*`): **BUILD SUCCESSFUL** — all tests pass.
- Puzzle run: `Part 1: 452` ✓ — answer unchanged.
- No compilation errors.

### Deviations from Fix Packet
None. Fix applied exactly as specified.

---

## Part 2 Requirements

### Problem Statement
For each display entry, fully deduce the scrambled wire-to-segment mapping by applying logical set-intersection constraints to the ten signal patterns. Use the recovered mapping to decode the four-digit output value, then sum all decoded output values across every entry in the puzzle input.

### Example Test Cases

**Single-entry example:**
```
acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
```
Mapping recovered:
```
 dddd
e    a
e    a
 ffff
g    b
g    b
 cccc
```
| Signal pattern | Decoded digit |
|---|---|
| `acedgfb` | 8 |
| `cdfbe`   | 5 |
| `gcdfa`   | 2 |
| `fbcad`   | 3 |
| `dab`     | 7 |
| `cefabd`  | 9 |
| `cdfgeb`  | 6 |
| `eafb`    | 4 |
| `cagedb`  | 0 |
| `ab`      | 1 |

Output tokens: `cdfeb`→5, `fcadb`→3, `cdfeb`→5, `cdbaf`→3 → **5353**

**Larger 10-entry example:**
| Output tokens (left of `|` not shown) | Decoded value |
|---|---|
| `fdgacbe cefdb cefbgd gcbe` | 8394 |
| `fcgedb cgb dgebacf gc` | 9781 |
| `cg cg fdcagb cbg` | 1197 |
| `efabcd cedba gadfec cb` | 9361 |
| `gecf egdcabf bgf bfgea` | 4873 |
| `gebdcfa ecba ca fadegcb` | 8418 |
| `cefg dcbef fcge gbcadfe` | 4548 |
| `ed bcgafe cdgba cbgef` | 1625 |
| `gbdfcae bgc cg cgb` | 8717 |
| `fgae cfgab fg bagce` | 4315 |
| **Sum** | **61229** |

**Expected output for Part 2 on the larger example: `61229`.**

### Constraints
- All constraints from Part 1 apply (10 patterns + 4 output tokens per line, letters `a`–`g` only, independent scramble per display).
- The ten signal patterns on each line are guaranteed to contain every digit 0–9 exactly once — this is the property that makes unique deduction possible.
- Character order within any token is arbitrary; tokens must be treated as **sets** of characters.
- ~200 input lines in the real puzzle.

### Data Structures
- Each input line parsed into: 10 signal-pattern strings (left of `|`) + 4 output-token strings (right of `|`).
- Signal patterns treated as `Set<Character>` for set-intersection operations.
- A `Map<String, Integer>` keyed on **sorted** signal-pattern characters → digit value, built once per line and used to decode the four output tokens.

### How Part 2 Differs from Part 1
Part 1 needed only a segment-count filter applied to the output side; **no deduction** was required. Part 2 requires full constraint-solving of the wire scramble using all ten signal patterns (both sides of `|`), then applying the recovered mapping to decode the four output tokens and accumulate a numeric sum. The Part 1 algorithm is not reusable beyond its parsing scaffolding.

---

## Part 2 Algorithm Approach

### Recommended Algorithm — CONFIRMED
**Algorithm class:** Constraint Propagation / Set-Intersection Deduction

**Rationale:** The ten signal patterns plus the known segment-count facts about each digit form a small, fully-deterministic constraint system. A fixed sequence of seven set operations (using the already-identified digit-1, digit-4, and digit-7 patterns as anchors) resolves every ambiguous group without any backtracking or brute-force search, making the approach O(N) on input lines with constant work per line.

### Deduction Sequence (per display line)

Work from the unambiguous to the ambiguous using segment-count buckets and set containment:

1. **Anchor identification** (from Part 1's length heuristic — reused):
   - `len=2` → digit **1** (segments `{c, f}`)
   - `len=3` → digit **7** (segments `{a, c, f}`)
   - `len=4` → digit **4** (segments `{b, c, d, f}`)
   - `len=7` → digit **8** (all segments)

2. **Six-segment group** (`len=6` → candidates for **0, 6, 9**):
   - **Digit 6**: the only `len=6` pattern that does **not** contain both characters of digit 1 (i.e. `!pattern.containsAll(one)` — missing the `c` wire).
   - **Digit 9**: among the remaining two, the one that contains **all** characters of digit 4 (`pattern.containsAll(four)`).
   - **Digit 0**: the sole remaining `len=6` pattern.

3. **Five-segment group** (`len=5` → candidates for **2, 3, 5**):
   - **Digit 3**: the only `len=5` pattern that contains **both** characters of digit 1 (`pattern.containsAll(one)`).
   - **Digit 5**: among the remaining two, the one whose characters are **all present** in digit 6 (`six.containsAll(pattern)`).
   - **Digit 2**: the sole remaining `len=5` pattern.

4. **Build lookup map**: for each identified digit, sort its characters alphabetically → map the resulting `String` key to its `int` digit value.

5. **Decode output**: for each of the four output tokens, sort its characters and look up in the map; assemble into a four-digit integer (`d0×1000 + d1×100 + d2×10 + d3`).

6. **Accumulate sum** across all lines and return as a `String`.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Brute-force permutation of 7! wire mappings | Guarantees correctness but is 5040× slower per line than constraint propagation; viable given only ~200 lines, but unnecessarily complex. |
| Frequency-based segment analysis (count how often each wire appears across all 10 patterns) | Identifies individual wire roles via known segment frequencies (e.g. `b` appears 6 times in canonical truth table), but requires careful handling of the remaining ambiguous pair `(c, f)` → more error-prone than set-intersection approach. |
| Full SAT / constraint-solver library | Overkill — the system is always fully determined by the seven-step deduction sequence. |

---

## Part 2 Edge Cases

- **Character order within tokens is arbitrary**: `gcbe` and `bcge` represent the same set of segments. Always sort characters before building map keys and before looking up output tokens. Use `chars().sorted()` collect to `String` or an equivalent normalisation.
- **Output tokens share no guaranteed ordering with signal patterns**: the output-side tokens are not necessarily a subset of the left-side patterns as written, but they will always match one of them after character sorting. Sort both sides independently.
- **Repeated output digits**: two or more output tokens may decode to the same digit (e.g. both `cdfeb` occurrences decode to 5 in the single-entry example). This is expected — do not deduplicate.
- **Deduction order matters**: the six-segment group must be fully resolved before the five-segment group, because the digit-6 set is used as an anchor for identifying digit 5. Resolve anchors → 6-segment group → 5-segment group, in that order.
- **`containsAll` direction**: when checking "digit 5's segments are all within digit 6", call `six.containsAll(five_candidate)` not the reverse. Reversing it silently produces wrong deductions for some scrambles.
- **Map key collision**: if character-sorted tokens are not normalised consistently (e.g. lowercase vs. uppercase), lookups will silently fail. Enforce a single canonical form (`toLowerCase()` + sort) throughout.
- **Blank input lines**: guard with `line.isBlank()` to avoid `ArrayIndexOutOfBoundsException` on the `split("\\|")` step, consistent with Part 1 advice.
- **Integer overflow**: each decoded value is at most 9999; summing 200 of them yields at most 1,999,800 — safely within `int` range. No `long` is required, though using it is harmless.

---

## Pipeline Handoff

### Year
2021

### Day
8

### Puzzle Title
Seven Segment Search

### Skeleton Class
`src/main/java/odogwudozilla/year2021/day8/SevenSegmentSearchAOC2021Day8.java`

### Puzzle Input File
`src/main/resources/2021/day8/day8_puzzle_data.txt`

### Workflow Stage
B2 — Part 2 Analysis complete (consumed by @solution-implementer)

### Part 1 Answer (accepted)
452

### Recommended Algorithm — Part 2
Constraint-propagation / set-intersection deduction: anchor on digit-1 (len=2), digit-4 (len=4), digit-7 (len=3); resolve the six-segment group (digits 0, 6, 9) then the five-segment group (digits 2, 3, 5) via `Set.containsAll` checks; build a sorted-character-string → digit map per line; decode the four output tokens and accumulate the integer sum.

### Part 2 Status
Available — description read in full; 2 example cases extracted (single entry → 5353; 10-entry set → 61229).

### Section Anchors
- Part 1 requirements → `## Part 1 Requirements`
- Algorithm approach (Part 1) → `## Algorithm Approach`
- Implementation plan (Part 1) → `## Implementation Plan`
- Part 2 requirements → `## Part 2 Requirements`
- Part 2 algorithm approach → `## Part 2 Algorithm Approach`
- Part 2 edge cases → `## Part 2 Edge Cases`

---

## Review (Part 2)

### Review Cycle
1

### Status
ISSUES_FOUND

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-P2-001 | Convention | Minor | SevenSegmentSearchAOC2021Day8.java | Magic number `1` in the `multipliers` array literal (line 192). The three higher-order positional multipliers are extracted as named constants (`MULTIPLIER_THOUSANDS`, `MULTIPLIER_HUNDREDS`, `MULTIPLIER_TENS`), but the units-position value is the bare literal `1`. A `MULTIPLIER_UNITS = 1` constant is required for consistency. |

### Fix Packets

#### Fix Packet — Part 2 Cycle 1
**Issues in this pass:** RV-P2-001

##### [RV-P2-001] — Convention — Minor
**File:** SevenSegmentSearchAOC2021Day8.java
**Issue:** Inside `solvePartTwo`, line 192 declares:
```java
int[] multipliers = {MULTIPLIER_THOUSANDS, MULTIPLIER_HUNDREDS, MULTIPLIER_TENS, 1};
```
The three larger positional multipliers are already named constants, but the units-position value `1` is a bare numeric literal. This violates the "no magic numbers" convention.
**Resolution guidance:** Add a new `private static final int MULTIPLIER_UNITS = 1;` constant (alongside the other `MULTIPLIER_*` constants, with an appropriate Javadoc comment such as `/** Positional multiplier for the units digit in a four-digit output value. */`), then replace the literal `1` in the array with `MULTIPLIER_UNITS`.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 (Part 2) | Initial Part 2 review — Part 1 sanity check passed; all other Part 2 criteria passed | 0 | 1 issue queued (RV-P2-001) |
| 1 (Part 2) | Fix applied (RV-P2-001) | 1 | 0 |
| 2 (Part 2) | Re-review after Part 2 Cycle 1 fix — full criteria sweep; RV-P2-001 verified; 1 new issue found (RV-P2-002) | RV-P2-001 ✓ | 1 issue queued (RV-P2-002) |

### Commit Message (Part 2)
Add AOC 2021 Day 8 solution: Seven Segment Search - segment-count filter and constraint-propagation deduction

Part 1 applies a linear segment-count filter to the output side of each input line,
counting tokens whose length falls in the named constant set UNIQUE_SEGMENT_LENGTHS
{2, 3, 4, 7} — corresponding uniquely to digits 1, 7, 4, and 8 respectively. Part 2
performs a full per-line constraint-propagation deduction: anchor the four unique-length
digits, resolve the six-segment group {0, 6, 9} via Set.containsAll checks against digit 1
and digit 4, then resolve the five-segment group {2, 3, 5} using digit 1 and the recovered
digit 6 as anchors. A sorted-character-string map keys each identified pattern to its digit
value, enabling O(1) lookup for the four output tokens. Both parts run in O(N) time on the
~200-line real input.

---

## Review (Part 2) — Cycle 3

### Review Cycle
3

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-P2-002 | Convention | Minor | SevenSegmentSearchAOC2021Day8.java | ✅ VERIFIED — `UNIQUE_SEGMENT_LENGTHS` now uses named constants `Set.of(LEN_DIGIT_ONE, LEN_DIGIT_SEVEN, LEN_DIGIT_FOUR, LEN_DIGIT_EIGHT)`; `LEN_DIGIT_*` constants are declared before `UNIQUE_SEGMENT_LENGTHS` (lines 26–36 precede line 47). No magic numbers remain. |

### Full Criteria Sweep — Cycle 3 Results
| Category | Check | Result |
|---|---|---|
| Correctness | `main()` calls both `solvePartOne` and `solvePartTwo`, prints results | ✅ Pass |
| Correctness | Part 1 produces `"26"` for 10-entry example | ✅ Pass |
| Correctness | Part 2 produces `"61229"` for 10-entry example | ✅ Pass |
| Correctness | Puzzle run answers match accepted values (452, 1096964) | ✅ Pass |
| Output Format | `"Part 1: "` prefix on line 66 | ✅ Pass |
| Output Format | `"Part 2: "` prefix on line 67 | ✅ Pass |
| Resource Reading | Input read via `getResourceAsStream(INPUT_FILE)` from classpath | ✅ Pass |
| Resource Reading | No hardcoded absolute paths | ✅ Pass |
| Naming | Class `SevenSegmentSearchAOC2021Day8` | ✅ Pass |
| Naming | Package `odogwudozilla.year2021.day8` | ✅ Pass |
| Naming | Methods `solvePartOne`, `solvePartTwo` (exact spelling) | ✅ Pass |
| Magic Numbers | All segment-count and positional-multiplier literals behind named constants | ✅ Pass |
| Magic Numbers | `UNIQUE_SEGMENT_LENGTHS` initialised with `LEN_DIGIT_*` named constants | ✅ Pass (RV-P2-002 fix verified) |
| Magic Numbers | Digit values 0–9 used as map values — inherently self-documenting | ✅ Acceptable |
| Wildcard Imports | Source file — no wildcard imports | ✅ Pass |
| Wildcard Imports | Test file — `import static org.junit.jupiter.api.Assertions.assertEquals` (specific) | ✅ Pass |
| @NotNull | `solvePartOne(@NotNull List<String> input)` | ✅ Pass |
| @NotNull | `solvePartTwo(@NotNull List<String> input)` | ✅ Pass |
| @NotNull | `toCharSet(@NotNull String pattern)` | ✅ Pass |
| @NotNull | `sortChars(@NotNull Set<Character> chars)` | ✅ Pass |
| @NotNull | `sortChars(@NotNull String token)` | ✅ Pass |
| JavaDoc | All public/package-private/private methods have JavaDoc | ✅ Pass |
| JavaDoc | No blank line between description and `@param` tag in any method | ✅ Pass |
| Algorithm Efficiency | Part 1: O(N) linear pass, N ≈ 200 | ✅ Pass |
| Algorithm Efficiency | Part 2: O(N × 10) constant-work per line; no backtracking | ✅ Pass |
| Test Quality | `TEN_ENTRY_EXAMPLE` from puzzle description used for both tests | ✅ Pass |
| Test Quality | Part 1 asserts `"26"`, Part 2 asserts `"61229"` | ✅ Pass |
| Test Quality | Test names clearly describe scenario and expected outcome | ✅ Pass |
| Compilation | No errors in source or test | ✅ Pass |

---

## Review (Part 2) — Cycle 2

### Review Cycle
2

### Status
ISSUES_FOUND

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-P2-002 | Convention | Minor | SevenSegmentSearchAOC2021Day8.java | `UNIQUE_SEGMENT_LENGTHS = Set.of(2, 3, 4, 7)` uses bare numeric literals where named constants `LEN_DIGIT_ONE` (2), `LEN_DIGIT_SEVEN` (3), `LEN_DIGIT_FOUR` (4), and `LEN_DIGIT_EIGHT` (7) already exist in the same class. Violates the "no magic numbers" convention. |

### Fix Packets

#### Fix Packet — Part 2 Cycle 2
**Issues in this pass:** RV-P2-002

##### [RV-P2-002] — Convention — Minor
**File:** SevenSegmentSearchAOC2021Day8.java  
**Issue:** Line 34 initialises `UNIQUE_SEGMENT_LENGTHS` using bare integer literals:
```java
private static final Set<Integer> UNIQUE_SEGMENT_LENGTHS = Set.of(2, 3, 4, 7);
```
The class already declares `LEN_DIGIT_ONE = 2`, `LEN_DIGIT_SEVEN = 3`, `LEN_DIGIT_FOUR = 4`, and `LEN_DIGIT_EIGHT = 7` as named constants (lines 37–43). Using the bare literals here is inconsistent with the project's "no magic numbers" convention; the named constants exist precisely to avoid this.  
**Resolution guidance:** Replace the four numeric literals in `Set.of(...)` with the corresponding named constants: `Set.of(LEN_DIGIT_ONE, LEN_DIGIT_SEVEN, LEN_DIGIT_FOUR, LEN_DIGIT_EIGHT)`. Because all four `LEN_DIGIT_*` constants are compile-time constants (`static final int` with literal initialisers), Java permits their forward reference here even though their declarations appear after `UNIQUE_SEGMENT_LENGTHS` in the source. No other changes are needed.

### Review History (Part 2)
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 (Part 2) | Initial Part 2 review — RV-P2-001 queued | 0 | 1 (RV-P2-001) |
| 1 (Part 2) | Fix applied (RV-P2-001) | 1 | 0 |
| 2 (Part 2) | Re-review — RV-P2-001 verified; new issue RV-P2-002 found | RV-P2-001 ✓ | 1 (RV-P2-002) |
| 2 (Part 2) | Fix applied (RV-P2-002) | 1 | 0 |
| 3 (Part 2) | Re-review — RV-P2-002 verified; full 12-criteria sweep passed | RV-P2-002 ✓ | 0 |

### Commit Message (Part 2)
Add AOC 2021 Day 8 solution: Seven Segment Search - segment-count filter and constraint-propagation deduction

Part 1 applies a linear segment-count filter to the output side of each input line,
counting tokens whose length falls in the named constant set UNIQUE_SEGMENT_LENGTHS
{2, 3, 4, 7} — corresponding uniquely to digits 1, 7, 4, and 8 respectively. Part 2
performs a full per-line constraint-propagation deduction: anchor the four unique-length
digits, resolve the six-segment group {0, 6, 9} via Set.containsAll checks against digit 1
and digit 4, then resolve the five-segment group {2, 3, 5} using digit 1 and the recovered
digit 6 as anchors. A sorted-character-string map keys each identified pattern to its digit
value, enabling O(1) lookup for the four output tokens. Both parts run in O(N) time on the
~200-line real input.






