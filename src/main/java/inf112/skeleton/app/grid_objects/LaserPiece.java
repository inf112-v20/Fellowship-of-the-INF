package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;

public class LaserPiece extends DirectionedPiece {
    private boolean doubleLaser;
    private boolean crossingLasers;

    /**
     *
     * @param pos position of laser piece on board
     * @param id tmx id
     * @param dir direction the laser is shooting
     * @param doubleLaser true if there are two parallel lasers on this cell
     * @param crossingLasers true if this is a grid with crossing lasers
     */
    public LaserPiece(Position pos, int id, Direction dir, boolean doubleLaser, boolean crossingLasers) {
        super(pos, id, dir);
        this.doubleLaser = doubleLaser;
        this.crossingLasers = crossingLasers;
    }

    public boolean isDoubleLaser() {
        return doubleLaser;
    }

    public boolean isCrossingLasers() {
        return crossingLasers;
    }

}
