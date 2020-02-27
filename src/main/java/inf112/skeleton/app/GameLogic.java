package inf112.skeleton.app;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.skeleton.app.Cards.ProgramCard;
import inf112.skeleton.app.Deck.GameDeck;
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.PieceGrid;

import java.util.ArrayList;

public class GameLogic  {
    private PieceGrid logicGrid;
    private GameDeck gameDeck;
    private Player player;
    private int playerLayerIndex;


    public GameLogic(PieceGrid logicGrid) {
        this.logicGrid = logicGrid;
        this.player = new Player(1, this);
        logicGrid.placeNewPlayerPieceOnMap(player.getPlayerPiece()); //place the new player piece on logic grid
    }

    public PieceGrid getLogicGrid() {
        return logicGrid;
    }

    public void setLogicGrid(PieceGrid logicGrid) {
        this.logicGrid = logicGrid;
    }

    public GameDeck getGameDeck() {
        return gameDeck;
    }

    public void setGameDeck(GameDeck gameDeck) {
        this.gameDeck = gameDeck;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setNewPlayerPos(int x, int y) {

    }

    /**
     * Handles player input
     */
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.tryToGo(Direction.NORTH);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player.tryToGo(Direction.SOUTH);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player.tryToGo(Direction.WEST);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player.tryToGo(Direction.EAST);
        }
    }

    public void executePlayerHand(ArrayList<ProgramCard> hand) {
        for (ProgramCard programCard : hand) {
            convertCardToPlayerMove(programCard);
        }
    }



    /**
     * Gives the player a command, based on the program card
     * @param programCard to convert to player move
     */
    public void convertCardToPlayerMove(ProgramCard programCard) {
        switch (programCard.getCommand()) {
            case MOVE1:
                player.tryToGo(player.getPlayerPiece().getDir());
                break;
            case MOVE2:
                player.tryToGo(player.getPlayerPiece().getDir());
                player.tryToGo(player.getPlayerPiece().getDir());
                break;
            case MOVE3:
                player.tryToGo(player.getPlayerPiece().getDir());
                player.tryToGo(player.getPlayerPiece().getDir());
                player.tryToGo(player.getPlayerPiece().getDir());
                break;
            case UTURN:
                player.turnPlayerAround();
                break;
            case BACKUP:
                player.turnPlayerAround();
                player.tryToGo(player.getPlayerPiece().getDir());
                player.turnPlayerAround();
                break;
            case ROTATELEFT:
                player.turnPlayerLeft();
                break;
            case ROTATERIGHT:
                player.turnPlayerRight();
                break;
        }
    }


}