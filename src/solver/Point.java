package solver;

public class Point {
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Point pointAtMove(char direction) {

        switch (direction) {
            case 'u':
                return new Point(this.x, this.y - 1);
            case 'd':
                return new Point(this.x, this.y + 1);
            case 'l':
                return new Point(this.x - 1, this.y);
            case 'r':
                return new Point(this.x + 1, this.y);
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    @Override
    public int hashCode() {
        return 31 * x + y; // produces fewer collisions
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true; // same reference
        if (obj == null || getClass() != obj.getClass())
            return false; // null or diff type
        Point other = (Point) obj;
        return this.x == other.x && this.y == other.y; // compare fields
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
