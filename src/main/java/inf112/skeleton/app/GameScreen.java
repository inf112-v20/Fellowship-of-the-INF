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

public class GameScreen implements Screen {
    private TiledMap map;
    private TiledMapTileSet tiles;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Viewport gridPort;
    private final int MAP_WIDTH = 12;
    private final int TILE_WIDTH_DPI = 300;
    private final int MAP_WITDTH_DPI = MAP_WIDTH * TILE_WIDTH_DPI;

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
        // TODO or error in camera position
        //camera.position.x = (float) MAP_WIDTH/2;
        //camera.translate(MAP_WITDTH_DPI, MAP_WITDTH_DPI);
        //camera.position.set(6, 6, 0);
        camera.update();

        //TODO possibly generalize 300 to be able to apply other resolutions
        mapRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / 300);
        // Layers
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");

        initializePlayer();
    }

    public void initializePlayer() {
        player = new Player(0);
        TiledMapTileLayer.Cell playerCell = player.getPlayerCell();
        playerLayer.setCell((int) player.getPos().x, (int) player.getPos().y, playerCell);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(camera);
        update();
        mapRenderer.render();
    }

    /**
     * Update coordinate of player
     */
    public void update() {
        playerLayer.setCell((int) player.getPos().x, (int) player.getPos().y, null);
        handleInput();
        playerLayer.setCell((int) player.getPos().x, (int) player.getPos().y, player.getPlayerCell());
    }

    public void handleInput() {
        Vector2 dir = player.getPos();
        float newX = dir.x;
        float newY = dir.y;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            newY += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            newY -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            newX -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            newX += 1;
        }
        player.setDir(newX, newY);
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

    }
}
