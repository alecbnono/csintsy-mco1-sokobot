package solver.utils;

import java.util.HashSet;
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

    // consider moves that lead to deadlocks as an invalid move
    public static boolean isStateDeadlock(HashSet<Point> boxes, HashSet<Point> targets, HashSet<Point> walls) {

        // corner deadlock
        for (Point box : boxes) {
            if (isCornerDeadlock(box, targets, walls)) {
                return true;
            }
        }

        return false;
    }

}
