package inf112.skeleton.app.GridObjects;

public abstract class DirectionedPiece extends BoardPiece {
    Direction dir;

    /**
     * A board piece with direction
     * @param pos
     * @param id
     * @param dir
     */
    public DirectionedPiece(Position pos, int id, Direction dir) {
        super(pos, id);
        this.dir = dir;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction newDirection) {
        this.dir = newDirection;
    }
}
