package inf112.skeleton.app.Screens;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.Cards.ProgramCard;
import inf112.skeleton.app.Deck.GameDeck;
import inf112.skeleton.app.GameLogic;
import inf112.skeleton.app.Grid.PieceGrid;
import inf112.skeleton.app.Player;

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
    private final int MAP_WIDTH = 12; //dimensions of board
    private final int MAP_HEIGHT = 12;
    private final int TILE_WIDTH_DPI = 300; //pixel width per cell
    private final int MAP_WITDTH_DPI = MAP_WIDTH * TILE_WIDTH_DPI; //total width of map in pixels
    private TiledMapTileLayer playerLayer;
    private TmxMapLoader mapLoader;
    private Stage stage;
    private UIScreen uiScreen;
    private Player player;
    private GameLogic game;

    public GameScreen() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("RoborallyBoard_debugged.tmx");
        tiles = map.getTileSets().getTileSet("tileset.png");
        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WITDTH_DPI*2, MAP_WITDTH_DPI, camera);
        camera.translate(MAP_WITDTH_DPI, MAP_WITDTH_DPI/2);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        // Layers, add more later
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        PieceGrid pieceGrid = new PieceGrid(MAP_WIDTH, MAP_WIDTH, map);
        game = new GameLogic(pieceGrid);
        initializePlayer();
        GameDeck gameDeck = new GameDeck();
        uiScreen = new UIScreen(MAP_WITDTH_DPI, gameDeck, this);
    }
    /**
     * Create a simple player with the ability to move around the board
     * Add it to the playerLayer
     */
    public void initializePlayer() {
        player = game.getPlayer();
        TiledMapTileLayer.Cell playerCell = player.getPlayerCell();
        playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), playerCell);
    }

    @Override
    public void show() {
        stage = uiScreen.getStage();
        Gdx.input.setInputProcessor(stage);
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
        mapRenderer.render();
        stage.draw();
    }

    /**
     * Update all changes to board
     * For now they are only movements of player
     */
    public void update() {
       handleInput();
    }

    /**
     * Changes the coordinates of the player based on user input
     */

    public void handleInput() {
        playerLayer.setCell((int) player.getPos().getX(), (int) player.getPos().getY(), null);
        game.handleInput();
        playerLayer.setCell((int) player.getPos().getX(), (int) player.getPos().getY(), player.getPlayerCell());
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){Gdx.graphics.setWindowedMode(1200,1200);}
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

    //TODO understand and implement dispose
    @Override
    public void dispose() {

    }

    /**
     * Executes the cards that have been chosen
     * @param programCards to execute
     */
    public void executeLockIn(ArrayList<ProgramCard> programCards) {
        if (programCards != null) {
            playerLayer.setCell((int) player.getPos().getX(), (int) player.getPos().getY(), null);
            game.executePlayerHand(programCards);
            playerLayer.setCell((int) player.getPos().getX(), (int) player.getPos().getY(), player.getPlayerCell());
        }
    }
}