package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.grid.Map;
import inf112.skeleton.app.player.Player;

import java.util.ArrayList;

/**
 * Game screen at the moment only shows a board with a playerLayer, and a player
 */
public class GameScreen implements Screen {
    private TiledMap map; //roborally board
    private TiledMapTileSet tiles;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Viewport gridPort;
    private int MAP_WIDTH; //dimensions of board
    private int MAP_HEIGHT;
    private int TILE_WIDTH_DPI; //pixel width per cell
    private int MAP_WIDTH_DPI; //total width of map in pixels
    private TiledMapTileLayer playerLayer;
    private TmxMapLoader mapLoader;
    private MapProperties mapProperties;
    private Stage stage;
    private UIScreen uiScreen;
    private Player player;
    private Game game;


    public GameScreen(String mapName) {
        mapLoader = new TmxMapLoader();
        this.map = mapLoader.load(mapName);
        mapProperties = map.getProperties();
        MAP_WIDTH = mapProperties.get("width", Integer.class);
        MAP_HEIGHT = mapProperties.get("height", Integer.class);
        TILE_WIDTH_DPI = mapProperties.get("tilewidth", Integer.class);
        MAP_WIDTH_DPI = MAP_WIDTH * TILE_WIDTH_DPI;

        tiles = this.map.getTileSets().getTileSet("tileset.png");
        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WIDTH_DPI * 2, MAP_WIDTH_DPI, camera);
        camera.translate(MAP_WIDTH_DPI, MAP_WIDTH_DPI / 2);
        mapRenderer = new OrthogonalTiledMapRenderer(this.map);
        // Layers, add more later
        playerLayer = (TiledMapTileLayer) this.map.getLayers().get("Player");
        Map map = new Map(MAP_WIDTH, MAP_WIDTH, this.map);
        game = new Game(map);
        initializePlayers();
        //UI gets game deck from game class
        uiScreen = new UIScreen(MAP_WIDTH_DPI * 2, game, this);
    }

    /**
     * Create a simple player with the ability to move around the board
     * Add it to the playerLayer
     */
    public void initializePlayers() {
        for (Player playerToInitialize : game.getListOfPlayers()) {
            TiledMapTileLayer.Cell playerCell = playerToInitialize.getPlayerCell();
            playerLayer.setCell(playerToInitialize.getPos().getX(), playerToInitialize.getPos().getY(), playerCell);
        }
        player = game.getPlayer();
    }

    @Override
    public void show() {
        stage = uiScreen.getStage();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * This method is called continuously
     *
     * @param v deltaTime
     */
    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(camera);
        update(); //make changes to board, if there are any
        mapRenderer.render();
        stage.draw();
    }

    /**
     * Update all changes to board
     * For now they are only movements of player
     */
    public void update() {
        playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), null);
        handleInput();
        playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), player.getPlayerCell());
    }

    /**
     * Changes the coordinates of the player based on user input
     */

    public void handleInput() {
        playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), null);
        game.handleInput();
        playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), player.getPlayerCell());
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.graphics.setWindowedMode(600, 600);
        }
    }


    @Override
    public void resize(int i, int i1) {
        gridPort.update(i, i1);
        stage.setViewport(gridPort);
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

    //TODO This is a logic and should maybe be a method in the Game class?

    /**
     * Executes the cards that have been chosen
     *
     * @param programCards to execute
     */
    public void executeLockIn(ArrayList<ProgramCard> programCards) {
        if (programCards != null) {
            /*
            playerLayer.setCell( player.getPos().getX(), player.getPos().getY(), null);
            game.executePlayerHand(programCards);
            playerLayer.setCell( player.getPos().getX(), player.getPos().getY(), player.getPlayerCell());
            */
            erasePlayers();
            game.getPlayer().setSelectedCards(programCards); //set the selected cards of player
            game.executeRound();
            repaintPlayers();
        }
    }

    public void erasePlayers() {
        for (Player player : game.getListOfPlayers()) {
            playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), null);
        }
    }

    public void repaintPlayers() {
        for (Player player : game.getListOfPlayers()) {
            playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), player.getPlayerCell());
        }
    }
}