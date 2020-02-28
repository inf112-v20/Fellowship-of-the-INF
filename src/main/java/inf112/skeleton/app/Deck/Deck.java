package inf112.skeleton.app.Deck;

/**
 * I list of Cards, with some extra functionalities.
 */

import inf112.skeleton.app.Cards.CardType;
import inf112.skeleton.app.Cards.ProgramCard;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<ProgramCard> deck;

    public Deck() {
        deck = new ArrayList<>();
    }

    /**
     * Get size of deck
     */
    public int size(){
        return deck.size();
    }

    /**
     * Shuffle the deck
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Move all the cards from a different deck into this one. This removes the cards from the other deck
     *
     * @param otherDeck A different deck that you want to move all the cards from, to this deck
     */
    public void moveAll(Deck otherDeck){
        int deckSize = otherDeck.size();
        for(int i=0; i<deckSize; i++) {
            deck.add(otherDeck.getCard(0));
            otherDeck.removeCard(0);
        }
    }

    public void addCard(ProgramCard card){
        deck.add(card);
    }

    public void removeCard(int index){
        deck.remove(index);
    }

    /**
     * Used when looking at a card. This DOES NOT remove the card from the deck
     *
     * @param index The index of where in the deck you want to see the card
     * @return The card at the index
     */
    public ProgramCard getCard(int index){
        return deck.get(index);
    }

}