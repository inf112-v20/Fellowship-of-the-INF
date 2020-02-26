package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.PieceGrid;
import inf112.skeleton.app.Grid.Position;
import inf112.skeleton.app.GridObjects.*;

import java.util.ArrayList;

/**
 * Class representing a player.
 */
public class Player {

    private Position pos; //position of player
    private TiledMapTileLayer.Cell currentCell; //Cell for the current state of the player
    private TiledMapTileLayer.Cell playerCell; //cell for normal player
    private TiledMapTileLayer.Cell deadPlayerCell; //cell for dead player looks
    private TiledMapTileLayer.Cell wonPlayerCell; //cell for player who has won looks
    private int MAP_WIDTH;
    private int MAP_HEIGHT;
    private PieceGrid logicMap;
    private ArrayList<BoardPiece>[][] pieceGrid;
    private Direction dir;
    private PlayerPiece playerPiece;
    private GameLogic game;

    public Player(int playerNumber, GameLogic game) {
        // TODO Refactor getGrid() (lol)
        this.logicMap = game.getLogicGrid();
        this.pieceGrid = logicMap.getGrid();
        this.game = game;
        this.playerPiece = new PlayerPiece(new Position(0, 0), 200, Direction.NORTH);
        pos = new Position(0, 0);
        dir = Direction.NORTH;
        //place player at the bottom left corner
        pos.setX(0);
        pos.setY(0);
        MAP_WIDTH = game.getLogicGrid().getWidth();
        MAP_HEIGHT = game.getLogicGrid().getHeight();

        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 1)));
        wonPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 2)));
        currentCell = playerCell; //appearance starts as normal player
    }

    /**
     * Getter for position
     *
     * @return position of player
     */
    public Position getPos() {
        return pos;
    }

    /**
     * Setter for position of player
     *
     * @param x new x position
     * @param y new y position
     */
    public void setPos(int x, int y) {
        pos.setX(x);
        pos.setY(y);
    }

    /**
     * Getter for player cell
     *
     * @return player
     */
    public TiledMapTileLayer.Cell getPlayerCell() {
        return currentCell;
    }

    /**
     * Tries to move the player in a new direction
     * @param newDirection
     */
    public void tryToGo(Direction newDirection) {
        int newX = pos.getX();
        int newY = pos.getY();
        dir = newDirection;
        switch (newDirection) {
            case NORTH:
                if (isLegalMove(pos.getX(), pos.getY(), dir)) newY += 1;
                if (isDeadMove(pos.getX(), newY)) currentCell = deadPlayerCell;
                break;
            case SOUTH:
                if (isLegalMove(pos.getX(), pos.getY(), dir)) newY -= 1;
                if (isDeadMove(pos.getX(), newY)) currentCell = deadPlayerCell;
                break;
            case WEST:
                if (isLegalMove(pos.getX(), pos.getY(), dir)) newX -= 1;
                if (isDeadMove(newX, pos.getY())) currentCell = deadPlayerCell;
                break;
            case EAST:
                if (isLegalMove(pos.getX(), pos.getY(), dir)) newX += 1;
                if (isDeadMove(newX, pos.getY())) currentCell = deadPlayerCell;
                break;
        }

        //if position has changed, update logic grid
        if (newY != pos.getY() || newX != pos.getX()) {
            logicMap.movePlayerToNewPosition(pos, new Position(newX, newY));
        }

        if (newY != pos.getY() || newX != pos.getX()) {
            System.out.println("OLD: " + pos.getX() + "," + pos.getY());

            ArrayList<BoardPiece> array = pieceGrid[pos.getX()][pos.getY()];
            for (int i = 0; i < array.size(); i++) {
                BoardPiece p = array.get(i);
                System.out.println(p);
            }

            System.out.println("NEW: " + newX + "," + newY);
            array = pieceGrid[newX][newY];
            for (int i = 0; i < array.size(); i++) {
                BoardPiece p = array.get(i);
                System.out.println(p);
            }
        }
        setPos(newX, newY);
    }

    /**
     * Method for checking if a move-1-forward move is applicable.
     *
     * @param x-coordinate current x coordinate
     * @param y-coordinate current y coordinate
     * @param dir          current direction
     * @return whether moving one tile in a given direction is legal
     */
    private boolean isLegalMove(int x, int y, Direction dir) {
        //int xPosIn2dArray = MAP_WIDTH - 1 - x;
        BoardPiece currPiece;
        BoardPiece pieceInFront;
        if (isDead()) return false;
        for (int i = 0; i < pieceGrid[x][y].size(); i++) {
            boolean f = pieceGrid[x][y].isEmpty();
            currPiece = pieceGrid[x][y].get(i);

            switch (dir) {
                case EAST:
                    if (!isDeadMove(x + 1, y)) {
                        if (withinBoundaries(x + 1, y)) {
                            pieceInFront = pieceGrid[x + 1][y].get(i);
                            if (currPiece instanceof WallPiece || currPiece instanceof LaserSourcePiece) {
                                return ((WallPiece) currPiece).canLeave(dir);
                            }
                            if (pieceInFront instanceof WallPiece || pieceInFront instanceof LaserSourcePiece) {
                                return ((WallPiece) pieceInFront).canGo(dir);
                            }
                        }
                    }
                    break;
                case WEST:
                    if (!isDeadMove(x - 1, y)) {
                        if (withinBoundaries(x - 1, y)) {
                            pieceInFront = pieceGrid[x - 1][y].get(i);
                            if (currPiece instanceof WallPiece || currPiece instanceof LaserSourcePiece) {
                                return ((WallPiece) currPiece).canLeave(dir);
                            }
                            if (pieceInFront instanceof WallPiece || pieceInFront instanceof LaserSourcePiece) {
                                return ((WallPiece) pieceInFront).canGo(dir);
                            }
                        }
                    }
                    break;
                case NORTH:
                    if (!isDeadMove(x, y + 1)) {
                        if (withinBoundaries(x, y + 1)) {
                            pieceInFront = pieceGrid[x][y + 1].get(i);
                            if (currPiece instanceof WallPiece) {
                                return ((WallPiece) currPiece).canLeave(dir);
                            }
                            if (pieceInFront instanceof WallPiece) {
                                return ((WallPiece) pieceInFront).canGo(dir);
                            }
                        }
                    }
                    break;
                case SOUTH:
                    if (!isDeadMove(x, y - 1)) {
                        if (withinBoundaries(x, y - 1)) {
                            pieceInFront = pieceGrid[x][y - 1].get(i);
                            if (currPiece instanceof WallPiece) {
                                return ((WallPiece) currPiece).canLeave(dir);
                            }
                            if (pieceInFront instanceof WallPiece) {
                                return ((WallPiece) pieceInFront).canGo(dir);
                            }
                        }
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * Method for checking if a move is within the map boundaries
     * out of bounds is two tiles outside the map, since one tile is used for death
     *
     * @param x x-position after move
     * @param y y-position after move
     * @return whether the move is within map boundaries
     */
    private boolean withinBoundaries(int x, int y) {
        return x <= MAP_WIDTH + 1 && y <= MAP_HEIGHT + 1 && x >= -1 && y >= -1;
    }

    private boolean isDeadMove(int x, int y) {
        BoardPiece currPiece;
        if (x > MAP_WIDTH || x < 0 || y < 0 || y > MAP_WIDTH) return true;
        for (int i = 0; i < pieceGrid[x][y].size(); i++) {
            currPiece = pieceGrid[x][y].get(i);
            if (currPiece instanceof AbyssPiece) {
                return true;
            }
        }
        return false;
    }

    public boolean isDead() {
        return currentCell.equals(deadPlayerCell);
    }

    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }
}