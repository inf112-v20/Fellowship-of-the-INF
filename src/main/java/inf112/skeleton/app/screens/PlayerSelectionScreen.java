package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.audio.Wav;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.player.AIPlayer.Difficulty;

public class PlayerSelectionScreen implements Screen {
    private RoboRallyGame game;
    private float width;
    private float height;
    private Difficulty difficulty = Difficulty.EASY;
    private Wav.Sound sound;

    private Stage stage;

    public PlayerSelectionScreen(RoboRallyGame game) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from Main.java
        this.height = Gdx.graphics.getHeight();
        sound = (Wav.Sound) Gdx.audio.newSound(Gdx.files.internal("sounds/reitanna__thunk.wav"));

    }

    @Override
    public void show() {
        stage = new Stage();

        // Background
        Image background = createImage("StageSelectBackground");
        background.setSize(width, height);
        background.setPosition(0, 0);
        stage.addActor(background);

        // Logo
        int yPadding = 50;
        Image logo = createImage("RoboRally_logo");
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 2);
        logo.setPosition((width - logo.getWidth()) / 2, height - (logo.getHeight() + yPadding));
        stage.addActor(logo);

        //Text that says choose amount of players
        int xPadding = 80;
        Image choosePlayers = createImage("ChoosePlayers");
        choosePlayers.setPosition(width/2 - choosePlayers.getWidth()/2,  logo.getY()-yPadding-choosePlayers.getHeight());
        stage.addActor(choosePlayers);

        // Reference for size of player buttons, they are all the same
        yPadding = 400;
        Sprite picture = new Sprite(new Texture("menu/playerbuttons/1Players.png"));
        ImageButton playerButtonSize = new ImageButton(new SpriteDrawable(picture));

        // Constructing all buttons, first argument is which player button is made
        ImageButton player2Button = createPlayerButton(2,xPadding, yPadding);
        createPlayerButton(3, 2 * xPadding + playerButtonSize.getWidth(), yPadding);
        createPlayerButton(4,3 * xPadding + playerButtonSize.getWidth() + playerButtonSize.getWidth(), yPadding);
        createPlayerButton(5,width - xPadding - playerButtonSize.getWidth(), yPadding);
        createPlayerButton(6,xPadding, yPadding/4);
        createPlayerButton(7,2 * xPadding + playerButtonSize.getWidth(), yPadding/4);
        createPlayerButton(8,3 * xPadding + playerButtonSize.getWidth() + playerButtonSize.getWidth(), yPadding/4);

        //Back button
        picture = new Sprite(new Texture("menu/navbuttons/BackButton.png"));
        ImageButton backButton = new ImageButton(new SpriteDrawable(picture));
        backButton.setPosition(width - xPadding - playerButtonSize.getWidth(), yPadding/4);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backButton);

        //Skin Source: https://github.com/libgdx/libgdx-skins/tree/master/skins/visui/assets
        TextureAtlas dropdownAtlas = new TextureAtlas(Gdx.files.internal("menu/navbuttons/uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("menu/navbuttons/uiskin.json"), dropdownAtlas);
        final SelectBox<Difficulty> dropDownMenu = new SelectBox<>(skin);
        dropDownMenu.setSize(100,50);
        dropDownMenu.setPosition(player2Button.getX(), height-yPadding);
        dropDownMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound.play();
                difficulty = (dropDownMenu.getSelected());
            }
        });
        dropDownMenu.setItems(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD, Difficulty.EXPERT);
        stage.addActor(dropDownMenu);

        Gdx.input.setInputProcessor(stage);
    }

    /** Method for constructing and showing functional buttons for choosing players, will send appropriate
     * amount of players to MapSelectionScreen.
     * @param numberOfPlayers Amount of players the button is supposed to show and send to MapSelectionScreen
     * @param x x position of button
     * @param y y position of button
     * @return returns an ImageButton for reference if needed for positioning of other buttons
     */
    public ImageButton createPlayerButton(final int numberOfPlayers, float x, float y){
        Sprite picture = new Sprite(new Texture("menu/playerbuttons/" + numberOfPlayers + "Players.png"));
        ImageButton playerButton = new ImageButton(new SpriteDrawable(picture));
        playerButton.setPosition(x, y);
        playerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new MapSelectionScreen(game, numberOfPlayers,difficulty));
            }
        });
        stage.addActor(playerButton);
        return playerButton;
    }

    /**
     * Creates a sprite that can be used as an actor on the MainMenuScreen stage, in this case an image.
     *
     * @param filename name of the image file
     * @return the image to be set as an actor
     */
    public Image createImage (final String filename) {
        Sprite picture = new Sprite(new Texture("menu/" + filename + ".png"));
        return new Image(new SpriteDrawable(picture));
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); //added to fix dropDownMenu
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        //not used
    }

    @Override
    public void pause() {
        //not used
    }

    @Override
    public void resume() {
        //not used
    }

    @Override
    public void hide() {
        //not used
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
