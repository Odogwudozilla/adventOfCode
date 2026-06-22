package odogwudozilla.year2021.day14;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtendedPolymerizationAOC2021Day14Test {

    private static final List<String> EXAMPLE_INPUT = List.of(
        "NNCB",
        "",
        "CH -> B",
        "HH -> N",
        "CB -> H",
        "NH -> C",
        "HB -> C",
        "HC -> B",
        "HN -> C",
        "NN -> C",
        "BH -> H",
        "NC -> B",
        "NB -> B",
        "BN -> B",
        "BB -> N",
        "BC -> B",
        "CC -> N",
        "CN -> C"
    );

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        String result = callSolvePartOne(EXAMPLE_INPUT);
        assertEquals("1588", result);
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() {
        String result = callSolvePartTwo(EXAMPLE_INPUT);
        assertEquals("2188189693529", result);
    }

    private String callSolvePartOne(List<String> input) {
        try {
            var method = ExtendedPolymerizationAOC2021Day14.class.getDeclaredMethod("solvePartOne", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String callSolvePartTwo(List<String> input) {
        try {
            var method = ExtendedPolymerizationAOC2021Day14.class.getDeclaredMethod("solvePartTwo", List.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


