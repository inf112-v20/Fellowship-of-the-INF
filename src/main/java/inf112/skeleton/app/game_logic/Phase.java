package inf112.skeleton.app.game_logic;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.FlagPiece;
import inf112.skeleton.app.grid_objects.LaserPiece;
import inf112.skeleton.app.player.Player;

import java.util.*;

public class Phase {

    private Player[] listOfPlayers;
    private ArrayList<Player> orderedListOfPlayers;
    private int phaseNumber;
    HashMap<Player, Integer> playerAndPriority;
    private Game game;

    public Phase(Game game) {
        this.game = game;
        this.listOfPlayers = game.getListOfPlayers();
        this.playerAndPriority = new HashMap<>();
        this.phaseNumber = 0;
    }

    /**
     * Executes a phase which in order consists of:
     * Executing programcards
     * Move players on expressbelts once
     * Move players on expressbelts once and move players on conveyorbelts once
     * Rotate players on cogs
     *
     * @param phaseNumber the current phase number
     */
    public void executePhase(int phaseNumber) {
        startPhase(phaseNumber);
        moveRobots();
        moveConveyorBelts(true);
        moveConveyorBelts(false);
        rotateCogs();
        lasersFire();
        touchCheckPoints();
    }

    /**
     * Things that need to be done at the beginning of each phase
     * @param phaseNumber which phase out of five it is
     */
    public void startPhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    /**
     * TODO Add in a check to see if players are in power down mode, with player.isPowerDownMode. If they are, then they should not move
     * Sort all players based on the priority of their cards this phase.
     * Execute all players programcards for this phase in the order of sorting.
     */
    public void moveRobots() {
        playerAndPriority = new HashMap<>();
        orderedListOfPlayers = new ArrayList<>();
        for (Player player : listOfPlayers) {
            ProgramCard programCardThisPhase = player.getSelectedCards()[phaseNumber];
            Integer cardPriority = programCardThisPhase.getPriority();
            playerAndPriority.put(player, cardPriority);
        }
        Object[] a = playerAndPriority.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<Player, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<Player, Integer>) o1).getValue());
            }
        });
        for (Object e : a) {
            Player player = ((Map.Entry<Player, Integer>) e).getKey();
            orderedListOfPlayers.add(player);
            //if(player.getPlayerNumber() == 1 || player.getPlayerNumber() == 2){continue;}
            MovesToExecuteSimultaneously movesToExecuteTogether = generateMovesToExecuteTogether(player);
            game.executeMoves(movesToExecuteTogether); //executes backend, and adds to list of frontend moves to show
        }
    }

    /**
     * Even though only one player executes a move, it may affect other robots on the map.
     * Therefore we generate a list of moves that need to be executed together.
     *
     * @param player that is executing a move
     * @return list of moves to execute together, in the other they should be executed
     */
    private MovesToExecuteSimultaneously generateMovesToExecuteTogether(Player player) {
        ProgramCard cardThisPhase = player.getSelectedCards()[phaseNumber];
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        player.executeCardAction(cardThisPhase, moves); //updates the state of the player, not the board
        return moves;
    }

    /**
     * Checks if players are currently on a flag.
     * If so, check if this is the flag the player is currently going for, using the number of checkpoints visited.
     *
     * If this is the right checkpoint, then set a new spawnpoint from this location.
     */
    private void touchCheckPoints() {
        for (Player player : listOfPlayers) {

            if (player.getCurrentBoardPiece() instanceof FlagPiece) {
                FlagPiece flag = (FlagPiece) player.getCurrentBoardPiece();

                if (player.getCheckpointsVisited() + 1 == flag.getFlagNumber()) {
                    player.visitedCheckpoint();
                    player.setSpawnPoint(player.getPos().getX(), player.getPos().getY());
                }
            }
    }
}

    /**
     * // TODO first wall-lasers fire, then robots? @Henrik
     * Checks if a player is currently on a laser.
     * If so, the player receives damage, and the laser is intercepted.
     */
    private void lasersFire() {
        // Static lasers (from walls)
        for (int i = 0; i < listOfPlayers.length; i++) {
            Player player = listOfPlayers[i];
            if(game.getLogicGrid().isInBounds(player.getPos())) {
                int damage = 1;
                LaserPiece laser;
                if (game.getLogicGrid().getPieceType(player.getPos(), LaserPiece.class) != null) {
                    laser = game.getLogicGrid().getPieceType(player.getPos(), LaserPiece.class);
                    if (laser.isDoubleLaser() || laser.isCrossingLasers()) damage += 1;
                    // Intercepting laser after it hits a robot
                    interceptLaser(laser.getPos(), laser.getDir());
                    player.takeDamage(damage);
                }
            }
        }

    }

    /**
     * TODO @Henrik needs comments
     * @param pos
     * @param dir
     */
    private void interceptLaser(Position pos, Direction dir) {
        int xDir = 0;
        int yDir = 0;
        if (dir == Direction.NORTH || dir == Direction.SOUTH) yDir = 1;
        else xDir = 1;
    }


    /**
     * Checks if any player is on a conveyorbelt and will move them if they do
     * @param moveOnlyExpressBelts true if only expressbelts should move
     */
    public void moveConveyorBelts(boolean moveOnlyExpressBelts) {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        for (Player player : listOfPlayers){
            if(player.isOnConveyorBelt()) {
                if((moveOnlyExpressBelts && !player.isOnExpressBelt())||player.hasBeenMovedThisPhase()){
                    continue;
                }
                BoardElementsMove.moveConveyorBelt(player, game, moveOnlyExpressBelts, moves);
            }
        }
        game.executeMoves(moves);
        for (Player player : listOfPlayers){player.setHasBeenMovedThisPhase(false);}
    }

    /**
     * Checks if any player is on a cog,
     * and will rotate them accordingly if true.
     */
    public void rotateCogs() {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        for (Player player : listOfPlayers) {
            if (player.isOnCog()) {
                Move move = new Move(player);
                BoardElementsMove.rotateCog(player, game);
                move.updateMove(player);
                moves.add(move);
            }
        }
        game.executeMoves(moves);
    }

    public ArrayList<Player> getOrderedListOfPlayers() {
        return orderedListOfPlayers;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

}
