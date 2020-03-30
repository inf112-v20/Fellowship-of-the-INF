package inf112.skeleton.app.gridObjectTests;

import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.BoardPiece;
import inf112.skeleton.app.grid_objects.FloorPiece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardPieceTest {
    BoardPiece boardPiece;

    @Before
    public void generateBoardPiece() {
        this.boardPiece = new FloorPiece(new Position(0, 0), 1);
    }


    @Test
    public void getPos() {
        for (int x = 1; x <= 20; x++) {
            for (int y = 1; y <= 20; y++) {
                boardPiece = new FloorPiece(new Position(x, y), 1);
                assertEquals(boardPiece.getPos(), new Position(x, y));
            }
        }
    }

    @Test
    public void setPos() {
        for (int x = 1; x <= 20; x++) {
            for (int y = 1; y <= 20; y++) {
                assertNotEquals(boardPiece.getPos(), new Position(x, y));
                boardPiece.setPos(new Position(x, y));
                assertEquals(boardPiece.getPos(), new Position(x, y));
            }
        }
    }
}