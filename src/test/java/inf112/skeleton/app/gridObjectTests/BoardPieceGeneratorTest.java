package inf112.skeleton.app.gridObjectTests;

import inf112.skeleton.app.grid_objects.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * It's a bit diffucult to test the BoardPieceGenerator as one would need a list of what each object reffers to which
 * id in the tmx file.
 * For some known id's we have made tests
 */
public class BoardPieceGeneratorTest {
    private BoardPieceGenerator generator;

    @Before
    public void initialize() {
        generator = new BoardPieceGenerator(0, 0);
    }

    @Test
    public void generateFloorPieceIsFloorPiece() {
        BoardPiece piece = generator.generate(5);
        assertEquals(piece.getClass(), FloorPiece.class);
    }

    @Test
    public void generateAbyssPieceIsAbyssPiece() {
        BoardPiece piece = generator.generate(6);
        assertEquals(piece.getClass(), AbyssPiece.class);
    }

    /**
     * There are many types of flag pieces, with different id's
     */
    @Test
    public void generateFlagPieceIsFlagPiece() {
        BoardPiece piece = generator.generate(55);
        assertEquals(piece.getClass(), FlagPiece.class);
        piece = generator.generate(63);
        assertEquals(piece.getClass(), FlagPiece.class);
        piece = generator.generate(71);
        assertEquals(piece.getClass(), FlagPiece.class);
        piece = generator.generate(79);
        assertEquals(piece.getClass(), FlagPiece.class);
    }

    @Test
    public void xPositionInGeneratedPieceIsCorrect() {
        for (int xValue = 0; xValue < 12; xValue++) {
            generator = new BoardPieceGenerator(xValue, 0);
            BoardPiece piece = generator.generate(5);
            assertEquals(piece.getPos().getX(), xValue);
        }
    }

    @Test
    public void yPositionInGeneratedPieceIsCorrect() {
        for (int yValue = 0; yValue < 12; yValue++) {
            generator = new BoardPieceGenerator(0, yValue);
            BoardPiece piece = generator.generate(5);
            assertEquals(piece.getPos().getY(), yValue);
        }
    }
}