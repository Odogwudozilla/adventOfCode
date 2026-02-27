# Advent of Code 2016

This README tracks all implemented solutions for Advent of Code 2016.

## Implemented Puzzles

| Day | Title                   | Link                                         | Description                                      | Source Code Link                                                                 |
|-----|-------------------------|----------------------------------------------|--------------------------------------------------|----------------------------------------------------------------------------------|
| 1   | No Time for a Taxicab   | https://adventofcode.com/2016/day/1          | Part 1: Compute Manhattan distance to Easter Bunny HQ.<br>Part 2: Find the first location visited twice and its distance. | [NoTimeForATaxicabAOC2016Day1.java](./day1/NoTimeForATaxicabAOC2016Day1.java)    |
| 4   | Security Through Obscurity | https://adventofcode.com/2016/day/4      | Part 1: Sum the sector IDs of all real rooms by validating checksums.<br>Part 2: Decrypt names and find the sector ID for North Pole objects room. | [SecurityThroughObscurityAOC2016Day4.java](./day4/SecurityThroughObscurityAOC2016Day4.java) |
| 12  | Leonardo's Monorail        | https://adventofcode.com/2016/day/12          | Part 1: Execute assembunny code to determine value in register 'a'.<br>Part 2: Repeat with register 'c' starting at 1. | [LeonardosMonorailAOC2016Day12.java](./day12/LeonardosMonorailAOC2016Day12.java) |
| 17  | Two Steps Forward | https://adventofcode.com/2016/day/17 | Part 1: Navigate a 4x4 grid where doors open based on MD5 hash of passcode + path. Find the shortest path to the vault.<br>Part 2: Find the length of the longest path to reach the vault. | [TwoStepsForwardAOC2016Day17.java](./day17/TwoStepsForwardAOC2016Day17.java) |
| 19  | An Elephant Named Joseph | https://adventofcode.com/2016/day/19 | Part 1: Elves sit in a circle, each with a present. Each Elf takes all presents from the Elf to their left, removing Elves with no presents. Find which Elf gets all the presents.<br>Part 2: Elves steal from the Elf directly across the circle. Find which Elf gets all the presents. | [ElephantNamedJosephAOC2016Day19.java](./day19/ElephantNamedJosephAOC2016Day19.java) |
| 20  | Firewall Rules           | https://adventofcode.com/2016/day/20          | Part 1: Find the lowest-valued allowed IP.<br>Part 2: Count the total number of allowed IPs. | [FirewallRulesAOC2016Day20.java](./day20/FirewallRulesAOC2016Day20.java)        |
| 21  | Scrambled Letters and Hash | https://adventofcode.com/2016/day/21       | Part 1: Apply scrambling operations to password "abcdefgh".<br>Part 2: Reverse scrambling operations to unscramble "fbgdceah". | [ScrambledLettersAndHashAOC2016Day21.java](./day21/ScrambledLettersAndHashAOC2016Day21.java) |
| 22  | Grid Computing           | https://adventofcode.com/2016/day/22          | Part 1: Count the number of viable pairs of nodes in a storage grid. | [GridComputingAOC2016Day22.java](./day22/GridComputingAOC2016Day22.java)        |

## Navigation
- [Back to main README](../../../README.md)

---

### Notes
- Algorithm: Follows turn instructions, updates direction, and sums Manhattan distance.
