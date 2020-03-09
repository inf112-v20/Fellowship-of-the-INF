package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import inf112.skeleton.app.RoboRallyDemo;

public class MainMenuScreen implements Screen {

    private RoboRallyDemo game;
    private float width;
    private float height;

    private Stage stage;
    private Image background;
    private Image logo;
    private ImageButton playButton;
    private ImageButton testButton;
    private ImageButton exitButton;

    public MainMenuScreen (RoboRallyDemo game) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from Main.java
        this.height = Gdx.graphics.getHeight();
    }

    @Override
    public void show() {
        stage = new Stage();

        // picture = all actors on stage
        // Background
        Sprite picture = new Sprite(new Texture("menu_background.png"));
        background = new Image(new SpriteDrawable(picture));
        background.setSize(width, height);
        background.setPosition(0, 0);
        stage.addActor(background);

        // Logo
        int yPadding = 50;
        picture = new Sprite(new Texture("RoboRally_logo.png"));
        logo = new Image(new SpriteDrawable(picture));
        logo.setSize(logo.getWidth()*1.5f, logo.getHeight()*2);
        logo.setPosition((width-logo.getWidth())/2, height-(logo.getHeight()+yPadding));
        stage.addActor(logo);

        // TODO dispose of Play/Exit button, add new buttons to stage (choose maps)
        // Play button
        yPadding = 100;
        int xPadding = 100;
        picture = new Sprite(new Texture("play_button_up.png"));
        playButton = new ImageButton(new SpriteDrawable(picture));
        playButton.setPosition(xPadding, yPadding);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(new GameScreen("RoborallyBoard_debugged.tmx"));
            }
        });
        stage.addActor(playButton);

        // Exit button
        picture = new Sprite(new Texture("exit_button_up.png"));
        exitButton = new ImageButton(new SpriteDrawable(picture));
        exitButton.setPosition((width-xPadding)-exitButton.getWidth(), yPadding);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
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
