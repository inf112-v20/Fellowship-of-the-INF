package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;

public class LaserPiece extends DirectionedPiece {
    //true if there are two paralell lasers on this cell
    boolean doubleLaser;

    //true if there is a crossing of lasers on this
    boolean crossingLasers;


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
