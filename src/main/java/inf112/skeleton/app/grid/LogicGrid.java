package inf112.skeleton.app.grid;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.grid_objects.*;

import java.util.ArrayList;

public class LogicGrid {
    //dimensions of grid
    private int width;
    private int height;

    //TODO unused atm
    private int numberOfLayers;

    //The number of players i the game
    private int numberOfPlayers;

    //A list of the locations of the spawn points
    private ArrayList<Position> spawnPointPositions;

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

    public LogicGrid(int width, int height, TiledMap map) {
        grid = new ArrayList[width][height];
        this.width = grid[0].length;
        this.height = grid.length;
        numberOfPlayers = 8;
        numberOfLayers = map.getLayers().getCount(); //not used right now

        //Make a list of what will be the first player spawns
        initializeSpawns();

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
     * <p>
     * Ignore the warnings such as "x should not be passed as parameter y", since this setup is neccasary
     * because of the way the grid is initialized.
     */
    public void readTiledMapToPieceGrid() {
        for (int y = width - 1; y >= 0; y--) {
            for (int x = 0; x < height; x++) {
                grid[x][y] = new ArrayList<>();
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

            isThisASpawnPoint(piece, x, y);

            //check returned piece isn't a null
            int size = grid[x][y].size();
            grid[x][y].add(layerIndex, piece);
            return;
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
     *
     * @param playerPiece playerPiece to place
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
     *
     * @param oldPosition the position player is moving from
     * @param newPosition the position player is moving to
     */
    public void movePlayerToNewPosition(Position oldPosition, Position newPosition) {
        BoardPiece playerPiece = grid[oldPosition.getX()][oldPosition.getY()].get(playerLayerIndex);
        if (playerPiece instanceof PlayerPiece) {
            //check if position is free in logic grid
            if (positionIsFree(newPosition, playerLayerIndex)) {
                //set old position to NullPiece
                grid[oldPosition.getX()][oldPosition.getY()].set(playerLayerIndex, new NullPiece(oldPosition, 0));
                //add piece to new position
                grid[newPosition.getX()][newPosition.getY()].set(playerLayerIndex, playerPiece);
            } else {
                System.out.println(newPosition.toString() + " is not available for " + playerPiece.toString());
            }
        } else {
            System.out.println("Cannot move nonplayer object" + playerPiece.toString() + "from " + oldPosition.toString() + " to " + newPosition.toString());
        }
    }

    /**
     * Checks if the position is available in the logic grid
     *
     * @param position position to check if it is free
     * @param layerIndex layer we are checking position in
     * @return true if there is a NullPiece in the position you are checking
     */
    public boolean positionIsFree(Position position, int layerIndex) {
        //conveyor belts cannot move players off board as it causes error
        //TODO, allow conveyorBelt to push player to death.
        if(!isInBounds(position)){
            return false;
        }
        return (grid[position.getX()][position.getY()].get(layerIndex) instanceof NullPiece);
    }

    /**
     * @param pos position you are getting a list of piece from
     * @return list of piece in the position you are checking
     */
    public ArrayList<BoardPiece> getAllPieces(Position pos) {
        return grid[pos.getX()][pos.getY()];
    }

    /**
     * Remember to check that this method does not return null
     * @param pos position of piece
     * @param type piece type
     * @param <T> piece class
     * @return piece if it is there, null otherwise
     */
    public <T extends BoardPiece> T getPieceType(Position pos, Class<T> type) {
        for (BoardPiece piece : grid[pos.getX()][pos.getY()]) {
            if (piece.getClass().equals(type)) return (T) piece;
        }
        return null;
    }

    private void initializeSpawns() {
        spawnPointPositions = new ArrayList<>();
        for (int i=0; i<numberOfPlayers; i++) {
            spawnPointPositions.add(null);
        }
    }

    /**
     * Check if the piece is a spawn point.
     * If so, then add it's position to the list spawnPointPosition,
     * at the location in the list corresponding to the spawnNumber of the piece.
     *
     * This list is used by the players to find their first spawn point.
     * @param piece
     * @param x
     * @param y
     */
    private void isThisASpawnPoint(BoardPiece piece, int x, int y) {
        //If it is a spawnPoint tile, add it to a list of start positions
        int MIN_SPAWNPOINT_NUMBER = 121;
        int MAX_SPAWNPOINT_NUMBER = 132;
        int id = piece.getId();
        if (id >= MIN_SPAWNPOINT_NUMBER && id <= MAX_SPAWNPOINT_NUMBER) {
            spawnPointPositions.set(((SpawnPointPiece) piece).getSpawnNumber()-1, new Position(x,y));
        }
    }

    public ArrayList<Position> getSpawnPointPositions() { return spawnPointPositions; }

    /**
     * Checks if robot is allowed to leave its current position in a certain direction.
     * Checks if walls are blocking the way.
     * @param currentPosition position player is in now
     * @param dir direction we would like to leave piece from
     * @return true if robot can leave it's current position.
     */
    public boolean canLeavePosition(Position currentPosition, Direction dir) {
        WallPiece possibleWallPiece = getPieceType(currentPosition, WallPiece.class);
        if (possibleWallPiece != null && possibleWallPiece instanceof WallPiece) {
            //cannot leave if the WallPiece player is standing on has a wall in the direction
            if (!(possibleWallPiece).canLeave(dir)) return false;
        }
        LaserSourcePiece possibleLaserSourcePiece = getPieceType(currentPosition, LaserSourcePiece.class);
        if (possibleLaserSourcePiece != null && possibleLaserSourcePiece instanceof WallPiece) {
            //cannot leave if the laserSourcePiece player is standing on has a wall in the direction
            if (!(possibleLaserSourcePiece).canLeave(dir)) return false;
        }
        return true;
    }

    /**
     * Checks if a robot is allowed to enter the position in from from a certain direction.
     * Checks if walls are blocking the way.
     * Checks if players that cannot be pushed are blocking the way.
     * @param positionInFront position we are checking if the robot can enter
     * @param dir direction we are entering the new position from
     * @return true if it is legal to enter position from direction
     */
    public boolean canEnterNewPosition(Position positionInFront, Direction dir) {
        //if the piece in front is within the bounds, check what is there
        if (isInBounds(positionInFront)) {
            WallPiece possibleWallPiece = getPieceType(positionInFront, WallPiece.class);
            if (possibleWallPiece != null && possibleWallPiece instanceof WallPiece) {
                //cannot go if WallPiece in front has a wall facing player
                if (!(possibleWallPiece).canGo(dir)) return false;
            }
            LaserSourcePiece possibleLaserSourcePiece = getPieceType(positionInFront, LaserSourcePiece.class);
            if (possibleLaserSourcePiece != null && possibleLaserSourcePiece instanceof WallPiece) {
                //cannot go if WallPiece in front has a wall facing player
                if (!(possibleLaserSourcePiece).canGo(dir)) return false;
            }
        }
        return true;
    }

    /**
     * Checks if a position is inbounds
     * @param pos the position to check
     * @return true if the position is inside the map, false otherwise
     */
    public boolean isInBounds(Position pos){
        return pos.getY() < height && pos.getY() >= 0 && pos.getX() < width && pos.getX() >= 0;
    }
}
