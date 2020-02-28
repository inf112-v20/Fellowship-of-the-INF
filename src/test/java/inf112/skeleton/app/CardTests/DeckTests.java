package inf112.skeleton.app.CardTests;

import inf112.skeleton.app.Cards.CardType;
import inf112.skeleton.app.Cards.ProgramCard;
import inf112.skeleton.app.Deck.Deck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeckTests {

    Deck deck;
    Deck deck2;
    ProgramCard move1Card;
    ProgramCard move2Card;

    /**
    @Before
    public void setup() {
        deck = new Deck();
        move1Card = new ProgramCard(10,CardType.MOVE1);
        move2Card = new ProgramCard(20,CardType.MOVE2);
    }

    @Test
    public void addGetTest() {
        deck.addCard(move1Card);
        assertEquals(move1Card,deck.getCard(0));
    }

    @Test
    public void addRemoveTest() {
        deck.addCard(move1Card);
        deck.addCard(move2Card);
        deck.removeCard(0);
        assertEquals(move2Card,deck.getCard(0));
    }

    @Test
    public void sizeTest() {
        deck.addCard(move1Card);
        deck.addCard(move2Card);
        assertEquals(2,deck.size());
    }

    @Test
    public void moveAll() {
        deck2 = new Deck();

        deck2.addCard(move1Card);
        deck2.addCard(move2Card);

        deck.moveAll(deck2);

        assertEquals(0,deck2.size());
    }
    */
}
