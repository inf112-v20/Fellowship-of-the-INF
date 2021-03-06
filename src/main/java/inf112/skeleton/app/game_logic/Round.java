package inf112.skeleton.app.game_logic;

import inf112.skeleton.app.grid_objects.FlagPiece;
import inf112.skeleton.app.grid_objects.RepairPiece;
import inf112.skeleton.app.player.AIPlayer;
import inf112.skeleton.app.player.Player;

/**
 * The rounds within a RoboRally game. Consists of 5 phases.
 */
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

    /*
    Getters
     */
    public Phase getPhase() {
        return this.phase;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public int getPhaseNr() {
        return this.phaseNr;
    }

    /*
    Setters
     */
    public void setRoundNumber(int number) {
        this.roundNumber = number;
    }


    /**
     * Starts a new round.
     * Gives every player in the game a new hand of cards with respect to their damage
     * A players locked cards will carry over from last round and be automatically put
     * in that players list of selected cards (from right to left).
     */
    public void startRound() {
        for (int i = 0; i < game.getListOfPlayers().length; i++) {
            Player player = game.getListOfPlayers()[i];
            if(player.isPermanentlyDead()){
                continue;
            }
            player.setPlayerHandDeck(game.getGameDeck().drawHand(player.getPlayerHandDeck(), player.getDamage()));
            player.removeSelectedCards();
            int numberOfLockedCards = player.getLockedCards().size();
            for (int j = 0; j < numberOfLockedCards; j++) {
                int number = 5 - numberOfLockedCards;
                player.getSelectedCards()[number + j] = (player.getLockedCards().get(j));
            }
        }
        lockInCardsForComputers(true);
    }

    /**
     * Execute the next phase
     */
    public void nextPhase() {
        if (phaseNr >= NUMBER_OF_PHASES) {
            return;
        }
        phase.executePhase(phaseNr);
        phaseNr++;
    }

    /**
     * When the round is done, check if there are any players in power down mode.
     * If there are any, and they still have lives left, then take them out of power down mode.
     * Respawn any players that died during the round.
     */
    public void finishRound() {
        for (int i = 0; i < game.getListOfPlayers().length; i++) {
            Player player = game.getListOfPlayers()[i];
            if(!player.isPermanentlyDead()){
                player.setLockedIn(false);
            }
            // Repairs
            checkForRepair(player);
            if (player.isPowerDownMode() && player.getLives() >= 0) {
                player.setPowerDownMode(false);
            }
        }
        respawnPlayers();
    }

    /**
     * Checks if a player is standing on a single-wrench repair field or a flag field
     * at the end of a round. If so, discard 1 damage token.
     *
     * @param player player (robot) being checked.
     */
    public void checkForRepair(Player player) {
        if (player.isDead()) return;
        // Checks if player is standing on a field which qualifies for a repair.
        if ((game.getLogicGrid().getPieceType(player.getPos(), RepairPiece.class) != null) ||
                (game.getLogicGrid().getPieceType(player.getPos(), FlagPiece.class) != null)) {
            player.repairDamage(1);
        }
    }

    /**
     * Lock in cards for computer players.
     * Press 0 to do it manually (Left CTRL + 0 to do one player at a time).
     */
    public void lockInCardsForComputers(boolean lockInForAll){
        for (Player player : game.getListOfPlayers()) {
            if(player instanceof AIPlayer && !player.hasLockedIn() && !player.isPermanentlyDead()) {
                AIPlayer aiPlayer = (AIPlayer) player;
                aiPlayer.pickCards();
                if(!lockInForAll){return;}
            }
        }
    }

    /**
     * Respawn any robots that died during the phase in the order they died in (i.e. the first robot to die is the
     * first robot to respawn). If player 1 died, any player that died before him is respawned first and any player that
     * died after player 1 must wait to respawn until player 1 has confirmed the respawn position and direction.
     * Then another call to this method is made from the GameScreen class to respawn the remaining players.
     */
    public void respawnPlayers(){
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        for(int i = 0; i < game.getDeadPlayers().size(); i++){
            Player deadPlayer = game.getDeadPlayers().get(i);
            if(deadPlayer.getPlayerNumber()==1){
                game.executeMoves(moves);
                deadPlayer.checkForRespawn(moves);
                game.getDeadPlayers().remove(deadPlayer);
                game.setChoosingRespawn(true);
                return;
            }
            deadPlayer.checkForRespawn(moves);
            game.getDeadPlayers().remove(deadPlayer);
            i--;
        }
        game.executeMoves(moves);
    }
}
