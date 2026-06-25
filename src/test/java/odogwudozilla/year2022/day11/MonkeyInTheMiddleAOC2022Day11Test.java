package odogwudozilla.year2022.day11;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonkeyInTheMiddleAOC2022Day11Test {

    @Test
    void givenExampleInput_solvePartOne_returnsExpectedValue() {
        assertEquals("10605", MonkeyInTheMiddleAOC2022Day11.solvePartOne(sampleInput()));
    }

    @Test
    void givenExampleInput_afterRoundOne_holdingsMatchPublishedSample() throws Exception {
        List<?> monkeys = parseMonkeys(sampleInput());
        runRounds(monkeys, 1);

        assertEquals(List.of(20L, 23L, 27L, 26L), monkeyItems(monkeys.get(0)));
        assertEquals(List.of(2080L, 25L, 167L, 207L, 401L, 1046L), monkeyItems(monkeys.get(1)));
        assertEquals(List.of(), monkeyItems(monkeys.get(2)));
        assertEquals(List.of(), monkeyItems(monkeys.get(3)));
    }

    @Test
    void givenExampleInput_afterTwentyRounds_inspectionCountsMatchPublishedSample() throws Exception {
        List<?> monkeys = parseMonkeys(sampleInput());
        runRounds(monkeys, 20);

        assertEquals(101L, inspectionCount(monkeys.get(0)));
        assertEquals(95L, inspectionCount(monkeys.get(1)));
        assertEquals(7L, inspectionCount(monkeys.get(2)));
        assertEquals(105L, inspectionCount(monkeys.get(3)));
    }

    @Test
    void givenExampleInput_solvePartTwo_returnsExpectedValue() throws Exception {
        assertEquals("2713310158", solvePartTwo(sampleInput()));
    }

    @Test
    void givenExampleInput_partTwoInspectionCheckpoints_matchPublishedSample() throws Exception {
        assertPartTwoInspectionCounts(1, new long[]{2L, 4L, 3L, 6L});
        assertPartTwoInspectionCounts(20, new long[]{99L, 97L, 8L, 103L});
        assertPartTwoInspectionCounts(1_000, new long[]{5204L, 4792L, 199L, 5192L});
        assertPartTwoInspectionCounts(10_000, new long[]{52166L, 47830L, 1938L, 52013L});
    }

    private static List<String> sampleInput() {
        return List.of(
                "Monkey 0:",
                "  Starting items: 79, 98",
                "  Operation: new = old * 19",
                "  Test: divisible by 23",
                "    If true: throw to monkey 2",
                "    If false: throw to monkey 3",
                "",
                "Monkey 1:",
                "  Starting items: 54, 65, 75, 74",
                "  Operation: new = old + 6",
                "  Test: divisible by 19",
                "    If true: throw to monkey 2",
                "    If false: throw to monkey 0",
                "",
                "Monkey 2:",
                "  Starting items: 79, 60, 97",
                "  Operation: new = old * old",
                "  Test: divisible by 13",
                "    If true: throw to monkey 1",
                "    If false: throw to monkey 3",
                "",
                "Monkey 3:",
                "  Starting items: 74",
                "  Operation: new = old + 3",
                "  Test: divisible by 17",
                "    If true: throw to monkey 0",
                "    If false: throw to monkey 1"
        );
    }

    private static List<?> parseMonkeys(List<String> lines)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method parseMonkeys = MonkeyInTheMiddleAOC2022Day11.class.getDeclaredMethod("parseMonkeys", List.class);
        parseMonkeys.setAccessible(true);
        return (List<?>) parseMonkeys.invoke(null, lines);
    }

    private static String solvePartTwo(List<String> lines)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method solvePartTwo = MonkeyInTheMiddleAOC2022Day11.class.getDeclaredMethod("solvePartTwo", List.class);
        solvePartTwo.setAccessible(true);
        return (String) solvePartTwo.invoke(null, lines);
    }

    private static void runRounds(List<?> monkeys, int rounds)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method runRounds = MonkeyInTheMiddleAOC2022Day11.class.getDeclaredMethod("runRounds", List.class, int.class);
        runRounds.setAccessible(true);
        runRounds.invoke(null, monkeys, rounds);
    }

    private static void runPartTwoRounds(List<?> monkeys, int rounds)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method computeCommonModulus = MonkeyInTheMiddleAOC2022Day11.class
                .getDeclaredMethod("computeCommonModulus", List.class);
        computeCommonModulus.setAccessible(true);
        long modulus = (long) computeCommonModulus.invoke(null, monkeys);

        Method runRounds = MonkeyInTheMiddleAOC2022Day11.class
                .getDeclaredMethod("runRounds", List.class, int.class, boolean.class, long.class);
        runRounds.setAccessible(true);
        runRounds.invoke(null, monkeys, rounds, false, modulus);
    }

    private static void assertPartTwoInspectionCounts(int rounds, long[] expected)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        List<?> monkeys = parseMonkeys(sampleInput());
        runPartTwoRounds(monkeys, rounds);
        for (int monkeyIndex = 0; monkeyIndex < expected.length; monkeyIndex++) {
            assertEquals(expected[monkeyIndex], inspectionCount(monkeys.get(monkeyIndex)));
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Long> monkeyItems(Object monkey) throws NoSuchFieldException, IllegalAccessException {
        Field itemsField = monkey.getClass().getDeclaredField("items");
        itemsField.setAccessible(true);
        Deque<Long> items = (Deque<Long>) itemsField.get(monkey);
        return new ArrayList<>(items);
    }

    private static long inspectionCount(Object monkey) throws NoSuchFieldException, IllegalAccessException {
        Field inspectionsField = monkey.getClass().getDeclaredField("inspections");
        inspectionsField.setAccessible(true);
        return inspectionsField.getLong(monkey);
    }
}




