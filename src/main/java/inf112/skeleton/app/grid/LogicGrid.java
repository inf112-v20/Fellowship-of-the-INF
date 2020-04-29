package inf112.skeleton.app.grid;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.grid_objects.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The backend representation of the game board. It is a 2d grid of ArrayList<BoardPiece>.
 */
public class LogicGrid {
    //dimensions of grid
    private int width;
    private int height;

    //The number of players i the game
    private int numberOfPlayers;


    //A list of the locations of the spawn points
    private ArrayList<Position> spawnPointPositions;
    private ArrayList<Position> flagPositions;
    private ArrayList<ArrayList<List<Object>>> flagPositionsScores;

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
    private int repairLayerIndex; //1
    private int opCardLayerIndex; //2
    private int abyssLayerIndex;
    private int conveyorBeltLayerIndex; //4
    private int expressBeltLayerIndex; //5
    private int cogLayerIndex;
    private int pusherLayerIndex;
    private int laserLayerIndex; //8
    private int laserSourceLayerIndex; //9
    private int wallLayerIndex;
    private int flagLayerIndex;
    private int playerLayerIndex; //12

    //private BoardPiece[] [][] grid;
    private ArrayList<BoardPiece>[][] grid;
    private ArrayList<LaserSourcePiece> laserSourceList = new ArrayList<>();
    private ArrayList<PusherPiece> pushersList = new ArrayList<>();
    private BoardPieceGenerator boardPieceGenerator;


    public LogicGrid(int width, int height, TiledMap map) {
        grid = new ArrayList[width][height];
        this.width = grid.length;
        this.height = grid[0].length;

        numberOfPlayers = 8;

        //Make a lists for the location of spawns and flags
        initializeSpawns();
        initializeFlagList();

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
        removeUnusedFlags();
        createScoresForPositions();

    }

    /**
     * For each position in the grid, the corresponding cell in each layer is checked.
     * If the cell is non-empty, the corresponding BoardPiece is added to the PieceGrid
     * <p>
     * Ignore the warnings such as "x should not be passed as parameter y", since this setup is neccasary
     * because of the way the grid is initialized.
     */
    public void readTiledMapToPieceGrid() {
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
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
            if (layer == flagLayer) {
                //flagPositions.add(null);
                addToListOfFlagsPos(piece, x, y);
            } else if (layer == laserSourceLayer && piece instanceof LaserSourcePiece) {
                laserSourceList.add((LaserSourcePiece) piece);
            } else if (layer == pusherLayer && piece instanceof PusherPiece) {
                pushersList.add((PusherPiece) piece);
            } else if (layer == floorLayer) {
                addToListOfSpawnPos(piece, x, y);
            }

            //check returned piece isn't a null
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
    public void movePlayerToNewPosition(PlayerPiece playerPiece, Position oldPosition, Position newPosition) {

        if (!isInBounds(newPosition)) {
            grid[oldPosition.getX()][oldPosition.getY()].set(playerLayerIndex, new NullPiece(oldPosition, 0));
            return;
        }
        if (!isInBounds(oldPosition)) {
            grid[newPosition.getX()][newPosition.getY()].set(playerLayerIndex, playerPiece);
            return;
        }
        //check if position is free in logic grid
        if (positionIsFree(newPosition, playerLayerIndex)) {
            //set old position to NullPiece
            grid[oldPosition.getX()][oldPosition.getY()].set(playerLayerIndex, new NullPiece(oldPosition, 0));
            //add piece to new position
            grid[newPosition.getX()][newPosition.getY()].set(playerLayerIndex, playerPiece);
        }
    }

    /**
     * Checks if the position is available in the logic grid
     * Layers index objects are initialized at the beginning of this class
     *
     * @param position   position to check if it is free
     * @param layerIndex layer we are checking position in
     * @return true if there is a NullPiece in the position you are checking
     */
    public boolean positionIsFree(Position position, int layerIndex) {
        //conveyor belts cannot move players off board as it causes error
        if (!isInBounds(position)) {
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
     *
     * @param pos  position of piece
     * @param type piece type
     * @param <T>  piece class
     * @return piece if it is there, null otherwise
     */
    public <T extends BoardPiece> T getPieceType(Position pos, Class<T> type) {
        if (!isInBounds(pos)) return null;
        for (BoardPiece piece : grid[pos.getX()][pos.getY()]) {
            if (piece.getClass().equals(type)) return (T) piece;
        }
        return null;
    }

    /**
     * Checks if there is a certain type of piece in the given position
     * @param pos position to check for piece type
     * @param type piece type to check for
     * @return true if there is a piece of that type in the position
     */
    public boolean positionHasPieceType(Position pos, Class type) {
        if (!isInBounds(pos)) return false;
        for (BoardPiece piece : grid[pos.getX()][pos.getY()]) {
            if (piece.getClass().equals(type)) return true;
        }
        return false;
    }

    /**
     * A method used to create the list of spawn points based on the number of players
     */
    private void initializeSpawns() {
        spawnPointPositions = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            spawnPointPositions.add(null);
        }
    }

    /**
     * Check if the piece is a spawn point.
     * If so, then add it's position to the list spawnPointPosition,
     * at the location in the list corresponding to the spawnNumber of the piece.
     * <p>
     * This list is used by the players to find their first spawn point.
     *
     * @param piece piece to be checked
     * @param x     coordinates for @param piece
     * @param y     coordinates for @param piece
     */
    private void addToListOfSpawnPos(BoardPiece piece, int x, int y) {
        //If it is a spawnPoint tile, add it to a list of start positions
        int MIN_SPAWNPOINT_NUMBER = 121;
        int MAX_SPAWNPOINT_NUMBER = 132;
        int id = piece.getId();
        if (id >= MIN_SPAWNPOINT_NUMBER && id <= MAX_SPAWNPOINT_NUMBER) {
            spawnPointPositions.set(((SpawnPointPiece) piece).getSpawnNumber() - 1, new Position(x, y));
        }
    }

    public ArrayList<Position> getSpawnPointPositions() {
        return spawnPointPositions;
    }

    /**
     * Checks if robot is allowed to leave its current position in a certain direction.
     * Checks if walls are blocking the way.
     *
     * @param currentPosition position player is in now
     * @param dir             direction we would like to leave piece from
     * @return true if robot can leave it's current position.
     */
    public boolean canLeavePosition(Position currentPosition, Direction dir) {
        WallPiece possibleWallPiece = getPieceType(currentPosition, WallPiece.class);
        if (possibleWallPiece != null) {
            //cannot leave if the WallPiece player is standing on has a wall in the direction
            if (!(possibleWallPiece).canLeave(dir)) return false;
        }
        LaserSourcePiece possibleLaserSourcePiece = getPieceType(currentPosition, LaserSourcePiece.class);
        if (possibleLaserSourcePiece != null) {
            //cannot leave if the laserSourcePiece player is standing on has a wall in the direction
            return (possibleLaserSourcePiece).canLeave(dir);
        }
        PusherPiece possiblePusherPiece = getPieceType(currentPosition, PusherPiece.class);
        if (possiblePusherPiece != null) {
            //cannot leave if the pusherPiece player is standing on has a wall in the direction
            return (possiblePusherPiece).canLeave(dir);
        }
        return true;
    }

    /**
     * Checks if a robot is allowed to enter the position in from from a certain direction.
     * Checks if walls are blocking the way.
     * Checks if players that cannot be pushed are blocking the way.
     *
     * @param positionInFront position we are checking if the robot can enter
     * @param dir             direction we are entering the new position from
     * @return true if it is legal to enter position from direction
     */
    public boolean canEnterNewPosition(Position positionInFront, Direction dir) {
        //if the piece in front is within the bounds, check what is there
        if (isInBounds(positionInFront)) {
            WallPiece possibleWallPiece = getPieceType(positionInFront, WallPiece.class);
            if (possibleWallPiece != null) {
                //cannot go if WallPiece in front has a wall facing player
                if (!(possibleWallPiece).canGo(dir)) return false;
            }
            LaserSourcePiece possibleLaserSourcePiece = getPieceType(positionInFront, LaserSourcePiece.class);
            if (possibleLaserSourcePiece != null) {
                //cannot go if LaserSourcePiece in front has a wall facing player
                return (possibleLaserSourcePiece).canGo(dir);
            }
            PusherPiece possiblePusherPiece = getPieceType(positionInFront, PusherPiece.class);
            if (possiblePusherPiece != null) {
                //cannot go if PusherPiece in front has a wall facing player
                return (possiblePusherPiece).canGo(dir);
            }
        }
        return true;
    }

    /**
     * Checks if a position is inbounds
     *
     * @param pos the position to check
     * @return true if the position is inside the map, false otherwise
     */
    public boolean isInBounds(Position pos) {
        return pos.getY() < height && pos.getY() >= 0 && pos.getX() < width && pos.getX() >= 0;
    }

    /**
     * Method used for creating the list of flag locations
     */
    public void initializeFlagList() {
        //Create an empty list with enough space for the max number of flags
        int MAX_NUMBER_OF_FLAGS = 4;
        flagPositions = new ArrayList<>();
        for (int i=0; i < MAX_NUMBER_OF_FLAGS; i++) { flagPositions.add(null); }
    }

    /**
     * Add a flag piece to the list of spawn points, where the location of the position in the list corresponds to
     * flagNumber - 1. Meaning the position of flag 1 is located at flagPosition.get(0);
     *
     * @param piece The piece that may be a flag piece
     * @param x The x coordinate of the location of the piece
     * @param y The y coordinate of the location of the piece
     */
    public void addToListOfFlagsPos(BoardPiece piece, int x, int y) {
        if (piece instanceof FlagPiece) {
            flagPositions.set(((FlagPiece) piece).getFlagNumber() - 1, new Position(x,y));
        }
    }

    public ArrayList<Position> getFlagPositions() {
        return flagPositions;
    }

    /**
     * TODO I believe this method is no longer needed.
     * The method addToListOfFlagsPos add to flagPositions as flag pieces are discovered during setPieceInGrid
     */
    private void sortFlagPositions() {
        for (int y = width - 1; y >= 0; y--) {
            for (int x = 0; x < height; x++) {
                Position pos = new Position(x, y);
                if (!positionIsFree(pos, flagLayerIndex)) {
                    FlagPiece flagPiece = (FlagPiece) grid[x][y].get(flagLayerIndex);
                    flagPositions.set(flagPiece.getFlagNumber() - 1, pos);
                }
            }
        }
    }

    /**
     * Finds a valid spawn point.
     * <p>
     * If spawn point is not valid, the positions N, S, E, W are checked, and if these are not valid,
     * then the positions N, S, E, W of them are checked.
     *
     * @param spawnPoint check if it is valid
     * @return valid spawn point
     */
    public ArrayList<Position> getValidSpawnPointPosition(Position spawnPoint) {
        //if spawnPoint is valid, return spawnPoint
        ArrayList<Position> availablePositions = new ArrayList<>();
        if (positionIsFree(spawnPoint, playerLayerIndex)) {
            System.out.println("Actual spawnpoint is available at " + spawnPoint);
            availablePositions.add(spawnPoint);
            return availablePositions;
        }

        //if spawnPoint is not valid, check the neighbouring positions.
        for (Direction dir : Direction.values()) {
            Position pos = spawnPoint.getPositionIn(dir);
            Position pos2 = pos.getPositionIn(dir.getRightTurnDirection());
            if (positionIsFree(pos, playerLayerIndex)
                    && spawnIsSafe(pos)) {
                System.out.println("Spawnpoint1 is available at " + pos);
                availablePositions.add(pos);
            }
            if (positionIsFree(pos2, playerLayerIndex)
                    && spawnIsSafe(pos2)) {
                System.out.println("Spawnpoint2 is available at " + pos2);
                availablePositions.add(pos2);
            }
        }
        return availablePositions;
    }

    /**
     * Checks if it is save for a player to spawn in a position
     *
     * @param spawnPoint position to be checked
     * @return true if the player doesn't die by spawning there
     */
    private boolean spawnIsSafe(Position spawnPoint) {
        if(!isInBounds(spawnPoint))return false;
        ArrayList<BoardPiece> boardPieceList = grid[spawnPoint.getX()][spawnPoint.getY()];
        for (BoardPiece piece : boardPieceList) {
            if (piece instanceof AbyssPiece) return false;
            //you can add more things to check for here
        }
        return true;
    }

    public ArrayList<LaserSourcePiece> getLaserSourceList() {
        return laserSourceList;
    }

    public ArrayList<PusherPiece> getPushersList() {
        return pushersList;
    }

    /**
     * Checks of the position the laser wan't to enter is
     * - In bounds
     * - The position is not blocked by a player
     * - The previous position can be left (not blocked by wall)
     * - The position can be entered (not blocked by wall)
     *
     * @param checkPosition position to check if the laser can enter
     * @param dir           direction of laser
     * @return true if laser has not been blocked an can the position
     */
    public boolean blocksLaser(Position checkPosition, Direction dir) {
        Position previousPosition = checkPosition.getPositionIn(dir.getOppositeDirection());
        if (isInBounds(checkPosition)) {
            return (!positionIsFree(checkPosition, playerLayerIndex) ||
                    !canLeavePosition(previousPosition, dir) ||
                    !canEnterNewPosition(checkPosition, dir));
        }
        return true;
    }

    /**
     * Gets the list of positions that are the lasers path.
     *
     * @param laserSourcePiece the source of the laser who's path we are finding
     * @return the positions of the path of the laser
     */
    public ArrayList<Position> getLaserPath(LaserSourcePiece laserSourcePiece) {
        ArrayList<Position> laserPositions = new ArrayList<>();
        Direction directionOflaser = laserSourcePiece.getLaserDir();
        Position currentPosition = laserSourcePiece.getPos();
        laserPositions.add(currentPosition);
        //if there is a player standing on the lasersource piece, the path does not need to be generated
        if (!positionIsFree(currentPosition, playerLayerIndex)) return laserPositions;
        currentPosition = currentPosition.getPositionIn(directionOflaser);
        while (!blocksLaser(currentPosition, directionOflaser)) {
            laserPositions.add(currentPosition);
            currentPosition = currentPosition.getPositionIn(directionOflaser);
        }
        return laserPositions;
    }


    /**
     * Creates a list of scores for every position (not abysses) for every flag in the game.
     * The score is how many moves it takes to reach the flag from that position,
     * which takes walls and holes into consideration.
     */
    private void createScoresForPositions(){

        ArrayList <Position> flags = getFlagPositions();
        ArrayList<ArrayList<List<Object>>> flagMapPositions = new ArrayList<>();

        for (Position flag : flags) {
            ArrayList<List<Object>> mapPositions = new ArrayList<>();
            int moves = 0;
            List<Object> posAndScore = Arrays.asList(flag, moves);
            mapPositions.add(posAndScore);

            for (int j = 0; j < mapPositions.size(); j++) {
                Position pos = (Position) mapPositions.get(j).get(0);
                int movesToPos = (Integer) mapPositions.get(j).get(1);

                for (Direction dir : Direction.values()) {
                    Position neighborPos = pos.getPositionIn(dir);
                    if (isDeadMove(neighborPos)) {
                        continue;
                    }
                    if (!(canLeavePosition(pos, dir) && canEnterNewPosition(neighborPos, dir))) {
                        continue;
                    }
                    int movesToNeighborPos = movesToPos + 1;
                    posAndScore = Arrays.asList(neighborPos, movesToNeighborPos);
                    boolean alreadyChecked = false;

                    for (List<Object> mapPosition : mapPositions) {
                        Position checkedPos = (Position) mapPosition.get(0);
                        if (checkedPos.equals(neighborPos)) {
                            alreadyChecked = true;
                            break;
                        }
                    }
                    if(!alreadyChecked) mapPositions.add(posAndScore);

                }
            }
            flagMapPositions.add(mapPositions);
        }
        flagPositionsScores = flagMapPositions;
    }

    public ArrayList<ArrayList<List<Object>>> getFlagPositionScores(){
        return flagPositionsScores;
    }


    /**
     * Method for checking if a move results in death
     *
     * @param position to check
     * @return whether the move results in death
     */
    public boolean isDeadMove(Position position) {
        int x = position.getX();
        int y = position.getY();
        BoardPiece currPiece;
        if (isInBounds(position)) {
            for (int i = 0; i < grid[x][y].size(); i++) {
                currPiece = grid[x][y].get(i);
                if (currPiece instanceof AbyssPiece) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private void removeUnusedFlags(){
        for(int i = 0; i < flagPositions.size();i++){
            if(flagPositions.get(i) == null){
                flagPositions.remove(i);
                i--;
            }
        }
    }

}
