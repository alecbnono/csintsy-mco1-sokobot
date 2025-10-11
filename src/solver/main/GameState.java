package solver.main;

import java.util.ArrayList;
import java.util.HashSet;
import solver.utils.Heuristics;
import solver.utils.Prune;

public class GameState {

    public static final int MAX_MOVES = 4;
    private final int cachedHashCode;

    private LevelData level;

    private Point playerPosition;
    private HashSet<Point> boxPosition;
    private int heuristicValue;
    private char move;

    // Constructor for next states (with move)
    public GameState(Point playerPosition,
            HashSet<Point> boxPosition,
            LevelData level,
            char move) {
        this.playerPosition = playerPosition;
        this.boxPosition = new HashSet<>(boxPosition);
        this.level = level;
        this.move = move;
        this.heuristicValue = Heuristics.totalManhattan(boxPosition, level.getTargets());
        this.cachedHashCode = computeHashCode();
    }

    // Constructor for origin (no move)
    public GameState(Point playerPosition, HashSet<Point> boxPosition, LevelData level) {
        this(playerPosition, boxPosition, level, ' ');
    }

    // Consturctor for incrmeental heuristic update
    public GameState(Point playerPosition, HashSet<Point> boxPosition, LevelData level, char move, int heuristic) {
        this.playerPosition = playerPosition;
        this.boxPosition = new HashSet<>(boxPosition);
        this.level = level;
        this.move = move;
        this.heuristicValue = heuristic;
        this.cachedHashCode = computeHashCode();
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    public HashSet<Point> getBoxPosition() {
        return boxPosition;
    }

    public int getHeuristicValue() {
        return heuristicValue;
    }

    private int computeHashCode() {
        int hash = playerPosition.hashCode();
        for (Point p : boxPosition) {
            hash = hash * 31 + p.hashCode();
        }
        return hash;
    }

    public boolean isValidAction(char direction) {
        Point nextPoint = playerPosition.pointAtMove(direction);

        // Can't move into walls
        if (level.getWalls().contains(nextPoint))
            return false;

        if (boxPosition.contains(nextPoint)) {
            Point nextNextPoint = nextPoint.pointAtMove(direction);
            // Can't push box into wall or another box
            if (level.getWalls().contains(nextNextPoint) || boxPosition.contains(nextNextPoint))
                return false;

            // Only check for deadlocks if a box is being pushed
            if (Prune.isCornerDeadlock(nextNextPoint, level.getTargets(), level.getWalls()))
                return false;
        }

        return true;
    }

    public GameState generateState(char direction) {
        if (!isValidAction(direction))
            throw new IllegalArgumentException("Invalid move: " + direction);

        Point nextPlayerPoint = playerPosition.pointAtMove(direction);
        HashSet<Point> newBoxPosition = boxPosition;
        int newHeuristic = heuristicValue;

        if (boxPosition.contains(nextPlayerPoint)) {
            Point movedBoxFrom = nextPlayerPoint;
            Point movedBoxTo = nextPlayerPoint.pointAtMove(direction);

            newBoxPosition = new HashSet<>(boxPosition);
            newBoxPosition.remove(movedBoxFrom);
            newBoxPosition.add(movedBoxTo);

            // Updatese heuristics incrementally when box is moved
            int oldDistance = Heuristics.minimumManhattan(movedBoxFrom, level.getTargets());
            int newDistance = Heuristics.minimumManhattan(movedBoxTo, level.getTargets());
            newHeuristic = heuristicValue - oldDistance + newDistance;
        }

        return new GameState(nextPlayerPoint, newBoxPosition, level, direction, newHeuristic);
    }

    public ArrayList<GameState> getNextStates() {
        ArrayList<GameState> nextGameStates = new ArrayList<>(4);
        for (char move : new char[] { 'u', 'd', 'l', 'r' }) {
            if (isValidAction(move)) {
                nextGameStates.add(generateState(move));
            }
        }
        return nextGameStates;
    }

    public char getMoveMadeFrom(GameState previous) {
        return this.move;
    }

    @Override
    public int hashCode() {
        return cachedHashCode;
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
