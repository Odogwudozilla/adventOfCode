package odogwudozilla.year2021.day9;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SmokeBasinAOC2021Day9Test {
    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "2199943210",
            "3987894921",
            "9856789892",
            "8767896789",
            "9899965678"
        );
        String result = invokeSolvePartOne(input);
        assertEquals("15", result);
    }

    // Helper to access private static method
    private String invokeSolvePartOne(List<String> input) {
        try {
            var method = SmokeBasinAOC2021Day9.class.getDeclaredMethod("solvePartOne", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "2199943210",
            "3987894921",
            "9856789892",
            "8767896789",
            "9899965678"
        );
        String result = invokeSolvePartTwo(input);
        assertEquals("1134", result);
    }

    // Helper to access private static method for Part 2
    private String invokeSolvePartTwo(List<String> input) {
        try {
            var method = SmokeBasinAOC2021Day9.class.getDeclaredMethod("solvePartTwo", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
