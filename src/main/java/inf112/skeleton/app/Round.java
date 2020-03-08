package inf112.skeleton.app;

public class Round {

    final int NUMBER_OF_PHASES = 5;
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
            phase.performPhase(i);
        }
    }

    public Phase getPhase(){return phase;}
}