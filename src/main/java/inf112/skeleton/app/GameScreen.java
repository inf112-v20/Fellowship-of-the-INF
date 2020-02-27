package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Deck.GameDeck;
import inf112.skeleton.app.Grid.GameLogic;
import inf112.skeleton.app.Grid.PieceGrid;
import inf112.skeleton.app.Grid.Position;

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
    private CardButton cardButton;
    private GameDeck gameDeck;
    private Stage stage;
    private Texture texture;
    private SpriteBatch batch;
    private BitmapFont font;
    private GameLogic gameLogic;
    private GameLogic game;
    private PieceGrid pieceGrid;

    public GameScreen() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("RoborallyBoard.tmx");
        tiles = map.getTileSets().getTileSet("tileset.png");
        camera = new OrthographicCamera();
        gridPort = new StretchViewport(MAP_WITDTH_DPI, MAP_WITDTH_DPI, camera);
        camera.setToOrtho(false, MAP_WIDTH, MAP_WIDTH*2); //Stretch width of map to the edge
        camera.position.y = 0;        //Make map only take up the upper half of the screen
        camera.update();
        mapRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / TILE_WIDTH_DPI);
        // Layers, add more later
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        PieceGrid pieceGrid = new PieceGrid(MAP_WIDTH, MAP_WIDTH, map);
        game = new GameLogic(pieceGrid);

        initializePlayer();
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("cardslot.png")); //the background image for where the a selected card goes
        stage = new Stage(new ScreenViewport()); //Set up a stage for the ui
        Gdx.input.setInputProcessor(stage); //Start taking input from the ui
        cardButton = new CardButton();
        stage.addActor(cardButton.getLockInButton());
        gameDeck = new GameDeck();
        gameDeck.drawHand(gameDeck.getDrawDeck(), 0);
        for (int i = 0; i < cardButton.getListOfCardButtons().size(); i++) {
            stage.addActor(cardButton.getListOfCardButtons().get(i).getButton());
        }
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
        batch.begin();
        for (int xPosition = 0; xPosition <5 ; xPosition++) {
            batch.draw(texture, 150*xPosition, 75, texture.getWidth()*0.41f, texture.getHeight()*0.41f);
        }
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.draw(); //Draw the ui
        font = new BitmapFont();
        batch.begin();
        int numberOfCardButtons = cardButton.getListOfCardButtons().size();
        for (int i = 0; i < numberOfCardButtons; i++) {
            String priorityText = String.valueOf(cardButton.getListOfCardButtons().get(i).getCard().getPriority());
            int textPosX = cardButton.getListOfCardButtons().get(i).getCurrentPosX()+75;
            int textPosY = cardButton.getListOfCardButtons().get(i).getCurrentPosY()+178;
            font.draw(batch, priorityText, textPosX, textPosY);
        }
        batch.end();
    }

    /**
     * Update all changes to board
     * For now they are only movements of player
     */
    public void update() {
        playerLayer.setCell((int) player.getPos().getX(), (int) player.getPos().getY(), null);
        game.handleInput();
       // player.handleInput();
        playerLayer.setCell((int) player.getPos().getX(), (int) player.getPos().getY(), player.getPlayerCell());
    }

    /**
     * Changes the coordinates of the player based on user input
     */
    /*
    public void handleInput() {
        Position pos = player.getPos();
        float newX = pos.x;
        float newY = pos.y;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (pos.y + 1 < MAP_WIDTH) newY += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (pos.y - 1 >= 0) newY -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (pos.x - 1 >= 0) newX -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (pos.x + 1 < MAP_WIDTH) newX += 1;
        }
        player.setPos(newX, newY);
    }
*/
    @Override
    public void resize(int i, int i1) {

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
}