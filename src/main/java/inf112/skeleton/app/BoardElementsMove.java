package inf112.skeleton.app;

import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;

public class BoardElementsMove {

    public BoardElementsMove(){ }

    public void moveExpressBelt(BoardPiece boardPiece, Player player){
        if(((ExpressBeltPiece) boardPiece).isTurn()) {
            if (((ExpressBeltPiece) boardPiece).isTurnRight()) {
                System.out.println("Standing on expressBeltPieceRightTurn");
                player.turnPlayerRight();
            }
            else {
                System.out.println("Standing on expressBeltPieceLeftTurn");
                player.turnPlayerLeft();
            }
        }
        System.out.println("Expressbelt direction: " + ((ExpressBeltPiece) boardPiece).getDir());
        player.tryToGo(((ExpressBeltPiece) boardPiece).getDir());
    }

    public void moveConveyorBelt(BoardPiece boardPiece, Player player){

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
        System.out.println("Conveyorbelt direction: " + ((ConveyorBeltPiece) boardPiece).getDir());
        player.tryToGo(((ConveyorBeltPiece) boardPiece).getDir());
    }

    public void rotateCog(BoardPiece boardPiece, Player player){
        if(((CogPiece) boardPiece).isRotateClockwise()){
            player.turnPlayerRight();
        }
        else{
            player.turnPlayerLeft();
        }
    }
}
