package odogwudozilla.year2021.day13;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransparentOrigamiAOC2021Day13Test {

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
            "6,10",
            "0,14",
            "9,10",
            "0,3",
            "10,4",
            "4,11",
            "6,0",
            "6,12",
            "4,1",
            "0,13",
            "10,12",
            "3,4",
            "3,0",
            "8,4",
            "1,10",
            "2,14",
            "8,10",
            "9,0",
            "",
            "fold along y=7",
            "fold along x=5"
        );

        String result = callSolvePartOne(input);
        assertEquals("17", result);
    }

    @Test
    void givenRenderedLetterInput_solvePartTwo_returnsDecodedMessage() {
        List<String> input = List.of(
            "1,0",
            "2,0",
            "0,1",
            "3,1",
            "0,2",
            "3,2",
            "0,3",
            "3,3",
            "0,4",
            "3,4",
            "1,5",
            "2,5"
        );

        String result = callSolvePartTwo(input);
        assertEquals("O", result);
    }

    private String callSolvePartOne(List<String> input) {
        try {
            var method = TransparentOrigamiAOC2021Day13.class.getDeclaredMethod("solvePartOne", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String callSolvePartTwo(List<String> input) {
        try {
            var method = TransparentOrigamiAOC2021Day13.class.getDeclaredMethod("solvePartTwo", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

