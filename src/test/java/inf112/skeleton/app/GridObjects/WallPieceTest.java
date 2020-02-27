package inf112.skeleton.app.GridObjects;

import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WallPieceTest {
    private WallPiece wallPiece;

    @Before
    public void initializeWallPiece() {
        this.wallPiece = new WallPiece(new Position(0, 0), 1, Direction.NORTH);
    }

    @Test
    public void canGo() {
        for (Direction dir : Direction.values()) {
            wallPiece = new WallPiece(new Position(0, 0), 1, dir);
            assertFalse(wallPiece.canGo(dir.getOppositeDirection()));
            assertTrue(wallPiece.canGo(dir));
            assertTrue(wallPiece.canGo(dir.getRightTurnDirection()));
            assertTrue(wallPiece.canGo(dir.getLeftTurnDirection()));

        }
    }

    @Test
    public void canLeave() {
        for (Direction dir : Direction.values()) {
            wallPiece = new WallPiece(new Position(0, 0), 1, dir);
            assertTrue(wallPiece.canLeave(dir.getOppositeDirection()));
            assertFalse(wallPiece.canLeave(dir));
            assertTrue(wallPiece.canLeave(dir.getRightTurnDirection()));
            assertTrue(wallPiece.canLeave(dir.getLeftTurnDirection()));

        }
    }
}