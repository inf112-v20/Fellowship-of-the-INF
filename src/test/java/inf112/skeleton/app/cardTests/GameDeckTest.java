package inf112.skeleton.app.cardTests;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.Deck;
import inf112.skeleton.app.deck.GameDeck;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * This test cannot run, as the ProgramCard class uses gdx to save a texture,
 * this makes the class not able to run by itself
 *
 * To run this test comment out line 15 in ProgramCard.java where the createImage(); method is called
 */
public class GameDeckTest {

    GameDeck gameDeck;
    ArrayList<ProgramCard> playerHand;

    @Before
    public void setUp() {
        gameDeck = new GameDeck();
        playerHand = new ArrayList<>();
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
