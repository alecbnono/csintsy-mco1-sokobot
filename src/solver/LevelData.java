package solver;

import java.util.HashSet;

public class LevelData {

    // Converted data to be more memory effecient
    private HashSet<Point> walls;
    private HashSet<Point> targets; // use this to check for goal state

    private GameState origin;

    public LevelData(int width, int height, char[][] mapData, char[][] itemsData) {

        // searches through map for constants; converts to Points
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // adds wall point to hashset
                if (mapData[height][width] == '#') {
                    walls.add(new Point(width, height));
                }
                // adds target point to hashset
                else if (mapData[height][width] == '.') {
                    targets.add(new Point(width, height));
                }
            }
        }

        // temporary storage for box coords
        HashSet<Point> tempBoxPoints = new HashSet<Point>();
        Point tempPlayer = null;

        // searches through map for relevant states; converts to Points
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Sets coords if a box has been found; adds to count
                if (mapData[height][width] == '$') {
                    tempBoxPoints.add(new Point(width, height));
                }
                // Sets coords for player origin if found
                else if (mapData[height][width] == '@') {
                    tempPlayer = new Point(width, height);
                }
            }
        }

        // Initialize origin state
        origin = new GameState(tempPlayer, tempBoxPoints, this);
    }

    public HashSet getWalls() {
        return walls;
    }

    public HashSet getTargets() {
        return targets;
    }

}
