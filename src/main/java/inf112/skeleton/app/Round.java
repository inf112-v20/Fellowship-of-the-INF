package inf112.skeleton.app;

import java.util.concurrent.TimeUnit;

public class Round {

    final int NUMBER_OF_PHASES = 5;
    private Phase phase;

    public Round(Game game) {
        this.phase = new Phase(game);
    }

    /**
     * Starts executing all the phases
     */
    public void startRound(){
        for (int i = 0; i < NUMBER_OF_PHASES; i++) {
            phase.executePhase(i);
        }
    }
}
//FIND OUT WHY NO ONE IS MOVING!!!!!