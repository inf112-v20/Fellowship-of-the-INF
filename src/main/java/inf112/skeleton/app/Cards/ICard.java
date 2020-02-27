package inf112.skeleton.app.Cards;

import java.awt.image.BufferedImage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 *
 * ICard
 * Interface class for the programming cards.
 *
 */
public interface ICard{

    /**
     * Get the png file of the card.
     *
     */
    public Texture getTexture();

}
