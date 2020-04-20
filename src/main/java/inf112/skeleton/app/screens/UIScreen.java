package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.audio.Wav;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.game_logic.Game;
import inf112.skeleton.app.game_logic.Phase;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.player.Player;


import java.util.ArrayList;


public class UIScreen {
    private ScoreBoardScreen scoreBoardScreen;
    private Stage stage;
    private float width;
    private float height;
    private CardButton cardButton;
    private Game game;
    private ImageButton lockInButton;
    private ImageButton powerDownButton;
    private Player player;
    private Actor[] lifeActors = new Actor[3];
    private Actor[] checkpointActors = new Actor[3];
    private Actor[] damageActors = new Actor[10];
    private ArrayList<Actor> gamelogActors = new ArrayList<>();
    private Label timerLabel = null;
    private Wav.Sound thudSound;


    public UIScreen(final float width, Game game) {
        this.width = width;
        this.game = game;
        this.scoreBoardScreen = new ScoreBoardScreen(game);
        height = width * 0.5f;
        player = game.getListOfPlayers()[0];
        stage = new Stage();
        thudSound = (Wav.Sound) Gdx.audio.newSound(Gdx.files.internal("assets/sounds/reitanna__thunk.wav"));
        Texture texture = new Texture(Gdx.files.internal("ui/background.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        createImage(textureRegion, 1, width * 0.5f, 0, 1);
        createLockInButton();
        createPlayerPicture();
        createDamageTokens();
        createLifeTokens();
        createCheckPointTokens();
        //createPowerDownImage();
        createPowerDownButton();
        this.newRound();
    }

    /**
     * Press ENTER to play the next phase.
     * Starts a new round if the last phase was phase number 5
     */
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (game.getRound().getPhaseNr() > 4) {
                game.getRound().finishRound();
                this.newRound();
            } else {
                if (game.getRound().getPhaseNr() == 0) {
                    executeLockInButton();
                    return;
                }
                game.getRound().nextPhase();
                updateGameLog();
            }
        }
    }

    /**
     * Starts a new round
     * Removes the game log from the last phase
     * Creates new cardbuttons for the new playerhand
     */
    public void newRound() {
        game.executeRound();
        lockInButton.setTouchable(Touchable.disabled);
        powerDownButton.setColor(powerDownButton.getColor().r, powerDownButton.getColor().g, powerDownButton.getColor().b, 1f);
        powerDownButton.setTouchable(Touchable.enabled);
        //Check if the player had the correct number of cards on the hand
        if (player.getLockedCards().size() == 5 || player.getLockedCards().size() == (9 - player.getDamage())) {
            lockInButton.setTouchable(Touchable.enabled);
            Color c = lockInButton.getColor();
            lockInButton.setColor(c.r, c.g, c.b, 1);
        }
        removeGameLog();
        cardButton = new CardButton(player, width, height, stage, lockInButton);
    }

    /**
     * Removes the gamelog from the last phase and creates a new gamelog for this phase.
     * The gamelog shows current round, current phase the order of the cards for this phase and which player
     * played which card.
     */
    public void updateGameLog() {
        removeGameLog();
        Phase phase = game.getRound().getPhase();
        gamelogActors.add(drawText("Round " + game.getRound().getRoundNumber() +
                ":          Phase " + (phase.getPhaseNumber() + 1), 10, width * 0.52f, height * 0.7f, Color.GOLD));
        //Go through the list of players with cards locked in and add them to the game log
        for (int i = 0; i < phase.getOrderedListOfPlayers().size(); i++) {
            Player player = phase.getOrderedListOfPlayers().get(i);
            ProgramCard card = player.getSelectedCards()[phase.getPhaseNumber()];
            float gap = card.getTexture().getWidth() * 1.3f;
            float posX = (width * 0.51f) + (i * gap);
            TextureRegion cardPicture = new TextureRegion(card.getTexture());
            gamelogActors.add(createImage(cardPicture, 0.2f, posX, height * 0.4f, 1));
            String priorityText = String.valueOf(card.getPriority());
            float textPosX = cardPicture.getRegionWidth() * 0.65f;
            float textPosY = cardPicture.getRegionHeight();
            gamelogActors.add(drawText(priorityText, 4, posX + textPosX, height * 0.4f + textPosY, Color.GREEN));
            TextureRegion playerPicture = player.getPlayerCell().getTile().getTextureRegion();
            posX = (width * 0.515f) + (i * gap);
            gamelogActors.add(createImage(playerPicture, 0.01f, posX, height * 0.55f, 1));
            posX = (width * 0.53f) + (i * gap);
            gamelogActors.add(drawText("" + (i + 1), 10, posX, height * 0.37f, Color.BLACK));
        }
        //Check for player that are in power down, and add them to the game log after the players wth cards
        for (int i = 0; i < game.getListOfPlayers().length; i++) {
            Player player = game.getListOfPlayers()[i];
            if(player.isPowerDownMode()) {
                float gap = powerDownButton.getWidth() * 1.4f;
                float posX = (width * 0.51f) + ((phase.getOrderedListOfPlayers().size() + i) * gap);
                Texture texture = new Texture(Gdx.files.internal("ui/powerdown.png"));
                TextureRegion powerDownTexture = new TextureRegion(texture);
                gamelogActors.add(createImage(powerDownTexture, 0.2f, posX, height * 0.4f, 1));
                TextureRegion playerPicture = player.getPlayerCell().getTile().getTextureRegion();
                posX = (width * 0.515f) + ((phase.getOrderedListOfPlayers().size() + i) * gap);
                gamelogActors.add(createImage(playerPicture, 0.01f, posX, height * 0.55f, 1));
                posX = (width * 0.53f) + ((phase.getOrderedListOfPlayers().size() + i) * gap);
                gamelogActors.add(drawText("" + ((phase.getOrderedListOfPlayers().size() + i) + 1), 10, posX, height * 0.37f, Color.BLACK));
            }
        }
    }

    /**
     * Removes every actor from the gamelog
     * (and will automatically be removed from the stage)
     */
    public void removeGameLog() {
        int numberOfActors = gamelogActors.size();
        for (int i = 0; i < numberOfActors; i++) {
            gamelogActors.get(0).remove();
            gamelogActors.remove(0);
        }
    }

    /**
     * Updates the UI for the player info when taking damage,
     * getting repairs, visiting flags/checkpoints, losing lifes.
     */
    public void update() {

        float alpha; //opacity of the image
        Color c = lifeActors[0].getColor();
        for (int i = 0; i < 3; i++) {
            if (i > player.getLives() - 1) {
                alpha = 0.2f;
            } else {
                alpha = 1;
            }
            lifeActors[i].setColor(c.r, c.g, c.b, alpha);
        }
        c = checkpointActors[0].getColor();
        for (int i = 0; i < 3; i++) {
            if (i > player.getCheckpointsVisited() - 1) {
                alpha = 0.2f;
            } else {
                alpha = 1;
            }
            checkpointActors[i].setColor(c.r, c.g, c.b, alpha);
        }
        c = damageActors[0].getColor();
        for (int i = 0; i < 10; i++) {
            if (i > player.getDamage() - 1) {
                alpha = 0.2f;
            } else {
                alpha = 1;
            }
            damageActors[9 - i].setColor(c.r, c.g, c.b, alpha);
        }
        if (player.getSelectedCards()[4] != null) {
            for (int i = 0; i < 5; i++) {
                if (i < player.getLockedCards().size()) {
                    cardButton.getSelectedCardImages()[4 - i].setColor(Color.RED);
                } else {
                    cardButton.getSelectedCardImages()[4 - i].setColor(Color.WHITE);
                }
            }
        }
        scoreBoardScreen.update();
    }

    /**
     * Creates the player picture and the name of the player above it.
     */
    public void createPlayerPicture() {
        Texture backgroundTexture = new Texture(Gdx.files.internal("ui/profilepicture.png"));
        TextureRegion backgroundPicture = new TextureRegion(backgroundTexture);
        createImage(backgroundPicture, 1, width * 0.5f, height * 0.75f, 0.85f);
        TextureRegion playerPicture = player.getPlayerCell().getTile().getTextureRegion();
        createImage(playerPicture, 1, width * 0.5f, height * 0.75f, 1);
        String playerName = "Player " + player.getPlayerNumber();
        drawText(playerName, 10, width * 0.51f, height * 0.95f, Color.BLACK);
    }

    /**
     * Creates all 3 life tokens.
     */
    public void createLifeTokens() {
        Texture texture = new Texture(Gdx.files.internal("ui/lifetoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float alpha = 1;
        float posY = height * 0.87f;
        for (int i = 0; i < 3; i++) {
            float posX = (width * 0.6f) + (i * texture.getWidth() * 1.8f);
            Image lifeTokenImage = createImage(textureRegion, 0.25f, posX, posY, alpha);
            lifeActors[i] = lifeTokenImage;
        }
    }

    /**
     * Creates all 3 checkpoint tokens
     */
    public void createCheckPointTokens() {
        Texture texture = new Texture(Gdx.files.internal("ui/checkpointtoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.75f;
        for (int i = 0; i < 3; i++) {
            float posX = (width * 0.6f) + (i * texture.getWidth() * 1.8f);
            Image checkpointTokenImage = createImage(textureRegion, 0.25f, posX, posY, 0.2f);
            checkpointActors[i] = checkpointTokenImage;
        }
    }

    /**
     * Creates all 10 damagetokens
     */
    public void createDamageTokens() {
        Texture texture = new Texture(Gdx.files.internal("ui/damagetoken.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.25f;
        for (int i = 0; i < 10; i++) {
            float posX = (width * 0.51f) + (i * texture.getWidth() * 2.5f);
            Image damageTokenImage = createImage(textureRegion, 1.1f, posX, posY, 1);
            damageActors[i] = damageTokenImage;
        }
    }

    /**
     * Creates the powerDown image
     */
    public void createPowerDownImage() {
        Texture texture = new Texture(Gdx.files.internal("ui/powerdown.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        float posY = height * 0.08f;
        float posX = width * 0.9f;
        createImage(textureRegion, 0.5f, posX, posY, 1);
    }

    /**
     * Creates the powerDown button
     */
    public void createPowerDownButton() {
        Texture texture = new Texture(Gdx.files.internal("ui/powerdown.png"));
        powerDownButton = createButton(texture, 0.5f, width * 0.9f, height * 0.08f);
        Color c = powerDownButton.getColor();
        powerDownButton.setColor(c.r, c.g, c.b, 1f);
        powerDownButtonPressed(powerDownButton);
    }

    /**
     * Creates the lockInButton
     */
    public void createLockInButton() {
        Texture texture = new Texture(Gdx.files.internal("ui/lockinbutton.png"));
        lockInButton = createButton(texture, 0.75f, width * 0.8f, height * 0.08f);
        Color c = lockInButton.getColor();
        lockInButton.setColor(c.r, c.g, c.b, 0.5f);
        lockInButtonPressed(lockInButton);
    }

    /**
     * Creates an ImageButton and adds it to the stage
     *
     * @param texture the texture for the button
     * @param scale   the scale to size the button by
     * @param posX    the wanted x position of the button
     * @param posY    the wanted y position of the button
     * @return the created ImageButton
     */
    public ImageButton createButton(Texture texture, float scale, float posX, float posY) {
        TextureRegion myTextureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        ImageButton button = new ImageButton(myTexRegionDrawable);
        button.getImage().scaleBy(scale);
        button.setPosition(posX, posY);
        stage.addActor(button);
        return button;
    }

    /**
     * Creates an Image and adds to the stage
     *
     * @param textureRegion the texture for the image
     * @param scale         the scale to size the image by
     * @param posX          the wanted x position of the image
     * @param posY          the wanted y position of the image
     * @param alpha         the opacity of the image
     * @return the created Image
     */
    public Image createImage(TextureRegion textureRegion, float scale, float posX, float posY, float alpha) {
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(textureRegion);
        Image image = new Image(myTexRegionDrawable);
        image.scaleBy(scale);
        image.setPosition(posX, posY);
        Color c = image.getColor();
        image.setColor(c.r, c.g, c.b, alpha);
        stage.addActor(image);
        return image;
    }

    /**
     * Clicklistener for the lockinbutton
     *
     * @param lockInButton the button to create a clicklistener for
     */
    public void lockInButtonPressed(final ImageButton lockInButton) {
        lockInButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                executeLockInButton();
            }

        });
    }

    /**
     * Clicklistener for the powerDownButton
     *
     * @param powerDownButton the button to  create a clicklistener for
     */
    public void powerDownButtonPressed(ImageButton powerDownButton) {
        powerDownButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { executePowerDownButton();
            }
        });
    }

    /**
     * Draws text on the UI
     *
     * @param text  the text to draw
     * @param scale the scale to size the text by
     * @param posX  the wanted x position of the text
     * @param posY  the wanted y position of the text
     * @param color the wanted color of the text
     * @return the Label that is created
     */
    public Label drawText(String text, float scale, float posX, float posY, Color color) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = color;
        Label textLabel = new Label(text, labelStyle);
        textLabel.setFontScale(scale);
        textLabel.setPosition(posX, posY);
        stage.addActor(textLabel);
        return textLabel;
    }

    /**
     * Removes the cards from the player hand,
     * and sets the player in power down mode.
     * This means that it repairs itself of all damage,
     * but do not move during this round.
     * It can still shoot lasers and take damage again.
     */
    private void executePowerDownButton() {
        if(game.getRound().getPhaseNr() != 0 ){return;}
        Color c = powerDownButton.getColor();
        powerDownButton.setColor(c.r, c.g, c.b, 0.5f);
        powerDownButton.setTouchable(Touchable.disabled);
        removeUnusedCardsFromScreen();
        player.doPowerDown();
        player.setLockedIn(true);
        game.getRound().nextPhase();
        updateGameLog();
    }

    /**
     * Locks in the selected cards if the player has selected
     * five cards and starts phase 1 of the current round.
     * Removes the non selected cardbuttons from the stage.
     * Disables the buttons for the cards so the
     * player cant move them after locking in.
     */
    public void executeLockInButton() {
        if(game.getRound().getPhaseNr() != 0 ){return;}
        for (int i = 0; i < 5; i++) {
            if (cardButton.getSelectedCards()[i] == null) {
                System.out.println("Not enough cards");
                return;
            }
        }
        for (int i = 1; i < game.getListOfPlayers().length ; i++) {
            if(!game.getListOfPlayers()[i].hasLockedIn()){
                System.out.println("Not every computer player has locked in. " +
                        "Press 0 to lock in cards for every computer player, " +
                        "or LEFT CTRL + 0 to lock in player one by one.");
                return;
        }
    }
        for (int i = 0; i < 5; i++) {
            cardButton.getSelectedCardButtons()[i].setTouchable(Touchable.disabled);
        }
        Color c = lockInButton.getColor();
        lockInButton.setColor(c.r, c.g, c.b, 0.5f);
        lockInButton.setTouchable(Touchable.disabled);
        removeUnusedCardsFromScreen();
        player.setLockedIn(true);
        game.getRound().nextPhase();
        updateGameLog();
    }

    /**
     * Removes the cards that are not used from the UI
     */
    public void removeUnusedCardsFromScreen() {
        for (int i = 0; i < cardButton.getLeftOverCardButtons().size(); i++) {
            if (cardButton.getLeftOverCardButtons().get(i) != null) {
                cardButton.getLeftOverCardButtons().get(i).remove();
            }
        }
    }

    /**
     * Draws timertext in the topright corner of the screen
     * @param seconds the current timer number to draw
     */
    public void drawTimer(int seconds){
        if(timerLabel != null){ timerLabel.remove();}
        if(seconds>= 0) timerLabel = drawText(String.valueOf(seconds), 30, width*0.9f, height*0.9f, Color.BLACK);
    }

    public Stage getStage() { return stage; }

    public ScoreBoardScreen getScoreBoardScreen() { return scoreBoardScreen; }

    public CardButton getCardButton() { return cardButton; }

    public Label getTimerLabel(){return timerLabel;}

}

