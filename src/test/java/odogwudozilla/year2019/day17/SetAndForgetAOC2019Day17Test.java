package odogwudozilla.year2019.day17;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetAndForgetAOC2019Day17Test {

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() throws Exception {
        List<String> cameraView = List.of(
                "..#..........",
                "..#..........",
                "#######...###",
                "#.#...#...#.#",
                "#############",
                "..#...#...#..",
                "..#####...^.."
        );

        String asciiProgram = buildAsciiOutputProgram(cameraView);

        Method solvePartOne = SetAndForgetAOC2019Day17.class.getDeclaredMethod("solvePartOne", List.class);
        solvePartOne.setAccessible(true);

        String result = (String) solvePartOne.invoke(null, List.of(asciiProgram));

        assertEquals("76", result);
    }

    @Test
    void givenPartTwoExampleMap_buildFullPathTokens_returnsExpectedRouteTokens() throws Exception {
        List<String> grid = List.of(
                "#######...#####",
                "#.....#...#...#",
                "#.....#...#...#",
                "......#...#...#",
                "......#...###.#",
                "......#.....#.#",
                "^########...#.#",
                "......#.#...#.#",
                "......#########",
                "........#...#..",
                "....#########..",
                "....#...#......",
                "....#...#......",
                "....#...#......",
                "....#####......"
        );

        Method findRobot = SetAndForgetAOC2019Day17.class.getDeclaredMethod("findRobot", List.class);
        findRobot.setAccessible(true);
        Object robotState = findRobot.invoke(null, grid);

        Method buildPath = SetAndForgetAOC2019Day17.class.getDeclaredMethod("buildFullPathTokens", List.class, robotState.getClass());
        buildPath.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> tokens = (List<String>) buildPath.invoke(null, grid, robotState);

        assertEquals(
                List.of("R", "8", "R", "8", "R", "4", "R", "4", "R", "8", "L", "6", "L", "2", "R", "4", "R", "4", "R", "8", "R", "8", "R", "8", "L", "6", "L", "2"),
                tokens
        );
    }

    @Test
    void givenPartTwoExampleRoute_compressToRoutines_returnsExpectedPlan() throws Exception {
        List<String> tokens = Arrays.asList(
                "R", "8", "R", "8", "R", "4", "R", "4", "R", "8", "L", "6", "L", "2",
                "R", "4", "R", "4", "R", "8", "R", "8", "R", "8", "L", "6", "L", "2"
        );

        Method compressToRoutines = SetAndForgetAOC2019Day17.class.getDeclaredMethod("compressToRoutines", List.class);
        compressToRoutines.setAccessible(true);
        Object plan = compressToRoutines.invoke(null, tokens);

        Field mainRoutineField = plan.getClass().getDeclaredField("mainRoutine");
        Field functionMapField = plan.getClass().getDeclaredField("functions");
        mainRoutineField.setAccessible(true);
        functionMapField.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> mainRoutine = (List<String>) mainRoutineField.get(plan);
        @SuppressWarnings("unchecked")
        Map<Character, List<String>> functions = (Map<Character, List<String>>) functionMapField.get(plan);

        assertEquals(List.of("A", "B", "C", "B", "A", "C"), mainRoutine);
        assertEquals(List.of("R", "8", "R", "8"), functions.get('A'));
        assertEquals(List.of("R", "4", "R", "4", "R", "8"), functions.get('B'));
        assertEquals(List.of("L", "6", "L", "2"), functions.get('C'));
    }

    private static String buildAsciiOutputProgram(List<String> cameraView) {
        StringBuilder text = new StringBuilder();
        for (String line : cameraView) {
            text.append(line).append('\n');
        }

        List<String> instructions = new ArrayList<>();
        for (char c : text.toString().toCharArray()) {
            instructions.add("104");
            instructions.add(Integer.toString(c));
        }
        instructions.add("99");
        return String.join(",", instructions);
    }
}


