package inf112.skeleton.app.game_logic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.player.Player;
import inf112.skeleton.app.screens.GameScreen;

import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private LogicGrid logicGrid;
    private final int NUMBER_OF_PLAYERS = 4;
    private GameDeck gameDeck;
    private Player player1;
    private Player[] playerList = new Player[NUMBER_OF_PLAYERS];;
    private int roundNumber = 0;
    private Round round;
    private Queue<MovesToExecuteSimultaneously> moves = new LinkedList<>();
    private GameScreen gameScreen;


    public Game(LogicGrid logicGrid, GameScreen gameScreen) {
        this.logicGrid = logicGrid;
        this.gameScreen = gameScreen;
        this.gameDeck = new GameDeck(); //make sure this is initialized before players
        this.player1 = new Player(1, this);
        playerList[0] = player1;
        logicGrid.placeNewPlayerPieceOnMap(player1.getPlayerPiece()); //place the new player piece on logic grid
        initiateComputerPlayers();
    }

    public void initiateComputerPlayers() {
        for (int playerNumber = 2; playerNumber <= NUMBER_OF_PLAYERS; playerNumber++) {
            Player playerToBeInitiated = new Player(playerNumber, this);
            playerList[playerNumber - 1] = playerToBeInitiated;
            logicGrid.placeNewPlayerPieceOnMap(playerToBeInitiated.getPlayerPiece());
        }
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
                logicGrid.movePlayerToNewPosition(oldPos, newPos);
        }
    }

    public LogicGrid getLogicGrid() {
        return logicGrid;
    }

    public void setLogicGrid(LogicGrid logicGrid) {
        this.logicGrid = logicGrid;
    }

    public GameDeck getGameDeck() { return gameDeck; }

    public Player getPlayer() {
        return player1;
    }

    public void setPlayer(Player player) {
        this.player1 = player;
    }

    public Round getRound(){return round;}

    /**
     * Handles keyboard input for manually moving Player 1 around.
     */
    public void handleKeyBoardInput() {
        Move rotateMove = new Move(player1); //initiate possible rotateMove to be done
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();//initiate moves to be done
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player1.tryToGo(player1.getPlayerPiece().getDir(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player1.tryToGo(player1.getPlayerPiece().getDir().getOppositeDirection(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player1.turnPlayerLeft();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player1.turnPlayerRight();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player1.turnPlayerAround();
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
            if(player1.isOnConveyorBelt()) {
                BoardElementsMove.moveConveyorBelt(player1, this, false, moves);
            }
        }
        rotateMove.updateMove(player1); //complete rotateMove object
        moves.add(rotateMove);
        //first player moves get executed
        performMoves(moves); //execute moves if there are any
        for (Move move : moves) {
            gameScreen.redrawPlayer(move); //redraw player if it needs to be redrawn
        }
        moves.clear();

        //second player moves get handled
        Player player2 = playerList[1];
        Move move2 = new Move(player2);
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player2.tryToGo(player2.getPlayerPiece().getDir(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player2.tryToGo(player2.getPlayerPiece().getDir().getOppositeDirection(), moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            player2.turnPlayerLeft();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player2.turnPlayerRight();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player2.turnPlayerAround();
        }
        move2.updateMove(player2); //complete move object
        moves.add(move2);
        //execution of player 2 moves
        performMoves(moves); //execute moves if there are any
        for (Move move : moves) {
            gameScreen.redrawPlayer(move); //redraw player if it needs to be redrawn
        }
        moves.clear();
    }


    public Player[] getListOfPlayers() {
        return playerList;
    }


    public void executeRound() {
        // If there are moves to execute, then don't start new round
        this.round = new Round(this);
        if (!moves.isEmpty())
            return;
        //let computer players pick the first five cards as their selected
        for (int playerNumber = 2; playerNumber <= 4; playerNumber++) {
            playerList[playerNumber - 1].pickFirstFiveCards();
        }
        roundNumber++;
        round.setRoundNumber(roundNumber);
        //check all players have hand
        round.startRound();
        round.finishRound();
    }

    public Queue<MovesToExecuteSimultaneously> getMoves() {
        return moves;
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
     * @param pos the position of a player
     * @return the player at that position
     */
    public Player getPlayerAt(Position pos){
        for (Player player : playerList) {
            if(player.getPos().equals(pos)){
                return player;
            }
        }
        return null; //no player in position
    }
}