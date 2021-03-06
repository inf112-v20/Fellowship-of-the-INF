package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;

public class WallPiece extends DirectionedPiece {
    public WallPiece(Position pos, int id, Direction dir) {
        super(pos, id, dir);
    }

    /**
     * @param dir direction of player piece
     * @return whether player piece can move into this tile
     */
    public boolean canGo(Direction dir) {
        return !dir.getOppositeDirection().equals(this.dir);
    }

    /**
     * @param dir direction of player piece
     * @return whether a player piece can move away from this tile
     */
    public boolean canLeave(Direction dir) {
        return !this.dir.equals(dir);
    }
}
