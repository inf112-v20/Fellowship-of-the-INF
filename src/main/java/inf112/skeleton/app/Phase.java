package inf112.skeleton.app;

import inf112.skeleton.app.Cards.ProgramCard;

import java.util.*;

public class Phase {

    private ArrayList<Player> listOfPlayers;
    private static int phaseNumber = 0;

    public Phase (Game game){
        // kake
        listOfPlayers = game.getListOfPlayers();  //Make this variable and getter in Game class
        HashMap<Player, Integer> playerAndPriority = new HashMap<>();
        for (int i = 0; i < listOfPlayers.size() ; i++) {
            Player player = listOfPlayers.get(i);
            ProgramCard programCardThisPhase = player.getSelectedCards.get(phaseNumber); //Make this variable and getter in Player class
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
            ProgramCard cardThisPhase = player.getSelectedCards.get(phaseNumber);
            player.executeCardAction(cardThisPhase); //Create this method in Player
        }
    }
}
