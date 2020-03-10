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
import inf112.skeleton.app.Move;
import inf112.skeleton.app.BoardElementsMove;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Map;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.player.Player;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


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
    private boolean currentMoveIsExecuted;
    private ScoreBoardScreen scoreBoardScreen;

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
        Map map = new Map(MAP_WIDTH, MAP_HEIGHT, this.map);
        game = new Game(map, this);
        initializePlayers();
        currentMoveIsExecuted = true;
        //UI gets game deck from game class
        uiScreen = new UIScreen(MAP_WIDTH_DPI * 2, game);
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
        scoreBoardScreen = uiScreen.getScoreBoardScreen();
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
        uiScreen.update();
        mapRenderer.render();
        stage.draw();
    }

    /**
     * Update all changes to board
     *
     * If there are moves to execute, they are executed with a delay.
     * This is so that when many moves are executed, the user can differentiate between them.
     */
    public void update() {
        //only handle keyboard inpuut if there are no moves to execute
        if (!movesToExecute()) {
            handleKeyboardInput();
        }
        //only execute moves if there are any, the the current one hasn't been executed yet
        if (movesToExecute() && currentMoveIsExecuted) {
            currentMoveIsExecuted = false;
            executeMove();
            delayForSeconds(1); //add delay
        }
    }

    /**
     * @return true if there are frontend moves to execute
     */
    public boolean movesToExecute() {
        return !game.getMoves().isEmpty();
    }

    /**
     * This method executed the list of moves in the front end of the movesToExecute queue.
     */
    public void executeMove() {
        ArrayList<Move> firstSetOfMoves = game.getMoves().peek();
        for (Move currentMove : firstSetOfMoves) {
            redrawPlayer(currentMove);
        }
        game.getMoves().poll(); //remove set of moves once they have been executed
        currentMoveIsExecuted = true;
    }


    /**
     * Changes the coordinates of the player based on user input
     */

    public void handleKeyboardInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.TAB)){
            stage = scoreBoardScreen.getStage();
            stage.setViewport(gridPort);
        }
        else {
            stage = uiScreen.getStage();
            stage.setViewport(gridPort);
        }
        game.handleKeyBoardInput();
        uiScreen.handleInput();
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
    /*
    public void executeLockIn(ArrayList<ProgramCard> programCards) {
        if (programCards != null) {
            game.getPlayer().setSelectedCards(programCards); //set the selected cards of player
            game.executeRound();
            uiScreen.updateGameLog();
        }
    }

     */

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

    /**
     * @param secondsOfDelay number of seconds delay should last
     */
    public void delayForSeconds(int secondsOfDelay) {
        try {
            TimeUnit.SECONDS.sleep(secondsOfDelay);
        } catch (InterruptedException e) {
            System.out.println("Timer was interrupted");
        }
    }

    /**
     * Erases the player currently on the board, and redraws it in it's state after having executed the move
     * @param move that the player performed
     */
    public void redrawPlayer(Move move) {
        Player playerToUpdate = move.getPlayer();
        Position oldPos = move.getOldPos();
        Position newPos = move.getNewPos();
        Direction newDir = move.getNewDir();
        playerToUpdate.getPlayerPiece().turnCellInDirection(newDir); //turn the cell in the correct direction
        playerLayer.setCell(oldPos.getX(), oldPos.getY(), null); //set the old cell position to null
        playerToUpdate.getPlayerPiece().turnCellInDirection(newDir); //turn the cell in the new direction
        playerLayer.setCell(newPos.getX(), newPos.getY(), playerToUpdate.getPlayerCell()); //repaint at new position
        System.out.println("Frontend executing: " + move);
    }
}