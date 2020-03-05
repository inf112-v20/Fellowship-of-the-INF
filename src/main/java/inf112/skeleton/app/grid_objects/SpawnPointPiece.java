package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Position;

/**
 * This boardPiece represents a spawn point for a robot.
 */
public class SpawnPointPiece extends BoardPiece {
    int spawnNumber;
    public SpawnPointPiece(Position pos, int id, int spawnNumber) {
        super(pos, id);
        this.spawnNumber = spawnNumber;
    }

    public int getSpawnNumber() {
        return spawnNumber;
    }
}
