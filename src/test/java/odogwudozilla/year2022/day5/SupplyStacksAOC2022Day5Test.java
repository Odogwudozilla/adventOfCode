package odogwudozilla.year2022.day5;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplyStacksAOC2022Day5Test {

    /**
     * Example from the AoC 2022 Day 5 puzzle description.
     * <p>
     * Initial stacks (bottom → top):
     *   Stack 1: Z, N
     *   Stack 2: M, C, D
     *   Stack 3: P
     * After applying 4 move instructions the top crates are C, M, Z → "CMZ".
     */
    @Test
    void givenExampleInput_solvePartOne_returnsCMZ() {
        List<String> lines = List.of(
            "    [D]    ",
            "[N] [C]    ",
            "[Z] [M] [P]",
            " 1   2   3 ",
            "",
            "move 1 from 2 to 1",
            "move 3 from 1 to 3",
            "move 2 from 2 to 1",
            "move 1 from 1 to 2"
        );
        SupplyStacksAOC2022Day5 solver = new SupplyStacksAOC2022Day5();
        assertEquals("CMZ", solver.solvePartOne(lines));
    }

    /**
     * Example from the AoC 2022 Day 5 puzzle description — Part 2 (CrateMover 9001).
     * <p>
     * Same initial stacks and moves as Part 1, but crates are moved as a group,
     * preserving their original relative order. Top crates after all moves are M, C, D → "MCD".
     */
    @Test
    void givenExampleInput_solvePartTwo_returnsMCD() {
        List<String> lines = List.of(
            "    [D]    ",
            "[N] [C]    ",
            "[Z] [M] [P]",
            " 1   2   3 ",
            "",
            "move 1 from 2 to 1",
            "move 3 from 1 to 3",
            "move 2 from 2 to 1",
            "move 1 from 1 to 2"
        );
        SupplyStacksAOC2022Day5 solver = new SupplyStacksAOC2022Day5();
        assertEquals("MCD", solver.solvePartTwo(lines));
    }
}


