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

    // deadlocks can be determined by checking if box is in a corner
    public boolean isDeadlock(Point position, HashSet<Point> targets, HashSet<Point> walls) {

        // ignore deadlock if in target already
        if (targets.contains(position)) {
            return false;
        }

        // checks if box is located in a corner
        boolean topLeft = walls.contains(position.pointAtMove('u')) && walls.contains(position.pointAtMove('l'));
        boolean topRight = walls.contains(position.pointAtMove('u')) && walls.contains(position.pointAtMove('r'));
        boolean bottomLeft = walls.contains(position.pointAtMove('d')) && walls.contains(position.pointAtMove('l'));
        boolean bottomRight = walls.contains(position.pointAtMove('d')) && walls.contains(position.pointAtMove('r'));

        if (topLeft || topRight || bottomLeft || bottomRight) {
            return true;
        }

        return false;

    }

    public boolean isStateDeadlock(HashSet<Point> boxes, HashSet<Point> targets, HashSet<Point> walls) {
        for (Point box : boxes) {
            if (isDeadlock(box, targets, walls)) {
                return true;
            }
        }

        return false;
    }

    public int getStateHeuristic(HashSet<Point> boxes, HashSet<Point> targets, HashSet<Point> walls) {
        int distanceHeuristic = totalManhattan(boxes, targets);
        boolean deadlockExists = isStateDeadlock(boxes, targets, walls);

        if (deadlockExists) {
            return 999999999;
        }

        return distanceHeuristic;

    }

}
