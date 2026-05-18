package odogwudozilla.year2019.day14;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceStoichiometryAOC2019Day14Test {

    @Test
    void givenSimpleExample_solvePartOne_returns31() {
        List<String> lines = List.of(
            "10 ORE => 10 A",
            "1 ORE => 1 B",
            "7 A, 1 B => 1 C",
            "7 A, 1 C => 1 D",
            "7 A, 1 D => 1 E",
            "7 A, 1 E => 1 FUEL"
        );
        assertEquals("31", SpaceStoichiometryAOC2019Day14.solvePartOne(lines));
    }

    @Test
    void givenBranchingExample_solvePartOne_returns165() {
        List<String> lines = List.of(
            "9 ORE => 2 A",
            "8 ORE => 3 B",
            "7 ORE => 5 C",
            "3 A, 4 B => 1 AB",
            "5 B, 7 C => 1 BC",
            "4 C, 1 A => 1 CA",
            "2 AB, 3 BC, 4 CA => 1 FUEL"
        );
        assertEquals("165", SpaceStoichiometryAOC2019Day14.solvePartOne(lines));
    }

    @Test
    void givenSimpleExample_solvePartTwo_findsMaxFuelFrom1Trillion() {
        // Simple linear chain example: Part 1 returns 31 ORE per FUEL
        // Part 2: binary search to find max FUEL from 1 trillion ORE
        List<String> lines = List.of(
            "10 ORE => 10 A",
            "1 ORE => 1 B",
            "7 A, 1 B => 1 C",
            "7 A, 1 C => 1 D",
            "7 A, 1 D => 1 E",
            "7 A, 1 E => 1 FUEL"
        );
        // With 1 trillion ORE and ~31 ORE per FUEL,
        // we expect around 32+ billion FUEL (with leftover efficiency gains)
        String result = SpaceStoichiometryAOC2019Day14.solvePartTwo(lines);
        long fuelProduced = Long.parseLong(result);
        // Verify the answer is plausible and reasonable
        assertTrue(fuelProduced > 20_000_000_000L, "Should produce more than 20 billion FUEL");
        assertTrue(fuelProduced < 50_000_000_000L, "Should produce less than 50 billion FUEL");
    }

    @Test
    void givenBranchingExample_solvePartTwo_findsMaxFuelFrom1Trillion() {
        // Branching example: Part 1 returns 165 ORE per FUEL
        // Part 2: binary search to find max FUEL from 1 trillion ORE
        List<String> lines = List.of(
            "9 ORE => 2 A",
            "8 ORE => 3 B",
            "7 ORE => 5 C",
            "3 A, 4 B => 1 AB",
            "5 B, 7 C => 1 BC",
            "4 C, 1 A => 1 CA",
            "2 AB, 3 BC, 4 CA => 1 FUEL"
        );
        // With 1 trillion ORE and ~165 ORE per FUEL,
        // we expect around 6+ billion FUEL (with leftover efficiency gains)
        String result = SpaceStoichiometryAOC2019Day14.solvePartTwo(lines);
        long fuelProduced = Long.parseLong(result);
        // Verify the answer is plausible
        assertTrue(fuelProduced > 3_000_000_000L, "Should produce more than 3 billion FUEL");
        assertTrue(fuelProduced < 10_000_000_000L, "Should produce less than 10 billion FUEL");
    }
}








