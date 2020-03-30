package inf112.skeleton.app.game_logic;

import inf112.skeleton.app.player.AIPlayer;
import inf112.skeleton.app.player.Player;

import java.util.Arrays;


public class Round {

    final int NUMBER_OF_PHASES = 5;
    private int phaseNr = 0;
    private int roundNumber;
    private Phase phase;
    private Game game;

    public Round(Game game) {
        this.phase = new Phase(game);
        this.game = game;
    }

    /**
     * Starts a new round.
     * Gives every player in the game a new hand of cards with respect to their damage
     * A players locked cards will carry over from last round and be automatically put
     * in that players list of selected cards (from right to left).
     * Computer players pick the first five cards they are given.
     */
    public void startRound(){
        for (int i = 0; i < game.getListOfPlayers().length ; i++) {
            Player player = game.getListOfPlayers()[i];
            player.setPlayerHandDeck(game.getGameDeck().drawHand(player.getPlayerHandDeck(), player.getDamage()));
            player.removeSelectedCards();
            int numberOfLockedCards = player.getLockedCards().size();
            for (int j = 0; j < numberOfLockedCards; j++) {
                int number = 5 - numberOfLockedCards;
                player.getSelectedCards()[number + j] = (player.getLockedCards().get(j));
            }
        }
        for (int i = 1; i < game.getListOfPlayers().length ; i++) {
            AIPlayer aiPlayer = (AIPlayer) game.getListOfPlayers()[i];
            System.out.println(aiPlayer.toString() + " playerhand: " + aiPlayer.getPlayerHandDeck());
            aiPlayer.pickCards();
            System.out.println(aiPlayer.toString() + " chose " + Arrays.toString(aiPlayer.getSelectedCards()) + "\n");
        }

    }

    /**
     * Execute the next phase
     */
    public void nextPhase(){
        if(phaseNr >= NUMBER_OF_PHASES){ return; }
        phase.executePhase(phaseNr);
        phaseNr++;
    }

    /**
     * When the round is done, check if there are any players in power down mode.
     * If there are any, and they still have lives left, then take them out of power down mode.
     */
    public void finishRound(){
        for (int i=0; i<game.getListOfPlayers().length; i++) {
            Player player = game.getListOfPlayers()[i];
            if (player.isPowerDownMode() && player.getLives()>=0) {
                player.setPowerDownMode(false);
            }
        }
    }

    public Phase getPhase(){return phase;}

    public void setRoundNumber(int number){ this.roundNumber = number;}

    public int getRoundNumber(){return roundNumber;}

    public int getPhaseNr(){return phaseNr;}
}
