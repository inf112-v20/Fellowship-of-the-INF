package inf112.skeleton.app.game_logic;

import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;



public class BoardElementsMove {

    public static void pusherPushesPlayer(PusherPiece pusherPiece, Player player, MovesToExecuteSimultaneously moves) { //TODO: @Erlend remove?
        player.tryToGo(pusherPiece.getDir(), moves);
    }

    /**
     * Rotates a player standing on a cog 90* in the direction of the cog.
     *
     * @param player The player that is currently standing on the BoardPiece.
     * @param moves list to update with rotate move
     */
    public static void rotateCog(Player player, MovesToExecuteSimultaneously moves) {
        BoardPiece boardPiece = player.getCurrentBoardPiece();
        if (((CogPiece) boardPiece).isRotateClockwise()) {
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
     */
    public static void moveConveyorBelt(Player player, Game game, boolean onlyExpressBelt, MovesToExecuteSimultaneously moves) {
        if (player == null || player.getCurrentBoardPiece() == null) {
            System.out.println("Error: couldn't move player on conveyorbelt");
            return;
        }
        BoardPiece boardPiece = player.getCurrentBoardPiece();
        if (!(boardPiece instanceof ConveyorBeltPiece)) {
            System.out.println("Error: couldn't move player on conveyorbelt");
            return;
        }
        LogicGrid logicGrid = game.getLogicGrid();
        if (!logicGrid.isInBounds(player.getPos())) {
            System.out.println("Error: conveyorbelt can't move " + player.toString() + " because they are out of bounds");
            return;
        }
        ConveyorBeltPiece conveyorBeltPiece = (ConveyorBeltPiece) logicGrid.getPieceType(player.getPos(), boardPiece.getClass());
        //System.out.println(player.toString() + " is moving on conveyorbelt at pos " + player.getPos() );
        Direction conveyorBeltDirection = conveyorBeltPiece.getDir();
        Position newPos = player.getPos().getPositionIn(conveyorBeltDirection);
        if (isPlayerInFront(player, game, onlyExpressBelt)) {
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
     * @param player          the player that is going to get moved
     * @param game            the current game
     * @param onlyExpressBelt true if only expressbelt are moving at this point in the phase
     * @return true if there is player in front (in the direction of the conveyorbelt), and that player is standing
     * on a conveyorbelt that is not facing towards the first conveyorbelt, false otherwise
     */
    private static boolean isPlayerInFront(Player player, Game game, boolean onlyExpressBelt) {
        BoardPiece boardPiece = player.getCurrentBoardPiece();
        LogicGrid logicGrid = game.getLogicGrid();
        Position newPos = player.getPos().getPositionIn(((ConveyorBeltPiece) boardPiece).getDir());
        if (!logicGrid.isInBounds(newPos)) {
            return false;
        }
        //12 = playerindex, 4 = conveyorbeltindex, 5 = expressbeltindex
        if (!logicGrid.positionIsFree(newPos, 12) &&
                (!logicGrid.positionIsFree(newPos, 4) || !logicGrid.positionIsFree(newPos, 5))) {
            ConveyorBeltPiece piece = logicGrid.getPieceType(newPos, ConveyorBeltPiece.class);
            if (!logicGrid.positionIsFree(newPos, 5)) {
                piece = logicGrid.getPieceType(newPos, ExpressBeltPiece.class);
            }
            if (onlyExpressBelt && boardPiece instanceof ExpressBeltPiece && !(piece instanceof ExpressBeltPiece)) {
                return false;
            }

            return piece.getDir().getOppositeDirection() != ((ConveyorBeltPiece) boardPiece).getDir();
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
        BoardPiece boardPiece = player.getCurrentBoardPiece();
        LogicGrid logicGrid = game.getLogicGrid();
        Position newPos = player.getPos().getPositionIn(((ConveyorBeltPiece) boardPiece).getDir());
        if (!logicGrid.isInBounds(newPos)) { return false; }
        if(game.getPlayerAt(newPos) != null) return  true;
        for (Direction dir : Direction.values()) {
            Position orthoPos = newPos.getPositionIn(dir);
            if (!logicGrid.isInBounds(orthoPos) || orthoPos.equals(player.getPos())) { continue; }
            if (game.getPlayerAt(newPos) != null &&
                    (!logicGrid.positionIsFree(orthoPos, 4) || !logicGrid.positionIsFree(orthoPos, 5))) {
                if (!game.getPlayerAt(orthoPos).hasBeenMovedThisPhase()) {
                    if (onlyExpressBelt && boardPiece instanceof ExpressBeltPiece && !logicGrid.positionIsFree(orthoPos, 4)) {
                        return false;
                    }
                    ConveyorBeltPiece piece = logicGrid.getPieceType(orthoPos, ConveyorBeltPiece.class);
                    if (!logicGrid.positionIsFree(orthoPos, 5)) {
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
