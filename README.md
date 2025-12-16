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
├── src/main/java/odogwudozilla/
│   ├── Main.java                          # Main entry point
│   ├── PuzzleRandomizer.java              # Random puzzle selector utility
│   ├── core/                              # Enhancement features (core utilities)
│   ├── dashboard/                         # Enhancement features (web dashboard)
│   ├── examples/                          # Enhancement features (usage examples)
│   └── year<YYYY>/
│       └── day<D>/
│           └── <PuzzleName>AOC<YYYY>Day<D>.java
│
├── src/main/resources/
│   ├── aoc_challenge_config.json          # Available years and days configuration
│   └── <YYYY>/
│       └── day<D>/
│           ├── day<D>_puzzle_description.txt
│           └── day<D>_puzzle_data.txt
│
├── docs/enhancements/                     # Enhancement features documentation
│   ├── README.md                          # Enhancement docs index
│   ├── QUICK_START.md                     # Quick integration guide
│   ├── ENHANCEMENT_FEATURES.md            # Complete feature documentation
│
├── cache/                                 # Runtime-generated cache
│   └── puzzle-results/                    # Cached puzzle results (JSON)
│
├── dashboard/                             # Runtime-generated dashboard
│   └── index.html                         # Progress dashboard
│
├── build.gradle.kts                        # Gradle build configuration
└── README.md                               # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java JDK 11 or higher**
- **Gradle** (or use the included Gradle wrapper)

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
| `./gradlew randomPuzzle` | Select a random unsolved puzzle |
| `./gradlew test` | Run unit tests |
| `./gradlew runDay<D>` | Run a specific day's solution (if task exists) |

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

## 📊 Years

Explore solutions organized by year:

- **[2015](src/main/java/odogwudozilla/year2015/README.md)** - The inaugural Advent of Code year
- **[2017](src/main/java/odogwudozilla/year2017/README.md)** - Assembly instructions and circular lists
- **[2018](src/main/java/odogwudozilla/year2018/README.md)** - Fabric cutting and sleigh systems
- **[2020](src/main/java/odogwudozilla/year2020/README.md)** - Tropical vacation themed puzzles
- **[2021](src/main/java/odogwudozilla/year2021/README.md)** - Submarine adventure with sonar analysis
- **[2022](src/main/java/odogwudozilla/year2022/README.md)** - Elf expedition with calorie counting
- **[2023](src/main/java/odogwudozilla/year2023/README.md)** - Garden plots and infinite grids
- **[2024](src/main/java/odogwudozilla/year2024/README.md)** - Computer systems and algorithms
- **[2025](src/main/java/odogwudozilla/year2025/README.md)** - Journey through North Pole headquarters (12 days)

## 🔗 Useful Links

- [Advent of Code Official Site](https://adventofcode.com/)
- [AoC Reddit Community](https://www.reddit.com/r/adventofcode/)
- [AoC Wiki](https://adventofcode.fandom.com/)

## 📄 License

This project is for educational purposes. Advent of Code puzzles are created by [Eric Wastl](http://was.tl/).

---

**Happy Coding! 🎄✨**

