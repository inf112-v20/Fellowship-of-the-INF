package inf112.skeleton.app.CardTests;

import inf112.skeleton.app.Deck.Deck;
import inf112.skeleton.app.Deck.GameDeck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameDeckTest {

    GameDeck gameDeck;
    Deck playerHand;

    /**
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
    */
}
