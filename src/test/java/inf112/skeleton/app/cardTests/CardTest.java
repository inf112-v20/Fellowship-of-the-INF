package inf112.skeleton.app.cardTests;

import inf112.skeleton.app.cards.CardType;
import inf112.skeleton.app.cards.ProgramCard;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test cannot run, as the ProgramCard class uses gdx to save a texture,
 * this makes the class not able to run by itself
 *
 * To run this test comment out line 15 in ProgramCard.java where the createImage(); method is called
 */
public class CardTest {

    @Test
    public void getPriorityTest() {
        assertEquals(100,new ProgramCard(100, CardType.MOVE1).getPriority());
    }

    @Test
    public void getCardTypeTest() {
        assertEquals(CardType.MOVE1,new ProgramCard(100, CardType.MOVE1).getCommand());
    }
}
