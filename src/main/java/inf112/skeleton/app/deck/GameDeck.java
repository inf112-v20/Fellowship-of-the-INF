package inf112.skeleton.app.deck;


import inf112.skeleton.app.cards.CardType;
import inf112.skeleton.app.cards.ProgramCard;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The deck of programcards used to draw from and put in hands players.
 */
public class GameDeck {
    private ArrayList<ProgramCard> drawDeck = new ArrayList<>();
    private ArrayList<ProgramCard> discardDeck = new ArrayList<>();

    public GameDeck() {
        generateDeck(drawDeck);
    }

    /*
    Getters
     */

    public ArrayList<ProgramCard> getDiscardDeck(){
        return this.discardDeck;
    }

    public ArrayList<ProgramCard> getDrawDeck() { return this.drawDeck;}

    /**
     * Draw a new hand based on the damage you have taken. Will also handle if you are running out of cards.
     * Number of cards are calculated by subtracting the number of damage you have taken from the maximum number of cards
     *
     * @param playerHand  The cards the player has had on their hand
     * @param damageValue The amount of damage the player has taken
     * @return A new deck of cards to the play
     */
    public ArrayList<ProgramCard> drawHand(ArrayList<ProgramCard> playerHand, int damageValue) {
        int maxNumberOfCardsOnHand = 9;
        int numberOfCards = maxNumberOfCardsOnHand - damageValue;
        moveAll(discardDeck, playerHand);
        if (canDrawFullHand(numberOfCards)) {
            drawNumberOfCardsFromPile(playerHand, numberOfCards);
        } else {
            //First draw the remaining cards from the draw deck, then reset discard and draw pile
            int remainingInDraw = drawDeck.size();
            drawNumberOfCardsFromPile(playerHand, remainingInDraw);
            resetDrawAndDiscard();
            drawNumberOfCardsFromPile(playerHand, numberOfCards - remainingInDraw);
        }
        return playerHand;
    }

    //Check to see if you can draw a hand without the drawDeck running out of cards
    private boolean canDrawFullHand(int numberOfCards) {
        return numberOfCards <= drawDeck.size();
    }

    private void resetDrawAndDiscard() {
        moveAll(drawDeck, discardDeck);
        shuffle(drawDeck);
    }

    private void drawNumberOfCardsFromPile(ArrayList<ProgramCard> playerHand, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            playerHand.add(drawDeck.get(0));
            drawDeck.remove(0);
        }
    }

    public void moveAll(ArrayList<ProgramCard> toDeck, ArrayList<ProgramCard> fromDeck) {
        int deckSize = fromDeck.size();
        for (int i = 0; i < deckSize; i++) {
            toDeck.add(fromDeck.get(0));
            fromDeck.remove(0);
        }
    }

    private void shuffle(ArrayList<ProgramCard> deck) {
        Collections.shuffle(deck);
    }

    /**
     * Add the cards used in a programCard deck. Should only be used when making a new deck
     *
     * @param deck deck to be added to
     */
    private void generateDeck(ArrayList<ProgramCard> deck) {
        addMove1(deck);
        addMove2(deck);
        addMove3(deck);
        addBackup(deck);
        addRotateLeft(deck);
        addRotateRight(deck);
        addUturn(deck);
        shuffle(deck);
    }

    private void addMove1(ArrayList<ProgramCard> deck) {
        int priority = 490;
        int nrOfMove1 = 18;
        for (int i = 0; i < nrOfMove1; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE1));
            priority += 10;
        }
    }

    private void addMove2(ArrayList<ProgramCard> deck) {
        int priority = 670;
        int nrOfMove2 = 12;
        for (int i = 0; i < nrOfMove2; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE2));
            priority += 10;
        }
    }

    private void addMove3(ArrayList<ProgramCard> deck) {
        int priority = 790;
        int nrOfMove3 = 6;
        for (int i = 0; i < nrOfMove3; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE3));
            priority += 10;
        }
    }

    private void addBackup(ArrayList<ProgramCard> deck) {
        int priority = 430;
        int nrOfBackup = 6;
        for (int i = 0; i < nrOfBackup; i++) {
            deck.add(new ProgramCard(priority, CardType.BACKUP));
            priority += 10;
        }
    }

    private void addRotateRight(ArrayList<ProgramCard> deck) {
        int priority = 80;
        int nrOfRotateRight = 18;
        for (int i = 0; i < nrOfRotateRight; i++) {
            deck.add(new ProgramCard(priority, CardType.ROTATERIGHT));
            priority += 20;
        }
    }

    private void addRotateLeft(ArrayList<ProgramCard> deck) {
        int priority = 70;
        int nrOfRotateLeft = 18;
        for (int i = 0; i < nrOfRotateLeft; i++) {
            deck.add(new ProgramCard(priority, CardType.ROTATELEFT));
            priority += 20;
        }
    }

    private void addUturn(ArrayList<ProgramCard> deck) {
        int priority = 10;
        int nrOfUturn = 6;
        for (int i = 0; i < nrOfUturn; i++) {
            deck.add(new ProgramCard(priority, CardType.UTURN));
            priority += 10;
        }
    }
}