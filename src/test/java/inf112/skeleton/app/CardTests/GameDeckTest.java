package inf112.skeleton.app.CardTests;

import inf112.skeleton.app.Deck.Deck;
import inf112.skeleton.app.Deck.GameDeck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test cannot run, as the ProgramCard class uses gdx to save a texture,
 * this makes the class not able to run by itself
 *
 * To run this test comment out line 15 in ProgramCard.java where the createImage(); method is called
 */
public class GameDeckTest {

    GameDeck gameDeck;
    Deck playerHand;

    @Before
    public void setUp() {
        gameDeck = new GameDeck();
        playerHand = new Deck();
    }

    @Test
    public void canDrawHand() {
        playerHand = gameDeck.drawHand(playerHand,0);
        assertEquals(9,playerHand.size());
    }

    @Test
    public void canContinuouslyDrawHand() {
        for (int i=0; i<20; i++) {
            playerHand = gameDeck.drawHand(playerHand,0);
        }
        assertEquals(9,playerHand.size());
    }
}
