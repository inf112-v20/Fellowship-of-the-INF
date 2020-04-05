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
        this.playerHandDeck = getPlayerHandDeck();

    }

    /**
     * Pick cards for computer player depending on what difficult they are set to
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

    /**
     * Picks a random card for each phase as long as it doesn't put the robot
     * in a position at the end of the phase that will kill it.
     * Is used for picking cards at easy difficulty.
     */
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
            if(logicGrid.isDeadMove(finalPos)){ continue; }
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

    /**
     * Picks the card for a phase that has the best score for that phase.
     * I.e for each phase pick the card that will put the robot closest to the goal.
     * Is used for picking cards at medium difficulty.
     */
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


    /**
     * Get the best card for a phase
     * @return the card with best score (i.e lowest score)
     */
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


    /**
     * Sort all unique available cards left in the playerhand by their score.
     * If a score = 100, then that card will result in death or make the robot stand still
     * (no change in position or direction).
     * @return Sorted array with keysets, with the key being ProgramCards
     * and the value is the score for that card. Sorted from best to worst score.
     */
    private Object[] getBestCardsOrdered() {
        HashMap<ProgramCard, Integer> cardAndScore = new HashMap<>();
        ArrayList<ProgramCard> checkedCards = new ArrayList<>();
        for (ProgramCard card : availableCardsLeft) {
            if (isCardChecked(checkedCards, card)) {
                continue;
            }
            checkedCards.add(card);
            if (isCardUseless(card, newRobotPos, newRobotDir)) {
                cardAndScore.put(card, 100);
                continue;
            }
            List<Object> finalPosAndDir = getPosFromCardMove(card, newRobotPos, newRobotDir);
            Position finalPos = (Position) finalPosAndDir.get(0);
            Direction finalDir = (Direction) finalPosAndDir.get(1);
            int cardScore = getScore(finalPos, finalDir, nextGoalFlag);
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

    /**
     * Gets the score for a position depending how close it is to the goal
     * This is the only difference between hard and expert difficulty.
     * Hard difficulty uses manhattan score, which can be misleading (due to walls and holes).
     * Expert difficulty knows exactly how far away a position is from the goal (factoring in holes and walls.
     * @param pos the position to get the score for
     * @param dir the direction of the robot (affects the score too)
     * @param goalFlag the current goal flag
     * @return the score calculated for a position with the direction given
     */
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


    /**
     * Picks the cards that will put the robot at the position with the best score at round end.
     * The search for the best cards is guided by ignoring cards that don't do anything (e.g. moving into walls)
     * and cards that will end up killing the robot. It focuses first on cards that have at least equal or better score
     * than the current score.
     * Is used for picking cards at hard and expert difficulty.
     */
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

        int[] bestFinalTotalScore = {goalFlag, 100, 100};

          for (int i = 0; i < path.size() ; i++) {
             ArrayList<List<Object>> frontier = path.get(i);
             for (List<Object> frontierNode : frontier) {

                 ArrayList<ProgramCard> currentChosenCards = new ArrayList<ProgramCard>((ArrayList) frontierNode.get(3));

                 if (currentChosenCards.size() == cardsToPick) {
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

                     List<Object> posAndScore = getFinalPosAndScore(currentPos, currentDir, card, currentTotalScore);
                     totalScore = (int[]) posAndScore.get(2);
                     goalFlag = totalScore[0];
                     score = totalScore[2];

                     if (score < bestNodeScore && goalFlag == currentTotalScore[0]) {
                         bestNodeScore = score;
                     }
                     newFrontier.add(createNode(posAndScore, card, currentChosenCards, currentAvailableCards, goalFlag));
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
                     if (cardsInFrontier.size() == cardsToPick &&
                             nextFlag >= bestFinalTotalScore[0] &&
                             movesToFlag <= bestFinalTotalScore[1] &&
                             nodeScore < bestFinalTotalScore[2]) {
                         bestFinalTotalScore = nodeTotalScore;
                     }
                 }
                 path.add(newFrontier);
             }
        }
          Collections.reverse(path);
         a :for (ArrayList<List<Object>> paths : path) {
             for (List<Object> objects : paths) {
                 ArrayList<ProgramCard> bestCards = new ArrayList<>((ArrayList<ProgramCard>) objects.get(3));
                 totalScore = (int[]) objects.get(2);
                 if (totalScore != bestFinalTotalScore) {
                     continue;
                 }
                 addCardsToSelectedCards(bestCards);
                 break a;
             }
         }
    }

    /**
     * Gets the final position and direction from using a card.
     * Calculates the score for this new position and direction.
     * @param pos the current (node) position
     * @param dir the current (node) direction
     * @param card the card to check
     * @param totalScore the current (node) total score. [0] = current goalFlag,
     * [1] = moves used to get to flag (if flag is reached in a round), [2] current score for current position
     * @return list of new postion, direction, and the total score for the new position and direction.
     */
    private List<Object> getFinalPosAndScore(Position pos, Direction dir, ProgramCard card, int[] totalScore){
        List<Object> finalPosAndDir = getPosFromCardMove(card, pos, dir);
        Position finalPos = (Position) finalPosAndDir.get(0);
        Direction finalDir = (Direction) finalPosAndDir.get(1);
        int score = getScore(finalPos, finalDir, totalScore[0]);
        int[] newTotalScore = {totalScore[0], totalScore[1], score};
        return Arrays.asList(finalPos, finalDir, newTotalScore);
    }

    /**
     * Creates a node for a card that may be added to the frontier.
     * @param posDirAndScore Position, direction, and total score for the node.
     * @param card ProgramCard for the node
     * @param cardsChosen The ProgramCards chosen from the previous node.
     * @param cardsLeft The ProgramCards left to choose from the previous node.
     * @param roundStartGoalFlag The goalFlag number at the start of the round.
     * @return the new node created with updated list of cards.
     */
    private List<Object> createNode(List<Object> posDirAndScore, ProgramCard card, ArrayList<ProgramCard> cardsChosen,
                                    ArrayList<ProgramCard> cardsLeft, int roundStartGoalFlag){
        Position nodePos = (Position) posDirAndScore.get(0);
        Direction nodeDir = (Direction) posDirAndScore.get(1);
        int[] nodeTotalScore = (int[]) posDirAndScore.get(2);
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


    /**
     * Checks if a ProgramCard is already in a list of ProgramCards.
     * @param checkedCards the list of ProgramCards to check
     * @param card the ProgramCard to check
     * @return true if it is already in the list, false otherwise
     */
    private boolean isCardChecked(ArrayList<ProgramCard> checkedCards, ProgramCard card){
        for (ProgramCard checkedCard : checkedCards) {
            if (checkedCard.getCommand().equals(card.getCommand())) return true;
        }
        return false;
    }

    /**
     * Checks if card will be useless to pick in a phase.
     * A card is useless if it will result in death or make no change in position and direction.
     * @param card the ProgramCard to check
     * @param pos the current position
     * @param dir the current direction
     * @return true if the card is useless, false otherwise.
     */
    private boolean isCardUseless(ProgramCard card, Position pos, Direction dir){
        List<Object> finalPosAndDir = getPosFromCardMove(card, pos, dir);
        Position newPos = (Position) finalPosAndDir.get(0);
        Direction newDir = (Direction) finalPosAndDir.get(1);
        if(pos.equals(newPos) && dir.equals(newDir)){ return true;}
        return logicGrid.isDeadMove(newPos);
    }

    /**
     * Updates the current goal flag number.
     * Handles potential error when the current number is higher than the number of flags in the game.
     * @param flagGoal the new goal flag number
     * @return the new goal flag number
     */
    private int updateGoalFlag(int flagGoal){
        int goalFlag = flagGoal;
        if (goalFlag > logicGrid.getFlagPositions().size() - 1) {
            goalFlag = logicGrid.getFlagPositions().size() - 1;
        }
        return goalFlag;
    }

    /**
     * Gets the first available card in the playerHandDeck that is not already chosen.
     * @return the first card available in the playerHandDeck.
     */
    private ProgramCard getFirstAvailableCard(){
        for (ProgramCard programCard : playerHandDeck) {
            if (!isCardChecked(chosenCards, programCard)) {
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
        if (logicGrid.isDeadMove(pos)) {
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

    /**
     * Gets the final position and direction after using a card and board elements are moved.
     * i.e phase end position and direction.
     * @param card the card to check
     * @param pos the current position
     * @param dir the current direction
     * @return the final position and direction.
     */
    private List<Object> getPosFromCardMove(ProgramCard card, Position pos, Direction dir) {
        int moves = Math.abs(card.getMovement());
        Position newPos = pos;
        Direction moveDir = dir;
        if(card.getCommand().equals(CardType.BACKUP)) moveDir = dir.getOppositeDirection();

        for (int i = 0; i < moves; i++) {
            if (logicGrid.isDeadMove(newPos.getPositionIn(moveDir))) {
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
