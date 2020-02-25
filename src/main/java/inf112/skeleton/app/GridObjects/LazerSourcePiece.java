package inf112.skeleton.app.GridObjects;

<<<<<<< HEAD
=======
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

>>>>>>> 4c370910558ee481059077764f1c4ebaed24d2e1
public class LazerSourcePiece extends DirectionedPiece {

    private boolean doubleLaser;

    public LazerSourcePiece(Position pos, int id, Direction dir, boolean doubleLaser) {
        super(pos, id, dir);
        this.doubleLaser = doubleLaser;
    }
}
