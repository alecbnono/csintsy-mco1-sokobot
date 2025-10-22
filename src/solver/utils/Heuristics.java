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

  public static int hungarian(int[][] costMatrix) {
    int n = costMatrix.length;
    int m = costMatrix[0].length;
    int dim = Math.max(n, m);

    int[][] cost = new int[dim][dim];
    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        cost[i][j] = (i < n && j < m) ? costMatrix[i][j] : 0;
      }
    }

    int[] u = new int[dim + 1];
    int[] v = new int[dim + 1];
    int[] p = new int[dim + 1];
    int[] way = new int[dim + 1];

    for (int i = 1; i <= dim; i++) {
      p[0] = i;
      int j0 = 0;
      int[] minv = new int[dim + 1];
      boolean[] used = new boolean[dim + 1];
      for (int j = 1; j <= dim; j++) {
        minv[j] = Integer.MAX_VALUE;
        way[j] = 0;
      }

      do {
        used[j0] = true;
        int i0 = p[j0];
        int delta = Integer.MAX_VALUE;
        int j1 = 0;
        for (int j = 1; j <= dim; j++) {
          if (used[j])
            continue;
          int cur = cost[i0 - 1][j - 1] - u[i0] - v[j];
          if (cur < minv[j]) {
            minv[j] = cur;
            way[j] = j0;
          }
          if (minv[j] < delta) {
            delta = minv[j];
            j1 = j;
          }
        }
        for (int j = 0; j <= dim; j++) {
          if (used[j]) {
            u[p[j]] += delta;
            v[j] -= delta;
          } else {
            minv[j] -= delta;
          }
        }
        j0 = j1;
      } while (p[j0] != 0);

      do {
        int j1 = way[j0];
        p[j0] = p[j1];
        j0 = j1;
      } while (j0 != 0);
    }

    return -v[0];
  }

  public static int hungarianHeuristic(LevelData level, HashSet<Point> boxes, HashSet<Point> targets) {
    // Check for deadlocks and frozen states first
    HashSet<Point> deadlockFloors = level.getDeadlocks();

    for (Point b : boxes) {
      if (Prune.isStateDeadlock(b, targets, level.getWalls(), boxes) ||
          deadlockFloors.contains(b) ||
          Prune.isFrozen(level, boxes, b)) {
        return Integer.MAX_VALUE;
      }
    }

    int[][] manhattan = precomputeManhattan(boxes, targets);
    return hungarian(manhattan);
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
