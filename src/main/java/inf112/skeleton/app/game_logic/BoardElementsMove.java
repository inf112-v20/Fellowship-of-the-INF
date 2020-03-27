package inf112.skeleton.app.game_logic;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;


import static inf112.skeleton.app.grid.Direction.*;

public class BoardElementsMove {

    /**
     * Rotates a player standing on a cog 90* in the direction of the cog.
     * @param player The player that is currently standing on the BoardPiece.
     */
    public static void rotateCog(Player player){
        BoardPiece boardPiece = player.getCurrentBoardPiece();
        if(((CogPiece) boardPiece).isRotateClockwise()){
            player.turnPlayerRight();
        }
        else{
            player.turnPlayerLeft();
        }
}

    /**
     * Moves a player standing on an conveyorBelt one tile in the direction of the conveyorBelt.
     * Creates a recursive call if there is a robot standing directly in front of the first robot
     * that can be moved away (i.e. that robot is also standing on conveyorbelt facing away from the first robot).
     * Will also rotate the player if the conveyorBelt is a turn and the last movement of the player was by a conveyorbelt.
     * Will not move a player if there is another player standing in the way that cant be moved away.
     * @param player The player that is currently standing on the BoardPiece.
     * @param game the current game
     * @param onlyExpressBelt true if only expressbelt are moving at this point in the phase
     */
    public static void moveConveyorBelt(Player player, Game game, boolean onlyExpressBelt, MovesToExecuteSimultaneously moves){
        if(player == null || player.getCurrentBoardPiece() == null){System.out.println("Error: couldn't move player on conveyorbelt"); return;}
        BoardPiece boardPiece = player.getCurrentBoardPiece();
        LogicGrid logicGrid = game.getLogicGrid();
        ConveyorBeltPiece conveyorBeltPiece = (ConveyorBeltPiece) logicGrid.getPieceType(player.getPos(), boardPiece.getClass());
        Direction conveyorBeltDirection = conveyorBeltPiece.getDir();
        Position newPos = player.getPos().getPositionIn(conveyorBeltDirection);
        if(isPlayerInFront(player, game, onlyExpressBelt)) {
            moveConveyorBelt(game.getPlayerAt(newPos), game, onlyExpressBelt, moves);
            game.performMoves(moves);
        }
        if(isPlayerGoingToCrash(player, game, onlyExpressBelt)){ return; }
        if((conveyorBeltPiece.isTurn() && player.isLatestMoveConveyorBelt()
                && player.latestMoveDirection() != conveyorBeltDirection)) {
            if(player.latestMoveDirection().getRightTurnDirection() == conveyorBeltDirection) {
                player.turnPlayerRight();
            }
            else if(player.latestMoveDirection().getLeftTurnDirection() == conveyorBeltDirection){
                player.turnPlayerLeft();
            }
        }
        player.tryToGo(conveyorBeltDirection, moves);
        player.setConveyorBeltMove(true);
        player.setHasBeenMovedThisPhase(true);
    }

    /**
     * Checks if there a player standing in front of the conveyorbelt
     * @param player the player that is going to get moved
     * @param game the current game
     * @param onlyExpressBelt true if only expressbelt are moving at this point in the phase
     * @return true if there is player in front (in the direction of the conveyorbelt), and that player is standing
     * on a conveyorbelt that is not facing towards the first conveyorbelt, false otherwise
     */
    private static boolean isPlayerInFront(Player player, Game game, boolean onlyExpressBelt){
        BoardPiece boardPiece = player.getCurrentBoardPiece();
        LogicGrid logicGrid = game.getLogicGrid();
        Position newPos = player.getPos().getPositionIn(((ConveyorBeltPiece) boardPiece).getDir());
        if(!logicGrid.isInBounds(newPos)){return false;}
        //12 = playerindex, 4 = conveyorbeltindex, 5 = expressbeltindex
        if(!logicGrid.positionIsFree(newPos, 12) &&
                (!logicGrid.positionIsFree(newPos, 4)||!logicGrid.positionIsFree(newPos, 5))){
            ConveyorBeltPiece piece = logicGrid.getPieceType(newPos, ConveyorBeltPiece.class);
            if(!logicGrid.positionIsFree(newPos, 5)){
                piece = logicGrid.getPieceType(newPos, ExpressBeltPiece.class);
            }
            if(onlyExpressBelt && boardPiece instanceof  ExpressBeltPiece && !(piece instanceof ExpressBeltPiece)){
                return false;
            }

            return piece.getDir().getOppositeDirection() != ((ConveyorBeltPiece) boardPiece).getDir();
        }
        return false;
    }

    /**
     * Checks if a player is going to crash into another player from the movement of the conveyorbelt
     * @param player the player that is going to be moved
     * @param game the current game
     * @param onlyExpressBelt true if only expressbelt are moving at this point in the phase
     * @return true if player is going to end up on the same tile as another player, false otherwise
     */
    private static boolean isPlayerGoingToCrash(Player player, Game game, boolean onlyExpressBelt){
        BoardPiece boardPiece = player.getCurrentBoardPiece();
        LogicGrid logicGrid = game.getLogicGrid();
        Position newPos = player.getPos().getPositionIn(((ConveyorBeltPiece) boardPiece).getDir());
        if(!logicGrid.isInBounds(newPos)){return false;}
        if(!logicGrid.positionIsFree(newPos, 12)){ return true; }
        for (int i = 0; i <4 ; i++) {
            Position orthoPos;
            if (i == 0) { orthoPos = newPos.getPositionIn(NORTH); }
            else if (i == 1) { orthoPos = newPos.getPositionIn(EAST); }
            else if (i == 2) { orthoPos = newPos.getPositionIn(SOUTH); }
            else{ orthoPos = newPos.getPositionIn(WEST); }
            if(!logicGrid.isInBounds(orthoPos) || orthoPos.equals(player.getPos())){continue;}
            if (!logicGrid.positionIsFree(orthoPos, 12) &&
                    (!logicGrid.positionIsFree(orthoPos, 4)||!logicGrid.positionIsFree(orthoPos, 5))) {
                if (!game.getPlayerAt(orthoPos).hasBeenMovedThisPhase()) {
                    if(onlyExpressBelt && boardPiece instanceof ExpressBeltPiece && !logicGrid.positionIsFree(orthoPos, 4)){
                        return false;
                    }
                    ConveyorBeltPiece piece = logicGrid.getPieceType(orthoPos, ConveyorBeltPiece.class);
                    if(!logicGrid.positionIsFree(orthoPos, 5)){
                        piece = logicGrid.getPieceType(orthoPos, ExpressBeltPiece.class);
                    }
                    Position orthoPosDir = orthoPos.getPositionIn(piece.getDir());
                    if (orthoPosDir.equals(newPos)) { return true;}
                }
            }
        }
        return false;
    }
}
