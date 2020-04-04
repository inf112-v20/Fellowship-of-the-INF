package inf112.skeleton.app.game_logic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.LaserSourcePiece;
import inf112.skeleton.app.player.AIPlayer;
import inf112.skeleton.app.player.Player;
import inf112.skeleton.app.screens.GameScreen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private LogicGrid logicGrid;
    private GameDeck gameDeck;
    private Player player1;
    private Player[] playerList;
    private int numberOfPlayers;
    private int roundNumber = 0;
    private Round round;
    private Queue<MovesToExecuteSimultaneously> moves;
    private GameScreen gameScreen;
    private Player playerRemaining;
    private ArrayList<Player> deadPlayers;
    private boolean phaseDone = false;
    private boolean autoStartNextPhase = false;

    public Game(LogicGrid logicGrid, GameScreen gameScreen, int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.logicGrid = logicGrid;
        this.gameScreen = gameScreen;
        this.gameDeck = new GameDeck(); //make sure this is initialized before players
        this.player1 = new Player(1, this);
        this.playerList = new Player[numberOfPlayers];
        this.deadPlayers = new ArrayList<>();
        playerList[0] = player1;
        logicGrid.placeNewPlayerPieceOnMap(player1.getPlayerPiece()); //place the new player piece on logic grid
        initiateComputerPlayers();
        this.moves = new LinkedList<>();
    }

    public void initiateComputerPlayers() {
        for (int playerNumber = 2; playerNumber <= numberOfPlayers; playerNumber++) {
            Player playerToBeInitiated = new AIPlayer(playerNumber, this);
            playerList[playerNumber - 1] = playerToBeInitiated;
            logicGrid.placeNewPlayerPieceOnMap(playerToBeInitiated.getPlayerPiece());
        }
    }


    public LogicGrid getLogicGrid() {
        return logicGrid;
    }

    public void setLogicGrid(LogicGrid logicGrid) {
        this.logicGrid = logicGrid;
    }

    public GameDeck getGameDeck() {
        return gameDeck;
    }

    public Player getPlayer() {
        return player1;
    }

    public void setPlayer(Player player) {
        this.player1 = player;
    }

    public Round getRound() {
        return round;
    }

    /**
     * Handles keyboard input for manually moving Player 1 around.
     */
    public void handleKeyBoardInput() {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();//initiate moves to be done
        Player player2 = player1;
        if (numberOfPlayers > 1) player2 = playerList[1];

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player1.setKeyInput(true);
            player1.tryToGo(player1.getPlayerPiece().getDir(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player1.setKeyInput(true);
            player1.tryToGo(player1.getPlayerPiece().getDir().getOppositeDirection(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player1.turnPlayerLeft(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player1.turnPlayerRight(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player1.turnPlayerAround(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            player1.shootLaser();
            gameScreen.shootRobotLasers();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) { //only used for testing board lasers
            gameScreen.blinkBoardLasers();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            player1.takeDamage(1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            player1.repairDamage(1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            player1.gainLife();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            player1.loseLife();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            player1.visitedCheckpoint();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            player1.removeCheckpoint();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (player1.isOnConveyorBelt()) {
                BoardElementsMove.moveConveyorBelt(player1, this, false, moves);
            }
        }


        //second player moves get handled
        else if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player2.setKeyInput(true);
            player2.tryToGo(player2.getPlayerPiece().getDir(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player2.setKeyInput(true);
            player2.tryToGo(player2.getPlayerPiece().getDir().getOppositeDirection(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            player2.turnPlayerLeft(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player2.turnPlayerRight(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player2.turnPlayerAround(moves);
        }
        //execution of moves
        performMoves(moves); //execute moves if there are any
        for (Move move : moves) {
            gameScreen.redrawPlayer(move); //redraw player if it needs to be redrawn
        }
        moves.clear();

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                round.lockInCardsForComputers(false);
            }
            else{
                round.lockInCardsForComputers(true);
            }
        }

    }

    public void handleNonGameLogicKeyBoardInput(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            autoStartNextPhase = !autoStartNextPhase;
            if(autoStartNextPhase){
                System.out.println("Turning autostart of phases on");
            }
            else {
                System.out.println("Turning autostart of phases off");
            }
        }
    }

    public Player[] getListOfPlayers() {
        return playerList;
    }

    public void executeRound() {
        // If there are moves to execute, then don't start new round
        this.round = new Round(this);
        roundNumber++;
        round.setRoundNumber(roundNumber);
        round.startRound();
    }

    public Queue<MovesToExecuteSimultaneously> getMoves() {
        return moves;
    }

    /**
     * Perform moves for robots
     *
     * @param moves All moves to execute
     */
    public void performMoves(MovesToExecuteSimultaneously moves) {
        for (Move move : moves) {
            Position oldPos = move.getOldPos();
            Position newPos = move.getNewPos();
            if (!oldPos.equals(newPos)) //if the positions are not the same, then move the player on the board
                logicGrid.movePlayerToNewPosition(move.getPlayerPiece(), oldPos, newPos);
        }
    }

    /**
     * Executes both the backend and frontend version of the move
     *
     * @param moves to execute
     */
    public void executeMoves(MovesToExecuteSimultaneously moves) {
        performMoves(moves); //backend execution
        this.moves.add(moves);//add to list of things to do in frontend
    }

    /**
     * Gets a player at a given position
     *
     * @param pos the position of a player
     * @return the player at that position
     */
    public Player getPlayerAt(Position pos) {
        for (Player player : playerList) {
            if (player.getPos().equals(pos)) {
                return player;
            }
        }
        return null; //no player in position
    }

    /**
     * Checks if all robots are done shooting their laser.
     * @return true if robots are done shooting, false otherwise.
     */
    public boolean moreLaserToShoot() {
        for (Player player : playerList) {
            if (!player.getOldLaserPos().equals(new Position(-1, -1))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if there is only one player that hasn't locked in cards for the next round
     * @return true if there is only one player left picking cards, false otherwise.
     */
    public boolean onePlayerLeftToPick(){
        int lockedInPlayers = 0;
        for (Player player : playerList) {
            if (player.hasLockedIn()) {
                lockedInPlayers++;
            }
            else{
                playerRemaining = player;
            }
        }
        return (lockedInPlayers == playerList.length-1);
    }

    /**
     * Get player that hasn't locked card
     * @return the one player that hasn't locked in cards for the next round
     */
    public Player getPlayerRemaining(){ return playerRemaining; }

    public ArrayList<Player> getDeadPlayers(){return deadPlayers;}

    public void setPhaseDone(boolean bool){ phaseDone = bool; }

    public  boolean isPhaseDone(){return phaseDone;}

    public boolean isAutoStartNextPhaseOn(){return autoStartNextPhase;}

}