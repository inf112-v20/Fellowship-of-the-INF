package inf112.skeleton.app.Deck;

import inf112.skeleton.app.CardButton;

public class GameDeck {
    private Deck drawDeck;
    private Deck discardDeck;
    private final int maxNumberOfCardsOnHand = 9;
    private CardButton cardButton;

    public GameDeck() {
        drawDeck = new Deck();
        drawDeck.createDeck();
        discardDeck = new Deck();
    }

    /**
     * Draw a new hand based on the damage you have taken. Will also handle if you are running out of cards.
     * Number of cards are calculated by subtracting the number of damage you have taken from the maximum number of cards
     *
     * @param playerHand The Deck of Cards the player had last round
     * @param numberOfCards The number of cards the player should draw
     * @return A new hand of cards to a player
     */
    public Deck drawHand(Deck playerHand, int numberOfCards) {
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
            cardButton = new CardButton(drawDeck.getCard(0), i);
            drawDeck.removeCard(0);
        }
    }
}
