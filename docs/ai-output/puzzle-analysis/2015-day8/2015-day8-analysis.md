# 2015 Day 8 - Matchsticks - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2015 Day 8

### Puzzle Title
Matchsticks

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @puzzle-analyser | Updated ## Part 2 Requirements, ## Algorithm Approach, ## Implementation Plan, and ## Pipeline Handoff |
| 5 | @solution-implementer | Updated ## Implementation Summary for Part 2 |
| 6 | @solution-reviewer | Updated ## Review for Part 2 verification |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2015-day8/2015-day8-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given a file of double-quoted string literals, compute the total number of characters used by their code representations minus the total number of characters those same strings occupy in memory after escape sequences are interpreted. Only the escape forms `\\`, `\"`, and `\xHH` occur.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| `""` | `code=2, memory=0, delta=2` | Narrative example for an empty string literal. |
| `"abc"` | `code=5, memory=3, delta=2` | Narrative example for plain characters inside quotes. |
| `"aaa\"aaa"` | `code=10, memory=7, delta=3` | Narrative example containing an escaped quote. |
| `"\x27"` | `code=6, memory=1, delta=5` | Narrative example containing a hexadecimal escape. |
| ```
""
"abc"
"aaa\"aaa"
"\x27"
``` | `12` | Combined worked example: total code `23` minus total memory `11`. |

### Constraints
- Input is a text file containing one double-quoted string literal per line.
- Whitespace in the file is disregarded for the final calculation.
- The only escape sequences used are `\\`, `\"`, and `\x` followed by exactly two hexadecimal characters.
- Each hexadecimal escape represents a single in-memory character.
- No explicit input-size bound is stated in the description.

### Data Structures in Description
- List of string literals
- Character stream within each line

---

## Part 2 Requirements

### Problem Statement
Re-encode each original string literal as a new code representation, escaping every existing backslash and double quote and then adding the new surrounding quotes. Compute the total number of characters in these newly encoded strings minus the total number of characters in the original code representations.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| `""` | `encoded=6, original=2, delta=4` | `""` becomes `"\\\"\\\""`. |
| `"abc"` | `encoded=9, original=5, delta=4` | Plain letters remain unchanged inside the newly escaped wrapper. |
| `"aaa\"aaa"` | `encoded=16, original=10, delta=6` | Existing escaped quotes require extra escaping in the new representation. |
| `"\x27"` | `encoded=11, original=6, delta=5` | The original backslash is escaped; the hexadecimal digits remain ordinary characters. |
| ```
""
"abc"
"aaa\"aaa"
"\x27"
``` | `19` | Combined worked example: total encoded length `42` minus original code length `23`. |

### How Part 2 Differs from Part 1
Part 2 reverses the direction of comparison. Instead of decoding literals to their in-memory size, it measures how much larger each literal becomes when encoded again for source-code storage.

---

## Algorithm Approach

### Recommended Algorithm - CONFIRMED
**Algorithm class:** String parsing / per-line character counting  
**Rationale:** Both parts are solved by scanning each literal once: Part 1 interprets escape sequences to count in-memory characters, while Part 2 counts how many extra characters are introduced when quotes and backslashes are re-escaped and a fresh outer pair of quotes is added.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Fully decode or fully re-encode each literal into a new `String` and compare lengths | Straightforward conceptually, but creates unnecessary allocations and makes escaping mistakes easier than direct counting. |
| Regex-based replacement of escape sequences before measuring length | Concise, but easier to mishandle quotes or overlapping escape rules and less explicit than a manual scan. |

### Known Pitfalls
- Count the surrounding quote characters in code length, but never in Part 1 memory length.
- When a backslash is encountered in Part 1, advance by the correct number of source characters: 2 for `\\` and `\"`, 4 for `\xHH`.
- In Part 2, every original backslash and every original double quote contributes two characters in the encoded form.
- Part 2 also adds exactly two new surrounding quote characters to every literal, including `""`.
- Do not let Java source escaping confuse file-content escaping; the input file already contains raw backslashes.
- Ignore incidental trailing whitespace if present, because the description says to disregard whitespace in the file.

---

## Complexity Assessment

### Input Scale
Not stated explicitly; expect many short-to-medium string literals, one per line, as is typical for Advent of Code input files.

### Required Time Complexity
`O(totalCharacters)` is appropriate and effectively required, since each character only needs to be inspected once per part to stay comfortably within AoC runtime expectations.

### Space Complexity
`O(1)` auxiliary space beyond the input storage if both decoded and re-encoded lengths are counted directly without constructing new strings.

### Naive Approach Viable?
Yes. A direct single-pass counter for each part is already optimal enough; no further optimisation should be necessary.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2015/day8/day8_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `List<String>` - raw input lines from the puzzle file.
- `long` or `int` accumulators - running totals for code length, memory length, and encoded length.
- Simple index variables while scanning each string literal.

### solvePartOne Outline
Iterate over every input line and add `line.length()` to the total code-character count. For memory length, scan between the outer quotes, incrementing by one for ordinary characters and by one for any recognised escape sequence while advancing the index by the correct amount. Return the difference between the accumulated code total and memory total as a string.

### solvePartTwo Outline
Iterate over every input line and accumulate the original code length exactly as written in the file. For the encoded length, start from `2` for the new outer quotes, then scan the original literal and add `2` for each existing backslash or double quote and `1` for every other character. Return encoded total minus original code total as a string.

### Helper Methods
- `countInMemoryCharacters(String literal)` - scans one literal and returns its decoded in-memory length.
- `countEncodedCharacters(String literal)` - counts the length of the newly encoded representation without building it.
- `isHexDigit(char ch)` - optional guard if defensive validation is desired for `\xHH` parsing.

### Parsing Notes
- Each non-empty line should already be a complete quoted literal.
- The parser should start after the opening quote and stop before the closing quote for Part 1 decoding.
- Part 2 should scan the full original literal, because original quote characters are themselves re-escaped into the new representation.
- Escape parsing must distinguish `\\`, `\"`, and `\xHH` from ordinary characters.
- If blank lines exist unexpectedly, skip them rather than treating them as malformed literals.

---

## Pipeline Handoff

### Year
2015

### Day
8

### Puzzle Title
Matchsticks

### Skeleton Class
`src/main/java/odogwudozilla/year2015/day8/MatchsticksAOC2015Day8.java`

### Puzzle Input File
`src/main/resources/2015/day8/day8_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Use direct per-line character counting: keep the existing Part 1 decoder, and add a Part 2 encoded-length counter that adds two wrapper quotes plus one extra character for every original quote or backslash.

### Part 2 Status
Available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Part 2 requirements - `## Part 2 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

## Implementation Summary

### Year and Day
2015 Day 8

### Algorithm Applied
Single-pass per-line string-literal parser that sums raw code length, decoded in-memory length, and re-encoded length without constructing decoded or encoded strings. No deviation from the analysed plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| MatchsticksAOC2015Day8Test | givenEmptyStringLiteral_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenPlainStringLiteral_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenEscapedQuoteStringLiteral_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenHexEscapeStringLiteral_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenEmptyStringLiteral_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenPlainStringLiteral_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenEscapedQuoteStringLiteral_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenHexEscapeStringLiteral_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| MatchsticksAOC2015Day8Test | givenExampleInput_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| MatchsticksAOC2015Day8.java | Implemented `solvePartOne` with a single-pass literal parser and added a helper to count decoded in-memory characters. |
| MatchsticksAOC2015Day8.java | Implemented `solvePartTwo` with a direct encoded-length counter for re-escaped literals. |
| MatchsticksAOC2015Day8Test.java | Added example-based unit tests covering all analysed Part 1 and Part 2 examples. |

### Output Format Verified
- Part 1 output: `Part 1: 1342` Yes
- Part 2 output: `Part 2: 2074` Yes

### Puzzle Run Result
```
Part 1: 1342
Part 2: 2074
```

### Deviations from Analysis
- None in the solving logic.
- The `puzzle` helper was not available as a PowerShell command name in this shell, and running `./puzzle 2015 day8` still ended with `Error executing puzzle: null`, so output verification was completed with `./gradlew run --args="2015 day8"`.

### Recommended Follow-up
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Request `@solution-reviewer` verification for the completed Part 2 implementation

## Review

### Review Cycle
1 (Phase B)

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| None | - | - | - | No issues found within the reviewed Part 2 implementation scope. |

### Fix Packets
None.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Initial Part 1 review | 0 issues found | 0 |
| 1 (Phase B) | Part 2 review | 0 issues found | 0 |

### Commit Message
Add AOC 2015 Day 8 solution: Matchsticks - single-pass code, memory, and encoded length counting

Use a single-pass scan over each quoted literal to count raw code length, decoded in-memory length, and re-encoded length without constructing intermediate strings. This keeps both parts linear in the input size, reads directly from the classpath resource, and is covered by the worked example cases for decoding and re-encoding from the puzzle description.



