package inf112.skeleton.app.GridObjects;

import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

public class LaserSourcePiece extends WallPiece {

    private boolean doubleLaser;

    /**
     *
     * @param pos
     * @param id
     * @param dir direction is opposite of laser direction, ie the wall side
     * @param doubleLaser
     */
    public LaserSourcePiece(Position pos, int id, Direction dir, boolean doubleLaser) {
        super(pos, id, dir);
        this.doubleLaser = doubleLaser;
    }
}
