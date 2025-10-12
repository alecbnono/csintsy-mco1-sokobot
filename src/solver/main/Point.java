package solver.main;

public class Point {
    private final int x, y;
    private final int cachedHash;

    // Mapping indices for directions: 'u'->0, 'd'->1, 'l'->2, 'r'->3
    private static final int[] DX = { 0, 0, -1, 1 }; // u, d, l, r
    private static final int[] DY = { -1, 1, 0, 0 };
    private static final char[] OPPOSITE = { 'd', 'u', 'r', 'l' };

    // ASCII lookup table for directions to index
    private static final byte[] DIR_INDEX = new byte[128];
    static {
        DIR_INDEX['u'] = 0;
        DIR_INDEX['d'] = 1;
        DIR_INDEX['l'] = 2;
        DIR_INDEX['r'] = 3;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.cachedHash = (x << 16) | y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point pointAtMove(char direction) {
        byte idx = DIR_INDEX[direction]; // branchless lookup
        return new Point(this.x + DX[idx], this.y + DY[idx]);
    }

    public static char opposite(char direction) {
        byte idx = DIR_INDEX[direction]; // branchless lookup
        return OPPOSITE[idx];
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
