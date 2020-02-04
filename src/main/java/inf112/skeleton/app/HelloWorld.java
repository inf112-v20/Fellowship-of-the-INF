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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class HelloWorld implements ApplicationListener {
    private SpriteBatch batch;
    private BitmapFont font;
    private TiledMap board;
    private TiledMapTileLayer floor, repair, opCard, abyss, conveyorBelt, expressBelt, cog, pusher, lasers, laserSource, wall, player;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private TmxMapLoader mapLoader;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.GREEN);
        mapLoader = new TmxMapLoader();

        // TODO Possible error in pathing
        board = mapLoader.load("assets\\RoborallyBoard.tmx");

        floor           = (TiledMapTileLayer) board.getLayers().get("Floor");
        repair          = (TiledMapTileLayer) board.getLayers().get("Repair");
        opCard          = (TiledMapTileLayer) board.getLayers().get("OpCard");
        abyss           = (TiledMapTileLayer) board.getLayers().get("Abyss");
        conveyorBelt    = (TiledMapTileLayer) board.getLayers().get("Converyor Belt");
        expressBelt     = (TiledMapTileLayer) board.getLayers().get("Express Belt");
        cog             = (TiledMapTileLayer) board.getLayers().get("Cog");
        pusher          = (TiledMapTileLayer) board.getLayers().get("Pusher");
        lasers          = (TiledMapTileLayer) board.getLayers().get("Laser");
        laserSource     = (TiledMapTileLayer) board.getLayers().get("Laser Source");
        wall            = (TiledMapTileLayer) board.getLayers().get("Wall");
        player          = (TiledMapTileLayer) board.getLayers().get("Player");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 12, 12);
        // TODO or error in camera position
        camera.position.set(6, 6, 0);
        camera.update();

        //TODO possibly generalize 300 to be able to apply other resolutions
        mapRenderer = new OrthogonalTiledMapRenderer(board, 1/300);
        mapRenderer.setView(camera);

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
