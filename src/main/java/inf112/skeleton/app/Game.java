package inf112.skeleton.app;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Map;
import inf112.skeleton.app.player.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game {
    private Map map;
    private GameDeck gameDeck;
    private Player player1;
    private Player[] playerList;
    private Round round;
    private final int NUMBER_OF_PLAYERS = 4;


    public Game(Map map) {
        this.map = map;
        this.gameDeck = new GameDeck(); //make sure this is initialized before players
        this.player1 = new Player(1, this);
        this.playerList = new Player[NUMBER_OF_PLAYERS];
        playerList[0] = player1;
        map.placeNewPlayerPieceOnMap(player1.getPlayerPiece()); //place the new player piece on logic grid
        initiateComputerPlayers();
        this.round = new Round(this);
    }

    public void initiateComputerPlayers() {
        for (int playerNumber = 2; playerNumber <= NUMBER_OF_PLAYERS; playerNumber++) {
            Player playerToBeInitiated = new Player(playerNumber, this);
            playerList[playerNumber - 1] = playerToBeInitiated;
            map.placeNewPlayerPieceOnMap(playerToBeInitiated.getPlayerPiece());
        }
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

    public void setNewPlayerPos(int x, int y) {

    }

    /**
     * Handles player input
     */
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player1.tryToGo(Direction.NORTH);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player1.tryToGo(Direction.SOUTH);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player1.tryToGo(Direction.WEST);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player1.tryToGo(Direction.EAST);
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
        //let computer players pick the first five cards as their selected
        for (int playerNumber = 2; playerNumber <= 4; playerNumber++) {
            playerList[playerNumber - 1].pickFirstFiveCards();
        }
        round.startRound();
    }
}