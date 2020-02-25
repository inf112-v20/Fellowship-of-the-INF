package inf112.skeleton.app.GridObjects;

<<<<<<< HEAD
=======
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

>>>>>>> 4c370910558ee481059077764f1c4ebaed24d2e1
public abstract class DirectionedPiece extends BoardPiece {
    Direction dir;

    /**
     * A board piece with direction
     * @param pos
     * @param id
     * @param dir
     */
    public DirectionedPiece(Position pos, int id, Direction dir) {
        super(pos, id);
        this.dir = dir;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction newDirection) {
        this.dir = newDirection;
    }
}
