package inf112.skeleton.app;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.player.Player;

public class Move {

    private Player player;
    private Position oldPos;
    private Position newPos;
    private Direction oldDir;
    private Direction newDir;

    public Move(Player player, Position oldPos, Position newPos, Direction oldDir, Direction newDir) {
        this.player = player;
        this.oldPos = oldPos;
        this.newPos = newPos;
        this.oldDir = oldDir;
        this.newDir = newDir;
    }

    /**
     * Constructor for initiating a move, before the player has executed it.
     * Since the player hasn't done anything yet, oldDir = newDir, and oldPos = newPos
     * @param player that is going to execute a move
     */
    public Move(Player player) {
        this.player = player;
        this.oldPos = player.getPos();
        this.newPos = player.getPos();
        this.oldDir = player.getPlayerPiece().getDir();
        this.newDir = player.getPlayerPiece().getDir();
    }

    public Player getPlayer() {
        return player;
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
        return "Move " +  player +
                ",  ("+ oldPos +
                ", " + oldDir +
                ") to (" + newPos +
                ", "+ newDir +
                ')';
    }

    /**
     *
     * @return true if the move changes either position or rotation
     */
    public boolean isNotStandStill() {
        return !(oldPos.equals(newPos) && oldDir == newDir);
    }

    /**
     * updates the move object with the players new position and direction
     * @param playerPostMove the player after it has executed a move
     */
    public void updateMove(Player playerPostMove) {
        this.player = playerPostMove;
        newPos = playerPostMove.getPlayerPiece().getPos();
        newDir = playerPostMove.getPlayerPiece().getDir();
    }
}
