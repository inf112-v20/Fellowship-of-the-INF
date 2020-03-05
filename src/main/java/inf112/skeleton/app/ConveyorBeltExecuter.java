package inf112.skeleton.app;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid_objects.AbyssPiece;
import inf112.skeleton.app.grid_objects.BoardPiece;
import inf112.skeleton.app.grid_objects.ConveyorBeltPiece;
import inf112.skeleton.app.grid_objects.ExpressBeltPiece;
import inf112.skeleton.app.player.Player;

public class ConveyorBeltExecuter {
    public ConveyorBeltExecuter(BoardPiece boardPiece, Player player){

        if(((ConveyorBeltPiece) boardPiece).isTurn()) {
            if (((ConveyorBeltPiece) boardPiece).isTurnRight()) {
                System.out.println("Standing on conveyorBeltPieceRightTurn");
                player.turnPlayerRight();
            }
            else {
                System.out.println("Standing on conveyorBeltPieceLeftTurn");
                player.turnPlayerLeft();
            }
        }

        if(boardPiece.getClass().equals(ExpressBeltPiece.class)) {
            System.out.println("Expressbelt direction: " + ((ExpressBeltPiece) boardPiece).getDir());
            player.tryToGo(((ExpressBeltPiece) boardPiece).getDir());
            player.tryToGo(((ExpressBeltPiece) boardPiece).getDir());
        }

        else if(boardPiece.getClass().equals(ConveyorBeltPiece.class)){
            System.out.println("Conveyorbelt direction: " + ((ConveyorBeltPiece) boardPiece).getDir());
            player.tryToGo(((ConveyorBeltPiece) boardPiece).getDir());

        }
    }
}
