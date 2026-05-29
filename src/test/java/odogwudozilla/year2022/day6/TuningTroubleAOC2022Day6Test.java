package odogwudozilla.year2022.day6;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TuningTroubleAOC2022Day6Test {
    @Test
    void givenExampleInput1_solvePartOne_returns7() {
        List<String> input = List.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb");
        assertEquals("7", TuningTroubleAOC2022Day6.solvePartOne(input));
    }

    @Test
    void givenExampleInput2_solvePartOne_returns5() {
        List<String> input = List.of("bvwbjplbgvbhsrlpgdmjqwftvncz");
        assertEquals("5", TuningTroubleAOC2022Day6.solvePartOne(input));
    }

    @Test
    void givenExampleInput3_solvePartOne_returns6() {
        List<String> input = List.of("nppdvjthqldpwncqszvftbrmjlhg");
        assertEquals("6", TuningTroubleAOC2022Day6.solvePartOne(input));
    }

    @Test
    void givenExampleInput4_solvePartOne_returns10() {
        List<String> input = List.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg");
        assertEquals("10", TuningTroubleAOC2022Day6.solvePartOne(input));
    }

    @Test
    void givenExampleInput5_solvePartOne_returns11() {
        List<String> input = List.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw");
        assertEquals("11", TuningTroubleAOC2022Day6.solvePartOne(input));
    }

    @Test
    void givenNoMarkerInput_solvePartOne_returns0() {
        List<String> input = List.of("aaaaaaa"); // No 4-unique window
        assertEquals("0", TuningTroubleAOC2022Day6.solvePartOne(input));
    }

    @Test
    void givenExampleInput1_solvePartTwo_returns19() {
        List<String> input = List.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb");
        assertEquals("19", TuningTroubleAOC2022Day6.solvePartTwo(input));
    }

    @Test
    void givenExampleInput2_solvePartTwo_returns23() {
        List<String> input = List.of("bvwbjplbgvbhsrlpgdmjqwftvncz");
        assertEquals("23", TuningTroubleAOC2022Day6.solvePartTwo(input));
    }

    @Test
    void givenExampleInput3_solvePartTwo_returns23() {
        List<String> input = List.of("nppdvjthqldpwncqszvftbrmjlhg");
        assertEquals("23", TuningTroubleAOC2022Day6.solvePartTwo(input));
    }

    @Test
    void givenExampleInput4_solvePartTwo_returns29() {
        List<String> input = List.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg");
        assertEquals("29", TuningTroubleAOC2022Day6.solvePartTwo(input));
    }

    @Test
    void givenExampleInput5_solvePartTwo_returns26() {
        List<String> input = List.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw");
        assertEquals("26", TuningTroubleAOC2022Day6.solvePartTwo(input));
    }
}
