package solver.main;

import java.util.ArrayList;
import java.util.HashSet;

public class GameState {

    public static final int MAX_MOVES = 4;

    /*
     * States Needed for the Game State:
     * 1. Player Position using Point probably use a class for points
     * 2. Box Position use a Hashset for uniqueness
     * 3. Goal Position as well with Hashset uniqueness
     * 4. Char move. ex: up = u, down = d, left = l, right = r
     */

    private Point playerPosition;
    private HashSet<Point> boxPosition;
    private LevelData level;

    public GameState(Point playerPosition, HashSet<Point> boxPosition, LevelData level) {
        this.playerPosition = playerPosition;
        this.boxPosition = new HashSet<>(boxPosition);
        this.level = level; // this reference gives access to the entire level
    }

    public Point getPlayer() {
        return playerPosition;
    }

    public HashSet<Point> getBoxPosition() {
        return new HashSet<>(boxPosition);
    }

    // Check validity of action
    //
    // 1. Do I move into a wall? POP IF YES
    // 2. Do I push a box? -> Is the box infront of a wall or another box? POP IF
    // YES
    // If NO to both questions -> VALID ACTION
    public boolean isValidAction(char direction) {

        // sets relevant points to the direction from param
        Point nextPoint = playerPosition.pointAtMove(direction);
        Point nextNextPoint = nextPoint.pointAtMove(direction);

        // check if adjacent point is wall
        if (level.getWalls().contains(nextPoint)) {
            return false;
        }
        // checks if next move pushes a box
        else if (boxPosition.contains(nextPoint)) {

            // checks if next next box is an obstacle (wall or box)
            if (level.getWalls().contains(nextNextPoint) || boxPosition.contains(nextNextPoint)) {
                return false;
            }
            return true;

        }
        // otherwise, its a valid move
        else {
            return true;
        }

    }

    public GameState generateState(char direction) {

        // replace player position
        Point nextPlayerPoint = playerPosition.pointAtMove(direction);

        // check if valid action
        if (isValidAction(direction)) {

            // if state will push box
            if (boxPosition.contains(nextPlayerPoint)) {

                // replace with new box position

                HashSet<Point> newBoxPosition = new HashSet<Point>(boxPosition); // clones box positions

                newBoxPosition.remove(nextPlayerPoint); // remove relevant box
                newBoxPosition.add(nextPlayerPoint.pointAtMove(direction)); // adds moved box

                return new GameState(nextPlayerPoint, newBoxPosition, level);
            }
            // no box will be pushed
            else {
                return new GameState(nextPlayerPoint, boxPosition, level);
            }
        } else {
            throw new IllegalArgumentException("(State cannot be generated) Invalid direction : " + direction);
        }

    }

    // TODO: Generate children states
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
        return "Player=" + playerPosition + ", Boxes =" + boxPosition;
    }

}
