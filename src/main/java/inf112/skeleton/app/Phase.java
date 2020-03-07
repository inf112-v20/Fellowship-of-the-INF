package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.player.Player;
import inf112.skeleton.app.BoardElementsMove;
import java.util.*;

public class Phase {

    private Player[] listOfPlayers;
    private ArrayList<Player> orderedListOfPlayers;
    private int phaseNumber;
    private HashMap<Player, Integer> playerAndPriority;

    public Phase (Game game){
        this.listOfPlayers = game.getListOfPlayers();
        this.playerAndPriority = new HashMap<>();
    }

    /**
     * Executes a phase which consists of:
     * Executing programcards
     * Move players on expressbelts
     * Move players on conveyorbelts
     * Rotate players on cogs
     * @param phaseNumber the current phase number
     */
    public void executePhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
        sortPlayersAndExecuteCards();
        moveExpressBelts();
        moveConveyorBelts();
        rotateCogs();
        //TODO add lasers
        //TODO add touching flags/checkpoints
        //TODO add updating of respawn point (flag or repair site)
    }

    /**
     * Sort all players based on the priority of their cards this phase.
     * Execute all players programcards for this phase in the order of sorting.
     */
    public void sortPlayersAndExecuteCards(){
        playerAndPriority = new HashMap<>();
        orderedListOfPlayers = new ArrayList<>();
        for (int i = 0; i < listOfPlayers.length ; i++) {
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
            ProgramCard cardThisPhase = player.getSelectedCards()[phaseNumber];
            player.executeCardAction(cardThisPhase);
            System.out.println(player.toString() + " played card " + cardThisPhase.toString());
        }
    }

    /**
     * Checks if any player is on an expressbelt,
     * and will move them accordingly if true.
     */

    public void moveExpressBelts(){
        for (int i = 0; i < listOfPlayers.length ; i++) {
            if(listOfPlayers[i].isOnExpressBelt()){
                BoardElementsMove.moveExpressBelt(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
            }
        }
    }

    /**
     * Checks if any player is on a conveyorbelt,
     * and will move them accordingly if true.
     */
    public void moveConveyorBelts(){
        for (int i = 0; i < listOfPlayers.length ; i++) {
            if(listOfPlayers[i].isOnConveyorBelt()){
                BoardElementsMove.moveConveyorBelt(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
            }
        }
    }

    /**
     * Checks if any player is on a cog,
     * and will rotate them accordingly if true.
     */
    public void rotateCogs(){
        for (int i = 0; i < listOfPlayers.length ; i++) {
            if(listOfPlayers[i].isOnCog()){
                BoardElementsMove.rotateCog(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
            }
        }
    }

    public ArrayList<Player> getOrderedListOfPlayers(){return orderedListOfPlayers;}

    public int getPhaseNumber(){return phaseNumber;}
}
