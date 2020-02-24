package inf112.skeleton.app.GridObjects;

/**
 * BoardPiece that rotates the player either to the left or to the right
 */
public class CogPiece extends BoardPiece {

    private boolean rotateClockwise;

    public CogPiece(Position pos, int id, boolean rotateClockwise) {
        super(pos, id);
        this.rotateClockwise = rotateClockwise;
    }

    public boolean isRotateClockwise() {
        return rotateClockwise;
    }
}
