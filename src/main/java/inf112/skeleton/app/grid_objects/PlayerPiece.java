package inf112.skeleton.app.grid_objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.player.TextureMaker;

public class PlayerPiece extends DirectionedPiece {

    private int playerNumber;
    private TiledMapTileLayer.Cell currentCell; //Cell for the current state of the player
    private TiledMapTileLayer.Cell playerCell; //cell for normal player
    private TiledMapTileLayer.Cell deadPlayerCell; //cell for dead player looks
    private TiledMapTileLayer.Cell wonPlayerCell; //cell for player who has won looks

    public PlayerPiece(Position pos, int id, Direction dir, int playerNumber) {
        super(pos, id, dir);
        this.playerNumber = playerNumber;

        initiatePlayerPieceCells();
    }

    public void initiatePlayerPieceCells() {
        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 1)));
        wonPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 2)));
        currentCell = playerCell; //appearance starts as normal player
    }

    public TiledMapTileLayer.Cell getCurrentCell() {
        return currentCell;
    }

    public TiledMapTileLayer.Cell getPlayerCell() {
        return playerCell;
    }

    /**
     * A cells rotation is an integer between 0 and 3, where 0 = North, 1 = West, 2 = South, 3 = East.
     */
    public void turnAround() {
      turnPieceInOppositeDirection();
        turnCellInDirection(dir);
      /*int newDir = currentCell.getRotation() + 2;
      if (newDir > 3){ newDir -= 4;}
      currentCell.setRotation(newDir);*/
    }

    public void turnLeft() {
        rotatePieceLeft();
        turnCellInDirection(dir);
       /* int newDir = currentCell.getRotation() + 1;
        if (newDir > 3){ newDir -= 4;}
        currentCell.setRotation(newDir);*/
    }

    public void turnRight() {
        rotatePieceRight();
        turnCellInDirection(dir);
        /*
        int newDir = currentCell.getRotation() + 3;
        if (newDir > 3){ newDir -= 4;}
        currentCell.setRotation(newDir);*/
    }

    public void turnCellInDirection(Direction newDir) {
        switch (newDir){
                case NORTH:
                    currentCell.setRotation(0);
                    break;
                case SOUTH:
                    currentCell.setRotation(2);
                    break;
                case WEST:
                    currentCell.setRotation(1);
                    break;
                case EAST:
                    currentCell.setRotation(3);
                    break;
        }

    }

    public void showDeadPlayer() {
        currentCell = deadPlayerCell;
    }

    @Override
    public String toString() {
        return "PlayerPiece{" +
                "playerNumber=" + playerNumber +
                '}';
    }
}
