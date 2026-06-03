package odogwudozilla.year2017.day10;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KnotHashAOC2017Day10Test {

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> lines = List.of("3,4,1,5");

        assertEquals("12", KnotHashAOC2017Day10.solvePartOne(lines, 5));
    }

    @Test
    void givenEmptyStringInput_solvePartTwo_returnsExpectedKnotHash() {
        List<String> lines = List.of("");

        assertEquals("a2582a3a0e66e6e86e3812dcb672a272", KnotHashAOC2017Day10.solvePartTwo(lines));
    }
}
