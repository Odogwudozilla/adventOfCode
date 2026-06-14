package odogwudozilla.year2022.day9;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RopeBridgeAOC2022Day9Test {

    @Test
    void givenPartOneExample_solvePartOne_returns13() {
        List<String> input = List.of(
                "R 4",
                "U 4",
                "L 3",
                "D 1",
                "R 4",
                "D 1",
                "L 5",
                "R 2"
        );

        assertEquals("13", RopeBridgeAOC2022Day9.solvePartOne(input));
    }

    @Test
    void givenPartOneExample_solvePartTwo_returns1() {
        List<String> input = List.of(
                "R 4",
                "U 4",
                "L 3",
                "D 1",
                "R 4",
                "D 1",
                "L 5",
                "R 2"
        );

        assertEquals("1", RopeBridgeAOC2022Day9.solvePartTwo(input));
    }

    @Test
    void givenPartTwoExtendedExample_solvePartTwo_returns36() {
        List<String> input = List.of(
                "R 5",
                "U 8",
                "L 8",
                "D 3",
                "R 17",
                "D 10",
                "L 25",
                "U 20"
        );

        assertEquals("36", RopeBridgeAOC2022Day9.solvePartTwo(input));
    }
}

