# 2023 Day 15 - Lens Library - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2023 Day 15

### Puzzle Title
Lens Library

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @solution-implementer | RV-001 fix update in ## Implementation Summary |
| 5 | @solution-reviewer | ## Review (Cycle 2 re-review verification) |
| 6 | @puzzle-analyser | ## Part 2 Analysis Update and refreshed ## Pipeline Handoff |
| 7 | @solution-implementer | Part 2 implementation update in ## Implementation Summary |
| 8 | @solution-reviewer | ## Review (Phase B Cycle 1) |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2023-day15/2023-day15-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given one comma-separated initialisation sequence, run the specified HASH procedure on each step string and return the sum of all per-step hash values. Parsing must ignore newline characters so the sequence is treated as one continuous comma-delimited stream.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| `HASH` | `52` | Direct worked example of the HASH algorithm. |
| `rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7` | `1320` | Part 1 verification sum for the full example sequence. |
| `rn=1` | `30` | Listed per-step hash value in the worked breakdown. |
| `cm-` | `253` | Listed per-step hash value in the worked breakdown. |
| `qp=3` | `97` | Listed per-step hash value in the worked breakdown. |
| `cm=2` | `47` | Listed per-step hash value in the worked breakdown. |
| `qp-` | `14` | Listed per-step hash value in the worked breakdown. |
| `pc=4` | `180` | Listed per-step hash value in the worked breakdown. |
| `ot=9` | `9` | Listed per-step hash value in the worked breakdown. |
| `ab=5` | `197` | Listed per-step hash value in the worked breakdown. |
| `pc-` | `48` | Listed per-step hash value in the worked breakdown. |
| `pc=6` | `214` | Listed per-step hash value in the worked breakdown. |
| `ot=7` | `231` | Listed per-step hash value in the worked breakdown. |

### Constraints
- HASH starts at `0` and, for each character, applies: add ASCII code, multiply by `17`, then modulo `256`.
- Each hash result is always in the range `0..255`.
- Puzzle input is a comma-separated list of step strings.
- Newline characters must be ignored when parsing the sequence.
- The initialisation sequence is effectively one long line.

### Data Structures in Description
- Comma-separated list of strings (steps).
- Character stream per step for deterministic state update.

---

## Part 2 Requirements

### Problem Statement
Not analysed in this run (user requested Part 1 only).

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| N/A | N/A | Part 2 intentionally out of scope for this analysis. |

### How Part 2 Differs from Part 1
N/A (Part 1-only scope).

---

## Algorithm Approach

### Recommended Algorithm - CONFIRMED
**Algorithm class:** Simulation / string processing  
**Rationale:** The puzzle explicitly defines a fixed per-character state transition, so a single pass over each step string and a running sum directly matches the specification.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Java Streams pipeline over split steps and chars | More concise, but creates extra objects and is less transparent for debugging each hash stage. |
| Regex-based tokenisation first, then hashing | Unnecessary complexity because commas are the only delimiter and newline removal is simple preprocessing. |

### Known Pitfalls
- Forgetting to ignore newline characters before splitting on commas.
- Applying modulo only once at the end instead of after each character update.
- Using signed/overflow-prone arithmetic carelessly (use `int`; values remain small with per-step modulo).
- Accidentally trimming or altering step text before hashing.

---

## Complexity Assessment

### Input Scale
The description does not give exact limits; AoC inputs are typically one long line with up to a few thousand comma-separated steps (total characters usually in the low tens of thousands).

### Required Time Complexity
`O(T)`, where `T` is the total number of characters across all steps, is sufficient for AoC runtime expectations.

### Space Complexity
`O(T)` if rebuilding a newline-free combined string; `O(1)` auxiliary beyond storing input lines if processed carefully.

### Naive Approach Viable?
Yes. The direct specification-driven simulation is already optimal enough; no advanced optimisation is needed.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2023/day15/day15_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `String joined` - input with newline characters removed.
- `String[] steps` - comma-separated step tokens.
- `int current` - rolling hash state for one step.
- `long total` (or `int`) - accumulator for sum of step hashes.

### solvePartOne Outline
Read all lines, concatenate without newline separators, then split by comma to obtain steps. For each step, run the HASH loop exactly as described, updating `current` per character with add/multiply/modulo in order. Add each final step hash to the running total. Return the total as a string.

### solvePartTwo Outline
Implement after Part 1 submission (outside this analysis scope).

### Helper Methods
- `computeHash(String step)` - returns hash value for one step using the defined algorithm.
- `parseInitialisationSequence(List<String> input)` - joins lines while ignoring newline characters.

### Parsing Notes
- Do not insert delimiter characters while joining lines; newline characters should effectively vanish.
- Split strictly on commas.
- Preserve each token exactly; no whitespace normalisation unless explicitly present in the input format.

---

## Pipeline Handoff

### Year
2023

### Day
15

### Puzzle Title
Lens Library

### Skeleton Class
`src/main/java/odogwudozilla/year2023/day15/LensLibraryAOC2023Day15.java`

### Puzzle Input File
`src/main/resources/2023/day15/day15_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Single-pass per-step HASH simulation with per-character modulo updates and summed outputs.

### Part 2 Status
Not yet available in this analysis output (Part 1-only run).

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

---

## Output to: `@solution-implementer`

- Puzzle: 2023 Day 15 - Lens Library
- Algorithm: Single-pass simulation of the HASH procedure for each comma-delimited step, summing results
- Part 2 available: No
- Example cases: 13 extracted

Saved to: docs/ai-output/puzzle-analysis/2023-day15/2023-day15-analysis.md (see ## Implementation Plan for full detail)

## Implementation Summary

### Year and Day
2023 Day 15

### Algorithm Applied
Part 1: Single-pass per-step HASH simulation with per-character add/multiply/modulo updates, after joining input lines without newline separators.  
Part 2: Stateful simulation of 256 ordered boxes using `LinkedHashMap<String, Integer>` per box, with hash(label)-based routing and focusing-power aggregation.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| LensLibraryAOC2023Day15Test | givenSingleHashToken_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| LensLibraryAOC2023Day15Test | givenWorkedExampleSequence_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| LensLibraryAOC2023Day15Test | givenWorkedExampleSplitAcrossLines_solvePartOne_returnsExpectedValue() | Unit | Added for RV-001 -> Passes |
| LensLibraryAOC2023Day15Test | givenWorkedExampleSequence_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| LensLibraryAOC2023Day15Test | givenWorkedExampleSplitAcrossLines_solvePartTwo_returnsExpectedValue() | Unit | Added with Part 2 implementation -> Passes |

### Changes Made
| File | Change description |
|---|---|
| LensLibraryAOC2023Day15.java | Implemented solvePartOne using input join, comma split, HASH helper, and summed hash values. |
| LensLibraryAOC2023Day15.java | Implemented solvePartTwo with 256 `LinkedHashMap` boxes, parsed remove/assign operations, applied in-place replacement/removal semantics, and computed focusing power. |
| LensLibraryAOC2023Day15Test.java | Created example-based unit tests for Part 1 HASH behaviour and full worked sequence. |
| LensLibraryAOC2023Day15Test.java | Added RV-001 regression test verifying worked example split across multiple input lines still returns `1320`. |
| LensLibraryAOC2023Day15Test.java | Added Part 2 example tests for full worked sequence and split-line parsing, both asserting `145`. |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```
Part 1: 516469
Part 2: 221627
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`

## Review

### Review Cycle
1

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| None | - | - | - | No issues found in the Phase B review scope for Part 2 implementation and related Day 15 tests. |

### Fix Packets

None.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | 0 issues verified | 1 queued |
| 2 | Re-review of Fix Packet Cycle 1 and Part 1 stability check | 1 issue verified | 0 |
| 1 (Phase B) | Initial review of Part 2 implementation and Day 15 test scope | 0 issues queued | 0 |

### Commit Message
Add AOC 2023 Day 15 solution: Lens Library - hash-indexed ordered box simulation for focusing power

Implemented the Day 15 solver using direct simulation: Part 1 computes per-token HASH values in a single pass, while Part 2 routes lens operations into 256 hash-indexed `LinkedHashMap` boxes to preserve insertion order for replacement, removal, and final focusing-power scoring. The implementation reads input from classpath resources, preserves required output formatting, and is backed by example-based tests including split-line parsing behaviour.

### Review Status Block

#### Year and Day
2023 Day 15

#### Review Cycle
1

#### Status
ALL_VERIFIED

#### Issue Counts
- Queued for fix: 0
- Verified: 0



---

## Part 2 Analysis Update

### Problem Statement
Process the same comma-separated initialisation sequence as a series of lens operations over 256 boxes. Hash each lens label to choose a box, apply remove (`-`) or insert/replace (`=`) rules while preserving in-box order semantics, then compute the total focusing power of the final lens configuration.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| `rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7` | `145` | Final Part 2 focusing power from the official worked example. |
| `ot=7` at end-state with `Box 3: [ot 9] [ab 5] [pc 6]` | `Box 3: [ot 7] [ab 5] [pc 6]` | Replacement updates focal length in place without changing slot order. |
| `pc-` at state `Box 3: [pc 4] [ot 9] [ab 5]` | `Box 3: [ot 9] [ab 5]` | Removal compacts forward while preserving relative order. |

### Constraints
- There are always exactly 256 boxes, numbered `0` to `255`.
- For each step, hash only the **label** (letters before `-` or `=`) to determine the target box.
- `-` operation removes the matching label in that box if present; otherwise no change.
- `=` operation with focal length `1..9` replaces existing same-label lens in place, or appends if absent.
- Focusing power per lens is `(boxIndex + 1) * slotIndex(1-based) * focalLength`.

### How Part 2 Differs from Part 1
Part 1 is a stateless per-token hash sum, while Part 2 is a stateful simulation over 256 ordered boxes that uses hash(label) only for routing and then scores the final arrangement.

---

## Part 2 Algorithm Approach

### Recommended Algorithm - CONFIRMED
**Algorithm class:** Simulation with ordered associative containers  
**Rationale:** The puzzle defines deterministic state transitions (insert, replace, remove, preserve order), so direct step-by-step simulation exactly matches required behaviour.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| `List<List<Lens>>` with linear search per operation | Simpler model but potentially `O(k)` search per step per box; still viable at AoC scale. |
| Per-box `Map<label,index>` + `ArrayList` | Faster random access updates but index maintenance on removals adds complexity and bug risk. |

### Recommended Java Structure
- `List<LinkedHashMap<String, Integer>> boxes = new ArrayList<>(256);`
- One `LinkedHashMap` per box to preserve insertion order, support O(1)-average lookup/removal, and in-place focal replacement via `put` on existing key.

### Known Pitfalls
- Hashing the full token (e.g., `pc=6`) instead of just the label (`pc`) for box selection.
- Replacing an existing lens by removing and re-appending, which incorrectly changes slot order.
- Incorrect parsing of operation tokens (`label-` versus `label=<digit>`).
- Off-by-one errors in focusing-power multipliers (`box + 1`, `slot starts at 1`).

---

## Part 2 Complexity Assessment

### Input Scale
Unspecified in text; expected AoC scale is a single long line with thousands of steps and short labels.

### Required Time Complexity
`O(T + S + L)` is sufficient, where `T` is total character count for parsing, `S` is number of steps processed, and `L` is number of lenses iterated when scoring.

### Space Complexity
`O(L)` for current lens state across 256 boxes.

### Naive Approach Viable?
Yes, a straightforward simulation is viable. A small structural optimisation (ordered map per box) keeps implementation clear and robust.

---

## Part 2 Implementation Addendum

### solvePartTwo Outline
Parse the initialisation sequence exactly as in Part 1 (newline-free joined input, split by commas). For each step, parse label and operation, compute `boxIndex = computeHash(label)`, and apply removal or insert/replace in the relevant box according to the rules. After all steps, iterate boxes in index order and each box's lenses in slot order to accumulate focusing power. Return the total as a string.

### Helper Methods
- `parseStep(String token)` - extracts `label`, `operation`, and optional `focalLength`.
- `applyStep(List<LinkedHashMap<String, Integer>> boxes, Step step)` - mutates one box according to `-`/`=` semantics.
- `computeFocusingPower(List<LinkedHashMap<String, Integer>> boxes)` - folds final state into the required score.

### Parsing Notes
- Labels are alphabetic and immediately followed by `-` or `=`.
- `=` is followed by focal length digit(s); puzzle narrative states `1..9`.
- Continue to ignore newlines in input before token splitting.

---

## Pipeline Handoff

### Year
2023

### Day
15

### Puzzle Title
Lens Library

### Skeleton Class
`src/main/java/odogwudozilla/year2023/day15/LensLibraryAOC2023Day15.java`

### Puzzle Input File
`src/main/resources/2023/day15/day15_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (Part 2 update, consumed by @solution-implementer)

### Recommended Algorithm
Stateful HASHMAP simulation using 256 ordered boxes (recommended: `LinkedHashMap<String,Integer>` per box), then final focusing-power fold.

### Part 2 Status
Available and analysed.

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Part 2 update - `## Part 2 Analysis Update`
- Part 2 algorithm - `## Part 2 Algorithm Approach`
- Part 2 implementation addendum - `## Part 2 Implementation Addendum`

---

## Output to: `@solution-implementer`

- Puzzle: 2023 Day 15 - Lens Library
- Algorithm: Simulate 256 hash-indexed ordered lens boxes with in-place replace/remove/append semantics, then sum focusing power
- Part 2 available: Yes
- Example cases: 3 extracted (including final `145` verification)

Saved to: docs/ai-output/puzzle-analysis/2023-day15/2023-day15-analysis.md (see ## Part 2 Implementation Addendum for full detail)


