package inf112.skeleton.app.Deck;

import inf112.skeleton.app.Cards.ProgramCard;

/**
 * Revision:
 * Lettere å ha én kortstokk som alle trekker fra. Dette gjør at priority ikke kan kolidere.
 * Game klassen har ansvar for spillerhendene og det de velger
 */

public class PlayerDeck {

    private Deck drawDeck;
    private Deck playerHand;
    private Deck chosenDeck;
    private Deck discardDeck;

    public PlayerDeck() {
        drawDeck = new Deck();
        drawDeck.createDeck();

        playerHand = new Deck();
        chosenDeck = new Deck();
        discardDeck = new Deck();
    }

    //Draw a new hand based on the damage you have taken. Will also handle if you are running out of cards.
    //TODO explain the 10
    public void drawHand(int damageValue){
        int numberOfCards = 10-damageValue;
        discardDeck.moveAll(playerHand);
        discardDeck.moveAll(chosenDeck);

        if (canDrawFullHand(numberOfCards)) {
            drawNumberOfCardsFromPile(numberOfCards);
        } else {
            //First draw the remaining cards from the draw deck, then reset discard and draw pile
            //TODO Trekk resten av kortene
            drawNumberOfCardsFromPile(drawDeck.size()-1);
            resetDrawAndDiscard();
        }
    }

    //TODO explain
    public void chooseCard(int index, ProgramCard card) {
        chosenDeck.addCard(playerHand.drawCard(index));
    }

    public Deck getDrawDeck() {
        return drawDeck;
    }

    public Deck getPlayerHand() {
        return playerHand;
    }

    public Deck getChosenDeck() {
        return chosenDeck;
    }

    public Deck getDiscardDeck() {
        return discardDeck;
    }

    //Check to see if you can draw a hand without the drawDeck running out of cards
    private boolean canDrawFullHand(int numberOfCards) {
        if (numberOfCards <= drawDeck.size()-1)
            return true;
        else
            return false;
    }

    private void resetDrawAndDiscard(){
        drawDeck.moveAll(discardDeck);
        drawDeck.shuffle();
    }

    private void drawNumberOfCardsFromPile(int numberOfCards) {
        for(int i=0; i<numberOfCards; i++)
            playerHand.addCard(drawDeck.drawCard(0));
    }
}
