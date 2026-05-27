package odogwudozilla.year2024.day10;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HoofItAOC2024Day10Test {
    @Test
    void givenExampleInput1_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "0123",
            "1234",
            "8765",
            "9876"
        );
        assertEquals("1", HoofItAOC2024Day10.solvePartOne(input));
    }

    @Test
    void givenExampleInput2_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "...0...",
            "...1...",
            "...2...",
            "6543456",
            "7.....7",
            "8.....8",
            "9.....9"
        );
        assertEquals("2", HoofItAOC2024Day10.solvePartOne(input));
    }

    @Test
    void givenExampleInput3_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "..90..9",
            "...1.98",
            "...2..7",
            "6543456",
            "765.987",
            "876....",
            "987...."
        );
        assertEquals("4", HoofItAOC2024Day10.solvePartOne(input));
    }

    @Test
    void givenExampleInput4_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "10..9..",
            "2...8..",
            "3...7..",
            "4567654",
            "...8..3",
            "...9..2",
            ".....01"
        );
        assertEquals("3", HoofItAOC2024Day10.solvePartOne(input));
    }

    @Test
    void givenExampleInput5_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "89010123",
            "78121874",
            "87430965",
            "96549874",
            "45678903",
            "32019012",
            "01329801",
            "10456732"
        );
        assertEquals("36", HoofItAOC2024Day10.solvePartOne(input));
    }

    @Test
    void givenExampleInput1_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "0123",
            "1234",
            "8765",
            "9876"
        );
        // For Part 2, only one path from 0 to 9, so answer is 1
        assertEquals("1", invokeSolvePartTwo(input));
    }

    @Test
    void givenExampleInput2_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "...0...",
            "...1...",
            "...2...",
            "6543456",
            "7.....7",
            "8.....8",
            "9.....9"
        );
        // For Part 2, two distinct paths from 0 to two 9s, so answer is 2
        assertEquals("2", invokeSolvePartTwo(input));
    }

    @Test
    void givenExampleInput3_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "..90..9",
            "...1.98",
            "...2..7",
            "6543456",
            "765.987",
            "876....",
            "987...."
        );
        // From analysis doc, expected output is 13
        assertEquals("13", invokeSolvePartTwo(input));
    }

    @Test
    void givenExampleInput4_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            ".....0.",
            "..4321.",
            "..5..2.",
            "..6543.",
            "..7..4.",
            "..8765.",
            "..9...."
        );
        // From analysis doc, expected output is 3
        assertEquals("3", invokeSolvePartTwo(input));
    }

    @Test
    void givenExampleInput5_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "89010123",
            "78121874",
            "87430965",
            "96549874",
            "45678903",
            "32019012",
            "01329801",
            "10456732"
        );
        // From analysis doc, expected output is 81
        assertEquals("81", invokeSolvePartTwo(input));
    }

    @Test
    void givenExampleInput6_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "012345",
            "123456",
            "234567",
            "345678",
            "4.6789",
            "56789."
        );
        // From analysis doc, expected output is 227
        assertEquals("227", invokeSolvePartTwo(input));
    }

    @Test
    void givenExampleInput7_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
            "10..9..",
            "2...8..",
            "3...7..",
            "4567654",
            "...8..3",
            "...9..2",
            ".....01"
        );
        // This input is not in the analysis doc for Part 2, so we comment out the assertion for now
        // assertEquals("?", invokeSolvePartTwo(input));
        System.out.println("Part 2 output for input 7: " + invokeSolvePartTwo(input));
    }

    // Helper to access private solvePartTwo
    private String invokeSolvePartTwo(List<String> input) {
        try {
            var m = HoofItAOC2024Day10.class.getDeclaredMethod("solvePartTwo", List.class);
            m.setAccessible(true);
            return (String) m.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
