package odogwudozilla.year2015.day9;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AllInASingleNightAOC2015Day9Test {

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "London to Dublin = 464",
            "London to Belfast = 518",
            "Dublin to Belfast = 141"
        );

        assertEquals("605", AllInASingleNightAOC2015Day9.solvePartOne(input));
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "London to Dublin = 464",
            "London to Belfast = 518",
            "Dublin to Belfast = 141"
        );

        assertEquals("982", AllInASingleNightAOC2015Day9.solvePartTwo(input));
    }
}
