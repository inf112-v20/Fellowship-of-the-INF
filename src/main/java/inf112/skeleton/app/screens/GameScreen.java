package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.audio.Wav;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.game_logic.Move;
import inf112.skeleton.app.game_logic.Game;
import inf112.skeleton.app.game_logic.MovesToExecuteSimultaneously;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.LaserSourcePiece;
import inf112.skeleton.app.grid_objects.PlayerPiece;
import inf112.skeleton.app.player.AIPlayer.Difficulty;
import inf112.skeleton.app.player.Player;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static inf112.skeleton.app.grid.Direction.*;


/**
 * Screen for the physical board.
 */
public class GameScreen implements Screen {
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Viewport gridPort;

    private Stage stage;
    private UIScreen uiScreen;
    private Game game;
    private boolean currentMoveIsExecuted;
    private ScoreBoardScreen scoreBoardScreen;
    private TiledMap map;
    private TiledMapTileLayer playerLayer;
    private int playerLayerIndex;
    private TiledMapTileLayer robotLasersLayer;
    private TiledMapTileLayer boardLaserLayer;
    private TiledMapTileLayer.Cell horizontalLaser;
    private TiledMapTileLayer.Cell verticalLaser;
    private TiledMapTileLayer.Cell doubleHorizLaser;
    private TiledMapTileLayer.Cell doubleVerticalLaser;
    private TiledMapTileLayer.Cell emptyCell;
    private Wav.Sound laserSound;
    final private int countdownTimer = 60;
    private int seconds = countdownTimer;
    private int prevSeconds = countdownTimer;
    private Timer timer;
    private boolean timerStarted = false;
    private boolean boardLasersVisible = false;
    private int TILE_WIDTH_DPI;
    private boolean createdButtons = false;
    private Position respawnPos;
    private ArrayList<ImageButton> respawnButtons;
    private ArrayList<Image> respawnImages;
    private boolean createdImage = false;


    public GameScreen(String mapName, int numberOfPlayers, Difficulty difficulty) {
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(mapName); //roborally board
        initializeCellsAndLayers(map);
        MapProperties mapProperties = map.getProperties();
        int MAP_WIDTH = mapProperties.get("width", Integer.class); //dimensions of board
        int MAP_HEIGHT = mapProperties.get("height", Integer.class);
        TILE_WIDTH_DPI = mapProperties.get("tilewidth", Integer.class); //pixel width per cell
        int MAP_WIDTH_DPI = MAP_WIDTH * TILE_WIDTH_DPI; //total width of map in pixels
        int MAP_HEIGHT_DPI = MAP_HEIGHT * TILE_WIDTH_DPI; //total height of map in pixels

        TiledMapTileSet tiles = map.getTileSets().getTileSet("tileset.png");
        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WIDTH_DPI * 2, MAP_HEIGHT_DPI, camera);
        camera.translate(MAP_WIDTH_DPI, MAP_WIDTH_DPI / 2);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        LogicGrid logicGrid = new LogicGrid(MAP_WIDTH, MAP_HEIGHT, map);
        game = new Game(logicGrid, this, numberOfPlayers, difficulty);
        initializePlayers();
        currentMoveIsExecuted = true;
        //UI gets game deck from game class
        uiScreen = new UIScreen(MAP_WIDTH_DPI * 2, game);
        clearLayer(boardLaserLayer); //lasers should only be shown when active
        this.respawnButtons = new ArrayList<>();
        this.respawnImages = new ArrayList<>();

        laserSound = (Wav.Sound) Gdx.audio.newSound( Gdx.files.internal("assets/sounds/bubaproducer__laser-shot-silenced.wav"));
    }

    /**
     * Create a simple player with the ability to move around the board
     * Add it to the playerLayer
     */
    private void initializePlayers() {
        for (Player playerToInitialize : game.getListOfPlayers()) {
            TiledMapTileLayer.Cell playerCell = playerToInitialize.getPlayerCell();
            playerLayer.setCell(playerToInitialize.getPos().getX(), playerToInitialize.getPos().getY(), playerCell);
        }
    }

    private void initializeCellsAndLayers(TiledMap map) {
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        playerLayerIndex = map.getLayers().getIndex("Player");
        robotLasersLayer = (TiledMapTileLayer) map.getLayers().get("Robot Lasers");
        boardLaserLayer = (TiledMapTileLayer) map.getLayers().get("Lasers");
        horizontalLaser = new TiledMapTileLayer.Cell().setTile(map.getTileSets().getTile(39));
        verticalLaser = new TiledMapTileLayer.Cell().setTile(map.getTileSets().getTile(47));
        doubleHorizLaser = new TiledMapTileLayer.Cell().setTile(map.getTileSets().getTile(103));
        doubleVerticalLaser = new TiledMapTileLayer.Cell().setTile(map.getTileSets().getTile(102));
        emptyCell = new TiledMapTileLayer.Cell().setTile(map.getTileSets().getTile(0));
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
     * <p>
     * If there are moves to execute, they are executed with a delay.
     * This is so that when many moves are executed, the user can differentiate between them.
     */
    public void update() {
        if(!createdImage){
            for(Player player : game.getListOfPlayers()){
                createRespawnImage(player);
            }
            createdImage = true;

        }

        if (game.isChoosingRespawn()) {
            if (!createdButtons) {
                choosePosition();
                createdButtons = true;
            }
            chooseDirection();
        }
        if (boardLasersVisible) {
            laserSound.play(0.5f,1.0f,1.0f);
            clearLayer(boardLaserLayer);
            boardLasersVisible = false;
        }
        //Start timer if there is only one left picking cards for the next round
        handleKeyboardInput();
        if (game.onePlayerLeftToPick() && !timerStarted) {
            timerStarted = true;
            startTimer();
        }
        if (timerStarted) {
            //Stop timer when everybody has locked in cards
            if (!game.onePlayerLeftToPick()) {
                stopTimer();
                uiScreen.executeLockInButton();
            } else if (seconds != prevSeconds) {
                uiScreen.drawTimer(seconds + 1);
                prevSeconds--;
            }
        }
        if (!movesToExecute() && !game.moreLaserToShoot()){
            updateRespawnImages();
        }
        //only handle keyboard input if there are no moves to execute
        if (!movesToExecute() && !game.moreLaserToShoot() && !game.isChoosingRespawn()) {
            uiScreen.update();
            if (game.isPhaseDone() && game.isAutoStartNextPhaseOn()) {
                game.setPhaseDone(false);
                if (game.getRound().getPhaseNr() > 4) {
                    game.getRound().finishRound();
                    uiScreen.newRound();
                } else {
                    game.getRound().nextPhase();
                    uiScreen.updateGameLog();
                }
            }
            game.handleKeyBoardInput();
            uiScreen.handleInput();
        }
        //only execute moves if there are any, the the current one hasn't been executed yet
        if (movesToExecute() && currentMoveIsExecuted) {
            currentMoveIsExecuted = false;
            executeMove();
            delayForSeconds(500); //add delay
        }
        if (!movesToExecute() && game.moreLaserToShoot()) {
            shootRobotLasers();
            showBoardLasers();
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

    /**
     * Draws lasers on a cell for 150ms
     */
    public void shootRobotLasers() {
        for (int i = 0; i < game.getListOfPlayers().length; i++) {
            Player player = game.getListOfPlayers()[i];
            Position oldLaserPos = player.getOldLaserPos();
            robotLasersLayer.setCell(oldLaserPos.getX(), oldLaserPos.getY(), null);
            if (player.getLaserPath().isEmpty()) {
                player.setOldLaserPos(new Position(-1, -1));
                continue;
            }
            TiledMapTileLayer.Cell laser = horizontalLaser;
            if (player.getPlayerPiece().getDir() == NORTH || player.getPlayerPiece().getDir() == SOUTH) {
                laser = verticalLaser;
            }
            Position newLaserPos = player.getLaserPath().get(0);
            robotLasersLayer.setCell(newLaserPos.getX(), newLaserPos.getY(), laser);
            player.getLaserPath().remove(0);
            player.setOldLaserPos(newLaserPos);
        }
    }

    /**
     * Makes the board lasers visible
     */
    public void showBoardLasers() {
        LogicGrid logicGrid = game.getLogicGrid();
        //activate lasers
        for (LaserSourcePiece laserSource : logicGrid.getLaserSourceList()) {
            TiledMapTileLayer.Cell laserCell = getLaserCell(laserSource);
            for (Position pos : logicGrid.getLaserPath(laserSource)) {
                boardLaserLayer.setCell(pos.getX(), pos.getY(), laserCell);
            }
        }
        boardLasersVisible = true;
    }


    private TiledMapTileLayer.Cell getLaserCell(LaserSourcePiece laserSource) {
        Direction laserDirection = laserSource.getLaserDir();
        TiledMapTileLayer.Cell laserId = horizontalLaser;
        switch (laserDirection) {
            case NORTH:
            case SOUTH:
                if (laserSource.isDoubleLaser()) laserId = doubleVerticalLaser;
                else laserId = verticalLaser;
                break;
            case EAST:
            case WEST:
                if (laserSource.isDoubleLaser()) laserId = doubleHorizLaser;
                else laserId = horizontalLaser;
                break;
        }
        return laserId;
    }


    /**
     * Handling UI options
     */

    public void handleKeyboardInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
            stage = scoreBoardScreen.getStage();
        } else {
            stage = uiScreen.getStage();
        }
        stage.setViewport(gridPort);
        game.handleNonGameLogicKeyBoardInput();
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
     *
     * @param move that the player performed
     */
    public void redrawPlayer(Move move) {
        PlayerPiece playerPieceToUpdate = move.getPlayerPiece();
        Position oldPos = move.getOldPos();
        Position newPos = move.getNewPos();
        Direction newDir = move.getNewDir();
        Position lastPosAlive = playerPieceToUpdate.getPlayer().getLastPosAlive();
        if (!game.getLogicGrid().isInBounds(oldPos) && game.getLogicGrid().positionIsFree(lastPosAlive, playerLayerIndex)) {
            playerLayer.setCell(lastPosAlive.getX(), lastPosAlive.getY(), null);
        }
        if (game.getLogicGrid().positionIsFree(oldPos, playerLayerIndex)) { //check that you are not erasing another player
            playerLayer.setCell(oldPos.getX(), oldPos.getY(), null); //set the old cell position to null
        }
        playerPieceToUpdate.turnCellInDirection(newDir); //turn the cell in the new direction
        playerLayer.setCell(newPos.getX(), newPos.getY(), playerPieceToUpdate.getPlayerCell()); //repaint at new position
    }


    /**
     * Starts a timer counting down for 30 seconds
     */
    private void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (seconds < 0 && timerStarted) {
                    game.getPlayerRemaining().pickRandomCards();
                    uiScreen.getCardButton().moveCardButtons();
                }
                seconds--;
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * Stops the timer and removes it from the screen
     */
    private void stopTimer() {
        timerStarted = false;
        timer.cancel();
        prevSeconds = countdownTimer;
        seconds = countdownTimer;
        uiScreen.getTimerLabel().remove();
    }

    /**
     * Sets all the cells in the layer to the empty cell.
     *
     * @param layerToClear layer to be cleared
     */
    public void clearLayer(TiledMapTileLayer layerToClear) {
        if (layerToClear != null) {
            for (int x = 0; x < layerToClear.getWidth(); x++) {
                for (int y = 0; y < layerToClear.getHeight(); y++) {
                    layerToClear.setCell(x, y, emptyCell);
                }
            }
        }
    }

    /**
     * Only used for testing.
     */
    public void blinkBoardLasers() {
        showBoardLasers();
    }

    private void createRespawnButton(Position pos) {
        Texture texture = new Texture("white.png");
        TextureRegion myTextureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        ImageButton button = new ImageButton(myTexRegionDrawable);
        button.setPosition(pos.getX() * TILE_WIDTH_DPI, pos.getY() * TILE_WIDTH_DPI);
        button.setColor(Color.GREEN);
        Color c = button.getColor();
        button.setColor(c.r, c.g, c.b, 0.4f);
        respawnButtonPressed(button);
        stage.addActor(button);
        respawnButtons.add(button);

    }


    private void respawnButtonPressed(final ImageButton respawnButton) {
        respawnButton.addListener(new ClickListener() {
            final ImageButton tempButton = respawnButton;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                executeRespawnButton(tempButton);
            }

        });


    }

    private void executeRespawnButton(ImageButton button) {
        int posX = (int) button.getX() / TILE_WIDTH_DPI;
        int posY = (int) button.getY() / TILE_WIDTH_DPI;
        Player player1 = game.getPlayer();
        PlayerPiece playerPiece = player1.getPlayerPiece();
        if (respawnPos != null) {
            playerLayer.setCell(respawnPos.getX(), respawnPos.getY(), null);
        }
        respawnPos = new Position(posX, posY);
        playerLayer.setCell(posX, posY, playerPiece.getPlayerCell());
        chooseDirection();
    }

    private void chooseDirection() {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        Player player = game.getPlayer();
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player.turnPlayerRight(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player.turnPlayerLeft(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setChoosingRespawn(false);
            game.getPlayer().setPos(respawnPos);
            PlayerPiece playerPiece = player.getPlayerPiece();
            game.getLogicGrid().movePlayerToNewPosition(playerPiece, player.getDeadPosition(), respawnPos);
            System.out.println("Player 1 has respawned at " + respawnPos + " in dir " + player.getPlayerPiece().getDir());
            for (ImageButton buttons : respawnButtons) {
                buttons.remove();
            }

            if (!game.getDeadPlayers().isEmpty()) {
                game.getRound().respawnPlayers();
            }
            return;
        }
        game.performMoves(moves);
        for (Move move : moves) {
            redrawPlayer(move);
        }
        moves.clear();
    }

    private void choosePosition() {
        Player player = game.getPlayer();
        if (player.getRespawnPositions().size() == 1) {
            respawnPos = player.getRespawnPositions().get(0);
            playerLayer.setCell(respawnPos.getX(), respawnPos.getY(), player.getPlayerPiece().getPlayerCell());
            return;
        }
        respawnButtons.clear();
        for (Position pos : player.getRespawnPositions()) {
            createRespawnButton(pos);
        }
    }

    private void createRespawnImage(Player player){
            TextureRegion textureRegion = player.getPlayerPiece().getPlayerCell().getTile().getTextureRegion();
            TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(textureRegion);
            Image image = new Image(myTexRegionDrawable);
            image.setSize(80, 80);
            Position pos = findPosition(player);
            image.setPosition(pos.getX(), pos.getY());
            stage.addActor(image);
            respawnImages.add(image);
    }

    private Position findPosition(Player player){
        Position respawnPosition = player.getSpawnPoint();
        int spacing = (player.getPlayerNumber()-1)* (TILE_WIDTH_DPI/4);
        int posX = respawnPosition.getX() * TILE_WIDTH_DPI;
        int posY = respawnPosition.getY() * TILE_WIDTH_DPI + spacing;
        if(player.getPlayerNumber() >= 5){
            posX += TILE_WIDTH_DPI*0.75f;
            spacing = (player.getPlayerNumber()-5)* (TILE_WIDTH_DPI/4);
            posY = respawnPosition.getY() * TILE_WIDTH_DPI + spacing;
        }
        return new Position(posX, posY);
    }

    private void updateRespawnImages(){
        for (int i = 0; i < respawnImages.size(); i++) {
            Image image = respawnImages.get(i);
            Player player = game.getListOfPlayers()[i];
            Position pos = findPosition(player);
            image.setPosition(pos.getX(),pos.getY());
        }
    }

}