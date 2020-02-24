package inf112.skeleton.app.GridObjects;

import inf112.skeleton.app.Grid.Position;

/**
 * Class representing an abstract board piece with position
 */
public abstract class BoardPiece {

    private Position pos;
    //id number is retrieved from RoborallyBoard.tmx
    private int id;

    public BoardPiece(Position pos, int id) {
        this.pos = pos;
        this.id = id;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
