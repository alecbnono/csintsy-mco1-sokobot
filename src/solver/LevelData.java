package solver;

import java.util.ArrayList;

public class LevelData {

    // Raw data
    private char[][] mapData;
    private int width;
    private int height;

    // Converted data to be more memory effecient
    private int boxCount;
    private Point playerOrigin;
    private Point[] boxesOrigin;

    public LevelData(int width, int height, char[][] mapData) {

        this.mapData = mapData;
        this.width = width;
        this.height = height;

        this.boxCount = 0;

        // temporary storage for box coords
        ArrayList<Point> tempBoxPoints = new ArrayList<Point>();

        // searches through map for relative states; converts to Points
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Sets coords if a box has been found; adds to count
                if (this.mapData[height][width] == '$') {
                    this.boxCount++;
                    tempBoxPoints.add(new Point(width, height));
                }
                // Sets coords for player origin if found
                else if (this.mapData[height][width] == '@') {
                    playerOrigin = new Point(width, height);
                }
            }
        }

        // Copies the ArrayList into an Array
        boxesOrigin = tempBoxPoints.toArray(new Point[boxCount]);

        // This is needed because Arrays have less
        // memory overhead compared to ArrayLists
    }
}
