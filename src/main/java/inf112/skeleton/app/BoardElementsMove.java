package inf112.skeleton.app;

import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;


import static inf112.skeleton.app.grid.Direction.*;

public class BoardElementsMove {
    /**
     * Moves a player standing on an expressBelt one tile in the direction of the expressBelt.
     * Will also rotate the player if the expressBelt is a turn and the last movement of the player was by a expressbelt.
     * Will not move a player if there is another player standing in the way.
     * @param boardPiece The BoardPiece that a player is currently standing on(should always be a ExpressBeltPiece!).
     * @param player The player that is currently standing on the BoardPiece.
     * @param logicGrid the logicgrid of the game
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
     * Will also rotate the player if the conveyorBelt is a turn and the last movement of the player was by a conveyorbelt.
     * Will not move a player if there is another player standing in the way.
     * @param boardPiece The BoardPiece that a player is currently standing on(should always be a ConveyorBeltPiece!).
     * @param player The player that is currently standing on the BoardPiece.
     * @param logicGrid the logicgrid of the game
     */
    public static void moveConveyorBelt(BoardPiece boardPiece, Player player,LogicGrid logicGrid){
        Position newPos = logicGrid.getNewPosition(player.getPos(),((ConveyorBeltPiece) boardPiece).getDir());
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

    /**
     * Checks if there a player standing in front of the conveyorbelt
     * @param boardPiece the conveyorbelt that is going to be moving the player
     * @param player the player that is going to get moved
     * @param logicGrid the logicgrid of the game
     * @return true if there is player in front (in the direction of the conveyorbelt), and that player is standing
     * on a conveyorbelt that is not facing towards the first conveyorbelt, false otherwise
     */
    public static boolean isPlayerInFront(BoardPiece boardPiece, Player player, LogicGrid logicGrid){
        Position newPos = logicGrid.getNewPosition(player.getPos(),((ConveyorBeltPiece) boardPiece).getDir());
        if(!logicGrid.positionIsFree(newPos, 12) && !logicGrid.positionIsFree(newPos, 4)){
            BoardPiece piece = logicGrid.getPieceType(newPos, boardPiece.getClass());
            if(((ConveyorBeltPiece) piece).getDir().getOppositeDirection() != ((ConveyorBeltPiece) boardPiece).getDir()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a player is going to crash into another player from the movement from the conveyorbelt
     * @param boardPiece the conveyorbelt that the player is going to get moved by
     * @param player the player that is going to be moved
     * @param logicGrid the logicgrid of the game
     * @return true if player is going to end up on the same tile as another player, false otherwise
     */
    public static boolean isPlayerGoingToCrash(BoardPiece boardPiece, Player player, LogicGrid logicGrid){
        Position newPos = logicGrid.getNewPosition(player.getPos(),((ConveyorBeltPiece) boardPiece).getDir());
        if(!logicGrid.positionIsFree(newPos, 12) && !logicGrid.positionIsFree(newPos, 4)){
            BoardPiece piece = logicGrid.getPieceType(newPos, boardPiece.getClass());
            if(((ConveyorBeltPiece) piece).getDir().getOppositeDirection() == ((ConveyorBeltPiece) boardPiece).getDir()){ return true;
            }
        }
        for (int i = 0; i <4 ; i++) {
            Position orthoPos;
            if (i == 0) { orthoPos = logicGrid.getNewPosition(newPos, NORTH); }
            else if (i == 1) { orthoPos = logicGrid.getNewPosition(newPos, EAST); }
            else if (i == 2) { orthoPos = logicGrid.getNewPosition(newPos, SOUTH); }
            else{ orthoPos = logicGrid.getNewPosition(newPos, WEST); }
            if (!logicGrid.positionIsFree(orthoPos, 12) && !logicGrid.positionIsFree(orthoPos, 4)) {
                if(orthoPos.getX() == player.getPos().getX() && orthoPos.getY() == player.getPos().getY()){continue;}
                BoardPiece piece = logicGrid.getPieceType(orthoPos, boardPiece.getClass());
                Position orthoPosDir = logicGrid.getNewPosition(orthoPos, ((ConveyorBeltPiece) piece).getDir());
                if(orthoPosDir.getX() == newPos.getX() && orthoPosDir.getY() == newPos.getY()){
                    return true;
                }
            }
        }
        return false;
    }
}
