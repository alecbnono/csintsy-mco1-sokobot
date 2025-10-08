package solver;

import java.util.HashSet;

public class LevelData {

    // Converted data to be more memory effecient
    private HashSet<Point> walls = new HashSet<>();
    private HashSet<Point> targets = new HashSet<>(); // use this to check for goal state

    private GameState origin;

    public LevelData(int width, int height, char[][] mapData, char[][] itemsData) {

        // temporary storage for box coords
        HashSet<Point> tempBoxPoints = new HashSet<Point>();
        Point tempPlayer = null;

        // searches through map for constants; converts to Points
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // adds wall point to hashset
                if (mapData[i][j] == '#') {
                    walls.add(new Point(j, i));
                }
                // adds target point to hashset
                if (mapData[i][j] == '.') {
                    targets.add(new Point(j, i));
                }

                // Sets coords if a box has been found; adds to count
                if (mapData[i][j] == '$') {
                    tempBoxPoints.add(new Point(j, i));
                }

                // Sets coords for player origin if found
                if (mapData[i][j] == '@') {
                    tempPlayer = new Point(j, i);
                }
            }
        }

        // Initialize origin state
        origin = new GameState(tempPlayer, tempBoxPoints, this);
    }

    public HashSet<Point> getWalls() {
        return walls;
    }

    public HashSet<Point> getTargets() {
        return targets;
    }

}
