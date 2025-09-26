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
    private char validMove;

    public GameState(Point playerPosition, HashSet<Point> boxPosition, HashSet<Point> goalPosition, char validMove) {
        this.playerPosition = playerPosition;
        this.boxPosition = boxPosition;
        this.goalPosition = goalPosition;
        this.validMove = validMove;
    }

    public Point getPlayer() {
        return player;
    }

    public HashSet<Point> getBoxLocations() {
        return boxLocations;
    }

    public boolean isValidAction(char direction) {

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

}
