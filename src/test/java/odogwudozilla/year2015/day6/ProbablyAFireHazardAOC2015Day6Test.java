package odogwudozilla.year2015.day6;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProbablyAFireHazardAOC2015Day6Test {
    @Test
    void givenExampleInput_turnOnWholeGrid_returns1000000() {
        List<String> input = List.of("turn on 0,0 through 999,999");
        assertEquals("1000000", ProbablyAFireHazardAOC2015Day6.solvePartOne(input));
    }

    @Test
    void givenExampleInput_toggleRowAndTurnOff_returns998996() {
        List<String> input = List.of(
            "toggle 0,0 through 999,0",
            "turn off 499,499 through 500,500"
        );
        assertEquals("998996", ProbablyAFireHazardAOC2015Day6.solvePartOne(input));
    }

    @Test
    void givenExampleInput_turnOnSingleLight_returns1_part2() {
        List<String> input = List.of("turn on 0,0 through 0,0");
        assertEquals("1", ProbablyAFireHazardAOC2015Day6.solvePartTwo(input));
    }

    @Test
    void givenExampleInput_toggleWholeGrid_returns2000000_part2() {
        List<String> input = List.of("toggle 0,0 through 999,999");
        assertEquals("2000000", ProbablyAFireHazardAOC2015Day6.solvePartTwo(input));
    }
}
