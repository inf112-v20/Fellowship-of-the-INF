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

public class StageSelectionScreen implements Screen {
    private RoboRallyGame game;
    private float width;
    private float height;

    private int playerCount;
    private Stage stage;

    public StageSelectionScreen(RoboRallyGame game, int playerCount) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from Main.java
        this.height = Gdx.graphics.getHeight();
        this.playerCount = playerCount;
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
        int yPadding = 50;
        picture = new Sprite(new Texture("menu/RoboRally_logo.png"));
        Image logo = new Image(new SpriteDrawable(picture));
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 2);
        logo.setPosition((width - logo.getWidth()) / 2, height - (logo.getHeight() + yPadding));
        stage.addActor(logo);

        int xPadding = 100;

        ImageButton stage1Button;
        ImageButton stageTestButton;
        ImageButton stage2Button;
        if (playerCount < 5) {
            //Stage 1 button
            yPadding = 400;
            picture = new Sprite(new Texture("menu/navbuttons/Stage1Button.png"));
            stage1Button = new ImageButton(new SpriteDrawable(picture));
            stage1Button.setPosition(xPadding, yPadding);
            stage1Button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //Original map: RoborallyBoard_debugged.tmx
                    //Test map for conveyorbelts: conveyorBeltTestMap.tmx
                    game.setScreen(new GameScreen("pushers_test_map_4.tmx", playerCount));                }
            });

            stage.addActor(stage1Button);
            // Stage 2 button
            picture = new Sprite(new Texture("menu/navbuttons/Stage2Button.png"));
            stage2Button = new ImageButton(new SpriteDrawable(picture));
            stage2Button.setPosition((width - xPadding) - stage2Button.getWidth(), yPadding);
            stage2Button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen("maps/RoborallyBoard_Vault_Assault.tmx", playerCount));
                }
            });
            stage.addActor(stage2Button);

            if (playerCount == 1) {
                // testStage button
                picture = new Sprite(new Texture("menu/navbuttons/TestStageButton.png"));
                stageTestButton = new ImageButton(new SpriteDrawable(picture));
                stageTestButton.setPosition(width / 2 - stageTestButton.getWidth() / 2, yPadding);
                stageTestButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new GameScreen("maps/TestMap.tmx", playerCount));
                    }
                });
                stage.addActor(stageTestButton);
            }
        } else {
            //Stage 1 button
            yPadding = 400;
            picture = new Sprite(new Texture("menu/navbuttons/Stage1Button.png"));
            stage1Button = new ImageButton(new SpriteDrawable(picture));
            stage1Button.setPosition(xPadding, yPadding);
            stage1Button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //Original map: RoborallyBoard_debugged.tmx
                    //Test map for conveyorbelts: conveyorBeltTestMap.tmx
                    game.setScreen(new GameScreen("maps/RoborallyBoard_debugged.tmx", playerCount));
                }
            });

            stage.addActor(stage1Button);
            // Stage 2 button
            picture = new Sprite(new Texture("menu/navbuttons/Stage2Button.png"));
            stage2Button = new ImageButton(new SpriteDrawable(picture));
            stage2Button.setPosition((width - xPadding) - stage2Button.getWidth(), yPadding);
            stage2Button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen("maps/RoborallyBoard_Vault_Assault.tmx", playerCount));
                }
            });
            stage.addActor(stage2Button);

            // testStage button
            picture = new Sprite(new Texture("menu/navbuttons/TestStageButton.png"));
            stageTestButton = new ImageButton(new SpriteDrawable(picture));
            stageTestButton.setPosition(width / 2 - stageTestButton.getWidth() / 2, yPadding);
            stageTestButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen("maps/TestMap.tmx", playerCount));
                    // Test map 1 for robot pushing: robot_pushing_test_map_1.tmx
                    //game.setScreen(new GameScreen("robot_pushing_test_map_1.tmx"));
                    // Test map 2 for robot pushing: robot_pushing_test_map_2.tmx
                    //game.setScreen(new GameScreen("robot_pushing_test_map_2.tmx"));
                    // Test map 3 for robot pushing: robot_pushing_test_map_3.tmx
                    //game.setScreen(new GameScreen("robot_pushing_test_map_3.tmx"));
                    // Test map for conveyorbelts: conveyorBeltTestMap.tmx
                    //game.setScreen(new GameScreen("conveyorBeltTestMap.tmx"));
                }
            });
            stage.addActor(stageTestButton);


        }
        // Back button
        picture = new Sprite(new Texture("menu/navbuttons/BackButton.png"));
        ImageButton backButton = new ImageButton(new SpriteDrawable(picture));
        backButton.setPosition((width - xPadding) - backButton.getWidth(), 100);
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
