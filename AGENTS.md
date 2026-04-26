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
- **Random unsolved puzzle (select only)**: `./gradlew randomPuzzle`
- **Install Playwright browsers** (one-time): `./gradlew installPlaywright`
- **Build project**: `./gradlew build`
- **Test**: `./gradlew test`

### Automation modes

| Command | Effect | Automated end-to-end? |
|---------|--------|----------------------|
| `./gradlew autoSolve` | Full pipeline; pauses at Stage 5 for user to implement | No - user solves |
| `./gradlew autoSolve --args="--setup --watch"` | Stages 1-4: fetch, scrape, generate skeleton only | Partial |
| `./gradlew autoSolve --args="--auto --watch"` | Full pipeline, skip pause (solution already written) | Yes, if already implemented |
| `./gradlew autoSolve --args="--submit YEAR DAY --watch"` | Stages 6-12 for a specific puzzle | Yes, if already implemented |

**Fully automated path (type `random` or `solve` in chat):**
1. Copilot checks for `.aoc-state` (pending puzzle) - if found, resumes it
2. If no pending puzzle: Copilot runs `--setup --watch` → puzzle fetched and skeleton created
3. Copilot reads description file → implements solve methods
4. Copilot runs `--auto --watch` → submits, documents, commits
5. All chat-driven commands always include `--watch`

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

### Automation Pipeline (`odogwudozilla.automation` package)
Full end-to-end automation triggered via `./gradlew autoSolve`. See `docs/AOC_AUTOMATION_PLAN.md` for full design details.

| Class | Responsibility |
|-------|---------------|
| `AutomationConfig` | All constants - URLs, paths, env vars, signal strings |
| `BrowserSessionManager` | Playwright lifecycle + AoC session cookie injection |
| `PuzzleScraper` | Scrapes puzzle title and description from AoC pages |
| `InputFetcher` | Fetches puzzle input and writes to resources folder |
| `SolutionSkeletonGenerator` | Creates Java class stub and resource text files |
| `SolverRunner` | Runs puzzle via Gradle, parses `Part 1:`/`Part 2:` stdout |
| `AnswerSubmitter` | Submits answers via Playwright form POST |
| `AcceptanceVerifier` | Parses AoC response for correct/incorrect/too-soon signals |
| `RateLimiter` | Enforces >= 60 s delay between submissions |
| `DocumentationUpdater` | Updates `solutions_database.json` and README files |
| `GitAutomationService` | Runs `git add .` and `git commit` |
| `PuzzleInfo` | Immutable data holder for puzzle metadata |
| `SubmissionResult` | Enum: CORRECT, INCORRECT, TOO_SOON, ALREADY_SOLVED, UNKNOWN |

Main orchestrator: `src/main/java/odogwudozilla/AutomationOrchestrator.java`

### Session Authentication
- Set env var `AOC_SESSION` or create `.aoc-session` in project root
- Never commit session values; `.aoc-session` is gitignored

### Other Automation
- **PuzzleRandomizer**: Selects unsolved puzzles by cross-referencing config and solution DB
- **Dashboard**: Progress and statistics in `dashboard/index.html` (auto-generated)
- **Yearly and main README**: Updated automatically by `DocumentationUpdater`
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

