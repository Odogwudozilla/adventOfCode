package odogwudozilla.year2021.day8;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link SevenSegmentSearchAOC2021Day8} — Parts 1 and 2.
 * <p>
 * Uses the 10-entry example from the puzzle description for both parts.
 */
class SevenSegmentSearchAOC2021Day8Test {

    private static final List<String> TEN_ENTRY_EXAMPLE = List.of(
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe",
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc",
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg",
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb",
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea",
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb",
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe",
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef",
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb",
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"
    );

    @Test
    void givenLargerExampleInput_solvePartOne_returns26() {
        assertEquals("26", SevenSegmentSearchAOC2021Day8.solvePartOne(TEN_ENTRY_EXAMPLE));
    }

    @Test
    void givenLargerExampleInput_solvePartTwo_returns61229() {
        assertEquals("61229", SevenSegmentSearchAOC2021Day8.solvePartTwo(TEN_ENTRY_EXAMPLE));
    }
}
