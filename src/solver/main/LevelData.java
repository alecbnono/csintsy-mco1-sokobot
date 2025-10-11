package solver.main;

import java.util.HashSet;

import solver.utils.Prune;

public class LevelData {

    // Converted data to be more memory effecient
    private HashSet<Point> walls = new HashSet<>();
    private HashSet<Point> targets = new HashSet<>(); // use this to check for goal state
    private HashSet<Point> deadlocks = new HashSet<>();
    private HashSet<Point> floors = new HashSet<>();

    private GameState origin;

    public LevelData(int width, int height, char[][] mapData, char[][] itemsData) {

        // temporary storage for box coords
        HashSet<Point> tempBoxPoints = new HashSet<Point>();
        Point tempPlayer = null;

        // searches through map for constants; converts to Points
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                // Walls and targets from mapData
                if (mapData[i][j] == '#') {
                    walls.add(new Point(j, i));
                }
                if (mapData[i][j] == '.') {
                    targets.add(new Point(j, i));
                }
                if (mapData[i][j] == '.' || mapData[i][j] == ' ') {
                    floors.add(new Point(j, i));
                }

                // Boxes and player may appear in itemsData
                char item = itemsData[i][j];
                if (item == '$') {
                    tempBoxPoints.add(new Point(j, i));
                }
                if (item == '@') {
                    tempPlayer = new Point(j, i);
                }
            }
        }

        deadlocks = Prune.reverseFloodFill(this);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mapData[i][j] == ' ') {
                    Point space = new Point(j, i);

                    if (Prune.isStateDeadlock(space, targets, walls))
                        deadlocks.add(space);
                }
            }
        }

        System.out.println("DEBUG: Finish Pre-computing Deadlock");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point p = new Point(x, y);
                if (this.walls.contains(p))
                    System.out.print("#");
                else if (this.targets.contains(p))
                    System.out.print(".");
                else if (this.deadlocks.contains(p))
                    System.out.print("X");
                else
                    System.out.print(" ");
            }
            System.out.println();
        }

        // Initialize origin state
        this.origin = new GameState(tempPlayer, tempBoxPoints, this);
    }

    public HashSet<Point> getWalls() {
        return walls;
    }

    public GameState getOrigin() {
        return this.origin;
    }

    public HashSet<Point> getTargets() {
        return targets;
    }

    public HashSet<Point> getDeadlocks() {
        return deadlocks;
    }

    public HashSet<Point> getFloors() {
        return floors;
    }

}
