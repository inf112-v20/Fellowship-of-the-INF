package inf112.skeleton.app.grid;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.gridObjects.BoardPiece;
import inf112.skeleton.app.gridObjects.BoardPieceGenerator;
import inf112.skeleton.app.gridObjects.NullPiece;
import inf112.skeleton.app.gridObjects.PlayerPiece;

import java.util.ArrayList;
public class Map {
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

    public Map(int width, int height, TiledMap map) {
        grid = new ArrayList[width][height];
        this.width = grid[0].length;
        this.height = grid.length;
        numberOfLayers = map.getLayers().getCount(); //not used right now

        //extract each layer from the tiled map
        floorLayer = (TiledMapTileLayer) map.getLayers().get("Floor");
        repairLayer = (TiledMapTileLayer) map.getLayers().get("Repair");
        opCardLayer = (TiledMapTileLayer) map.getLayers().get("OpCard");
        abyssLayer = (TiledMapTileLayer) map.getLayers().get("Abyss");
        conveyorBeltLayer = (TiledMapTileLayer) map.getLayers().get("Conveyor belt");
        expressBeltLayer = (TiledMapTileLayer) map.getLayers().get("Express belt");
        cogLayer = (TiledMapTileLayer) map.getLayers().get("Cog");
        pusherLayer = (TiledMapTileLayer) map.getLayers().get("Pusher");
        laserLayer = (TiledMapTileLayer) map.getLayers().get("Lasers");
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
        laserLayerIndex = map.getLayers().getIndex("Lasers");
        laserSourceLayerIndex = map.getLayers().getIndex("Laser Source");
        wallLayerIndex = map.getLayers().getIndex("Wall");
        flagLayerIndex = map.getLayers().getIndex("Flag");
        playerLayerIndex = map.getLayers().getIndex("Player");

        readTiledMapToPieceGrid();
    }

    /**
     * For each position in the grid, the corresponding cell in each layer is checked.
     * If the cell is non-empty, the corresponding BoardPiece is added to the PieceGrid
     *
     * Ignore the warnings such as "x should not be passed as parameter y", since this setup is neccasary
     * because of the way the grid is initialized.
     */
    public void readTiledMapToPieceGrid() {
            for (int x = 0; x < height; x++) {
                for (int y = width-1; y >= 0; y--) {
                grid[y][x] = new ArrayList<BoardPiece>();
                //create new boardpieceGenerator for appropriate coordinate
                boardPieceGenerator = new BoardPieceGenerator(y, x);

                setPieceInGrid(floorLayer, floorLayerIndex, y, x);
                setPieceInGrid(repairLayer, repairLayerIndex, y, x);
                setPieceInGrid(opCardLayer, opCardLayerIndex, y, x);
                setPieceInGrid(abyssLayer, abyssLayerIndex, y, x);
                setPieceInGrid(conveyorBeltLayer, conveyorBeltLayerIndex, y, x);
                setPieceInGrid(expressBeltLayer, expressBeltLayerIndex, y, x);
                setPieceInGrid(cogLayer, cogLayerIndex, y, x);
                setPieceInGrid(pusherLayer, pusherLayerIndex, y, x);
                setPieceInGrid(laserLayer, laserLayerIndex, y, x);
                setPieceInGrid(laserSourceLayer, laserSourceLayerIndex, y, x);
                setPieceInGrid(wallLayer, wallLayerIndex, y, x);
                setPieceInGrid(flagLayer, flagLayerIndex, y, x);
                setPieceInGrid(playerLayer, playerLayerIndex, y, x);
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<BoardPiece>[][] getGrid() {
        return grid;
    }

    /**
     * Adds a new PlayerPiece to the logic grid
     * @param playerPiece
     */
    public void placeNewPlayerPieceOnMap(PlayerPiece playerPiece) {
        if (positionIsFree(playerPiece.getPos(), playerLayerIndex)) {
            int x = playerPiece.getPos().getX();
            int y = playerPiece.getPos().getY();
            grid[x][y].set(playerLayerIndex, playerPiece);
        }
    }

    /**
     * If possible, move a player piece to a new position
     * @param oldPosition
     * @param newPosition
     */
    public void movePlayerToNewPosition(Position oldPosition, Position newPosition) {
        BoardPiece playerPiece = grid[oldPosition.getX()][oldPosition.getY()].get(playerLayerIndex);
        if (playerPiece instanceof PlayerPiece) {
            //check if position is free in logic grid
            if(positionIsFree(newPosition, playerLayerIndex)) {
                //set old position to NullPiece
                grid[oldPosition.getX()][oldPosition.getY()].set(playerLayerIndex, new NullPiece(oldPosition, 0));
                //add piece to new position
                grid[newPosition.getX()][newPosition.getY()].set(playerLayerIndex, playerPiece);
            } else {
                System.out.println(newPosition.toString() + " is not available for player");
            }
        } else {
            System.out.println("Cannot move nonplayer object" + playerPiece.toString() + " to new position");
        }
    }

    /**
     * Checks if the position is available in the logic grid
     * @param position
     * @param layerIndex
     * @return true if there is a NullPiece in the position you are checking
     */
    private boolean positionIsFree(Position position, int layerIndex) {
        return (grid[position.getX()][position.getY()].get(layerIndex) instanceof NullPiece);
    }


}

