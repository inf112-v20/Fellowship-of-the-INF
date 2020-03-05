package inf112.skeleton.app;

import java.util.concurrent.TimeUnit;

public class Round {

    final int NUMBER_OF_PHASES = 1;
    private Phase phase;

    public Round(Game game) {
        this.phase = new Phase(game);
    }

    /**
     * Starts executing all the phases
     */
    public void startRound(){
        for (int i = 0; i < NUMBER_OF_PHASES; i++) {
            System.out.println("PHASE " + (i+1) + " START!");
            phase.executePhase(i);
        }
    }

    public Phase getPhase(){return phase;}
}
//FIND OUT WHY NO ONE IS MOVING!!!!!