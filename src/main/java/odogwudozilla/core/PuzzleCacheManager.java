package odogwudozilla.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Manages caching of puzzle results for comparison and performance tracking.
 * Stores solution results to enable quick comparisons across multiple runs.
 */
public class PuzzleCacheManager {

    private static final String CACHE_DIR = "cache/puzzle-results";
    private final ObjectMapper objectMapper;

    public PuzzleCacheManager() {
        this.objectMapper = new ObjectMapper();
        initializeCacheDirectory();
    }

    /**
     * Initializes the cache directory if it doesn't exist.
     */
    private void initializeCacheDirectory() {
        try {
            Files.createDirectories(Paths.get(CACHE_DIR));
        } catch (IOException e) {
            System.err.println("Warning: Could not create cache directory: " + e.getMessage());
        }
    }

    /**
     * Caches a puzzle result.
     * @param year the puzzle year
     * @param day the puzzle day
     * @param part the part number (1 or 2)
     * @param result the solution result
     * @param executionTime execution time in milliseconds
     */
    public void cachePuzzleResult(String year, String day, int part, String result, long executionTime) {
        try {
            String cacheKey = generateCacheKey(year, day, part);
            Path cachePath = Paths.get(CACHE_DIR, cacheKey + ".json");

            Map<String, Object> cacheData = new LinkedHashMap<>();
            cacheData.put("year", year);
            cacheData.put("day", day);
            cacheData.put("part", part);
            cacheData.put("result", result);
            cacheData.put("executionTime", executionTime);
            cacheData.put("timestamp", System.currentTimeMillis());

            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cacheData);
            Files.write(cachePath, jsonContent.getBytes());

            System.out.println("Result cached: " + cachePath);
        } catch (IOException e) {
            System.err.println("Warning: Could not cache puzzle result: " + e.getMessage());
        }
    }

    /**
     * Retrieves a cached puzzle result.
     * @param year the puzzle year
     * @param day the puzzle day
     * @param part the part number (1 or 2)
     * @return the cached result, or null if not found
     */
    public String getCachedResult(String year, String day, int part) {
        try {
            String cacheKey = generateCacheKey(year, day, part);
            Path cachePath = Paths.get(CACHE_DIR, cacheKey + ".json");

            if (!Files.exists(cachePath)) {
                return null;
            }

            Map<?, ?> cacheData = objectMapper.readValue(cachePath.toFile(), Map.class);
            return (String) cacheData.get("result");
        } catch (IOException e) {
            System.err.println("Warning: Could not retrieve cached result: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if a result is cached.
     * @param year the puzzle year
     * @param day the puzzle day
     * @param part the part number (1 or 2)
     * @return true if the result is cached, false otherwise
     */
    public boolean isCached(String year, String day, int part) {
        String cacheKey = generateCacheKey(year, day, part);
        Path cachePath = Paths.get(CACHE_DIR, cacheKey + ".json");
        return Files.exists(cachePath);
    }

    /**
     * Compares current result with cached result.
     * @param year the puzzle year
     * @param day the puzzle day
     * @param part the part number (1 or 2)
     * @param currentResult the current solution result
     * @return true if results match, false otherwise
     */
    public boolean compareWithCache(String year, String day, int part, String currentResult) {
        String cachedResult = getCachedResult(year, day, part);
        if (cachedResult == null) {
            return false;
        }
        return cachedResult.equals(currentResult);
    }

    /**
     * Clears all cached results.
     */
    public void clearAllCache() {
        try {
            Path cacheDir = Paths.get(CACHE_DIR);
            if (Files.exists(cacheDir)) {
                Files.walk(cacheDir)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Could not delete cache file: " + path);
                        }
                    });
            }
            System.out.println("Cache cleared successfully");
        } catch (IOException e) {
            System.err.println("Warning: Could not clear cache: " + e.getMessage());
        }
    }

    /**
     * Clears cache for a specific puzzle.
     * @param year the puzzle year
     * @param day the puzzle day
     */
    public void clearPuzzleCache(String year, String day) {
        try {
            for (int part = 1; part <= 2; part++) {
                String cacheKey = generateCacheKey(year, day, part);
                Path cachePath = Paths.get(CACHE_DIR, cacheKey + ".json");
                if (Files.exists(cachePath)) {
                    Files.delete(cachePath);
                }
            }
            System.out.println("Cache cleared for " + year + " " + day);
        } catch (IOException e) {
            System.err.println("Warning: Could not clear puzzle cache: " + e.getMessage());
        }
    }

    /**
     * Generates a cache key for a specific puzzle part.
     * @param year the puzzle year
     * @param day the puzzle day
     * @param part the part number (1 or 2)
     * @return the cache key
     */
    private String generateCacheKey(String year, String day, int part) {
        return year + "_" + day + "_part" + part;
    }
}

