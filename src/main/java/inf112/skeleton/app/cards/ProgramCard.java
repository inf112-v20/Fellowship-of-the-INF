package inf112.skeleton.app.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ProgramCard implements IProgramCard{

    private int priority;
    private CardType cardType;
    private Texture texture;

    public ProgramCard(int priority, CardType cardType) {
        this.cardType = cardType;
        this.priority = priority;
        createImage();
    }


    private void createImage() {
        switch (cardType) {
            case MOVE1:
                texture = new Texture(Gdx.files.internal("cardmove1.png"));
                break;
            case MOVE2:
                texture = new Texture(Gdx.files.internal("cardmove2.png"));
                break;
            case MOVE3:
                texture = new Texture(Gdx.files.internal("cardmove3.png"));
                break;
            case BACKUP:
                texture = new Texture(Gdx.files.internal("cardbackup.png"));
                break;
            case ROTATERIGHT:
                texture = new Texture(Gdx.files.internal("cardrturn.png"));
                break;
            case ROTATELEFT:
                texture = new Texture(Gdx.files.internal("cardlturn.png"));
                break;
            case UTURN:
                texture = new Texture(Gdx.files.internal("carduturn.png"));
                break;
            default:
                //TODO error handling as default maybe?
                break;
        }
    }

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

}
