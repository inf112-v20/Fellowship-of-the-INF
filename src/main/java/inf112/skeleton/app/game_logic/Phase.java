package inf112.skeleton.app.game_logic;


import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;
import inf112.skeleton.app.player.Player;


import java.util.*;

/**
 * The phases within a round in a RoboRally game.
 */
public class Phase {

    private Player[] listOfPlayers;
    private ArrayList<Player> orderedListOfPlayers;
    private int phaseNumber;
    HashMap<Player, Integer> playerAndPriority;
    private Game game;
    private LogicGrid logicGrid;


    /*
    Class representing a single phase in a RoboRally game.
    First it moves robots from card moves in the correct order.
    Then board elements move (conveyorbelts, pushers, cogs etc.).
    Lasers fire and then checkpoints and spawnpoints are updated.
    At the very end it check if the game is over (winner or losers).
     */
    public Phase(Game game) {
        this.game = game;
        this.logicGrid = game.getLogicGrid();
        this.listOfPlayers = game.getListOfPlayers();
        this.playerAndPriority = new HashMap<>();
        this.phaseNumber = 0;
    }

    /*
    Getters
     */
    public ArrayList<Player> getOrderedListOfPlayers() {
        return this.orderedListOfPlayers;
    }

    public int getPhaseNumber() {
        return this.phaseNumber;
    }

    /**
     * Executes a phase based on RoboRally ruleset.
     *
     * @param phaseNumber the current phase number
     */
    public void executePhase(int phaseNumber) {
        startPhase(phaseNumber);
        moveRobots();
        moveConveyorBelts(true);
        moveConveyorBelts(false);
        pushersPush();
        rotateCogs();
        lasersFire();
        touchCheckPoints();
        game.checkForGameCompletion();
        game.setPhaseDone(true);
    }

    /**
     * Things that need to be done at the beginning of each phase
     *
     * @param phaseNumber which phase out of five it is
     */
    public void startPhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    /**
     * Sort all players based on the priority of their cards this phase.
     * Execute all players programcards for this phase in the order of sorting.
     */
    public void moveRobots() {
        playerAndPriority = new HashMap<>();
        orderedListOfPlayers = new ArrayList<>();
        for (Player player : listOfPlayers) {
            if (!player.isPowerDownMode() && !player.isPermanentlyDead()) {
                ProgramCard programCardThisPhase = player.getSelectedCards()[phaseNumber];
                Integer cardPriority = programCardThisPhase.getPriority();
                playerAndPriority.put(player, cardPriority);
            }
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
            if(player.isDead()){continue;}
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
     * Checks if players are currently on a flag.
     * If so, check if this is the flag the player is currently going for, using the number of checkpoints visited.
     * If this is the right checkpoint, then set a new spawnpoint from this location.
     */
    private void touchCheckPoints() {
        for (Player player : listOfPlayers) {
            if(player.isDead()){continue;}
            Position oldSpawnPoint = player.getSpawnPoint();
            if (!logicGrid.positionIsFree(player.getPos(), logicGrid.FLAG_LAYER_INDEX)) {
                FlagPiece flag = logicGrid.getPieceType(player.getPos(), FlagPiece.class);
                player.setSpawnPoint(player.getPos());
                if (player.getCheckpointsVisited() + 1 == flag.getFlagNumber()) {
                    player.visitedCheckpoint();
                }
            }
            else if(!logicGrid.positionIsFree(player.getPos(), logicGrid.REPAIR_LAYER_INDEX)){
                player.setSpawnPoint(player.getPos());
            }
            if(!oldSpawnPoint.equals(player.getSpawnPoint())){
                ArrayList<Player> respawnOrder = game.getRespawnOrder();
                respawnOrder.remove(player);
                respawnOrder.add(player);
            }

        }
    }

    /**
     * Checks if a player is currently on a laser.
     * If so, the player receives damage, and the laser is intercepted.
     * Players also shoot their own laser.
     */
    private void lasersFire() {
        // Static lasers (from walls)
        for (Player player : listOfPlayers) {
            if(player.isDead()){continue;}
            player.shootLaser();
            int damage = 1;
            LaserPiece laser;
            if (logicGrid.getPieceType(player.getPos(), LaserPiece.class) != null) {
                laser = logicGrid.getPieceType(player.getPos(), LaserPiece.class);
                if (!logicGrid.positionIsFree(player.getPos(), logicGrid.LASER_LAYER_INDEX)
                        && logicGrid.positionIsFree(player.getPos(), logicGrid.LASER_SOURCE_LAYER_INDEX)) {
                    if (isPlayerBlocking(player.getPos(), laser.getDir())) {
                        continue;
                    }
                }
                if (laser.isDoubleLaser() || laser.isCrossingLasers()) { damage++; }
                player.takeDamage(damage);
            }

        }
    }

    /**
     * Checks if a player is blocking a laser from hitting another player.
     * Since all horizontal lasers point west, and all vertical lasers point north
     * it is not certain we find the lasersource by following the laser direction,
     * if so we call the method again checking for the lasersource in the other direction.
     *
     * @param playerPos the position of a player that is standing on a laser.
     * @param laserDir  the direction of the laser.
     * @return true if a player is standing between the player on the laser and the lasersource, false otherwise.
     */
    private boolean isPlayerBlocking(Position playerPos, Direction laserDir) {
        Position laserPos = playerPos;
        boolean playerBetween = false;
        for (int i = 0; i < logicGrid.getHeight(); i++) {
            laserPos = laserPos.getPositionIn(laserDir);
            if (!logicGrid.isInBounds(laserPos)) {
                break;
            }
            if (!logicGrid.positionIsFree(laserPos, logicGrid.PLAYER_LAYER_INDEX)) {
                playerBetween = true;
            }
            if (!logicGrid.positionIsFree(laserPos, logicGrid.LASER_SOURCE_LAYER_INDEX)) {
                if (logicGrid.getPieceType(laserPos, LaserSourcePiece.class) != null) {
                    LaserSourcePiece laserSource = logicGrid.getPieceType(laserPos, LaserSourcePiece.class);
                    if (laserSource.getDir().equals(laserDir)) {
                        return playerBetween;
                    }
                }
            }
        }
        return (isPlayerBlocking(playerPos, laserDir.getOppositeDirection()));
    }


    /**
     * Checks if any player is on a conveyorbelt and will move them if they do
     *
     * @param moveOnlyExpressBelts true if only expressbelts should move
     */
    public void moveConveyorBelts(boolean moveOnlyExpressBelts) {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        for (Player player : listOfPlayers) {
            if(player.isDead() || !logicGrid.isInBounds(player.getPos())){continue;}
            boolean playerIsOnConveyorBelt = !logicGrid.positionIsFree(player.getPos(), logicGrid.CONVEYOR_BELT_LAYER_INDEX);
            boolean playerIsOnExpressBelt =  !logicGrid.positionIsFree(player.getPos(), logicGrid.EXPRESS_BELT_LAYER_INDEX);
            if (playerIsOnConveyorBelt || playerIsOnExpressBelt ) {
                if ((moveOnlyExpressBelts && !playerIsOnExpressBelt || player.hasBeenMovedThisPhase())) {
                    continue;
                }
                BoardElementsMove.moveConveyorBelt(player, game, moveOnlyExpressBelts, moves);
            }
        }
        game.executeMoves(moves);
        for (Player player : listOfPlayers) {
            player.setHasBeenMovedThisPhase(false);
        }
    }

    /**
     * Checks if any player is on a cog,
     * and will rotate them accordingly if true.
     */
    public void rotateCogs() {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        for (Player player : listOfPlayers) {
            if(player.isDead()){continue;}
            boolean playerIsOnCog = !logicGrid.positionIsFree(player.getPos(), logicGrid.COG_LAYER_INDEX);
            if (playerIsOnCog) {
                CogPiece cogPiece = game.getLogicGrid().getPieceType(player.getPos(), CogPiece.class);
                BoardElementsMove.rotateCog(player, moves, cogPiece);
            }
        }
        game.executeMoves(moves);
    }

    /**
     * If a player is standing on an active pusher, it will be pushed.
     */
    public void pushersPush() {
        MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
        boolean isOddPhase = ((phaseNumber + 1) % 2) != 0;
        for (PusherPiece pusher : logicGrid.getPushersList()) {
            if (pusher.isActiveWhenOddPhase() == isOddPhase) {
                PlayerPiece possiblePlayer = logicGrid.getPieceType(pusher.getPos(), PlayerPiece.class);
                if (possiblePlayer != null) {
                    Player player = possiblePlayer.getPlayer();
                    if (!player.hasBeenPushedThisPhase()) {
                        player.tryToGo(pusher.getPushingDir(), moves);
                        player.setHasBeenPushedThisPhase(true);
                    }
                }
            }
        }
        game.executeMoves(moves);
        for (Player player : listOfPlayers) {
            player.setHasBeenPushedThisPhase(false);
        }
    }
}
