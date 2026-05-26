package odogwudozilla.year2023.day14;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParabolicReflectorDishAOC2023Day14Test {
    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "O....#....",
            "O.OO#....#",
            ".....##...",
            "OO.#O....O",
            ".O.....O#.",
            "O.#..O.#.#",
            "..O..#O..O",
            ".......O..",
            "#....###..",
            "#OO..#...."
        );
        String result = invokeSolvePartOne(input);
        assertEquals("136", result);
    }

    // Helper to access private static method
    private String invokeSolvePartOne(List<String> input) {
        try {
            var method = ParabolicReflectorDishAOC2023Day14.class.getDeclaredMethod("solvePartOne", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "O....#....",
            "O.OO#....#",
            ".....##...",
            "OO.#O....O",
            ".O.....O#.",
            "O.#..O.#.#",
            "..O..#O..O",
            ".......O..",
            "#....###..",
            "#OO..#...."
        );
        String result = invokeSolvePartTwo(input);
        assertEquals("64", result);
    }

    // Helper to access private static method for Part 2
    private String invokeSolvePartTwo(List<String> input) {
        try {
            var method = ParabolicReflectorDishAOC2023Day14.class.getDeclaredMethod("solvePartTwo", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
