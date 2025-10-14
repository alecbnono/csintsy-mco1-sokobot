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
    return (min) * min;
  }

  public static int totalManhattan(HashSet<Point> boxes, HashSet<Point> targets) {
    int total = 0;
    for (Point b : boxes)
      total += minimumManhattan(b, targets);
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

  public static int divideAndConquerHeuristic(HashSet<Point> boxes, HashSet<Point> targets, int groupSize) {
    Point[] boxArray = boxes.toArray(Point[]::new);
    Point[] targetArray = targets.toArray(Point[]::new);

    // Precompute Manhattan distances
    int[][] manhattan = precomputeManhattan(boxes, targets);

    boolean[] usedGlobal = new boolean[targetArray.length];
    int heuristic = 0;

    for (int start = 0; start < boxArray.length; start += groupSize) {
      int end = Math.min(start + groupSize, boxArray.length);
      boolean[] usedLocal = new boolean[targetArray.length];

      for (int i = start; i < end; i++) {
        int minDist = Integer.MAX_VALUE;
        int bestIdx = -1;

        for (int j = 0; j < targetArray.length; j++) {
          if (usedGlobal[j] || usedLocal[j])
            continue;
          int d = manhattan[i][j]; // use precomputed distance
          if (d < minDist) {
            minDist = d;
            bestIdx = j;
          }
        }

        if (bestIdx >= 0) {
          usedLocal[bestIdx] = true;
          usedGlobal[bestIdx] = true;
          heuristic += minDist;
        }
      }
    }

    return heuristic;
  }
}
