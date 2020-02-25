package inf112.skeleton.app.GridObjects;

<<<<<<< HEAD
=======
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

>>>>>>> 4c370910558ee481059077764f1c4ebaed24d2e1
/**
 * BoardPiece representing a pusher, which when active, pushes the player on the cell
 */
public class PusherPiece extends DirectionedPiece {
    private boolean active;

    public PusherPiece(Position pos, int id, Direction dir, boolean active) {
        super(pos, id, dir);
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
