package inf112.skeleton.app.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.Cards.ProgramCard;
import inf112.skeleton.app.Deck.Deck;
import inf112.skeleton.app.Deck.GameDeck;
import inf112.skeleton.app.Screens.CardButton;
import inf112.skeleton.app.Screens.GameScreen;
import java.util.ArrayList;

public class UIScreen{
    private Stage stage;
    private float width;
    private GameScreen gameScreen;
    private float selectedCardPosX;
    private final float selectedCardPosY = 75;
    private CardButton cardButton;

    public UIScreen(float width, GameDeck gameDeck, GameScreen gameScreen) {
        this.width = width;
        this.gameScreen = gameScreen;
        selectedCardPosX =  width +50;
        stage = new Stage();
        Texture texture = new Texture(Gdx.files.internal("lockinbutton.png"));
        System.out.println(width);
        ImageButton lockInButton = createButton(texture, 1, width * 2 - 800, 200);
        lockInButtonPressed(lockInButton);
        for (int i = 0; i < 5; i++) {
            texture = new Texture(Gdx.files.internal("cardslot.png"));
            float scale = 0.51f;
            float posX = selectedCardPosX + i * 550;
            createImage(texture, scale, posX, selectedCardPosY);
        }
        createCardButtons(gameDeck.drawHand(gameDeck.getDrawDeck(), 0));
    }

    public Stage getStage() {
        return stage;
    }

//TODO replace 9 with decksize (and fix decksize)
    public void createCardButtons(Deck deck) {
        for (int i = 0; i < 9; i++) {
            ProgramCard card = deck.getCard(i);
            float posY = 2500;
            float distBetweenCards = card.getTexture().getWidth() * 1.4f + 50;
            float posX = width + 50 + distBetweenCards * i;
            if (i > 4) {
                posY -= 800;
                posX = width + 50 + distBetweenCards * (i - 5);
            }
            Table table = new Table();
            table.setPosition(posX, posY);
            cardButton = new CardButton(card, posX, posY, selectedCardPosX, selectedCardPosY);
            stage.addActor(cardButton.getTable());
        }
    }

    public ImageButton createButton(Texture texture, float scale, float posX, float posY) {
        TextureRegion myTextureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        ImageButton button = new ImageButton(myTexRegionDrawable);
        button.getImage().scaleBy(scale, scale);
        button.scaleBy(scale);
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

