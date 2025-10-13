package solver.searchAlgorithms;

import java.util.*;
import solver.main.GameState;

public class GreedyBestFirstSearch {

    public static List<GameState> GBFS(GameState startState) {

        PriorityQueue<GameState> frontier = new PriorityQueue<>(Comparator.comparingInt(GameState::getHeuristicValue));
        Set<GameState> visited = new HashSet<>();
        Set<GameState> frontierSet = new HashSet<>();
        Map<GameState, GameState> cameFrom = new HashMap<>();

        frontier.add(startState);
        frontierSet.add(startState);

        while (!frontier.isEmpty()) {
            GameState current = frontier.poll();
            frontierSet.remove(current);

            if (current.getHeuristicValue() == 0)
                return reconstructPath(cameFrom, current);

            visited.add(current);

            for (GameState neighbor : current.getNextStates()) {
                if (visited.contains(neighbor) || frontierSet.contains(neighbor))
                    continue;

                frontier.add(neighbor);
                frontierSet.add(neighbor);
                cameFrom.put(neighbor, current);
            }
        }

        return Collections.emptyList();
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
