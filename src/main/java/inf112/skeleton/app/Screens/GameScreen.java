package inf112.skeleton.app.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
    private Player player;
    private TiledMapTileLayer playerLayer;
    private TmxMapLoader mapLoader;
    private GameLogic game;
    private PieceGrid pieceGrid;
    private CardButton cardButton;
    private GameDeck gameDeck;
    private Stage stage;
    private Texture texture;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameScreen() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("RoborallyBoard.tmx");
        tiles = map.getTileSets().getTileSet("tileset.png");
        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WITDTH_DPI*2, MAP_WITDTH_DPI, camera);
        camera.translate(MAP_WITDTH_DPI, MAP_WITDTH_DPI/2);
        //camera.setToOrtho(false, MAP_WIDTH, MAP_WIDTH*2); //Stretch width of map to the edge
        //camera.position.y = 0;        //Make map only take up the upper half of the screen
        //camera.update();
       // mapRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / TILE_WIDTH_DPI);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        // Layers, add more later
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        PieceGrid pieceGrid = new PieceGrid(MAP_WIDTH, MAP_WIDTH, map);
        game = new GameLogic(pieceGrid);

        initializePlayer();

        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("cardslot.png")); //the background image for where the a selected card goes
        stage = new Stage(new ScreenViewport()); //Set up a stage for the ui
        //stage.setViewport(gridPort);
        //stage.getViewport().setCamera(camera);
        Gdx.input.setInputProcessor(stage); //Start taking input from the ui
        //Create lockInButton and add it as an actor to the stage
        cardButton = new CardButton(this);
        stage.addActor(cardButton.getLockInButton());
        //Create new GameDeck
        gameDeck = new GameDeck(); //TODO GameDeck should be made by GameLogic
        //Draw cards to hand
        gameDeck.drawHand(gameDeck.getDrawDeck(), 0); //TODO 0 should be replaced by gameLogic.getPlayer().getHealth() or something like that
        //Add every cardButton drawn as an actor to the stage
        for (int i = 0; i < cardButton.getListOfCardButtons().size(); i++) {
            stage.addActor(cardButton.getListOfCardButtons().get(i).getButton());
        }
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
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
        //Draw the background image for the selected cards first
        batch.begin();
        for (int i = 0; i <5 ; i++) {
            float selectedCardPosX = 150*i + cardButton.getSelectedCardPosX();
            float selectedCardPosY = cardButton.getSelectedCardPosY();
            batch.draw(texture, selectedCardPosX, selectedCardPosY, texture.getWidth()*0.41f, texture.getHeight()*0.41f);
        }
        batch.end();
        //Draw cards second
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.draw(); //Draw the ui
        //Draw prioritynumber as text on top of cards at the correct position third. Will update properly as well.
        //It is important they get drawn in the correct order, or else something will be hidden (e.g. the prioritynumber gets drawn behind the cards).
        font = new BitmapFont();
        font.setColor(Color.LIME);
        batch.begin();
        int numberOfCardButtons = cardButton.getListOfCardButtons().size();
        for (int i = 0; i < numberOfCardButtons; i++) {
            String priorityText = String.valueOf(cardButton.getListOfCardButtons().get(i).getCard().getPriority());
            float textPosX = cardButton.getListOfCardButtons().get(i).getCurrentPosX()+75;
            float textPosY = cardButton.getListOfCardButtons().get(i).getCurrentPosY()+178;
            font.draw(batch, priorityText, textPosX, textPosY);
        }
        batch.end();
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
        batch.getProjectionMatrix().setToOrtho2D(0, 0, i, i1);
        stage.getViewport().update(i, i1, true);
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