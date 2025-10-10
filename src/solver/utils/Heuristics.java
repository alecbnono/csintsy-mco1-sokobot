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
        HashSet<Point> boxesSet = state.getBoxPosition();
        Point[] boxes = boxesSet.toArray(new Point[0]);
        Point[] targetArray = targets.toArray(new Point[0]);
        boolean[] targetUsed = new boolean[targetArray.length];
        int heuristic = 0;

        for (Point box : boxes) {
            int minDist = Integer.MAX_VALUE;
            int minIndex = -1;

            for (int i = 0; i < targetArray.length; i++) {
                if (!targetUsed[i]) {
                    int dist = getManhattan(box, targetArray[i]);
                    if (dist < minDist) {
                        minDist = dist;
                        minIndex = i;
                    }
                }
            }

            if (minIndex != -1) {
                heuristic += minDist;
                targetUsed[minIndex] = true;
            }
        }

        return heuristic;

        // Total Manhattan Heuristics
        // return totalManhattan(boxesSet, targets);
    }

}
