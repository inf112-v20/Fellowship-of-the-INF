package inf112.skeleton.app.Deck;


import inf112.skeleton.app.Cards.CardType;
import inf112.skeleton.app.Cards.ProgramCard;

public class GameDeck {
    private Deck drawDeck;
    private Deck discardDeck;
    private final int maxNumberOfCardsOnHand = 9;

    private final int nrOfMove1 = 18;
    private final int nrOfMove2 = 12;
    private final int nrOfMove3 = 6;
    private final int nrOfBackup = 6;
    private final int nrOfRotateRight = 18;
    private final int nrOfRotateLeft = 18;
    private final int nrOfUturn = 6;

    public GameDeck() {
        drawDeck = new Deck();
        createDeck(drawDeck);
        discardDeck = new Deck();
    }

    //Draw a new hand based on the damage you have taken. Will also handle if you are running out of cards.
    //Number of cards are calculated by subtracting the number of damage you have taken from the maximum number of cards

    /**
     *
     * @param playerHand
     * @param damageValue
     * @return
     */
    public Deck drawHand(Deck playerHand, int damageValue) {
        int numberOfCards = maxNumberOfCardsOnHand - damageValue;
        discardDeck.moveAll(playerHand);
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

    public Deck getDrawDeck() {
        return drawDeck;
    }

    public Deck getDiscardDeck() {
        return discardDeck;
    }

    //Check to see if you can draw a hand without the drawDeck running out of cards
    private boolean canDrawFullHand(int numberOfCards) {
        return numberOfCards <= drawDeck.size() - 1;
    }

    private void resetDrawAndDiscard() {
        drawDeck.moveAll(discardDeck);
        drawDeck.shuffle();
    }

    private void drawNumberOfCardsFromPile(Deck playerHand, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            playerHand.addCard(drawDeck.getCard(0));
            drawDeck.removeCard(0);
        }
    }

    /**
     * Add the cards used in a programCard deck. Should only be used when making a new deck
     */
    private void createDeck(Deck deck) {
        addMove1(deck);
        addMove2(deck);
        addMove3(deck);
        addBackup(deck);
        addRotateLeft(deck);
        addRotateRight(deck);
        addUturn(deck);
        deck.shuffle();
    }

    private void addMove1(Deck deck) {
        int priority = 490;
        for (int i=0; i<nrOfMove1; i++) {
            deck.addCard(new ProgramCard(priority, CardType.MOVE1));
            priority += 10;
        }
    }

    private void addMove2(Deck deck) {
        int priority = 670;
        for (int i=0; i<nrOfMove2; i++) {
            deck.addCard(new ProgramCard(priority, CardType.MOVE2));
            priority += 10;
        }
    }

    private void addMove3(Deck deck) {
        int priority = 790;
        for (int i=0; i<nrOfMove3; i++) {
            deck.addCard(new ProgramCard(priority, CardType.MOVE3));
            priority += 10;
        }
    }

    private void addBackup(Deck deck) {
        int priority = 430;
        for (int i=0; i<nrOfBackup; i++) {
            deck.addCard(new ProgramCard(priority, CardType.BACKUP));
            priority += 10;
        }
    }

    private void addRotateRight(Deck deck) {
        int priority = 80;
        for (int i = 0; i < nrOfRotateRight; i++) {
            deck.addCard(new ProgramCard(priority, CardType.ROTATERIGHT));
            priority += 20;
        }
    }

    private void addRotateLeft(Deck deck) {
        int priority = 70;
        for (int i = 0; i < nrOfRotateLeft; i++) {
            deck.addCard(new ProgramCard(priority, CardType.ROTATELEFT));
            priority += 20;
        }
    }

    private void addUturn(Deck deck) {
        int priority = 10;
        for (int i = 0; i < nrOfUturn; i++) {
            deck.addCard(new ProgramCard(priority, CardType.UTURN));
            priority += 10;
        }
    }
}