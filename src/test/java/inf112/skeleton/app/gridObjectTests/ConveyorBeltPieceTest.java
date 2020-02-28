package inf112.skeleton.app.gridObjectTests;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.gridObjects.ConveyorBeltPiece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConveyorBeltPieceTest {

    private ConveyorBeltPiece conveyorBeltPiece;

    @Before
    public void initiateConveyorBeltPiece() {
        this.conveyorBeltPiece = new ConveyorBeltPiece(new Position(0, 0), 1, Direction.NORTH, true, true);
    }


    @Test
    public void isTurn() {
        assertTrue(conveyorBeltPiece.isTurn());
        conveyorBeltPiece = new ConveyorBeltPiece(new Position(0, 0), 1, Direction.NORTH, false, true);
        assertFalse(conveyorBeltPiece.isTurn());
    }

    @Test
    public void isTurnRight() {
        assertTrue(conveyorBeltPiece.isTurnRight());
        conveyorBeltPiece = new ConveyorBeltPiece(new Position(0, 0), 1, Direction.NORTH, true, false);
        assertFalse(conveyorBeltPiece.isTurnRight());
    }
}