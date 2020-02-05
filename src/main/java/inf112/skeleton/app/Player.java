package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;

public class Player {

    private Vector2 pos;
    private TiledMapTileLayer.Cell playerCell;
    private TiledMapTileLayer.Cell deadPlayerCell;
    private TiledMapTileLayer.Cell wonPlayerCell;

    public Player(int playerNumber) {
        pos = new Vector2();
        pos.x = 0;
        pos.y = 0;

        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,1)));
        wonPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,2)));
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setDir(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

    public TiledMapTileLayer.Cell getPlayerCell() {
        return playerCell;
    }
}
