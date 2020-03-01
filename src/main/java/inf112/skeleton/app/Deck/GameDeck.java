package inf112.skeleton.app.Deck;


import inf112.skeleton.app.Cards.ProgramCard;

public class GameDeck {
    private Deck drawDeck;
    private Deck lockedCards;
    private final int maxNumberOfCardsOnHand = 9;
    private int dealtCards;

    public GameDeck() {
        drawDeck = new Deck();
        drawDeck.createDeck();
        lockedCards = new Deck();
    }
    public Deck getDrawDeck(){
        return drawDeck;
    }

    public Deck dealHand(int damage) {
        Deck playerHand = new Deck();
        int numberOfCards = maxNumberOfCardsOnHand - damage;
        for (int i = dealtCards; i < dealtCards + numberOfCards; i++) {
            playerHand.addCard(drawDeck.getCard(i));
        }
        dealtCards += numberOfCards;
        return playerHand;
    }
/*
MIGHT BE USEFUL FOR LATER
    public void lockCard(ProgramCard card){
        for (int i = 0; i < drawDeck.size(); i++) {
            if(drawDeck.getCard(i).equals(card)){
                lockedCards.addCard(card);
                drawDeck.removeCard(i);
            }
        }
    }
    public void unlockCard(ProgramCard card){
        for (int i = 0; i < lockedCards.size(); i++) {
            if(lockedCards.getCard(i).equals(card)){
                drawDeck.addCard(card);
                lockedCards.removeCard(i);
            }
        }
    }
    public void newRound() {
        drawDeck.shuffle();
        dealtCards = 0;
    }

 */
}