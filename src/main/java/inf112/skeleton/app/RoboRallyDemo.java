package inf112.skeleton.app;

import com.badlogic.gdx.Game;

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
