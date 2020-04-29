package inf112.skeleton.app.grid;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
    Getters
     */

    public int getY() { return this.y; }

    public int getX() {
        return this.x;
    }

    /*
    Setters
    */
    public void setY(int y) { this.y = y; }

    public void setX(int x) { this.x = x; }

    @Override
    public String toString() {
        return "Pos(" + x +
                ", " + y +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    /**
     * Returns a new Position object positioned in the direction dir from this position
     *
     * @param dir direction from this position that the current position lies.
     * @return new Posiiton object
     */
    public Position getPositionIn(Direction dir) {
        int newX = x;
        int newY = y;
        switch (dir) {
            case NORTH:
                newY += 1;
                break;
            case SOUTH:
                newY -= 1;
                break;
            case WEST:
                newX -= 1;
                break;
            case EAST:
                newX += 1;
                break;
        }
        return new Position(newX, newY);
    }

}
