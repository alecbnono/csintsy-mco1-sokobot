package solver.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import solver.main.LevelData;
import solver.main.Point;

public class Prune {

    public static boolean isCornerDeadlock(Point position, HashSet<Point> targets, HashSet<Point> walls) {

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

    public static ArrayList<ArrayList<Point>> getHorizontalWalls(LevelData level) {

        // check for horizontal line segments
        ArrayList<ArrayList<Point>> horizontalSegments = new ArrayList<>();

        ArrayList<Point> currentWall = new ArrayList<Point>();
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                Point current = new Point(x, y);

                // if wall count till no walls
                if (level.getWalls().contains(current)) {
                    currentWall.add(current);
                } else if (!level.getWalls().contains(current) && !currentWall.isEmpty()) {
                    // add if you came from a wall segment
                    // and wipe current wall
                    horizontalSegments.add(currentWall);
                    currentWall = new ArrayList<Point>();
                }

            }
            if (!currentWall.isEmpty()) {
                horizontalSegments.add(currentWall);
                currentWall = new ArrayList<Point>();
            }
        }

        return horizontalSegments;
    }

    public static ArrayList<ArrayList<Point>> getVerticalWalls(LevelData level) {

        // check for horizontal line segments
        ArrayList<ArrayList<Point>> verticalSegments = new ArrayList<>();

        ArrayList<Point> currentWall = new ArrayList<Point>();
        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                Point current = new Point(x, y);

                // if wall count till no walls
                if (level.getWalls().contains(current)) {
                    currentWall.add(current);
                } else if (!level.getWalls().contains(current) && !currentWall.isEmpty()) {
                    // add if you came from a wall segment
                    // and wipe current wall
                    verticalSegments.add(currentWall);
                    currentWall = new ArrayList<Point>();
                }

            }
            if (!currentWall.isEmpty()) {
                verticalSegments.add(currentWall);
                currentWall = new ArrayList<Point>();
            }
        }

        return verticalSegments;
    }

    public static HashSet<Point> getWallDeadlocks(LevelData level) {
        ArrayList<ArrayList<Point>> horizontalWalls = getHorizontalWalls(level);
        ArrayList<ArrayList<Point>> verticalWalls = getVerticalWalls(level);
        HashSet<Point> deadlocks = new HashSet<Point>();

        // vertical walls
        for (ArrayList<Point> segment : verticalWalls) {

            boolean hasGoal = false;
            ArrayList<Point> floorsRight = new ArrayList<Point>();
            ArrayList<Point> floorsLeft = new ArrayList<Point>();

            // check for left
            for (Point wall : segment) {
                Point left = new Point(wall.getX() - 1, wall.getY());

                // check if it can be flagged as deadlock (is a floor)
                if (level.getFloors().contains(left))
                    floorsLeft.add(left);

                if (level.getTargets().contains(left))
                    hasGoal = true;
            }

            if (!hasGoal)
                deadlocks.addAll(floorsLeft);

            for (Point wall : segment) {
                Point right = new Point(wall.getX() + 1, wall.getY());

                // check if it can be flagged as deadlock (is a floor)
                if (level.getFloors().contains(right))
                    floorsRight.add(right);

                if (level.getTargets().contains(right))
                    hasGoal = true;
            }

            if (!hasGoal)
                deadlocks.addAll(floorsRight);

        }

        // horizontal walls
        for (ArrayList<Point> segment : horizontalWalls) {

            boolean hasGoal = false;
            ArrayList<Point> floorsBelow = new ArrayList<Point>();
            ArrayList<Point> floorsAbove = new ArrayList<Point>();

            // check for above
            for (Point wall : segment) {
                Point above = new Point(wall.getX(), wall.getY() + 1);

                // check if it can be flagged as deadlock (is a floor)
                if (level.getFloors().contains(above))
                    floorsAbove.add(above);

                if (level.getTargets().contains(above))
                    hasGoal = true;
            }

            if (!hasGoal)
                deadlocks.addAll(floorsAbove);

            for (Point wall : segment) {
                Point below = new Point(wall.getX(), wall.getY() - 1);

                // check if it can be flagged as deadlock (is a floor)
                if (level.getFloors().contains(below))
                    floorsBelow.add(below);

                if (level.getTargets().contains(below))
                    hasGoal = true;
            }

            if (!hasGoal)
                deadlocks.addAll(floorsBelow);

        }

        return deadlocks;

    }

    // consider moves that lead to deadlocks as an invalid move
    public static boolean isStateDeadlock(Point box, HashSet<Point> targets, HashSet<Point> walls) {

        // corner deadlock
        // for (Point box : boxes) {
        // if (isCornerDeadlock(box, targets, walls)) {
        // return true;
        // }
        // }

        return isCornerDeadlock(box, targets, walls);
    }

}
