# Quick Start Guide: Enhancement Features

Nnam Odogwu. This guide helps you quickly integrate and use the new enhancement features in your Advent of Code puzzle solutions.

## 5-Minute Setup

### 1. Build the Project
```bash
cd C:\Users\cnnachor\javaProjects\adventOfCode
.\gradlew build -x test -q
```

### 2. View Example Usage
Run the example class to see all features in action:
```bash
.\gradlew runEnhancementExample -q
```

**Note:** This command properly includes all dependencies (including Jackson ObjectMapper) and will display all 6 enhancement features with working examples.

### 3. View Generated Artifacts
After running the example, check:
```bash
# View cached puzzle results
Get-ChildItem cache\puzzle-results\

# View generated dashboard
Get-ChildItem dashboard\
```

### 4. View Documentation
- **Full Documentation:** `ENHANCEMENT_FEATURES.md`
- **Implementation Details:** `IMPLEMENTATION_SUMMARY.md`
- **Live Demo Results:** `DEMONSTRATION_RESULTS.md`

---

## Integration into Your Puzzle Solutions

### Basic Template

Add this to your puzzle's main method:

```java
public static void main(String[] args) {
    // 1. Initialize monitoring and testing tools
    PuzzlePerformanceMonitor monitor = new PuzzlePerformanceMonitor("2025", "day1");
    PuzzleTestHarness harness = new PuzzleTestHarness("2025", "day1");
    PuzzleCacheManager cache = new PuzzleCacheManager();
    
    try {
        // 2. Solve Part 1
        monitor.startTiming("Part 1");
        String result1 = solvePart1();
        monitor.stopTiming("Part 1");
        
        // Cache and test result
        cache.cachePuzzleResult("2025", "day1", 1, result1, 
                               monitor.getExecutionTime("Part 1"));
        harness.recordActualResult("Part 1", result1);
        
        // 3. Solve Part 2
        monitor.startTiming("Part 2");
        String result2 = solvePart2();
        monitor.stopTiming("Part 2");
        
        // Cache and test result
        cache.cachePuzzleResult("2025", "day1", 2, result2,
                               monitor.getExecutionTime("Part 2"));
        harness.recordActualResult("Part 2", result2);
        
        // 4. Display results
        System.out.println("Part 1 Result: " + result1);
        System.out.println("Part 2 Result: " + result2);
        
        monitor.printPerformanceReport();
        harness.printTestReport();
        
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
    }
}
```

### Required Imports

```java
import odogwudozilla.core.PuzzlePerformanceMonitor;
import odogwudozilla.core.PuzzleTestHarness;
import odogwudozilla.core.PuzzleCacheManager;
import odogwudozilla.core.PuzzleDifficultyRater;
import odogwudozilla.core.SolutionStatisticsCollector;
import odogwudozilla.dashboard.WebDashboardGenerator;
```

---

## Feature Usage Examples

### Testing Results

```java
// Set expected results (if you know them)
harness.setExpectedResult("Part 1", "138");
harness.setExpectedResult("Part 2", "1771");

// Record actual results
harness.recordActualResult("Part 1", "138");
harness.recordActualResult("Part 2", "1771");

// Print formatted test report
harness.printTestReport();
```

### Performance Monitoring

```java
// Time a solution
monitor.startTiming("Part 1");
String result = solvePuzzle();
monitor.stopTiming("Part 1");

// Get metrics
long executionTime = monitor.getExecutionTime("Part 1");
long memoryUsed = monitor.getMemoryUsage("Part 1");

System.out.println("Time: " + executionTime + "ms");
System.out.println("Memory: " + memoryUsed + " bytes");

// Print full report
monitor.printPerformanceReport();
```

### Caching Results

```java
// Cache a result
cache.cachePuzzleResult("2025", "day1", 1, "138", 45);

// Check if cached
if (cache.isCached("2025", "day1", 1)) {
    String cached = cache.getCachedResult("2025", "day1", 1);
    System.out.println("Previous answer: " + cached);
}

// Compare current with cached
boolean matches = cache.compareWithCache("2025", "day1", 1, "138");
System.out.println("Correct: " + matches);
```

### Difficulty Rating

```java
PuzzleDifficultyRater rater = new PuzzleDifficultyRater();

// Rate based on execution metrics and code size
int difficulty = rater.rateDifficulty("Part 1", 45, 51200, 78);

System.out.println("Difficulty: " + difficulty + "/10");
System.out.println("Level: " + rater.getDifficultyLabel("Part 1"));

rater.printDifficultyReport();
```

### Statistics Collection

```java
SolutionStatisticsCollector stats = new SolutionStatisticsCollector();

// Record stats for a solution
stats.recordStatistics("2025-day1", "Part 1", 45, 51200, 78, 
                       "Linear scan");

// Get performance leaders
System.out.println("Average time: " + stats.getAverageExecutionTime() + "ms");
System.out.println("Fastest: " + stats.getFastestSolution());

stats.printStatisticsReport();
```

### Web Dashboard

```java
WebDashboardGenerator dashboard = new WebDashboardGenerator();

// Add solved puzzles
dashboard.addPuzzleData("2025", "1", "Secret Entrance", true, true, 45);
dashboard.addPuzzleData("2025", "2", "Red-Nosed Reindeer", true, false, 67);

// Generate HTML dashboard
dashboard.generateDashboard();

System.out.println("Dashboard: " + dashboard.getDashboardPath());
```

---

## File Locations

### Source Code
```
src/main/java/odogwudozilla/
├── core/
│   ├── PuzzlePerformanceMonitor.java
│   ├── PuzzleTestHarness.java
│   ├── PuzzleCacheManager.java
│   ├── PuzzleDifficultyRater.java
│   └── SolutionStatisticsCollector.java
├── dashboard/
│   └── WebDashboardGenerator.java
└── examples/
    └── EnhancementFeaturesExample.java
```

### Output Locations
```
cache/puzzle-results/
└── YYYY_dayD_partN.json         (puzzle result caches)

dashboard/
└── index.html                   (generated dashboard)
```

### Documentation
```
ENHANCEMENT_FEATURES.md          (complete feature documentation)
IMPLEMENTATION_SUMMARY.md        (implementation details and status)
QUICK_START.md                   (this file)
```

---

## Common Patterns

### Pattern 1: Basic Puzzle Solution with Testing
```java
PuzzlePerformanceMonitor monitor = new PuzzlePerformanceMonitor("2025", "day1");
PuzzleTestHarness harness = new PuzzleTestHarness("2025", "day1");

monitor.startTiming("Part 1");
String result = solvePuzzle();
monitor.stopTiming("Part 1");

harness.setExpectedResult("Part 1", EXPECTED);
harness.recordActualResult("Part 1", result);

harness.printTestReport();
monitor.printPerformanceReport();
```

### Pattern 2: Solution with Caching and Validation
```java
PuzzleCacheManager cache = new PuzzleCacheManager();
String result = solvePuzzle();

// Check if correct
if (cache.compareWithCache("2025", "day1", 1, result)) {
    System.out.println("Correct!");
} else {
    System.out.println("Incorrect. Cached answer: " + 
                       cache.getCachedResult("2025", "day1", 1));
}

// Save for next time
cache.cachePuzzleResult("2025", "day1", 1, result, 45);
```

### Pattern 3: Performance-Optimized Solution
```java
PuzzlePerformanceMonitor monitor = new PuzzlePerformanceMonitor("2025", "day1");
PuzzleDifficultyRater rater = new PuzzleDifficultyRater();

monitor.startTiming("Part 1");
String result = fastSolution();
monitor.stopTiming("Part 1");

int difficulty = rater.rateDifficulty("Part 1", 
                                      monitor.getExecutionTime("Part 1"),
                                      monitor.getMemoryUsage("Part 1"),
                                      codeLinesCount);

System.out.println("Difficulty: " + rater.getDifficultyLabel("Part 1"));
monitor.printPerformanceReport();
```

---

## Troubleshooting

### Cache not persisting?
- Check that `cache/puzzle-results/` directory is created
- Verify write permissions in project directory
- Check console for error messages

### Dashboard not generating?
- Ensure `dashboard/` directory is created
- Check that puzzle data is added before generation
- Open `dashboard/index.html` in web browser

### Performance metrics showing zero?
- Ensure `startTiming()` and `stopTiming()` are called in pairs
- Verify part names are consistent (e.g., "Part 1", "Part 2")
- Check system permissions for memory profiling

---

## Next Steps

1. **Read Full Documentation:** See `ENHANCEMENT_FEATURES.md`
2. **Run Examples:** Execute `EnhancementFeaturesExample.java`
3. **Integrate Features:** Add to your puzzle solutions
4. **Generate Dashboard:** Create visual progress tracker
5. **Optimize Solutions:** Use difficulty ratings to focus efforts

---

## Support Resources

| Resource | Purpose |
|----------|---------|
| `ENHANCEMENT_FEATURES.md` | Comprehensive feature guide |
| `IMPLEMENTATION_SUMMARY.md` | Technical implementation details |
| `EnhancementFeaturesExample.java` | Live code examples |
| Source code (core/*.java) | Full API documentation via JavaDoc |

---

**Last Updated:** December 13, 2025
**Status:** Ready for Production Use

