package solver.main;

public class Point {
    private final int x, y;
    private final int cachedHash;

    private static final int[] DX = { 0, 0, -1, 1 }; // u, d, l, r
    private static final int[] DY = { -1, 1, 0, 0 };
    private static final char[] DIRS = { 'u', 'd', 'l', 'r' };
    private static final char[] OPPOSITE = { 'd', 'u', 'r', 'l' };

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.cachedHash = (x << 16) | y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Point pointAtMove(char direction) {
        for (int i = 0; i < 4; i++) {
            if (DIRS[i] == direction) {
                return new Point(this.x + DX[i], this.y + DY[i]);
            }
        }
        throw new IllegalArgumentException("Invalid direction: " + direction);
    }

    public static char opposite(char direction) {
        for (int i = 0; i < 4; i++) {
            if (DIRS[i] == direction)
                return OPPOSITE[i];
        }
        throw new IllegalArgumentException("Invalid direction: " + direction);
    }

    @Override
    public int hashCode() {
        return cachedHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Point))
            return false;
        Point o = (Point) obj;
        return this.x == o.x && this.y == o.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
