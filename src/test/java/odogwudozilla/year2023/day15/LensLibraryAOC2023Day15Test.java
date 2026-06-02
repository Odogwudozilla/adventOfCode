package odogwudozilla.year2023.day15;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LensLibraryAOC2023Day15Test {

    @Test
    void givenSingleHashToken_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of("HASH");

        String result = invokeSolvePartOne(input);

        assertEquals("52", result);
    }

    @Test
    void givenWorkedExampleSequence_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7");

        String result = invokeSolvePartOne(input);

        assertEquals("1320", result);
    }

    @Test
    void givenWorkedExampleSplitAcrossLines_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
                "rn=1,cm-,qp=3,cm=2,q",
                "p-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        );

        String result = invokeSolvePartOne(input);

        assertEquals("1320", result);
    }

    @Test
    void givenWorkedExampleSequence_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7");

        String result = invokeSolvePartTwo(input);

        assertEquals("145", result);
    }

    @Test
    void givenWorkedExampleSplitAcrossLines_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
                "rn=1,cm-,qp=3,cm=2,q",
                "p-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        );

        String result = invokeSolvePartTwo(input);

        assertEquals("145", result);
    }

    private String invokeSolvePartOne(List<String> input) {
        try {
            var method = LensLibraryAOC2023Day15.class.getDeclaredMethod("solvePartOne", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private String invokeSolvePartTwo(List<String> input) {
        try {
            var method = LensLibraryAOC2023Day15.class.getDeclaredMethod("solvePartTwo", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}



