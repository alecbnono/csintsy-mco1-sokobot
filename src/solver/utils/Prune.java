package solver.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import solver.main.GameState;
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

    public static boolean isStateFrozen(LevelData level, HashSet<Point> boxPositions) {
        for (Point box : boxPositions) {
            // check for all directions
            for (char dir : new char[] { 'u', 'd', 'l', 'r' }) {
                Point player = box.pointAtMove(dir); // box’s previous spot
                Point nextPoint = box.pointAtMove(Point.opposite(dir)); // where player would stand

                // check if statically reversible
                if (level.getFloors().contains(player) &&
                        level.getFloors().contains(nextPoint) &&
                        // check if dynamically reversible not blocked by boxes on
                        !boxPositions.contains(player) &&
                        !boxPositions.contains(nextPoint)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static HashSet<Point> reverseFloodFill(LevelData level) {
        HashSet<Point> reachable = new HashSet<>(level.getTargets());
        Queue<Point> queue = new LinkedList<>(level.getTargets());

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            for (char dir : new char[] { 'u', 'd', 'l', 'r' }) {
                Point from = current.pointAtMove(Point.opposite(dir)); // box’s previous spot
                Point pusher = from.pointAtMove(Point.opposite(dir)); // where player would stand

                if (level.getFloors().contains(from) && level.getFloors().contains(pusher)
                        && !reachable.contains(from)) {
                    reachable.add(from);
                    queue.add(from);
                }
            }
        }

        // Deadlocks = floor cells not reachable by reverse flood-fill
        HashSet<Point> deadlocks = new HashSet<>();
        for (Point floor : level.getFloors()) {
            if (!reachable.contains(floor)) {
                deadlocks.add(floor);
            }
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
