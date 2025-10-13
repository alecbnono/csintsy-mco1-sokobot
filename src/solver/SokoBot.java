import java.util.List;
import solver.main.GameState;
import solver.main.LevelData;
import solver.searchAlgorithms.GreedyBestFirstSearch;

public class SokoBot {

    public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
        LevelData level = new LevelData(width, height, mapData, itemsData);
        List<GameState> solution = GreedyBestFirstSearch.GBFS(level.getOrigin());



        if (solution == null || solution.isEmpty()) {
            return "No solution found.";
@@ -26,4 +28,4 @@ public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[]

        return moves.toString();
    }
}
