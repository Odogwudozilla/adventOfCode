# AGENTS.md — Advent of Code Java Project: AI Agent Guide


## Project Architecture & Data Flow
- **Monorepo for Advent of Code (AoC) solutions**: Each puzzle is a standalone Java class, grouped by year and day under `odogwudozilla.year<YYYY>.day<D>`.
- **Resource-driven**: All puzzle input and descriptions are stored in `src/main/resources/<YYYY>/day<D>/` as text files. No hardcoded puzzle data.
- **Puzzle metadata and progress**: Managed in `aoc_challenge_config.json` (available puzzles) and `solutions_database.json` (solved puzzles/results).
- **Enhancement features**: Core utilities, dashboard, and statistics live under `src/main/java/odogwudozilla/core/`, `dashboard/`, and `docs/enhancements/`.

### Enhancement Feature Modules (see `src/main/java/odogwudozilla/core/`)
- `PuzzleTestHarness.java`: Test harness for validating puzzle outputs (expected vs. actual, formatted reports)
- `PuzzlePerformanceMonitor.java`: Tracks execution time and memory usage for each puzzle part
- `PuzzleCacheManager.java`: Caches puzzle results for quick comparison and regression checks
- `PuzzleDifficultyRater.java`: Rates puzzle difficulty based on time, memory, and code size
- `SolutionStatisticsCollector.java`: Aggregates and reports statistics across all solutions

See `docs/enhancements/ENHANCEMENT_FEATURES.md` for details and usage examples.

## Key Developer Workflows
- **Run a specific puzzle**: `puzzle <year> day<day>` (preferred) or `./gradlew run --args="<year> day<day>"`
- **Random unsolved puzzle**: `./gradlew randomPuzzle` (uses `PuzzleRandomizer` and config files)
- **Build project**: `./gradlew build`
- **Test**: `./gradlew test`
- **Add new solution**: Follow the agent workflow in `.github/copilot-instructions.md` (stepwise: create resource files, generate class, update metadata, commit)

## Project-Specific Conventions
- **Class naming**: `<PuzzleTitle>AOC<YYYY>Day<D>.java` (e.g., `SecretEntranceAOC2025Day1.java`)
- **Package structure**: `odogwudozilla.year<YYYY>.day<D>`
- **Resource files**: Always use `day<D>_puzzle_description.txt` and `day<D>_puzzle_data.txt` for each puzzle
- **No hardcoded input**: Always read from resource files
- **Single-class per puzzle**: Unless complexity requires otherwise
- **JavaDocs**: Must include puzzle summary and official URL
- **No wildcard imports**; use JetBrains annotations (`@NotNull`, `@Nullable`)
- **British English for comments/JavaDocs**
- **No magic numbers**: Use named constants

### Hybrid/Polyglot Solutions
- Some puzzles (e.g., 2025 Day 10) require a hybrid approach:
  - Part 1: Java (main solution class)
  - Part 2: Python (Z3 solver, see `solve_part2_z3.py` in the puzzle directory)
- See the puzzle README for instructions on running both parts and requirements (e.g., `z3-solver` Python package).

## Integration Points & Automation
- **PuzzleRandomizer**: Selects unsolved puzzles by cross-referencing config and solution DB
- **Dashboard**: Progress and statistics in `dashboard/index.html` (auto-generated)
- **Yearly and main README**: Update after each new solution (see `.github/copilot-instructions.md` for details)
- **Cache**: JSON results in `cache/puzzle-results/`
- **Test Harness/Performance/Stats**: Use the core enhancement modules for validation, timing, and reporting (see above)

## Examples
- **Solution class**: `src/main/java/odogwudozilla/year2025/day1/SecretEntranceAOC2025Day1.java`
- **Resource files**: `src/main/resources/2025/day1/day1_puzzle_description.txt`, `day1_puzzle_data.txt`
- **Config**: `src/main/resources/aoc_challenge_config.json`, `solutions_database.json`

## Special Cases
- **Day 25**: Part 2 is locked until all previous days are solved; randomizer skips Day 25 unless eligible
- **Do not run solution classes directly**: Always use Gradle tasks or the `puzzle` command for correct environment

## Where to Find More
- **Agent workflow and coding rules**: `.github/copilot-instructions.md` (authoritative for all agent behaviour)
- **Project overview**: `README.md`
- **Enhancement docs**: `docs/enhancements/`
- **Dashboard**: `dashboard/index.html`

