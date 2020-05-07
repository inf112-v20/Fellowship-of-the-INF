package inf112.skeleton.app.gridObjectTests;

import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.FlagPiece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlagPieceTest {
    private FlagPiece flagPiece;

    @Before
    public void initializeFlagPiece() {
        this.flagPiece = new FlagPiece(new Position(0, 0), 1, 1);
    }

    @Test
    public void getFlagNumber() {
        for (int flagNumber = 1; flagNumber <= 4; flagNumber++) {
            flagPiece = new FlagPiece(new Position(0, 0), 1, flagNumber);
            assertEquals(flagPiece.getFlagNumber(), flagNumber);
        }
    }

    @Test
    public void setFlagNumber() {
        for (int flagNumber = 2; flagNumber <= 4; flagNumber++) {
            assertNotEquals(flagPiece.getFlagNumber(), flagNumber);
            flagPiece.setFlagNumber(flagNumber);
            assertEquals(flagPiece.getFlagNumber(), flagNumber);
        }
    }
}