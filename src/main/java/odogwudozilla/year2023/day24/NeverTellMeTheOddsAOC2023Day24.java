package odogwudozilla.year2023.day24;

import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

/**
 * Never Tell Me The Odds (Advent of Code 2023 Day 24)
 * <p>
 * It seems like something is going wrong with the snow-making process. Instead of forming snow,
 * the water that's been absorbed into the air seems to be forming hail!
 * <p>
 * Due to strong, probably-magical winds, the hailstones are all flying through the air in
 * perfectly linear trajectories. You make a note of each hailstone's position and velocity
 * (your puzzle input).
 * <p>
 * See the full puzzle at: https://adventofcode.com/2023/day/24
 */
public class NeverTellMeTheOddsAOC2023Day24 {

    private static final String INPUT_PATH = "src/main/resources/2023/day24/day24_puzzle_data.txt";
    private static final long TEST_AREA_MIN = 200_000_000_000_000L;
    private static final long TEST_AREA_MAX = 400_000_000_000_000L;
    private static final MathContext MC = new MathContext(60);

    private static class Hailstone {
        final double px, py, vx, vy;

        Hailstone(double px, double py, double vx, double vy) {
            this.px = px;
            this.py = py;
            this.vx = vx;
            this.vy = vy;
        }
    }

    private static class Hailstone3D {
        final long px, py, pz, vx, vy, vz;

        Hailstone3D(long px, long py, long pz, long vx, long vy, long vz) {
            this.px = px;
            this.py = py;
            this.pz = pz;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
        }
    }

    public static void main(String[] args) {
        List<String> inputLines = readInput(INPUT_PATH);
        System.out.println("Part 1: " + solvePartOne(inputLines));
        System.out.println("Part 2: " + solvePartTwo(inputLines));
    }

    @NotNull
    private static List<String> readInput(@NotNull String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input file: " + path, e);
        }
    }

    /**
     * Parses a hailstone from a line of input (XY only).
     * @param line the input line
     * @return the Hailstone object
     */
    @NotNull
    private static Hailstone parseHailstone(@NotNull String line) {
        String[] parts = line.split("[@,]");
        double px = Double.parseDouble(parts[0].trim());
        double py = Double.parseDouble(parts[1].trim());
        double vx = Double.parseDouble(parts[3].trim());
        double vy = Double.parseDouble(parts[4].trim());
        return new Hailstone(px, py, vx, vy);
    }

    /**
     * Parses a 3D hailstone from a line of input.
     * @param line the input line
     * @return the Hailstone3D object
     */
    @NotNull
    private static Hailstone3D parseHailstone3D(@NotNull String line) {
        String[] parts = line.split("[@,]");
        long px = Long.parseLong(parts[0].trim());
        long py = Long.parseLong(parts[1].trim());
        long pz = Long.parseLong(parts[2].trim());
        long vx = Long.parseLong(parts[3].trim());
        long vy = Long.parseLong(parts[4].trim());
        long vz = Long.parseLong(parts[5].trim());
        return new Hailstone3D(px, py, pz, vx, vy, vz);
    }

    /**
     * Checks if two hailstones' paths intersect in the future within the test area.
     * @param a first hailstone
     * @param b second hailstone
     * @return true if intersection is within the test area and in the future
     */
    private static boolean pathsIntersectInTestArea(@NotNull Hailstone a, @NotNull Hailstone b) {
        double det = a.vx * b.vy - b.vx * a.vy;
        if (Math.abs(det) < 1e-9) return false;
        double dx = b.px - a.px;
        double dy = b.py - a.py;
        double tA = (dx * b.vy - dy * b.vx) / det;
        double tB = (dx * a.vy - dy * a.vx) / det;
        if (tA < 0 || tB < 0) return false;
        double x = a.px + a.vx * tA;
        double y = a.py + a.vy * tA;
        return x >= TEST_AREA_MIN && x <= TEST_AREA_MAX && y >= TEST_AREA_MIN && y <= TEST_AREA_MAX;
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param inputLines the puzzle input lines
     * @return the number of intersections within the test area
     */
    private static long solvePartOne(@NotNull List<String> inputLines) {
        int n = inputLines.size();
        Hailstone[] hailstones = new Hailstone[n];
        for (int i = 0; i < n; i++) {
            hailstones[i] = parseHailstone(inputLines.get(i));
        }
        long count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pathsIntersectInTestArea(hailstones[i], hailstones[j])) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Solves Part 2 of the puzzle.
     * <p>
     * Finds the unique rock position (rx, ry, rz) and velocity (rvx, rvy, rvz) such that
     * the rock collides with every hailstone at some positive integer time t_i.
     * <p>
     * We set up a 6x6 linear system using 3 pairs of hailstones. For each pair (i, j),
     * subtracting the collision equations eliminates the nonlinear product terms, yielding
     * linear equations in the 6 unknowns.
     * <p>
     * Derivation for the XY plane:
     * For hailstone i: (rx - px_i)*(rvy - vy_i) = (ry - py_i)*(rvx - vx_i)
     * For hailstone j: (rx - px_j)*(rvy - vy_j) = (ry - py_j)*(rvx - vx_j)
     * Subtracting eliminates the nonlinear terms rx*rvy and ry*rvx:
     *   rx*(vy_i - vy_j) + ry*(vx_j - vx_i) + rvx*(py_j - py_i) + rvy*(px_i - px_j)
     *     = px_i*vy_i - py_i*vx_i - px_j*vy_j + py_j*vx_j
     * <p>
     * All arithmetic uses exact BigInteger to avoid any precision loss.
     * @param inputLines the puzzle input lines
     * @return the sum of the rock's initial position coordinates
     */
    private static long solvePartTwo(@NotNull List<String> inputLines) {
        int n = inputLines.size();
        Hailstone3D[] hail = new Hailstone3D[n];
        for (int i = 0; i < n; i++) {
            hail[i] = parseHailstone3D(inputLines.get(i));
        }

        // 6 unknowns: [rx, ry, rz, rvx, rvy, rvz] at indices [0, 1, 2, 3, 4, 5]
        // Build 6 equations from 3 pairs (0,1), (0,2), (0,3):
        //   - 1 XY equation per pair => 3 equations covering unknowns rx, ry, rvx, rvy
        //   - 1 XZ equation per pair => 3 equations covering unknowns rx, rz, rvx, rvz
        BigInteger[][] augmented = new BigInteger[6][7];
        for (BigInteger[] row : augmented) {
            Arrays.fill(row, BigInteger.ZERO);
        }

        int eq = 0;
        for (int pairIdx = 1; pairIdx <= 3; pairIdx++) {
            Hailstone3D hi = hail[0];
            Hailstone3D hj = hail[pairIdx];
            BigInteger pxi = BigInteger.valueOf(hi.px);
            BigInteger pyi = BigInteger.valueOf(hi.py);
            BigInteger pzi = BigInteger.valueOf(hi.pz);
            BigInteger vxi = BigInteger.valueOf(hi.vx);
            BigInteger vyi = BigInteger.valueOf(hi.vy);
            BigInteger vzi = BigInteger.valueOf(hi.vz);
            BigInteger pxj = BigInteger.valueOf(hj.px);
            BigInteger pyj = BigInteger.valueOf(hj.py);
            BigInteger pzj = BigInteger.valueOf(hj.pz);
            BigInteger vxj = BigInteger.valueOf(hj.vx);
            BigInteger vyj = BigInteger.valueOf(hj.vy);
            BigInteger vzj = BigInteger.valueOf(hj.vz);

            // XY equation:
            //   rx*(vy_i - vy_j) + ry*(vx_j - vx_i) + rvx*(py_j - py_i) + rvy*(px_i - px_j)
            //     = px_i*vy_i - py_i*vx_i - px_j*vy_j + py_j*vx_j
            augmented[eq][0] = vyi.subtract(vyj);                                   // rx
            augmented[eq][1] = vxj.subtract(vxi);                                   // ry
            augmented[eq][2] = BigInteger.ZERO;                                     // rz
            augmented[eq][3] = pyj.subtract(pyi);                                   // rvx
            augmented[eq][4] = pxi.subtract(pxj);                                   // rvy
            augmented[eq][5] = BigInteger.ZERO;                                     // rvz
            augmented[eq][6] = pxi.multiply(vyi).subtract(pyi.multiply(vxi))
                                .subtract(pxj.multiply(vyj)).add(pyj.multiply(vxj)); // RHS
            eq++;

            // XZ equation:
            //   rx*(vz_i - vz_j) + rz*(vx_j - vx_i) + rvx*(pz_j - pz_i) + rvz*(px_i - px_j)
            //     = px_i*vz_i - pz_i*vx_i - px_j*vz_j + pz_j*vx_j
            augmented[eq][0] = vzi.subtract(vzj);                                   // rx
            augmented[eq][1] = BigInteger.ZERO;                                     // ry
            augmented[eq][2] = vxj.subtract(vxi);                                   // rz
            augmented[eq][3] = pzj.subtract(pzi);                                   // rvx
            augmented[eq][4] = BigInteger.ZERO;                                     // rvy
            augmented[eq][5] = pxi.subtract(pxj);                                   // rvz
            augmented[eq][6] = pxi.multiply(vzi).subtract(pzi.multiply(vxi))
                                .subtract(pxj.multiply(vzj)).add(pzj.multiply(vxj)); // RHS
            eq++;
        }

        // Solve using BigDecimal Gaussian elimination - convert from BigInteger to BigDecimal
        BigDecimal[][] bdAugmented = new BigDecimal[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                bdAugmented[i][j] = new BigDecimal(augmented[i][j]);
            }
        }

        BigDecimal[] solution = gaussianElimination(bdAugmented);

        long rx = solution[0].setScale(0, RoundingMode.HALF_UP).longValueExact();
        long ry = solution[1].setScale(0, RoundingMode.HALF_UP).longValueExact();
        long rz = solution[2].setScale(0, RoundingMode.HALF_UP).longValueExact();
        return rx + ry + rz;
    }

    /**
     * Solves an NxN linear system using Gaussian elimination with partial pivoting
     * and BigDecimal arithmetic (60 significant digits precision).
     * @param M the Nx(N+1) augmented matrix
     * @return the solution vector of length N
     */
    private static BigDecimal[] gaussianElimination(BigDecimal[][] M) {
        int size = M.length;
        for (int col = 0; col < size; col++) {
            // Find pivot with the largest absolute value in this column
            int pivotRow = col;
            for (int row = col + 1; row < size; row++) {
                if (M[row][col].abs().compareTo(M[pivotRow][col].abs()) > 0) {
                    pivotRow = row;
                }
            }
            // Swap rows
            BigDecimal[] tmp = M[col];
            M[col] = M[pivotRow];
            M[pivotRow] = tmp;

            BigDecimal pivotVal = M[col][col];
            // Eliminate below
            for (int row = col + 1; row < size; row++) {
                BigDecimal factor = M[row][col].divide(pivotVal, MC);
                for (int c = col; c <= size; c++) {
                    M[row][c] = M[row][c].subtract(factor.multiply(M[col][c], MC), MC);
                }
            }
        }
        // Back substitution
        BigDecimal[] x = new BigDecimal[size];
        for (int i = size - 1; i >= 0; i--) {
            x[i] = M[i][size];
            for (int j = i + 1; j < size; j++) {
                x[i] = x[i].subtract(M[i][j].multiply(x[j], MC), MC);
            }
            x[i] = x[i].divide(M[i][i], MC);
        }
        return x;
    }
}
