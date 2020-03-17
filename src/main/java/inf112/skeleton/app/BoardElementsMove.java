package inf112.skeleton.app;

import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;

public class BoardElementsMove {

    //TODO Implement conflict solution for when robots collide on conveyor belts, and implement solutions for merging turns

    /**
     * Moves a player standing on an expressBelt one tile in the direction of the expressBelt.
     * Will also rotate the player if the expressBelt is a turn.
     * @param boardPiece The BoardPiece that a player is currently standing on(should always be an ExpressBeltPiece!).
     * @param player The player that is currently standing on the BoardPiece.
     */
    public static void moveExpressBelt(BoardPiece boardPiece, Player player){
        if(((ExpressBeltPiece) boardPiece).isTurn() && player.isLatestMoveConveyorBelt() && player.latestMoveDirection() != ((ExpressBeltPiece) boardPiece).getDir()) {
            if(player.latestMoveDirection().getRightTurnDirection() == ((ExpressBeltPiece) boardPiece).getDir()) {
                player.turnPlayerRight();
            }
            else if(player.latestMoveDirection().getLeftTurnDirection() == ((ExpressBeltPiece) boardPiece).getDir()){
                player.turnPlayerLeft();
            }
        }
        player.tryToGo(((ExpressBeltPiece) boardPiece).getDir());
    }

    /**
     * Moves a player standing on an conveyorBelt one tile in the direction of the conveyorBelt.
     * Will also rotate the player if the conveyorBelt is a turn.
     * @param boardPiece The BoardPiece that a player is currently standing on(should always be a ConveyorBeltPiece!).
     * @param player The player that is currently standing on the BoardPiece.
     */
    public static void moveConveyorBelt(BoardPiece boardPiece, Player player){

        if(((ConveyorBeltPiece) boardPiece).isTurn() && player.isLatestMoveConveyorBelt()&& player.latestMoveDirection() != ((ConveyorBeltPiece) boardPiece).getDir()) {
            if(player.latestMoveDirection().getRightTurnDirection() == ((ConveyorBeltPiece) boardPiece).getDir()) {
                player.turnPlayerRight();
            }
            else if(player.latestMoveDirection().getLeftTurnDirection() == ((ConveyorBeltPiece) boardPiece).getDir()){
                player.turnPlayerLeft();
            }
        }
        player.tryToGo(((ConveyorBeltPiece) boardPiece).getDir());
    }

    /**
     * Rotates a player standing on a cog 90* in the direction of the cog.
     * @param boardPiece The BoardPiece that a player is currently standing on(should always be a CogPiece!).
     * @param player The player that is currently standing on the BoardPiece.
     */
    public static void rotateCog(BoardPiece boardPiece, Player player){
        if(((CogPiece) boardPiece).isRotateClockwise()){
            player.turnPlayerRight();
        }
        else{
            player.turnPlayerLeft();
        }
    }
}
