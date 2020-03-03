package inf112.skeleton.app.deck;


import inf112.skeleton.app.cards.CardType;
import inf112.skeleton.app.cards.ProgramCard;

import java.util.ArrayList;
import java.util.Collections;

public class GameDeck {
    private ArrayList<ProgramCard> drawDeck;
    private ArrayList<ProgramCard> discardDeck;
    private final int maxNumberOfCardsOnHand = 9;

    private final int nrOfMove1 = 18;
    private final int nrOfMove2 = 12;
    private final int nrOfMove3 = 6;
    private final int nrOfBackup = 6;
    private final int nrOfRotateRight = 18;
    private final int nrOfRotateLeft = 18;
    private final int nrOfUturn = 6;

    public GameDeck() {
        drawDeck = new ArrayList<ProgramCard>();
        generateDeck(drawDeck);
        discardDeck = new ArrayList<ProgramCard>();
    }

    /**
     * Draw a new hand based on the damage you have taken. Will also handle if you are running out of cards.
     * Number of cards are calculated by subtracting the number of damage you have taken from the maximum number of cards
     *
     * @param playerHand The cards the player has had on their hand
     * @param damageValue The amount of damage the player has taken
     * @return A new deck of cards to the play
     */
    public ArrayList<ProgramCard> drawHand(ArrayList<ProgramCard> playerHand, int damageValue) {
        int numberOfCards = maxNumberOfCardsOnHand - damageValue;
        moveAll(discardDeck, playerHand);
        if (canDrawFullHand(numberOfCards)) {
            drawNumberOfCardsFromPile(playerHand, numberOfCards);
        } else {
            //First draw the remaining cards from the draw deck, then reset discard and draw pile
            int remainingInDraw = drawDeck.size();
            drawNumberOfCardsFromPile(playerHand, remainingInDraw - 1);
            resetDrawAndDiscard();
            drawNumberOfCardsFromPile(playerHand, numberOfCards - remainingInDraw);
        }
        return playerHand;
    }

    //Check to see if you can draw a hand without the drawDeck running out of cards
    private boolean canDrawFullHand(int numberOfCards) {
        return numberOfCards <= drawDeck.size() - 1;
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

    private void moveAll(ArrayList<ProgramCard> toDeck, ArrayList<ProgramCard> fromDeck){
        int deckSize = fromDeck.size();
        for(int i=0; i<deckSize; i++) {
            toDeck.add(fromDeck.get(0));
            fromDeck.remove(0);
        }
    }

    private void shuffle(ArrayList<ProgramCard> deck) {
        Collections.shuffle(deck);
    }

    /**
     * Add the cards used in a programCard deck. Should only be used when making a new deck
     * @param deck
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
        for (int i=0; i<nrOfMove1; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE1));
            priority += 10;
        }
    }

    private void addMove2(ArrayList<ProgramCard> deck) {
        int priority = 670;
        for (int i=0; i<nrOfMove2; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE2));
            priority += 10;
        }
    }

    private void addMove3(ArrayList<ProgramCard> deck) {
        int priority = 790;
        for (int i=0; i<nrOfMove3; i++) {
            deck.add(new ProgramCard(priority, CardType.MOVE3));
            priority += 10;
        }
    }

    private void addBackup(ArrayList<ProgramCard> deck) {
        int priority = 430;
        for (int i=0; i<nrOfBackup; i++) {
            deck.add(new ProgramCard(priority, CardType.BACKUP));
            priority += 10;
        }
    }

    private void addRotateRight(ArrayList<ProgramCard> deck) {
        int priority = 80;
        for (int i = 0; i < nrOfRotateRight; i++) {
            deck.add(new ProgramCard(priority, CardType.ROTATERIGHT));
            priority += 20;
        }
    }

    private void addRotateLeft(ArrayList<ProgramCard> deck) {
        int priority = 70;
        for (int i = 0; i < nrOfRotateLeft; i++) {
            deck.add(new ProgramCard(priority, CardType.ROTATELEFT));
            priority += 20;
        }
    }

    private void addUturn(ArrayList<ProgramCard> deck) {
        int priority = 10;
        for (int i = 0; i < nrOfUturn; i++) {
            deck.add(new ProgramCard(priority, CardType.UTURN));
            priority += 10;
        }
    }
}