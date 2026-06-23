package odogwudozilla.year2015.day8;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchsticksAOC2015Day8Test {
    @Test
    void givenEmptyStringLiteral_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of("\"\"");
        assertEquals("2", MatchsticksAOC2015Day8.solvePartOne(input));
    }

    @Test
    void givenPlainStringLiteral_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of("\"abc\"");
        assertEquals("2", MatchsticksAOC2015Day8.solvePartOne(input));
    }

    @Test
    void givenEscapedQuoteStringLiteral_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of("\"aaa\\\"aaa\"");
        assertEquals("3", MatchsticksAOC2015Day8.solvePartOne(input));
    }

    @Test
    void givenHexEscapeStringLiteral_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of("\"\\x27\"");
        assertEquals("5", MatchsticksAOC2015Day8.solvePartOne(input));
    }

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "\"\"",
            "\"abc\"",
            "\"aaa\\\"aaa\"",
            "\"\\x27\""
        );
        assertEquals("12", MatchsticksAOC2015Day8.solvePartOne(input));
    }

    @Test
    void givenEmptyStringLiteral_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of("\"\"");
        assertEquals("4", MatchsticksAOC2015Day8.solvePartTwo(input));
    }

    @Test
    void givenPlainStringLiteral_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of("\"abc\"");
        assertEquals("4", MatchsticksAOC2015Day8.solvePartTwo(input));
    }

    @Test
    void givenEscapedQuoteStringLiteral_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of("\"aaa\\\"aaa\"");
        assertEquals("6", MatchsticksAOC2015Day8.solvePartTwo(input));
    }

    @Test
    void givenHexEscapeStringLiteral_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of("\"\\x27\"");
        assertEquals("5", MatchsticksAOC2015Day8.solvePartTwo(input));
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "\"\"",
            "\"abc\"",
            "\"aaa\\\"aaa\"",
            "\"\\x27\""
        );
        assertEquals("19", MatchsticksAOC2015Day8.solvePartTwo(input));
    }
}
