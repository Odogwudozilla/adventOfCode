package odogwudozilla.year2022.day7;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoSpaceLeftOnDeviceAOC2022Day7Test {

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        List<String> lines = List.of(
            "$ cd /",
            "$ ls",
            "dir a",
            "14848514 b.txt",
            "8504156 c.dat",
            "dir d",
            "$ cd a",
            "$ ls",
            "dir e",
            "29116 f",
            "2557 g",
            "62596 h.lst",
            "$ cd e",
            "$ ls",
            "584 i",
            "$ cd ..",
            "$ cd ..",
            "$ cd d",
            "$ ls",
            "4060174 j",
            "8033020 d.log",
            "5626152 d.ext",
            "7214296 k"
        );

        NoSpaceLeftOnDeviceAOC2022Day7 solver = new NoSpaceLeftOnDeviceAOC2022Day7();
        assertEquals("95437", solver.solvePartOne(lines));
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() {
        List<String> lines = List.of(
            "$ cd /",
            "$ ls",
            "dir a",
            "14848514 b.txt",
            "8504156 c.dat",
            "dir d",
            "$ cd a",
            "$ ls",
            "dir e",
            "29116 f",
            "2557 g",
            "62596 h.lst",
            "$ cd e",
            "$ ls",
            "584 i",
            "$ cd ..",
            "$ cd ..",
            "$ cd d",
            "$ ls",
            "4060174 j",
            "8033020 d.log",
            "5626152 d.ext",
            "7214296 k"
        );

        NoSpaceLeftOnDeviceAOC2022Day7 solver = new NoSpaceLeftOnDeviceAOC2022Day7();
        assertEquals("24933642", solver.solvePartTwo(lines));
    }
}
