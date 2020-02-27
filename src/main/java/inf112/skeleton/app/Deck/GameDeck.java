package inf112.skeleton.app.Deck;


public class GameDeck {
    private Deck drawDeck;
    private Deck discardDeck;
    private final int maxNumberOfCardsOnHand = 9;

    public GameDeck() {
        drawDeck = new Deck();
        drawDeck.createDeck();
        discardDeck = new Deck();
    }

    //Draw a new hand based on the damage you have taken. Will also handle if you are running out of cards.
    //Number of cards are calculated by subtracting the number of damage you have taken from the maximum number of cards
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
}
