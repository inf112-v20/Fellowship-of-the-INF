package inf112.skeleton.app.Grid;

import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.Deck.GameDeck;
import inf112.skeleton.app.Grid.PieceGrid;
import inf112.skeleton.app.Player;

public class GameLogic  {

    private PieceGrid grid;
    private GameDeck gameDeck;
    private Player player;
    TiledMap map;

    public GameLogic(int width, int height, TiledMap map) {
        this.grid = new PieceGrid(width, height, map);
        this.player = new Player(1, grid);
        this.map = map;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public int getBoardWidth() {
        return grid.getWidth();
    }

    public int getBoardHeight() {
        return grid.getHeight();
    }
}
