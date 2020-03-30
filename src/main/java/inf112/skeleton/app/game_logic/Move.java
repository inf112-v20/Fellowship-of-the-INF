package inf112.skeleton.app.game_logic;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.PlayerPiece;
import inf112.skeleton.app.player.Player;

public class Move {

    private PlayerPiece playerPiece;
    private Position oldPos;
    private Position newPos;
    private Direction oldDir;
    private Direction newDir;

    public Move(PlayerPiece playerPiece, Position oldPos, Position newPos, Direction oldDir, Direction newDir) {
        this.playerPiece = playerPiece;
        this.oldPos = oldPos;
        this.newPos = newPos;
        this.oldDir = oldDir;
        this.newDir = newDir;
    }

    /**
     * Constructor for initiating a move, before the player has executed it.
     * Since the player hasn't done anything yet, oldDir = newDir, and oldPos = newPos
     *
     * @param player that is going to execute a move
     */
    public Move(Player player) {
        this.playerPiece = player.getPlayerPiece();
        this.oldPos = playerPiece.getPos();
        this.newPos = playerPiece.getPos();
        this.oldDir = playerPiece.getDir();
        this.newDir = playerPiece.getDir();
    }

    public Move(PlayerPiece playerPiece) {
        this.playerPiece = playerPiece;
        this.oldPos = playerPiece.getPos();
        this.newPos = playerPiece.getPos();
        this.oldDir = playerPiece.getDir();
        this.newDir = playerPiece.getDir();
    }

    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    public Position getOldPos() {
        return oldPos;
    }

    public Direction getOldDir() {
        return oldDir;
    }

    public Position getNewPos() {
        return newPos;
    }

    public Direction getNewDir() {
        return newDir;
    }

    @Override
    public String toString() {
        return "Move player " + playerPiece.getPlayerNumber() +
                " (" + oldPos +
                ", " + oldDir +
                ") to (" + newPos +
                ", " + newDir +
                ')';
    }

    /**
     * @return true if the move changes either position or rotation
     */
    public boolean isNotStandStill() {
        return !(oldPos.equals(newPos) && oldDir == newDir);
    }

    /**
     * updates the move object with the players new position and direction
     *
     */
    public void updateMove() {
        newPos = playerPiece.getPos();
        newDir = playerPiece.getDir();
    }

    /**
     * @return an ArrayList containing only this move
     */
    public MovesToExecuteSimultaneously toMovesList() {
        MovesToExecuteSimultaneously listWithSingleMove = new MovesToExecuteSimultaneously();
        listWithSingleMove.add(this);
        return listWithSingleMove;
    }
}
