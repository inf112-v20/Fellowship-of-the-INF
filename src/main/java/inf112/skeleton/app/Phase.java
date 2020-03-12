package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.BoardPiece;
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
     * Move players on expressbelts
     * Move players on conveyorbelts
     * Rotate players on cogs
     *
     * @param phaseNumber the current phase number
     */
    public void executePhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
        moveRobots();
        moveExpressBelts();
        moveConveyorBelts();
        rotateCogs();
        lasersFire();
        touchCheckPoints();
        //TODO add lasers
        //TODO add touching flags/checkpoints
        //TODO add updating of respawn point (flag or repair site)
    }

    /**
     * Sort all players based on the priority of their cards this phase.
     * Execute all players programcards for this phase in the order of sorting.
     */
    public void moveRobots() {
        playerAndPriority = new HashMap<>();
        orderedListOfPlayers = new ArrayList<>();
        for (int i = 0; i < listOfPlayers.length; i++) {
            Player player = listOfPlayers[i];
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
            System.out.println("PLayer: " + player.toString() + " pos: " + player.getPos().toString());
            ArrayList<Move> movesToExecuteTogether = generateMovesToExecuteTogether(player);
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
    private ArrayList<Move> generateMovesToExecuteTogether(Player player) {
        ProgramCard cardThisPhase = player.getSelectedCards()[phaseNumber];
        Move move = new Move(player);
        player.executeCardAction(cardThisPhase); //updates the state of the player, not the board
        move.updateMove(player);
        System.out.println("Player " + player.getPlayerNumber() + " played card "
                + cardThisPhase.getCommand() + ", Priority: " + cardThisPhase.getPriority());
        return move.toArrayList();
    }

    private void touchCheckPoints() {
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
     *
     * @param pos
     * @param dir
     */
    private void interceptLaser(Position pos, Direction dir) {
        int xDir = 0;
        int yDir = 0;
        if (dir == Direction.NORTH || dir == Direction.SOUTH) yDir = 1;
        else xDir = 1;



    }


    private void pushersPush() {
    }

    /**
     * Checks if any player is on an expressbelt,
     * and will move them accordingly if true.
     */

    public void moveExpressBelts() {
        for (int i = 0; i < listOfPlayers.length; i++) {
            if (listOfPlayers[i].isOnExpressBelt()) {
                Player player = listOfPlayers[i];
                Move move = new Move(player);
                BoardElementsMove.moveExpressBelt(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
                move.updateMove(player);
                game.executeMoves(move.toArrayList());
            }
        }
    }

    /**
     * Checks if any player is on a conveyorbelt,
     * and will move them accordingly if true.
     */
    public void moveConveyorBelts() {
        for (int i = 0; i < listOfPlayers.length; i++) {
            if (listOfPlayers[i].isOnConveyorBelt()) {
                Player player = listOfPlayers[i];
                Move move = new Move(player);
                BoardElementsMove.moveConveyorBelt(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
                move.updateMove(player);
                game.executeMoves(move.toArrayList());
            }
        }
    }

    /**
     * Checks if any player is on a cog,
     * and will rotate them accordingly if true.
     */
    public void rotateCogs() {
        for (int i = 0; i < listOfPlayers.length; i++) {
            if (listOfPlayers[i].isOnCog()) {
                Player player = listOfPlayers[i];
                Move move = new Move(player);
                BoardElementsMove.rotateCog(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
                move.updateMove(player);
                game.executeMoves(move.toArrayList());
            }
        }
    }

    public ArrayList<Player> getOrderedListOfPlayers() {
        return orderedListOfPlayers;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }
}
