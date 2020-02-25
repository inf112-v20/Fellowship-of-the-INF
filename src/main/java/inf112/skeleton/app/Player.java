package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.PieceGrid;
import inf112.skeleton.app.GridObjects.AbyssPiece;
import inf112.skeleton.app.GridObjects.BoardPiece;
import inf112.skeleton.app.GridObjects.WallPiece;

import java.util.ArrayList;

/**
 *
 */
public class Player {

    private Vector2 pos; //position of player
    private TiledMapTileLayer.Cell playerCell; //cell for normal player
    private TiledMapTileLayer.Cell deadPlayerCell; //cell for dead player looks
    private TiledMapTileLayer.Cell wonPlayerCell; //cell for player who has won looks
    private int MAP_WIDTH;
    private int MAP_HEIGHT;
    private ArrayList<BoardPiece>[][] pieceGrid;

    public Player(int playerNumber, PieceGrid pieceGrid) {
        this.pieceGrid = pieceGrid.getGrid();
        pos = new Vector2();
        //place player at the bottom left corner
        pos.x = 0;
        pos.y = 0;
        MAP_WIDTH = pieceGrid.getWidth();
        MAP_HEIGHT = pieceGrid.getHeight();

        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,1)));
        wonPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber,2)));
    }

    /**
     * Getter for position
     * @return position of player
     */
    public Vector2 getPos() {
        return pos;
    }

    /**
     * Setter for position of player
     * @param x new x position
     * @param y new y position
     */
    public void setPos(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

    /**
     * Getter for player
     * @return player
     */
    public TiledMapTileLayer.Cell getPlayerCell() {
        return playerCell;
    }

    public void handleInput() {
        Vector2 pos = getPos();
        float newX = pos.x;
        float newY = pos.y;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (isLegalMove(pos.x, pos.y, dir)) newY += 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (isLegalMove(pos.x, pos.y, dir)) newY -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (isLegalMove(pos.x, pos.y, dir)) newX -= 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (isLegalMove(pos.x, pos.y, dir)) newX += 1;
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
    }

    /** Method for checking if a move is within the map boundaries
     * out of bounds is two tiles outside the map, since one tile is used for death
     * @param x x-position after move
     * @param y y-position after move
     * @return  whether the move is within map boundaries
     */
    private boolean withinBoundaries(int x, int y){
        if (x > MAP_WIDTH+1 || y > MAP_HEIGHT+1 || x < -1 || y < -1)
            return false;
        return true;
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