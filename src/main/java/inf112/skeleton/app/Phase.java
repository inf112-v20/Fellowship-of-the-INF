package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.player.Player;
import inf112.skeleton.app.BoardElementsMove;


import java.util.*;

public class Phase {

    private Player[] listOfPlayers;
    private ArrayList<Player> orderedListOfPlayers = new ArrayList<>();
    private int phaseNumber;
    private HashMap<Player, Integer> playerAndPriority;
    private BoardElementsMove boardElementsMove = new BoardElementsMove();

    public Phase (Game game){
        listOfPlayers = game.getListOfPlayers();
        this.playerAndPriority = new HashMap<>();
    }

    public void executePhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
        sortPlayersAndExecuteCards();
        moveExpressBelts();
        moveConveyorBelts();
        rotateCogs();
        //fire lasers
        //touch flag
        //update respawn point (flag or repair site)
    }

    public void sortPlayersAndExecuteCards(){
        playerAndPriority = new HashMap<>();
        for (int i = 0; i < listOfPlayers.length ; i++) {
            Player player = listOfPlayers[i];
            ProgramCard programCardThisPhase = player.getSelectedCards().get(phaseNumber);
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
            ProgramCard cardThisPhase = player.getSelectedCards().get(phaseNumber);
            player.executeCardAction(cardThisPhase);
            System.out.println("Player " + player.getPlayerNumber() + " played card "
                    + cardThisPhase.getCommand() + ", Priority: " + cardThisPhase.getPriority());
        }

    }

    public void moveExpressBelts(){
        for (int i = 0; i < listOfPlayers.length ; i++) {
            if(listOfPlayers[i].isOnExpressBelt()){
                boardElementsMove.moveExpressBelt(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
            }
        }
    }

    public void moveConveyorBelts(){
        for (int i = 0; i < listOfPlayers.length ; i++) {
            if(listOfPlayers[i].isOnConveyorBelt()){
                boardElementsMove.moveConveyorBelt(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
            }
        }
    }

    public void rotateCogs(){
        for (int i = 0; i < listOfPlayers.length ; i++) {
            if(listOfPlayers[i].isOnCog()){
                boardElementsMove.rotateCog(listOfPlayers[i].getCurrentBoardPiece(), listOfPlayers[i]);
            }
        }
    }

    public ArrayList<Player> getOrderedListOfPlayers(){return orderedListOfPlayers;}

    public int getPhaseNumber(){return phaseNumber;}
}
