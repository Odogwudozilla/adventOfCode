package odogwudozilla.year2023.day12;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link HotSpringsAOC2023Day12} Parts 1 and 2.
 * <p>
 * Example inputs and expected values are taken from the AoC 2023 Day 12 puzzle description.
 * The six example rows produce a total of 21 for Part 1 and 525152 for Part 2 (×5 unfolded).
 */
class HotSpringsAOC2023Day12Test {

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> lines = List.of(
                "???.### 1,1,3",
                ".??..??...?##. 1,1,3",
                "?#?#?#?#?#?#?#? 1,3,1,6",
                "????.#...#... 4,1,1",
                "????.######..#####. 1,6,5",
                "?###???????? 3,2,1"
        );
        assertEquals("21", HotSpringsAOC2023Day12.solvePartOne(lines));
    }

    @Test
    void givenRow1_solvePartOne_returns1() {
        assertEquals("1", HotSpringsAOC2023Day12.solvePartOne(List.of("???.### 1,1,3")));
    }

    @Test
    void givenRow2_solvePartOne_returns4() {
        assertEquals("4", HotSpringsAOC2023Day12.solvePartOne(List.of(".??..??...?##. 1,1,3")));
    }

    @Test
    void givenRow3_solvePartOne_returns1() {
        assertEquals("1", HotSpringsAOC2023Day12.solvePartOne(List.of("?#?#?#?#?#?#?#? 1,3,1,6")));
    }

    @Test
    void givenRow4_solvePartOne_returns1() {
        assertEquals("1", HotSpringsAOC2023Day12.solvePartOne(List.of("????.#...#... 4,1,1")));
    }

    @Test
    void givenRow5_solvePartOne_returns4() {
        assertEquals("4", HotSpringsAOC2023Day12.solvePartOne(List.of("????.######..#####. 1,6,5")));
    }

    @Test
    void givenRow6_solvePartOne_returns10() {
        assertEquals("10", HotSpringsAOC2023Day12.solvePartOne(List.of("?###???????? 3,2,1")));
    }

    // -----------------------------------------------------------------------
    // Part 2 tests — six example rows unfolded ×5 (expected total: 525152)
    // -----------------------------------------------------------------------

    @Test
    void givenExampleInput_solvePartTwo_returns525152() {
        List<String> lines = List.of(
                "???.### 1,1,3",
                ".??..??...?##. 1,1,3",
                "?#?#?#?#?#?#?#? 1,3,1,6",
                "????.#...#... 4,1,1",
                "????.######..#####. 1,6,5",
                "?###???????? 3,2,1"
        );
        assertEquals("525152", HotSpringsAOC2023Day12.solvePartTwo(lines));
    }

    @Test
    void givenRow1Unfolded_solvePartTwo_returns1() {
        assertEquals("1", HotSpringsAOC2023Day12.solvePartTwo(List.of("???.### 1,1,3")));
    }

    @Test
    void givenRow2Unfolded_solvePartTwo_returns16384() {
        assertEquals("16384", HotSpringsAOC2023Day12.solvePartTwo(List.of(".??..??...?##. 1,1,3")));
    }

    @Test
    void givenRow3Unfolded_solvePartTwo_returns1() {
        assertEquals("1", HotSpringsAOC2023Day12.solvePartTwo(List.of("?#?#?#?#?#?#?#? 1,3,1,6")));
    }

    @Test
    void givenRow4Unfolded_solvePartTwo_returns16() {
        assertEquals("16", HotSpringsAOC2023Day12.solvePartTwo(List.of("????.#...#... 4,1,1")));
    }

    @Test
    void givenRow5Unfolded_solvePartTwo_returns2500() {
        assertEquals("2500", HotSpringsAOC2023Day12.solvePartTwo(List.of("????.######..#####. 1,6,5")));
    }

    @Test
    void givenRow6Unfolded_solvePartTwo_returns506250() {
        assertEquals("506250", HotSpringsAOC2023Day12.solvePartTwo(List.of("?###???????? 3,2,1")));
    }
}



