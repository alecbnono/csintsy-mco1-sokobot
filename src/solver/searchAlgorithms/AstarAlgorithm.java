package solver.searchAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import solver.main.GameState;
import solver.main.Point;

public class AstarAlgorithm {

  public static class SearchNode {
    public final GameState state;
    public final int movesMade;
    public final SearchNode parent;

    public SearchNode(GameState state, int movesMade, SearchNode parent) {
      this.state = state;
      this.movesMade = movesMade;
      this.parent = parent;
    }
  }

  public int startThreshold(GameState startState) {
    int movesMade = 0;
    return f(startState, movesMade);
  }

  public int manhattanDistance(GameState state) {
    return state.getHeuristicValue();
  }

  public int costFunction(int movesMade) {
    return movesMade;
  }

  public int f(GameState state, int movesMade) {
    return costFunction(movesMade) + manhattanDistance(state);
  }

  public List<GameState> AstarSearch(GameState startState) {
    PriorityQueue<SearchNode> openSet = new PriorityQueue<>(
        Comparator.comparingInt(n -> n.movesMade + n.state.getHeuristicValue()));
    Map<GameState, Integer> visited = new HashMap<>();

    openSet.add(new SearchNode(startState, 0, null));
    visited.put(startState, 0);

    while (!openSet.isEmpty()) {
      SearchNode current = openSet.poll();

      if (isGoal(current.state)) {
        return reconstructPath(current);
      }

      if (visited.get(current.state) < current.movesMade) {
        continue;
      }

      boolean deadlock = false;
      for (Point box : current.state.getBoxPosition()) {
        if (current.state.getLevel().getDeadlocks().contains(box)) {
          deadlock = true;
          break;
        }
      }
      if (deadlock)
        continue;

      for (GameState nextState : current.state.getNextStates()) {
        int nextMove = current.movesMade + 1;
        if (!visited.containsKey(nextState) || nextMove < visited.get(nextState)) {
          visited.put(nextState, nextMove);
          openSet.add(new SearchNode(nextState, nextMove, current));
        }
      }
    }
    return null;
  }

  private boolean isGoal(GameState state) {
    return state.getBoxPosition().containsAll(state.getLevel().getTargets())
        && state.getLevel().getTargets().containsAll(state.getBoxPosition());
  }

  private List<GameState> reconstructPath(SearchNode node) {
    List<GameState> path = new ArrayList<>();
    SearchNode current = node;
    while (current != null) {
      path.add(0, current.state);
      current = current.parent;
    }
    return path;
  }
}
