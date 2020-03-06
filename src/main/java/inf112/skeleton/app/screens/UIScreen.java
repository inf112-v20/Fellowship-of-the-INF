package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.Phase;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.player.Player;


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
    private Player player;
    private Actor[] lifeActors = new Actor[3];
    private Actor[] checkpointActors = new Actor[3];
    private Actor[] damageActors = new Actor[10];
    private ArrayList<Actor> gamelogActors = new ArrayList<>();

    public UIScreen(float width, Game game, GameScreen gameScreen) {
        this.width = width;
        this.game = game;
        this.gameScreen = gameScreen;
        height = width * 0.5f;
        player = game.getListOfPlayers()[0];
        playerHandDeck = player.getPlayerHandDeck();
        stage = new Stage();
        createLockInButton();
        createPlayerPicture();
        createDamageTokens();
        createLifeTokens();
        createCheckPointTokens();
        createPowerDownImage();
    }

    public void handleInput(){
         if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
             game.getRound().nextPhase();
             updateGameLog();
         }
         else if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
             newRound();
             game.getRound().startRound();
             cardButton = new CardButton(player, width, height, stage, lockInButton);
         }
    }

    public void newRound(){
        lockInButton.setTouchable(Touchable.disabled);
        removeGameLog();
        //update();
    }

    public void updateGameLog() {
        removeGameLog();
        Phase phase = game.getRound().getPhase();
        gamelogActors.add(drawText("Round " + game.getRound().getRoundNumber() +
                ":          Phase " + (phase.getPhaseNumber()+1), 10, width*0.52f, height*0.7f));
        for (int i = 0; i < phase.getOrderedListOfPlayers().size(); i++) {
            Player player = phase.getOrderedListOfPlayers().get(i);
            ProgramCard card = player.getSelectedCards()[phase.getPhaseNumber()];
            float gap = card.getTexture().getWidth() * 1.3f;
            float posX = (width * 0.51f) + (i * gap);
            TextureRegion cardPicture = new TextureRegion(card.getTexture());
            gamelogActors.add(createImage(cardPicture, 0.2f, posX, height*0.4f, 1));
            TextureRegion playerPicture = player.getStandardPlayerCell().getTile().getTextureRegion();
            posX = (width * 0.515f) + (i * gap);
            gamelogActors.add(createImage(playerPicture, 0.01f, posX, height*0.55f, 1));
            posX = (width * 0.53f) + (i * gap);
            gamelogActors.add(drawText("" + (i+1), 10, posX, height*0.37f));
        }
    }
    public void removeGameLog(){
        int numberOfActors = gamelogActors.size();
        for (int i = 0; i < numberOfActors ; i++) {
            gamelogActors.get(0).remove();
            gamelogActors.remove(0);
        }
    }

    public void update(){
        float alpha; //opacity of the image
        Color c = lifeActors[0].getColor();
        for (int i = 0; i < 3 ; i++) {
            if (i > player.getLifes() - 1) {alpha = 0.2f; }
            else {alpha = 1;}
            lifeActors[i].setColor(c.r, c.g, c.b, alpha);
        }
        c = checkpointActors[0].getColor();
        for (int i = 0; i < 3; i++) {
            if(i > player.getCheckpointsVisited()-1){alpha = 0.2f;}
            else{alpha = 1;}
            checkpointActors[i].setColor(c.r, c.g, c.b, alpha);
        }
        c = damageActors[0].getColor();
        for (int i = 0; i < 10; i++) {
            if(i > player.getDamage()-1){alpha = 0.2f;}
            else{alpha = 1;}
            damageActors[9-i].setColor(c.r, c.g, c.b, alpha);
        }
        if(player.getSelectedCards()[4] != null) {
            for (int i = 0; i < 5; i++) {
                if (i < player.getLockedCards().size()) {
                    cardButton.getSelectedCardImages()[4 - i].setColor(Color.RED);
                } else {
                    cardButton.getSelectedCardImages()[4 - i].setColor(Color.WHITE);
                }
            }
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void createPlayerPicture(){
        TextureRegion playerPicture = player.getPlayerCell().getTile().getTextureRegion();
        createImage(playerPicture, 1, width *0.5f, height*0.75f, 1);
        String playerName  = "Player " + player.getPlayerNumber();
        drawText(playerName, 10, width * 0.51f, height * 0.95f);
    }

    public void createLifeTokens(){
        Texture texture = new Texture(Gdx.files.internal("lifetoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float alpha = 1;
        float posY = height * 0.87f;
        for (int i = 0; i < 3 ; i++) {
            float posX = (width * 0.6f) + (i * texture.getWidth() * 1.8f);
            Image lifeTokenImage = createImage(textureRegion, 0.25f, posX, posY, alpha);
            lifeActors[i] = lifeTokenImage;
        }
    }

    public void createCheckPointTokens(){
        Texture texture = new Texture(Gdx.files.internal("checkpointtoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.75f;
        for (int i = 0; i < 3 ; i++) {
            float posX = (width * 0.6f) + (i * texture.getWidth() * 1.8f);
            Image checkpointTokenImage = createImage(textureRegion, 0.25f, posX, posY, 0.2f);
            checkpointActors[i] = checkpointTokenImage;
        }
    }

    public void createDamageTokens(){
        Texture texture = new Texture(Gdx.files.internal("damagetoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.25f;
        for (int i = 0; i < 10 ; i++) {
            float posX = (width * 0.51f) + (i * texture.getWidth() * 2.5f);
            Image damageTokenImage = createImage(textureRegion, 1.1f, posX, posY, 1);
            damageActors[i] = damageTokenImage;
        }
    }

    //TODO Change powerDown from image to button
    public void createPowerDownImage(){
        Texture texture = new Texture(Gdx.files.internal("powerdown.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.08f;
        float posX = width * 0.9f;
        createImage(textureRegion, 0.5f, posX, posY, 1);
    }

    public void createLockInButton(){
        Texture texture = new Texture(Gdx.files.internal("lockinbutton.png"));
        lockInButton = createButton(texture, 0.75f, width * 0.8f, height*0.08f);
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

    public Image createImage(TextureRegion textureRegion, float scale, float posX, float posY, float alpha) {
        TextureRegion myTextureRegion = textureRegion;
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        Image image = new Image(myTexRegionDrawable);
        image.scaleBy(scale);
        image.setPosition(posX, posY);
        Color c = image.getColor();
        image.setColor(c.r, c.g, c.b, alpha);
        stage.addActor(image);
        return image;
    }

    public void lockInButtonPressed(ImageButton lockInButton){

        lockInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.executeLockIn(getSelectedProgramCards());
                updateGameLog();
            }
        });
    }
    public Label drawText(String text, float scale, float posX, float posY){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.BLACK;
        Label textLabel = new Label(text, labelStyle);
        textLabel.setFontScale(scale);
        textLabel.setPosition(posX, posY);
        stage.addActor(textLabel);
        return textLabel;
    }

    public ProgramCard[] getSelectedProgramCards(){

        for (int i = 0; i < 5 ; i++) {
            if(cardButton.getSelectedCards()[i] == null){System.out.println("Not enough cards");return null;}
        }
        lockInButton.setTouchable(Touchable.disabled);
        for (int i = 0; i < 5 ; i++) {
            cardButton.getSelectedCardButtons()[i].setTouchable(Touchable.disabled);
        }
        for (int i = 0; i <cardButton.getLeftOverCardButtons().size() ; i++) {
            if(cardButton.getLeftOverCardButtons().get(i) != null) {
                cardButton.getLeftOverCardButtons().get(i).remove();
            }
        }
        return cardButton.getSelectedCards();
    }
}

