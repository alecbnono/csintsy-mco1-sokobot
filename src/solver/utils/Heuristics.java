package solver.utils;

import java.util.HashSet;
import solver.main.GameState;
import solver.main.Point;

public class Heuristics {

    public static int getManhattan(Point a, Point b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    public static int minimumManhattan(Point box, HashSet<Point> targets) {
        int min = Integer.MAX_VALUE;
        for (Point t : targets) {
            int d = getManhattan(box, t);
            if (d < min)
                min = d;
        }
        return min;
    }

    public static int totalManhattan(HashSet<Point> boxes, HashSet<Point> targets) {
        int total = 0;
        for (Point b : boxes)
            total += minimumManhattan(b, targets);
        return total;
    }

    // Greedy matching heuristic — faster and cleaner
    public static int getStateHeuristic(GameState state, HashSet<Point> targets) {
        Point[] boxArray = state.getBoxPosition().toArray(Point[]::new);
        Point[] targetArray = targets.toArray(Point[]::new);
        boolean[] used = new boolean[targetArray.length];
        int heuristic = 0;

        for (Point box : boxArray) {
            int minDist = Integer.MAX_VALUE;
            int bestIdx = -1;

            for (int i = 0; i < targetArray.length; i++) {
                if (used[i])
                    continue;
                int d = getManhattan(box, targetArray[i]);
                if (d < minDist) {
                    minDist = d;
                    bestIdx = i;
                }
            }

            if (bestIdx >= 0) {
                used[bestIdx] = true;
                heuristic += minDist;
            }
        }
        return heuristic;
    }
}
