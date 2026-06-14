package odogwudozilla.year2017.day15;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuelingGeneratorsAOC2017Day15Test {

    @Test
    void givenSampleInput_solvePartOne_returnsExpectedMatchCount() {
        List<String> lines = List.of(
                "Generator A starts with 65",
                "Generator B starts with 8921"
        );

        assertEquals("588", DuelingGeneratorsAOC2017Day15.solvePartOne(lines));
    }

    @Test
    void givenSampleInput_solvePartTwo_returnsExpectedMatchCount() {
        List<String> lines = List.of(
                "Generator A starts with 65",
                "Generator B starts with 8921"
        );

        assertEquals("309", DuelingGeneratorsAOC2017Day15.solvePartTwo(lines));
    }
}

