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
import solver.main.GameState;

public class GreedyBestFirstSearch {

    public static List<GameState> GBFS(GameState startState) {

        // PriorityQueue sorts by heuristic value
        PriorityQueue<GameState> frontier = new PriorityQueue<>(
                Comparator.comparingInt(GameState::getHeuristicValue));

        Set<GameState> visited = new HashSet<>();
        Set<GameState> frontierSet = new HashSet<>();
        Map<GameState, GameState> cameFrom = new HashMap<>();

        frontier.add(startState);
        frontierSet.add(startState);

        while (!frontier.isEmpty()) {
            GameState current = frontier.poll();
            frontierSet.remove(current);

            visited.add(current);

            // Check goal
            if (isGoalState(current)) {
                return reconstructPath(cameFrom, current);
            }

            // Explore neighbors
            for (GameState neighbor : current.getNextStates()) {
                if (visited.contains(neighbor))
                    continue;

                // Add neighbor only if state is not visited to avoid duplicates
                // (dont remove this again please)
                if (!visited.contains(neighbor) && !frontierSet.contains(neighbor)) {
                    frontier.add(neighbor);
                    frontierSet.add(neighbor);
                    cameFrom.put(neighbor, current);
                }

            }
        }

        // No solution found
        return Collections.emptyList();
    }

    private static boolean isGoalState(GameState state) {
        return state.getHeuristicValue() == 0;
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
