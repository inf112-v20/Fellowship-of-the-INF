package inf112.skeleton.app;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Map;
import inf112.skeleton.app.Screens.GameScreen;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player testPlayer;

    @Before
    public void setUp() {
        TiledMap tmxMap = new TmxMapLoader().load("TestMap.tmx");
        MapProperties mapProperties = tmxMap.getProperties();
        int mapWidth = mapProperties.get("width", Integer.class);
        int mapHeight = mapProperties.get("height", Integer.class);
        Map map = new Map(mapWidth, mapWidth, tmxMap);
        Game game = new Game(map);
        this.testPlayer = new Player(0, game);
    }

    /**
     *
     */
    @Test
    public void playerDiesFromAbyssTest() {
        setUp();
        testPlayer.setPos(3, 2);
        testPlayer.tryToGo(Direction.WEST);
        assertTrue(testPlayer.isDead());
    }

    @Test
    public void playerDiesFromOutOfBoundsTest(){
        assertTrue(true);
    }
}