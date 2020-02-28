package inf112.skeleton.app.GridObjects;

import inf112.skeleton.app.Grid.Position;
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