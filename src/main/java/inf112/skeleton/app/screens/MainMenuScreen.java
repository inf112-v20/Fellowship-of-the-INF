package inf112.skeleton.app.Screens;

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
import com.badlogic.gdx.utils.Align;
import inf112.skeleton.app.RoboRallyDemo;
import inf112.skeleton.app.screens.GameScreen;

public class MainMenuScreen implements Screen {

    private RoboRallyDemo game;
    private float width;
    private float height;

    private Stage stage;
    private Image background;
    private Image logo;
    private ImageButton playButton;
    private ImageButton exitButton;
    private ImageButton testButton;

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
        int paddingY = 50; // padding from the screen wall
        picture = new Sprite(new Texture("RoboRally_logo.png"));
        logo = new Image(new SpriteDrawable(picture));
        logo.setSize(logo.getWidth()*1.5f, logo.getHeight()*2);
        logo.setPosition((width-logo.getWidth())/2, height-(logo.getHeight()+paddingY));
        stage.addActor(logo);

        // Play button
        int paddingButton = 25; // padding between buttons
        int paddingX = 50;
//        paddingY = 50;
        picture = new Sprite(new Texture("play_button_up.png"));
        playButton = new ImageButton(new SpriteDrawable(picture));
        playButton.setPosition(paddingX,paddingY);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(new GameScreen("RoborallyBoard_debugged.tmx"));
            }
        });
        stage.addActor(playButton);

        // Test button
        picture = new Sprite(new Texture("test_button_up.png"));
        testButton = new ImageButton(new SpriteDrawable(picture));
        testButton.setSize(playButton.getWidth(), playButton.getHeight());
        testButton.setPosition(playButton.getWidth()+paddingX+paddingButton, paddingY);
        testButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                // TODO implement test map
                System.out.println("TEST button is clicked");
            }
        });
        stage.addActor(testButton);

        // Exit button
        picture = new Sprite(new Texture("exit_button_up.png"));
        exitButton = new ImageButton(new SpriteDrawable(picture));
        exitButton.setSize(exitButton.getWidth()/2, exitButton.getHeight()/2);
        exitButton.setPosition(testButton.getX()+testButton.getWidth()+paddingButton, paddingY);
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
