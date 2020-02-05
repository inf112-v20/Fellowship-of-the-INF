package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class TextureMaker {

    public static TextureRegion getPlayerTextureRegion(int playerNumber, int state) {
        Texture playerTexture = new Texture("player.png");
        TextureRegion region = new TextureRegion(playerTexture);
        TextureRegion[][] regions = region.split(300,300);

        TextureRegion alive = regions[0][0];
        TextureRegion dead = regions[0][1];
        TextureRegion won = regions[0][2];

        switch(state) {
            case 0: return alive;
            case 1: return dead;
            case 2: return won;
        }
        return null;
    }
}
