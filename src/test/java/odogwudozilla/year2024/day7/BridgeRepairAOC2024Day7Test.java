package odogwudozilla.year2024.day7;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BridgeRepairAOC2024Day7Test {

    /** Full 9-line example from the analysis; expected sum: 190 + 3267 + 292 = 3749 */
    @Test
    void givenExampleInput_solvePartOne_returns3749() {
        List<String> lines = List.of(
            "190: 10 19",
            "3267: 81 40 27",
            "83: 17 5",
            "156: 15 6",
            "7290: 6 8 6 15",
            "161011: 16 10 13",
            "192: 17 8 14",
            "21037: 9 7 18 13",
            "292: 11 6 16 20"
        );
        assertEquals("3749", BridgeRepairAOC2024Day7.solvePartOne(lines));
    }

    /**
     * Full 9-line example from the analysis; expected sum:
     * 190 + 3267 + 292 + 156 + 7290 + 192 = 11387
     */
    @Test
    void givenExampleInput_solvePartTwo_returns11387() {
        List<String> lines = List.of(
            "190: 10 19",
            "3267: 81 40 27",
            "83: 17 5",
            "156: 15 6",
            "7290: 6 8 6 15",
            "161011: 16 10 13",
            "192: 17 8 14",
            "21037: 9 7 18 13",
            "292: 11 6 16 20"
        );
        assertEquals("11387", BridgeRepairAOC2024Day7.solvePartTwo(lines));
    }
}

