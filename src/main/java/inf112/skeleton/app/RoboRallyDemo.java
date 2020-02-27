package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import inf112.skeleton.app.Screens.GameScreen;

/**
 * Game to send to the application
 * Has gameScreen, which contains all the logic. This of course will be changed later.
 */
public class RoboRallyDemo extends Game {
    @Override
    public void create() {
        GameScreen gameScreen = new GameScreen();
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }
}
