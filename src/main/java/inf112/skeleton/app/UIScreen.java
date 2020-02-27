package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.Cards.ProgramCard;
import inf112.skeleton.app.Deck.Deck;
import inf112.skeleton.app.Deck.GameDeck;

public class UIScreen {
    private Stage stage;
    private float width;
    private float selectedCardPosX;
    private final float selectedCardPosY = 75;

    public UIScreen(float width, GameDeck gameDeck) {
        this.width = width;
        selectedCardPosX =  width +50;
        stage = new Stage();
        Texture texture = new Texture(Gdx.files.internal("lockinbutton.png"));
        System.out.println(width);
        createButton(texture, 1, width * 2 - 800, 200);
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
            CardButton cardButton = new CardButton(card, posX, posY, selectedCardPosX, selectedCardPosY);
            stage.addActor(cardButton.getTable());
        }
    }

    public void createButton(Texture texture, float scale, float posX, float posY) {
        TextureRegion myTextureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        ImageButton button = new ImageButton(myTexRegionDrawable);
        button.getImage().scaleBy(scale, scale);
        button.scaleBy(scale);
        button.setPosition(posX, posY);
        stage.addActor(button);
    }

    public void createImage(Texture texture, float scale, float posX, float posY) {
        TextureRegion myTextureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        Image image = new Image(myTexRegionDrawable);
        image.scaleBy(scale);
        image.setPosition(posX, posY);
        stage.addActor(image);
    }
}

