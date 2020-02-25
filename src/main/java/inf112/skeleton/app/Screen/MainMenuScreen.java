package inf112.skeleton.app.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.RoboRallyDemo;

public class MainMenuScreen implements Screen {

    private RoboRallyDemo game;

    // Button scales
    private static final int EXIT_BUTTON_WIDTH = 300;
    private static final int EXIT_BUTTON_HEIGHT = 150;
    private static final int PLAY_BUTTON_WIDTH = 330;
    private static final int PLAY_BUTTON_HEIGHT = 150;
    private static final int LOGO_WIDTH = 3600/4;
    private static final int LOGO_HEIGHT = 500;
    // Button position
    private static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_Y = 300;
    private static final int LOGO_Y = 650;

    private Texture RoboRallyLogo;
    private Texture playButtonActive;
    private Texture playButtonInactive;
    private Texture exitButtonActive;
    private Texture exitButtonInactive;

    public MainMenuScreen (RoboRallyDemo game) {
        this.game = game;

        RoboRallyLogo = new Texture("RoboRally_logo.png");
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        // Logo
        game.batch.draw(RoboRallyLogo, 100, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);

        // TODO: fix mouse hovering
        // Exit button
        int buttonX = game.mapSize/8 - EXIT_BUTTON_WIDTH/8;
        if (Gdx.input.getX() < buttonX + EXIT_BUTTON_WIDTH && Gdx.input.getX() > buttonX && game.mapSize - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && game.mapSize - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonActive, buttonX, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            // TEST TODO fjern
            System.out.println("TEST: Hovering over exit button now.");
        }
        else {
            game.batch.draw(exitButtonInactive, buttonX, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        // Play button
        buttonX = game.mapSize/8 - PLAY_BUTTON_WIDTH/8;
        if (Gdx.input.getX() < buttonX + PLAY_BUTTON_WIDTH && Gdx.input.getX() > buttonX) {
            game.batch.draw(playButtonActive, buttonX, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }
        else {
            game.batch.draw(playButtonInactive, buttonX, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        game.batch.end();
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

    }

}
