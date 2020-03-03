package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;

public class LaserSourcePiece extends WallPiece {

    private boolean doubleLaser;

    /**
     *
     * @param pos position of laser in map
     * @param id tmx id of Laser
     * @param dir direction is opposite of laser direction, ie the wall side
     * @param doubleLaser true if it is a double laser
     */
    public LaserSourcePiece(Position pos, int id, Direction dir, boolean doubleLaser) {
        super(pos, id, dir);
        this.doubleLaser = doubleLaser;
    }
}
