package solver.utils;

import java.util.HashSet;
import solver.main.GameState;
import solver.main.Point;

public class Heuristics {

    private static int getAbsoulteValue(int number) {
        return number < 0 ? -number : number;
    }

    public static int getManhattan(Point position, Point target) {
        int differenceInX = getAbsoulteValue(position.getX() - target.getX());
        int differenceInY = getAbsoulteValue(position.getY() - target.getY());
        return differenceInX + differenceInY;
    }

    public static int minimumManhattan(Point position, HashSet<Point> targets) {
        int minDistance = Integer.MAX_VALUE;
        for (Point target : targets) {
            int distance = getManhattan(position, target);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }

    public static int totalManhattan(HashSet<Point> boxes, HashSet<Point> targets) {
        int total = 0;
        for (Point box : boxes) {
            total += minimumManhattan(box, targets);
        }
        return total;
    }

    public static int getStateHeuristic(GameState state, HashSet<Point> targets) {
        // Greedy assignment heuristic
        HashSet<Point> boxes = state.getBoxPosition();
        HashSet<Point> remainingTargets = new HashSet<>(targets);
        int heuristic = 0;

        for (Point box : boxes) {
            Point closestTarget = null;
            int minDistance = Integer.MAX_VALUE;

            for (Point target : remainingTargets) {
                int distance = getManhattan(box, target);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestTarget = target;
                }
            }

            if (closestTarget != null) {
                heuristic += minDistance;
                remainingTargets.remove(closestTarget);
            }

        }

        return heuristic;

        // Total Manhattan Heuristics
        // return totalManhattan(boxes, targets);
    }
}
