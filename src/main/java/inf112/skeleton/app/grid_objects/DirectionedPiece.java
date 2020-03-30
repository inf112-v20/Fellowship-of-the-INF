package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;

public abstract class DirectionedPiece extends BoardPiece {
    
    Direction dir;

    /**
     * A board piece with direction
     * @param pos position of piece on map
     * @param id tmx id
     * @param dir direction piece is facing
     */
    public DirectionedPiece(Position pos, int id, Direction dir) {
        super(pos, id);
        this.dir = dir;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction newDirection) {
        this.dir = newDirection;
    }

    public void turnPieceInOppositeDirection() {
        this.dir = dir.getOppositeDirection();
    }

    public void rotatePieceLeft() {
        this.dir = dir.getLeftTurnDirection();
    }

    public void rotatePieceRight() {
        this.dir = dir.getRightTurnDirection();
    }

}
