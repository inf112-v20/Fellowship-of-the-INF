package inf112.skeleton.app;

import inf112.skeleton.app.player.Player;

public class Round {

    final int NUMBER_OF_PHASES = 5;
    private int phaseNr = 0;
    private int roundNumber;
    private Phase phase;
    private Game game;

    public Round(Game game) {
        this.game = game;
        this.phase = new Phase(game);
    }

    /**
     * Starts executing all the phases
     */
    public void startRound(){
        System.out.println("ROUND " + (roundNumber) + " START!");
        for (int i = 0; i < game.getListOfPlayers().length ; i++) {
            Player player = game.getListOfPlayers()[i];
            player.setPlayerHandDeck(game.getGameDeck().drawHand(player.getPlayerHandDeck(), player.getDamage()));
            player.removeSelectedCards();
            int numberOfLockedCards = player.getLockedCards().size();
            for (int j = 0; j < numberOfLockedCards; j++) {
                player.getSelectedCards()[4-j] = (player.getLockedCards().get(j));
            }
        }
        for (int playerNumber = 2; playerNumber <= 4; playerNumber++) {
            game.getListOfPlayers()[playerNumber - 1].pickFirstFiveCards();
        }

        /*
        for (int i = 0; i < NUMBER_OF_PHASES; i++) {
            System.out.println("PHASE " + (i+1) + " START!");
            phase.executePhase(i);
        }

         */
    }

    public void nextPhase(){
        if(phaseNr > 4){
            return;
        }
        System.out.println("PHASE " + (phaseNr+1) + " START!");
        phase.executePhase(phaseNr);
        phaseNr++;
    }

    public Phase getPhase(){return phase;}

    public void setRoundNumber(int number){ this.roundNumber = number;}
    public int getRoundNumber(){return roundNumber;}

    public int getPhaseNr(){return phaseNr;}
}
