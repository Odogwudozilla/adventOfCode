package odogwudozilla.year2023.day11;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CosmicExpansionAOC2023Day11Test {

    @Test
    void givenExampleInput_solvePartOne_returns374() {
        List<String> lines = List.of(
            "...#......",
            ".......#..",
            "#.........",
            "..........",
            "......#...",
            ".#........",
            ".........#",
            "..........",
            ".......#..",
            "#...#....."
        );
        CosmicExpansionAOC2023Day11 solver = new CosmicExpansionAOC2023Day11();
        assertEquals("374", solver.solvePartOne(lines));
    }

    @Test
    void givenExampleInput_solvePartTwo_returns82000210() {
        List<String> lines = List.of(
            "...#......",
            ".......#..",
            "#.........",
            "..........",
            "......#...",
            ".#........",
            ".........#",
            "..........",
            ".......#..",
            "#...#....."
        );
        CosmicExpansionAOC2023Day11 solver = new CosmicExpansionAOC2023Day11();
        assertEquals("82000210", solver.solvePartTwo(lines));
    }
}

