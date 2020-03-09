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
}
