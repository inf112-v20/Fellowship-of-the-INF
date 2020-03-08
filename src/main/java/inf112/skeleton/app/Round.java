package inf112.skeleton.app;

import java.util.concurrent.TimeUnit;

public class Round {

    final int NUMBER_OF_PHASES = 2;
    private Phase phase;
    private Game game;

    public Round(Game game) {
        this.phase = new Phase(game);
        this.game = game;
    }

    /**
     * Starts executing all the phases
     */
    public void startRound(){
        for (int i = 0; i < NUMBER_OF_PHASES; i++) {
            System.out.println("PHASE " + (i+1) + " START!");
           /* while(!game.getMoves().isEmpty() && i != 0) {
                System.out.println("Moves to execute");
            }*/
            phase.executePhase(i);
        }
    }

    public Phase getPhase(){return phase;}
}