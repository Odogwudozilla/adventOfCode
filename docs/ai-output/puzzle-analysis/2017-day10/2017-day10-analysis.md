# 2017 Day 10 - Knot Hash - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2017 Day 10

### Puzzle Title
Knot Hash

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Pipeline Handoff |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @puzzle-analyser | Updated ## Part 2 Requirements, ## Algorithm Approach, ## Complexity Assessment, ## Implementation Plan, and ## Pipeline Handoff |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2017-day10/2017-day10-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Simulate one round of the knot-tying process on a circular list of numbers (0 to 255) using the input length sequence. After all lengths are processed, return the product of the first two numbers in the resulting list.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| List size: `5` (for example only)\nLengths: `3,4,1,5` | `12` | Final list starts with `3,4`, and `3 * 4 = 12`. |

### Constraints
- Real puzzle uses a circular list of size `256`, initialised with values `0..255`.
- Current position starts at `0`; skip size starts at `0`.
- For each length: reverse that span from current position, advance current position by `length + skipSize` (wrapping), then increment skip size by `1`.
- Circular wrapping applies both to span reversal and current-position movement.
- Lengths larger than the list size are invalid.

### Data Structures in Description
- Circular list (array-like sequence of integers)
- Sequence/list of integer lengths
- Scalar state: current position and skip size

---

## Part 2 Requirements

### Problem Statement
Treat the puzzle input as a raw ASCII string, append the fixed suffix lengths `17,31,73,47,23`, execute 64 rounds of the knot process while preserving current position and skip size across rounds, then convert the sparse hash into a dense hash via XOR blocks of 16 and output a 32-character lower-case hexadecimal Knot Hash.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| `""` (empty string) | `a2582a3a0e66e6e86e3812dcb672a272` | Canonical empty-input Part 2 check. |
| `"AoC 2017"` | `33efeb34ea91902bb2f59c9920caa6cd` | Includes a space character in ASCII conversion. |
| `"1,2,3"` | `3efbe78a8d82f29979031a4aa0b16a9d` | Input is processed as characters, not integer tokens. |
| `"1,2,4"` | `63960835bcdc130f0b66d7ff4f6a5a8e` | Distinguishes close string inputs. |

### How Part 2 Differs from Part 1
Part 2 extends Part 1 from a single numeric-length round into a full hash pipeline: ASCII byte parsing, fixed suffix append, 64 repeated rounds with preserved state, XOR compaction into 16 values, and fixed-width hexadecimal encoding.

### Explicit Constraints
- Use ASCII code values for each character of the trimmed input string.
- Append the exact suffix `17, 31, 73, 47, 23` to the length sequence.
- Run exactly `64` rounds.
- Preserve `currentPosition` and `skipSize` between rounds.
- Sparse hash size remains `256`; dense hash is `16` numbers, each from XOR of a 16-number block.
- Output must be exactly `32` hexadecimal characters, two digits per dense value, with leading zeroes where needed.
- Ignore leading/trailing whitespace in puzzle input.

---

## Algorithm Approach

### Recommended Algorithm - CONFIRMED
**Algorithm class:** Simulation + bitwise reduction + hexadecimal formatting
**Rationale:** The puzzle specifies a deterministic multi-round circular reversal process followed by XOR block reduction and fixed-format hex encoding.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Deque/list rotation to keep current position at index 0 | Adds structural overhead and extra copying versus simple modular index arithmetic on a fixed array. |
| Linked-list style node rewiring | Unnecessarily complex for fixed-size random-index reversal operations. |
| Recompute per-round state from scratch | Incorrect for Part 2 because position and skip size must carry across all 64 rounds. |

### Known Pitfalls
- Parsing comma-separated lengths from a single line and handling incidental whitespace.
- Reversing wrapped segments correctly when `start + length` crosses array end.
- Applying modulo consistently when advancing current position.
- For Part 2, parsing input as comma-separated numbers instead of raw ASCII bytes.
- Forgetting to append the fixed suffix values in the correct order.
- Resetting `currentPosition` or `skipSize` between rounds.
- Producing single-digit hex without left-padding (`0f` required, not `f`).
- Returning upper-case hex when examples and convention expect lower-case.

---

## Complexity Assessment

### Input Scale
- Fixed list size `N = 256` for the actual puzzle.
- Let `K` be the ASCII-length count of the trimmed input string plus 5 suffix values.
- Part 2 performs `64` rounds over the same `K` lengths.

### Required Time Complexity
Part 1: `O(sum(lengths))` over one round.

Part 2: `O(64 * sum(lengths)) + O(256)` for dense-hash reduction and hex formatting; this is comfortably within AoC limits because the list size is fixed and small.

### Space Complexity
`O(N)` for the sparse-hash array plus `O(K)` for length bytes and `O(16)` for dense hash.

### Naive Approach Viable?
Yes. The direct modular simulation with a final XOR/hex post-processing phase is sufficient; no advanced optimisation is needed.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2017/day10/day10_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `int[] numbers = IntStream.range(0, 256).toArray();` - circular list state.
- `int[] lengths` (or `List<Integer>`) - parsed comma-separated length sequence.
- `int currentPosition`, `int skipSize` - mutable simulation state.
- `List<Integer> asciiLengths` - ASCII codes plus fixed suffix for Part 2.
- `int[] denseHash = new int[16]` - XOR-reduced hash blocks.
- `StringBuilder hex` - 32-character lower-case hexadecimal output.

### solvePartOne Outline
Parse the first input line into integer lengths split by commas. Initialise the array `0..255`, then iterate each length and reverse that many values in-place using modular index lookups across the circular boundary. After each reversal, advance `currentPosition` by `length + skipSize` modulo array length and increment `skipSize`. Return `numbers[0] * numbers[1]` as a string.

### solvePartTwo Outline
Read the puzzle input as a single trimmed string and convert each character to its ASCII code, then append `17,31,73,47,23`. Initialise the `0..255` array once and run the circular reversal loop for 64 rounds without resetting `currentPosition` and `skipSize`. Build the dense hash by XOR-ing each consecutive 16-value block, and format each result with two-digit lower-case hexadecimal (`%02x`) into a 32-character string. Return that string.

### Helper Methods
- `parseLengths(String line)` - split and convert comma-separated tokens to integers.
- `reverseCircular(int[] array, int start, int length)` - reverse a wrapped segment in-place.
- `buildAsciiLengths(String line)` - convert trimmed input string to ASCII codes and append suffix.
- `runRounds(int[] numbers, List<Integer> lengths, int rounds)` - execute knot rounds while preserving state.
- `toDenseHash(int[] sparseHash)` - XOR each 16-number block.
- `toHexString(int[] denseHash)` - produce two-digit lower-case hex per value.

### Parsing Notes
- Input is expected on one line; still guard against empty input lines.
- Part 1 expects comma-separated integers; trim each token before parsing.
- Part 2 expects raw characters, so do not split on commas.
- For Part 2, trim only leading/trailing whitespace before ASCII conversion.

---

## Pipeline Handoff

### Year
2017

### Day
10

### Puzzle Title
Knot Hash

### Skeleton Class
`src/main/java/odogwudozilla/year2017/day10/KnotHashAOC2017Day10.java`

### Puzzle Input File
`src/main/resources/2017/day10/day10_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (Part 2 implementation ready) - consumed by @solution-implementer

### Recommended Algorithm
Run 64 rounds of circular-array reversal using ASCII lengths plus suffix, then XOR each 16-value block and emit a two-digit lower-case hexadecimal string.

### Implementation Constraints for Next Agent
- Keep existing Part 1 behaviour unchanged.
- Implement `solvePartTwo(List<String> input)` using raw trimmed input text as ASCII bytes.
- Append exactly `17,31,73,47,23` before processing rounds.
- Preserve `currentPosition` and `skipSize` across all 64 rounds.
- Dense hash must XOR 16 contiguous blocks of 16 values.
- Output must be a 32-character lower-case hexadecimal string with zero-padding.

### Part 2 Status
Available and analysed.

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Part 2 requirements - `## Part 2 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

## Implementation Summary

### Year and Day
2017 Day 10

### Algorithm Applied
Single-round circular-array simulation with modular index segment reversal for each parsed comma-separated length. No deviation from the recommended Part 1 approach.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| KnotHashAOC2017Day10Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| src/main/java/odogwudozilla/year2017/day10/KnotHashAOC2017Day10.java | Implemented `solvePartOne` using circular reversal simulation, robust length parsing, and first-two product output. |
| src/main/java/odogwudozilla/year2017/day10/KnotHashAOC2017Day10.java | Left `solvePartTwo` as skeleton stub returning `not implemented` (Part 2 pending). |
| src/test/java/odogwudozilla/year2017/day10/KnotHashAOC2017Day10Test.java | Created example-based Part 1 unit test using the `3,4,1,5` sample with list size 5. |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes (stub value)

### Puzzle Run Result
```text
Part 1: 8536
Part 2: not implemented
```

### Deviations from Analysis
- Added a test-focused overload `solvePartOne(List<String> input, int listSize)` so the official list-size-5 example could be validated while preserving the default 256-size solve path.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available.

## Review

### Review Cycle
1

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| - | - | - | - | No issues found in scope for Part 1 implementation. |

### Fix Packets
None.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial review | 0 issues queued | - |

### Commit Message
Add AOC 2017 Day 10 solution: Knot Hash - single-round circular reversal simulation

Implemented Part 1 using a direct circular-array simulation that parses comma-separated lengths, reverses each wrapped segment in place with modular indexing, advances current position and skip size per specification, and returns the product of the first two values. The approach is efficient for the fixed 256-element list and is validated with the official `3,4,1,5` example using the list-size-5 test overload.

