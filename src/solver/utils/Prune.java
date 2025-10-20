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
    // If the box is already on a target, it’s not frozen
    if (level.getTargets().contains(box))
      return false;

    // Check vertical immobility
    Point up = box.pointAtMove('u');
    Point down = box.pointAtMove('d');
    boolean blockedVertically = (level.getWalls().contains(up) || boxes.contains(up)) &&
        (level.getWalls().contains(down) || boxes.contains(down));

    // Check horizontal immobility
    Point left = box.pointAtMove('l');
    Point right = box.pointAtMove('r');
    boolean blockedHorizontally = (level.getWalls().contains(left) || boxes.contains(left)) &&
        (level.getWalls().contains(right) || boxes.contains(right));

    // If both vertical and horizontal blocked, box is frozen
    return blockedVertically && blockedHorizontally;
  }

  public static boolean isSquareDeadlock(Point box, HashSet<Point> boxes, HashSet<Point> targets,
      HashSet<Point> walls) {
    if (targets.contains(box))
      return false;
    Point right = box.pointAtMove('r');
    Point down = box.pointAtMove('d');
    Point diagonal = box.pointAtMove('d').pointAtMove('r');
    return boxes.contains(right) && boxes.contains(down) && boxes.contains(diagonal);
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

  public static boolean isTunnelDeadlock(Point box, HashSet<Point> boxes, HashSet<Point> walls,
      HashSet<Point> targets) {
    if (targets.contains(box))
      return false;

    boolean verticalTunnel = walls.contains(box.pointAtMove('l')) && walls.contains(box.pointAtMove('r'));
    boolean horizontalTunnel = walls.contains(box.pointAtMove('d')) && walls.contains(box.pointAtMove('u'));

    if (verticalTunnel) {
      Point up = box.pointAtMove('u');
      Point down = box.pointAtMove('d');
      if ((walls.contains(up) || boxes.contains(up)) && (walls.contains(down) || boxes.contains(down))) {
        return true;
      }
    }

    if (horizontalTunnel) {
      Point left = box.pointAtMove('l');
      Point right = box.pointAtMove('r');
      if ((walls.contains(left) || boxes.contains(left)) && (walls.contains(right) || boxes.contains(right))) {
        return true;
      }
    }

    return false;
  }

  public static boolean isStateDeadlock(Point box, HashSet<Point> targets, HashSet<Point> walls, HashSet<Point> boxes) {
    return isCornerDeadlock(box, targets, walls) || isTunnelDeadlock(box, boxes, walls, targets)
        || isSquareDeadlock(box, boxes, targets, walls);
  }
}
