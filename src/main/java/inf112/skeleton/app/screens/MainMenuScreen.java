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

public class MainMenuScreen implements Screen {

    private Wav.Sound sound;
    private RoboRallyGame game;
    private float width;
    private float height;

    private Stage stage;

    public MainMenuScreen(RoboRallyGame game) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from Main.java
        this.height = Gdx.graphics.getHeight();
        sound = (Wav.Sound) Gdx.audio.newSound(Gdx.files.internal("sounds/reitanna__thunk.wav"));
    }

    @Override
    public void show() {
        stage = new Stage();

        // picture = all actors on stage
        // Background
        Sprite picture = new Sprite(new Texture("menu/MainMenuBackground.png"));
        Image background = new Image(new SpriteDrawable(picture));
        background.setSize(width, height);
        background.setPosition(0, 0);
        stage.addActor(background);

        // Logo
        int yPadding = 50;
        picture = new Sprite(new Texture("menu/RoboRally_logo.png"));
        Image logo = new Image(new SpriteDrawable(picture));
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 2);
        logo.setPosition((width - logo.getWidth()) / 2, height - (logo.getHeight() + yPadding));
        stage.addActor(logo);

        // Play button
        yPadding = 440;
        int xPadding = 500;
        picture = new Sprite(new Texture("menu/navbuttons/PlayButton.png"));
        ImageButton playButton = new ImageButton(new SpriteDrawable(picture));
        playButton.setPosition((width - picture.getWidth()) / 2, height - (picture.getHeight() + yPadding));
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new PlayerSelectionScreen(game));
            }
        });
        stage.addActor(playButton);

        // Play button
        yPadding = 100;
        xPadding = 100;
        picture = new Sprite(new Texture("menu/navbuttons/PlayButton.png"));
        ImageButton testButton = new ImageButton(new SpriteDrawable(picture));
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
        picture = new Sprite(new Texture("menu/navbuttons/ExitButton.png"));
        ImageButton exitButton = new ImageButton(new SpriteDrawable(picture));
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
