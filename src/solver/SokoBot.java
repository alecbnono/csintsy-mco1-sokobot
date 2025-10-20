package solver;

import java.util.List;
import solver.main.GameState;
import solver.main.LevelData;
import solver.searchAlgorithms.AstarAlgorithm;
import solver.searchAlgorithms.GreedyBestFirstSearch;

public class SokoBot {

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    LevelData level = new LevelData(width, height, mapData, itemsData);
    AstarAlgorithm astar = new AstarAlgorithm();

    // List<GameState> solution = astar.AstarSearch(level.getOrigin());
    List<GameState> solution = GreedyBestFirstSearch.GBFS(level.getOrigin());

    if (solution == null || solution.isEmpty()) {
      return "No solution found.";
    }

    StringBuilder moves = new StringBuilder(solution.size() - 1);

    GameState prev = solution.get(0);
    for (int i = 1; i < solution.size(); i++) {
      GameState curr = solution.get(i);
      moves.append(curr.getMoveMadeFrom(prev));
      prev = curr;
    }


    return moves.toString();
  }
}
