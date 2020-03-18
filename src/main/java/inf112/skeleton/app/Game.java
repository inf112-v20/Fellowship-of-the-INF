package inf112.skeleton.app;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
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
    private int roundNumber = 0;
    private Round round;
    private final int NUMBER_OF_PLAYERS = 4;
    private Queue<ArrayList<Move>> moves;
    private GameScreen gameScreen;


    public Game(LogicGrid logicGrid, GameScreen gameScreen) {
        this.logicGrid = logicGrid;
        this.gameScreen = gameScreen;
        this.gameDeck = new GameDeck(); //make sure this is initialized before players
        this.player1 = new Player(1, this);
        this.playerList = new Player[NUMBER_OF_PLAYERS];
        playerList[0] = player1;
        logicGrid.placeNewPlayerPieceOnMap(player1.getPlayerPiece()); //place the new player piece on logic grid
        initiateComputerPlayers();
        this.moves = new LinkedList<>();
    }

    public void initiateComputerPlayers() {
        for (int playerNumber = 2; playerNumber <= NUMBER_OF_PLAYERS; playerNumber++) {
            Player playerToBeInitiated = new Player(playerNumber, this);
            playerList[playerNumber - 1] = playerToBeInitiated;
            logicGrid.placeNewPlayerPieceOnMap(playerToBeInitiated.getPlayerPiece());
        }
    }

    public void performMoves(ArrayList<Move> moves) {
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

    public void setGameDeck(GameDeck gameDeck) {
        this.gameDeck = gameDeck;
    }

    public Player getPlayer() {
        return player1;
    }

    public void setPlayer(Player player) {
        this.player1 = player;
    }

    public Round getRound(){return round;}

    /**
     * Handles keyboard input
     */
    public void handleKeyBoardInput() {
        Move move = new Move(player1); //initiate move to be done
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player1.tryToGo(player1.getPlayerPiece().getDir());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player1.tryToGo(player1.getPlayerPiece().getDir().getOppositeDirection());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player1.turnPlayerLeft();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player1.turnPlayerRight();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
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
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if(player1.isOnConveyorBelt()) {
                BoardElementsMove.moveConveyorBelt(player1.getCurrentBoardPiece(), player1, logicGrid);
                player1.setConveyorBeltMove(true);
            }
            else if(player1.isOnExpressBelt()){
                BoardElementsMove.moveExpressBelt(player1.getCurrentBoardPiece(), player1, logicGrid);
                player1.setConveyorBeltMove(true);
            }
        }
        move.updateMove(player1); //complete move object
        if (move.isNotStandStill()) {
            performMoves(move.toArrayList()); //execute move if there is one
            gameScreen.redrawPlayer(move); //redraw player if it needs to be redrawn
        }

        Player player2 = playerList[1];
        Move move2 = new Move(player2);
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player2.tryToGo(player2.getPlayerPiece().getDir());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player2.tryToGo(player2.getPlayerPiece().getDir().getOppositeDirection());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            player2.turnPlayerLeft();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player2.turnPlayerRight();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player2.turnPlayerAround();
        }
        move2.updateMove(player2); //complete move object
        if (move2.isNotStandStill()) {
            performMoves(move2.toArrayList()); //execute move if there is one
            gameScreen.redrawPlayer(move2); //redraw player if it needs to be redrawn
        }


    }

    public void executePlayerHand(ArrayList<ProgramCard> hand) {
        for (ProgramCard programCard : hand) {
            convertCardToPlayerMove(programCard);
        }
    }


    /**
     * Gives the player a command, based on the program card
     *
     * @param programCard to convert to player move
     */
    public void convertCardToPlayerMove(ProgramCard programCard) {
        switch (programCard.getCommand()) {
            case MOVE1:
                player1.tryToGo(player1.getPlayerPiece().getDir());
                break;
            case MOVE2:
                player1.tryToGo(player1.getPlayerPiece().getDir());
                player1.tryToGo(player1.getPlayerPiece().getDir());
                break;
            case MOVE3:
                player1.tryToGo(player1.getPlayerPiece().getDir());
                player1.tryToGo(player1.getPlayerPiece().getDir());
                player1.tryToGo(player1.getPlayerPiece().getDir());
                break;
            case UTURN:
                player1.turnPlayerAround();
                break;
            case BACKUP:
                player1.turnPlayerAround();
                player1.tryToGo(player1.getPlayerPiece().getDir());
                player1.turnPlayerAround();
                break;
            case ROTATELEFT:
                player1.turnPlayerLeft();
                break;
            case ROTATERIGHT:
                player1.turnPlayerRight();
                break;
            default:
                //TODO error handling as default maybe?
                break;
        }
    }

    public Player[] getListOfPlayers() { return playerList; }

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
    }

    public Queue<ArrayList<Move>> getMoves() {
        return moves;
    }

    /**
     * Executes both the backend and frontend version of the move
     * @param moves to execute
     */
    public void executeMoves(ArrayList<Move> moves) {
        performMoves(moves); //backend execution
        this.moves.add(moves);//add to list of things to do in frontend
       // gameScreen.executeMove(move); //frontend execution
    }
}