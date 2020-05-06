package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.audio.Wav;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import inf112.skeleton.app.RoboRallyGame;

/**
 * The main menu of the game.
 */
public class MainMenuScreen implements Screen {

    private Wav.Sound sound;
    private RoboRallyGame game;
    private float width;
    private float height;
    private int yPadding;
    private int xPadding;

    private Stage stage;

    public MainMenuScreen(RoboRallyGame game) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from tmx maps
        this.height = Gdx.graphics.getHeight();
        sound = (Wav.Sound) Gdx.audio.newSound(Gdx.files.internal("assets/sounds/reitanna__thunk.wav"));
    }

    /**
     * TODO write docs
     *
     * @param filename
     * @return
     */
    public Image createImage (final String filename) {
        Sprite picture = new Sprite(new Texture("menu/" + filename + ".png"));
        Image image = new Image(new SpriteDrawable(picture));
        return image;
    }

    /**
     * TODO write docs
     *
     * @param filename
     * @return
     */
    public ImageButton createImageButton (final String filename) {
        Sprite picture = new Sprite(new Texture("menu/navbuttons/" + filename + ".png"));
        ImageButton button = new ImageButton(new SpriteDrawable(picture));
        return button;
    }

    @Override
    public void show() {
        stage = new Stage();

        // Background
        Image background = createImage("MainMenuBackground");
        background.setSize(width, height);
        background.setPosition(0, 0);
        stage.addActor(background);

        // Logo
        yPadding = 50;
        Image logo = createImage("RoboRally_logo");
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 2);
        logo.setPosition((width - logo.getWidth()) / 2, height - (logo.getHeight() + yPadding));
        stage.addActor(logo);

        // Play button
        yPadding = 440;
        xPadding = 500;
        ImageButton playButton = createImageButton("PlayButton");
        playButton.setPosition((width - playButton.getWidth()) / 2, height - (playButton.getHeight() + yPadding));
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new PlayerSelectionScreen(game));
            }
        });
        stage.addActor(playButton);

        // Test button
        yPadding = 100;
        xPadding = 100;
        ImageButton testButton = createImageButton("TestButton");
        testButton.setPosition(xPadding, yPadding);
        testButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new TestMapSelectionScreen(game));
            }
        });
        stage.addActor(testButton);

        // Exit button
        ImageButton exitButton = createImageButton("ExitButton");
        exitButton.setPosition((width - xPadding) - exitButton.getWidth(), yPadding);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
