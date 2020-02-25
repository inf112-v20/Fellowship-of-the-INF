package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.PieceGrid;
import inf112.skeleton.app.Grid.Position;
import inf112.skeleton.app.GridObjects.AbyssPiece;
import inf112.skeleton.app.GridObjects.BoardPiece;
import inf112.skeleton.app.GridObjects.WallPiece;

import java.util.ArrayList;

/**
 *
 */
public class Player {

    private Position pos; //position of player
    private TiledMapTileLayer.Cell currentCell; //Cell for the current state of the player
    private TiledMapTileLayer.Cell playerCell; //cell for normal player
    private TiledMapTileLayer.Cell deadPlayerCell; //cell for dead player looks
    private TiledMapTileLayer.Cell wonPlayerCell; //cell for player who has won looks
    private int MAP_WIDTH;
    private int MAP_HEIGHT;
    private ArrayList<BoardPiece>[][] pieceGrid;
    private Direction dir;

    public Player(int playerNumber, PieceGrid pieceGrid) {
        this.pieceGrid = pieceGrid.getGrid();
        pos = new Position(0,0);
        dir = Direction.NORTH;
        //place player at the bottom left corner
        pos.setX(0);
        pos.setY(0);
        MAP_WIDTH = pieceGrid.getWidth();
        MAP_HEIGHT = pieceGrid.getHeight();

        currentCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,0)));
        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,1)));
        wonPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,2)));

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

    public void handleInput() {
        Position pos = getPos();
        int newX = pos.getX();
        int newY = pos.getY();
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (isLegalMove(pos.getX(), pos.getY(), dir)) newY += 1;
            if (isDeadMove(pos.getX(), newY)) currentCell = deadPlayerCell;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (isLegalMove(pos.getX(), pos.getY(), dir)) newY -= 1;
            if (isDeadMove(pos.getX(), newY)) currentCell = deadPlayerCell;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (isLegalMove(pos.getX(), pos.getY(), dir)) newX -= 1;
            if (isDeadMove(newX, pos.getY())) currentCell = deadPlayerCell;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (isLegalMove(pos.getX(), pos.getY(), dir)) newX += 1;
            if (isDeadMove(newX, pos.getY())) currentCell = deadPlayerCell;
        }
        setPos(newX, newY);
    }

    /** Method for checking if a move-1-forward move is applicable.
     * @param x-coordinate   current x coordinate
     * @param y-coordinate   current y coordinate
     * @param dir            current direction
     * @return whether moving one tile in a given direction is legal
     */
    private boolean isLegalMove(int x, int y, Direction dir) {
        int xPosIn2dArray = MAP_WIDTH - 1 - x;
        BoardPiece currPiece;
        BoardPiece pieceInFront;
        for (int i = 0; i < pieceGrid[xPosIn2dArray][y].size(); i++) {
            currPiece = pieceGrid[xPosIn2dArray][y].get(i);

            switch (dir) {
                case EAST:
                    if (withinBoundaries(x + 1, y)) {
                        pieceInFront = pieceGrid[xPosIn2dArray + 1][y].get(i);
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                        if (pieceInFront instanceof WallPiece) {
                            return ((WallPiece) pieceInFront).canGo(dir);
                        }
                    }
                case WEST:
                    if (withinBoundaries(x - 1, y)) {
                        pieceInFront = pieceGrid[xPosIn2dArray - 1][y].get(i);
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                        if (pieceInFront instanceof WallPiece) {
                            return ((WallPiece) pieceInFront).canGo(dir);
                        }
                    }
                case NORTH:
                    if (withinBoundaries(x, y + 1)) {
                        pieceInFront = pieceGrid[xPosIn2dArray][y + 1].get(i);
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                        if (pieceInFront instanceof WallPiece) {
                            return ((WallPiece) pieceInFront).canGo(dir);
                        }
                    }
                case SOUTH:
                    if (withinBoundaries(x, y - 1)) {
                        pieceInFront = pieceGrid[xPosIn2dArray][y - 1].get(i);
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                        if (pieceInFront instanceof WallPiece) {
                            return ((WallPiece) pieceInFront).canGo(dir);
                        }
                    }
            }
        }
        return true;
    }

    /** Method for checking if a move is within the map boundaries
     * out of bounds is two tiles outside the map, since one tile is used for death
     * @param x x-position after move
     * @param y y-position after move
     * @return  whether the move is within map boundaries
     */
    private boolean withinBoundaries(int x, int y){
        return x <= MAP_WIDTH + 1 && y <= MAP_HEIGHT + 1 && x >= -1 && y >= -1;
    }

    private boolean isDeadMove(int x, int y){
        int xPosIn2dArray =  MAP_WIDTH-1 - x;
        BoardPiece currPiece;
        if (x > MAP_WIDTH || x < 0 || y < 0 || y > MAP_WIDTH) return true;
        for (int i=0; i<pieceGrid[xPosIn2dArray][y].size(); i++) {
            currPiece = pieceGrid[xPosIn2dArray][y].get(i);
            if (currPiece instanceof AbyssPiece) {
                return true;
            }
        }
        return false;
    }

}