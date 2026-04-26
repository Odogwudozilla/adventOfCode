package odogwudozilla.automation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Generates the Java solution class skeleton and the two resource text files
 * for a given Advent of Code puzzle.
 *
 * Files created:
 * - {@code src/main/java/odogwudozilla/year<YYYY>/day<D>/<Title>AOC<YYYY>Day<D>.java}
 * - {@code src/main/resources/<YYYY>/day<D>/day<D>_puzzle_description.txt}
 * - {@code src/main/resources/<YYYY>/day<D>/day<D>_puzzle_data.txt} (if not already present)
 */
public final class SolutionSkeletonGenerator {

    private static final Logger LOGGER = Logger.getLogger(SolutionSkeletonGenerator.class.getName());

    /**
     * Generates the solution skeleton and resource files for the given puzzle.
     * @param info PuzzleInfo with year, day, title, and descriptions populated
     * @throws IOException if any file cannot be created or written
     */
    public void generate(@NotNull PuzzleInfo info) throws IOException {
        String className = resolveClassName(info);
        String packageName = AutomationConfig.PACKAGE_BASE
                + ".year" + info.getYear()
                + ".day" + info.getDay();

        Path javaDir = Paths.get(AutomationConfig.JAVA_BASE_PATH)
                .resolve(AutomationConfig.PACKAGE_BASE.replace('.', '/'))
                .resolve("year" + info.getYear())
                .resolve("day" + info.getDay())
                .toAbsolutePath();

        Path resourceDir = Paths.get(AutomationConfig.RESOURCES_BASE_PATH)
                .resolve(String.valueOf(info.getYear()))
                .resolve("day" + info.getDay())
                .toAbsolutePath();

        Files.createDirectories(javaDir);
        Files.createDirectories(resourceDir);

        createJavaClass(javaDir, className, packageName, info);
        createDescriptionFile(resourceDir, info);
        createDataFileIfAbsent(resourceDir, info);

        LOGGER.info("generate - skeleton created: " + className + ".java");
    }

    /**
     * Writes the Java solution class skeleton file.
     */
    private void createJavaClass(@NotNull Path dir,
                                 @NotNull String className,
                                 @NotNull String packageName,
                                 @NotNull PuzzleInfo info) throws IOException {
        Path classFile = dir.resolve(className + ".java");
        if (Files.exists(classFile)) {
            LOGGER.info("createJavaClass - class file already exists, skipping: " + classFile);
            return;
        }

        String resourcePath = info.getYear() + "/day" + info.getDay()
                + "/day" + info.getDay() + "_puzzle_data.txt";

        String content = buildClassContent(packageName, className, info, resourcePath);
        Files.writeString(classFile, content);
        LOGGER.info("createJavaClass - written: " + classFile);
    }

    /**
     * Builds the Java class file content as a string.
     */
    @NotNull
    private String buildClassContent(@NotNull String packageName,
                                     @NotNull String className,
                                     @NotNull PuzzleInfo info,
                                     @NotNull String resourcePath) {
        return "package " + packageName + ";\n\n"
                + "import java.io.IOException;\n"
                + "import java.io.InputStream;\n"
                + "import java.nio.charset.StandardCharsets;\n"
                + "import java.util.List;\n"
                + "import java.util.Scanner;\n\n"
                + "/**\n"
                + " * Advent of Code " + info.getYear() + " - Day " + info.getDay()
                + ": " + info.getTitle() + "\n"
                + " * <p>\n"
                + " * Puzzle URL: " + info.getPuzzleUrl() + "\n"
                + " */\n"
                + "public final class " + className + " {\n\n"
                + "    private static final String INPUT_FILE = \"/" + resourcePath + "\";\n\n"
                + "    /**\n"
                + "     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.\n"
                + "     * @param args command line arguments (not used)\n"
                + "     */\n"
                + "    public static void main(String[] args) {\n"
                + "        List<String> input = readInput();\n"
                + "        System.out.println(\"Part 1: \" + solvePartOne(input));\n"
                + "        System.out.println(\"Part 2: \" + solvePartTwo(input));\n"
                + "    }\n\n"
                + "    /**\n"
                + "     * Solves Part 1 of the puzzle.\n"
                + "     * @param input list of input lines\n"
                + "     * @return the Part 1 answer\n"
                + "     */\n"
                + "    private static String solvePartOne(List<String> input) {\n"
                + "        // TODO: implement Part 1 solution\n"
                + "        return \"not implemented\";\n"
                + "    }\n\n"
                + "    /**\n"
                + "     * Solves Part 2 of the puzzle.\n"
                + "     * @param input list of input lines\n"
                + "     * @return the Part 2 answer\n"
                + "     */\n"
                + "    private static String solvePartTwo(List<String> input) {\n"
                + "        // TODO: implement Part 2 solution\n"
                + "        return \"not implemented\";\n"
                + "    }\n\n"
                + "    /**\n"
                + "     * Reads the puzzle input file from the classpath.\n"
                + "     * @return list of input lines\n"
                + "     */\n"
                + "    private static List<String> readInput() {\n"
                + "        try (InputStream stream = " + className + ".class.getResourceAsStream(INPUT_FILE)) {\n"
                + "            if (stream == null) {\n"
                + "                throw new IllegalStateException(\"Input file not found: \" + INPUT_FILE);\n"
                + "            }\n"
                + "            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);\n"
                + "            java.util.List<String> lines = new java.util.ArrayList<>();\n"
                + "            while (scanner.hasNextLine()) {\n"
                + "                lines.add(scanner.nextLine());\n"
                + "            }\n"
                + "            return lines;\n"
                + "        } catch (IOException e) {\n"
                + "            throw new IllegalStateException(\"Failed to read input file\", e);\n"
                + "        }\n"
                + "    }\n"
                + "}\n";
    }

    /**
     * Writes the puzzle description to the description text file.
     */
    private void createDescriptionFile(@NotNull Path dir, @NotNull PuzzleInfo info) throws IOException {
        Path descFile = dir.resolve("day" + info.getDay() + "_puzzle_description.txt");

        StringBuilder content = new StringBuilder();
        content.append("Advent of Code ").append(info.getYear())
                .append(" - Day ").append(info.getDay())
                .append(": ").append(info.getTitle()).append("\n")
                .append(info.getPuzzleUrl()).append("\n\n")
                .append("=== Part 1 ===\n\n");

        if (info.getPartOneDescription() != null) {
            content.append(info.getPartOneDescription());
        }

        if (info.getPartTwoDescription() != null) {
            content.append("\n\n=== Part 2 ===\n\n")
                    .append(info.getPartTwoDescription());
        }

        Files.writeString(descFile, content.toString());
        LOGGER.info("createDescriptionFile - written: " + descFile);
    }

    /**
     * Creates an empty puzzle data file only if one does not already exist.
     * (The InputFetcher is responsible for populating this file.)
     */
    private void createDataFileIfAbsent(@NotNull Path dir, @NotNull PuzzleInfo info) throws IOException {
        Path dataFile = dir.resolve("day" + info.getDay() + "_puzzle_data.txt");
        if (!Files.exists(dataFile)) {
            Files.createFile(dataFile);
            LOGGER.info("createDataFileIfAbsent - created empty data file: " + dataFile);
        }
    }

    /**
     * Resolves the expected Java class name for a given puzzle.
     * Format: {@code <TitleCamelCase>AOC<Year>Day<Day>}
     * @param info PuzzleInfo with title, year, and day populated
     * @return the class name, e.g. {@code HighEntropyPassphrasesAOC2017Day4}
     */
    @NotNull
    public static String resolveClassName(@NotNull PuzzleInfo info) {
        String titlePart = toUpperCamelCase(info.getTitle() != null ? info.getTitle() : "Day" + info.getDay());
        return titlePart + "AOC" + info.getYear() + "Day" + info.getDay();
    }

    /**
     * Converts a puzzle title such as "Not Quite Lisp" to "NotQuiteLisp".
     * Non-alphanumeric characters are used as word separators and stripped.
     * @param title the raw puzzle title
     * @return a UpperCamelCase identifier string
     */
    @NotNull
    private static String toUpperCamelCase(@NotNull String title) {
        StringBuilder result = new StringBuilder();
        boolean capitaliseNext = true;

        for (char c : title.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                result.append(capitaliseNext ? Character.toUpperCase(c) : c);
                capitaliseNext = false;
            } else {
                capitaliseNext = true;
            }
        }

        return result.toString();
    }
}

