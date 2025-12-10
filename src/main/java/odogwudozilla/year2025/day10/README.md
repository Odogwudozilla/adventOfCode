# Advent of Code 2025 - Day 10: Factory

## Solution Overview

This puzzle has two parts with different solution approaches:

### Part 1: Indicator Lights (Java)
- **Approach**: Gaussian elimination over GF(2) (modulo 2 arithmetic)
- **Answer**: 459
- **Implementation**: `FactoryAOC2025Day10.java`

### Part 2: Joltage Configuration (Python + Z3)
- **Approach**: Integer Linear Programming (ILP) using Z3 theorem prover
- **Answer**: 18687
- **Implementation**: `solve_part2_z3.py`

## Why Python for Part 2?

Part 2 is an integer linear programming problem that requires finding the minimum number of button presses where each button increases specific counters by 1. This type of optimization problem is best solved using specialized solvers like:
- Z3 (Microsoft's theorem prover)
- PuLP (Python linear programming)
- OR-Tools (Google's optimization toolkit)

While Java has some ILP libraries, they are not as readily available or easy to integrate. Python with Z3 provides a clean, efficient solution.

## Running Part 2 (Python Script)

### Prerequisites
1. **Python 3.x** installed on your system
2. **z3-solver** package

### Installation

Install the Z3 solver:
```bash
pip install z3-solver
```

### Running the Script

The script automatically finds the input file in common locations:

```bash
# From the script directory
cd src/main/java/odogwudozilla/year2025/day10
python solve_part2_z3.py

# From project root
python src/main/java/odogwudozilla/year2025/day10/solve_part2_z3.py

# Or specify the input file explicitly
python solve_part2_z3.py path/to/day10_puzzle_data.txt
```

### Expected Output
```
Using input file: .../day10_puzzle_data.txt
============================================================
Parsing machines...
Found 171 machines

Solving Part 2 with Z3...
Machine 1: 58 presses
Machine 2: 49 presses
...
Machine 171: 260 presses

============================================================
Part 2 - Total minimum button presses: 18687
============================================================
```

## Running the Complete Java Solution

The Java solution solves Part 1 correctly and uses a greedy approximation for Part 2 (which is NOT optimal):

```bash
# Compile and run
./gradlew build
java -cp "build/classes/java/main:build/resources/main" odogwudozilla.year2025.day10.FactoryAOC2025Day10
```

**Note**: The Java implementation shows ~70,000+ for Part 2 (greedy approximation), but the correct answer is 18687 (obtained via Z3).

## Files in This Directory

- `FactoryAOC2025Day10.java` - Main Java solution (Part 1 correct, Part 2 approximation)
- `solve_part2_z3.py` - Python Z3 solver for Part 2 (correct optimal solution)
- `README.md` - This file

## Puzzle Input

The puzzle input file is located at:
```
src/main/resources/2025/day10/day10_puzzle_data.txt
```

## Links

- [Puzzle Description](https://adventofcode.com/2025/day/10)
- [Z3 Documentation](https://github.com/Z3Prover/z3)

