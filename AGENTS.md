# AGENTS.md — Advent of Code Java Project: AI Agent Guide

## Project Architecture & Data Flow
- **Monorepo for Advent of Code (AoC) solutions**: Each puzzle is a standalone Java class, grouped by year and day under `odogwudozilla.year<YYYY>.day<D>`.
- **Resource-driven**: All puzzle input and descriptions are stored in `src/main/resources/<YYYY>/day<D>/` as text files. No hardcoded puzzle data.
- **Puzzle metadata and progress**: Managed in `aoc_challenge_config.json` (available puzzles) and `solutions_database.json` (solved puzzles/results).
- **Enhancement features**: Core utilities, dashboard, and statistics live under `src/main/java/odogwudozilla/core/`, `dashboard/`, and `docs/enhancements/`.

## Key Developer Workflows
- **Run a specific puzzle**: `puzzle <year> day<day>` (preferred) or `./gradlew run --args="<year> day<day>"`
- **Random unsolved puzzle**: `./gradlew randomPuzzle` (uses `PuzzleRandomizer` and config files)
- **Build project**: `./gradlew build`
- **Test**: `./gradlew test`
- **Add new solution**: Follow the workflow in `.github/copilot-instructions.md` (stepwise: create resource files, generate class, update metadata, commit)

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

## Integration Points & Automation
- **PuzzleRandomizer**: Selects unsolved puzzles by cross-referencing config and solution DB
- **Dashboard**: Progress and statistics in `dashboard/index.html` (auto-generated)
- **Yearly and main README**: Update after each new solution (see `.github/copilot-instructions.md` for details)
- **Cache**: JSON results in `cache/puzzle-results/`

## Examples
- **Solution class**: `src/main/java/odogwudozilla/year2025/day1/SecretEntranceAOC2025Day1.java`
- **Resource files**: `src/main/resources/2025/day1/day1_puzzle_description.txt`, `day1_puzzle_data.txt`
- **Config**: `src/main/resources/aoc_challenge_config.json`, `solutions_database.json`

## Special Cases
- **Day 25**: Part 2 is locked until all previous days are solved; randomizer skips Day 25 unless eligible
- **Do not run solution classes directly**: Always use Gradle tasks or the `puzzle` command for correct environment

## Where to Find More
- **Workflow/coding rules**: `.github/copilot-instructions.md`
- **Project overview**: `README.md`
- **Enhancement docs**: `docs/enhancements/`
- **Dashboard**: `dashboard/index.html`

