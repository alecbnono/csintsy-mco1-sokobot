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

    public String validMoves() {

        return "";
    }

}
