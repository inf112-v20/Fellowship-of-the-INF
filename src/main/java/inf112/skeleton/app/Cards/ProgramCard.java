package inf112.skeleton.app.Cards;

import java.awt.image.BufferedImage;

public class ProgramCard implements IProgramCard{

    private int priority;
    private CardType cardType;
    private BufferedImage image;

    public ProgramCard(int priority, CardType cardType) {
        this.cardType = cardType;
        this.priority = priority;
        createImage();
    }

    //TODO get the correct png file according to the cardType
    private void createImage() {
        switch (cardType) {
            case MOVE1:
                //....
                break;
            case MOVE2:
                break;
            case MOVE3:
                break;
            case BACKUP:
                break;
            case ROTATERIGHT:
                break;
            case ROTATELEFT:
                break;
            case UTURN:
                break;
        }
        //Then maybe write priority
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
    public BufferedImage getImage() {
        return image;
    }

}
