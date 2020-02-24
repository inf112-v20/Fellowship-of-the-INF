package inf112.skeleton.app.GridObjects;

public class LazerSourcePiece extends DirectionedPiece {

    private boolean doubleLaser;

    public LazerSourcePiece(Position pos, int id, Direction dir, boolean doubleLaser) {
        super(pos, id, dir);
        this.doubleLaser = doubleLaser;
    }
}
