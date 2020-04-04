package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;

/**
 * BoardPiece representing a pusher, which when active, pushes the player on the cell
 * A pusher piece is either active during all odd-numbered phases, or all even-numbered phases
 */
public class PusherPiece extends WallPiece {
    private boolean activeWhenOddPhase;

    public PusherPiece(Position pos, int id, Direction dir, boolean activeWhenOddPhase) {
        super(pos, id, dir);
        this.activeWhenOddPhase = activeWhenOddPhase;
    }

    public boolean isActiveWhenOddPhase() {
        return activeWhenOddPhase;
    }

    public Direction getPushingDir() {
        return getDir().getOppositeDirection();
    }
}
