package inf112.skeleton.app.GridObjects;

import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

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

    /***
     * Function that returns how much damage a laser gives
     * Is based on if there is a double laser or cross.
     * @return
     */
    public int getDamageFromLasers() {
        int damageCount = 1;
        if (doubleLaser) damageCount = damageCount * 2;
        if (crossingLasers) damageCount = damageCount * 2;
        return damageCount;
    }
}
