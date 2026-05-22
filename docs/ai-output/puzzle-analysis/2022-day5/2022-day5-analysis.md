# 2022 Day 5 - Supply Stacks - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2022 Day 5

### Puzzle Title
Supply Stacks

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Puzzle Summary, ## Part 1 Requirements, ## Algorithm Approach, ## Edge Cases, ## Implementation Plan, ## Pipeline Handoff |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @solution-implementer | ## Fix Implementation Summary — Cycle 1 |
| 5 | @solution-reviewer | ## Review — Cycle 2 (re-review) |
| 6 | @solution-reviewer | ## Review — Cycle 3 (re-review, final) |
| 7 | @puzzle-analyser | ## Part 2 Requirements (updated), ## Part 2 Algorithm Approach, ## Part 2 Edge Cases, ## Pipeline Handoff (updated to B2) |
| 8 | @solution-implementer | ## Part 2 Implementation Summary |
| 9 | @solution-reviewer | ## Review (Part 2) |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2022-day5/2022-day5-analysis.md | Consolidated analysis lifecycle file |

---

## Puzzle Summary

Crates labelled with single capital letters are arranged in numbered stacks, depicted as
a fixed-width ASCII drawing. A crane moves crates one at a time according to a list of
`move N from X to Y` instructions. After all moves are applied, the answer is the
concatenation of the top crate letter from each stack, read left-to-right in stack-number
order.

---

## Part 1 Requirements

### Problem Statement
Given a visual drawing of initial crate stacks and a list of move instructions, simulate
a crane that picks crates **one at a time** (LIFO order) from a source stack and places
them onto a destination stack. After all moves are complete, report the top-of-stack
crate letters concatenated in ascending stack-number order.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| Initial stacks: Stack 1 → [Z, N] (bottom→top), Stack 2 → [M, C, D], Stack 3 → [P]. Moves: `move 1 from 2 to 1`, `move 3 from 1 to 3`, `move 2 from 2 to 1`, `move 1 from 1 to 2` | `CMZ` | C is top of stack 1, M top of stack 2, Z top of stack 3 |

### Constraints
- Crates are moved **one at a time**; moving N crates reverses their relative order on
  the destination stack.
- Stack numbers in instructions are **1-indexed**.
- The number of stacks is determined by the label row at the bottom of the drawing (the
  line containing ` 1   2   3 ...`).
- There may be up to 9 (or more) stacks and arbitrarily tall stacks in the real input.
- A stack position in the drawing may be empty (represented by three spaces `   `).
- The drawing and the instructions are separated by a single blank line.
- Return type is `String` (concatenated letters), not a number.

### Data Structures in Description
- Stacks of crates (vertical LIFO collections)
- Fixed-width ASCII grid diagram
- Sequence of move instructions (quantity, source, destination)

---

## Part 2 Requirements

### Problem Statement
The crane is revealed to be a **CrateMover 9001**, which moves multiple crates simultaneously
rather than one at a time. Crates moved as a group **retain their original relative order**
on the destination stack (i.e. no reversal). After all moves are applied to the same initial
stacks and same instruction list, report the concatenated top-of-stack letters as before.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| Same initial stacks as Part 1 (Stack 1 → [Z,N], Stack 2 → [M,C,D], Stack 3 → [P]). Same moves: `move 1 from 2 to 1`, `move 3 from 1 to 3`, `move 2 from 2 to 1`, `move 1 from 1 to 2` | `MCD` | M top of stack 1, C top of stack 2, D top of stack 3 — order preserved across group moves |

### How Part 2 Differs from Part 1
Part 2 is a **semantics change** to the move execution only. The input format, stack parsing,
instruction parsing, and result-collection logic are all identical. The single difference is
that when moving `N` crates from source to destination:
- **Part 1 (CrateMover 9000):** pop one at a time → relative order is **reversed** on destination.
- **Part 2 (CrateMover 9001):** move as a group → relative order is **preserved** on destination.

All three helper methods (`parseStacks`, `parseMove`, `buildResult`) are directly reusable.
Only the inner move-execution loop inside `solvePartTwo` needs to change.

---

## Algorithm Approach

### Recommended Algorithm - CONFIRMED
**Algorithm class:** Simulation using Stacks (Deque)

**Rationale:** The problem is a direct stack-manipulation simulation; each `move N from X
to Y` instruction is executed by popping `N` elements one at a time from the source
`Deque<Character>` and pushing each onto the destination `Deque<Character>`. No search,
dynamic programming, or graph traversal is required — the single challenge is correct
two-phase parsing of the input (drawing section then instruction section).

The recommended approach is:
1. Split the raw input into two sections at the blank separator line.
2. Parse the drawing section column-by-column using the fixed-width formula
   `charIndex = 1 + (stackIndex * 4)` (0-based stack index), reading rows from the
   **bottom upward** (excluding the label row) to build each stack in correct bottom-to-top
   order.
3. Parse each instruction line with a simple regex or `String.split` to extract
   `(count, from, to)`.
4. Execute every move by popping `count` times from `stacks[from-1]` and pushing onto
   `stacks[to-1]`.
5. Collect the top element of each stack (in order) and concatenate into the answer string.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Read rows top-to-bottom, store intermediate lists, reverse at the end | Equally correct but requires an extra reversal step; marginally less clear |
| Regex-only parsing of the entire input | More concise for the instruction lines but overly complex for the fixed-width drawing section |

### Known Pitfalls
- **Fixed-width column parsing**: crate letters sit at character position `1 + stackIndex * 4`
  (0-based). Off-by-one errors here will silently misassign or skip crates.
- **Including the label row**: the line `" 1   2   3 "` must be skipped when parsing the
  drawing; accidentally treating a digit as a crate letter will corrupt the stacks.
- **Empty stack positions**: a position with no crate is `"   "` (three spaces); the code
  must skip these rather than pushing a space character.
- **1-indexed stack numbers**: instructions reference stacks starting at 1; Java arrays
  are 0-indexed — subtract 1 when accessing the array.
- **Return type is String**: `solvePartOne` returns `String`, not `int`/`long`.

---

## Part 2 Algorithm Approach

### Recommended Algorithm - CONFIRMED
**Algorithm class:** Simulation using Stacks (Deque) with temporary buffer

**Rationale:** The only behavioural difference from Part 1 is that moving `N` crates must
preserve their original relative order on the destination stack. Popping `N` items from the
source `ArrayDeque` into a temporary `ArrayDeque` reverses their order once; popping all
items from the temporary buffer onto the destination reverses them a second time, restoring
original order. This double-reversal is O(N) in extra work and needs no additional data
structures beyond the one temporary deque per move.

The recommended approach is:
1. Parse stacks and instructions identically to Part 1 (reuse `parseStacks` and `parseMove`).
2. For each move `(count, from, to)`, create a fresh temporary `ArrayDeque<Character>`.
3. Pop `count` items from `stacks[from - STACK_INDEX_OFFSET]` into the temporary deque
   (first pop lands at the top of the temp deque — order is now reversed).
4. Pop all items from the temporary deque onto `stacks[to - STACK_INDEX_OFFSET]`
   (second pop reverses back to original order).
5. Collect and concatenate top-of-stack letters exactly as in Part 1 (reuse `buildResult`).

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Pop into a `List`, then iterate forward and push onto destination | Equally correct and arguably more readable; uses `ArrayList` rather than a second `ArrayDeque`; marginally more allocations |
| Use `LinkedList.subList` or splice operations | Fragile and non-idiomatic with `ArrayDeque`; not recommended |
| Reverse the instruction list and apply Part 1 logic | Incorrect — instructions are order-dependent and cannot simply be reversed |

---

## Part 2 Edge Cases

- **Moving exactly 1 crate:** A single-crate move is identical in both Part 1 and Part 2
  (no order difference). The temporary-buffer approach still works correctly — one pop in,
  one pop out.
- **Moving all crates from a source stack:** After the move the source deque will be empty.
  Subsequent `peek()` calls on an empty deque return `null` — ensure `buildResult` guards
  against or skips empty stacks. (The existing `buildResult` helper from Part 1 should
  already handle this if it calls `peek()` on each deque and skips `null`.)
- **Fresh temporary buffer per move:** The temporary `ArrayDeque` must be created inside the
  move loop, not reused across iterations — stale contents from a previous move would corrupt
  the destination stack.
- **Source equals destination edge case:** The puzzle does not appear to exercise
  `move N from X to X`, but if it did, popping from and pushing to the same deque via a
  temporary buffer would still produce the correct result (group preserved). No special guard
  is needed.
- **Part 1 vs Part 2 stacks must be independent:** Both methods must parse fresh copies of
  the stacks from the original input. If the stacks parsed in `solvePartOne` are mutated in
  place, `solvePartTwo` must re-parse from scratch — do not share a single parsed stacks
  array between the two methods.

---

## Complexity Assessment

### Input Scale
Typical AoC real input for this puzzle: 8–9 stacks, ~50 crates total, ~500–600 move
instructions. Extremely small by AoC standards.

### Required Time Complexity
O(M × N) where M = number of move instructions and N = average crate count moved per
instruction. In practice this is effectively O(total crate moves), which is at most a
few thousand operations. No optimisation is required.

### Space Complexity
O(S × H) where S = number of stacks and H = maximum stack height. Completely negligible.

### Naive Approach Viable?
**Yes** — direct simulation with `ArrayDeque` per stack is both the naive and optimal
approach for this input size.

---

## Implementation Plan

### Input Reading
```java
// Already wired in the skeleton via InputStream + Scanner.
// `input` is List<String> passed into solvePartOne(List<String> input).
List<String> lines = readInput(); // uses getResourceAsStream("/2022/day5/day5_puzzle_data.txt")
```

### Key Data Structures
- `Deque<Character>[] stacks` — one `ArrayDeque<Character>` per stack; index 0 = stack 1.
- `int[] move` — three-element array `[count, from, to]` extracted per instruction line.
- `int separatorIndex` — the line index of the blank line dividing drawing from instructions.

### solvePartOne Outline
1. Find the blank-line separator index in `input`. Everything before it is the drawing
   section; everything after it is the instruction section.
2. Use the label row (`input.get(separatorIndex - 1)`) to determine the number of stacks,
   then initialise an `ArrayDeque<Character>[]` of that size.
3. Iterate over the drawing rows from `separatorIndex - 2` down to `0`; for each row
   check character position `1 + stackIdx * 4` — if it is a letter (not a space), push
   it onto the corresponding deque.
4. For each instruction line after the separator, extract `(count, from, to)` via
   `split("\\D+")` or a regex, then loop `count` times popping from `stacks[from-1]`
   and pushing onto `stacks[to-1]`.
5. Build the result string by calling `peekFirst()` on each non-empty deque in order and
   returning the concatenation.

### solvePartTwo Outline
Implement after Part 1 submission. Expected change: instead of reversing order (one-at-a-
time pop/push), move crates as a group preserving their original order — likely achieved
by using a temporary `Deque` or reversing the popped segment before pushing.

### Helper Methods
- `parseStacks(List<String> drawingLines)` — parses fixed-width drawing rows into
  `Deque<Character>[]`; returns the initialised stacks.
- `parseMove(String line)` — extracts `[count, from, to]` from a single instruction
  string; returns `int[]`.
- `buildResult(Deque<Character>[] stacks)` — peeks the top of each stack in order and
  returns the concatenated `String`.

### Parsing Notes
- The separator blank line is crucial — find it first before processing either section.
- The label row (`" 1   2   3 ..."`) contains the stack count; parse it with
  `trim().split("\\s+")` and take its `length` to get the number of stacks.
- Character index for crate letter in column `c` (0-based): `1 + c * 4`.
- Instruction parsing: `"move 3 from 1 to 3".split("\\D+")` yields
  `["", "3", "1", "3"]` — skip index 0 (empty string before "move").
- Guard against empty lines at end of input when iterating instructions.

---

## Implementation Summary

### Year and Day
2022 Day 5

### Algorithm Applied
Simulation using one `ArrayDeque<Character>` per stack.

**Two-phase parsing:**
- Phase 1 — label row (`" 1   2   3 ..."`) parsed with `trim().split("\\s+")` to determine stack count; drawing rows iterated bottom-to-top (excluding label row); crate letters extracted at fixed-width position `CRATE_LETTER_POSITION + stackIdx * CHARS_PER_STACK_COLUMN` (i.e. `1 + i * 4`); non-letter characters (spaces) silently skipped.
- Phase 2 — each instruction line split on `\\D+` to yield `["", count, from, to]`; stacks are 1-indexed in instructions and converted to 0-indexed array access.

**Move execution:** `count` individual pop-and-push operations from source to destination deque, producing natural LIFO reversal.

**Result:** `peekFirst()` on each non-empty deque concatenated in order.

No deviations from the recommended algorithm.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| SupplyStacksAOC2022Day5Test | givenExampleInput_solvePartOne_returnsCMZ() | Unit | Fails before fix → Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| SupplyStacksAOC2022Day5.java | Implemented `solvePartOne`: two-phase parsing + LIFO stack simulation returning concatenated top-crate letters |
| SupplyStacksAOC2022Day5.java | `solvePartTwo` left as stub returning `"not implemented"` — Part 2 not yet available |
| SupplyStacksAOC2022Day5Test.java | Created with example-based unit test for Part 1 |

### Output Format Verified
- Part 1 output: `Part 1: GFTNRBZPF` ✅
- Part 2 output: `Part 2: not implemented` ✅ (stub)

### Puzzle Run Result
```
Part 1: GFTNRBZPF
Part 2: not implemented
```

### Deviations from Analysis
None. The implementation follows the analysis exactly:
- `1 + stackIdx * 4` column formula used (`CRATE_LETTER_POSITION` + `CHARS_PER_STACK_COLUMN`)
- `split("\\D+")` instruction parsing with token indices 1, 2, 3
- Drawing rows iterated bottom-to-top; label row (`separatorIndex - 1`) used to determine stack count
- `ArrayDeque.push()` / `pop()` for LIFO semantics; `peek()` for result collection

### Recommended Follow-up
- [x] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available

---

## Part 2 Implementation Summary

### Year and Day
2022 Day 5

### Algorithm Applied
Simulation using `ArrayDeque<Character>` per stack with a temporary `ArrayDeque` buffer
per move (double-reversal technique).

**Move execution (Part 2):** For each `move N from X to Y`:
1. Pop `N` crates from the source deque into a fresh temporary `ArrayDeque` (order reversed once).
2. Pop all items from the temporary buffer onto the destination deque (reversed again — original
   relative order restored).

All three helpers (`parseStacks`, `parseMove`, `buildResult`) reused unchanged from Part 1.
`solvePartTwo` calls `parseStacks` independently so Part 1's in-place mutations do not affect
Part 2's initial state.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| SupplyStacksAOC2022Day5Test | givenExampleInput_solvePartOne_returnsCMZ() | Unit | Passes (pre-existing) |
| SupplyStacksAOC2022Day5Test | givenExampleInput_solvePartTwo_returnsMCD() | Unit | Fails before fix → Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| SupplyStacksAOC2022Day5.java | Implemented `solvePartTwo`: double-reversal via temporary `ArrayDeque` buffer preserving crate group order |
| SupplyStacksAOC2022Day5Test.java | Added `givenExampleInput_solvePartTwo_returnsMCD()` example-based unit test |

### Output Format Verified
- Part 1 output: `Part 1: GFTNRBZPF` ✅
- Part 2 output: `Part 2: VRQWPDSGP` ✅

### Puzzle Run Result
```
Part 1: GFTNRBZPF
Part 2: VRQWPDSGP
```

### Deviations from Analysis
None. Implementation follows the double-reversal algorithm exactly as specified in the
Part 2 Algorithm Approach section.

### Recommended Follow-up
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`

---

## Pipeline Handoff

### Year
2022

### Day
5

### Puzzle Title
Supply Stacks

### Skeleton Class
`src/main/java/odogwudozilla/year2022/day5/SupplyStacksAOC2022Day5.java`

### Puzzle Input File
`src/main/resources/2022/day5/day5_puzzle_data.txt`

### Workflow Stage
B2 — Part 2 Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Part 1: Simulation — one `ArrayDeque<Character>` per stack; execute each move by popping and
pushing one crate at a time (LIFO reversal).
Part 2: Same simulation — execute each move by popping `N` crates into a temporary
`ArrayDeque` buffer (which reverses them once), then popping all from the buffer onto the
destination (reversing again to restore original order).

### Constraints Summary
- Stacks are 1-indexed in instructions; map to 0-indexed Java array.
- Blank line separates drawing from instructions.
- Empty columns in the drawing are spaces — must not push space characters onto stacks.
- Return type is `String`.
- Part 2: temporary buffer must be a fresh `ArrayDeque` for each move instruction to avoid
  contamination between moves.

### Part 2 Status
Available — description scraped post Part 1 submission (accepted answer: `GFTNRBZPF`).

### Section Anchors
- Puzzle summary — `## Puzzle Summary`
- Part 1 requirements — `## Part 1 Requirements`
- Algorithm approach — `## Algorithm Approach`
- Edge cases — `## Edge Cases` (see Known Pitfalls under `## Algorithm Approach`)
- Implementation plan — `## Implementation Plan`
- Pipeline handoff — `## Pipeline Handoff`

## Review

### Review Cycle
1

### Status
ISSUES_FOUND

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-001 | Coding convention | Minor | SupplyStacksAOC2022Day5.java | `parseMove` accesses `tokens[1]`, `tokens[2]`, `tokens[3]` using raw integer literals instead of the already-defined `TOKEN_COUNT`, `TOKEN_FROM`, `TOKEN_TO` constants |
| RV-002 | Coding convention | Minor | SupplyStacksAOC2022Day5.java | The literal `1` in `move[TOKEN_FROM] - 1` and `move[TOKEN_TO] - 1` (1-to-0 stack-number offset) is an unnamed magic number; must be extracted to a named constant |

### Fix Packets

#### Fix Packet - Cycle 1
**Issues in this pass:** RV-001, RV-002

##### RV-001 - Coding convention - Minor
**File:** SupplyStacksAOC2022Day5.java
**Issue:** Inside `parseMove`, the intermediate `tokens` string array is indexed with the raw literals `1`, `2`, `3` (i.e. `tokens[1]`, `tokens[2]`, `tokens[3]`). The class already defines `TOKEN_COUNT = 1`, `TOKEN_FROM = 2`, `TOKEN_TO = 3` for precisely these positions. Using raw literals here defeats the purpose of those constants and constitutes magic-number usage.
**Resolution guidance:** Replace `tokens[1]`, `tokens[2]`, `tokens[3]` with `tokens[TOKEN_COUNT]`, `tokens[TOKEN_FROM]`, `tokens[TOKEN_TO]` respectively. No other change is needed.

##### RV-002 - Coding convention - Minor
**File:** SupplyStacksAOC2022Day5.java
**Issue:** The arithmetic `move[TOKEN_FROM] - 1` and `move[TOKEN_TO] - 1` inside `solvePartOne` uses the raw literal `1` to convert 1-indexed stack numbers (from the puzzle instructions) to 0-indexed Java array positions. Despite the inline comment, this unnamed literal is a magic number under the project's conventions.
**Resolution guidance:** Introduce a private static final constant — e.g. `STACK_INDEX_OFFSET = 1` — and replace both occurrences of `- 1` with `- STACK_INDEX_OFFSET`. Add a brief Javadoc comment to the constant explaining that puzzle instructions use 1-based stack numbering.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | 0 — issues queued | 2 (RV-001, RV-002) |
| 1 | Fix Cycle 1 applied | 2 (RV-001, RV-002) | 0 |
| 2 | Re-review: RV-001 ✅ RV-002 ✅; new issue RV-003 found | 2 (RV-001, RV-002) | 1 (RV-003) |
| 3 | Re-review: RV-003 ✅; full final sweep — no new issues | 3 (RV-001, RV-002, RV-003) | 0 |

### Commit Message
```
Add AOC 2022 Day 5 solution: Supply Stacks - stack simulation with fixed-width drawing parser

The solution performs two-phase input parsing: the crate drawing is parsed bottom-to-top
using the fixed-width column formula (position 1 + stackIndex × 4) to build one ArrayDeque
per stack in correct bottom-to-top order; the instruction section is parsed by splitting on
non-digit characters. Each move instruction is executed by popping and pushing crates one at
a time, naturally producing the LIFO reversal required by Part 1. The result is assembled by
peeking the front of each deque in ascending stack-number order.
```

---

## Review — Cycle 2

### Review Cycle
2

### Status
ISSUES_FOUND

### Cycle 1 Fix Verification
| ID | Fix Applied | Verified |
|---|---|---|
| RV-001 | `parseMove` indexes `tokens` via `tokens[TOKEN_COUNT]`, `tokens[TOKEN_FROM]`, `tokens[TOKEN_TO]` | ✅ VERIFIED (line 157) |
| RV-002 | `STACK_INDEX_OFFSET = 1` constant added with Javadoc; used in both offset expressions in `solvePartOne` | ✅ VERIFIED (lines 41, 74–75) |

### Full-Criteria Check Results
| Criterion | Result |
|---|---|
| No magic numbers | ✅ All literals extracted to named constants |
| No wildcard imports | ✅ All imports explicit (incl. `import static org.junit.jupiter.api.Assertions.assertEquals`) |
| @NotNull/@Nullable on all non-null parameters | ✅ Present on all non-JVM-entry-point parameters |
| British English in comments/Javadoc | ✅ ("artefact" used correctly) |
| Javadoc @param and @return; no @throws; no blank line before tags | ⚠️ Minor issue — see RV-003 |
| Constants on left in `.equals()` | ✅ N/A — no `.equals()` calls in production code |
| Well-structured, readable code | ✅ Methods are focused and clearly named |
| Part 1 output exactly `Part 1: <value>` | ✅ `System.out.println("Part 1: " + solver.solvePartOne(input))` |
| `solvePartTwo` is a stub | ✅ Returns `"not implemented"` with TODO comment |
| Test follows plain JUnit 5 | ✅ `@Test` + explicit `assertEquals` import |
| Test covers example case | ✅ Asserts `"CMZ"` against the full puzzle example |
| No fully-qualified class references | ✅ |
| `@SuppressWarnings("unchecked")` justified | ✅ Generic array creation — acceptable |

### New Issues Found in Cycle 2
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-003 | Javadoc | Minor | SupplyStacksAOC2022Day5.java | `@return` tag in `parseMove` states `{@code ["", count, from, to]}` but the method returns `int[]{0, count, from, to}`; the `""` notation (string empty) is inaccurate for an `int[]` — index 0 holds integer `0`, not an empty string |

### Fix Packets

#### Fix Packet - Cycle 2
**Issues in this pass:** RV-003

##### RV-003 - Javadoc - Minor
**File:** SupplyStacksAOC2022Day5.java
**Issue:** The `@return` Javadoc tag on `parseMove` reads `{@code ["", count, from, to]}`, borrowing the `""` notation from the `String[]` tokens produced by `split("\\D+")`. The actual return type is `int[]` where index 0 carries the integer value `0`, not an empty string. The description line above also states "Index 0 is empty" which is misleading — it is a dummy placeholder whose value is the integer `0`, not an absent element.
**Resolution guidance:** Update the `@return` tag to accurately reflect the `int[]` return type, expressing index 0 as `0` (integer zero) rather than `""`. Adjust the description sentence for index 0 accordingly — e.g. "Index 0 is a dummy placeholder (`0`); index 1 is count, index 2 is source stack number, index 3 is destination stack number."

---

## Fix Implementation Summary — Cycle 1

### Issues Resolved
| ID | Category | Severity | Resolution |
|---|---|---|---|
| RV-001 | Coding convention | Minor | Replaced `tokens[1]`, `tokens[2]`, `tokens[3]` in `parseMove` with `tokens[TOKEN_COUNT]`, `tokens[TOKEN_FROM]`, `tokens[TOKEN_TO]` |
| RV-002 | Coding convention | Minor | Added `private static final int STACK_INDEX_OFFSET = 1` with Javadoc; replaced both `- 1` occurrences in `solvePartOne` with `- STACK_INDEX_OFFSET` |

### Changes Made
| File | Change description |
|---|---|
| SupplyStacksAOC2022Day5.java | RV-001: `parseMove` now indexes `tokens` via `TOKEN_COUNT`, `TOKEN_FROM`, `TOKEN_TO` constants throughout |
| SupplyStacksAOC2022Day5.java | RV-002: Added `STACK_INDEX_OFFSET = 1` constant with Javadoc; applied in `solvePartOne` for 1→0 index conversion |

### Test Results
- Test suite `odogwudozilla.year2022.day5.*`: **BUILD SUCCESSFUL** — all tests pass

### Puzzle Run Result
```
Part 1: GFTNRBZPF
Part 2: not implemented
```

### Part 1 Answer Confirmed
`GFTNRBZPF` ✅ — unchanged after fixes

### Deviations from Fix Packet
None. Both fixes applied exactly as specified.

---

## Review — Cycle 3 (Final)

### Review Cycle
3

### Status
ALL_VERIFIED

### Cycle 2 Fix Verification
| ID | Fix Applied | Verified |
|---|---|---|
| RV-003 | `parseMove` `@return` tag updated to `{@code [0, count, from, to]}`; description updated to "Index 0 is a dummy placeholder ({@code 0})" | ✅ VERIFIED (lines 149, 153) |

### Full Final Sweep Results
| Criterion | Result |
|---|---|
| RV-003 `parseMove` `@return` Javadoc accuracy | ✅ VERIFIED |
| Output format `Part 1: <value>` / `Part 2: <value>` | ✅ Lines 50–51 exact |
| Resource read via classpath `getResourceAsStream` | ✅ No hardcoded paths |
| Class name `SupplyStacksAOC2022Day5` | ✅ |
| Package `odogwudozilla.year2022.day5` | ✅ |
| Method names `solvePartOne` / `solvePartTwo` | ✅ |
| Magic numbers — all literals covered by named constants | ✅ |
| Wildcard imports (production + test) | ✅ None; test uses explicit `assertEquals` import |
| `@NotNull` on all non-JVM-entry-point parameters | ✅ All 6 non-entry methods covered |
| Javadoc `@param` / `@return` on every method | ✅ All present; no blank lines before tag blocks |
| British English in comments and Javadoc | ✅ |
| Algorithm efficiency O(M×N) | ✅ Well within 1 second for typical AoC input |
| Test: uses example data, asserts "CMZ" | ✅ |
| Test: descriptive method name | ✅ `givenExampleInput_solvePartOne_returnsCMZ` |
| No fully-qualified class references | ✅ |
| `@SuppressWarnings("unchecked")` justified | ✅ Generic array creation — acceptable |

### No New Issues Found
All criteria passed. No additional issues raised.

### Review History
*(See consolidated table in `## Review` — Cycle 1 section above.)*

---

## Review (Part 2)

### Review Cycle
1 (Part 2 phase, reset)

### Status
ALL_VERIFIED

### Scope
Part 2 implementation only (`solvePartTwo` and new helpers), plus a sanity check that
`solvePartOne` and its test remain intact.

### Full-Criteria Check Results
| Criterion | Result | Notes |
|---|---|---|
| Correctness — `solvePartTwo` produces `"MCD"` for example | ✅ | Double-reversal logic verified by trace and test |
| Correctness — `solvePartOne` produces `"CMZ"` for example | ✅ | Method untouched since Part 1 cycle 3 sign-off |
| Part 1 real answer `GFTNRBZPF` still intact | ✅ | Confirmed in Part 2 Implementation Summary run result |
| Part 2 real answer `VRQWPDSGP` | ✅ | Confirmed in Part 2 Implementation Summary run result |
| Output format — `Part 2: <value>` exactly | ✅ | Line 51: `System.out.println("Part 2: " + solver.solvePartTwo(input))` |
| Output format — `Part 1: <value>` intact | ✅ | Line 50 unchanged |
| Resource reading — `getResourceAsStream` only, no hardcoded paths | ✅ | `INPUT_FILE` constant used; no changes in `readInput()` |
| Class name `SupplyStacksAOC2022Day5` | ✅ | Unchanged |
| Package `odogwudozilla.year2022.day5` | ✅ | Unchanged |
| Method names `solvePartOne` / `solvePartTwo` | ✅ | Exact spelling confirmed |
| No magic numbers (Part 2 code) | ✅ | `STACK_INDEX_OFFSET`, `TOKEN_COUNT`, `TOKEN_FROM`, `TOKEN_TO` all used; no new bare literals |
| No wildcard imports (production) | ✅ | No changes to import block |
| No wildcard imports (test) | ✅ | `import static org.junit.jupiter.api.Assertions.assertEquals` — explicit, unchanged |
| `@NotNull` on `solvePartTwo` parameter | ✅ | `solvePartTwo(@NotNull List<String> input)` — line 96 |
| `@NotNull` on all other non-JVM-entry parameters | ✅ | Helpers unchanged from Part 1 cycle 3 sign-off |
| Javadoc on `solvePartTwo` — description, `@param`, `@return` | ✅ | All three present; no blank line before tag block; no `@throws` |
| British English in `solvePartTwo` Javadoc and comments | ✅ | "preserving", "reversing", "restoring" — no Americanisms |
| `tempBuffer` declared as `Deque<Character>`, instantiated as `new ArrayDeque<>()` | ✅ | No fully-qualified class references |
| Fresh `tempBuffer` created per-move inside the loop | ✅ | `new ArrayDeque<>()` allocated at line 114, inside the `for` loop |
| `parseStacks` called independently in `solvePartTwo` | ✅ | Fresh stacks parsed at line 101; no shared state with `solvePartOne` |
| `@SuppressWarnings("unchecked")` on `solvePartTwo` stack array creation | ✅ | Justified — generic array creation |
| Algorithm efficiency — O(M×N) | ✅ | No change in complexity from Part 1 |
| `givenExampleInput_solvePartTwo_returnsMCD` — uses example input | ✅ | Identical 9-line example from puzzle description |
| `givenExampleInput_solvePartTwo_returnsMCD` — asserts `"MCD"` | ✅ | `assertEquals("MCD", solver.solvePartTwo(lines))` |
| `givenExampleInput_solvePartTwo_returnsMCD` — descriptive name | ✅ | Follows `given_method_returns` naming convention |
| `givenExampleInput_solvePartTwo_returnsMCD` — Javadoc present | ✅ | Describes CrateMover 9001 scenario and expected outcome |
| `givenExampleInput_solvePartOne_returnsCMZ` test intact | ✅ | No modifications |
| Constants on left in `.equals()` | ✅ | N/A — no `.equals()` calls in production code |
| No fully-qualified class references | ✅ | None observed |

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| — | — | — | — | No issues found |

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 (Part 2 reset) | Part 2 initial review — full criteria sweep | All criteria passed | 0 |

### Commit Message
```
Add AOC 2022 Day 5 solution: Supply Stacks - one-at-a-time and group-move stack simulation

The solution performs two-phase input parsing: the fixed-width crate drawing is read
bottom-to-top using the column formula (position 1 + stackIndex × 4) to build one
ArrayDeque per stack in correct bottom-to-top order; move instructions are extracted by
splitting on non-digit characters. Part 1 (CrateMover 9000) executes each move by
popping and pushing crates one at a time, producing the natural LIFO reversal required.
Part 2 (CrateMover 9001) uses a double-reversal technique: count crates are popped into a
temporary ArrayDeque (reversing their order once), then all are popped from the buffer onto
the destination (reversing again), restoring the original relative order. All three parsing
helpers (parseStacks, parseMove, buildResult) are shared between both parts; each solve
method parses a fresh copy of the stacks so mutations made during Part 1 do not affect
Part 2.
```

