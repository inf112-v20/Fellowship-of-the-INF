package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.audio.Wav;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.player.AIPlayer;
import inf112.skeleton.app.player.AIPlayer.Difficulty;

public class PlayerSelectionScreen implements Screen {
    private RoboRallyGame game;
    private float width;
    private float height;
    private Difficulty difficulty = Difficulty.EASY;
    private Wav.Sound sound;

    private Stage stage;

    public PlayerSelectionScreen(RoboRallyGame game) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from Main.java
        this.height = Gdx.graphics.getHeight();
        sound = (Wav.Sound) Gdx.audio.newSound(Gdx.files.internal("sounds/reitanna__thunk.wav"));

    }

    //TODO Lots of repeated code, maybe make button construction into methods?
    @Override
    public void show() {
        stage = new Stage();

        // picture = all actors on stage
        // Background
        Sprite picture = new Sprite(new Texture("menu/StageSelectBackground.png"));
        Image background = new Image(new SpriteDrawable(picture));
        background.setSize(width, height);
        background.setPosition(0, 0);
        stage.addActor(background);

        // Logo
        int yPadding = 50;
        picture = new Sprite(new Texture("menu/RoboRally_logo.png"));
        Image logo = new Image(new SpriteDrawable(picture));
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 2);
        logo.setPosition((width - logo.getWidth()) / 2, height - (logo.getHeight() + yPadding));
        stage.addActor(logo);

        //Text that says choose amount of players
        picture = new Sprite(new Texture("menu/ChoosePlayers.png"));
        Image choosePlayers = new Image(new SpriteDrawable(picture));
        choosePlayers.setPosition((width - choosePlayers.getWidth()) / 2 - 50, (height - yPadding*2 - logo.getHeight()));
        stage.addActor(choosePlayers);

        // 1 Players button
        yPadding = 400;
        int xPadding = 80;
        picture = new Sprite(new Texture("menu/playerbuttons/1Player.png"));
        ImageButton player1Button = new ImageButton(new SpriteDrawable(picture));
        player1Button.setPosition(xPadding, yPadding);
        player1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new StageSelectionScreen(game, 1, difficulty));
            }
        });
        stage.addActor(player1Button);

        // 2 Players button
        picture = new Sprite(new Texture("menu/playerbuttons/2Players.png"));
        ImageButton player2Button = new ImageButton(new SpriteDrawable(picture));
        player2Button.setPosition(2 * xPadding + player1Button.getWidth(), yPadding);
        player2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new StageSelectionScreen(game, 2, difficulty));
            }
        });
        stage.addActor(player2Button);

        //3 Players button
        picture = new Sprite(new Texture("menu/playerbuttons/3Players.png"));
        ImageButton player3Button = new ImageButton(new SpriteDrawable(picture));
        player3Button.setPosition(3 * xPadding + player1Button.getWidth() + player2Button.getWidth(), yPadding);
        player3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new StageSelectionScreen(game, 3, difficulty));
            }
        });
        stage.addActor(player3Button);

        //4 Players button
        picture = new Sprite(new Texture("menu/playerbuttons/4Players.png"));
        ImageButton player4Button = new ImageButton(new SpriteDrawable(picture));
        player4Button.setPosition(width - xPadding - player4Button.getWidth(), yPadding);
        player4Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new StageSelectionScreen(game, 4,difficulty));
            }
        });
        stage.addActor(player4Button);

        //5 Players button
        picture = new Sprite(new Texture("menu/playerbuttons/5Players.png"));
        ImageButton player5Button = new ImageButton(new SpriteDrawable(picture));
        player5Button.setPosition(xPadding, yPadding/4);
        player5Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new StageSelectionScreen(game, 5,difficulty));
            }
        });
        stage.addActor(player5Button);

        //6 Players button
        picture = new Sprite(new Texture("menu/playerbuttons/6Players.png"));
        ImageButton player6Button = new ImageButton(new SpriteDrawable(picture));
        player6Button.setPosition(2 * xPadding + player1Button.getWidth(), yPadding/4);
        player6Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new StageSelectionScreen(game, 6,difficulty));
            }
        });
        stage.addActor(player6Button);

        //7 Players button
        picture = new Sprite(new Texture("menu/playerbuttons/7Players.png"));
        ImageButton player7Button = new ImageButton(new SpriteDrawable(picture));
        player7Button.setPosition(3 * xPadding + player1Button.getWidth() + player2Button.getWidth(), yPadding/4);
        player7Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new StageSelectionScreen(game, 7,difficulty));
            }
        });
        stage.addActor(player7Button);

        //8 Players button
        picture = new Sprite(new Texture("menu/playerbuttons/8Players.png"));
        ImageButton player8Button = new ImageButton(new SpriteDrawable(picture));
        player8Button.setPosition(width - xPadding - player8Button.getWidth(), yPadding/4);
        player8Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new StageSelectionScreen(game, 8,difficulty));
            }
        });
        stage.addActor(player8Button);

        //Back button
        picture = new Sprite(new Texture("menu/navbuttons/BackButton.png"));
        ImageButton backButton = new ImageButton(new SpriteDrawable(picture));
        backButton.setPosition((width - xPadding) - backButton.getWidth(), height-yPadding);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backButton);

        //Skin Source: https://github.com/libgdx/libgdx-skins/tree/master/skins/visui/assets
        TextureAtlas dropdownAtlas = new TextureAtlas(Gdx.files.internal("menu/navbuttons/uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("menu/navbuttons/uiskin.json"), dropdownAtlas);
        final SelectBox<Difficulty> dropDownMenu = new SelectBox<>(skin);
        dropDownMenu.setSize(100,50);
        dropDownMenu.setPosition(player1Button.getX(), height-yPadding);
        dropDownMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound.play();
                difficulty = (dropDownMenu.getSelected());
            }
        });
        dropDownMenu.setItems(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD, Difficulty.EXPERT);
        stage.addActor(dropDownMenu);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); //added to fix dropDownMenu
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
