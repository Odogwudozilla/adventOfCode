# 2022 Day 7 - No Space Left On Device - Analysis

## Run Metadata

### Workflow Type
Puzzle Analysis

### Year and Day
2022 Day 7

### Puzzle Title
No Space Left On Device

### Producing Agents (lifecycle)
| Step | Agent | Section Added |
|------|-------|---------------|
| 1 | @puzzle-analyser | ## Part 1 Requirements - ## Implementation Plan |
| 2 | @solution-implementer | ## Implementation Summary |
| 3 | @solution-reviewer | ## Review |
| 4 | @puzzle-analyser | Refreshed ## Part 2 Requirements, ## Implementation Plan, and ## Pipeline Handoff |
| 5 | @solution-implementer | Updated ## Implementation Summary for Part 2 implementation |
| 6 | @solution-reviewer | Updated ## Review for Part 2 verification |

### Files Created
| File | Purpose |
|------|---------|
| docs/ai-output/puzzle-analysis/2022-day7/2022-day7-analysis.md | Consolidated analysis lifecycle file |

---

## Part 1 Requirements

### Problem Statement
Parse the recorded terminal session, reconstruct enough of the filesystem structure to determine each directory's total size, and then sum the total sizes of all directories whose size is at most 100000. A directory's total includes all files contained directly or indirectly beneath it, while directories themselves contribute no intrinsic size.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| <pre>$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k</pre> | 584 | Total size of directory `e`. |
| <pre>$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k</pre> | 94853 | Total size of directory `a`. |
| <pre>$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k</pre> | 24933642 | Total size of directory `d`. |
| <pre>$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k</pre> | 48381165 | Total size of the root directory `/`. |
| <pre>$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k</pre> | 95437 | Final Part 1 answer for the worked example: sum of qualifying directories `a` and `e`. |

### Constraints
- The input is a terminal transcript containing `cd` commands, `ls` commands, directory listings (`dir name`), and file listings (`size name`).
- The filesystem is a tree rooted at `/`.
- A directory's total size is the sum of all file sizes contained directly or indirectly within it.
- Directories have no intrinsic size of their own.
- Only directories with total size at most `100000` contribute to the required sum.
- The final summation may count the same file size more than once via different ancestor directories.

### Data Structures in Description
- Tree of directories and files
- Sequential terminal transcript
- Parent-child directory relationships

---

## Part 2 Requirements

### Problem Statement
Using the same reconstructed filesystem, determine how much space must be freed so that at least `30000000` space is unused on a disk with total capacity `70000000`. Return the total size of the smallest directory whose deletion would free enough space to meet that target.

### Example Test Cases
| Example Input | Expected Output | Notes |
|---|---|---|
| <pre>$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k</pre> | 21618835 | Current unused space in the worked example (`70000000 - 48381165`). |
| <pre>$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k</pre> | 8381165 | Minimum space that still needs to be freed after accounting for current unused space. |
| <pre>$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k</pre> | 24933642 | Smallest deletable directory is `d`; this is the final Part 2 answer for the worked example. |

### How Part 2 Differs from Part 1
Part 2 reuses the same directory totals, but instead of summing all directories under a threshold, it derives a required deletion size from total capacity, current usage, and target free space, then selects the smallest directory whose total meets or exceeds that threshold.

---

## Algorithm Approach

### Recommended Algorithm - PROPOSED
**Algorithm class:** Simulation with directory-size aggregation
**Rationale:** The input is already an ordered shell transcript, so the most direct solution is to simulate directory navigation and accumulate each file's size into the current directory and all of its ancestors in one pass.

### Alternative Approaches
| Approach | Trade-offs |
|---|---|
| Explicit filesystem tree plus post-order DFS | Easy to reason about, but uses more memory and requires an extra traversal to compute directory totals. |
| Recompute each directory total by scanning descendants on demand | Simpler to sketch, but wastes work and becomes unnecessarily slow if many directory totals are recalculated repeatedly. |

### Known Pitfalls
- Treat `dir name` lines as structure only; they do not contribute any size.
- Handle `cd /`, `cd ..`, and `cd x` distinctly so the current path stays correct.
- Do not count directories merely because they were listed; only file entries add size.
- If using path strings as keys, ensure root and nested paths are represented consistently.
- The worked example explicitly allows the final sum to count the same file through multiple qualifying ancestor directories.

---

## Complexity Assessment

### Input Scale
The description does not give a numeric upper bound; the input is a single terminal transcript with one line per command or directory entry.

### Required Time Complexity
A linear pass over the transcript, plus at most one update per ancestor directory for each file entry, is appropriate. In practice this should remain efficient for Advent of Code input sizes and avoids repeated subtree rescans.

### Space Complexity
`O(D)` for aggregated directory totals if only directory sizes are stored, or `O(D + F)` if an explicit tree of directories and files is built.

### Naive Approach Viable?
Not ideally. Recomputing each directory size from scratch after parsing would add avoidable repeated work; a single-pass aggregation or one post-order traversal is the better fit.

---

## Implementation Plan

### Input Reading
```java
List<String> lines = Files.readAllLines(Paths.get(getClass().getResource(
    "/2022/day7/day7_puzzle_data.txt").toURI()));
```

### Key Data Structures
- `Deque<String>` or `List<String>` for the current directory stack
- `Map<String, Long>` for cumulative directory sizes keyed by canonical path
- Optionally, a small parser branch per line type (`$ cd`, `$ ls`, `dir`, file entry)

### solvePartOne Outline
Read the transcript line by line and maintain the current directory path as commands are processed. When a file entry is encountered, parse its size and add that value to every directory on the current path, including root. After the scan, iterate through the accumulated directory totals, filter those at most `100000`, and return their sum as a string.

### solvePartTwo Outline
Reuse the directory-size aggregation from Part 1 so every directory total, including the root total, is available after one transcript scan. Compute current unused space as `70000000 - rootSize`, derive the additional space required as `30000000 - unusedSpace`, then scan the directory totals for the smallest value greater than or equal to that requirement. Return that minimum qualifying directory size as a string.

### Helper Methods
- `isCommand(String line)` - distinguish command lines from `ls` output
- `applyCd(String argument, Deque<String> path)` - update the current path for `/`, `..`, and child directories
- `addSizeToAncestors(long fileSize, Deque<String> path, Map<String, Long> totals)` - propagate a file's size to the current directory and all ancestors
- `toPathKey(Deque<String> path)` - build a stable map key for the current directory path
- `findSmallestDirectoryToDelete(Map<String, Long> totals)` - compute the required free space and choose the smallest qualifying directory total

### Parsing Notes
- Ignore `$ ls` lines beyond signalling that following lines are listings.
- File entries begin with a numeric size followed by a filename; filenames should not be split beyond separating the leading size.
- Blank lines are not part of the worked example, but defensive trimming is harmless.
- Use `long` rather than `int` for directory totals to avoid accidental overflow across many files.

---

## Pipeline Handoff

### Year
2022

### Day
7

### Puzzle Title
No Space Left On Device

### Skeleton Class
`src/main/java/odogwudozilla/year2022/day7/NoSpaceLeftOnDeviceAOC2022Day7.java`

### Puzzle Input File
`src/main/resources/2022/day7/day7_puzzle_data.txt`

### Workflow Stage
Puzzle Analysis refresh for Part 2 implementation (consumed by @solution-implementer)

### Recommended Algorithm
Simulate the shell transcript once, aggregate each file size into the current directory and all ancestors, then select the smallest directory whose total size is at least the required deletion threshold.

### Part 2 Status
Available

### Section Anchors
- Part 1 requirements - `## Part 1 Requirements`
- Part 2 requirements - `## Part 2 Requirements`
- Algorithm approach - `## Algorithm Approach`
- Implementation plan - `## Implementation Plan`

---

## Implementation Summary

### Year and Day
2022 Day 7

### Algorithm Applied
Simulated the shell transcript and aggregated each file size into the current directory and every ancestor directory using canonical path keys. Part 2 reuses the same aggregated directory totals, computes the extra free space required from total capacity and current usage, then selects the smallest qualifying directory to delete. No deviation from the analysis.

### TDD Summary
| Test class | Test method | Level | Status |
|---|---|---|---|
| NoSpaceLeftOnDeviceAOC2022Day7Test | givenExampleInput_solvePartOne_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |
| NoSpaceLeftOnDeviceAOC2022Day7Test | givenExampleInput_solvePartTwo_returnsExpectedValue() | Unit | Fails before fix -> Passes after fix |

### Changes Made
| File | Change description |
|---|---|
| NoSpaceLeftOnDeviceAOC2022Day7.java | Reused shared transcript parsing and ancestor size aggregation for Part 1 and Part 2 via `buildDirectoryTotals(List<String>)` |
| NoSpaceLeftOnDeviceAOC2022Day7.java | Implemented `solvePartTwo(List<String>)` to compute required free space and return the smallest deletable directory total |
| NoSpaceLeftOnDeviceAOC2022Day7Test.java | Added the worked Part 2 example-based unit test alongside the existing Part 1 example test |

### Output Format Verified
- Part 1 output: `Part 1: <answer>` Yes
- Part 2 output: `Part 2: <answer>` Yes

### Puzzle Run Result
```
Part 1: 1444896
Part 2: 404395
```

### Deviations from Analysis
- None in the solution algorithm.
- The `puzzle.cmd` wrapper returned `Error executing puzzle: null` in this Windows PowerShell session, so final output verification was completed by running `java -cp "build\\classes\\java\\main;build\\resources\\main" odogwudozilla.Main 2022 day7` directly after a successful build.

### Recommended Follow-up
- [ ] Submit Part 1 via `./gradlew autoSolve --args="--auto --watch"`
- [ ] Submit Part 2 via `./gradlew autoSolve --args="--auto --watch"`

## Review

### Review Cycle
1

### Status
ALL_VERIFIED

### Issues
| ID | Category | Severity | File | Description |
|---|---|---|---|---|
| None | - | - | - | No Part 2 issues found. |

### Fix Packets
None.

### Review History
| Cycle | Action | Verified | Remaining |
|---|---|---|---|
| 1 | Part 1 initial review | 0 issues queued | - |
| 1 | Part 2 initial review | 0 issues queued | - |

### Commit Message
Add AOC 2022 Day 7 solution: No Space Left On Device - transcript simulation with ancestor size aggregation

Implemented both parts with a single-pass simulation of the shell transcript, aggregating each file size into the current directory and all ancestors so directory totals are immediately available for threshold summation in Part 1 and minimum viable deletion selection in Part 2. The solution keeps input loading on the classpath resource, matches the worked examples, and remains efficient for typical Advent of Code input sizes.



