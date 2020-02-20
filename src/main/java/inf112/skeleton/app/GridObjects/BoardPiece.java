package inf112.skeleton.app.GridObjects;

/**
 * An abstract class that all objects on the board extend
 * Position (make class)
 * Direction (make enum class)
 * Tiled image (?? the visual representation of the object)
 */


public abstract class BoardPiece {

    private Position pos;
    private Direction dir;
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
