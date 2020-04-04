package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;

/**
 * BoardPiece representing a pusher, which when active, pushes the player on the cell
 */
public class PusherPiece extends DirectionedPiece {
    private boolean active;

    public PusherPiece(Position pos, int id, Direction dir, boolean activeWhenOddPhase) {
        super(pos, id, dir);
        this.active = activeWhenOddPhase;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
