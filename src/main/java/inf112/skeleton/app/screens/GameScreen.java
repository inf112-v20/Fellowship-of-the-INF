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
import inf112.skeleton.app.game_logic.Move;
import inf112.skeleton.app.game_logic.Game;
import inf112.skeleton.app.game_logic.MovesToExecuteSimultaneously;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.PlayerPiece;
import inf112.skeleton.app.player.Player;

import java.util.concurrent.TimeUnit;

import static inf112.skeleton.app.grid.Direction.NORTH;
import static inf112.skeleton.app.grid.Direction.SOUTH;


/**
 * Screen for the physical board.
 */
public class GameScreen implements Screen {
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Viewport gridPort;
    private TiledMapTileLayer playerLayer;
    private TiledMapTileLayer robotLasersLayer;
    private Stage stage;
    private UIScreen uiScreen;
    private Game game;
    private boolean currentMoveIsExecuted;
    private ScoreBoardScreen scoreBoardScreen;
    private TiledMapTileLayer.Cell horizontalLaser;
    private TiledMapTileLayer.Cell verticalLaser;



    public GameScreen(String mapName) {
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load(mapName); //roborally board
        MapProperties mapProperties = map.getProperties();
        int MAP_WIDTH = mapProperties.get("width", Integer.class); //dimensions of board
        int MAP_HEIGHT = mapProperties.get("height", Integer.class);
        int TILE_WIDTH_DPI = mapProperties.get("tilewidth", Integer.class); //pixel width per cell
        int MAP_WIDTH_DPI = MAP_WIDTH * TILE_WIDTH_DPI; //total width of map in pixels
        int MAP_HEIGHT_DPI = MAP_HEIGHT * TILE_WIDTH_DPI; //total height of map in pixels

        TiledMapTileSet tiles = map.getTileSets().getTileSet("tileset.png");
        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WIDTH_DPI * 2, MAP_HEIGHT_DPI, camera);
        camera.translate(MAP_WIDTH_DPI, MAP_WIDTH_DPI / 2);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        // Layers, add more later
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        horizontalLaser = new TiledMapTileLayer.Cell().setTile(map.getTileSets().getTile(39));
        verticalLaser = new TiledMapTileLayer.Cell().setTile(map.getTileSets().getTile(47));
        robotLasersLayer = (TiledMapTileLayer) map.getLayers().get("Robot Lasers");
        LogicGrid logicGrid = new LogicGrid(MAP_WIDTH, MAP_HEIGHT, map);
        game = new Game(logicGrid, this);
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
        //only handle keyboard input if there are no moves to execute
        if (!movesToExecute() && !game.moreLaserToShoot()) {
            handleKeyboardInput();
            uiScreen.update();
        }
        //only execute moves if there are any, the the current one hasn't been executed yet
        if (movesToExecute() && currentMoveIsExecuted) {
            currentMoveIsExecuted = false;
            executeMove();
            delayForSeconds(1000); //add delay
        }
        if(!movesToExecute() && game.moreLaserToShoot()){
            shootLasers();
            delayForSeconds(150);
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
        MovesToExecuteSimultaneously firstSetOfMoves = game.getMoves().peek();
        assert firstSetOfMoves != null;
        for (Move currentMove : firstSetOfMoves) {
            redrawPlayer(currentMove);
        }
        game.getMoves().poll(); //remove set of moves once they have been executed
        currentMoveIsExecuted = true;
    }

    public void shootLasers(){
        for (int i = 0; i <game.getListOfPlayers().length; i++) {
            Player player = game.getListOfPlayers()[i];
            Position oldLaserPos = player.getOldLaserPos();
            robotLasersLayer.setCell(oldLaserPos.getX(), oldLaserPos.getY(), null);
            if(player.getLaserPath().isEmpty()){
                player.setOldLaserPos(new Position(-1,-1));
                continue;
            }
            TiledMapTileLayer.Cell laser = horizontalLaser;
            if(player.getPlayerPiece().getDir() == NORTH ||player.getPlayerPiece().getDir() == SOUTH){
                laser = verticalLaser;
            }
            Position newLaserPos = player.getLaserPath().get(0);
            robotLasersLayer.setCell(newLaserPos.getX(), newLaserPos.getY(), laser);
            player.getLaserPath().remove(0);
            player.setOldLaserPos(newLaserPos);
        }
    }


    /**
     * Handling UI options
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
     * @param milliSecondsOfDelay number of milliseconds delay should last
     */
    public void delayForSeconds(int milliSecondsOfDelay) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliSecondsOfDelay);
        } catch (InterruptedException e) {
            System.out.println("Timer was interrupted");
        }
    }

    /**
     * Erases the player currently on the board, and redraws it in it's state after having executed the move
     * @param move that the player performed
     */
    public void redrawPlayer(Move move) {
        PlayerPiece playerPieceToUpdate = move.getPlayerPiece();
        Position oldPos = move.getOldPos();
        Position newPos = move.getNewPos();
        Direction newDir = move.getNewDir();
        playerLayer.setCell(oldPos.getX(), oldPos.getY(), null); //set the old cell position to null
        playerPieceToUpdate.turnCellInDirection(newDir); //turn the cell in the new direction
        playerLayer.setCell(newPos.getX(), newPos.getY(), playerPieceToUpdate.getPlayerCell()); //repaint at new position
    }
}