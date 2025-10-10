package solver.heuristics;

import java.util.HashSet;

import solver.main.Point;

public class Heuristics {

    private int getAbsoulteValue(int number) {

        if (number < 0) {
            return number * -1;
        } else
            return number;

    }

    public int getManhattan(Point position, Point target) {
        int differenceInX = getAbsoulteValue(position.getX() - target.getX());
        int differenceInY = getAbsoulteValue(position.getY() - target.getY());

        return differenceInX + differenceInY;
    }

    public int minimumManhattan(Point position, HashSet<Point> targets) {
        int minDistance = 0;
        for (Point target : targets) {
            int distance = getManhattan(position, target);

            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    public int totalManhattan(HashSet<Point> boxes, HashSet<Point> targets) {
        int total = 0;

        for (Point box : boxes) {
            total += minimumManhattan(box, targets);
        }

        return total;
    }

}
