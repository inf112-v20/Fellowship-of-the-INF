package inf112.skeleton.app.gridObjects;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;

public class ConveyorBeltPiece extends DirectionedPiece {

    private boolean turn; //true if converyBeltPiece is a corner
    private boolean turnRight; //false is it's a left turn

    public ConveyorBeltPiece(Position pos, int id, Direction dir, boolean turn, boolean turnRight) {
        super(pos, id, dir);
        this.turn = turn;
        this.turnRight = turnRight;
    }

    public boolean isTurn() {
        return turn;
    }

    public boolean isTurnRight() {
        return turnRight;
    }
}