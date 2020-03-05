package inf112.skeleton.app;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid_objects.BoardPiece;
import inf112.skeleton.app.grid_objects.ConveyorBeltPiece;
import inf112.skeleton.app.grid_objects.ExpressBeltPiece;
import inf112.skeleton.app.player.Player;

public class ConveyerBeltExecuter {
    public ConveyerBeltExecuter(BoardPiece boardPiece, Player player){

        if(((ConveyorBeltPiece) boardPiece).isTurn()) {
            if (((ConveyorBeltPiece) boardPiece).isTurnRight()) {
                player.turnPlayerRight();
            }
            else {
                player.turnPlayerLeft();
            }
        }
        if(boardPiece instanceof ExpressBeltPiece) {
            player.tryToGo(((ExpressBeltPiece) boardPiece).getDir());
            player.tryToGo(((ExpressBeltPiece) boardPiece).getDir());
        }

        else {
            //playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), null);
            player.tryToGo(((ConveyorBeltPiece) boardPiece).getDir());
            //playerLayer.setCell(player.getPos().getX(), player.getPos().getY(), player.getPlayerCell());

        }
    }
}
