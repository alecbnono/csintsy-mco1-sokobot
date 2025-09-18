package solver;

public class GameState {
    /*
     * States Needed for the Game State:
     * 1. Player Position using Point probably use a class for points
     * 2. Box Position use a Hashset for uniqueness
     * 3. Goal Position as well with Hashset uniqueness
     * 4. Char move. ex: up = u, down = d, left = l, right = r
     */

    private Point player;
    private Point[] boxLocations;

    public GameState(int boxCount) {
        this.boxLocations = new Point[boxCount];
    }

    public String validMoves() {

        return "";
    }

}
