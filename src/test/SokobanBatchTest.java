import solver.SokoBot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Batch test runner for Sokoban solver.
 *
 * Scans the maps/ folder and runs the solver per map with a 10s timeout.
 * Prints: [#<n>] <filename> | STATUS <OK|TIMEOUT|FAILED|NOSOLN> in <elapsed_ms>
 */
public class SokobanBatchTest {

    public static class TestResult {
        public final String filename; // file name
        public final long elapsed;    // time taken in milliseconds
        public final String status;   // OK / TIMEOUT / FAILED / NOSOLN

        public TestResult(String filename, long elapsed, String status) {
            this.filename = filename;
            this.elapsed = elapsed;
            this.status = status;
        }
    }

    /**
     * Test a single map file with a 10-second timeout.
     * The solver is considered OK if it completes within time and returns a non-"No solution found." result.
     */
    public static TestResult testOne(File mapFile) {
        final String filename = mapFile.getName();

        ExecutorService exec = Executors.newSingleThreadExecutor();
        long startNs = System.nanoTime();
        try {
            Callable<String> task = () -> {
                // Taken from @/gui/GamePanel.loadMap
                List<String> lines = readAllLines(mapFile);
                int rows = lines.size();
                int columns = lines.stream().mapToInt(String::length).max().orElse(0);
                char[][] tiles = new char[rows][columns];
                for (int i = 0; i < rows; i++) {
                    Arrays.fill(tiles[i], ' ');
                    String line = lines.get(i);
                    for (int j = 0; j < line.length(); j++) {
                        tiles[i][j] = line.charAt(j);
                    }
                }

                char[][] map = new char[rows][columns];
                char[][] items = new char[rows][columns];
                int playerCount = 0;
                int boxCount = 0;
                int goalCount = 0;
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        char ch = tiles[i][j];
                        switch (ch) {
                            case '#':
                                map[i][j] = '#';
                                items[i][j] = ' ';
                                break;
                            case '@':
                                map[i][j] = ' ';
                                items[i][j] = '@';
                                playerCount++;
                                break;
                            case '$':
                                map[i][j] = ' ';
                                items[i][j] = '$';
                                boxCount++;
                                break;
                            case '.':
                                map[i][j] = '.';
                                items[i][j] = ' ';
                                goalCount++;
                                break;
                            case '+':
                                map[i][j] = '.';
                                items[i][j] = '@';
                                playerCount++;
                                goalCount++;
                                break;
                            case '*':
                                map[i][j] = '.';
                                items[i][j] = '$';
                                boxCount++;
                                goalCount++;
                                break;
                            case ' ':
                            default:
                                map[i][j] = ' ';
                                items[i][j] = ' ';
                                break;
                        }
                    }
                }

                if (!(playerCount == 1 && boxCount == goalCount && boxCount > 0)) {
                    return "No solution found."; // treat invalid maps as unsolved
                }

                SokoBot bot = new SokoBot();
                return bot.solveSokobanPuzzle(columns, rows, map, items);
            };

            Future<String> future = exec.submit(task);
            String result = future.get(30, TimeUnit.SECONDS);
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

            boolean solved = result != null && !result.trim().isEmpty() && !"No solution found.".equals(result);
            return new TestResult(filename, elapsedMs, solved ? "OK" : "NOSOLN");

        } catch (TimeoutException e) {
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            return new TestResult(filename, elapsedMs, "TIMEOUT");
        } catch (InterruptedException | ExecutionException e) {
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            return new TestResult(filename, elapsedMs, "FAILED");
        } finally {
            exec.shutdownNow();

        }
    }

    private static List<String> readAllLines(File file) throws FileNotFoundException {
        List<String> lines = new ArrayList<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
        }
        return lines;
    }

    public static void main(String[] args) {
        File mapsDir = new File("maps");
        File[] files = mapsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.err.println("No map files found in maps/");
            return;
        }

        Arrays.sort(files, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
        int testNum = 1;
        int okCount = 0;
        int failCount = 0;
        List<String> lines = new ArrayList<>();
        for (File f : files) {
            TestResult r = testOne(f);
            if ("OK".equals(r.status)) okCount++; else failCount++;
            lines.add(String.format("[#%d] %s | %s in %dms", testNum, r.filename, r.status, r.elapsed));
            System.out.printf("[#%d] %s | %s in %dms%n", testNum, r.filename, r.status, r.elapsed);
            testNum++;
        }

        for (String line : lines) {
            System.out.println(line);
        }
        System.out.printf("Summary: %d total | %d OK | %d FAILED%n", lines.size(), okCount, failCount);
    }
}
