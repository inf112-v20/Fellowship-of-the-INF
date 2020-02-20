package inf112.skeleton.app.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.RoboRallyDemo;

public class MainMenuScreen implements Screen {

    private RoboRallyDemo game;

    // Button scales
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 150;
    // Button position
    private static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_Y = 300;
    private Texture playButtonActive;
    private Texture playButtonInactive;
    private Texture exitButtonActive;
    private Texture exitButtonInactive;

    public MainMenuScreen (RoboRallyDemo game) {
        this.game = game;

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

        // TODO: fix positioning
        int gameWidth = 3600;
        int gameHeight = 3600;
        int buttonX = gameWidth/2 - BUTTON_WIDTH/2;

        if (Gdx.input.getX() < buttonX + BUTTON_WIDTH && Gdx.input.getX() > buttonX && gameHeight - Gdx.input.getY() < EXIT_BUTTON_Y + BUTTON_HEIGHT && gameHeight - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonActive, buttonX, EXIT_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        else {
            game.batch.draw(exitButtonInactive, buttonX, EXIT_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
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
