package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.audio.Wav;
import com.badlogic.gdx.files.FileHandle;
import inf112.skeleton.app.screens.MainMenuScreen;

/**
 * Game to send to the application
 * Has gameScreen, which contains all the logic. This of course will be changed later.
 */
public class RoboRallyGame extends Game {
    private Wav.Music backgroundMusic;

    @Override
    public void create() {
        MainMenuScreen menuScreen = new MainMenuScreen(this);
        setScreen(menuScreen);
        startMusic();
    }

    @Override
    public void render() {
        super.render();
    }

    private void startMusic() {
        String musicPath = "sounds/patricklieberkind__futuristic-rhythmic-game-ambience.wav";
        backgroundMusic = (Wav.Music) Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.6f);
        backgroundMusic.play();
    }
}
