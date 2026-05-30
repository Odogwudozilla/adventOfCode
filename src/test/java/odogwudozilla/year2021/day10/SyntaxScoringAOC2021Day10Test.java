package odogwudozilla.year2021.day10;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SyntaxScoringAOC2021Day10Test {
    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "[({(<(())[]>[[{[]{<()<>>",
            "[(()[<>])]({[<{<<[]>>(",
            "{([(<{}[<>[]}>{[]{[(<()>",
            "(((({<>}<{<{<>}{[]{[]{}",
            "[[<[([]))<([[{}[[()]]]",
            "[{[{({}]{}}([{[{{{}}([]",
            "{<[[]]>}<{[{[{[]{()[[[]",
            "[<(<(<(<{}))><([]([]()",
            "<{([([[(<>()){}]>(<<{{",
            "<{([{{}}[<[[[<>{}]]]>[]]"
        );
        String result = callSolvePartOne(input);
        assertEquals("26397", result);
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "[({(<(())[]>[[{[]{<()<>>",
            "[(()[<>])]({[<{<<[]>>(",
            "{([(<{}[<>[]}>{[]{[(<()>",
            "(((({<>}<{<{<>}{[]{[]{}",
            "[[<[([]))<([[{}[[()]]]",
            "[{[{({}]{}}([{[{{{}}([]",
            "{<[[]]>}<{[{[{[]{()[[[]",
            "[<(<(<(<{}))><([]([]()",
            "<{([([[(<>()){}]>(<<{{",
            "<{([{{}}[<[[[<>{}]]]>[]]"
        );
        String result = callSolvePartTwo(input);
        assertEquals("288957", result);
    }

    // Helper to call the private static method
    private String callSolvePartOne(List<String> input) {
        try {
            var method = SyntaxScoringAOC2021Day10.class.getDeclaredMethod("solvePartOne", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String callSolvePartTwo(List<String> input) {
        try {
            var method = SyntaxScoringAOC2021Day10.class.getDeclaredMethod("solvePartTwo", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
