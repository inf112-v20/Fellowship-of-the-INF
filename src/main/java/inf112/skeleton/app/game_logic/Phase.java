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
    private ArrayList<Player> copyOfPlayers;

    public Phase(Game game) {
        this.game = game;
        this.listOfPlayers = game.getListOfPlayers();
        this.playerAndPriority = new HashMap<>();
        this.phaseNumber = 0;
        this.copyOfPlayers = new ArrayList<>();
    }

    /**
     * Executes a phase which in order consists of:
     * Executing programcards
     * Move players on expressbelts
     * Move players on conveyorbelts
     * Rotate players on cogs
     *
     * @param phaseNumber the current phase number
     */
    public void executePhase(int phaseNumber) {
        startPhase(phaseNumber);
        moveRobots();
        setUpConveyorBelts();
        moveConveyorBelts(true);
        setUpConveyorBelts();
        moveConveyorBelts(false);
        rotateCogs();
        lasersFire();
        touchCheckPoints();
        //TODO add updating of respawn point (flag or repair site)
    }

    /**
     * Things that need to be done at the beginning of each phase
     * @param phaseNumber which phase out of five it is
     */
    public void startPhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
        this.copyOfPlayers = new ArrayList<>();
    }

    /**
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
     * TODO @Lena needs comments
     */
    private void touchCheckPoints() {
        for (Player player : listOfPlayers) {
            if (player.getCurrentBoardPiece() instanceof FlagPiece) {
                FlagPiece flag = (FlagPiece) player.getCurrentBoardPiece();

                if (player.getCheckpointsVisited() == flag.getFlagNumber() - 1) {
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
            if (listOfPlayers[i].isOnLaser()) {
                Player player = listOfPlayers[i];
                int damage = 1;
                LaserPiece laser;
                if (game.getLogicGrid().getPieceType(player.getPos(), LaserPiece.class) != null) {
                    laser = game.getLogicGrid().getPieceType(player.getPos(), LaserPiece.class);
                    if (laser.isDoubleLaser() || laser.isCrossingLasers()) damage += 1;
                    // Intercepting laser after it hits a robot
                    interceptLaser(laser.getPos(), laser.getDir());
                }
                player.takeDamage(damage);
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
     * Checks if any player is on a conveyorbelt
     * If true, check if there is player in front (in the direction of the conveyorbelt) on a conveyorbelt and that
     * conveyorbelt is not facing towards the first conveyorbelt.
     * If true, move the first player later by a recursive call so that the other player standing in the way can be
     * moved by the conveyorbelt first.
     * If two players are standing on different conveyorbelts pointing directly into eachother, none of them will move
     * If two players are standing on different conveyorbelts that will put both players in the same tile, none of
     * them will move.
     * @param moveOnlyExpressBelts true if only expressbelts should move
     */
    public void moveConveyorBelts(boolean moveOnlyExpressBelts) {
        boolean morePlayersToMove = false;
        for (int i = 0; i < copyOfPlayers.size(); i++) {
            Player player = copyOfPlayers.get(i);
            if(player.isOnConveyorBelt()) {
                if(moveOnlyExpressBelts && !player.isOnExpressBelt()){

                    copyOfPlayers.remove(i);
                    i--;
                    continue;
                }
                if(BoardElementsMove.isPlayerInFront(player.getCurrentBoardPiece(), player, game.getLogicGrid(), moveOnlyExpressBelts)){
                    morePlayersToMove = true;
                    continue;
                }
                if(BoardElementsMove.isPlayerGoingToCrash(player.getCurrentBoardPiece(), player, game.getLogicGrid(), game, moveOnlyExpressBelts)){
                    copyOfPlayers.remove(i);
                    i--;
                    continue;
                }
                Move move = new Move(player);
                BoardElementsMove.moveConveyorBelt(player.getCurrentBoardPiece(), player, game.getLogicGrid());
                move.updateMove(player);
                game.executeMoves(move.toMovesList());
                player.setConveyorBeltMove(true);
                player.setHasBeenMovedThisPhase(true);
                copyOfPlayers.remove(i);
                i--;
            }
        }
        if (morePlayersToMove) {
            moveConveyorBelts(moveOnlyExpressBelts);
        }
    }

    /**
     * Checks if any player is on a cog,
     * and will rotate them accordingly if true.
     */
    public void rotateCogs() {
        for (Player player : listOfPlayers) {
            if (player.isOnCog()) {
                Move move = new Move(player);
                BoardElementsMove.rotateCog(player.getCurrentBoardPiece(), player);
                move.updateMove(player);
                game.executeMoves(move.toMovesList());
            }
        }
    }

    public ArrayList<Player> getOrderedListOfPlayers() {
        return orderedListOfPlayers;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    //TODO @Erlend needs comments
    private void setUpConveyorBelts() {
        copyOfPlayers.clear();
        Collections.addAll(copyOfPlayers, listOfPlayers);
        for (Player player : listOfPlayers) {
            player.setHasBeenMovedThisPhase(false);
        }
    }
}
