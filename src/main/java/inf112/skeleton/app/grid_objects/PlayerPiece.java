package inf112.skeleton.app.grid_objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.game_logic.Move;
import inf112.skeleton.app.game_logic.MovesToExecuteSimultaneously;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.player.Player;
import inf112.skeleton.app.player.TextureMaker;

public class PlayerPiece extends DirectionedPiece {

    private Player player; //player who owns this piece
    private final int playerNumber;
    private TiledMapTileLayer.Cell currentCell; //Cell for the current state of the player
    private TiledMapTileLayer.Cell playerCell; //cell for normal player
    private TiledMapTileLayer.Cell deadPlayerCell; //cell for dead player looks

    public PlayerPiece(Position pos, int id, Direction dir, Player player) {
        super(pos, id, dir);
        this.player = player;
        this.playerNumber = player.getPlayerNumber();

        initiatePlayerPieceCells();
    }

    public void initiatePlayerPieceCells() {
        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 1)));
        currentCell = playerCell; //appearance starts as normal player
    }

    public TiledMapTileLayer.Cell getCurrentCell() {
        return currentCell;
    }

    public TiledMapTileLayer.Cell getPlayerCell() {
        return playerCell;
    }

    /**
     * Turns the player around.
     * @param moves list to update
     */
    public void turnAround(MovesToExecuteSimultaneously moves) {
            Move move = new Move(this);
            turnPieceInOppositeDirection();
            move.updateMove();
            moves.add(move);
    }

    /**
     * Turn the player to the left
     * @param moves list to update
     */
    public void turnLeft(MovesToExecuteSimultaneously moves) {
            Move move = new Move(this);
            rotatePieceLeft();
            move.updateMove();
            moves.add(move);
        }

    /**
     * Turns the player to the right
     * @param moves list to update
     */
    public void turnRight(MovesToExecuteSimultaneously moves) {
            Move move = new Move(this);
            rotatePieceRight();
            move.updateMove();
            moves.add(move);
    }

    /**
     * Turns the sprite of the robot in the new direction.
     * @param newDir the new direction the player should face
     */
    public void turnCellInDirection(Direction newDir) {
        switch (newDir) {
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

    public void showAlivePlayer() {
        currentCell = playerCell;
    }

    public Player getPlayer() {
        return player;
    }
}
