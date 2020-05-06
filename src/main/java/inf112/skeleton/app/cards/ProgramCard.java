package inf112.skeleton.app.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * The programcards to be used in the game.
 */
public class ProgramCard implements IProgramCard {

    private int priority;
    private CardType cardType;
    private Texture texture;
    private int movement = 0;

    public ProgramCard(int priority, CardType cardType) {
        this.cardType = cardType;
        this.priority = priority;
        createImage();
    }

    /*
    Getters
     */
    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public CardType getCommand() {
        return cardType;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    public int getMovement() {
        return movement;
    }


    @Override
    public String toString() {
        return "ProgramCard{" + cardType + ", " + priority + "} ";
    }


    private void createImage() {
        switch (cardType) {
            case MOVE1:
                texture = new Texture(Gdx.files.internal("ui/cards/cardmove1.png"));
                movement = 1;
                break;
            case MOVE2:
                texture = new Texture(Gdx.files.internal("ui/cards/cardmove2.png"));
                movement = 2;
                break;
            case MOVE3:
                texture = new Texture(Gdx.files.internal("ui/cards/cardmove3.png"));
                movement = 3;
                break;
            case BACKUP:
                texture = new Texture(Gdx.files.internal("ui/cards/cardbackup.png"));
                movement = -1;
                break;
            case ROTATERIGHT:
                texture = new Texture(Gdx.files.internal("ui/cards/cardrturn.png"));
                break;
            case ROTATELEFT:
                texture = new Texture(Gdx.files.internal("ui/cards/cardlturn.png"));
                break;
            case UTURN:
                texture = new Texture(Gdx.files.internal("ui/cards/carduturn.png"));
                break;
            default:
                break;
        }
    }



}
