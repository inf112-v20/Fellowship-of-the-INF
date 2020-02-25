package inf112.skeleton.app.Grid;

import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.Deck.GameDeck;
import inf112.skeleton.app.Player;

/**
 * This is the class representing the "bridge" between frontend and backend.
 * All backend classes (ie Player) can get information about the PieceGrid from here,
 * additionally all frontend classes (primarily GameScreen) can get info about the Tiled grid.
 */
public class GameLogic  {

    private PieceGrid grid;
    private GameDeck gameDeck;
    private Player player;
    TiledMap tiledMap;

    public GameLogic(int width, int height, TiledMap tiledMap) {
        this.grid = new PieceGrid(width, height, tiledMap);
        this.player = new Player(1, grid);
        this.tiledMap = tiledMap;
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
        return tiledMap;
    }

    public void setMap(TiledMap map) {
        this.tiledMap = map;
    }

    public int getBoardWidth() {
        return grid.getWidth();
    }

    public int getBoardHeight() {
        return grid.getHeight();
    }
}
