package solver.main;

import java.util.ArrayList;
import java.util.HashSet;

import solver.heuristics.Heuristics;

public class GameState {

    public static final int MAX_MOVES = 4;

    private Point playerPosition;
    private HashSet<Point> boxPosition;
    private LevelData level;
    private Heuristics heuristics;
    private int heuristicValue;
    private char move;

    // Constructor for next states (with move)
    public GameState(Point playerPosition,
            HashSet<Point> boxPosition,
            LevelData level,
            Heuristics heuristics,
            char move) {
        this.playerPosition = playerPosition;
        this.boxPosition = new HashSet<>(boxPosition);
        this.level = level;
        this.move = move;
        this.heuristics = heuristics;
        this.heuristicValue = heuristics.totalManhattan(boxPosition, level.getTargets());
    }

    // Constructor for origin (no move)
    public GameState(Point playerPosition, HashSet<Point> boxPosition, Heuristics heuristics, LevelData level) {
        this(playerPosition, boxPosition, level, heuristics, ' ');
    }

    public Point getPlayer() {
        return playerPosition;
    }

    public HashSet<Point> getBoxPosition() {
        return new HashSet<>(boxPosition);
    }

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

    // consider moves that lead to deadlocks as an invalid move
    public boolean isStateDeadlock(HashSet<Point> boxes, HashSet<Point> targets, HashSet<Point> walls) {
        for (Point box : boxes) {
            if (isDeadlock(box, targets, walls)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidAction(char direction) {
        Point nextPoint = playerPosition.pointAtMove(direction);
        Point nextNextPoint = nextPoint.pointAtMove(direction);

        if (isStateDeadlock(boxPosition, level.getTargets(), level.getWalls())) {
            return false;
        }

        if (level.getWalls().contains(nextPoint))
            return false;

        if (boxPosition.contains(nextPoint)) {
            if (level.getWalls().contains(nextNextPoint) || boxPosition.contains(nextNextPoint)) {
                return false;
            }
        }

        return true;
    }

    public GameState generateState(char direction) {
        Point nextPlayerPoint = playerPosition.pointAtMove(direction);

        if (!isValidAction(direction))
            throw new IllegalArgumentException("(State cannot be generated) Invalid direction: " + direction);

        if (boxPosition.contains(nextPlayerPoint)) {
            HashSet<Point> newBoxPosition = new HashSet<>(boxPosition);
            newBoxPosition.remove(nextPlayerPoint);
            newBoxPosition.add(nextPlayerPoint.pointAtMove(direction));
            return new GameState(nextPlayerPoint, newBoxPosition, level, , direction);
        } else {
            return new GameState(nextPlayerPoint, boxPosition, level, direction);
        }
    }

    public ArrayList<GameState> getNextStates() {
        char[] moves = { 'u', 'd', 'l', 'r' };
        ArrayList<GameState> nextGameStates = new ArrayList<>();

        for (char move : moves) {
            if (isValidAction(move)) {
                nextGameStates.add(generateState(move));
            }
        }
        return nextGameStates;
    }

    public char getMoveMadeFrom(GameState previous) {
        return this.move;
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(playerPosition, boxPosition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof GameState))
            return false;

        GameState other = (GameState) obj;
        return playerPosition.equals(other.playerPosition) && boxPosition.equals(other.boxPosition);
    }

    @Override
    public String toString() {
        return "Player=" + playerPosition + ", Boxes=" + boxPosition + ", Move=" + move;
    }
}
