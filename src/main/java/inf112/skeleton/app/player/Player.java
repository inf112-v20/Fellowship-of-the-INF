package inf112.skeleton.app.player;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Map;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.gridObjects.AbyssPiece;
import inf112.skeleton.app.gridObjects.BoardPiece;
import inf112.skeleton.app.gridObjects.PlayerPiece;
import inf112.skeleton.app.gridObjects.WallPiece;

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
    private Map map;
    private ArrayList<BoardPiece>[][] pieceGrid;
    private PlayerPiece playerPiece;
    private Game game;

    public Player(int playerNumber, Game game) {
        this.map = game.getMap();
        this.pieceGrid = map.getGrid();
        this.game = game;
        this.playerPiece = new PlayerPiece(new Position(0, 0), 200, Direction.NORTH);
        pos = new Position(0, 0);
        //place player at the bottom left corner
        pos.setX(0);
        pos.setY(0);
        MAP_WIDTH = game.getMap().getWidth();
        MAP_HEIGHT = game.getMap().getHeight();

        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 1)));
        wonPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 2)));
        currentCell = playerCell; //appearance starts as normal player
    }

    /**
     * Getter for position
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
     * Getter for player
     * @return player
     */
    public TiledMapTileLayer.Cell getPlayerCell() {
        return currentCell;
    }

    /**
     * Tries to move the player in a new direction
     *
     * @param newDirection new direction to move the player
     */
    public void tryToGo(Direction newDirection) {
        int newX = pos.getX();
        int newY = pos.getY();
        playerPiece.setDir(newDirection);
        switch (newDirection) {
            case NORTH:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newY += 1;
                if (isDeadMove(pos.getX(), newY)) currentCell = deadPlayerCell;
                break;
            case SOUTH:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newY -= 1;
                if (isDeadMove(pos.getX(), newY)) currentCell = deadPlayerCell;
                break;
            case WEST:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newX -= 1;
                if (isDeadMove(newX, pos.getY())) currentCell = deadPlayerCell;
                break;
            case EAST:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newX += 1;
                if (isDeadMove(newX, pos.getY())) currentCell = deadPlayerCell;
                break;
        }

        //if position has changed and player isn't dead, update logic grid
        if ((newY != pos.getY() || newX != pos.getX()) && !isDead()) {
            map.movePlayerToNewPosition(pos, new Position(newX, newY));
        }

        /* UNCOMMENT TO SEE PRINTOUT OF PIECES IN CELL
        if (newY != pos.getY() || newX != pos.getX() && !isDead()) {
            System.out.println("OLD: " + pos.getX() + "," + pos.getY());

            ArrayList<BoardPiece> array = pieceGrid[pos.getX()][pos.getY()];
            for (int i = 0; i < array.size(); i++) {
                BoardPiece p = array.get(i);l
                System.out.println(p);
            }

            System.out.println("NEW: " + newX + "," + newY);
            array = pieceGrid[newX][newY];
            for (int i = 0; i < array.size(); i++) {
                BoardPiece p = array.get(i);
                System.out.println(p);
            }
        }*/
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
                    if (!isLegalMoveInDirection(x+1, y, currPiece, dir, i)) {
                        return false;
                    }
                    break;
                case WEST:
                    if (!isLegalMoveInDirection(x-1, y, currPiece, dir, i)) {
                        return false;
                    }
                    break;
                case NORTH:
                    if (!isLegalMoveInDirection(x, y+1, currPiece, dir, i)) {
                        return false;
                    }
                    break;
                case SOUTH:
                    if (!isLegalMoveInDirection(x, y-1, currPiece, dir, i)) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public boolean isLegalMoveInDirection(int newX, int newY, BoardPiece currentPiece, Direction dir, int layerLevel) {
        if (isDeadMove(newX, newY)) {
            if (currentPiece instanceof WallPiece) {
                return ((WallPiece) currentPiece).canLeave(dir);
            }
        } else {
            BoardPiece pieceInFront = pieceGrid[newX][newY].get(layerLevel);
            if (currentPiece instanceof WallPiece) {
                return ((WallPiece) currentPiece).canLeave(dir);
            }
            if (pieceInFront instanceof WallPiece) {
                return ((WallPiece) pieceInFront).canGo(dir);
            }
        }
        return true;
    }

    /**
     * Method for checking if a move results in death
     *
     * @param x x-position after move
     * @param y y-position after move
     * @return whether the move results in death
     */
    private boolean isDeadMove(int x, int y) {
        BoardPiece currPiece;
        if (x > MAP_WIDTH - 1 || x < 0 || y < 0 || y > MAP_WIDTH - 1) {
            return true;
        } else {
            for (int i = 0; i < pieceGrid[x][y].size(); i++) {
                currPiece = pieceGrid[x][y].get(i);
                if (currPiece instanceof AbyssPiece) {
                    return true;
                }
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


    public void turnPlayerAround() {
        playerPiece.turnPieceInOppositeDirection();
    }

    public void turnPlayerLeft() {
        playerPiece.rotatePieceLeft();
    }

    public void turnPlayerRight() {
        playerPiece.rotatePieceRight();
    }
}