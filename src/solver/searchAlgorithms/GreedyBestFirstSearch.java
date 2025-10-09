package solver.searchAlgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import solver.heuristics.Heuristics;
import solver.main.GameState;
import solver.main.Point;

public class GreedyBestFirstSearch {
    public static List<GameState> GBFS(GameState startState, HashSet<Point> targetPoints,
            HashSet<Point> wallPoints) {
        Heuristics heuristics = new Heuristics();

        PriorityQueue<GameState> frontier = new PriorityQueue<>(Comparator.comparingInt(
                state -> heuristics.getStateHeuristic(state.getBoxPosition(), targetPoints, wallPoints)));

        Set<GameState> visited = new HashSet<>();
        Map<GameState, GameState> cameFrom = new HashMap<>();

        frontier.add(startState);
        while (!frontier.isEmpty()) {
            GameState current = frontier.poll();

            if (isGoalState(current, targetPoints)) {
                return reconstructPath(cameFrom, current);
            }

            visited.add(current);

            for (GameState neighbor : current.getNextStates()) {
                if (visited.contains(neighbor) || frontier.contains(neighbor))
                    continue;

                boolean deadlock = false;

                if (heuristics.isStateDeadlock(neighbor.getBoxPosition(), targetPoints, wallPoints)) {
                    continue;
                }

                if (deadlock)
                    continue;

                cameFrom.put(neighbor, current);

                frontier.add(neighbor);
            }
        }

        return Collections.emptyList();
    }

    private static boolean isGoalState(GameState state, HashSet<Point> targets) {
        return targets.containsAll(state.getBoxPosition());
    }

    private static List<GameState> reconstructPath(Map<GameState, GameState> cameFrom, GameState current) {
        List<GameState> path = new ArrayList<>();

        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}
