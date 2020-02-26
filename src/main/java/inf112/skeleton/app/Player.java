package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.Deck.GameDeck;
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.PieceGrid;
import inf112.skeleton.app.Grid.Position;
import inf112.skeleton.app.GridObjects.AbyssPiece;
import inf112.skeleton.app.GridObjects.BoardPiece;
import inf112.skeleton.app.GridObjects.LaserSourcePiece;
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

    public Player(int playerNumber, GameLogic game) {
        // TODO Refactor getGrid() (lol)
        this.pieceGrid = game.getGrid().getGrid();
        pos = new Position(0,0);
        dir = Direction.NORTH;
        //place player at the bottom left corner
        pos.setX(0);
        pos.setY(0);
        MAP_WIDTH = game.getGrid().getWidth();
        MAP_HEIGHT = game.getGrid().getHeight();

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
            this.dir=Direction.NORTH;
            if (isLegalMove(pos.getX(), pos.getY(), dir)) newY += 1;
            if (isDeadMove(pos.getX(), newY)) currentCell = deadPlayerCell;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            this.dir=Direction.SOUTH;
            if (isLegalMove(pos.getX(), pos.getY(), dir)) newY -= 1;
            if (isDeadMove(pos.getX(), newY)) currentCell = deadPlayerCell;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            this.dir=Direction.WEST;
            if (isLegalMove(pos.getX(), pos.getY(), dir)) newX -= 1;
            if (isDeadMove(newX, pos.getY())) currentCell = deadPlayerCell;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            this.dir=Direction.EAST;
            if (isLegalMove(pos.getX(), pos.getY(), dir)) newX += 1;
            if (isDeadMove(newX, pos.getY())) currentCell = deadPlayerCell;
        }
        /*if (!isDeadMove(pos.getX(), pos.getY()) && (newY != pos.getY() || newX != pos.getX())) {
            System.out.println(newX + "," + newY);
            ArrayList<BoardPiece> array = pieceGrid[newX][newY];
            for (int i = 0; i < array.size(); i++) {
                BoardPiece p = array.get(i);
                System.out.println(p);
            }
        }*/
        setPos(newX, newY);
    }

    /** Method for checking if a move-1-forward move is applicable.
     * @param x-coordinate   current x coordinate
     * @param y-coordinate   current y coordinate
     * @param dir            current direction
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
                    if (isDeadMove(x + 1, y)) {
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                    } else {
                        pieceInFront = pieceGrid[x + 1][y].get(i);
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                        if (pieceInFront instanceof WallPiece) {
                            return ((WallPiece) pieceInFront).canGo(dir);
                        }
                    }
                    break;
                case WEST:
                    if (isDeadMove(x - 1, y)) {
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                    } else {
                        pieceInFront = pieceGrid[x - 1][y].get(i);
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                        if (pieceInFront instanceof WallPiece) {
                            return ((WallPiece) pieceInFront).canGo(dir);
                        }
                    }
                    break;
                case NORTH:
                    if (isDeadMove(x, y + 1)) {
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                    } else {
                        pieceInFront = pieceGrid[x][y + 1].get(i);
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                        if (pieceInFront instanceof WallPiece) {
                            return ((WallPiece) pieceInFront).canGo(dir);
                        }
                    }
                    break;
                case SOUTH:
                    if (isDeadMove(x, y - 1)) {
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                    } else {
                        pieceInFront = pieceGrid[x][y - 1].get(i);
                        if (currPiece instanceof WallPiece) {
                            return ((WallPiece) currPiece).canLeave(dir);
                        }
                        if (pieceInFront instanceof WallPiece) {
                            return ((WallPiece) pieceInFront).canGo(dir);
                        }
                    }

                    break;
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

    /** Method for checking if a move results in death
     * @param x x-position after move
     * @param y y-position after move
     * @return  whether the move results in death
     */
    private boolean isDeadMove(int x, int y) {
        BoardPiece currPiece;
        if (x > MAP_WIDTH-1 || x < 0 || y < 0 || y > MAP_WIDTH-1) {
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

    public boolean isDead(){
        return currentCell.equals(deadPlayerCell);
    }
}