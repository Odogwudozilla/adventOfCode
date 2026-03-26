package odogwudozilla.year2020.day21;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * --- Day 21: Allergen Assessment ---
 * You reach the train's last stop and the closest you can get to your vacation island without getting wet. There aren't even any boats here, but nothing can stop you now: you build a raft. You just need a few days' worth of food for your journey.
 *
 * You don't speak the local language, so you can't read any ingredients lists. However, sometimes, allergens are listed in a language you do understand. You should be able to use this information to determine which ingredient contains which allergen and work out which foods are safe to take with you on your trip.
 *
 * Each allergen is found in exactly one ingredient. Each ingredient contains zero or one allergen. Allergens aren't always marked; when they're listed (as in (contains nuts, shellfish) after an ingredients list), the ingredient that contains each listed allergen will be somewhere in the corresponding ingredients list. However, even if an allergen isn't listed, the ingredient that contains that allergen could still be present: maybe they forgot to label it, or maybe it was labeled in a language you don't know.
 *
 * The first step is to determine which ingredients can't possibly contain any of the allergens in any food in your list. In the above example, none of the ingredients kfcds, nhms, sbzzf, or trh can contain an allergen. Counting the number of times any of these ingredients appear in any ingredients list produces 5: they all appear once each except sbzzf, which appears twice.
 *
 * Determine which ingredients cannot possibly contain any of the allergens in your list. How many times do any of those ingredients appear?
 *
 * Puzzle link: https://adventofcode.com/2020/day/21
 */
public class AllergenAssessmentAOC2020Day21 {
    public static void main(String[] args) {
        List<String> input = readInput();
        int partOneResult = solvePartOne(input);
        System.out.println("Part 1: " + partOneResult);
        // Uncomment below if/when Part 2 is implemented
        String partTwoResult = solvePartTwo(input);
        System.out.println("Part 2: " + partTwoResult);
    }

    /**
     * Reads the puzzle input from the resource file.
     * @return list of input lines
     */
    private static List<String> readInput() {
        String resourcePath = "src/main/resources/2020/day21/day21_puzzle_data.txt";
        try {
            return java.nio.file.Files.readAllLines(java.nio.file.Paths.get(resourcePath));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read input file: " + resourcePath, e);
        }
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param input the puzzle input lines
     * @return the answer for Part 1
     */
    private static int solvePartOne(List<String> input) {
        // Parse foods: each line has ingredients and allergens
        Map<String, Set<String>> allergenToPossibleIngredients = new HashMap<>();
        List<Set<String>> allIngredientsList = new ArrayList<>();
        Map<String, Integer> ingredientCount = new HashMap<>();

        for (String line : input) {
            String[] parts = line.split(" \\(contains ");
            Set<String> ingredients = new HashSet<>(Arrays.asList(parts[0].split(" ")));
            allIngredientsList.add(ingredients);
            for (String ing : ingredients) {
                ingredientCount.put(ing, ingredientCount.getOrDefault(ing, 0) + 1);
            }
            if (parts.length > 1) {
                String[] allergens = parts[1].replace(")", "").split(", ");
                for (String allergen : allergens) {
                    if (!allergenToPossibleIngredients.containsKey(allergen)) {
                        allergenToPossibleIngredients.put(allergen, new HashSet<>(ingredients));
                    } else {
                        allergenToPossibleIngredients.get(allergen).retainAll(ingredients);
                    }
                }
            }
        }

        // Find all ingredients that could possibly contain an allergen
        Set<String> possibleAllergenIngredients = new HashSet<>();
        for (Set<String> possible : allergenToPossibleIngredients.values()) {
            possibleAllergenIngredients.addAll(possible);
        }

        // Count ingredients that cannot possibly contain any allergen
        int safeCount = 0;
        for (String ing : ingredientCount.keySet()) {
            if (!possibleAllergenIngredients.contains(ing)) {
                safeCount += ingredientCount.get(ing);
            }
        }
        return safeCount;
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input the puzzle input lines
     * @return the answer for Part 2
     */
    private static String solvePartTwo(List<String> input) {
        Map<String, Set<String>> allergenToPossibleIngredients = new HashMap<>();
        List<Set<String>> allIngredientsList = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(" \\(contains ");
            Set<String> ingredients = new HashSet<>(Arrays.asList(parts[0].split(" ")));
            allIngredientsList.add(ingredients);
            if (parts.length > 1) {
                String[] allergens = parts[1].replace(")", "").split(", ");
                for (String allergen : allergens) {
                    if (!allergenToPossibleIngredients.containsKey(allergen)) {
                        allergenToPossibleIngredients.put(allergen, new HashSet<>(ingredients));
                    } else {
                        allergenToPossibleIngredients.get(allergen).retainAll(ingredients);
                    }
                }
            }
        }
        // Resolve allergen to ingredient mapping
        Map<String, String> allergenToIngredient = new HashMap<>();
        Set<String> assignedIngredients = new HashSet<>();
        while (allergenToIngredient.size() < allergenToPossibleIngredients.size()) {
            for (Map.Entry<String, Set<String>> entry : allergenToPossibleIngredients.entrySet()) {
                Set<String> possible = new HashSet<>(entry.getValue());
                possible.removeAll(assignedIngredients);
                if (possible.size() == 1) {
                    String ingredient = possible.iterator().next();
                    allergenToIngredient.put(entry.getKey(), ingredient);
                    assignedIngredients.add(ingredient);
                }
            }
        }
        // Sort allergens alphabetically and join their ingredients
        List<String> sortedAllergens = new ArrayList<>(allergenToIngredient.keySet());
        java.util.Collections.sort(sortedAllergens);
        List<String> dangerousIngredients = new ArrayList<>();
        for (String allergen : sortedAllergens) {
            dangerousIngredients.add(allergenToIngredient.get(allergen));
        }
        return String.join(",", dangerousIngredients);
    }
}
