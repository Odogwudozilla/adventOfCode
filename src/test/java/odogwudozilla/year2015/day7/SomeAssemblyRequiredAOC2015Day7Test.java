package odogwudozilla.year2015.day7;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link SomeAssemblyRequiredAOC2015Day7}.
 * <p>
 * Example circuit from the puzzle description is used to verify individual
 * gate evaluations. Since the example has no wire {@code a}, a passthrough
 * instruction is appended so that {@code solvePartOne} can return a
 * deterministic answer.
 */
class SomeAssemblyRequiredAOC2015Day7Test {

    /** Example circuit shared by all Part 1 tests. */
    private static final List<String> EXAMPLE_CIRCUIT = List.of(
            "123 -> x",
            "456 -> y",
            "x AND y -> d",
            "x OR y -> e",
            "x LSHIFT 2 -> f",
            "y RSHIFT 2 -> g",
            "NOT x -> h",
            "NOT y -> i"
    );

    @Test
    void givenExampleInput_solvePartOne_returnsAndResult() {
        // d = 123 AND 456 = 72; route it through wire a for the assertion
        List<String> input = appendWireA(EXAMPLE_CIRCUIT, "d -> a");
        assertEquals("72", SomeAssemblyRequiredAOC2015Day7.solvePartOne(input));
    }

    @Test
    void givenExampleInput_solvePartOne_returnsOrResult() {
        // e = 123 OR 456 = 507
        List<String> input = appendWireA(EXAMPLE_CIRCUIT, "e -> a");
        assertEquals("507", SomeAssemblyRequiredAOC2015Day7.solvePartOne(input));
    }

    @Test
    void givenExampleInput_solvePartOne_returnsLshiftResult() {
        // f = 123 LSHIFT 2 = 492
        List<String> input = appendWireA(EXAMPLE_CIRCUIT, "f -> a");
        assertEquals("492", SomeAssemblyRequiredAOC2015Day7.solvePartOne(input));
    }

    @Test
    void givenExampleInput_solvePartOne_returnsRshiftResult() {
        // g = 456 RSHIFT 2 = 114
        List<String> input = appendWireA(EXAMPLE_CIRCUIT, "g -> a");
        assertEquals("114", SomeAssemblyRequiredAOC2015Day7.solvePartOne(input));
    }

    @Test
    void givenExampleInput_solvePartOne_returnsNotXResult() {
        // h = NOT 123 = 65412 (16-bit complement)
        List<String> input = appendWireA(EXAMPLE_CIRCUIT, "h -> a");
        assertEquals("65412", SomeAssemblyRequiredAOC2015Day7.solvePartOne(input));
    }

    @Test
    void givenExampleInput_solvePartOne_returnsNotYResult() {
        // i = NOT 456 = 65079 (16-bit complement)
        List<String> input = appendWireA(EXAMPLE_CIRCUIT, "i -> a");
        assertEquals("65079", SomeAssemblyRequiredAOC2015Day7.solvePartOne(input));
    }

    @Test
    void givenLiteralAndWire_solvePartOne_returnsLiteralAndResult() {
        // Exercises the "1 AND someWire" edge case from the real puzzle input
        List<String> input = List.of(
                "3 -> cx",
                "1 AND cx -> cy",
                "cy -> a"
        );
        // 1 AND 3 = 1
        assertEquals("1", SomeAssemblyRequiredAOC2015Day7.solvePartOne(input));
    }

    // -------------------------------------------------------------------------
    // Part 2 tests
    // -------------------------------------------------------------------------

    /**
     * Verifies that {@code solvePartTwo} overrides wire {@code b} with
     * {@code PART_ONE_ANSWER_FOR_WIRE_B} (956), ignoring {@code b}'s original
     * instruction, and that wire {@code a} reflects the overridden value.
     * <p>
     * Circuit: {@code b}'s original instruction gives it the value 10.
     * Wire {@code a} is a passthrough of {@code b}.
     * Part 1 would therefore return "10" for wire {@code a}.
     * Part 2 must return "956" because {@code b} is seeded from the constant.
     */
    @Test
    void givenCircuitWithBOverridden_solvePartTwo_reEvaluatesWithOverriddenB() {
        List<String> input = List.of(
                "10 -> b",
                "b -> a"
        );
        // Part 1 sanity-check: "b" resolves to 10 normally
        assertEquals("10", SomeAssemblyRequiredAOC2015Day7.solvePartOne(input));
        // Part 2 must ignore "b"'s instruction and use 956 instead
        assertEquals("956", SomeAssemblyRequiredAOC2015Day7.solvePartTwo(input));
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    /**
     * Returns a new list containing all entries from {@code base} plus an
     * additional instruction that routes a named wire through to wire {@code a}.
     */
    private static List<String> appendWireA(List<String> base, String extraInstruction) {
        List<String> extended = new ArrayList<>(base);
        extended.add(extraInstruction);
        return extended;
    }
}
