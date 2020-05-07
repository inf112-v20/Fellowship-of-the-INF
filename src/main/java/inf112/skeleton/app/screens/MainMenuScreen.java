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

    private Stage stage;

    public MainMenuScreen(RoboRallyGame game) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from gdx
        this.height = Gdx.graphics.getHeight();
        sound = (Wav.Sound) Gdx.audio.newSound(Gdx.files.internal("assets/sounds/reitanna__thunk.wav"));
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
        int yPadding = 50;
        Image logo = createImage("RoboRally_logo");
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 2);
        logo.setPosition((width - logo.getWidth()) / 2, height - (logo.getHeight() + yPadding));
        stage.addActor(logo);

        // Play button
        yPadding = 440;
        int xPadding = 500;
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

    /**
     * Creates a sprite that can be used as an actor on the MainMenuScreen stage, in this case an imagebutton.
     *
     * @param filename name of the image file
     * @return the imagebutton to bet set as an actor
     */
    public ImageButton createImageButton (final String filename) {
        Sprite picture = new Sprite(new Texture("menu/navbuttons/" + filename + ".png"));
        return new ImageButton(new SpriteDrawable(picture));
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
