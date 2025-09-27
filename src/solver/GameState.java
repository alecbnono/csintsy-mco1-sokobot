package solver;

import java.util.HashSet;

public class GameState {
    /*
     * States Needed for the Game State:
     * 1. Player Position using Point probably use a class for points
     * 2. Box Position use a Hashset for uniqueness
     * 3. Goal Position as well with Hashset uniqueness
     * 4. Char move. ex: up = u, down = d, left = l, right = r
     */

    private Point playerPosition;
    private HashSet<Point> boxPosition;
    private HashSet<Point> goalPosition;
    private LevelData level;

    public GameState(Point playerPosition, HashSet<Point> boxPosition, HashSet<Point> goalPosition, LevelData level) {
        this.playerPosition = playerPosition;
        this.boxPosition = boxPosition;
        this.goalPosition = goalPosition;
        this.level = level;
    }

    public Point getPlayer() {
        return player;
    }

    public HashSet<Point> getBoxLocations() {
        return boxLocations;
    }

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

        }
        // otherwise, its a valid move
        else {
            return true;
        }

    }

    public String validActions() {

        // Start out with 4 moves (up, down, left, right)
        //
        // Check if already explored based on coords
        // (use hashmap to stored visited states)
        //
        // Check validity of action
        //
        // 1. Do I move into a wall? POP IF YES
        // 2. Do I push a box? -> Is the box infront of a wall or another box? POP IF
        // YES
        // If NO to both questions -> VALID ACTION

    }

    public boolean isGoalState() {

    }

    public char getValidMode() {
        return validMove;
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(playerPosition, boxPosition, goalPosition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof GameState))
            return false;

        GameState other = (GameState) obj;

        return playerPosition.equals(other.playerPosition) && boxPosition.equals(other.boxPosition)
                && goalPosition.equals(other.goalPosition);

    }

}
