package odogwudozilla.year2015.day15;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Advent of Code 2015 Day 15 - Science for Hungry People
 *
 * The task is to find the optimal combination of cookie ingredients that maximises
 * the total score, where the score is calculated by multiplying the properties
 * (capacity, durability, flavor, texture) after accounting for ingredient amounts.
 * The total amount of ingredients must be exactly 100 teaspoons.
 *
 * Puzzle: https://adventofcode.com/2015/day/15
 */
public class ScienceForHungryPeopleAOC2015Day15 {

    private static final String PUZZLE_INPUT_PATH = "src/main/resources/2015/day15/day15_puzzle_data.txt";
    private static final int TOTAL_TEASPOONS = 100;

    static class Ingredient {
        String name;
        int capacity;
        int durability;
        int flavor;
        int texture;
        int calories;

        Ingredient(String name, int capacity, int durability, int flavor, int texture, int calories) {
            this.name = name;
            this.capacity = capacity;
            this.durability = durability;
            this.flavor = flavor;
            this.texture = texture;
            this.calories = calories;
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PUZZLE_INPUT_PATH));
            List<Ingredient> ingredients = parseIngredients(lines);

            long maxScore = findMaxScore(ingredients, -1);
            System.out.println("Part 1 - Highest-scoring cookie: " + maxScore);

            long maxScoreWith500Calories = findMaxScore(ingredients, 500);
            System.out.println("Part 2 - Highest-scoring cookie with 500 calories: " + maxScoreWith500Calories);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Ingredient> parseIngredients(List<String> lines) {
        List<Ingredient> ingredients = new ArrayList<>();
        Pattern pattern = Pattern.compile(
            "([^:]+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)"
        );

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String name = matcher.group(1);
                int capacity = Integer.parseInt(matcher.group(2));
                int durability = Integer.parseInt(matcher.group(3));
                int flavor = Integer.parseInt(matcher.group(4));
                int texture = Integer.parseInt(matcher.group(5));
                int calories = Integer.parseInt(matcher.group(6));

                ingredients.add(new Ingredient(name, capacity, durability, flavor, texture, calories));
            }
        }

        return ingredients;
    }

    private static long findMaxScore(List<Ingredient> ingredients, int targetCalories) {
        long maxScore = 0;
        int numIngredients = ingredients.size();

        if (numIngredients == 1) {
            int calories = ingredients.get(0).calories * TOTAL_TEASPOONS;
            if (targetCalories == -1 || calories == targetCalories) {
                return calculateScore(ingredients, new int[]{TOTAL_TEASPOONS});
            }
            return 0;
        }

        maxScore = findMaxScoreRecursive(ingredients, new int[numIngredients], 0, TOTAL_TEASPOONS, targetCalories);

        return maxScore;
    }

    private static long findMaxScoreRecursive(List<Ingredient> ingredients, int[] amounts, int index, int remaining, int targetCalories) {
        if (index == ingredients.size() - 1) {
            amounts[index] = remaining;

            // Check calorie constraint if specified
            if (targetCalories != -1) {
                int totalCalories = calculateCalories(ingredients, amounts);
                if (totalCalories != targetCalories) {
                    return 0;
                }
            }

            return calculateScore(ingredients, amounts);
        }

        long maxScore = 0;

        for (int i = 0; i <= remaining; i++) {
            amounts[index] = i;
            long score = findMaxScoreRecursive(ingredients, amounts, index + 1, remaining - i, targetCalories);
            maxScore = Math.max(maxScore, score);
        }

        return maxScore;
    }

    private static int calculateCalories(List<Ingredient> ingredients, int[] amounts) {
        int totalCalories = 0;

        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            int amount = amounts[i];
            totalCalories += ingredient.calories * amount;
        }

        return totalCalories;
    }

    private static long calculateScore(List<Ingredient> ingredients, int[] amounts) {
        int capacity = 0;
        int durability = 0;
        int flavor = 0;
        int texture = 0;

        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            int amount = amounts[i];

            capacity += ingredient.capacity * amount;
            durability += ingredient.durability * amount;
            flavor += ingredient.flavor * amount;
            texture += ingredient.texture * amount;
        }

        // Negative properties become 0
        capacity = Math.max(0, capacity);
        durability = Math.max(0, durability);
        flavor = Math.max(0, flavor);
        texture = Math.max(0, texture);

        return (long) capacity * durability * flavor * texture;
    }
}

