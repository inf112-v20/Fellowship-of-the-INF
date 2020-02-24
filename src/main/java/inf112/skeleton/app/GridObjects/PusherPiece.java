package inf112.skeleton.app.GridObjects;

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
