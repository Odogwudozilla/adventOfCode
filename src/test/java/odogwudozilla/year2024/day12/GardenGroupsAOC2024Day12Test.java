package odogwudozilla.year2024.day12;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GardenGroupsAOC2024Day12Test {

    @Test
    void givenSmallExample_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
                "AAAA",
                "BBCD",
                "BBCC",
                "EEEC"
        );

        assertEquals("140", GardenGroupsAOC2024Day12.solvePartOne(input));
    }

    @Test
    void givenNestedRegionExample_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
                "OOOOO",
                "OXOXO",
                "OOOOO",
                "OXOXO",
                "OOOOO"
        );

        assertEquals("772", GardenGroupsAOC2024Day12.solvePartOne(input));
    }

    @Test
    void givenLargeExample_solvePartOne_returnsExpectedValue() {
        List<String> input = List.of(
                "RRRRIICCFF",
                "RRRRIICCCF",
                "VVRRRCCFFF",
                "VVRCCCJFFF",
                "VVVVCJJCFE",
                "VVIVCCJJEE",
                "VVIIICJJEE",
                "MIIIIIJJEE",
                "MIIISIJEEE",
                "MMMISSJEEE"
        );

        assertEquals("1930", GardenGroupsAOC2024Day12.solvePartOne(input));
    }

    @Test
    void givenSmallExample_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
                "AAAA",
                "BBCD",
                "BBCC",
                "EEEC"
        );

        assertEquals("80", GardenGroupsAOC2024Day12.solvePartTwo(input));
    }

    @Test
    void givenNestedRegionExample_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
                "OOOOO",
                "OXOXO",
                "OOOOO",
                "OXOXO",
                "OOOOO"
        );

        assertEquals("436", GardenGroupsAOC2024Day12.solvePartTwo(input));
    }

    @Test
    void givenEshapedExample_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
                "EEEEE",
                "EXXXX",
                "EEEEE",
                "EXXXX",
                "EEEEE"
        );

        assertEquals("236", GardenGroupsAOC2024Day12.solvePartTwo(input));
    }

    @Test
    void givenIndentedBoundaryExample_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
                "AAAAAA",
                "AAABBA",
                "AAABBA",
                "ABBAAA",
                "ABBAAA",
                "AAAAAA"
        );

        assertEquals("368", GardenGroupsAOC2024Day12.solvePartTwo(input));
    }

    @Test
    void givenLargeExample_solvePartTwo_returnsExpectedValue() {
        List<String> input = List.of(
                "RRRRIICCFF",
                "RRRRIICCCF",
                "VVRRRCCFFF",
                "VVRCCCJFFF",
                "VVVVCJJCFE",
                "VVIVCCJJEE",
                "VVIIICJJEE",
                "MIIIIIJJEE",
                "MIIISIJEEE",
                "MMMISSJEEE"
        );

        assertEquals("1206", GardenGroupsAOC2024Day12.solvePartTwo(input));
    }
}

