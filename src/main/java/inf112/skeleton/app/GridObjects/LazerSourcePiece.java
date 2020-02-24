package inf112.skeleton.app.GridObjects;

import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

public class LazerSourcePiece extends DirectionedPiece {

    private boolean doubleLaser;

    public LazerSourcePiece(Position pos, int id, Direction dir, boolean doubleLaser) {
        super(pos, id, dir);
        this.doubleLaser = doubleLaser;
    }
}
