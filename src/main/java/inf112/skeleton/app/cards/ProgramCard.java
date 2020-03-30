package inf112.skeleton.app.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ProgramCard implements IProgramCard {

    private int priority;
    private CardType cardType;
    private Texture texture;
    private boolean moveCard = true;
    private int movement = 0;

    public ProgramCard(int priority, CardType cardType) {
        this.cardType = cardType;
        this.priority = priority;
        createImage();
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
                movement = 1;
                break;
            case ROTATERIGHT:
                texture = new Texture(Gdx.files.internal("ui/cards/cardrturn.png"));
                moveCard = false;
                break;
            case ROTATELEFT:
                texture = new Texture(Gdx.files.internal("ui/cards/cardlturn.png"));
                moveCard = false;
                break;
            case UTURN:
                texture = new Texture(Gdx.files.internal("ui/cards/carduturn.png"));
                moveCard = false;
                break;
            default:
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

    @Override
    public String toString() {
        //TODO Remember to change this back
        return cardType + ": " + priority + " ";

        /*
        return "ProgramCard{" +
                "priority=" + priority +
                ", cardType=" + cardType +
                '}';

         */
    }

    public boolean isMoveCard() {
        return moveCard;
    }

    public int getMovement() {
        return movement;
    }


}
