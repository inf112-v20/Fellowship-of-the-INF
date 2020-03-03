package inf112.skeleton.app.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.Deck;
import inf112.skeleton.app.deck.GameDeck;

import java.util.ArrayList;

public class UIScreen{
    private Stage stage;
    private float width;
    private GameScreen gameScreen;
    private float selectedCardPosX;
    private final float selectedCardPosY = 75;
    private CardButton cardButton;
    private ArrayList<ProgramCard> playerHandDeck;

    public UIScreen(float width, GameDeck gameDeck, GameScreen gameScreen) {
        this.width = width;
        this.gameScreen = gameScreen;
        selectedCardPosX =  width * 1.05f;
        stage = new Stage();
        Texture texture = new Texture(Gdx.files.internal("lockinbutton.png"));
        ImageButton lockInButton = createButton(texture, 1, width * 1.81f, 200);
        lockInButtonPressed(lockInButton);
        for (int i = 0; i < 5; i++) {
            texture = new Texture(Gdx.files.internal("cardslot.png"));
            float scale = 0.51f;
            float posX = selectedCardPosX + i * 550;
            createImage(texture, scale, posX, selectedCardPosY);
        }

        playerHandDeck = gameDeck.drawHand(new ArrayList<ProgramCard>(),0);
        createCardButtons(playerHandDeck);
    }

    public Stage getStage() {
        return stage;
    }

//TODO replace 9 with decksize (and fix decksize)
    public void createCardButtons(ArrayList<ProgramCard> deck) {
        for (int i = 0; i < 9; i++) {
            ProgramCard card = deck.get(i);
            float posY = 2500;
            float posX =  (width * 1.05f) + (i * 500);
            if (i > 4) {
                posY -= 800;
                posX = (width * 1.05f) + ((i-5) * 500);
            }
            cardButton = new CardButton(card, posX, posY, selectedCardPosX, selectedCardPosY);
            stage.addActor(cardButton.getButton());
            stage.addActor(cardButton.getTable());
        }
    }

    public ImageButton createButton(Texture texture, float scale, float posX, float posY) {
        TextureRegion myTextureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        ImageButton button = new ImageButton(myTexRegionDrawable);
        button.getImage().scaleBy(scale);
        button.setPosition(posX, posY);
        stage.addActor(button);
        return button;
    }

    public void createImage(Texture texture, float scale, float posX, float posY) {
        TextureRegion myTextureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        Image image = new Image(myTexRegionDrawable);
        image.scaleBy(scale);
        image.setPosition(posX, posY);
        stage.addActor(image);
    }
    public void lockInButtonPressed(ImageButton lockInButton){
        lockInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.executeLockIn(getSelectedProgramCards());
            }
        });
    }
    public ArrayList<ProgramCard> getSelectedProgramCards(){
        for (int i = 0; i < 5 ; i++) {
            if(cardButton.getSelectedCardButtons()[i] == null){System.out.println("Not enough cards");return null;}
        }
        ArrayList<ProgramCard> selectedProgramCards = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            selectedProgramCards.add(cardButton.getSelectedCardButtons()[i].getProgramCard());
        }
        return selectedProgramCards;
    }
}

