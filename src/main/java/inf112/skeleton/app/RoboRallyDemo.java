package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Screen.MainMenuScreen;

/**
 * Game to send to the application
 * Has gameScreen, which contains all the logic. This of course will be changed later.
 */
public class RoboRallyDemo extends Game {

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        GameScreen gameScreen = new GameScreen();
//        setScreen(gameScreen);
        // TEST: Main menu screen
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
