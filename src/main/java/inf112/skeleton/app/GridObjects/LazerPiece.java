package inf112.skeleton.app.GridObjects;

<<<<<<< HEAD
=======
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

>>>>>>> 4c370910558ee481059077764f1c4ebaed24d2e1
public class LazerPiece extends DirectionedPiece {
    //true if there are two paralell lasers on this cell
    boolean doubleLaser;

    //true if there is a crossing of lasers on this
    boolean crossingLasers;


    public LazerPiece(Position pos, int id, Direction dir, boolean doubleLaser, boolean crossingLasers) {
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
     * Is based on if there is a double lazer or cross.
     * @return
     */
    public int getDamageFromLasers() {
        int damageCount = 1;
        if (doubleLaser) damageCount = damageCount * 2;
        if (crossingLasers) damageCount = damageCount * 2;
        return damageCount;
    }
}
