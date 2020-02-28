package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import inf112.skeleton.app.Screens.GameScreen;
import inf112.skeleton.app.Screens.MainMenuScreen;

/**
 * Game to send to the application
 * Has gameScreen, which contains all the logic. This of course will be changed later.
 */
public class RoboRallyDemo extends Game {
    @Override
    public void create() {
        MainMenuScreen menuScreen = new MainMenuScreen(this);
        setScreen(menuScreen);
    }

    @Override
    public void render() {
        super.render();
    }
}
