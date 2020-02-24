package inf112.skeleton.app.Deck;

/**
 * TODO Find a way to make priority. Atm are all cards set to priority 1
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
    private final int nrOfrotateRight = 18;
    private final int nrOfrotateLeft = 18;
    private final int nrOfUturn = 6;


    public Deck() {
        deck = new ArrayList<ProgramCard>();
    }

    //Draw card from the top of the deck. This DOES removes the card from the deck
    public ProgramCard drawCard(int index) {
        ProgramCard card = deck.get(index);
        deck.remove(index);
        return card;
    }

    //get size of deck
    public int size(){
        return deck.size();
    }

    //shuffle deck
    public void shuffle() {
        Collections.shuffle(deck);
    }

    //TODO Explain
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

    //Used when looking at a card. This DOES NOT remove the card from the deck
    public ProgramCard seeCard(int index){
        return deck.get(index);
    }

    //Add the cards used in a programCard deck. Should only be used when making a new deck
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
        for (int i=0; i<nrOfMove1; i++)
            deck.add(new ProgramCard(1, CardType.MOVE1));
    }

    private void addMove2() {
        for (int i=0; i<nrOfMove2; i++)
            deck.add(new ProgramCard(1, CardType.MOVE2));
    }

    private void addMove3() {
        for (int i=0; i<nrOfMove3; i++)
            deck.add(new ProgramCard(1, CardType.MOVE3));
    }

    private void addBackup() {
        for (int i=0; i<nrOfBackup; i++)
            deck.add(new ProgramCard(1, CardType.BACKUP));
    }

    private void addRotateRight() {
        for (int i = 0; i < nrOfrotateRight; i++)
            deck.add(new ProgramCard(1, CardType.ROTATERIGHT));
    }

    private void addRotateLeft() {
        for (int i = 0; i < nrOfrotateLeft; i++)
            deck.add(new ProgramCard(1, CardType.ROTATELEFT));
    }

    private void addUturn() {
        for (int i = 0; i < nrOfUturn; i++)
            deck.add(new ProgramCard(1, CardType.UTURN));
    }
}
