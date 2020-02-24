package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.Grid.PieceGrid;

/**
 *
 */
public class Player {

    private Vector2 pos; //position of player
    private TiledMapTileLayer.Cell playerCell; //cell for normal player
    private TiledMapTileLayer.Cell deadPlayerCell; //cell for dead player looks
    private TiledMapTileLayer.Cell wonPlayerCell; //cell for player who has won looks
    private PieceGrid grid;

    public Player(int playerNumber, PieceGrid grid) {
        pos = new Vector2();
        //place player at the bottom left corner
        pos.x = 0;
        pos.y = 0;
        this.grid = grid;

        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,1)));
        wonPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,2)));
    }

    /**
     * Getter for position
     * @return position of player
     */
    public Vector2 getPos() {
        return pos;
    }

    /**
     * Setter for position of player
     * @param x new x position
     * @param y new y position
     */
    public void setPos(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

    /**
     * Getter for player
     * @return player
     */
    public TiledMapTileLayer.Cell getPlayerCell() {
        return playerCell;
    }
}