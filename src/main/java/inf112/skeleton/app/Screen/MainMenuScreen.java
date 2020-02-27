package inf112.skeleton.app.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.RoboRallyDemo;

public class MainMenuScreen implements Screen {

    private RoboRallyDemo game;

    private Stage stage;
    private Skin skin; // appearance of buttons
    private TextureAtlas atlas;
    private Table table;
    private TextButton buttonPlay, buttonExit;
    private BitmapFont white, black;
    private Label heading;

    private ImageButton playButton;

    public MainMenuScreen (RoboRallyDemo game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();

        Sprite picture = new Sprite(new Texture("play_button_active.png"));
        playButton = new ImageButton(new SpriteDrawable(picture));
        playButton.setPosition(50,50);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                System.out.println("TEST: Play button is clicked");
            }
        });
        stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);

        // TODO sjekk ut resten av tutorial
//        table = new Table();
//        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//        buttonExit = new TextButton("EXIT", )
//
//        // TODO add Atlas
////        atlas = new TextureAtlas();
//
//        white = new BitmapFont(Gdx.files.internal("font/white.fnt"), false);
//        black = new BitmapFont(Gdx.files.internal("font/black.fnt"), false);
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
