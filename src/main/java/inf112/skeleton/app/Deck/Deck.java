package inf112.skeleton.app.Deck;

import inf112.skeleton.app.Cards.CardType;
import inf112.skeleton.app.Cards.ProgramCard;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<ProgramCard> deck;
    private ArrayList<Integer> priorities;

    private final int nrOfMove1 = 18;
    private final int nrOfMove2 = 12;
    private final int nrOfMove3 = 6;
    private final int nrOfBackup = 6;
    private final int nrOfRotateRight = 18;
    private final int nrOfRotateLeft = 18;
    private final int nrOfUturn = 6;
    private int nrOfCards;

    public Deck() {
        deck = new ArrayList<ProgramCard>();
        priorities = new ArrayList<Integer>();
        nrOfCards = nrOfMove1+nrOfMove2+nrOfMove3+nrOfBackup+nrOfRotateRight+nrOfRotateLeft+nrOfUturn;
    }

    /**
     * Draw card from the top of the deck. This DOES removes the card from the deck
     *
     * @param index The index of where in the deck you want to draw the card from
     * @return The chosen card
     */
    public ProgramCard drawCard(int index) {
        ProgramCard card = deck.get(index);
        deck.remove(index);
        return card;
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
        for(int i=0; i<otherDeck.size(); i++)
            deck.add(otherDeck.drawCard(0));
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
    public ProgramCard seeCard(int index){
        return deck.get(index);
    }

    /**
     * Add the cards used in a programCard deck. Should only be used when making a new deck
     */
    public void createDeck() {
        createPriority();
        addMove1();
        addMove2();
        addMove3();
        addBackup();
        addRotateLeft();
        addRotateRight();
        addUturn();
        shuffle();
    }

    /**
     * Creates a list with unique priority values. The value is a number between 200 and 200+(20* number of cards)
     */
    private void createPriority() {
        for (int i=0; i<nrOfCards; i++)
            priorities.add(200+(i*20));
        Collections.shuffle(priorities);
    }

    private void addMove1() {
        for (int i=0; i<nrOfMove1; i++) {
            deck.add(new ProgramCard(priorities.get(0), CardType.MOVE1));
            priorities.remove(0);
        }
    }

    private void addMove2() {
        for (int i=0; i<nrOfMove2; i++) {
            deck.add(new ProgramCard(priorities.get(0), CardType.MOVE2));
            priorities.remove(0);
        }
    }

    private void addMove3() {
        for (int i=0; i<nrOfMove3; i++) {
            deck.add(new ProgramCard(priorities.get(0), CardType.MOVE3));
            priorities.remove(0);
        }
    }

    private void addBackup() {
        for (int i=0; i<nrOfBackup; i++) {
            deck.add(new ProgramCard(priorities.get(0), CardType.BACKUP));
            priorities.remove(0);
        }
    }

    private void addRotateRight() {
        for (int i = 0; i < nrOfRotateRight; i++) {
            deck.add(new ProgramCard(priorities.get(0), CardType.ROTATERIGHT));
            priorities.remove(0);
        }
    }

    private void addRotateLeft() {
        for (int i = 0; i < nrOfRotateLeft; i++) {
            deck.add(new ProgramCard(priorities.get(0), CardType.ROTATELEFT));
            priorities.remove(0);
        }
    }

    private void addUturn() {
        for (int i = 0; i < nrOfUturn; i++) {
            deck.add(new ProgramCard(priorities.get(0), CardType.UTURN));
            priorities.remove(0);
        }
    }
}
