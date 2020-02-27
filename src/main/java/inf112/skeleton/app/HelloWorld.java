package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * This class is now obsolete, but can be used for reference.
 * All the layers have been added to this class. Perhaps move this code to an appropriate class
 * later in the project
 */

public class HelloWorld implements ApplicationListener {
    private SpriteBatch batch;
    private BitmapFont font;
    private TiledMap map;
    private TiledMapTileLayer floor, repair, opCard, abyss, conveyorBelt, expressBelt, cog, pusher, lasers, laserSource, wall, player;
    private TiledMapTileSet tiles;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Viewport gridPort;
    private final int MAP_WIDTH = 12;
    private final int TILE_WIDTH_DPI = 300;
    private final int MAP_WITDTH_DPI = MAP_WIDTH *TILE_WIDTH_DPI;

    private TmxMapLoader mapLoader;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.GREEN);
        mapLoader = new TmxMapLoader();

        // TODO Possible error in pathing
        map = mapLoader.load("assets\\RoborallyBoard.tmx");

        floor           = (TiledMapTileLayer) map.getLayers().get("Floor");
        repair          = (TiledMapTileLayer) map.getLayers().get("Repair");
        opCard          = (TiledMapTileLayer) map.getLayers().get("OpCard");
        abyss           = (TiledMapTileLayer) map.getLayers().get("Abyss");
        conveyorBelt    = (TiledMapTileLayer) map.getLayers().get("Converyor Belt");
        expressBelt     = (TiledMapTileLayer) map.getLayers().get("Express Belt");
        cog             = (TiledMapTileLayer) map.getLayers().get("Cog");
        pusher          = (TiledMapTileLayer) map.getLayers().get("Pusher");
        lasers          = (TiledMapTileLayer) map.getLayers().get("Laser");
        laserSource     = (TiledMapTileLayer) map.getLayers().get("Laser Source");
        wall            = (TiledMapTileLayer) map.getLayers().get("Wall");
        player          = (TiledMapTileLayer) map.getLayers().get("Player");

        tiles = map.getTileSets().getTileSet("tileset.png");


        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WITDTH_DPI, MAP_WITDTH_DPI, camera);
        camera.setToOrtho(false, MAP_WIDTH/2, MAP_WIDTH/2);
        //  or error in camera position
        //camera.position.x = (float) MAP_WIDTH/2;
        //camera.translate(MAP_WITDTH_DPI, MAP_WITDTH_DPI);
        //camera.position.set(6, 6, 0);
        camera.update();

        // possibly generalize 300 to be able to apply other resolutions
        mapRenderer = new OrthogonalTiledMapRenderer(map);

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    //TODO from the tutorial, most likely OK.
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(camera);

        mapRenderer.render();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
