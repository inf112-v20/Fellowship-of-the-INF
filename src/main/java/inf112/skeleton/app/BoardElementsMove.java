package inf112.skeleton.app;

import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;

public class BoardElementsMove {

    //TODO Implement conflict solution for when robots collide on conveyor belts
    public BoardElementsMove(){ }

    public void moveExpressBelt(BoardPiece boardPiece, Player player){
        if(((ExpressBeltPiece) boardPiece).isTurn()) {
            if (((ExpressBeltPiece) boardPiece).isTurnRight()) {
                player.turnPlayerRight();
            }
            else {
                player.turnPlayerLeft();
            }
        }
        player.tryToGo(((ExpressBeltPiece) boardPiece).getDir());
    }

    public void moveConveyorBelt(BoardPiece boardPiece, Player player){

        if(((ConveyorBeltPiece) boardPiece).isTurn()) {
            if (((ConveyorBeltPiece) boardPiece).isTurnRight()) {
                player.turnPlayerRight();
            }
            else {
                player.turnPlayerLeft();
            }
        }
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
