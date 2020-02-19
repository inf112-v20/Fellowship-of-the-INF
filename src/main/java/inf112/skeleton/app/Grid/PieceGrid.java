package inf112.skeleton.app.Grid;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.GridObjects.BoardPiece;
import inf112.skeleton.app.GridObjects.FloorPiece;
import inf112.skeleton.app.GridObjects.Position;
import inf112.skeleton.app.GridObjects.RepairPiece;

import java.util.ArrayList;

public class PieceGrid {

    private int width;
    private int height;

    //Tiled layers
    private TiledMapTileLayer floorLayer;
    private TiledMapTileLayer repairLayer;
    private TiledMapTileLayer opCardLayer;
    private TiledMapTileLayer abyssLayer;
    private TiledMapTileLayer conveyorBeltLayer;
    private TiledMapTileLayer expressBeltLayer;
    private TiledMapTileLayer cogLayer;
    private TiledMapTileLayer pusherLayer;
    private TiledMapTileLayer lasersLayer;
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

    ArrayList<BoardPiece>[][] grid;

    public PieceGrid(int width, int height, TiledMap map) { //TODO get source for map
        map.getLayers().
        grid = new ArrayList[width][height];
        this.width = grid[0].length;
        this.height = grid.length;

        //extract each later from the tiled map
        floorLayer = (TiledMapTileLayer) map.getLayers().get("Floor");
        repairLayer = (TiledMapTileLayer) map.getLayers().get("Repair");
        opCardLayer = (TiledMapTileLayer) map.getLayers().get("OpCard");
        abyssLayer = (TiledMapTileLayer) map.getLayers().get("Abyss");
        conveyorBeltLayer = (TiledMapTileLayer) map.getLayers().get("Conveyor belt");
        expressBeltLayer = (TiledMapTileLayer) map.getLayers().get("Express belt");
        cogLayer = (TiledMapTileLayer) map.getLayers().get("Cog");
        pusherLayer = (TiledMapTileLayer) map.getLayers().get("Pusher");
        lasersLayer = (TiledMapTileLayer) map.getLayers().get("Lazer");
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

    public void readTiledMapToPieceGrid(TiledMap map) {
        //id to assign to BoardPiece
        int id;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new ArrayList<BoardPiece>();

                //get id for FloorPiece (it's 5)
                id = floorLayer.getCell(x,y).getTile().getId();
                //add a FloorPiece to it's position in the PieceGrid
                grid[x][y].add(floorLayerIndex, new FloorPiece(new Position(x,y), id));

                //get id for RepairPiece (it's 15)
                id = repairLayer.getCell(x,y).getTile().getId();
                if (id != 0) grid[x][y].add(floorLayerIndex, new RepairPiece(new Position(x,y), id));


                /**
                 *   // Flag layer
                 *                 TiledMapTileLayer.Cell flagCell = flagLayer.getCell(x, y);
                 *                 if (flagCell == null)
                 *                     gameLogicGrid[x][y].add(flagIndex, new NothingSpecial());
                 *                 else {
                 *                     int flagLayerId = flagCell.getTile().getId();
                 *                     gameLogicGrid[x][y].add(flagIndex, new SpecialLayerObject(tiles, flagLayerId));
                 *                 }
                 */


            }
        }
    }


    public void setPieceInPieceGrid(TiledMapTileLayer layer, int layerIndex, int x, int y) {
        //get id of BoardPiece
        int id = layer.getCell(x, y).getTile().getId();
        //if cell in layer is not empty, generate the corresponding BoardPiece
        if (id != 0) {
            BoardPiece piece = boardPieceGenerator(new Position(x,y), id);
            grid[x][y].add(layerIndex, piece);
        }
    }




}
