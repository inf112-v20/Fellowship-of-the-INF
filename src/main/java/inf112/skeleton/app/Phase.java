package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.player.Player;

import java.util.*;

public class Phase {

    private Player[] listOfPlayers;
    private ArrayList<Player> orderedListOfPlayers = new ArrayList<>();
    private int phaseNumber;
    HashMap<Player, Integer> playerAndPriority;
    private Game game;

    public Phase (Game game){
        this.game = game;
        listOfPlayers = game.getListOfPlayers();
        this.playerAndPriority = new HashMap<>();
    }

    public void executePhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
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

            Position oldPos = player.getPos();
            Direction oldDir = player.getPlayerPiece().getDir();
            player.executeCardAction(cardThisPhase);
            Direction newDir = player.getPlayerPiece().getDir();
            Position newPos = player.getPos();
           game.executeMove(new Move(player, oldPos, newPos, oldDir, newDir));

            //game.getMoves().add(new Move(player, oldPos, newPos, oldDir, newDir)); //adds to moves that need to be shown
            System.out.println("Player " + player.getPlayerNumber() + " played card "
                    + cardThisPhase.getCommand() + ", Priority: " + cardThisPhase.getPriority());
        }

    }
    public ArrayList<Player> getOrderedListOfPlayers(){return orderedListOfPlayers;}

    public int getPhaseNumber(){return phaseNumber;}
}
