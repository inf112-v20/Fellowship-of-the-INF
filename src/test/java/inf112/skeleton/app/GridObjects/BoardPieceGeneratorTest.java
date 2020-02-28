package inf112.skeleton.app.GridObjects;

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
        BoardPiece piece = generator.generate(1);
        assertEquals(piece.getClass(), FloorPiece.class);
    }
}