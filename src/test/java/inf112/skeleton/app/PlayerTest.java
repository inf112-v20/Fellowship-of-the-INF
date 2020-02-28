package inf112.skeleton.app;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.skeleton.app.Grid.Map;
import org.junit.Before;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player testPlayer;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private MapProperties mapProperties;
    private int mapWidth;
    private int mapHeight;
    private int tileWidthDPI;
    private Map gameMap;
    private Game game;

    @Before
    public void setUp() {
        // Set up of a game
        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("TestMap.tmx");
        this.mapProperties = map.getProperties();
        this.mapWidth = mapProperties.get("width", Integer.class);
        this.mapHeight = mapProperties.get("height", Integer.class);
        this.tileWidthDPI = mapProperties.get("tilewidth", Integer.class);
        this.gameMap = new Map(mapWidth, mapHeight, map);
        this.game = new Game(gameMap);
        this.testPlayer = new Player(0, game);
    }

    public void testPlayerDiesInAbyss() {
        setUp();
        testPlayer.setPos(2, 3);
        assertTrue(true);
    }

}