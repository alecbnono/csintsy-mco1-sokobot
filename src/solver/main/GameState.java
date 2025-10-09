package solver.main;

import java.util.ArrayList;
import java.util.HashSet;

public class GameState {

    public static final int MAX_MOVES = 4;
    private final int cachedHashCode;

    private Point playerPosition;
    private HashSet<Point> boxPosition;
    private LevelData level;
    private char move;

    public GameState(Point playerPosition, HashSet<Point> boxPosition, LevelData level, char move) {
        this.playerPosition = playerPosition;
        this.boxPosition = new HashSet<>(boxPosition);
        this.level = level;
        this.move = move;
        this.cachedHashCode = computeHashCode();
    }

    public GameState(Point playerPosition, HashSet<Point> boxPosition, LevelData level) {
        this(playerPosition, boxPosition, level, ' ');
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    public HashSet<Point> getBoxPosition() {
        return boxPosition;
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
        Point nextNextPoint = nextPoint.pointAtMove(direction);

        if (level.getWalls().contains(nextPoint))
            return false;

        if (boxPosition.contains(nextPoint)) {
            if (level.getWalls().contains(nextNextPoint) || boxPosition.contains(nextNextPoint))
                return false;
        }

        return true;
    }

    public GameState generateState(char direction) {
        Point nextPlayerPoint = playerPosition.pointAtMove(direction);
        if (!isValidAction(direction))
            throw new IllegalArgumentException("Invalid move: " + direction);

        HashSet<Point> newBoxPosition = boxPosition;
        if (boxPosition.contains(nextPlayerPoint)) {
            newBoxPosition = new HashSet<>(boxPosition);
            newBoxPosition.remove(nextPlayerPoint);
            newBoxPosition.add(nextPlayerPoint.pointAtMove(direction));
        }

        return new GameState(nextPlayerPoint, newBoxPosition, level, direction);
    }

    public ArrayList<GameState> getNextStates() {
        char[] moves = { 'u', 'd', 'l', 'r' };
        ArrayList<GameState> nextGameStates = new ArrayList<>(4);

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
