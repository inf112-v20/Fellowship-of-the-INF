package inf112.skeleton.app.game_logic;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;

/*
Class for methods that handles board elements that moves or rotates players such as cogs, conveyorbelts etc.
These got quite big and complicated so we moved them from the phase class to here and made the methods static so we
don't have to create an instance of the class.
 */

public class BoardElementsMove {

    /**
     * Rotates a player standing on a cog 90* in the direction of the cog.
     *
     * @param player The player that is currently standing on the CogPiece.
     * @param moves list of moves to update with the new rotation move from the cog.
     * @param cogPiece the CogPiece the player is standing on.
     */
    public static void rotateCog(Player player, MovesToExecuteSimultaneously moves, CogPiece cogPiece) {
        if (cogPiece.isRotateClockwise()) {
            player.getPlayerPiece().turnRight(moves);
        } else {
            player.getPlayerPiece().turnLeft(moves);
        }
    }

    /**
     * Moves a player standing on an conveyorBelt one tile in the direction of the conveyorBelt.
     * Creates a recursive call if there is a robot standing directly in front of the first robot
     * that can be moved away (i.e. that robot is also standing on conveyorbelt facing away from the first robot).
     * Will also rotate the player if the conveyorBelt is a turn and the last movement of the player was by a conveyorbelt.
     * Will not move a player if there is another player standing in the way that can't be moved away.
     *
     * @param player          The player that is currently standing on the BoardPiece.
     * @param game            the current game
     * @param onlyExpressBelt true if only expressbelt are moving at this point in the phase
     * @param moves           the list of moves to update with the new move from the conveyorbelt
     */
    public static void moveConveyorBelt(Player player, Game game, boolean onlyExpressBelt, MovesToExecuteSimultaneously moves) {

        LogicGrid logicGrid = game.getLogicGrid();
        ConveyorBeltPiece conveyorBeltPiece = logicGrid.getPieceType(player.getPos(),  ConveyorBeltPiece.class);
        if(!logicGrid.positionIsFree(player.getPos(), logicGrid.EXPRESS_BELT_LAYER_INDEX)){
            conveyorBeltPiece = logicGrid.getPieceType(player.getPos(), ExpressBeltPiece.class);
        }
        Direction conveyorBeltDirection = conveyorBeltPiece.getDir();
        Position newPos = player.getPos().getPositionIn(conveyorBeltDirection);
        if (isPlayerInFront(player, game, onlyExpressBelt, conveyorBeltPiece)) {
            moveConveyorBelt(game.getPlayerAt(newPos), game, onlyExpressBelt, moves);
            game.performMoves(moves);
        }
        if (isPlayerGoingToCrash(player, game, onlyExpressBelt)) {
            return;
        }
        if ((conveyorBeltPiece.isTurn() && player.isLatestMoveConveyorBelt()
                && player.latestMoveDirection() != conveyorBeltDirection)) {
            if (player.latestMoveDirection().getRightTurnDirection() == conveyorBeltDirection) {
                player.getPlayerPiece().turnRight(moves);
            } else if (player.latestMoveDirection().getLeftTurnDirection() == conveyorBeltDirection) {
                player.getPlayerPiece().turnLeft(moves);
            }
        }
        player.tryToGo(conveyorBeltDirection, moves);
        player.setConveyorBeltMove(true);
        player.setHasBeenMovedThisPhase(true);
    }

    /**
     * Checks if there a player standing in front of the conveyorbelt
     *
     * @param player            the player that is going to get moved
     * @param game              the current game
     * @param onlyExpressBelt   true if only expressbelt are moving at this point in the phase
     * @param conveyorBeltPiece the ConveyorBeltPiece that the player is standing on
     * @return true if there is player in front (in the direction of the conveyorbelt), and that player is standing
     * on a conveyorbelt that is not facing towards the first conveyorbelt, false otherwise.
     */
    private static boolean isPlayerInFront(Player player, Game game, boolean onlyExpressBelt, ConveyorBeltPiece conveyorBeltPiece) {
        LogicGrid logicGrid = game.getLogicGrid();
        Position newPos = player.getPos().getPositionIn(conveyorBeltPiece.getDir());
        if (!logicGrid.isInBounds(newPos)) {
            return false;
        }
        if (game.getPlayerAt(newPos) != null &&
                (!logicGrid.positionIsFree(newPos, logicGrid.CONVEYOR_BELT_LAYER_INDEX) || !logicGrid.positionIsFree(newPos, logicGrid.EXPRESS_BELT_LAYER_INDEX))) {
            ConveyorBeltPiece conveyorBeltPieceInFront = logicGrid.getPieceType(newPos, ConveyorBeltPiece.class);
            if(!logicGrid.positionIsFree(newPos, logicGrid.EXPRESS_BELT_LAYER_INDEX)){
                conveyorBeltPieceInFront = logicGrid.getPieceType(newPos, ExpressBeltPiece.class);
            }
            if (onlyExpressBelt && conveyorBeltPiece instanceof ExpressBeltPiece && !(conveyorBeltPieceInFront instanceof ExpressBeltPiece)) {
                return false;
            }
            return conveyorBeltPieceInFront.getDir().getOppositeDirection() != conveyorBeltPiece.getDir();
        }
        return false;
    }

    /**
     * Checks if a player is going to crash into another player from the movement of the conveyorbelt
     *
     * @param player          the player that is going to be moved
     * @param game            the current game
     * @param onlyExpressBelt true if only expressbelt are moving at this point in the phase
     * @return true if player is going to end up on the same tile as another player, false otherwise
     */
    private static boolean isPlayerGoingToCrash(Player player, Game game, boolean onlyExpressBelt) {
        LogicGrid logicGrid = game.getLogicGrid();
        ConveyorBeltPiece conveyorBeltPiece = logicGrid.getPieceType(player.getPos(),  ConveyorBeltPiece.class);
        if(!logicGrid.positionIsFree(player.getPos(), logicGrid.EXPRESS_BELT_LAYER_INDEX)){
            conveyorBeltPiece = logicGrid.getPieceType(player.getPos(), ExpressBeltPiece.class);
        }
        Position newPos = player.getPos().getPositionIn(conveyorBeltPiece.getDir());
        if (!logicGrid.isInBounds(newPos)) { return false; }
        if(game.getPlayerAt(newPos) != null) return  true;
        for (Direction dir : Direction.values()) {
            Position orthoPos = newPos.getPositionIn(dir);
            if (!logicGrid.isInBounds(orthoPos) || orthoPos.equals(player.getPos())) { continue; }
            if (game.getPlayerAt(orthoPos) != null &&
                    (!logicGrid.positionIsFree(orthoPos, logicGrid.CONVEYOR_BELT_LAYER_INDEX) || !logicGrid.positionIsFree(orthoPos, logicGrid.EXPRESS_BELT_LAYER_INDEX))) {
                if (!game.getPlayerAt(orthoPos).hasBeenMovedThisPhase()) {
                    if (onlyExpressBelt && conveyorBeltPiece instanceof ExpressBeltPiece && !logicGrid.positionIsFree(orthoPos, logicGrid.CONVEYOR_BELT_LAYER_INDEX)) {
                        return false;
                    }
                    ConveyorBeltPiece piece = logicGrid.getPieceType(orthoPos, ConveyorBeltPiece.class);
                    if (!logicGrid.positionIsFree(orthoPos, logicGrid.EXPRESS_BELT_LAYER_INDEX)) {
                        piece = logicGrid.getPieceType(orthoPos, ExpressBeltPiece.class);
                    }
                    Position orthoPosDir = orthoPos.getPositionIn(piece.getDir());
                    if (orthoPosDir.equals(newPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
