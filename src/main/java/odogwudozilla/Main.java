package odogwudozilla;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Centralized CLI-based puzzle executor for Advent of Code solutions.
 *
 * This class serves as the main entry point for executing puzzle solutions dynamically.
 * It uses Java reflection to discover and load puzzle classes based on year and day parameters.
 *
 * Class Discovery Strategy:
 * - Scans the package: odogwudozilla.year<YYYY>.day<D>
 * - Searches for files matching pattern: *AOC<YYYY>Day<D>.class
 * - Filters out inner classes (containing '$')
 * - Invokes the public static main(String[] args) method
 *
 * Usage: java odogwudozilla.Main <year> <day>
 * Examples:
 *   java odogwudozilla.Main 2025 day1
 *   java odogwudozilla.Main 2024 day18
 *   java odogwudozilla.Main 2018 day3
 *
 * Also used by the puzzle command wrapper:
 *   puzzle 2025 day1
 */
public class Main {

    private static final String PACKAGE_PREFIX = "odogwudozilla.year";
    private static final int EXIT_CODE_INVALID_ARGS = 1;
    private static final int EXIT_CODE_PUZZLE_NOT_FOUND = 2;
    private static final int EXIT_CODE_EXECUTION_ERROR = 3;

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            System.exit(EXIT_CODE_INVALID_ARGS);
        }

        String year = args[0];
        String day = args[1];

        if (!isValidYear(year)) {
            System.err.println("Error: Invalid year format. Expected a 4-digit year (e.g., 2025).");
            System.exit(EXIT_CODE_INVALID_ARGS);
        }

        if (!isValidDay(day)) {
            System.err.println("Error: Invalid day format. Expected 'day' followed by 1-2 digits (e.g., day1, day25).");
            System.exit(EXIT_CODE_INVALID_ARGS);
        }

        executePuzzle(year, day);
    }

    /**
     * Executes the puzzle solution class for the specified year and day.
     * Dynamically discovers the puzzle class by scanning the package directory,
     * then uses reflection to load and invoke the main method.
     */
    private static void executePuzzle(String year, String day) {
        String packageName = PACKAGE_PREFIX + year + "." + day;
        String dayNumber = extractDayNumber(day);
        String classSuffix = "AOC" + year + "Day" + dayNumber;

        try {
            String puzzleClassName = discoverPuzzleClass(packageName, classSuffix);

            if (puzzleClassName == null) {
                System.err.println("Error: Puzzle class for year " + year + " " + day + " not found.");
                System.exit(EXIT_CODE_PUZZLE_NOT_FOUND);
            }

            Class<?> puzzleClass = Class.forName(puzzleClassName);
            Method mainMethod = puzzleClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[]{});

        } catch (ClassNotFoundException e) {
            System.err.println("Error: Puzzle class not found - " + e.getMessage());
            System.exit(EXIT_CODE_EXECUTION_ERROR);
        } catch (NoSuchMethodException e) {
            System.err.println("Error: Puzzle class does not have a public static main(String[] args) method.");
            System.exit(EXIT_CODE_EXECUTION_ERROR);
        } catch (Exception e) {
            System.err.println("Error executing puzzle: " + e.getMessage());
            System.exit(EXIT_CODE_EXECUTION_ERROR);
        }
    }

    /**
     * Discovers the puzzle class by scanning the package directory for a class file
     * that ends with the specified class suffix (e.g., AOC2025Day1).
     *
     * Returns the fully-qualified class name if found, or null if not found.
     * If multiple matching classes are found, returns the first one and prints a warning.
     */
    private static String discoverPuzzleClass(String packageName, String classSuffix) {
        try {
            String packagePath = packageName.replace('.', '/');
            URL packageUrl = Main.class.getClassLoader().getResource(packagePath);

            if (packageUrl == null) {
                return null;
            }

            String decodedPath = URLDecoder.decode(packageUrl.getPath(), StandardCharsets.UTF_8);
            // Handle Windows paths that may start with /C:/ format
            if (decodedPath.startsWith("/") && decodedPath.length() > 2 && decodedPath.charAt(2) == ':') {
                decodedPath = decodedPath.substring(1);
            }

            Path packageDir = Paths.get(decodedPath);

            if (!Files.isDirectory(packageDir)) {
                return null;
            }

            // Find all classes matching the suffix pattern (excluding inner classes)
            List<String> matchingClasses = new ArrayList<>();

            try (Stream<Path> files = Files.list(packageDir)) {
                files
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".class") && name.contains(classSuffix) && !name.contains("$"))
                    .forEach(name -> {
                        String className = packageName + "." + name.replace(".class", "");
                        matchingClasses.add(className);
                    });
            }

            if (matchingClasses.isEmpty()) {
                return null;
            }

            if (matchingClasses.size() > 1) {
                System.err.println("Warning: Multiple matching classes found. Using the first one: " + matchingClasses.get(0));
            }

            return matchingClasses.get(0);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Validates that the year is a valid 4-digit year.
     */
    private static boolean isValidYear(String year) {
        return year.matches("\\d{4}");
    }

    /**
     * Validates that the day format is correct (e.g., "day1", "day25").
     */
    private static boolean isValidDay(String day) {
        return day.matches("day\\d{1,2}");
    }

    /**
     * Extracts the day number from the day format string.
     * Example: "day3" returns "3", "day25" returns "25"
     */
    private static String extractDayNumber(String day) {
        return day.replaceAll("day", "");
    }

    /**
     * Prints usage information for the puzzle executor.
     */
    private static void printUsage() {
        System.out.println("Advent of Code Puzzle Executor");
        System.out.println("==============================");
        System.out.println();
        System.out.println("Usage: java odogwudozilla.Main <year> <day>");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  <year>  - The year of the puzzle (e.g., 2025, 2024, 2023)");
        System.out.println("  <day>   - The day of the puzzle in format 'day<D>' (e.g., day1, day25)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java odogwudozilla.Main 2025 day1   # Run 2025 Day 1");
        System.out.println("  java odogwudozilla.Main 2024 day18  # Run 2024 Day 18");
        System.out.println("  java odogwudozilla.Main 2018 day3   # Run 2018 Day 3");
        System.out.println();
    }
}

