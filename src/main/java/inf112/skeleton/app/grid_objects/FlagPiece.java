package inf112.skeleton.app.grid_objects;

import inf112.skeleton.app.grid.Position;

public class FlagPiece extends BoardPiece {
    private int flagNumber;

    public FlagPiece(Position pos, int id, int flagNumber) {
        super(pos, id);
        this.flagNumber = flagNumber;
    }

    public int getFlagNumber() {
        return flagNumber;
    }

    public void setFlagNumber(int flagNumber) {
        this.flagNumber = flagNumber;
    }
}
