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
import inf112.skeleton.app.grid_objects.LaserPiece;

import java.util.*;


public class AIPlayer extends Player {
    private Position newRobotPos;
    private Direction newRobotDir;
    private int newRobotDamage;
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
    public enum Difficulty{EASY, MEDIUM, HARD, EXPERT, TESTING}
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
        this.newRobotDamage = getDamage();
        this.nextGoalFlag = checkpointsVisited;
        this.nextGoalPos = logicGrid.getFlagPositions().get(nextGoalFlag);


        if(difficulty.equals(Difficulty.TESTING)){
            createTestPlayer();
        }

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
        System.out.println(toString() + difficulty + " playerhand: " + playerHandDeck);
        System.out.println(toString() + difficulty +" chose " + Arrays.toString(getSelectedCards()) + "\n");
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
            if(logicGrid.isDeadMove(finalPos)
            || newRobotDamage + getLaserDamage(finalPos) >= 10){ continue; }
            newRobotPos = finalPos;
            newRobotDir = finalDir;
            newRobotDamage = getLaserDamage(finalPos);
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
            nextGoalFlag = updateGoalFlag(nextGoalFlag+flagsVisitedInRound);
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
            if (cardScore == 100) { continue;}
            List<Object> finalPosAndDir = getPosFromCardMove(card, newRobotPos, newRobotDir);
            newRobotPos = (Position) finalPosAndDir.get(0);
            newRobotDir = (Direction) finalPosAndDir.get(1);
            newRobotDamage += getLaserDamage(newRobotPos);
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
            if (isCardUseless(card, newRobotPos, newRobotDir, newRobotDamage)) {
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
        int goalFlag = updateGoalFlag(nextGoalFlag);
        int score = getScore(newRobotPos, newRobotDir, goalFlag);
        int damage = getDamage();
        ArrayList<ArrayList<List<Object>>> path = new ArrayList<>();
        ArrayList<ProgramCard> chosenCards = new ArrayList<>();
        int[] totalScore = {goalFlag, 0, score};
        List<Object> startNode = Arrays.asList(newRobotPos, newRobotDir, totalScore, chosenCards, playerHandDeck, damage);
        ArrayList<List<Object>> startFrontier = new ArrayList<>();
        startFrontier.add(startNode);
        path.add(startFrontier);
        int[] bestFinalTotalScore = {goalFlag, 100, 100};

          for (int i = 0; i < path.size() ; i++) {
             ArrayList<List<Object>> frontier = path.get(i);
             for (List<Object> frontierNode : frontier) {

                 ArrayList<ProgramCard> currentChosenCards = new ArrayList<ProgramCard>((ArrayList) frontierNode.get(3));
                 if (currentChosenCards.size() == cardsToPick) { break; }
                 int[] currentTotalScore = (int[]) frontierNode.get(2);
                 Position currentPos = (Position) frontierNode.get(0);
                 Direction currentDir = (Direction) frontierNode.get(1);
                 int currentDamage = (int) frontierNode.get(5);
                 ArrayList<ProgramCard> currentAvailableCards = new ArrayList<ProgramCard>((ArrayList) frontierNode.get(4));
                 ArrayList<List<Object>> newFrontier = new ArrayList<>();
                 ArrayList<ProgramCard> checkedCards = new ArrayList<>();
                 int bestNodeScore = 100;

                 for (int k = 0; k < currentAvailableCards.size(); k++) {
                     ProgramCard card = currentAvailableCards.get(k);
                     if (isCardChecked(checkedCards, card)) {
                         continue;
                     }
                     checkedCards.add(card);
                     if (isCardUseless(card, currentPos, currentDir, currentDamage)) {
                         continue;
                     }
                     newFrontier.add(createNode(currentPos, currentDir, card, currentTotalScore,
                             currentChosenCards, currentAvailableCards, goalFlag, currentDamage));
                 }

                 for (int k = 0; k < newFrontier.size(); k++) {
                     ArrayList<ProgramCard> cardsInFrontier = (ArrayList) newFrontier.get(k).get(3);
                     int[] nodeTotalScore = (int[]) newFrontier.get(k).get(2);
                     int nodeScore = nodeTotalScore[2];
                     int flag = nodeTotalScore[0];
                     if (nodeScore < bestNodeScore) {
                         bestNodeScore = score;
                     }
                     if ((nodeScore > bestNodeScore) && nodeScore > currentTotalScore[2] &&
                     flag <= currentTotalScore[0]) {
                         newFrontier.remove(k);
                         k--;
                     }
                     if (cardsInFrontier.size() == cardsToPick && isCurrentBetter(bestFinalTotalScore, nodeTotalScore)){
                         bestFinalTotalScore = nodeTotalScore;
                         chosenCards = cardsInFrontier;
                     }
                 }
                 path.add(newFrontier);
             }
          }
          addCardsToSelectedCards(chosenCards);
    }

    /**
     * Creates a node for a card that may be added to the frontier
     * @param pos the current position from the previous node
     * @param dir the current direction from the previous node
     * @param card ProgramCard for the node
     * @param totalScore the total score from the previous node
     * @param cardsChosen the ProramCards chosen from the previous node
     * @param cardsLeft the available ProgramCards left from the previous node
     * @param roundStartGoalFlag the goal flag number at round start
     * @return the new node created with updated position, direction, total score, and lists of cards from using the card
     */
    private List<Object> createNode(Position pos, Direction dir, ProgramCard card, int[] totalScore,
                                    ArrayList<ProgramCard> cardsChosen, ArrayList<ProgramCard> cardsLeft,
                                    int roundStartGoalFlag, int damage){
        List<Object> finalPosAndDir = getPosFromCardMove(card, pos, dir);
        Position finalPos = (Position) finalPosAndDir.get(0);
        Direction finalDir = (Direction) finalPosAndDir.get(1);
        int newDamage = damage + getLaserDamage(finalPos);
        ArrayList<ProgramCard> nodeChosenCards = new ArrayList<>(cardsChosen);
        ArrayList<ProgramCard> nodeAvailableCards = new ArrayList<>(cardsLeft);
        nodeChosenCards.add(card);
        nodeAvailableCards.remove(card);
        int score = getScore(finalPos, finalDir, totalScore[0]);
        int goalFlag = totalScore[0];
        int moves = totalScore[1];
        Position goalPos = logicGrid.getFlagPositions().get(goalFlag);
        if(goalFlag == roundStartGoalFlag){
            moves = nodeChosenCards.size();
        }

        if (finalPos.equals(goalPos)) {
            goalFlag = updateGoalFlag(goalFlag + 1);
            score = getScore(finalPos, finalDir, goalFlag);
            moves = nodeChosenCards.size();
        }
        int[] newTotalScore = {goalFlag, moves , score};
        return Arrays.asList(finalPos, finalDir, newTotalScore, nodeChosenCards, nodeAvailableCards, newDamage);
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
    private boolean isCardUseless(ProgramCard card, Position pos, Direction dir, int damage){
        List<Object> finalPosAndDir = getPosFromCardMove(card, pos, dir);
        Position newPos = (Position) finalPosAndDir.get(0);
        Direction newDir = (Direction) finalPosAndDir.get(1);
        if(pos.equals(newPos) && dir.equals(newDir)){ return true;}
        if(damage + getLaserDamage(newPos) >= 10){
            System.out.println("Card is useless at " + pos + ", new pos: " + newPos + " damage: " + damage + " laser damage: " + getLaserDamage(newPos));
            return true;}
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

    /**
     * Checks if the current total score for a node is better than the best total score found so far
     * @param currentBest the current best total score for any node throughout the search
     * @param current the total score for the current node
     * @return true if the total score for the current node is better, false otherwise.
     */
    private boolean isCurrentBetter(int[] currentBest, int[] current){
        if (current[0] < currentBest[0]){
            return false;
        }
        if(current[0] == currentBest[0]){
            if(current[1] > currentBest[1]){
                return false;
            }
            if(current[1] == currentBest[1]){
                return current[2] < currentBest[2];
            }
        }
        return true;
    }


    private int getLaserDamage(Position pos){
        if(!logicGrid.isInBounds(pos))return  0;
        if(!logicGrid.positionIsFree(pos,3)) return 0;
        int damage = 0;
        if (logicGrid.getPieceType(pos, LaserPiece.class) != null){
            damage++;
            LaserPiece laser = logicGrid.getPieceType(pos, LaserPiece.class);
            if (laser.isDoubleLaser() || laser.isCrossingLasers()) {
                damage++;
            }
        }
        return damage;
    }

    private void evaluatePowerDown(){
        if (getDamage() < 5)return;
        int random =(int) Math.random()*10;
        if(random < getDamage()){
            setPowerDownMode(true);
        }
    }

    public Position chooseRespawnPos(ArrayList<Position> positions){
        if(difficulty.equals(Difficulty.EASY)){
            int random  = (int) Math.random()*positions.size();
            return (positions.get(random));
        }
        Position bestPos = positions.get(0);
        int bestScore = 100;
        for (int i = 1; i < positions.size() ; i++) {
            Position pos = positions.get(i);
             chooseRespawnDir(pos);
             int score =getScore(pos, chooseRespawnDir(pos), nextGoalFlag);
             if( score < bestScore){
                 //System.out.println("Pos " + pos + " score " + score + " is better than current best "
                 //+ bestPos + " score " + bestScore);
                 bestScore = score;
                 bestPos = pos;
             }
             else{
                 //System.out.println("Pos " + pos + " score " + score + " is NOT better than current best "
                 //      + bestPos + " score " + bestScore);
             }
        }
        //System.out.println(toString() + " Best respawn pos is " + bestPos);
        return bestPos;
    }

    public Direction chooseRespawnDir(Position pos){
        //System.out.println("Choosing best dir for " + toString() + " at " + pos);
        Direction bestDir = getPlayerPiece().getDir();
        int bestScore = 100;
        for(Direction dir : Direction.values()){
            Position posInFront = pos.getPositionIn(dir);
            if(logicGrid.isDeadMove(posInFront)){
               // System.out.println("Pos in front will result in death");
                continue;
            }
            int score = getScore(posInFront, dir, nextGoalFlag);
            if(score < bestScore){
                bestScore = score;
                //System.out.println("Dir " + dir + " is better than current best dir " + bestDir);
                bestDir = dir;
            }
            else {
                //System.out.println("Dir " + dir + " is NOT better than current best dir " + bestDir);
            }
        }
        //System.out.println(toString() + " Best respawn dir is " + bestDir);
        return bestDir;
    }

    private void createTestPlayer(){
        if(getPlayerNumber() == 2) this.difficulty = Difficulty.MEDIUM;
        else if(getPlayerNumber() == 3)this.difficulty = Difficulty.HARD;
        else if(getPlayerNumber() == 4)this.difficulty = Difficulty.EXPERT;
        else this.difficulty = Difficulty.EASY;
        ArrayList<ProgramCard> hand = new ArrayList<>();
        hand.add(new ProgramCard(1, CardType.MOVE1));
        hand.add(new ProgramCard(2, CardType.MOVE1));
        hand.add(new ProgramCard(3, CardType.MOVE2));
        hand.add(new ProgramCard(4, CardType.ROTATERIGHT));
        hand.add(new ProgramCard(5, CardType.ROTATELEFT));
        hand.add(new ProgramCard(6, CardType.BACKUP));
        hand.add(new ProgramCard(7, CardType.UTURN));
        hand.add(new ProgramCard(8, CardType.UTURN));
        hand.add(new ProgramCard(9, CardType.ROTATELEFT));
        playerHandDeck = hand;
        nextGoalFlag = getPlayerNumber()-1;
        nextGoalPos = logicGrid.getFlagPositions().get(nextGoalFlag);
        availableCardsLeft = playerHandDeck;
    }
}

