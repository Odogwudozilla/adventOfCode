# 2022 Day 11 - Monkey in the Middle - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2022 Day 11

### Puzzle Title
Monkey in the Middle

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @solution-implementer | ## Implementation Summary (Fix Cycle 1 update) |
| 5 | @solution-reviewer | ## Review (Cycle 2 re-review update) |
| 6 | @puzzle-analyser | Refresh Part 2 requirements, algorithm approach, and handoff for Part 2 implementation |
| 7 | @solution-implementer | ## Implementation Summary (Part 2 update) |
| 8 | @solution-reviewer | ## Review (Part 2 Cycle 1) |
| 9 | @solution-implementer | ## Implementation Summary (Fix Cycle 2 update - RV-002) |
| 10 | @solution-reviewer | ## Review (Part 2 Cycle 2 re-review closure) |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2022-day11/2022-day11-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Simulate monkeys processing items for 20 rounds. Each inspection applies a monkey-specific operation, then reduces worry by dividing by 3 (floor), routes by divisibility test, and the final answer is the product of the two highest inspection counts.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| ```text
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
``` | `10605` | Final Part 1 sample answer after 20 rounds. |
| Same sample input | After round 1 holdings: `M0: 20,23,27,26`; `M1: 2080,25,167,207,401,1046`; `M2:` empty; `M3:` empty | Intermediate simulation assertion. |
| Same sample input | After round 20 holdings: `M0: 10,12,14,26,34`; `M1: 245,93,53,199,115`; `M2:` empty; `M3:` empty | Late-round simulation assertion. |
| Same sample input | Inspection counts after 20 rounds: `M0=101, M1=95, M2=7, M3=105` | Two highest are 105 and 101; product is 10605. |

### Constraints
- Exactly 20 rounds for Part 1.
- During each monkey turn, process all currently held items in FIFO order.
- Relief step is mandatory after operation: integer floor division by 3.
- Throw target depends on divisibility test result.
- Thrown items append to recipient list and may be processed later in the same round if recipient turn is still pending.
- Monkeys with no items at turn start do nothing.

### Data Structures in Description
- Ordered per-monkey item queues/lists.
- Per-monkey operation definition (`+` or `*`, operand either constant or `old`).
- Per-monkey divisibility test and two destination indices.
- Per-monkey inspection counter.

---

## Part 2 Requirements

### Problem Statement
Re-run the monkey simulation from the initial state for 10,000 rounds, but remove the relief rule (no division by 3 after inspection). Compute the new monkey business level as the product of the two highest inspection counts while keeping worry values mathematically manageable.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| Same sample input as Part 1 | After round 1 inspection counts: `M0=2, M1=4, M2=3, M3=6` | Early checkpoint under Part 2 rules. |
| Same sample input as Part 1 | After round 20 inspection counts: `M0=99, M1=97, M2=8, M3=103` | Confirms short-run behaviour without relief division. |
| Same sample input as Part 1 | After round 1000 inspection counts: `M0=5204, M1=4792, M2=199, M3=5192` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 2000 inspection counts: `M0=10419, M1=9577, M2=392, M3=10391` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 3000 inspection counts: `M0=15638, M1=14358, M2=587, M3=15593` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 4000 inspection counts: `M0=20858, M1=19138, M2=780, M3=20797` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 5000 inspection counts: `M0=26075, M1=23921, M2=974, M3=26000` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 6000 inspection counts: `M0=31294, M1=28702, M2=1165, M3=31204` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 7000 inspection counts: `M0=36508, M1=33488, M2=1360, M3=36400` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 8000 inspection counts: `M0=41728, M1=38268, M2=1553, M3=41606` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 9000 inspection counts: `M0=46945, M1=43051, M2=1746, M3=46807` | Long-run checkpoint. |
| Same sample input as Part 1 | After round 10000 inspection counts: `M0=52166, M1=47830, M2=1938, M3=52013` | Final inspection counts before monkey business calculation. |
| ```text
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
``` | `2713310158` | Final Part 2 sample answer after 10,000 rounds. |

### How Part 2 Differs from Part 1
- Relief division is removed entirely.
- Round count increases from 20 to 10,000.
- Worry values explode without mitigation, so arithmetic reduction is required while preserving divisibility outcomes.
- Core turn order and routing logic are unchanged, so Part 1 simulation structure is reusable with adjusted post-operation handling.

---

## Algorithm Approach

### Recommended Algorithm - CONFIRMED
**Algorithm class:** Simulation with modular arithmetic (round-based queue/state machine).
**Rationale:** Behaviour remains deterministic turn-by-turn, and reducing each worry value modulo the least common multiple (or product) of all divisors preserves every divisibility test needed for routing in Part 2.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| BigInteger simulation without reduction | Correct but slower and unnecessary; values grow extremely large over 10,000 rounds. |
| Reduction by per-item memoisation of operation chains | Complicated state tracking for little benefit over simple modulus-based reduction. |
| Event-driven simulation with a global throw log | More complex bookkeeping without practical gain versus direct per-monkey queue processing. |

### Known Pitfalls
- Applying divisibility test before the relief division (wrong order).
- Processing only items present at round start rather than all items on a monkey turn.
- Using LIFO stack behaviour instead of queue/append order.
- Parsing `old * old` incorrectly as multiplication by a constant.
- Off-by-one mistakes in round count (20 for Part 1, 10,000 for Part 2).
- Using `int` for inspection counts or worry arithmetic; `long` is required.
- Applying modulus at the wrong point; apply operation first, then reduce by common modulus before divisibility routing.

---

## Complexity Assessment

### Input Scale
Small to moderate monkey-definition input, but simulation scale is high in Part 2 (10,000 rounds; each item can be inspected many thousands of times).

### Required Time Complexity
`O(total inspections)` (equivalently `O(R * average items processed per round)`), which is practical for `R=10,000` when each step is O(1) arithmetic and queue operations.

### Space Complexity
`O(K + M)` where `K` is total items in circulation and `M` is monkey count; items are redistributed, not duplicated.

### Naive Approach Viable?
Partially. Naive direct simulation is viable structurally, but raw worry values will overflow; modular reduction by combined divisor is required for correctness and performance in Part 2.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2022/day11/day11_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `static final class Monkey { Deque<Long> items; char op; String rhs; long divisor; int ifTrue; int ifFalse; long inspections; }`
- `List<Monkey>` for indexed monkey access by throw target id.
- `Pattern`/string-splitting helpers for parsing each monkey block.

### solvePartOne Outline
Parse input into monkey blocks separated by blank lines, building each monkey's starting queue, operation, test divisor, and throw targets. Run 20 rounds; for each monkey, repeatedly poll from its queue, apply operation, apply relief (`value / 3`), test divisibility, and append to destination monkey queue while incrementing inspections. After simulation, sort or scan inspection counts to find the top two and return their product as a string.

### solvePartTwo Outline
Re-parse the input to reset monkeys to their initial state. Compute a common modulus from all monkey test divisors (LCM preferred; product also works for AoC input sizes), then run 10,000 rounds using the same throw order as Part 1 but without dividing by 3. After each operation, reduce worry via `value % commonModulus`, route by divisibility test, count inspections, and return the product of the top two inspection counts.

### Helper Methods
- `parseMonkeys(List<String> input)` - convert text blocks into monkey objects.
- `applyOperation(long oldValue, Monkey monkey)` - compute new worry value including `old` operand support.
- `runRounds(List<Monkey> monkeys, int rounds, boolean applyRelief, long modulus)` - execute both parts with configurable post-operation handling.
- `computeMonkeyBusiness(List<Monkey> monkeys)` - product of two highest inspection counts.
- `lcm(long a, long b)` / `computeCommonModulus(List<Monkey> monkeys)` - preserve divisibility behaviour for Part 2.

### Parsing Notes
- Monkey blocks are separated by blank lines.
- Strip labels like `Starting items:` and split comma-separated integers with optional spaces.
- Operation right-hand operand may be numeric or `old`.
- Destination monkey ids are trailing integers on `If true/If false` lines.

---

## Pipeline Handoff

### Year
2022

### Day
11

### Puzzle Title
Monkey in the Middle

### Skeleton Class
`src/main/java/odogwudozilla/year2022/day11/MonkeyInTheMiddleAOC2022Day11.java`

### Puzzle Input File
`src/main/resources/2022/day11/day11_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (Part 2 update; consumed by @solution-implementer)

### Recommended Algorithm
Round-based queue simulation with shared parser and engine: Part 1 uses relief division for 20 rounds; Part 2 runs 10,000 rounds with modular reduction by combined divisors instead of relief division.

### Part 2 Status
Available and extracted from the updated description file.

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

## Implementation Summary

### Year and Day
2022 Day 11

### Algorithm Applied
Round-based queue simulation for 20 rounds with per-item operation application, relief division by 3, divisibility routing, and inspection counting. No deviation from the recommended Part 1 plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| MonkeyInTheMiddleAOC2022Day11Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| MonkeyInTheMiddleAOC2022Day11.java | Implemented `solvePartOne` with monkey parsing, operation handling, 20-round simulation, and monkey-business calculation |
| MonkeyInTheMiddleAOC2022Day11.java | Kept `solvePartTwo` as stub returning `not implemented` |
| MonkeyInTheMiddleAOC2022Day11Test.java | Created example-based Part 1 unit test from analysis sample |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```text
Part 1: 55930
Part 2: not implemented
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 using the modular-reduction simulation plan in `## Implementation Plan`

### Fix Cycle 1 Update (RV-001)
| File | Change description |
|---|---|
| src/test/java/odogwudozilla/year2022/day11/MonkeyInTheMiddleAOC2022Day11Test.java | Added sample-driven regression assertions for round 1 holdings and round 20 inspection counts using reflection against existing simulation internals; retained final-answer assertion (`10605`). |

Validation:
- `./gradlew.bat test --tests "odogwudozilla.year2022.day11.MonkeyInTheMiddleAOC2022Day11Test"` -> **BUILD SUCCESSFUL**

## Review

### Review Cycle
2

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-001 | TestQuality | Minor | src/test/java/odogwudozilla/year2022/day11/MonkeyInTheMiddleAOC2022Day11Test.java | **Verified in Cycle 2.** Added sample-based intermediate assertions for round 1 holdings and round 20 inspection counts, closing the regression gap identified in Cycle 1. |

### Fix Packets

#### Fix Packet - Cycle 1
**Issues in this pass:** RV-001

##### [RV-001] - TestQuality - Minor
**File:** src/test/java/odogwudozilla/year2022/day11/MonkeyInTheMiddleAOC2022Day11Test.java
**Issue:** Current test validates only terminal output (`10605`) and does not lock in the documented sample intermediate behaviour.
**Resolution guidance:** Add focused assertions derived from the published sample behaviour (for example, inspection counts after 20 rounds and/or a representative intermediate round state) to improve regression detection for simulation order rules.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | 1 issue queued | RV-001 |
| 2 | Re-review after Fix Cycle 1 | RV-001 verified | - |

### Commit Message
Add AOC 2022 Day 11 solution: Monkey in the Middle - round-based monkey item simulation

Implemented a direct queue-driven simulation for Part 1 that parses monkey definitions from the classpath resource, executes 20 rounds with operation and relief rules in the correct order, routes items by divisibility tests, and computes monkey business from the top two inspection counts. The test suite now locks in both the published final sample answer and key intermediate sample states (round 1 holdings and round 20 inspection counts) to guard against turn-order and routing regressions.

### Review Status Block

### Year and Day
2022 Day 11

### Review Cycle
2

### Status
ALL_VERIFIED

### Issue Counts
- Queued for fix: 0
- Verified: 1


## Implementation Summary (Part 2 update)

### Year and Day
2022 Day 11

### Algorithm Applied
Round-based queue simulation reused from Part 1, with Part 2 changes applied exactly as analysed: 10,000 rounds, no relief division, and post-operation modulus reduction using the LCM of monkey divisors to preserve divisibility routing.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| MonkeyInTheMiddleAOC2022Day11Test | givenExampleInput_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| MonkeyInTheMiddleAOC2022Day11.java | Implemented `solvePartTwo` with common-modulus simulation for 10,000 rounds and monkey-business calculation |
| MonkeyInTheMiddleAOC2022Day11.java | Refactored simulation engine to support configurable relief/modulus modes while keeping existing Part 1 behaviour unchanged |
| MonkeyInTheMiddleAOC2022Day11Test.java | Added Part 2 sample assertion for expected value `2713310158` |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```text
Part 1: 55930
Part 2: 14636993466
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`





## Review (Part 2)

### Review Cycle
2

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| RV-002 | TestQuality | Major | src/test/java/odogwudozilla/year2022/day11/MonkeyInTheMiddleAOC2022Day11Test.java | **Verified in Cycle 2.** Part 2 tests now assert published intermediate inspection checkpoints (rounds 1, 20, 1000, and 10000) in addition to the final sample value, closing the long-run routing/modulus regression gap. |

### Fix Packets

#### Fix Packet - Cycle 1
**Issues in this pass:** RV-002

##### [RV-002] - TestQuality - Major
**File:** src/test/java/odogwudozilla/year2022/day11/MonkeyInTheMiddleAOC2022Day11Test.java
**Issue:** Part 2 tests currently lock in only the terminal sample value and do not assert the documented intermediate inspection-count milestones that detect drift in no-relief/modulus routing logic.
**Resolution guidance:** Add Part 2 sample-driven checkpoint assertions for published rounds (at minimum early and late checkpoints) using the existing simulation internals, while keeping the final-value assertion.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial Part 2 review | 1 issue queued | RV-002 |
| 2 | Re-review after Fix Cycle 2 | RV-002 verified | - |

### Commit Message
Add AOC 2022 Day 11 solution: Monkey in the Middle - modular arithmetic monkey simulation

Implemented the Part 2 extension by reusing the round-based monkey simulation and applying LCM-based modulus reduction after each operation to preserve divisibility routing over 10,000 rounds without relief division. The test suite now verifies both the final sample answer and published intermediate inspection-count checkpoints, providing strong regression protection for long-run behaviour.

### Review Status Block

### Year and Day
2022 Day 11

### Review Cycle
2

### Status
ALL_VERIFIED

### Issue Counts
- Queued for fix: 0
- Verified: 1



## Implementation Summary (Fix Cycle 2 update - RV-002)

### Year and Day
2022 Day 11

### Fix Applied
Added Part 2 sample checkpoint assertions for published intermediate inspection counts using existing simulation internals (`parseMonkeys`, `computeCommonModulus`, and `runRounds` via reflection), while retaining the final sample-value assertion.

### Changes Made
| File | Change description |
|---|---|
| src/test/java/odogwudozilla/year2022/day11/MonkeyInTheMiddleAOC2022Day11Test.java | Added `givenExampleInput_partTwoInspectionCheckpoints_matchPublishedSample()` with round checkpoints at 1, 20, 1000, and 10000; added helper methods to run Part 2 simulation mode and assert per-monkey inspection counts. |

### Validation Results
- `./gradlew.bat test --tests "odogwudozilla.year2022.day11.MonkeyInTheMiddleAOC2022Day11Test"` -> **BUILD SUCCESSFUL**

### Scope Control
- No production code changes.
- No files changed outside the requested test file and this analysis document.



