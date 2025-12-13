package odogwudozilla.dashboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Generates an HTML web dashboard to track Advent of Code solving progress.
 * Displays statistics and completion status organized by year and day.
 */
public class WebDashboardGenerator {

    private static final String DASHBOARD_OUTPUT_DIR = "dashboard";
    private static final String DASHBOARD_FILENAME = "index.html";

    private final Map<String, Map<Integer, PuzzleInfo>> dashboardData;

    public WebDashboardGenerator() {
        this.dashboardData = new LinkedHashMap<>();
        initializeDashboardDirectory();
    }

    /**
     * Inner class to store puzzle information.
     */
    private static class PuzzleInfo {
        String day;
        String title;
        boolean partOne;
        boolean partTwo;
        long executionTime;

        PuzzleInfo(String day, String title, boolean partOne, boolean partTwo, long executionTime) {
            this.day = day;
            this.title = title;
            this.partOne = partOne;
            this.partTwo = partTwo;
            this.executionTime = executionTime;
        }
    }

    /**
     * Initializes the dashboard output directory.
     */
    private void initializeDashboardDirectory() {
        try {
            Files.createDirectories(Paths.get(DASHBOARD_OUTPUT_DIR));
        } catch (IOException e) {
            System.err.println("Warning: Could not create dashboard directory: " + e.getMessage());
        }
    }

    /**
     * Adds puzzle completion data to the dashboard.
     * @param year the puzzle year
     * @param day the puzzle day
     * @param title the puzzle title
     * @param partOneStatus completion status for Part 1
     * @param partTwoStatus completion status for Part 2
     * @param executionTime execution time in milliseconds
     */
    public void addPuzzleData(String year, String day, String title, boolean partOneStatus,
                             boolean partTwoStatus, long executionTime) {
        dashboardData.computeIfAbsent(year, k -> new LinkedHashMap<>())
            .put(Integer.parseInt(day), new PuzzleInfo(day, title, partOneStatus, partTwoStatus, executionTime));
    }

    /**
     * Generates the HTML dashboard file.
     */
    public void generateDashboard() {
        try {
            String htmlContent = buildHtmlContent();
            Path dashboardPath = Paths.get(DASHBOARD_OUTPUT_DIR, DASHBOARD_FILENAME);
            Files.write(dashboardPath, htmlContent.getBytes());
            System.out.println("Dashboard generated: " + dashboardPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error generating dashboard: " + e.getMessage());
        }
    }

    /**
     * Builds the complete HTML content for the dashboard.
     */
    private String buildHtmlContent() {

        return  "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Advent of Code - Progress Dashboard</title>\n" +
                "    <style>\n" +
                getCssStyles() +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <h1>Advent of Code Progress</h1>\n" +
                "    <p class=\"subtitle\">Tracking puzzle solutions by year and day</p>\n" +
                "    <p class=\"timestamp\">Generated: " + getCurrentTimestamp() + "</p>\n" +
                buildGlobalStatistics() +
                buildYearSections() +
                "</div>\n" +
                "<footer class=\"footer\">\n" +
                "    <p>Advent of Code Progress Dashboard | Last updated " + getCurrentTimestamp() + "</p>\n" +
                "</footer>\n" +
                "</body>\n" +
                "</html>\n";

    }

    /**
     * Builds global statistics cards.
     */
    private String buildGlobalStatistics() {
        // Total possible puzzles: 2015-2024 have 25 days each (10 years = 250), 2025 has 12 days = 262 total
        int totalPossiblePuzzles = (10 * 25) + 12; // 250 + 12 = 262

        // Calculate completed puzzles (both parts solved)
        int completedPuzzles = 0;
        for (Map.Entry<String, Map<Integer, PuzzleInfo>> yearEntry : dashboardData.entrySet()) {
            for (PuzzleInfo info : yearEntry.getValue().values()) {
                if (info.partOne && info.partTwo) {
                    completedPuzzles++;
                }
            }
        }

        return getHtml(completedPuzzles, totalPossiblePuzzles);
    }

    private static @NotNull String getHtml(int completedPuzzles, int totalPossiblePuzzles) {
        int percentage = completedPuzzles * 100 / totalPossiblePuzzles;

        String html = "<div class=\"global-stats\">\n" +
                "    <div class=\"stat-card\">\n" +
                "        <div class=\"stat-label\">Total Puzzles (Possible)</div>\n" +
                "        <div class=\"stat-value\">" + totalPossiblePuzzles + "</div>\n" +
                "    </div>\n" +
                "    <div class=\"stat-card\">\n" +
                "        <div class=\"stat-label\">Completed</div>\n" +
                "        <div class=\"stat-value\">" + completedPuzzles + "</div>\n" +
                "    </div>\n" +
                "    <div class=\"stat-card\">\n" +
                "        <div class=\"stat-label\">Completion Rate</div>\n" +
                "        <div class=\"stat-value\">" + percentage + "%</div>\n" +
                "    </div>\n" +
                "</div>\n";
        return html;
    }

    /**
     * Builds year-by-year sections with day grids.
     */
    private String buildYearSections() {
        StringBuilder html = new StringBuilder();

        List<String> years = new ArrayList<>(dashboardData.keySet());
        years.sort(Collections.reverseOrder());

        for (String year : years) {
            Map<Integer, PuzzleInfo> puzzles = dashboardData.get(year);

            int totalDays = year.equals("2025") ? 12 : 25;

            int solvedCount = (int) puzzles.values().stream()
                .filter(p -> p.partOne && p.partTwo)
                .count();

            html.append("<div class=\"year-section\">\n");
            html.append("    <div class=\"year-header\">\n");
            html.append("        <div class=\"year-title\">").append(year).append("</div>\n");
            html.append("        <div class=\"year-stats\">\n");
            html.append("            Completed: <span>").append(solvedCount).append("</span> / ").append(totalDays).append("\n");
            html.append("        </div>\n");
            html.append("    </div>\n");
            html.append("    <div class=\"day-grid\">\n");

            for (int day = 1; day <= totalDays; day++) {
                PuzzleInfo info = puzzles.get(day);
                boolean isSolved = info != null && info.partOne && info.partTwo;
                String cssClass = isSolved ? "day-solved" : "day-unsolved";

                html.append("        <div class=\"day-cell ").append(cssClass).append("\" title=\"");
                if (info != null) {
                    html.append(info.title);
                }
                html.append("\">\n");
                html.append("            ").append(day).append("\n");
                html.append("        </div>\n");
            }

            html.append("    </div>\n");
            html.append("</div>\n");
        }

        return html.toString();
    }

    /**
     * Builds the CSS styles for the dashboard.
     * Matches the Advent of Code website theme colors and design.
     */
    private String getCssStyles() {
        return "* { margin: 0; padding: 0; box-sizing: border-box; } " +
            "body { font-family: 'Courier New', 'Courier', monospace; " +
            "background: #0a0e27; " +
            "min-height: 100vh; padding: 20px; color: #cccccc; } " +
            ".container { max-width: 1400px; margin: 0 auto; background: #0f1419; " +
            "border-radius: 0px; box-shadow: 0 0 30px rgba(0,0,0,0.5); padding: 40px; " +
            "border: 1px solid #1a1f2e; } " +
            "h1 { color: #ffcc00; margin-bottom: 10px; text-align: center; font-size: 2.5em; " +
            "text-shadow: 0 0 10px rgba(255, 204, 0, 0.3); } " +
            ".subtitle { text-align: center; color: #888888; font-size: 1em; margin-bottom: 30px; } " +
            ".timestamp { text-align: center; color: #666666; font-size: 0.85em; margin-bottom: 20px; } " +
            ".global-stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); " +
            "gap: 20px; margin-bottom: 40px; } " +
            ".stat-card { background: #1a1f2e; color: #ffcc00; padding: 25px; border-radius: 0px; " +
            "text-align: center; border: 1px solid #2a3f5f; transition: all 0.3s ease; } " +
            ".stat-card:hover { border-color: #ffcc00; box-shadow: 0 0 20px rgba(255, 204, 0, 0.2); } " +
            ".stat-value { font-size: 2.8em; font-weight: bold; margin: 10px 0; } " +
            ".stat-label { font-size: 0.85em; color: #888888; } " +
            ".year-section { margin-bottom: 60px; padding-bottom: 40px; border-bottom: 1px solid #2a3f5f; } " +
            ".year-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; } " +
            ".year-title { font-size: 1.8em; color: #ffcc00; font-weight: bold; } " +
            ".year-stats { font-size: 0.95em; color: #888888; } " +
            ".year-stats span { font-weight: bold; color: #ffcc00; margin: 0 10px; } " +
            ".day-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(50px, 1fr)); " +
            "gap: 10px; } " +
            ".day-cell { aspect-ratio: 1; display: flex; align-items: center; justify-content: center; " +
            "border-radius: 0px; font-weight: bold; font-size: 1em; cursor: pointer; " +
            "transition: all 0.2s ease; border: 1px solid #2a3f5f; } " +
            ".day-cell:hover { border-color: #ffcc00; box-shadow: 0 0 15px rgba(255, 204, 0, 0.3); } " +
            ".day-unsolved { background: #0a0e27; color: #333333; border-color: #1a1f2e; } " +
            ".day-solved { background: #1a2f1a; color: #00ff00; border: 1px solid #00ff00; " +
            "box-shadow: 0 0 10px rgba(0, 255, 0, 0.2); } " +
            ".footer { text-align: center; margin-top: 40px; padding-top: 20px; " +
            "border-top: 1px solid #2a3f5f; color: #666666; font-size: 0.9em; }";
    }

    /**
     * Gets the current timestamp formatted for display.
     */
    private String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    /**
     * Clears all dashboard data.
     */
    public void clearData() {
        dashboardData.clear();
    }

    /**
     * Gets the dashboard output path.
     */
    public Path getDashboardPath() {
        return Paths.get(DASHBOARD_OUTPUT_DIR, DASHBOARD_FILENAME).toAbsolutePath();
    }
}

