package odogwudozilla.year2017.day12;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DigitalPlumberAOC2017Day12Test {

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> lines = List.of(
            "0 <-> 2",
            "1 <-> 1",
            "2 <-> 0, 3, 4",
            "3 <-> 2, 4",
            "4 <-> 2, 3, 6",
            "5 <-> 6",
            "6 <-> 4, 5"
        );

        assertEquals("6", DigitalPlumberAOC2017Day12.solvePartOne(lines));
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() {
        List<String> lines = List.of(
            "0 <-> 2",
            "1 <-> 1",
            "2 <-> 0, 3, 4",
            "3 <-> 2, 4",
            "4 <-> 2, 3, 6",
            "5 <-> 6",
            "6 <-> 4, 5"
        );

        assertEquals("2", DigitalPlumberAOC2017Day12.solvePartTwo(lines));
    }

    @Test
    void givenInputWithBlankLines_solvePartOne_ignoresEmptyRows() {
        List<String> lines = List.of(
            "0 <-> 2",
            "",
            "2 <-> 0"
        );

        assertEquals("2", DigitalPlumberAOC2017Day12.solvePartOne(lines));
    }

    @Test
    void givenInputWithBlankLines_solvePartTwo_ignoresEmptyRows() {
        List<String> lines = List.of(
            "0 <-> 2",
            "",
            "2 <-> 0"
        );

        assertEquals("1", DigitalPlumberAOC2017Day12.solvePartTwo(lines));
    }
}
