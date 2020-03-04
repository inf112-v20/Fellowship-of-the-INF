package inf112.skeleton.app.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.ShapeCache;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;

import java.awt.*;
import java.util.ArrayList;
//TODO add comments to this class
public class UIScreen{
    private Stage stage;
    private float width;
    private float height;
    private GameScreen gameScreen;
    private CardButton cardButton;
    private Game game;
    private ArrayList<ProgramCard> playerHandDeck;
    private ImageButton lockInButton;

    public UIScreen(float width, Game game, GameScreen gameScreen) {
        this.width = width;
        height = width * 0.5f; //TODO refactor
        this.gameScreen = gameScreen;
        this.game = game;
        this.playerHandDeck = game.getPlayer().getPlayerHandDeck();
        stage = new Stage();
        createLockInButton();
        cardButton = new CardButton(playerHandDeck, width, height, stage, lockInButton);
        lockInButton.setTouchable(Touchable.disabled);
        TextureRegion playerPicture = game.getListOfPlayers()[1].getPlayerCell().getTile().getTextureRegion();
        createImage(playerPicture, 1, width *0.5f, height*0.75f, 1);
        String playerName  = "Player " + game.getListOfPlayers()[1].getPlayerNumber();
        drawText(playerName, 10, width * 0.51f, height * 0.95f);
        createDamageTokens();
        createLifeTokens();
        createCheckPointTokens();
        createPowerDownImage();
    }

    public Stage getStage() {
        return stage;
    }


    public void createLifeTokens(){
        Texture texture = new Texture(Gdx.files.internal("lifetoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.87f;
        for (int i = 0; i < 3 ; i++) {
            float posX = (width * 0.6f) + (i * texture.getWidth() * 1.8f);
            createImage(textureRegion, 0.25f, posX, posY, 1);
        }
    }

    public void createCheckPointTokens(){
        Texture texture = new Texture(Gdx.files.internal("checkpointtoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.75f;
        for (int i = 0; i < 3 ; i++) {
            float posX = (width * 0.6f) + (i * texture.getWidth() * 1.8f);
            createImage(textureRegion, 0.25f, posX, posY, 0.2f);
        }
    }

    public void createDamageTokens(){
        Texture texture = new Texture(Gdx.files.internal("damagetoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.25f;
        for (int i = 0; i < 10 ; i++) {
            float posX = (width * 0.51f) + (i * texture.getWidth() * 2.5f);
            createImage(textureRegion, 1.1f, posX, posY, 1);
        }
    }

    //TODO Change powerDown from image to button
    public void createPowerDownImage(){
        Texture texture = new Texture(Gdx.files.internal("powerdown.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.39f;
        float posX = width * 0.74f;
        createImage(textureRegion, 0.5f, posX, posY, 1);
    }

    public void createLockInButton(){
        Texture texture = new Texture(Gdx.files.internal("lockinbutton.png"));
        lockInButton = createButton(texture, 0.75f, width * 0.8f, height*0.1f);
        Color c = lockInButton.getColor();
        lockInButton.setColor(c.r, c.g, c.b, 0.5f);
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

    public void createImage(TextureRegion textureRegion, float scale, float posX, float posY, float alpha) {
        TextureRegion myTextureRegion = textureRegion;
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        Image image = new Image(myTexRegionDrawable);
        image.scaleBy(scale);
        image.setPosition(posX, posY);
        Color c = image.getColor();
        image.setColor(c.r, c.g, c.b, alpha);
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
    public void drawText(String text, float scale, float posX, float posY){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.BLACK;
        Label textLabel = new Label(text, labelStyle);
        textLabel.setFontScale(scale);
        textLabel.setPosition(posX, posY);
        stage.addActor(textLabel);
    }

    public ArrayList<ProgramCard> getSelectedProgramCards(){

        for (int i = 0; i < 5 ; i++) {
            if(cardButton.getSelectedCards()[i] == null){System.out.println("Not enough cards");return null;}
        }
        ArrayList<ProgramCard> selectedProgramCards = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            selectedProgramCards.add(cardButton.getSelectedCards()[i]);
            cardButton.getSelectedCardButtons()[i].setTouchable(Touchable.disabled);
        }
        for (int i = 0; i <cardButton.getLeftOverCardButtons().size() ; i++) {
            if(cardButton.getLeftOverCardButtons().get(i) != null) {
                cardButton.getLeftOverCardButtons().get(i).remove();
                cardButton.getLeftOverCardTexts().get(i).remove();
            }
        }
        return selectedProgramCards;
    }
}

