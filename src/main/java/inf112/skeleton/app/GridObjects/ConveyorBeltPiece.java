package inf112.skeleton.app.GridObjects;

<<<<<<< HEAD
=======
import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

>>>>>>> 4c370910558ee481059077764f1c4ebaed24d2e1
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