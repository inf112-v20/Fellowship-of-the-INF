package inf112.skeleton.app.gridObjectTests;

import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.gridObjects.CogPiece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CogPieceTest {
    private CogPiece cogPiece;

    @Before
    public void initiateCogPiece() {
        this.cogPiece = new CogPiece(new Position(0, 0), 0, true);
    }

    @Test
    public void isRotatedClockwise() {
        assertTrue(cogPiece.isRotateClockwise());
        cogPiece = new CogPiece(new Position(0, 0), 0, false);
        assertFalse(cogPiece.isRotateClockwise());
    }
}