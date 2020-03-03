package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.player.Player;

import java.util.*;

public class Phase {

    private Player[] listOfPlayers;
    private static int phaseNumber = 0;
    HashMap<Player, Integer> playerAndPriority;

    public Phase (Game game){
        listOfPlayers = game.getListOfPlayers();
        this.playerAndPriority = new HashMap<>();
    }

    public void executePhase(int phaseNumber) {
        playerAndPriority = new HashMap<>();
        for (int i = 0; i < listOfPlayers.length ; i++) {
            System.out.println("Moving Player " + i);
            Player player = listOfPlayers[i];
            ProgramCard programCardThisPhase = player.getSelectedCards().get(phaseNumber);
            System.out.println(player.toString() + " is executing " + programCardThisPhase.toString());
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
            ProgramCard cardThisPhase = player.getSelectedCards().get(phaseNumber);
            System.out.println(player.toString() + " is executing " + cardThisPhase.toString());
            player.executeCardAction(cardThisPhase);
        }
    }

}
