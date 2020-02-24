package inf112.skeleton.app.GridObjects;

import inf112.skeleton.app.Grid.PieceGrid;

public class GameLogic  {

    private PieceGrid grid;
    private GameDeck gameDeck;

    public GameLogic(PieceGrid grid) {
        this.grid = grid;

    }

    public PieceGrid getGrid() {
        return grid;
    }

    public void setGrid(PieceGrid grid) {
        this.grid = grid;
    }

    public GameDeck getGameDeck() {
        return gameDeck;
    }

    public void setGameDeck(GameDeck gameDeck) {
        this.gameDeck = gameDeck;
    }
}
