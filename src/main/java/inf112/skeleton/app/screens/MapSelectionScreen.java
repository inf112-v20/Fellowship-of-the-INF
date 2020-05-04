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
        float mapScale = 0.25f;
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

        //Text that says choose map
        picture = new Sprite(new Texture("menu/ChooseMap.png"));
        Image chooseMap = new Image(new SpriteDrawable(picture));
        chooseMap.setPosition((width - chooseMap.getWidth()) / 2, logo.getY()-yPadding-chooseMap.getHeight()/2);
        stage.addActor(chooseMap);

        int xPadding = 100;

        ImageButton stage1Button;
        ImageButton stage2Button;
        ImageButton stage3Button;
        ImageButton stage4Button;
        ImageButton stage5Button;
        ImageButton stage6Button;

        //Stage 1 button
        yPadding = 400;
        picture = new Sprite(new Texture("menu/mapButtons/Spin.png"));
        stage1Button = new ImageButton(new SpriteDrawable(picture));
        stage1Button.setTransform(true);
        stage1Button.setScale(mapScale);
        stage1Button.setPosition(xPadding, yPadding);
        stage1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Original map: RoborallyBoard_debugged.tmx
                //Test map for conveyorbelts: conveyorBeltTestMap.tmx
                game.setScreen(new GameScreen(game, "maps/RoborallyBoard_Spin.tmx", playerCount, difficulty));
            }
        });
        stage.addActor(stage1Button);
        // Stage 2 button
        picture = new Sprite(new Texture("menu/mapButtons/Vault_Assault.png"));
        stage2Button = new ImageButton(new SpriteDrawable(picture));
        stage2Button.setPosition(stage1Button.getX() + xPadding + stage2Button.getWidth() * mapScale, yPadding);
        stage2Button.setTransform(true);
        stage2Button.setScale(mapScale);
        stage2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "maps/RoborallyBoard_Vault_Assault.tmx", playerCount, difficulty));
            }
        });
        stage.addActor(stage2Button);

        // Stage 3 button
        picture = new Sprite(new Texture("menu/mapButtons/Exchange.png"));
        stage3Button = new ImageButton(new SpriteDrawable(picture));
        stage3Button.setPosition(stage2Button.getX() + stage3Button.getWidth() * mapScale, yPadding);
        stage3Button.setTransform(true);
        stage3Button.setScale(mapScale);
        stage3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "maps/RoborallyBoard_Exchange.tmx", playerCount, difficulty));
            }
        });
        stage.addActor(stage3Button);

        // Stage 4 button
        picture = new Sprite(new Texture("menu/mapButtons/Debugged.png"));
        stage4Button = new ImageButton(new SpriteDrawable(picture));
        stage4Button.setPosition(stage3Button.getX() + stage4Button.getWidth() * mapScale + xPadding / 2, yPadding);
        stage4Button.setTransform(true);
        stage4Button.setScale(mapScale);
        stage4Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "maps/RoborallyBoard_Debugged.tmx", playerCount, difficulty));
            }
        });
        stage.addActor(stage4Button);

        yPadding -= stage1Button.getHeight() * mapScale + 50;

        // Stage 5 button
        picture = new Sprite(new Texture("menu/mapButtons/Maelstrom.png"));
        stage5Button = new ImageButton(new SpriteDrawable(picture));
        stage5Button.setPosition(stage1Button.getX(), yPadding);
        stage5Button.setTransform(true);
        stage5Button.setScale(mapScale);
        stage5Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "maps/RoborallyBoard_Maelstrom.tmx", playerCount, difficulty));
            }
        });
        stage.addActor(stage5Button);

        // Stage 6 button
        picture = new Sprite(new Texture("menu/mapButtons/Vault.png"));
        stage6Button = new ImageButton(new SpriteDrawable(picture));
        stage6Button.setPosition(stage5Button.getX() + stage6Button.getWidth() * mapScale + xPadding, yPadding);
        stage6Button.setTransform(true);
        stage6Button.setScale(mapScale);
        stage6Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, "maps/RoborallyBoard_Vault.tmx", playerCount, difficulty));
            }
        });
        stage.addActor(stage6Button);

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
