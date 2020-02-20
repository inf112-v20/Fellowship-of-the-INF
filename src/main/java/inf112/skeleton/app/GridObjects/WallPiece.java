package inf112.skeleton.app.GridObjects;

public class WallPiece extends BoardPiece {
    private Direction dir;
    public WallPiece(Position pos, int id, Direction dir) {
        super(pos, id);
        this.dir = dir;
    }
}
