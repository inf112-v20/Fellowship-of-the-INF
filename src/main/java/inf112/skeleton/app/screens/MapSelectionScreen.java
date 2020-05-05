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
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.player.AIPlayer.Difficulty;

public class MapSelectionScreen implements Screen {
    private RoboRallyGame game;
    private float width;
    private float height;
    private int xPadding;
    private int yPadding;
    private float MAP_SCALE = 0.25f;

    private int playerCount;
    private Difficulty difficulty;
    private Stage stage;

    public MapSelectionScreen(RoboRallyGame game, int playerCount, Difficulty difficulty) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from Main.java
        this.height = Gdx.graphics.getHeight();
        this.playerCount = playerCount;
        this.difficulty = difficulty;
    }

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
        yPadding = 50;
        picture = new Sprite(new Texture("menu/RoboRally_logo.png"));
        Image logo = new Image(new SpriteDrawable(picture));
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 2);
        logo.setPosition((width - logo.getWidth()) / 2, height - (logo.getHeight() + yPadding));
        stage.addActor(logo);

        //Text that says choose map
        picture = new Sprite(new Texture("menu/ChooseMap.png"));
        Image chooseMap = new Image(new SpriteDrawable(picture));
        chooseMap.setPosition((width - chooseMap.getWidth()) / 2, logo.getY()-yPadding-chooseMap.getHeight()/2);
        stage.addActor(chooseMap);

        xPadding = 100;
        yPadding = 400;

        ImageButton stage1Button = createMapButton("Spin");
        stage1Button.setPosition(xPadding, yPadding);

        ImageButton stage2Button = createMapButton("Vault_Assault");
        stage2Button.setPosition(stage1Button.getX() + xPadding + stage2Button.getWidth() * MAP_SCALE, yPadding);

        ImageButton stage3Button = createMapButton("Exchange");
        stage3Button.setPosition(stage2Button.getX() + stage3Button.getWidth() * MAP_SCALE, yPadding);

        ImageButton stage4Button = createMapButton("Debugged");
        stage4Button.setPosition(stage3Button.getX() + stage4Button.getWidth() * MAP_SCALE + xPadding / 2, yPadding);

        yPadding -= stage1Button.getHeight() * MAP_SCALE + 50;

        ImageButton stage5Button = createMapButton("Maelstrom");
        stage5Button.setPosition(stage1Button.getX(), yPadding);

        ImageButton stage6Button = createMapButton("Vault");
        stage6Button.setPosition(stage5Button.getX() + stage6Button.getWidth() * MAP_SCALE + xPadding, yPadding);

        // Back button
        picture = new Sprite(new Texture("menu/navbuttons/BackButton.png"));
        ImageButton backButton = new ImageButton(new SpriteDrawable(picture));
        backButton.setPosition((width - xPadding) - backButton.getWidth(), yPadding);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backButton);

        Gdx.input.setInputProcessor(stage);
    }


    public ImageButton createMapButton(final String mapName){
        Sprite picture = new Sprite(new Texture("menu/mapButtons/" + mapName + ".png"));
        ImageButton stageButton = new ImageButton(new SpriteDrawable(picture));
        stageButton.setTransform(true);
        stageButton.setScale(MAP_SCALE);
        stageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "maps/RoborallyBoard_"+ mapName + ".tmx", playerCount, difficulty));
            }
        });
        stage.addActor(stageButton);
        return stageButton;
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
