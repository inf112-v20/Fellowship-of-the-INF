package inf112.skeleton.app.gridObjectTests;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.gridObjects.DirectionedPiece;
import inf112.skeleton.app.gridObjects.WallPiece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionedPieceTest {
    private DirectionedPiece directionedPiece;

    @Before
    public void initializeDirectionedPiece() {
        this.directionedPiece = new WallPiece(new Position(0, 0), 1, Direction.NORTH);
    }

    @Test
    public void getDir() {
        for (Direction dir : Direction.values()) {
            directionedPiece = new WallPiece(new Position(0, 0), 1, dir);
            assertEquals(directionedPiece.getDir(), dir);
        }
    }

    @Test
    public void setDir() {
        //change starting direction to west, as Direction emun starts with NORTH
        directionedPiece = new WallPiece(new Position(0, 0), 1, Direction.WEST);
        for (Direction dir : Direction.values()) {
            assertNotEquals(directionedPiece.getDir(), dir);
            directionedPiece.setDir(dir);
            assertEquals(directionedPiece.getDir(), dir);
        }
    }

    @Test
    public void turnPieceInOppositeDirection() {
        for (Direction dir : Direction.values()) {
            directionedPiece = new WallPiece(new Position(0, 0), 1, dir);
            assertNotEquals(directionedPiece.getDir(), dir.getOppositeDirection());
            directionedPiece.turnPieceInOppositeDirection();
            assertEquals(directionedPiece.getDir(), dir.getOppositeDirection());
        }
    }

    @Test
    public void rotatePieceLeft() {
        for (Direction dir : Direction.values()) {
            directionedPiece = new WallPiece(new Position(0, 0), 1, dir);
            assertNotEquals(directionedPiece.getDir(), dir.getRightTurnDirection());
            directionedPiece.rotatePieceLeft();
            assertEquals(directionedPiece.getDir(), dir.getLeftTurnDirection());
        }

    }

    @Test
    public void turnPieceRight() {
        for (Direction dir : Direction.values()) {
            directionedPiece = new WallPiece(new Position(0, 0), 1, dir);
            assertNotEquals(directionedPiece.getDir(), dir.getRightTurnDirection());
            directionedPiece.rotatePieceRight();
            assertEquals(directionedPiece.getDir(), dir.getRightTurnDirection());
        }
    }
}