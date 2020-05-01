package inf112.skeleton.app.game_logic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.ConveyorBeltPiece;
import inf112.skeleton.app.grid_objects.ExpressBeltPiece;
import inf112.skeleton.app.grid_objects.PlayerPiece;
import inf112.skeleton.app.player.AIPlayer;
import inf112.skeleton.app.player.AIPlayer.Difficulty;

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
    private Queue<MovesToExecuteSimultaneously> frontendMoves;
    private GameScreen gameScreen;
    private Player playerRemaining;
    private ArrayList<Player> deadPlayers;
    private boolean phaseDone = false;
    private boolean autoStartNextPhase = false;
    private boolean choosingRespawn;
    private Difficulty difficulty;
    private ArrayList<Player> respawnOrder = new ArrayList<>();
    private String mapName;

    /**
     * The Game object acts like the main control center of the backend side of the game, and also as the bridge between
     * backend and frontent.
     * When robots execute actions in the backend, a list of MovesToExecuteSimultaneously objects is generated.
     * This is then sent to Game, which executed it backend-wise. This is done in the method executeMoves(), which moves
     * robots to their new position in the logic grid, and adds the executed MovesToExecuteSimultaneously object to
     * the queue called frontendMoved.
     * When render() is called in GameScreen, the class calls getMoves() in Game so that it recieves the queue of moves
     * that need to be shown, and then shows the moves and clears the moves that have been shown from frontendMoves.
     *
     * @param logicGrid the grid containing information about the board in the game
     * @param gameScreen screen showing the game
     * @param numberOfPlayers number of players in the game
     * @param difficulty the level of intelligence the AI players have
     */
    public Game(LogicGrid logicGrid, GameScreen gameScreen, int numberOfPlayers, Difficulty difficulty, String mapName) {
        this.numberOfPlayers = numberOfPlayers;
        this.difficulty = difficulty;
        this.logicGrid = logicGrid;
        this.gameScreen = gameScreen;
        this.gameDeck = new GameDeck(); //make sure this is initialized before players
        this.player1 = new Player(1, this);
        this.playerList = new Player[numberOfPlayers];
        this.deadPlayers = new ArrayList<>();
        findMapName(mapName);
        playerList[0] = player1;
        respawnOrder.add(player1);
        logicGrid.placeNewPlayerPieceOnMap(player1.getPlayerPiece()); //place the new player piece on logic grid
        initiateComputerPlayers();
        this.frontendMoves = new LinkedList<>();

    }
    /*
    Getters
     */

    public LogicGrid getLogicGrid() {
        return this.logicGrid;
    }

    public GameDeck getGameDeck() {
        return this.gameDeck;
    }

    public Player getPlayer() {
        return this.player1;
    }

    public Round getRound() {
        return this.round;
    }

    public Player[] getListOfPlayers() {
        return this.playerList;
    }

    public Player getPlayerRemaining(){ return this.playerRemaining; }

    public ArrayList<Player> getDeadPlayers(){return this.deadPlayers;}

    public boolean isPhaseDone(){return this.phaseDone;}

    public boolean isAutoStartNextPhaseOn(){return this.autoStartNextPhase;}

    public boolean isChoosingRespawn(){
        return this.choosingRespawn;
    }

    public ArrayList<Player> getRespawnOrder(){
        return this.respawnOrder;
    }

    public String getMapName(){ return this.mapName;}

    /**
     * This method is called by GameScreen so that it can show the moves that have been executed in backend.
     * @return a queue of the frontend moves that need to be shown
     */
    public Queue<MovesToExecuteSimultaneously> getFrontendMoves() {
        return this.frontendMoves;
    }

    /*
    Setters
     */
    public void setLogicGrid(LogicGrid logicGrid) {
        this.logicGrid = logicGrid;
    }

    public void setPlayer(Player player) {
        this.player1 = player;
    }

    public void setPhaseDone(boolean bool){ this.phaseDone = bool; }

    public void setChoosingRespawn(boolean bool){ this.choosingRespawn = bool; }


    /**
     * The computer players are initiated and added to the playerList and position in the logicGrid
     */
    public void initiateComputerPlayers() {
        for (int playerNumber = 2; playerNumber <= numberOfPlayers; playerNumber++) {
            Player playerToBeInitiated = new AIPlayer(playerNumber, this, difficulty);
            respawnOrder.add(playerToBeInitiated);
            playerList[playerNumber - 1] = playerToBeInitiated;
            logicGrid.placeNewPlayerPieceOnMap(playerToBeInitiated.getPlayerPiece());
        }
    }

    /**
     * Handles keyboard input for manually moving Player 1 and player 2 around
     */
    public void handleKeyBoardInput() {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();//initiate moves to be done
        Player player2 = player1;
        if (numberOfPlayers > 1) player2 = playerList[1];

        PlayerPiece player1Piece = player1.getPlayerPiece();
        PlayerPiece player2Piece = player2.getPlayerPiece();
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player1.setKeyInput(true);
            player1.tryToGo(player1.getPlayerPiece().getDir(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player1.setKeyInput(true);
            player1.tryToGo(player1.getPlayerPiece().getDir().getOppositeDirection(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player1Piece.turnLeft(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player1Piece.turnRight(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player1Piece.turnAround(moves);
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
            boolean playerIsOnConveyorBeltPiece = logicGrid.positionHasPieceType(player1Piece.getPos(), ConveyorBeltPiece.class);
            boolean playerIsOnExpressBeltPiece = logicGrid.positionHasPieceType(player1Piece.getPos(), ExpressBeltPiece.class);
            if (playerIsOnConveyorBeltPiece || playerIsOnExpressBeltPiece) {
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
            player2Piece.turnLeft(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player2Piece.turnRight(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player2Piece.turnAround(moves);
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

    /**
     * Creates a new round, increments the round number and start the new round.
     */
    public void executeRound() {
        // If there are moves to execute, then don't start new round
        this.round = new Round(this);
        roundNumber++;
        round.setRoundNumber(roundNumber);
        round.startRound();
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
        this.frontendMoves.add(moves);//add to list of things to do in frontend
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

    private void findMapName(String mapName){
        for(int i = mapName.length()-1; i >= 0; i--){
            char c = mapName.charAt(i);
            if(c == '/'){
                this.mapName = mapName.substring(i+1, mapName.length()-4);
                return;
            }
        }
    }

}