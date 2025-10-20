package solver.utils;

import java.util.PriorityQueue;
import java.util.Comparator;

import java.util.HashSet;
import solver.main.GameState;
import solver.main.LevelData;
import solver.main.Point;
import solver.utils.Prune;

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

  public static int totalManhattan(LevelData level, HashSet<Point> boxes, HashSet<Point> targets) {
    int total = 0;

    HashSet<Point> deadlockFloors = level.getDeadlocks();

    for (Point b : boxes) {
      if (Prune.isStateDeadlock(b, targets, level.getWalls(), boxes) || deadlockFloors.contains(b)
          || Prune.isFrozen(level, boxes, b)) {
        return Integer.MAX_VALUE;
      }
      total += minimumManhattan(b, targets);
    }
    return total;
  }

  public static int[][] precomputeManhattan(HashSet<Point> boxes, HashSet<Point> targets) {
    Point[] boxArray = boxes.toArray(Point[]::new);
    Point[] targetArray = targets.toArray(Point[]::new);

    int[][] manhattan = new int[boxArray.length][targetArray.length];
    for (int i = 0; i < boxArray.length; i++) {
      for (int j = 0; j < targetArray.length; j++) {
        manhattan[i][j] = getManhattan(boxArray[i], targetArray[j]);
      }
    }
    return manhattan;
  }

  public static int divideAndConquerHeuristic(HashSet<Point> boxes, HashSet<Point> targets, int groupSize) {
    Point[] boxArray = boxes.toArray(Point[]::new);
    Point[] targetArray = targets.toArray(Point[]::new);

    int[][] manhattan = precomputeManhattan(boxes, targets);
    boolean[] targetUsed = new boolean[targetArray.length];
    int heuristic = 0;

    for (int i = 0; i < boxArray.length; i++) {
      int minDist = Integer.MAX_VALUE;
      int bestIdx = -1;

      for (int j = 0; j < targetArray.length; j++) {
        if (targetUsed[j])
          continue;

        int manhattanDistance = manhattan[i][j];

        if (manhattanDistance == Integer.MAX_VALUE)
          continue;

        if (manhattanDistance < minDist) {
          minDist = manhattanDistance;
          bestIdx = j;
        }
      }

      if (bestIdx >= 0) {
        targetUsed[bestIdx] = true;
        heuristic += minDist;
      } else {
        return Integer.MAX_VALUE;
      }
    }

    return heuristic;
  }

}
