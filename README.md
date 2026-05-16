# Advent of Code Solutions in Java

A comprehensive Java-based solution repository for [Advent of Code](https://adventofcode.com/) puzzles. This project provides an organized, automated workflow for solving AoC challenges with support for multiple years and a smart puzzle randomizer.

## 🎯 Project Overview

This repository contains solutions to Advent of Code puzzles from various years (2015-2025). Each solution is implemented as a standalone Java class with clear documentation, efficient algorithms, and proper error handling.

### Key Features

- **📁 Organized Structure**: Solutions organized by year and day with dedicated package structure (`odogwudozilla.year<YYYY>.day<D>`)
- **🎲 Random Puzzle Selector**: Built-in utility to randomly select unsolved puzzles
- **🔧 Gradle Build System**: Modern Gradle build with Kotlin DSL
- **📝 Comprehensive Documentation**: Each solution includes puzzle descriptions and input data
- **🚀 Easy Execution**: Custom Gradle tasks for running solutions and selecting puzzles

## ✨ Enhancement Features

This project includes advanced enhancement features for testing, monitoring, and statistics tracking:

- **Test Harness** - Validate puzzle solutions against expected results
- **Performance Monitoring** - Track execution time and memory usage
- **Puzzle Caching** - Persist results with JSON-based storage
- **Difficulty Rating** - Rate puzzles on a 1-10 scale
- **Solution Statistics** - Analyze comprehensive performance metrics
- **Web Dashboard** - Generate interactive HTML progress tracker

📚 **Documentation:** See [`docs/enhancements/`](docs/enhancements/) for complete details and integration guides.

## 🏗️ Project Structure

```
adventOfCode/
├── .github/
│   ├── agents/                            # GitHub Copilot agent definitions
│   │   ├── puzzle-analyser.agent.md       # Analyses puzzle descriptions
│   │   ├── solution-implementer.agent.md  # Implements solve methods with TDD
│   │   ├── solution-reviewer.agent.md     # Reviews solutions and produces commit messages
│   │   ├── puzzle-orchestrator.agent.md   # Orchestrates the full pipeline
│   │   └── agent-builder.agent.md         # Builds new agents for this project
│   ├── instructions/
│   │   └── agent-shared-rules.instructions.md  # Cross-cutting rules for all agents
│   ├── prompts/
│   │   └── update-agents-md.prompt.md     # Keeps AGENTS.md current
│   └── copilot-instructions.md            # GitHub Copilot project instructions
│
├── src/main/java/odogwudozilla/
│   ├── Main.java                          # Main entry point
│   ├── PuzzleRandomizer.java              # Random puzzle selector utility
│   ├── automation/                        # autoSolve pipeline classes
│   ├── core/                              # Enhancement features (core utilities)
│   ├── dashboard/                         # Enhancement features (web dashboard)
│   ├── examples/                          # Enhancement features (usage examples)
│   └── year<YYYY>/
│       └── day<D>/
│           └── <PuzzleName>AOC<YYYY>Day<D>.java
│
├── src/main/resources/
│   ├── aoc_challenge_config.json          # Available years and days configuration
│   ├── solutions_database.json            # Solved puzzle registry
│   └── <YYYY>/
│       └── day<D>/
│           ├── day<D>_puzzle_description.txt
│           └── day<D>_puzzle_data.txt
│
├── docs/
│   ├── enhancements/                      # Enhancement features documentation
│   │   ├── README.md
│   │   ├── QUICK_START.md
│   │   └── ENHANCEMENT_FEATURES.md
│   ├── ai-output/                         # Local-only AI analysis output (not pushed)
│   │   └── puzzle-analysis/
│   └── memory/                            # Agent lessons-learned system
│       ├── LESSONS.md                     # Lessons index
│       └── lessons/                       # Individual lesson files
│
├── cache/                                 # Runtime-generated cache
│   └── puzzle-results/                    # Cached puzzle results (JSON)
│
├── dashboard/                             # Runtime-generated dashboard
│   └── index.html                         # Progress dashboard (auto-generated)
│
├── AGENTS.md                              # AI agent guide for this project
├── build.gradle.kts                       # Gradle build configuration
└── README.md                              # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java JDK 11 or higher**
- **Gradle** (or use the included Gradle wrapper)
- **GitHub Copilot** with agent support *(optional - for AI-assisted workflow)*

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd adventOfCode
```

2. Build the project:
```bash
./gradlew build
```

3. *(One-time)* Install Playwright browsers for the `autoSolve` automation:
```bash
./gradlew installPlaywright
```

4. *(One-time)* Set your AoC session cookie so `autoSolve` can fetch puzzles and submit answers.
   Log in to [adventofcode.com](https://adventofcode.com), open DevTools → Application → Cookies,
   copy the `session` value, then either:

   ```powershell
   # Option A - environment variable (current session only)
   $env:AOC_SESSION="<your session cookie value>"

   # Option B - file (persists across sessions; gitignored automatically)
   "<your session cookie value>" | Set-Content .aoc-session
   ```

5. *(Optional)* If you want to use the GitHub Copilot agents, see the
   [AI-Assisted Workflow](#-ai-assisted-workflow-github-copilot) section above for first-time setup.

## 🤖 AI-Assisted Workflow (GitHub Copilot)

This project ships with a set of GitHub Copilot agent files that automate the
puzzle-solving pipeline end-to-end. If you use GitHub Copilot with agent support
(JetBrains IDE or VS Code), you can solve puzzles entirely through chat.

### Prerequisites

- GitHub Copilot subscription with agent/chat support enabled
- Your AoC session cookie (see [Automated Pipeline](#automated-pipeline-gradlew-autosolve)
  below for how to set it)

### First-time Setup After Cloning

The agents reference an output directory for analysis files. This path is
machine-specific and must be configured locally - it is **not** stored in the repository.

Run the workflow initialiser once after cloning:

```
@workflow-initializer
Target project: <absolute path to this cloned repo>
Templates: <path to your agentic-workflow-templates folder>
```

The initialiser will:
1. Update `AGENTS.md` to reflect the current project state
2. Infer the workflow intent and agent pipeline from the project files
3. Ask you 4 short questions (output directory, response prefix, memory system, test base class)
4. Generate personalised agent files and deploy them to `.github/agents/`
5. Write a `workflow-how-to.md` guide tailored to your machine

> **Note:** The three output files the initialiser produces (`ide-global-reference.md`,
> `workflow-generation-report.md`, `workflow-how-to.md`) contain your local absolute paths
> and are automatically excluded from git via `.git/info/exclude` - they will never be pushed.

### Daily Use - Solving Puzzles via Chat

Once initialised, simply type in GitHub Copilot chat:

```
random
```

or

```
solve
```

`@puzzle-orchestrator` handles the full pipeline:

| Step | Who | What |
|------|-----|------|
| 1 | `autoSolve --setup` | Selects puzzle, fetches description + input, generates Java skeleton |
| 2 | `@puzzle-analyser` | Reads description, extracts examples, recommends algorithm |
| 3 | *(you confirm)* | Review algorithm recommendation before implementation begins |
| 4 | `@solution-implementer` | Writes `solvePartOne`/`solvePartTwo` with TDD against examples |
| 5 | `@solution-reviewer` | Reviews correctness, conventions, efficiency; produces commit message |
| 6 | `autoSolve --auto` | Submits answers, updates database, commits |

### Available Agents

| Agent | Purpose | Invoke with |
|-------|---------|-------------|
| `@puzzle-orchestrator` | Full pipeline orchestrator | Type `random` or `solve` in chat |
| `@puzzle-analyser` | Analyse one puzzle description | `@puzzle-analyser` + year/day |
| `@solution-implementer` | Implement solve methods | `@solution-implementer` + year/day |
| `@solution-reviewer` | Review a completed solution | `@solution-reviewer` + year/day |
| `@agent-builder` | Create or adapt agents | `@agent-builder` + description |

### Lessons-Learned Memory

The `docs/memory/` directory holds a lessons system for capturing reusable algorithm
insights across puzzles. See `docs/memory/lessons/README.md` for the lesson file template.
`docs/memory/LESSONS.md` is the searchable index.

---

## 📖 Usage

### Running a Specific Solution

Execute a specific puzzle solution using its main class:

```bash
./gradlew runDay18  # Example: Run 2024 Day 18
```

Or run directly:

```bash
./gradlew run --args="<class-name>"
```

### Random Puzzle Selection

Let the system choose a random unsolved puzzle for you:

```bash
./gradlew randomPuzzle
```

This will:
- Scan all available puzzle years and days from `aoc_challenge_config.json`
- Cross-reference with completed puzzles to find unsolved ones
- Exclude future-dated puzzles
- Randomly select one unsolved puzzle
- Output the year and day in format: `YYYY,D`

### Creating a New Solution

Each solution follows a standardized workflow:

1. **Prepare the Package Structure**
   ```
   src/main/java/odogwudozilla/year<YYYY>/day<D>/
   src/main/resources/<YYYY>/day<D>/
   ```

2. **Create Resource Files**
   - `day<D>_puzzle_description.txt` - Paste the puzzle description
   - `day<D>_puzzle_data.txt` - Paste your puzzle input

3. **Generate the Solution Class**
   - Name format: `<PuzzleTitle>AOC<YYYY>Day<D>.java`
   - Package: `odogwudozilla.year<YYYY>.day<D>`
   - Include JavaDoc with puzzle summary and URL

4. **Implement the Solution**
   - Read input from resource file
   - Solve Part 1 (and Part 2 if applicable)
   - Output results to console

## 📋 Configuration Files

### `aoc_challenge_config.json`

Defines all available Advent of Code years and days:

```json
{
  "adventOfCodeConfig": {
    "yearsAvailable": {
      "2024": {
        "totalDays": 25,
        "availableUntil": "2024-12-25"
      },
      "2025": {
        "totalDays": 12,
        "availableUntil": "2025-12-12"
      }
    }
  }
}
```

## 🎯 Solution Template

Each solution class follows this template:

```java
package odogwudozilla.year<YYYY>.day<D>;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Advent of Code <YYYY> - Day <D>: <Puzzle Title>
 *
 * <Brief puzzle description>
 *
 * Part 1: <Part 1 description>
 * Part 2: <Part 2 description>
 *
 * Puzzle link: https://adventofcode.com/<YYYY>/day/<D>
 */
public class <PuzzleName>AOC<YYYY>Day<D> {

    private static final String INPUT_FILE = "/<YYYY>/day<D>/day<D>_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            // Read input
            // Solve Part 1
            // Solve Part 2
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 🔧 Gradle Tasks

| Task | Description |
|------|-------------|
| `./gradlew build` | Compile and build the project |
| `./gradlew run` | Run the main application |
| `./gradlew randomPuzzle` | Select a random unsolved puzzle (outputs `YYYY,D`) |
| `./gradlew test` | Run unit tests |
| `./gradlew autoSolve` | Full automation pipeline (pauses at implementation step) |
| `./gradlew autoSolve --args="--setup --watch"` | Fetch puzzle + generate skeleton only |
| `./gradlew autoSolve --args="--auto --watch"` | Submit answers + document + commit |
| `./gradlew installPlaywright` | Install Playwright browsers (one-time setup for autoSolve) |
| `puzzle <YEAR> day<N>` | Run a specific puzzle via the `puzzle` command script |

## 📦 Dependencies

- **JetBrains Annotations** (`org.jetbrains:annotations:24.0.1`) - For `@NotNull`, `@Nullable` annotations
- **JUnit Jupiter** (`org.junit.jupiter:junit-jupiter`) - For unit testing

## 🎄 Special Considerations

### Day 25 Handling

Day 25 is special in Advent of Code:
- Part 1 is a regular puzzle
- Part 2 requires all 49 stars from Days 1-24 to be completed first
- The randomizer will not select Day 25 until all previous days are solved
- If Day 25 Part 1 is completed but Part 2 is locked, skip Part 2

## 📝 Coding Standards

- Use meaningful variable names and consistent formatting
- No magic numbers - define constants with descriptive names
- Comments should explain complex logic (in British English)
- No wildcard imports - explicitly import required classes
- Use JetBrains annotations (`@NotNull`, `@Nullable`)
- Read input from resource files, never hardcode puzzle data
- Each puzzle gets a single Java class unless complexity requires more

## 🤝 Contributing

When adding new solutions:

1. Follow the package structure: `odogwudozilla.year<YYYY>.day<D>`
2. Include complete JavaDoc comments
3. Add puzzle description and input data to resources
4. Commit with descriptive message: `Add AOC <Year> Day <Day> solution: <PuzzleTitle> - <approach>`

## 📅 Years

Explore solutions organized by year:

- **[2015 Solutions](src/main/java/odogwudozilla/year2015/README.md)** - The inaugural Advent of Code year
- **[2016 Solutions](src/main/java/odogwudozilla/year2016/README.md)** - Chronal Calibration and Keypad Security
- **[2017 Solutions](src/main/java/odogwudozilla/year2017/README.md)** - Assembly instructions and circular lists
- **[2018 Solutions](src/main/java/odogwudozilla/year2018/README.md)** - Fabric cutting and sleigh systems
- **[2019 Solutions](src/main/java/odogwudozilla/year2019/README.md)** - Intcode computer and space image format
- **[2020 Solutions](src/main/java/odogwudozilla/year2020/README.md)** - Tropical vacation themed puzzles
- **[2021 Solutions](src/main/java/odogwudozilla/year2021/README.md)** - Submarine adventure with sonar analysis
- **[2022 Solutions](src/main/java/odogwudozilla/year2022/README.md)** - Elf expedition with calorie counting
- **[2023 Solutions](src/main/java/odogwudozilla/year2023/README.md)** - Garden plots and infinite grids
- **[2024 Solutions](src/main/java/odogwudozilla/year2024/README.md)** - Computer systems and algorithms
- **[2025 Solutions](src/main/java/odogwudozilla/year2025/README.md)** - Journey through North Pole headquarters (12 days)

## 🔗 Useful Links

- [Advent of Code Official Site](https://adventofcode.com/)
- [AoC Reddit Community](https://www.reddit.com/r/adventofcode/)
- [AoC Wiki](https://adventofcode.fandom.com/)

## 📄 License

This project is for educational purposes. Advent of Code puzzles are created by [Eric Wastl](http://was.tl/).

---

**Happy Coding! 🎄✨**
