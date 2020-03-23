package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import inf112.skeleton.app.RoboRallyDemo;

public class PlayerSelectionScreen implements Screen {
    private RoboRallyDemo game;
    private float width;
    private float height;

    private Stage stage;
    private Image background;
    private Image logo;
    private ImageButton player1Button, player2Button, player3Button, player4Button, backButton;

    public PlayerSelectionScreen(RoboRallyDemo game) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from Main.java
        this.height = Gdx.graphics.getHeight();
    }

    @Override
    public void show() {
        stage = new Stage();

        // picture = all actors on stage
        // Background
        Sprite picture = new Sprite(new Texture("StageSelectBackground.png"));
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

        // Stage1 button
        yPadding = 400;
        int xPadding = 100;
        picture = new Sprite(new Texture("Stage1Button.png"));
        player1Button = new ImageButton(new SpriteDrawable(picture));
        player1Button.setPosition(xPadding, yPadding);
        player1Button.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                //Original map: RoborallyBoard_debugged.tmx
                //Test map for conveyorbelts: conveyorBeltTestMap.tmx
                game.setScreen(new GameScreen("RoborallyBoard_debugged.tmx"));
            }
        });
        stage.addActor(player1Button);

        // Stage2 button
        picture = new Sprite(new Texture("Stage2Button.png"));
        player2Button = new ImageButton(new SpriteDrawable(picture));
        player2Button.setPosition((width - xPadding)-player2Button.getWidth(), yPadding);
        player2Button.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(new GameScreen("RoborallyBoard_Vault_Assault.tmx"));
            }
        });
        stage.addActor(player2Button);

        // testStage button
        picture = new Sprite(new Texture("TestStageButton.png"));
        player3Button = new ImageButton(new SpriteDrawable(picture));
        player3Button.setPosition(width/2 - player3Button.getWidth()/2, yPadding);
        player3Button.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(new GameScreen("TestMap.tmx"));
            }
        });
        stage.addActor(player3Button);



        // Back button
        picture = new Sprite(new Texture("BackButton.png"));
        backButton = new ImageButton(new SpriteDrawable(picture));
        backButton.setPosition((width-xPadding)- backButton.getWidth(), 100);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backButton);

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
