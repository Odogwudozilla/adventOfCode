# 2022 Day 6 - Tuning Trouble - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2022 Day 6

### Puzzle Title
Tuning Trouble

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2022-day6/2022-day6-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given a datastream buffer (a string of characters), find the first position where the last four characters received are all different. Report the number of characters processed up to and including the end of this first such four-character marker.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| mjqjpqmgbljsphdztnvjfqwrcgsmlb | 7 | First marker after character 7 |
| bvwbjplbgvbhsrlpgdmjqwftvncz | 5 | First marker after character 5 |
| nppdvjthqldpwncqszvftbrmjlhg | 6 | First marker after character 6 |
| nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg | 10 | First marker after character 10 |
| zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw | 11 | First marker after character 11 |

### Constraints
- Input is a single line of lowercase and/or uppercase letters (no spaces).
- The marker is a sequence of four consecutive, all-different characters.
- The input length is unspecified, but likely up to several thousand characters.
- Only the first marker is relevant; stop after finding it.

### Data Structures in Description
- String (datastream buffer)
- Sliding window of four characters

---

## Part 2 Requirements

### Problem Statement
Given the same datastream buffer as Part 1, find the first position where the last fourteen characters received are all different. Report the number of characters processed up to and including the end of this first such fourteen-character marker (the start-of-message marker).

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| mjqjpqmgbljsphdztnvjfqwrcgsmlb | 19 | First marker after character 19 |
| bvwbjplbgvbhsrlpgdmjqwftvncz | 23 | First marker after character 23 |
| nppdvjthqldpwncqszvftbrmjlhg | 23 | First marker after character 23 |
| nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg | 29 | First marker after character 29 |
| zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw | 26 | First marker after character 26 |

### How Part 2 Differs from Part 1
- The window size for the uniqueness check increases from 4 to 14 characters.
- The logic is otherwise identical: find the first window of the specified size where all characters are unique, and return the position of the last character in that window.

---

## Algorithm Approach (Part 2)

### Recommended Algorithm - CONFIRMED
**Algorithm class:** Sliding window with uniqueness check (window size 14)
**Rationale:** The only change from Part 1 is the window size; the sliding window and uniqueness check remain optimal and efficient for the input size.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Brute-force check each substring of length 14 | Acceptable for small input, but less efficient for larger window sizes |
| Use a queue and set for the window | Slightly more complex, but can be more generalisable |

### Known Pitfalls
- Off-by-one errors in window indexing (ensure the count includes the marker's last character)
- Forgetting to stop at the first valid marker
- Not handling input with no valid marker (should be clarified by constraints)
- Performance: for very large input, repeated set creation could be slow, but for AoC input this is not an issue

---

## Complexity Assessment (Part 2)

### Input Scale
Likely up to several thousand characters (typical AoC input size).

### Required Time Complexity
O(N), where N is the length of the input string, since each window is checked once.

### Space Complexity
O(1), as only a fixed-size window and set are needed (window size 14).

### Naive Approach Viable?
Yes, as the input is small and the window is only 14 characters.

---

## Implementation Plan (Part 2)

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2022/day6/day6_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `String` for the datastream buffer
- `Set<Character>` for checking uniqueness in the window (size 14)

### solvePartTwo Outline
Read the input line as a string. Iterate through the string with a sliding window of size fourteen. For each window, check if all fourteen characters are unique (using a set). When a unique window is found, return the position (1-based index) of the last character in that window. Stop after the first such marker.

### Helper Methods
- `allUnique(String window)` - returns true if all characters in the window are unique (already present, works for any window size)

### Parsing Notes
- Input is a single line; ignore blank lines or extra lines if present
- No delimiters or mixed formats

---

## Pipeline Handoff (Part 2)

### Year
2022

### Day
6

### Puzzle Title
Tuning Trouble

### Skeleton Class
`src/main/java/odogwudozilla/year2022/day6/TuningTroubleAOC2022Day6.java`

### Puzzle Input File
`src/main/resources/2022/day6/day6_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Sliding window with uniqueness check (window size 14, O(N))

### Part 2 Status
Available

### Section Anchors
- Part 2 requirements - `## Part 2 Requirements`
- Algorithm approach - `## Algorithm Approach (Part 2)`
- Implementation plan - `## Implementation Plan (Part 2)`

---

## Algorithm Approach

### Recommended Algorithm - PROPOSED
**Algorithm class:** Sliding window with uniqueness check
**Rationale:** The problem requires checking each group of four consecutive characters for uniqueness, which is efficiently done with a sliding window and a set.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Brute-force check each substring of length 4 | Acceptable for small input, but less clear and less efficient for larger input |
| Use a queue and set for the window | Slightly more complex, but can be more generalisable |

### Known Pitfalls
- Off-by-one errors in window indexing (ensure the count includes the marker's last character)
- Forgetting to stop at the first valid marker
- Not handling input with no valid marker (should be clarified by constraints)

---

## Complexity Assessment

### Input Scale
Likely up to several thousand characters (typical AoC input size).

### Required Time Complexity
O(N), where N is the length of the input string, since each window is checked once.

### Space Complexity
O(1), as only a fixed-size window and set are needed.

### Naive Approach Viable?
Yes, as the input is small and the window is only four characters.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2022/day6/day6_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `String` for the datastream buffer
- `Set<Character>` for checking uniqueness in the window

### solvePartOne Outline
Read the input line as a string. Iterate through the string with a sliding window of size four. For each window, check if all four characters are unique (using a set). When a unique window is found, return the position (1-based index) of the last character in that window. Stop after the first such marker.

### solvePartTwo Outline
Implement after Part 1 submission.

### Helper Methods
- `allUnique(String window)` - returns true if all characters in the window are unique

### Parsing Notes
- Input is a single line; ignore blank lines or extra lines if present
- No delimiters or mixed formats

---

## Pipeline Handoff

### Year
2022

### Day
6

### Puzzle Title
Tuning Trouble

### Skeleton Class
`src/main/java/odogwudozilla/year2022/day6/TuningTroubleAOC2022Day6.java`

### Puzzle Input File
`src/main/resources/2022/day6/day6_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Sliding window with uniqueness check (O(N))

### Part 2 Status
Not yet available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

---

## Implementation Summary

### Year and Day
2022 Day 6

### Algorithm Applied
Sliding window with uniqueness check (O(N)), as described in the analysis. For each window of 4 characters, check if all are unique using a set. Returns the 1-based index of the last character in the first such window. If no marker is found, returns "0".

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| TuningTroubleAOC2022Day6Test | givenExampleInput1_solvePartOne_returns7() | Unit | Pass |
| TuningTroubleAOC2022Day6Test | givenExampleInput2_solvePartOne_returns5() | Unit | Pass |
| TuningTroubleAOC2022Day6Test | givenExampleInput3_solvePartOne_returns6() | Unit | Pass |
| TuningTroubleAOC2022Day6Test | givenExampleInput4_solvePartOne_returns10() | Unit | Pass |
| TuningTroubleAOC2022Day6Test | givenExampleInput5_solvePartOne_returns11() | Unit | Pass |
| TuningTroubleAOC2022Day6Test | givenNoMarkerInput_solvePartOne_returns0() | Unit | Pass |

### Changes Made
| File | Change description |
|---|---|
| TuningTroubleAOC2022Day6.java | Implemented solvePartOne: sliding window uniqueness check, returns answer as string |
| TuningTroubleAOC2022Day6Test.java | Added test for no valid marker (edge case) |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` [Yes]
- Part 2 output: `Part 2: <answer>` [Yes, prints 'not implemented']

### Puzzle Run Result
```
Part 1: <answer captured from puzzle command>
Part 2: not implemented
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [x] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available (if pending)

---

## Implementation Summary

### Year and Day
2022 Day 6

### Algorithm Applied
Sliding window with uniqueness check (O(N)), as described in the analysis. For each window of 14 characters, check if all are unique using a set. Returns the 1-based index of the last character in the first such window. If no marker is found, returns "0".

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| TuningTroubleAOC2022Day6Test | givenExampleInput1_solvePartTwo_returns19() | Unit | Fails before fix -> Passes after fix |
| TuningTroubleAOC2022Day6Test | givenExampleInput2_solvePartTwo_returns23() | Unit | Fails before fix -> Passes after fix |
| TuningTroubleAOC2022Day6Test | givenExampleInput3_solvePartTwo_returns23() | Unit | Fails before fix -> Passes after fix |
| TuningTroubleAOC2022Day6Test | givenExampleInput4_solvePartTwo_returns29() | Unit | Fails before fix -> Passes after fix |
| TuningTroubleAOC2022Day6Test | givenExampleInput5_solvePartTwo_returns26() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| TuningTroubleAOC2022Day6.java | Implemented solvePartTwo: sliding window uniqueness check, window size 14 |
| TuningTroubleAOC2022Day6Test.java | Added all Part 2 example-based unit tests |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` [Yes]
- Part 2 output: `Part 2: <answer>` [Yes]

### Puzzle Run Result
```
Part 1: 1100
Part 2: 2421
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [x] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [x] Implement Part 2 once description is available (if pending)
