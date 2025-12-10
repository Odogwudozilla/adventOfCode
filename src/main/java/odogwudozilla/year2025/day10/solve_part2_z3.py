"""
Advent of Code 2025 Day 10 Part 2 - Z3 Solver
Uses Z3 theorem prover to solve the integer linear programming problem.

REQUIREMENTS:
    - Python 3.x
    - z3-solver package: pip install z3-solver

USAGE:
    python solve_part2_z3.py [input_file_path]

    If no input file is provided, the script will search for the puzzle data file
    in common locations relative to the script location.

ANSWER: 18687 button presses
"""

from z3 import *
import re
import os
import sys

def find_input_file():
    """
    Find the puzzle input file by searching in multiple locations.
    Returns the path to the input file if found, otherwise None.
    """
    # Get the directory where this script is located
    script_dir = os.path.dirname(os.path.abspath(__file__))

    # List of possible input file locations (relative to script location)
    possible_paths = [
        # Same directory as script
        os.path.join(script_dir, 'day10_puzzle_data.txt'),
        # Resources folder structure
        os.path.join(script_dir, '..', '..', '..', '..', 'resources', '2025', 'day10', 'day10_puzzle_data.txt'),
        # Alternative: from project root
        os.path.join(script_dir, '..', '..', '..', '..', '..', '..', 'src', 'main', 'resources', '2025', 'day10', 'day10_puzzle_data.txt'),
        # Current working directory
        os.path.join(os.getcwd(), 'src', 'main', 'resources', '2025', 'day10', 'day10_puzzle_data.txt'),
        # Direct from current directory
        os.path.join(os.getcwd(), 'day10_puzzle_data.txt'),
    ]

    # Try each possible path
    for path in possible_paths:
        normalized_path = os.path.normpath(path)
        if os.path.exists(normalized_path):
            return normalized_path

    return None

def parse_machines(filename):
    """Parse the input file into machine data"""
    machines = []

    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            if not line:
                continue

            # Parse indicator lights (not needed for part 2)
            indicator_match = re.search(r'\[([.#]+)\]', line)
            if not indicator_match:
                continue

            # Parse buttons
            button_matches = re.findall(r'\(([0-9,]+)\)', line)
            buttons = []
            for button_str in button_matches:
                indices = [int(x.strip()) for x in button_str.split(',')]
                buttons.append(indices)

            # Parse joltage requirements
            joltage_match = re.search(r'\{([0-9,]+)\}', line)
            if joltage_match:
                joltage_str = joltage_match.group(1)
                joltage_requirements = [int(x.strip()) for x in joltage_str.split(',')]

                machines.append({
                    'buttons': buttons,
                    'joltage_requirements': joltage_requirements
                })

    return machines

def solve_machine_z3(machine):
    """Solve a single machine using Z3 optimizer"""
    buttons = machine['buttons']
    targets = machine['joltage_requirements']

    num_buttons = len(buttons)
    num_counters = len(targets)

    # Create Z3 optimizer
    opt = Optimize()

    # Create button press variables (integer >= 0)
    button_vars = [Int(f'button_{i}') for i in range(num_buttons)]

    # Add constraints: each button must be pressed >= 0 times
    for var in button_vars:
        opt.add(var >= 0)

    # Add constraints: for each counter, sum of button effects must equal target
    for counter_idx in range(num_counters):
        target = targets[counter_idx]

        # Find which buttons affect this counter
        affecting_buttons = []
        for btn_idx, button_indices in enumerate(buttons):
            if counter_idx in button_indices:
                # This button affects this counter (increments by 1 per press)
                affecting_buttons.append(button_vars[btn_idx])

        if affecting_buttons:
            # Sum of button presses affecting this counter must equal target
            opt.add(Sum(affecting_buttons) == target)

    # Minimize total button presses
    total_presses = Sum(button_vars)
    opt.minimize(total_presses)

    # Solve
    if opt.check() == sat:
        model = opt.model()
        result = sum(model.eval(var).as_long() for var in button_vars)
        return result
    else:
        print(f"No solution found for machine!")
        return 0

def main():
    """Main function to solve Part 2 using Z3"""
    # Check for command-line argument
    if len(sys.argv) > 1:
        input_file = sys.argv[1]
        if not os.path.exists(input_file):
            print(f"Error: Input file not found: {input_file}")
            sys.exit(1)
    else:
        # Try to find the input file automatically
        input_file = find_input_file()
        if input_file is None:
            print("Error: Could not find puzzle input file!")
            print("\nPlease provide the input file path as an argument:")
            print(f"  python {os.path.basename(__file__)} <path_to_day10_puzzle_data.txt>")
            print("\nOr place 'day10_puzzle_data.txt' in the same directory as this script.")
            sys.exit(1)

    print(f"Using input file: {input_file}")
    print("="*60)

    try:
        print("Parsing machines...")
        machines = parse_machines(input_file)
        print(f"Found {len(machines)} machines")

        print("\nSolving Part 2 with Z3...")
        total_presses = 0

        for i, machine in enumerate(machines):
            presses = solve_machine_z3(machine)
            total_presses += presses
            if i < 5 or i >= len(machines) - 2:  # Show first 5 and last 2
                print(f"Machine {i+1}: {presses} presses")
            elif i == 5:
                print("...")

        print(f"\n{'='*60}")
        print(f"Part 2 - Total minimum button presses: {total_presses}")
        print(f"{'='*60}")

    except FileNotFoundError as e:
        print(f"Error: Could not read file: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"Error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)

if __name__ == '__main__':
    main()

