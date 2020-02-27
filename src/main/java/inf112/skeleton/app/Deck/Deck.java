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

    private final int nrOfMove1 = 18;
    private final int nrOfMove2 = 12;
    private final int nrOfMove3 = 6;
    private final int nrOfBackup = 6;
    private final int nrOfRotateRight = 18;
    private final int nrOfRotateLeft = 18;
    private final int nrOfUturn = 6;

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
        for(int i=0; i<otherDeck.size(); i++) {
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

    /**
     * Add the cards used in a programCard deck. Should only be used when making a new deck
     */
    public void createDeck() {
        addMove1();
        addMove2();
        addMove3();
        addBackup();
        addRotateLeft();
        addRotateRight();
        addUturn();
        shuffle();
    }

    private void addMove1() {
        int priority = 490;
        for (int i=0; i<nrOfMove1; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE1));
            priority += 10;
        }
    }

    private void addMove2() {
        int priority = 670;
        for (int i=0; i<nrOfMove2; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE2));
            priority += 10;
        }
    }

    private void addMove3() {
        int priority = 790;
        for (int i=0; i<nrOfMove3; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE3));
            priority += 10;
        }
    }

    private void addBackup() {
        int priority = 430;
        for (int i=0; i<nrOfBackup; i++) {
            deck.add(new ProgramCard(priority, CardType.BACKUP));
            priority += 10;
        }
    }

    private void addRotateRight() {
        int priority = 80;
        for (int i = 0; i < nrOfRotateRight; i++) {
            deck.add(new ProgramCard(priority, CardType.ROTATERIGHT));
            priority += 20;
        }
    }

    private void addRotateLeft() {
        int priority = 70;
        for (int i = 0; i < nrOfRotateLeft; i++) {
            deck.add(new ProgramCard(priority, CardType.ROTATELEFT));
            priority += 20;
        }
    }

    private void addUturn() {
        int priority = 10;
        for (int i = 0; i < nrOfUturn; i++) {
            deck.add(new ProgramCard(priority, CardType.UTURN));
            priority += 10;
        }
    }
}
