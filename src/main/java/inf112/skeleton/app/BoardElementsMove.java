package inf112.skeleton.app;

import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;


import static inf112.skeleton.app.grid.Direction.*;

public class BoardElementsMove {

    /**
     * Moves a player standing on an expressBelt one tile in the direction of the expressBelt.
     * Will also rotate the player if the expressBelt is a turn.
     * @param boardPiece The BoardPiece that a player is currently standing on(should always be an ExpressBeltPiece!).
     * @param player The player that is currently standing on the BoardPiece.
     */
    public static void moveExpressBelt(BoardPiece boardPiece, Player player, LogicGrid logicGrid){
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
    public static void moveConveyorBelt(BoardPiece boardPiece, Player player,LogicGrid logicGrid){
        Position newPos = player.getNewPosition(player.getPos(),((ConveyorBeltPiece) boardPiece).getDir());
        if(!logicGrid.positionIsFree(newPos, 12)){
            return;
        }
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

    public static boolean isPlayerInFront(BoardPiece boardPiece, Player player, LogicGrid logicGrid){
        Position newPos = player.getNewPosition(player.getPos(),((ConveyorBeltPiece) boardPiece).getDir());
        if(!logicGrid.positionIsFree(newPos, 12) && !logicGrid.positionIsFree(newPos, 4)){
            BoardPiece piece = logicGrid.getPieceType(newPos, boardPiece.getClass());
            if(((ConveyorBeltPiece) piece).getDir().getOppositeDirection() != ((ConveyorBeltPiece) boardPiece).getDir()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPlayerGoingToCrash(BoardPiece boardPiece, Player player, LogicGrid logicGrid){
        Position newPos = player.getNewPosition(player.getPos(),((ConveyorBeltPiece) boardPiece).getDir());
        if(!logicGrid.positionIsFree(newPos, 12) && !logicGrid.positionIsFree(newPos, 4)){
            BoardPiece piece = logicGrid.getPieceType(newPos, boardPiece.getClass());
            if(((ConveyorBeltPiece) piece).getDir().getOppositeDirection() == ((ConveyorBeltPiece) boardPiece).getDir()){ return true;
            }
        }
        for (int i = 0; i <4 ; i++) {
            Position orthoPos;
            if (i == 0) { orthoPos = player.getNewPosition(newPos, NORTH); }
            else if (i == 1) { orthoPos = player.getNewPosition(newPos, EAST); }
            else if (i == 2) { orthoPos = player.getNewPosition(newPos, SOUTH); }
            else{ orthoPos = player.getNewPosition(newPos, WEST); }
            if (!logicGrid.positionIsFree(orthoPos, 12) && !logicGrid.positionIsFree(orthoPos, 4)) {
                if(orthoPos.getX() == player.getPos().getX() && orthoPos.getY() == player.getPos().getY()){continue;}
                BoardPiece piece = logicGrid.getPieceType(orthoPos, boardPiece.getClass());
                Position orthoPosDir = player.getNewPosition(orthoPos, ((ConveyorBeltPiece) piece).getDir());
                if(orthoPosDir.getX() == newPos.getX() && orthoPosDir.getY() == newPos.getY()){
                    return true;
                }
            }
        }
        return false;
    }

}
