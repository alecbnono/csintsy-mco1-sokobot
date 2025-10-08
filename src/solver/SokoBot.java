package solver;

import java.util.List;

import solver.heuristics.Heuristics;
import solver.main.GameState;
import solver.main.LevelData;
import solver.searchAlgorithms.GreedyBestFirstSearch;

public class SokoBot {

    public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
        LevelData level = new LevelData(width, height, mapData, itemsData);

        Heuristics heuristics = new Heuristics();

        GameState startState = level.getOrigin();

        List<GameState> solution = GreedyBestFirstSearch.GBFS(startState, heuristics, level.getTargets(),
                level.getWalls());

        StringBuilder moves = new StringBuilder();
        for (int i = 1; i < solution.size(); i++) {
            GameState prev = solution.get(i - 1);
            GameState curr = solution.get(i);

            char move = curr.getMoveMadeFrom(prev);

            moves.append(move);
        }

        if (solution.isEmpty()) {
            return "No solution found.";
        }

        return moves.toString();

    }

}
