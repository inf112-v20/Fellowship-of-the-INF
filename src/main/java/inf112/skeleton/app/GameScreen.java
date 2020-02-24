package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Grid.PieceGrid;

/**
 * Game screen at the moment only shows a board with a playerLayer, and a player
 */
public class GameScreen implements Screen {
    private TiledMap map; //roborally board
    private TiledMapTileSet tiles;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Viewport gridPort;
    private final int MAP_WIDTH = 12; //dimentions of board
    private final int TILE_WIDTH_DPI = 300; //pixel width per cell
    private final int MAP_WITDTH_DPI = MAP_WIDTH * TILE_WIDTH_DPI; //total width of map in pixels
    private Player player;
    private TiledMapTileLayer playerLayer;
    private TmxMapLoader mapLoader;
    private CardButton cardButton1;
    private CardButton cardButton2;
    private CardButton cardButton3;
    private CardButton cardButton_1;
    private CardButton cardButtonR;
    private CardButton cardButtonL;
    private CardButton cardButtonU;
    private Stage stage;
    private Texture texture;
    private SpriteBatch batch;

    public GameScreen() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("RoborallyBoard.tmx");
        tiles = map.getTileSets().getTileSet("tileset.png");
        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WITDTH_DPI, MAP_WITDTH_DPI, camera);
        camera.setToOrtho(false, MAP_WIDTH, MAP_WIDTH*2);
        camera.position.y = 0;
        camera.update();
        mapRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / TILE_WIDTH_DPI);
        // Layers, add more later
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        initializePlayer();
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("cardslot.png"));

        stage = new Stage(new ScreenViewport()); //Set up a stage for the ui
        Gdx.input.setInputProcessor(stage); //Start taking input from the ui

        cardButton1 = new CardButton(player, playerLayer,1,0,0);
        stage.addActor(cardButton1.getButton());//Add the button to the stage to perform rendering and take input.

        cardButton2 = new CardButton(player, playerLayer,2,0,150);
        stage.addActor(cardButton2.getButton());

        cardButton3 = new CardButton(player, playerLayer,3,0,300);
        stage.addActor(cardButton3.getButton());

        cardButton_1 = new CardButton(player, playerLayer,-1,0,450);
        stage.addActor(cardButton_1.getButton());

        cardButtonR = new CardButton(player, playerLayer,0,-1,600);
        stage.addActor(cardButtonR.getButton());

        cardButtonL = new CardButton(player, playerLayer,0,1,750);
        stage.addActor(cardButtonL.getButton());

        cardButtonU = new CardButton(player, playerLayer,0,2,900);
        stage.addActor(cardButtonU.getButton());
        stage.addActor(cardButtonU.getLockInButton());
    }

    /**
     * Create a simple player with the ability to move around the board
     * Add it to the playerLayer
     */

    public void initializePlayer() {
        player = new Player(0);
        TiledMapTileLayer.Cell playerCell = player.getPlayerCell();
        playerLayer.setCell((int) player.getPos().x, (int) player.getPos().y, playerCell);
    }

    @Override
    public void show() {

    }

    /**
     *This method is called continuously
     * @param v deltaTime
     */
    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(camera);
        update(); //make changes to board, if there are any
        batch.begin();
        for (int i = 0; i <5 ; i++) {
            batch.draw(texture, 150*i, 75, texture.getWidth()*0.41f, texture.getHeight()*0.41f);
        }
        batch.end();
        mapRenderer.render();
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.draw(); //Draw the ui
    }
    /**
     * Update all changes to board
     * For now they are only movements of player
     */
    public void update() {
        playerLayer.setCell((int) player.getPos().x, (int) player.getPos().y, null);
        handleInput();
        playerLayer.setCell((int) player.getPos().x, (int) player.getPos().y, player.getPlayerCell());
    }

    /**
     * Changes the coordinates of the player based on user input
     */
    public void handleInput() {
        Vector2 pos = player.getPos();
        float newX = pos.x;
        float newY = pos.y;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (pos.y + 1 < MAP_WIDTH) newY += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (pos.y - 1 >= 0) newY -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (pos.x - 1 >= 0) newX -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (pos.x + 1 < MAP_WIDTH) newX += 1;
        }
        player.setPos(newX, newY);
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

    //TODO understand and implement dispose
    @Override
    public void dispose() {

    }
}
