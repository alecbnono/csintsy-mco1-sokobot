package solver.utils;

import java.util.*;
import solver.main.LevelData;
import solver.main.Point;

public class Prune {

    public static boolean isCornerDeadlock(Point p, HashSet<Point> targets, HashSet<Point> walls) {
        if (targets.contains(p))
            return false;

        Point up = p.pointAtMove('u');
        Point down = p.pointAtMove('d');
        Point left = p.pointAtMove('l');
        Point right = p.pointAtMove('r');

        return (walls.contains(up) && walls.contains(left)) ||
                (walls.contains(up) && walls.contains(right)) ||
                (walls.contains(down) && walls.contains(left)) ||
                (walls.contains(down) && walls.contains(right));
    }

    public static boolean isFrozen(LevelData level, HashSet<Point> boxes, Point box) {
        for (char dir : new char[] { 'u', 'd', 'l', 'r' }) {
            Point player = box.pointAtMove(dir);
            Point next = box.pointAtMove(Point.opposite(dir));

            // check if blocked by floors
            if (level.getFloors().contains(player) &&
                    level.getFloors().contains(next) &&
                    // check if blocked by boxes
                    !boxes.contains(player) &&
                    !boxes.contains(next)) {
                return false;
            }
        }
        return true;
    }

    public static HashSet<Point> reverseFloodFill(LevelData level) {
        HashSet<Point> reachable = new HashSet<>(level.getTargets());
        ArrayDeque<Point> queue = new ArrayDeque<>(level.getTargets());

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            for (char dir : new char[] { 'u', 'd', 'l', 'r' }) {
                Point from = current.pointAtMove(Point.opposite(dir));
                Point pusher = from.pointAtMove(Point.opposite(dir));
                if (level.getFloors().contains(from) && level.getFloors().contains(pusher)
                        && reachable.add(from)) {
                    queue.add(from);
                }
            }
        }

        HashSet<Point> deadlocks = new HashSet<>();
        for (Point floor : level.getFloors())
            if (!reachable.contains(floor))
                deadlocks.add(floor);

        return deadlocks;
    }

    public static boolean isStateDeadlock(Point box, HashSet<Point> targets, HashSet<Point> walls) {
        return isCornerDeadlock(box, targets, walls);
    }
}
