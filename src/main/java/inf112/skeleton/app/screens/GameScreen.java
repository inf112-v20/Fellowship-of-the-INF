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
import inf112.skeleton.app.RoboRallyGame;
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


import javax.swing.*;
import java.util.*;
import java.util.Timer;
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
    private RoboRallyGame roborallyGame;
    private boolean currentMoveIsExecuted;
    private ScoreBoardScreen scoreBoardScreen;
    private TiledMapTileLayer playerLayer;
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
    private boolean hasUpdated = false;
    private int currentPhaseNr = 0;
    private boolean keepWatching = false;


    public GameScreen(RoboRallyGame roborallyGame, String mapName, int numberOfPlayers, Difficulty difficulty) {
        this.roborallyGame = roborallyGame;
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load(mapName); //roborally board
        initializeCellsAndLayers(map);
        MapProperties mapProperties = map.getProperties();
        int MAP_WIDTH = mapProperties.get("width", Integer.class); //dimensions of board
        int MAP_HEIGHT = mapProperties.get("height", Integer.class);
        TILE_WIDTH_DPI = mapProperties.get("tilewidth", Integer.class); //pixel width per cell
        int MAP_WIDTH_DPI = MAP_WIDTH * TILE_WIDTH_DPI; //total width of map in pixels
        int MAP_HEIGHT_DPI = MAP_HEIGHT * TILE_WIDTH_DPI; //total height of map in pixels

        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WIDTH_DPI * 2, MAP_HEIGHT_DPI, camera);
        camera.translate(MAP_WIDTH_DPI, MAP_HEIGHT_DPI / 2);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        LogicGrid logicGrid = new LogicGrid(MAP_WIDTH, MAP_HEIGHT, map, difficulty);
        game = new Game(logicGrid, this, numberOfPlayers, difficulty, mapName);
        initializePlayers();
        currentMoveIsExecuted = true;
        //UI gets game deck from game class
        uiScreen = new UIScreen(MAP_WIDTH_DPI * 2, game);
        stage = uiScreen.getStage();
        clearLayer(boardLaserLayer); //lasers should only be shown when active
        this.respawnButtons = new ArrayList<>();
        this.respawnImages = new ArrayList<>();
        for (Player player : game.getRespawnOrder()) { createRespawnImage(player); }

        laserSound = (Wav.Sound) Gdx.audio.newSound(Gdx.files.internal("assets/sounds/bubaproducer__laser-shot-silenced.wav"));
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
     * If there are moves to execute, they are executed with a delay.
     * This is so that when many moves are executed, the user can differentiate between them.
     */
    public void update() {
        checkForChoosingRespawn();
        playLaserSound();
        handleKeyboardInput();
        timerChecker();
        checkForRespawnUpdate();
        checkIfNextPhaseCanStart();
        executeMovesWithDelay();
        executeLasersWithDelay();
        updatePhaseNr();
    }

    /**
     * If player 1 is dead and is respawning then remove the gamelog or cards in the UI and write the respawn text.
     * The player should then be able to choose the direction to respawn in.
     * If the spawnpoint is occupied then they should be able to choose an adjacent position to respawn in as well.
     *
     */
    private void checkForChoosingRespawn() {
        if (game.isChoosingRespawn()) {
            if (game.getRound().getPhaseNr() == 0) {
                uiScreen.removeCards();
            }
            if (game.getPlayer().isDead()) {
                game.getPlayer().setIsDead(false);
                createdButtons = false;
            }
            if (!createdButtons) {
                choosePosition();
                uiScreen.removeGameLog();
                uiScreen.update();
                uiScreen.createRespawnText();
                createdButtons = true;
            }
            chooseDirection();
        }
    }

    /**
     * If lasers are shown on the board then play lasersounds as well
     */
    private void playLaserSound(){
        if (boardLasersVisible) {
            laserSound.play(0.25f, 1.0f, 1.0f);
            clearLayer(boardLaserLayer);
            boardLasersVisible = false;
        }
    }

    /**
     * Methods that checks if timer should be started, stopped, or if it should keep going (if started).
     */
    private void timerChecker(){
        // Start timer if there is only one left picking cards
        if (game.onePlayerLeftToPick() && !timerStarted) {
            timerStarted = true;
            startTimer();
        }
        if (timerStarted) {
            //Stop timer when everybody has locked in cards
            if (!game.onePlayerLeftToPick()) {
                stopTimer();
                uiScreen.executeLockInButton();
            }
            //else draw the timer again
            else if (seconds != prevSeconds) {
                uiScreen.drawTimer(seconds + 1);
                prevSeconds--;
            }
        }
    }

    /**
     * Checks if the spawnpoints for the players are updated and will then update the respawn images.
     */
    private void checkForRespawnUpdate(){
        if (!movesToExecute() && !game.moreLaserToShoot() && !hasUpdated) {
            updateRespawnImages();
            hasUpdated = true;
        }
    }

    /**
     * If there are no more moves to execute and lasers to shoot then check if the game has ended.
     * If player 1 is not choosing a respawn position/direction then update the UI elements.
     * If autostart is toggled on then start the next phase immediately or start the next round if the current phase is nr 5.
     * If autostart is toggled off then you can press ENTER to go the next phase (or possibly new round).
     * Other keyboard inputs that affects game logic (like WASD, UPARROW, LEFTARROW etc.) are only accepted when there is
     * no moves or lasers to execute on the gamescreen.
     */
    private void checkIfNextPhaseCanStart(){
        if (!movesToExecute() && !game.moreLaserToShoot()) {
            if (!game.isChoosingRespawn()) {
                uiScreen.update();
                if (game.isPhaseDone() && game.isAutoStartNextPhaseOn()) {
                    game.setPhaseDone(false);
                    if (game.getRound().getPhaseNr() > 4) {
                        game.getRound().finishRound();
                        if (!game.isChoosingRespawn()) {
                            uiScreen.newRound();
                        }
                    } else {
                        game.getRound().nextPhase();
                        uiScreen.updateGameLog();
                    }
                }
                game.handleKeyBoardInput();
                handleEnterInput();
            }
            checkForEndGame();
        }
    }

    /**
     * Updates a phaseNr variable which is different to the phaseNr variable in Game.java
     * This is mostly to make sure the respawn images are only drawn once.
     */
    private void updatePhaseNr(){
        if (currentPhaseNr != game.getRound().getPhaseNr()) {
            hasUpdated = false;
            currentPhaseNr = game.getRound().getPhaseNr();
        }
    }

    /**
     * Checks if the game is over (all players are dead or there is a winner),
     * and if so shows the appropriate message to the user
     */
    public void checkForEndGame() {
        Player victoriousPlayer = game.getVictoriousPlayer();
        Object[] options = {"Exit", "Main Menu"}; //opiton to exit or restart
        String message = "Error, game needs to be restarted"; //this should never be shown
        if (victoriousPlayer != null)
            message = "Player " + victoriousPlayer.getPlayerNumber() + " won!";
        else if (game.isGameOver())
            message = "All the players are dead!";
        if (victoriousPlayer != null || game.isGameOver()) {
            int gameOverDialog = JOptionPane.showOptionDialog(null,
                    message,
                    message,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (gameOverDialog == 1) {
                roborallyGame.setScreen(new MainMenuScreen(roborallyGame));
            } else Gdx.app.exit();
        } else {
            checkIfPlayer1Died();
        }
    }

    /**
     * This method is only called if there isn't a winner and all players aren't dead.
     * If player 1 is dead, but there are still some AI's alive, then the user has the option of continuing to watch
     * the game.
     */
    public void checkIfPlayer1Died() {
        Object[] options = {"Exit", "Main Menu", "Keep Watching"};
        String message = "Error, game needs to be restarted";
        if (game.getPlayer().isPermanentlyDead() && !keepWatching) {
            message = "You are dead!";
            int gameOverDialog = JOptionPane.showOptionDialog(null,
                    message,
                    message,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[2]);
            if (gameOverDialog == 1) {
                roborallyGame.setScreen(new MainMenuScreen(roborallyGame));
            } else if(gameOverDialog == 2) {
                keepWatching = true;
                if(game.getRound().getPhaseNr() == 0) {
                    uiScreen.executePowerDownButton();
                }

                game.setAutoStartNextPhase(true);
            } else {
            Gdx.app.exit();
            }
        }
    }

    /**
     * Execute the all the moves in a phase with a delay of 500 milliseconds
     */
    private void executeMovesWithDelay(){
        if (movesToExecute() && currentMoveIsExecuted) {
            currentMoveIsExecuted = false;
            executeMove();
            delayForSeconds(500); //add delay
        }

    }

    /**
     * Execute all lasers (both boardlasers and player lasers)
     * Player lasers move across the map until they hit a wall, player, or reach the end of the map
     * The player lasers move with a delay of 150 milliseconds.
     */
    private void executeLasersWithDelay(){
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
        return !game.getFrontendMoves().isEmpty();
    }

    /**
     * This method executed the list of moves in the front end of the movesToExecute queue.
     */
    public void executeMove() {
        MovesToExecuteSimultaneously firstSetOfMoves = game.getFrontendMoves().peek();
        assert firstSetOfMoves != null;
        for (Move currentMove : firstSetOfMoves) {
            redrawPlayer(currentMove);
        }
        game.getFrontendMoves().poll(); //remove set of moves once they have been executed
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

    /**
     * Returns the type of laser cell that needs to be used to draw the laser coming from the laser source
     *
     * @param laserSource the laser source piece of the laser
     * @return the type of laser call that should be drawn from the source
     */
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

    /**
     * Handles ENTER keyboard input.
     * Starts the next phase if the current phase is done.
     * Starts the next round if the current phasenr is 5.
     * You can also use ENTER to lock in when you have chosen 5 cards at round start.
     */
    private void handleEnterInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (game.getRound().getPhaseNr() > 4) {
                uiScreen.removeGameLog();
                game.getRound().finishRound();
                if (!game.isChoosingRespawn()) {
                    uiScreen.newRound();
                }
            } else {
                if (game.getRound().getPhaseNr() == 0) {
                    uiScreen.executeLockInButton();
                    return;
                }
                game.getRound().nextPhase();
                uiScreen.updateGameLog();
            }
        }
    }


    @Override
    public void resize(int i, int i1) {
        gridPort.update(i, i1);
        stage.setViewport(gridPort);
    }


    @Override
    public void pause() {
        //not used
    }

    @Override
    public void resume() {
        //not used
    }

    @Override
    public void hide() {
        //not used
    }

    @Override
    public void dispose() {
        //not used
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
        LogicGrid logicGrid = game.getLogicGrid();
        if (!logicGrid.isInBounds(oldPos) && logicGrid.positionIsFree(lastPosAlive, logicGrid.PLAYER_LAYER_INDEX)
                && !game.isChoosingRespawn()) {
            playerLayer.setCell(lastPosAlive.getX(), lastPosAlive.getY(), null);
        }
        if (logicGrid.positionIsFree(oldPos, logicGrid.PLAYER_LAYER_INDEX)) { //check that you are not erasing another player
            playerLayer.setCell(oldPos.getX(), oldPos.getY(), null); //set the old cell position to null
        }
        if (oldPos.equals(playerPieceToUpdate.getPos())) {
            playerLayer.setCell(oldPos.getX(), oldPos.getY(), null);
        }
        playerPieceToUpdate.turnCellInDirection(newDir); //turn the cell in the new direction
        playerLayer.setCell(newPos.getX(), newPos.getY(), playerPieceToUpdate.getPlayerCell()); //repaint at new position
    }


    /**
     * Starts a timer counting down from 60 seconds
     * Picks random cards for remaining open slots in the register
     * for the last player picking cards if they let the timer run out
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
     * Makes the board lasers show for a millisecond. Only used for testing.
     */
    public void blinkBoardLasers() {
        showBoardLasers();
    }

    /**
     * If player 1 is respawning and their spawnpoint is occupied then highlight each valid adjacent spawnpoint and
     * make a button for each.
     * @param pos the actual spawnpoint of player 1 that is occupied
     */
    private void createRespawnButton(Position pos) {
        respawnPos = null;
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


    /**
     * Clicklistener for the respawnbuttons.
     * If left-clicked then move player 1's image to the clicked cell.
     * @param respawnButton the button to make a listener for
     */
    private void respawnButtonPressed(final ImageButton respawnButton) {
        respawnButton.addListener(new ClickListener() {
            final ImageButton tempButton = respawnButton;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                executeRespawnButton(tempButton);
            }

        });


    }

    /**
     * Moves player 1's image to the position of the button (the cell)
     * @param button the button that is left-clicked
     */
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

    /**
     * Method for when player 1 respawns and chooses which direction to respawn in.
     * If R is pressed then the respawn direction is confirmed and cant be changed.
     * If there are more dead players then they are respawned afterwards and then the next round starts.
     * If respawns buttons were made for this respawn then those are removed.
     */
    private void chooseDirection() {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        Player player = game.getPlayer();
        PlayerPiece playerPiece = player.getPlayerPiece();
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            playerPiece.turnRight(moves);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            playerPiece.turnLeft(moves);
        }
        game.performMoves(moves); //execute moves if there are any
        for (Move move : moves) {
            redrawPlayer(move); //redraw player if it needs to be redrawn
        }
        moves.clear();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setChoosingRespawn(false);
            game.getPlayer().setPos(respawnPos);
            game.getLogicGrid().movePlayerToNewPosition(playerPiece, player.getDeadPosition(), respawnPos);
            for (ImageButton buttons : respawnButtons) {
                buttons.remove();
            }

            if (!game.getDeadPlayers().isEmpty()) {
                game.getRound().respawnPlayers();
            }
            if (!game.getPlayer().isKeyInput()) {
                uiScreen.newRound();
            } else {
                uiScreen.addCards();
            }
        }

    }

    /**
     * Method for player 1 to choose which position to respawn in if the spawnpoint is occupied.
     */
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

    /**
     * Creates the respawnimage for a player in the game
     * The respawnimage is just a small picture of the robot in the lower left corner of their current spawnpoint.
     * @param player the player to create a respawnimage for
     */
    private void createRespawnImage(Player player) {
        TextureRegion textureRegion = player.getPlayerPiece().getPlayerCell().getTile().getTextureRegion();
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(textureRegion);
        Image image = new Image(myTexRegionDrawable);
        image.setSize(80, 80);
        HashMap<String, Integer> posCounter = new HashMap<>();
        Position pos = findPosition(player, posCounter);
        image.setPosition(pos.getX(), pos.getY());
        image.setName(player.toString());
        stage.addActor(image);
        respawnImages.add(image);
    }

    /**
     * Finds the position of where to place the respawnimage.
     * If more than 1 player has the same respawn position then they should stack nicely on top of each other
     * on the left side of the cell.
     * If more than 5 players has the same respawn position then they start stacking at the right side of the cell too.
     *
     * @param player the player to find the respawnimage position for
     * @param posCounter HashMap with string of position as key, and number of people that has their respawnimage at that position as value.
     * @return the position to place the respawnimage for a given player
     */
    private Position findPosition(Player player, HashMap posCounter) {
        Position respawnPosition = player.getSpawnPoint();
        int posX = respawnPosition.getX() * TILE_WIDTH_DPI;
        Integer playersOnSameTile = 0;
        String spawnString = respawnPosition.toString();
        if (posCounter.containsKey(spawnString)) {
            playersOnSameTile = (Integer) posCounter.get(spawnString);
        }
        int spacing = playersOnSameTile * (TILE_WIDTH_DPI / 4);
        int posY = respawnPosition.getY() * TILE_WIDTH_DPI + spacing;
        if (playersOnSameTile >= 4) {
            posX += TILE_WIDTH_DPI * 0.75f;
            spacing = (playersOnSameTile - 4) * (TILE_WIDTH_DPI / 4);
            posY = respawnPosition.getY() * TILE_WIDTH_DPI + spacing;
        }
        posCounter.put(spawnString, playersOnSameTile + 1);
        return new Position(posX, posY);
    }

    /**
     * Updates the respawn image after every phase
     */
    private void updateRespawnImages() {
        HashMap<String, Integer> posCounter = new HashMap<>();
        for (int i = 0; i < game.getRespawnOrder().size(); i++) {
            Player player = game.getRespawnOrder().get(i);
            Image image = respawnImages.get(0);
            for (Image respawnImage : respawnImages) {
                image = respawnImage;
                if (image.getName().equals(player.toString())) {
                    break;
                }
            }
            Position pos = findPosition(player, posCounter);
            image.setPosition(pos.getX(), pos.getY());
        }
    }


}