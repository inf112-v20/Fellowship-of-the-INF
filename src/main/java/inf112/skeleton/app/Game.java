package inf112.skeleton.app;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Map;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.player.Player;
import inf112.skeleton.app.screens.GameScreen;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Game {
    private Map map;
    private GameDeck gameDeck;
    private Player player1;
    private Player[] playerList;
    private Round round;
    private final int NUMBER_OF_PLAYERS = 4;
    private Queue<Move> moves;
    private GameScreen gameScreen;


    public Game(Map map, GameScreen gameScreen) {
        this.map = map;
        this.gameScreen = gameScreen;
        this.gameDeck = new GameDeck(); //make sure this is initialized before players
        this.player1 = new Player(1, this);
        this.playerList = new Player[NUMBER_OF_PLAYERS];
        playerList[0] = player1;
        map.placeNewPlayerPieceOnMap(player1.getPlayerPiece()); //place the new player piece on logic grid
        initiateComputerPlayers();
        this.round = new Round(this);
        this.moves = new LinkedList<>();
    }

    public void initiateComputerPlayers() {
        for (int playerNumber = 2; playerNumber <= NUMBER_OF_PLAYERS; playerNumber++) {
            Player playerToBeInitiated = new Player(playerNumber, this);
            playerList[playerNumber - 1] = playerToBeInitiated;
            map.placeNewPlayerPieceOnMap(playerToBeInitiated.getPlayerPiece());
        }
    }

    public void performMove(Move move) {
        Position oldPos = move.getOldPos();
        Position newPos = move.getNewPos();
        map.movePlayerToNewPosition(oldPos, newPos);;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public GameDeck getGameDeck() {
        return gameDeck;
    }

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
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player1.tryToGo(player1.getPlayerPiece().getDir());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player1.turnPlayerAround();
            player1.tryToGo(player1.getPlayerPiece().getDir());
            player1.turnPlayerAround();
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

    public Player[] getListOfPlayers() {
        return playerList;
    }

    public void executeRound() {
        // If there are moves to execute, then don't start new round
        if (!moves.isEmpty())
            return;
        //let computer players pick the first five cards as their selected
        for (int playerNumber = 2; playerNumber <= 4; playerNumber++) {
            playerList[playerNumber - 1].pickFirstFiveCards();
        }
        //check all players have hand
        round.startRound();
    }

    public Queue<Move> getMoves() {
        return moves;
    }

    /**
     * Executes both the backend and frontend version of the move
     * @param move to execute
     */
    public void executeMove(Move move) {
        performMove(move); //backend execution
        moves.add(move);//add to list of things to do in frontend
       // gameScreen.executeMove(move); //frontend execution
    }
}