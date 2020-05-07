package inf112.skeleton.app.game_logic;


import java.util.ArrayList;

/**
 * A type of arrayList for moves that are to be executed simultaneously. These objects are used when events on the
 * board are shown, as some events happen simultaneously (fex when a robot pushes another, both moves simultaneously).
 */
public class MovesToExecuteSimultaneously extends ArrayList<Move> {
    /**
     * If the move contains  changes, add the move to the list
     *
     * @param move move to add
     * @return true if the move was added, else false
     */
    @Override
    public boolean add(Move move) {
        if (move.isNotStandStill()) return super.add(move);
        else return false;
    }
}
