package inf112.skeleton.app.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A little class to move the create of player texture away from player
 * The playerSprites.png file is a 900*24000 dpi image, which needs to be split into three,
 * as each 300*300 image is an appearance of the player.
 */
public class TextureMaker {

    public static TextureRegion getPlayerTextureRegion(int playerNumber, int state) {
        Texture playerTexture = new Texture("playerSprites.png");
        TextureRegion region = new TextureRegion(playerTexture);
        //split image into texture regions
        TextureRegion[][] regions = region.split(300,300);

        TextureRegion alive = regions[playerNumber-1][0];
        TextureRegion dead = regions[playerNumber-1][1];
        TextureRegion won = regions[playerNumber-1][2];

        //this can be used later to change the state of the player
        switch(state) {
            case 0:
                return alive;
            case 1:
                return dead;
            case 2:
                return won;
            default:
                return null;
        }
    }
}
