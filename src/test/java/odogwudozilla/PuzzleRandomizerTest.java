package odogwudozilla;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PuzzleRandomizer prioritization logic.
 */
@DisplayName("PuzzleRandomizer Prioritization Tests")
class PuzzleRandomizerTest {

    /**
     * Test that unsolved years are prioritised and always return day 1.
     */
    @Test
    @DisplayName("Priority 1: Unsolved years return day 1")
    void testUnsolvedYearsPriority() throws IOException {
        // Run multiple times to ensure consistent behavior
        for (int i = 0; i < 10; i++) {
            PuzzleRandomizer.PuzzleSelection selection = PuzzleRandomizer.selectRandomUnsolvedPuzzle();

            assertNotNull(selection, "Selection should not be null");
            assertEquals(1, selection.day, "Unsolved years should always return day 1");

            // Verify it's from the unsolved years list (2016, 2019, 2021, 2022)
            assertTrue(isUnsolvedYear(selection.year),
                "Year " + selection.year + " should be in unsolved years");
        }
    }

    /**
     * Check if a year is in the unsolved list.
     */
    private boolean isUnsolvedYear(int year) {
        Set<Integer> unsolvedYears = new HashSet<>();
        unsolvedYears.add(2016);
        unsolvedYears.add(2019);
        unsolvedYears.add(2021);
        unsolvedYears.add(2022);
        return unsolvedYears.contains(year);
    }

    /**
     * Test that the selection is not null.
     */
    @Test
    @DisplayName("Selection is never null")
    void testSelectionNotNull() throws IOException {
        PuzzleRandomizer.PuzzleSelection selection = PuzzleRandomizer.selectRandomUnsolvedPuzzle();
        assertNotNull(selection, "Random puzzle selection should not return null");
    }

    /**
     * Test that year is valid.
     */
    @Test
    @DisplayName("Selected year is valid")
    void testValidYear() throws IOException {
        PuzzleRandomizer.PuzzleSelection selection = PuzzleRandomizer.selectRandomUnsolvedPuzzle();
        assertTrue(selection.year >= 2015 && selection.year <= 2025,
            "Year should be between 2015 and 2025");
    }

    /**
     * Test that day is valid.
     */
    @Test
    @DisplayName("Selected day is valid")
    void testValidDay() throws IOException {
        PuzzleRandomizer.PuzzleSelection selection = PuzzleRandomizer.selectRandomUnsolvedPuzzle();
        assertTrue(selection.day >= 1 && selection.day <= 25,
            "Day should be between 1 and 25");
    }

    /**
     * Test the toString method.
     */
    @Test
    @DisplayName("PuzzleSelection toString format")
    void testToStringFormat() throws IOException {
        PuzzleRandomizer.PuzzleSelection selection = PuzzleRandomizer.selectRandomUnsolvedPuzzle();
        String result = selection.toString();
        assertNotNull(result);
        assertTrue(result.contains("Year"), "toString should contain 'Year'");
        assertTrue(result.contains("Day"), "toString should contain 'Day'");
    }
}

