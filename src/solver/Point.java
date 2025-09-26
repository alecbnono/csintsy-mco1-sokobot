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
                return new Point(this.x, this.y + 1);
            case 'd':
                return new Point(this.x, this.y - 1);
            case 'l':
                return new Point(this.x - 1, this.y);
            case 'r':
                return new Point(this.x + 1, this.y);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "(" + x + ")" + " " + "(" + y + ")";
    }
}
