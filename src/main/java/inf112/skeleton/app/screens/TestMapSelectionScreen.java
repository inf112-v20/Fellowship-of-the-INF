package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RoboRallyGame;
import inf112.skeleton.app.player.AIPlayer.Difficulty;

public class TestMapSelectionScreen implements Screen {
    private RoboRallyGame game;
    private float width;
    private float height;
    private String[] mapNameList;
    private String mapName;
    private Difficulty difficulty = Difficulty.EASY;

    private Stage stage;

    public TestMapSelectionScreen(RoboRallyGame game) {
        this.game = game;
        this.width = Gdx.graphics.getWidth(); // width and height from Main.java
        this.height = Gdx.graphics.getHeight();
        generateMapNameList();
    }

    private void generateMapNameList() {
        mapNameList = new String[]{
                "ConveyorBeltTestMap",
                "RoborallyBoard_AI_Testmap",
                "pushers_test_map_1",
                "pushers_test_map_2",
                "pushers_test_map_3",
                "pushers_test_map_4",
                "robot_pushing_test_map_1",
                "robot_pushing_test_map_2",
                "robot_pushing_test_map_3"
        };
        mapName = mapNameList[0];
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

        // Back button
        yPadding = 400;
        int xPadding = 80;
        //Back button
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

        //"Start Test" button
        yPadding = 150;
        xPadding = 100;
        ImageButton startTestButton;
        picture = new Sprite(new Texture("menu/navbuttons/Stage1Button.png"));
        startTestButton = new ImageButton(new SpriteDrawable(picture));
        startTestButton.setPosition(backButton.getX() - xPadding, backButton.getY() + picture.getHeight() + yPadding);
        startTestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Original map: RoborallyBoard_debugged.tmx
                //Test map for conveyorbelts: conveyorBeltTestMap.tmx
                game.setScreen(new GameScreen("maps/testMaps/" + mapName + ".tmx", 4, difficulty));
            }
        });

        stage.addActor(startTestButton);


        yPadding = 300;
        xPadding = 700;
        int selectBoxWidth = 300;
        int selectBoxHeight = 50;
        //Skin Source: https://github.com/libgdx/libgdx-skins/tree/master/skins/visui/assets
        TextureAtlas dropdownAtlas = new TextureAtlas(Gdx.files.internal("menu/navbuttons/uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("menu/navbuttons/uiskin.json"), dropdownAtlas);
        final SelectBox<String> dropDownMenu = new SelectBox<>(skin);
        dropDownMenu.setSize(selectBoxWidth, selectBoxHeight);
        dropDownMenu.setPosition(logo.getX(), logo.getY() - yPadding * 0.5f);
        dropDownMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mapName = (dropDownMenu.getSelected());
            }
        });
        dropDownMenu.setItems(mapNameList);
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