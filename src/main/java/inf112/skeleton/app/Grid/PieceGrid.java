package inf112.skeleton.app.Grid;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.GridObjects.*;

import java.util.ArrayList;

public class PieceGrid {
    //dimensions of grid
    private int width;
    private int height;
    private int numberOfLayers;

    //Tiled layers
    private TiledMapTileLayer floorLayer;
    private TiledMapTileLayer repairLayer;
    private TiledMapTileLayer opCardLayer;
    private TiledMapTileLayer abyssLayer;
    private TiledMapTileLayer conveyorBeltLayer;
    private TiledMapTileLayer expressBeltLayer;
    private TiledMapTileLayer cogLayer;
    private TiledMapTileLayer pusherLayer;
    private TiledMapTileLayer laserLayer;
    private TiledMapTileLayer laserSourceLayer;
    private TiledMapTileLayer wallLayer;
    private TiledMapTileLayer flagLayer;
    private TiledMapTileLayer playerLayer;

    //The indexes of the layers
    private int floorLayerIndex;
    private int repairLayerIndex;
    private int opCardLayerIndex;
    private int abyssLayerIndex;
    private int conveyorBeltLayerIndex;
    private int expressBeltLayerIndex;
    private int cogLayerIndex;
    private int pusherLayerIndex;
    private int laserLayerIndex;
    private int laserSourceLayerIndex;
    private int wallLayerIndex;
    private int flagLayerIndex;
    private int playerLayerIndex;

    //private BoardPiece[] [][] grid;
    private ArrayList<BoardPiece>[][] grid;
    private BoardPieceGenerator boardPieceGenerator;

    public PieceGrid(int width, int height, TiledMap map) { //TODO get source for map
        grid = new ArrayList[width][height];
        this.width = grid[0].length;
        this.height = grid.length;
        numberOfLayers = map.getLayers().getCount(); //not used right now

        //extract each later from the tiled map
        floorLayer = (TiledMapTileLayer) map.getLayers().get("Floor");
        repairLayer = (TiledMapTileLayer) map.getLayers().get("Repair");
        opCardLayer = (TiledMapTileLayer) map.getLayers().get("OpCard");
        abyssLayer = (TiledMapTileLayer) map.getLayers().get("Abyss");
        conveyorBeltLayer = (TiledMapTileLayer) map.getLayers().get("Conveyor belt");
        expressBeltLayer = (TiledMapTileLayer) map.getLayers().get("Express belt");
        cogLayer = (TiledMapTileLayer) map.getLayers().get("Cog");
        pusherLayer = (TiledMapTileLayer) map.getLayers().get("Pusher");
        laserLayer = (TiledMapTileLayer) map.getLayers().get("Lazer");
        laserSourceLayer = (TiledMapTileLayer) map.getLayers().get("Laser Source");
        wallLayer = (TiledMapTileLayer) map.getLayers().get("Wall");
        flagLayer = (TiledMapTileLayer) map.getLayers().get("Flag");
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");

        //extract index of each layer
        floorLayerIndex = map.getLayers().getIndex("Floor");
        repairLayerIndex = map.getLayers().getIndex("Repair");
        opCardLayerIndex = map.getLayers().getIndex("OpCard");
        abyssLayerIndex = map.getLayers().getIndex("Abyss");
        conveyorBeltLayerIndex = map.getLayers().getIndex("Conveyor belt");
        expressBeltLayerIndex = map.getLayers().getIndex("Express belt");
        cogLayerIndex = map.getLayers().getIndex("Cog");
        pusherLayerIndex = map.getLayers().getIndex("Pusher");
        laserLayerIndex = map.getLayers().getIndex("Lazer");
        laserSourceLayerIndex = map.getLayers().getIndex("Laser Source");
        wallLayerIndex = map.getLayers().getIndex("Wall");
        flagLayerIndex = map.getLayers().getIndex("Flag");
        playerLayerIndex = map.getLayers().getIndex("Player");
    }

    /**
     * For each position in the grid, the corresponding cell in each layer is checked.
     * If the cell is non-empty, the corresponding BoardPiece is added to the PieceGrid
     */
    public void readTiledMapToPieceGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new ArrayList<BoardPiece>();
                //create new boardpieceGenerator for appropriate coordinate
                boardPieceGenerator = new BoardPieceGenerator(x, y);

                setPieceInGrid(floorLayer, floorLayerIndex, x, y);
                setPieceInGrid(repairLayer, repairLayerIndex, x, y);
                setPieceInGrid(opCardLayer, opCardLayerIndex, x, y);
                setPieceInGrid(abyssLayer, abyssLayerIndex, x, y);
                setPieceInGrid(conveyorBeltLayer, conveyorBeltLayerIndex, x, y);
                setPieceInGrid(expressBeltLayer, expressBeltLayerIndex, x, y);
                setPieceInGrid(cogLayer, cogLayerIndex, x, y);
                setPieceInGrid(pusherLayer, pusherLayerIndex, x, y);
                setPieceInGrid(laserLayer, laserLayerIndex, x, y);
                setPieceInGrid(laserSourceLayer, laserSourceLayerIndex, x, y);
                setPieceInGrid(wallLayer, wallLayerIndex, x, y);
                setPieceInGrid(flagLayer, flagLayerIndex, x, y);
                setPieceInGrid(playerLayer, playerLayerIndex, x, y);
            }
        }
    }

    /**
     * Adds a BoardPiece to the PieceGrid when needed
     * If the cell at the current position in the layer is empty, nothing is added to the grid
     *
     * @param layer        the piece is from
     * @param layerIndex   index of the layer
     * @param x-coordinate
     * @param y-coordinate
     */
    public void setPieceInGrid(TiledMapTileLayer layer, int layerIndex, int x, int y) {
        if (layer != null && layer.getCell(x, y) != null) {
            //get id of BoardPiece
            int id = layer.getCell(x, y).getTile().getId();
            //if cell in layer is not empty, generate the corresponding BoardPiece and add to grid
            BoardPiece piece = boardPieceGenerator.generate(id);
            //check returned piece isn't a null
            int size = grid[x][y].size();
            if (piece != null) {
                grid[x][y].add(layerIndex, piece);
                return;
            }
        }
        //add a NullPiece if there is nothing to add
        grid[x][y].add(new NullPiece(new Position(x, y), -1));
    }
}

