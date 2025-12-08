# Advent of Code Challenge Tracking System

## Overview
This system tracks which Advent of Code puzzles are available and which have been solved, enabling random selection of unsolved puzzles.

## Files

### 1. `aoc_challenge_config.json`
**Purpose**: Defines all available Advent of Code years and their puzzle counts.

**Structure**:
```json
{
  "adventOfCodeConfig": {
    "yearsAvailable": {
      "2015": { "totalDays": 25, "availableUntil": "2015-12-25" },
      "2016": { "totalDays": 25, "availableUntil": "2016-12-25" },
      ...
      "2025": { "totalDays": 12, "availableUntil": "2025-12-12" }
    }
  }
}
```

**Key Features**:
- Years 2015-2024: 25 days each (December 1-25)
- Year 2025: 12 days (December 1-12)
- Easy to extend for future years

### 2. `solutions_database.json`
**Purpose**: Records all solved puzzles with their solutions.

**Structure**:
```json
{
  "adventOfCodeSolutions": {
    "2015": [
      {
        "year": 2015,
        "day": 1,
        "title": "Not Quite Lisp",
        "link": "https://adventofcode.com/2015/day/1",
        "solutions": {
          "partOne": "138",
          "partTwo": "1771"
        }
      }
    ],
    "2025": [ ... ]
  }
}
```

## How Random Selection Works

When a user requests a random puzzle:

1. **Load Available Puzzles**: Read `aoc_challenge_config.json` to get all available year/day combinations
2. **Load Solved Puzzles**: Read `solutions_database.json` to get already solved puzzles
3. **Calculate Unsolved**: Generate list of all unsolved puzzles (available - solved)
4. **Filter Future Dates**: Remove any puzzles from future dates that haven't been released yet
5. **Random Selection**: Pick one random puzzle from the unsolved list
6. **Proceed**: Continue with normal workflow for that year/day

## Adding a New Year (e.g., 2026)

When December 2026 arrives, simply update `aoc_challenge_config.json`:

```json
"2026": {
  "totalDays": 25,
  "availableUntil": "2026-12-25"
}
```

**That's it!** No code changes needed. The system automatically:
- Includes 2026 in random selection pool
- Tracks which 2026 puzzles are solved in `solutions_database.json`
- Filters out future dates based on current date

## Current Status (as of December 8, 2025)

**Total Available Puzzles**: 262
- 2015-2024: 250 puzzles (10 years × 25 days)
- 2025: 12 puzzles (only 12 days this year)

**Solved Puzzles**: 9
- 2015: Day 1 ✓
- 2025: Days 1-8 ✓

**Unsolved Puzzles**: 253

## Usage

When prompted by the assistant:

**Option 1 - Specific Puzzle**:
```
What year and day? → "2015 day 2"
```

**Option 2 - Random Puzzle**:
```
What year and day? → "random"
```
The system will automatically select an unsolved puzzle and inform you which one was chosen.

## Benefits

1. **Easy Maintenance**: Add new years with one JSON entry
2. **Smart Selection**: Never picks already-solved or future puzzles
3. **Complete Tracking**: Full history of all solutions
4. **Flexible**: Works with varying day counts per year
5. **Future-Proof**: Automatically handles new years with minimal changes

