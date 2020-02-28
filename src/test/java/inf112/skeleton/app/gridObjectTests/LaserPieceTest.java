package inf112.skeleton.app.gridObjectTests;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.gridObjects.LaserPiece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LaserPieceTest {
    private LaserPiece laserPiece;

    @Before
    public void initializeLaserPiece() {
        this.laserPiece = new LaserPiece(new Position(0, 0), 1, Direction.NORTH, true, true);
    }

    @Test
    public void isDoubleLaser() {
        assertTrue(laserPiece.isDoubleLaser());
        this.laserPiece = new LaserPiece(new Position(0, 0), 1, Direction.NORTH, false, true);
        assertFalse(laserPiece.isDoubleLaser());
    }

    @Test
    public void isCrossingLasers() {
        assertTrue(laserPiece.isCrossingLasers());
        this.laserPiece = new LaserPiece(new Position(0, 0), 1, Direction.NORTH, true, false);
        assertFalse(laserPiece.isCrossingLasers());
    }

    @Test
    public void getDamageFromLasers() {
        assertEquals(laserPiece.getDamageFromLasers(), 4);
        laserPiece = new LaserPiece(new Position(0, 0), 1, Direction.NORTH, false, true);
        assertEquals(laserPiece.getDamageFromLasers(), 2);
        laserPiece = new LaserPiece(new Position(0, 0), 1, Direction.NORTH, true, false);
        assertEquals(laserPiece.getDamageFromLasers(), 2);
        laserPiece = new LaserPiece(new Position(0, 0), 1, Direction.NORTH, false, false);
        assertEquals(laserPiece.getDamageFromLasers(), 1);
    }
}