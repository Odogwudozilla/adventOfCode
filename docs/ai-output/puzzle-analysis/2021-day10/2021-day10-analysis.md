# 2021 Day 10 - Syntax Scoring - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2021 Day 10

### Puzzle Title
Syntax Scoring

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2021-day10/2021-day10-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Given a list of lines containing nested chunks using (), [], {}, and <>, identify corrupted lines (where a closing character does not match the expected opener) and sum the syntax error scores for the first illegal character in each corrupted line, using a provided scoring table.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
|[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
((((<{<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]|
26397|From the description, this is the sum of the syntax error scores for the first illegal character in each corrupted line.|

### Constraints
- Each line contains only the characters ()[]{}<>
- Chunks can be nested arbitrarily deep
- Only the first illegal character per corrupted line is scored
- Ignore incomplete (but not corrupted) lines for Part 1

### Data Structures in Description
- Stack (for tracking openers)
- List of strings (input lines)

---

## Part 2 Requirements

### Problem Statement
After discarding corrupted lines, for each incomplete line, determine the sequence of closing characters needed to complete all open chunks. For each completion string, calculate its score using a specific scoring system, then return the middle score (median) of all completion scores.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
|[({(<(())[]>[[{[]{<()<>>\n[(()[<>])]({[<{<<[]>>(\n{([(<{}[<>[]}>{[]{[(<()>\n((((<{<>}<{<{<>}{[]{[]{}\n[[<[([]))<([[{}[[()]]]\n[{[{({}]{}}([{[{{{}}([]\n{<[[]]>}<{[{[{[]{()[[[]\n[<(<(<(<{}))><([]([]()\n<{([([[(<>()){}]>(<<{{\n<{([{{}}[<[[[<>{}]]]>[]]|
288957|Median of completion scores for the five incomplete lines in the example.|

### How Part 2 Differs from Part 1
- Corrupted lines are discarded; only incomplete lines are considered.
- Instead of scoring illegal characters, compute a completion string for each incomplete line and score it using a new scoring table.
- The answer is the median of all completion scores (not the sum).

---

## Algorithm Approach

### Recommended Algorithm - PROPOSED
**Algorithm class:** Stack-based parsing (simulation)
**Rationale:** Use a stack to track openers; on encountering a closer, check if it matches the stack's top. If not, record the first illegal character and score it. For incomplete lines, generate a completion string from the stack and score it to find the median completion score.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Regex or manual parsing | Not suitable for nested/recursive structures |

### Known Pitfalls
- Only score the first illegal character per corrupted line
- Do not score incomplete lines
- Correct mapping of openers to closers and their scores
- Edge case: deeply nested or empty lines

---

## Complexity Assessment

### Input Scale
Typical AoC input: 100-200 lines, each up to ~120 characters

### Required Time Complexity
O(N*M), where N = number of lines, M = max line length. This is efficient enough for AoC constraints.

### Space Complexity
O(M) per line for the stack; O(N) for input storage

### Naive Approach Viable?
Yes, stack-based simulation is efficient and direct for this problem size.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2021/day10/day10_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `Stack<Character>`: for tracking openers
- `Map<Character, Integer>`: for syntax error scores
- `List<String>`: input lines

### solvePartOne Outline
Iterate through each line, using a stack to track openers. For each closer, check if it matches the expected opener; if not, record the first illegal character and add its score. Ignore incomplete lines. Sum the scores for all corrupted lines and return as a string.

### solvePartTwo Outline
For each line, use a stack to process openers and closers. If a line is incomplete (stack not empty at end, but no corruption), generate the completion string by popping the stack and mapping each opener to its closer. Score each completion string using the provided rules (multiply by 5, add per-character value). Collect all scores, sort them, and return the median as a string.

### Helper Methods
- `isOpener(char c)`: checks if a character is an opener
- `matchingCloser(char opener)`: returns the expected closer for an opener
- `getScore(char illegal)`: returns the score for an illegal character
- `getCompletionString(Stack<Character> stack)`: builds the completion string for an incomplete line
- `scoreCompletionString(String completion)`: computes the score for a completion string

### Parsing Notes
- No blank lines or delimiters; each line is a sequence of chunk characters
- Only the first illegal character per corrupted line is relevant
- Only incomplete (not corrupted) lines are processed
- The number of completion scores is always odd (so median is well-defined)
- Use the correct per-character scoring table for completion strings

---

## Pipeline Handoff

### Year
2021

### Day
10

### Puzzle Title
Syntax Scoring

### Skeleton Class
`src/main/java/odogwudozilla/year2021/day10/SyntaxScoringAOC2021Day10.java`

### Puzzle Input File
`src/main/resources/2021/day10/day10_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis (consumed by @solution-implementer)

### Recommended Algorithm
Stack-based simulation to find and score the first illegal character per corrupted line

### Part 2 Status
Not yet available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

---

## Implementation Summary

### Year and Day
2021 Day 10

### Algorithm Applied
Stack-based parsing using a stack to track openers and a map for syntax error scores. For each line, the first illegal closer is scored; incomplete lines are ignored. No deviations from the plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| SyntaxScoringAOC2021Day10Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| SyntaxScoringAOC2021Day10.java | Implemented solvePartOne: stack-based syntax error scoring for corrupted lines |
| SyntaxScoringAOC2021Day10.java | Implemented solvePartTwo: stub prints 'Part 2: not implemented' |
| SyntaxScoringAOC2021Day10Test.java | Created with example-based unit test for Part 1 |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes (prints 'not implemented')

### Puzzle Run Result
```
Part 1: 374061
Part 2: not implemented
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Implement Part 2 once description is available (if pending)

---

## Implementation Summary

### Year and Day
2021 Day 10

### Algorithm Applied
Stack-based parsing for both parts. For Part 2, incomplete (not corrupted) lines are completed by popping the stack and mapping openers to closers, then scored using the specified completion scoring rules. The median of all completion scores is returned. No deviations from the plan.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| SyntaxScoringAOC2021Day10Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| SyntaxScoringAOC2021Day10Test | givenExampleInput_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| SyntaxScoringAOC2021Day10.java | Implemented solvePartTwo: stack-based completion scoring for incomplete lines, returns median |
| SyntaxScoringAOC2021Day10Test.java | Added example-based unit test for Part 2 |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```
Part 1: 374061
Part 2: 2116639949
```

### Deviations from Analysis
None.

### Recommended Follow-up
- [x] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [x] Implement Part 2 once description is available (now complete)
