package inf112.skeleton.app.player;

import inf112.skeleton.app.cards.CardType;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.game_logic.Game;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.CogPiece;
import inf112.skeleton.app.grid_objects.ConveyorBeltPiece;
import inf112.skeleton.app.grid_objects.ExpressBeltPiece;

import java.util.*;

public class AIPlayer extends Player {
    private Position newRobotPos;
    private Direction newRobotDir;
    private int flagsVisitedInRound = 0;
    private int checkpointsVisited;
    private Position nextGoalPos;
    private int nextGoalFlag;
    private LogicGrid logicGrid;
    private ArrayList<ProgramCard> playerHandDeck;
    private ArrayList<ArrayList<List<Object>>> flagPositionScores;
    private ArrayList<ProgramCard> availableCardsLeft;
    private int cardsToPick;
    private ArrayList<ProgramCard> chosenCards;
    public enum Difficulty{EASY, MEDIUM, HARD, EXPERT}
    private Difficulty difficulty;



    public AIPlayer(int playerNumber, Game game, Difficulty difficulty) {
        super(playerNumber, game);
        this.logicGrid = game.getLogicGrid();
        this.flagPositionScores = logicGrid.getFlagPositionScores();
        this.difficulty = difficulty;
    }

    /**
     * Pick cards for computer player
     */
    public void pickCards() {
        this.playerHandDeck = getPlayerHandDeck();
        this.checkpointsVisited = getCheckpointsVisited();
        this.cardsToPick = 5 - getLockedCards().size();
        this.chosenCards = new ArrayList<>();
        this.availableCardsLeft = new ArrayList<>(playerHandDeck);
        this.newRobotPos = getPos();
        this.newRobotDir = getPlayerPiece().getDir();

        if (playerHandDeck.isEmpty()) {
            setLockedIn(true);
            return;
        }

        switch (difficulty){
            case EASY: pickRandom(); break;
            case MEDIUM: pickGreedy(); break;
            case HARD:
            case EXPERT:
                pickOptimal(); break;
        }
        setLockedIn(true);
        System.out.println(toString() + " playerhand: " + playerHandDeck);
        System.out.println(toString() + " chose " + Arrays.toString(getSelectedCards()) + "\n");
    }

    private void pickRandom(){
        ArrayList<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < playerHandDeck.size(); i++) {
            randomNumbers.add(i);
        }
        Collections.shuffle(randomNumbers);
        for (int randomNumber : randomNumbers){
            ProgramCard card = playerHandDeck.get(randomNumber);
            List<Object> finalPosAndDir =  getPosFromCardMove(card, newRobotPos, newRobotDir);
            Position finalPos = (Position) finalPosAndDir.get(0);
            Direction finalDir = (Direction) finalPosAndDir.get(1);
            if(isDeadMove(finalPos)){ continue; }
            newRobotPos = finalPos;
            newRobotDir = finalDir;
            chosenCards.add(card);
            if(chosenCards.size() == cardsToPick) {break;}
        }

        if(chosenCards.size() != cardsToPick) {
            int cardsMissing = chosenCards.size() - cardsToPick;
            for (int i = 0; i < cardsMissing ; i++) {
                chosenCards.add(getFirstAvailableCard());
            }
        }

        addCardsToSelectedCards(chosenCards);
    }


    private void pickGreedy() {
        for (int i = 0; i < cardsToPick ; i++) {
            nextGoalFlag = updateGoalFlag(checkpointsVisited+flagsVisitedInRound);
            nextGoalPos = logicGrid.getFlagPositions().get(nextGoalFlag);
            ProgramCard bestCard = getBestCard();
            if (!(bestCard == null)) {
                chosenCards.add(bestCard);
                availableCardsLeft.remove(bestCard);
                continue;
            }
            ProgramCard firstAvailableCard = getFirstAvailableCard();
            chosenCards.add(firstAvailableCard);
            availableCardsLeft.remove(firstAvailableCard);
        }
        addCardsToSelectedCards(chosenCards);
    }



    private ProgramCard getBestCard() {
        Object[] bestMovesInOrder = getBestCardsOrdered();
        for (Object cardAndScore : bestMovesInOrder) {
            ProgramCard card = ((Map.Entry<ProgramCard, Integer>) cardAndScore).getKey();
            int cardScore = ((Map.Entry<ProgramCard, Integer>) cardAndScore).getValue();
            System.out.println(card.getCommand() + ", score: " + cardScore);
            if (cardScore == 100) { continue;}
            List<Object> finalPosAndDir = getPosFromCardMove(card, newRobotPos, newRobotDir);
            newRobotPos = (Position) finalPosAndDir.get(0);
            newRobotDir = (Direction) finalPosAndDir.get(1);
            if (nextGoalPos.equals(newRobotPos)) { flagsVisitedInRound++; }
            return card;
        }
        return null;
    }


    private Object[] getBestCardsOrdered() {
        HashMap<ProgramCard, Integer> cardAndScore = new HashMap<>();
        ArrayList<ProgramCard> checkedCards = new ArrayList<>();
        System.out.println("Available cards left: " + availableCardsLeft);
        for (ProgramCard card : availableCardsLeft) {
            if (isCardChecked(checkedCards, card)) {
                continue;
            }
            checkedCards.add(card);
            System.out.println("Finding score for " + card.getCommand());
            if (isCardUseless(card, newRobotPos, newRobotDir)) {
                System.out.println("Using " + card.getCommand() + " at pos " + newRobotPos
                        + " with dir " + newRobotDir + " is useless\n");
                cardAndScore.put(card, 100);
                continue;
            }
            List<Object> finalPosAndDir = getPosFromCardMove(card, newRobotPos, newRobotDir);
            Position finalPos = (Position) finalPosAndDir.get(0);
            Direction finalDir = (Direction) finalPosAndDir.get(1);
            int cardScore = getScore(finalPos, finalDir, nextGoalFlag);
            System.out.println("Using " + card.getCommand() + " at pos " + newRobotPos
                    + " with dir " + newRobotDir + " moves the robot to pos " + finalPos
                    + " facing dir " + finalDir + " which has a score of " + cardScore + "\n");
            cardAndScore.put(card, cardScore);
        }

        Object[] a = cardAndScore.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<ProgramCard, Integer>) o1).getValue()
                        .compareTo(((Map.Entry<ProgramCard, Integer>) o2).getValue());
            }
        });

        return a;

    }

    private int getScore (Position pos, Direction dir, int goalFlag){
        ArrayList<List<Object>> flagPosScores =  flagPositionScores.get(goalFlag);
        Position goalPos = (Position) flagPosScores.get(0).get(0);
        int score =  getDistanceAway(pos, goalPos);
        if(!isPosCloserToGoal(pos, pos.getPositionIn(dir), goalPos)){score++;}
        if(difficulty.equals(Difficulty.MEDIUM) || difficulty.equals(Difficulty.HARD)){return score;}

        for (List<Object> flagPosScore : flagPosScores) {
            Position mapPos = (Position) flagPosScore.get(0);
            if (pos.equals(mapPos)) {
                score = (Integer) flagPosScore.get(1);
                if(!isPosCloserToGoal(pos, pos.getPositionIn(dir), goalPos)){score++;}
                break;
            }
        }
        return score;
    }




    private void pickOptimal(){
        int goalFlag = updateGoalFlag(checkpointsVisited);
        int score = getScore(newRobotPos, newRobotDir, goalFlag);
        ArrayList<ArrayList<List<Object>>> path = new ArrayList<>();
        ArrayList<ProgramCard> chosenCards = new ArrayList<>();
        int[] totalScore = {goalFlag, 0, score};


        List<Object> startNode = Arrays.asList(newRobotPos, newRobotDir, totalScore, chosenCards, playerHandDeck, goalFlag);
        ArrayList<List<Object>> startFrontier = new ArrayList<>();
        startFrontier.add(startNode);
        path.add(startFrontier);

        int counter = 1;
        int[] bestFinalTotalScore = {goalFlag, 100, 100};
        int firstNodeWithFiveCards = 0;

         for (int i = 0; i < path.size() ; i++) {
             ArrayList<List<Object>> frontier = path.get(i);
             for (List<Object> frontierNode : frontier) {

                 ArrayList<ProgramCard> currentChosenCards = new ArrayList<ProgramCard>((ArrayList) frontierNode.get(3));
                 if (currentChosenCards.size() == 4 && firstNodeWithFiveCards == 0) {
                     firstNodeWithFiveCards = counter - 1;
                 }
                 if (currentChosenCards.size() == 5) {
                     break;
                 }

                 int[] currentTotalScore = (int[]) frontierNode.get(2);
                 int currentGoalFlag = currentTotalScore[0];
                 Position currentGoalPos = logicGrid.getFlagPositions().get(currentGoalFlag);
                 Position currentPos = (Position) frontierNode.get(0);
                 Direction currentDir = (Direction) frontierNode.get(1);
                 ArrayList<ProgramCard> currentAvailableCards = new ArrayList<ProgramCard>((ArrayList) frontierNode.get(4));

                 if (currentPos.equals(currentGoalPos)) {
                     currentGoalFlag = updateGoalFlag(currentGoalFlag + 1);
                     currentTotalScore[0] = currentGoalFlag;
                     currentTotalScore[1] = currentChosenCards.size();
                     currentTotalScore[2] = getScore(currentPos, currentDir, currentGoalFlag);
                 }

                 ArrayList<List<Object>> newFrontier = new ArrayList<>();
                 ArrayList<ProgramCard> checkedCards = new ArrayList<>();
                 int bestNodeScore = 100;

                 for (int k = 0; k < currentAvailableCards.size(); k++) {
                     ProgramCard card = currentAvailableCards.get(k);
                     if (isCardChecked(checkedCards, card)) {
                         continue;
                     }
                     checkedCards.add(card);
                     if (isCardUseless(card, currentPos, currentDir)) {
                         continue;
                     }

                     List<Object> node = getNode(currentPos, currentDir, card, currentTotalScore);
                     int[] nodeTotalScore = (int[]) node.get(2);
                     int nodeScore = nodeTotalScore[2];
                     int nodeGoalFlag = nodeTotalScore[0];

                     if (nodeScore < bestNodeScore && nodeGoalFlag == currentTotalScore[0]) {
                         bestNodeScore = nodeScore;
                     }
                     newFrontier.add(createList(node, card, currentChosenCards, currentAvailableCards, goalFlag));
                 }


                 for (int k = 0; k < newFrontier.size(); k++) {
                     ArrayList<ProgramCard> cardsInFrontier = (ArrayList) newFrontier.get(k).get(3);
                     int[] nodeTotalScore = (int[]) newFrontier.get(k).get(2);
                     int nextFlag = nodeTotalScore[0];
                     int movesToFlag = nodeTotalScore[1];
                     int nodeScore = nodeTotalScore[2];
                     if ((nodeScore > bestNodeScore) && nodeScore > currentTotalScore[2]) {
                         newFrontier.remove(k);
                         k--;
                     }
                     if (cardsInFrontier.size() == 5 &&
                             nextFlag >= bestFinalTotalScore[0] &&
                             movesToFlag <= bestFinalTotalScore[1] &&
                             nodeScore < bestFinalTotalScore[2]) {
                         bestFinalTotalScore = nodeTotalScore;
                     }
                 }
                 path.add(newFrontier);
                 counter++;
             }
        }
         for (int i = firstNodeWithFiveCards; i < path.size(); i++) {
             ArrayList<List<Object>> paths = path.get(i);
             for (List<Object> objects : paths) {
                 ArrayList<ProgramCard> fiveCards = new ArrayList<>((ArrayList<ProgramCard>) objects.get(3));
                 totalScore = (int[]) objects.get(2);
                 if (totalScore != bestFinalTotalScore) { continue; }
                 addCardsToSelectedCards(fiveCards);
                 break;
             }
        }
    }

    private List<Object> getNode(Position pos, Direction dir, ProgramCard card, int[] totalScore){
        List<Object> finalPosAndDir = getPosFromCardMove(card, pos, dir);
        Position finalPos = (Position) finalPosAndDir.get(0);
        Direction finalDir = (Direction) finalPosAndDir.get(1);
        int score = getScore(finalPos, finalDir, totalScore[0]);
        int[] newTotalScore = {totalScore[0], totalScore[1], score};
        return Arrays.asList(finalPos, finalDir, newTotalScore);
    }

    private List<Object> createList(List<Object> node, ProgramCard card, ArrayList<ProgramCard> cardsChosen,
                                    ArrayList<ProgramCard> cardsLeft, int roundStartGoalFlag){
        Position nodePos = (Position) node.get(0);
        Direction nodeDir = (Direction) node.get(1);
        int[] nodeTotalScore = (int[]) node.get(2);
        ArrayList<ProgramCard> nodeChosenCards = new ArrayList<>(cardsChosen);
        ArrayList<ProgramCard> nodeAvailableCards = new ArrayList<>(cardsLeft);
        nodeChosenCards.add(card);
        nodeAvailableCards.remove(card);
        if(nodeTotalScore[0] == roundStartGoalFlag) {nodeTotalScore[1] = nodeChosenCards.size();}
        return Arrays.asList(nodePos, nodeDir, nodeTotalScore, nodeChosenCards, nodeAvailableCards);
    }













    /*
    All methods below are just simple helper methods.
     */




    private boolean isCardChecked(ArrayList<ProgramCard> checkedCards, ProgramCard card){
        for (ProgramCard checkedCard : checkedCards) {
            if (checkedCard.getCommand().equals(card.getCommand())) return true;
        }
        return false;
    }

    private boolean isCardUseless(ProgramCard card, Position pos, Direction dir){
        List<Object> finalPosAndDir = getPosFromCardMove(card, pos, dir);
        Position newPos = (Position) finalPosAndDir.get(0);
        Direction newDir = (Direction) finalPosAndDir.get(1);
        if(pos.equals(newPos) && dir.equals(newDir)){ return true;}
        return isDeadMove(newPos);
    }

    private int updateGoalFlag(int flagGoal){
        int goalFlag = flagGoal;
        if (goalFlag > logicGrid.getFlagPositions().size() - 1) {
            goalFlag = logicGrid.getFlagPositions().size() - 1;
        }
        return goalFlag;
    }

    private ProgramCard getFirstAvailableCard(){
        for (ProgramCard programCard : playerHandDeck) {
            if (isCardAvailable(programCard)) {
                return programCard;
            }
        }
        return null;
    }


    /**
     * Checks if a position is closer to current goal than an other position
     *
     * @param oldPos the old position
     * @param newPos the new position
     * @return true if the new position is closer to goal, false otherwise
     */
    private boolean isPosCloserToGoal(Position oldPos, Position newPos, Position goalPos) {
        if(oldPos == null || newPos == null || goalPos == null){
            System.out.println("Error: Couldn't calculate distance from goal");
            return false;
        }
        int oldDistanceAway = getDistanceAway(goalPos, oldPos);
        int newDistanceAway = getDistanceAway(goalPos, newPos);
        return (newDistanceAway < oldDistanceAway);
    }

    /**
     * Check how far away two positions are
     *
     * @param pos1 position 1
     * @param pos2 position 2
     * @return sum of difference of x pos and y pos of the two positions (manhattan distance)
     */
    private int getDistanceAway(Position pos1, Position pos2) {
        return (Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY()));
    }


    /**
     * Find the final position and direction at the end of the phase (after conveyorbelts and cogs move)
     *
     * @param pos             the position after a card move
     * @param dir             the direction after a card move
     * @param expressBeltMove always false if calling this method from outside of this method
     * @return the final position at the end of the phase
     */
    private List<Object> findFinalPosAndDir(Position pos, Direction dir, boolean expressBeltMove) {
        if (isDeadMove(pos)) {
            return Arrays.asList(pos, dir);
        }
        Position finalPos = pos;
        Direction finalDir = dir;

        if (!logicGrid.positionIsFree(pos, 5)) {
            ExpressBeltPiece expressBelt = logicGrid.getPieceType(pos, ExpressBeltPiece.class);
            if (expressBelt.isTurn()) {
                if (expressBelt.isTurnRight()) {
                    finalDir = finalDir.getRightTurnDirection();
                } else {
                    finalDir = finalDir.getLeftTurnDirection();
                }
            }
            finalPos = finalPos.getPositionIn(expressBelt.getDir());
            if (!logicGrid.positionIsFree(finalPos, 5) && !expressBeltMove) {
                findFinalPosAndDir(finalPos, finalDir, true);
            }
        }

        if (!logicGrid.isInBounds(finalPos)) {
            return Arrays.asList(finalPos, finalDir);
        }
        if (!logicGrid.positionIsFree(finalPos, 4) && !expressBeltMove) {
            ConveyorBeltPiece conveyorBelt = logicGrid.getPieceType(finalPos, ConveyorBeltPiece.class);
            if (conveyorBelt.isTurn()) {
                if (conveyorBelt.isTurnRight()) {
                    finalDir = finalDir.getRightTurnDirection();
                } else {
                    finalDir = finalDir.getLeftTurnDirection();
                }
            }
            finalPos = finalPos.getPositionIn(conveyorBelt.getDir());
        }

        if (!logicGrid.isInBounds(finalPos)) {
            return Arrays.asList(finalPos, finalDir);
        }
        if (!logicGrid.positionIsFree(finalPos, 6)) {
            CogPiece cog = logicGrid.getPieceType(finalPos, CogPiece.class);
            if (cog.isRotateClockwise()) {
                finalDir = finalDir.getRightTurnDirection();
            } else {
                finalDir = finalDir.getLeftTurnDirection();
            }
        }
        return Arrays.asList(finalPos, finalDir);
    }

    private List<Object> getPosFromCardMove(ProgramCard card, Position pos, Direction dir) {
        int moves = Math.abs(card.getMovement());
        Position newPos = pos;
        Direction moveDir = dir;
        if(card.getCommand().equals(CardType.BACKUP)) moveDir = dir.getOppositeDirection();

        for (int i = 0; i < moves; i++) {
            if (isDeadMove(newPos.getPositionIn(moveDir))) {
                return findFinalPosAndDir(newPos.getPositionIn(moveDir), dir, false);
            }
            if(!isLegalMoveInDirection(newPos, moveDir)){
                return findFinalPosAndDir(newPos, dir, false);
            }
            newPos = newPos.getPositionIn(moveDir);
        }
        Direction newDir = dir.getCardTurnDirection(card.getCommand());
        return findFinalPosAndDir(newPos, newDir, false);
    }
}
