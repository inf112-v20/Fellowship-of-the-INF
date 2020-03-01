package inf112.skeleton.app.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.Cards.ProgramCard;
import inf112.skeleton.app.Deck.Deck;
import inf112.skeleton.app.Deck.GameDeck;
import java.util.ArrayList;

public class UIScreen{
    private Stage stage;
    private float width;
    private float height;
    private GameScreen gameScreen;
    private CardButton cardButton;
    private Deck playerHandDeck;

    public UIScreen(float width, GameDeck gameDeck, GameScreen gameScreen) {
        this.width = width;
        height = width * 0.5f;
        this.gameScreen = gameScreen;
        stage = new Stage();
        playerHandDeck = gameDeck.dealHand(0);
        createLockInButton();
        cardButton = new CardButton(playerHandDeck, width, height, stage);
    }

    public Stage getStage() {
        return stage;
    }

    public void createLockInButton(){
        Texture texture = new Texture(Gdx.files.internal("lockinbutton.png"));
        ImageButton lockInButton = createButton(texture, 1, width * 0.90f, height*0.1f);
        lockInButtonPressed(lockInButton);
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
            if(cardButton.getSelectedCards()[i] == null){System.out.println("Not enough cards");return null;}
        }
        ArrayList<ProgramCard> selectedProgramCards = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            selectedProgramCards.add(cardButton.getSelectedCards()[i]);
        }
        return selectedProgramCards;
    }
}

