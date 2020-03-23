package inf112.skeleton.app;

import java.util.ArrayList;

/**
 * A type of arrayList for moves that are to be executed simultaneously
 */
public class MovesToExecuteSimultaneously extends ArrayList<Move> {
    /**
     * If the move contains  changes, add the move to the list
     * @param move
     * @return true if the move was added, else false
     */
    @Override
    public boolean add(Move move) {
        if (move.isNotStandStill()) return super.add(move);
        else return false;
    }
}
