package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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

    public GameScreen() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("assets\\RoborallyBoard.tmx");
        tiles = map.getTileSets().getTileSet("tileset.png");
        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WITDTH_DPI, MAP_WITDTH_DPI, camera);
        camera.setToOrtho(false, MAP_WIDTH, MAP_WIDTH);
        camera.update();
        mapRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / TILE_WIDTH_DPI);

        // Layers, add more later
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");

        initializePlayer();
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(camera);
        update(); //make changes to board, if there are any
        mapRenderer.render();
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
            newY += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            newY -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            newX -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            newX += 1;
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
