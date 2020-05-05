package inf112.skeleton.app.player;

import inf112.skeleton.app.cards.CardType;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.game_logic.Game;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;

import java.util.*;

/**
 * Brief description of the different levels of AI:
 * -Easy: Will pick random cards
 * -Medium: Will always pick the best available card for that phase.
 * Does not look at how good the position is at the of the round, just how good it is at the end of the phase.
 * -Hard: Will pick the cards that will put them closest to the goal at the end of the round.
 * When calculating how far a position is from the goal it uses manhattan distance (sum of absolute value of differences of x and y values of two positions).
 * The manhattan distance at the round end positions doesn't factor in holes and walls, so it can mislead the robot in a map with many walls and holes.
 * -Expert: Will pick the cards that will put them closest to the goal at the end of the round.
 * When calculating how far a position is from the goal it knows exactly how many moves it takes to reach the goal from there,
 * even factoring in holes and walls. It will always choose the most optimal route (or cards rather).
 *
 * AI's at every difficulty will avoid picking cards that are useless (if it is possible), i.e. cards that will either kill them
 * or cards that won't change the direction or position of the robot.
 * AI's at every difficulty will evaluate powerdown at round start if they have 5 or more damage. The percentage chance
 * to powerdown is directly tied to the amount of current damage, i.e. 5 damage = 50% to powerdown, 6 = 60% and so on.
 *
 * When checking how good a card is, it checks what position and direction the player will end up on at the end of the
 * phase (after conveyorbelts, pushers, cogs etc. are activated) by using that card.
 * It will not check if it will be pushed by other players or not.
 * The card is given a score by how good that position and direction is relative to the next goal flag position.
 * No AI will factor in the locked cards if there is any.
 *
 * The AI differ in how they choose respawn direction and position as well.
 */
public class AIPlayer extends Player {
    private Position newRobotPos;
    private Direction newRobotDir;
    private int newRobotDamage;
    private int flagsVisitedInRound = 0;
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
    private Game game;


    public AIPlayer(int playerNumber, Game game, Difficulty difficulty) {
        super(playerNumber, game);
        this.game = game;
        this.logicGrid = game.getLogicGrid();
        this.flagPositionScores = logicGrid.getFlagPositionScores();
        this.difficulty = difficulty;
        this.playerHandDeck = getPlayerHandDeck();
        checkIfTestMap();

    }

    /**
     * Pick cards for computer player depending on what difficult they are set to
     */
    public void pickCards() {
        this.playerHandDeck = getPlayerHandDeck();
        this.cardsToPick = 5 - getLockedCards().size();
        this.chosenCards = new ArrayList<>();
        this.availableCardsLeft = new ArrayList<>(playerHandDeck);
        this.newRobotPos = getPos();
        this.newRobotDir = getPlayerPiece().getDir();
        this.newRobotDamage = getDamage();
        this.nextGoalFlag = getCheckpointsVisited();
        this.nextGoalPos = logicGrid.getFlagPositions().get(nextGoalFlag);

        if(difficulty.equals(Difficulty.TESTING)){
            createTestPlayer();
        }
        checkIfTestMap();
        evaluatePowerDown();
        if(isPowerDownMode())return;

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
            int phaseNr = chosenCards.size() + 1;
            List<Object> finalPosAndDir =  getPosFromCardMove(card, newRobotPos, newRobotDir, phaseNr);
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
            int cardsMissing = cardsToPick - chosenCards.size();
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
            int phaseNr = i+1;
            ProgramCard bestCard = getBestCard(phaseNr);
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
     * @param phaseNr the phaseNr of the phase we are currently finding the best card for
     * @return the card with best score (i.e lowest score)
     */
    private ProgramCard getBestCard(int phaseNr) {
        Object[] bestMovesInOrder = getBestCardsOrdered(phaseNr);
        for (Object cardAndScore : bestMovesInOrder) {
            ProgramCard card = ((Map.Entry<ProgramCard, Integer>) cardAndScore).getKey();
            int cardScore = ((Map.Entry<ProgramCard, Integer>) cardAndScore).getValue();
            if (cardScore == 100) { continue;}
            List<Object> finalPosAndDir = getPosFromCardMove(card, newRobotPos, newRobotDir, phaseNr);
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
     * @param phaseNr the phaseNr of the phase we are currently finding the best card for
     * @return Sorted array with keysets, with the key being ProgramCards
     * and the value is the score for that card. Sorted from best to worst score.
     */
    private Object[] getBestCardsOrdered(int phaseNr) {
        HashMap<ProgramCard, Integer> cardAndScore = new HashMap<>();
        ArrayList<ProgramCard> checkedCards = new ArrayList<>();
        for (ProgramCard card : availableCardsLeft) {
            if (isCardTypeChecked(checkedCards, card)) {
                continue;
            }
            checkedCards.add(card);
            if (isCardUseless(card, newRobotPos, newRobotDir, newRobotDamage, phaseNr)) {
                cardAndScore.put(card, 100);
                continue;
            }
            List<Object> finalPosAndDir = getPosFromCardMove(card, newRobotPos, newRobotDir, phaseNr);
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
     * Expert difficulty knows exactly how far away a position is from the goal (factoring in holes and walls).
     * Expert uses the createScoreForPositions method in LogicGrid.java to find the exact score for each position.
     * @param pos the position to get the score for
     * @param dir the direction of the robot (affects the score too)
     * @param goalFlag the current goal flag
     * @return the score calculated for a position with the direction given
     */
    private int getScore (Position pos, Direction dir, int goalFlag){
        Position goalPos = logicGrid.getFlagPositions().get(goalFlag);
        int score =  getDistanceAway(pos, goalPos);
        if(!isPosCloserToGoal(pos, pos.getPositionIn(dir), goalPos)){score++;}
        if(difficulty.equals(Difficulty.MEDIUM) || difficulty.equals(Difficulty.HARD)){return score;}

        ArrayList<List<Object>> flagPosScores =  flagPositionScores.get(goalFlag);
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
     * The score looks first at which flag is the next to get at the end of the round. (i.e any selection of cards
     * that picked up a flag in the round is always better than any selection of cards that didn't pick up any).
     * If some selection of cards are tied, then the score looks at how many cards it took to get to the flag (fewer = better)
     * If the score is still tied then it looks how far away the robot is from the next goal flag (lower = better).
     * Is used for picking cards at hard and expert difficulty.
     */
    private void pickOptimal(){
        int goalFlag = updateGoalFlag(nextGoalFlag);
        int score = getScore(newRobotPos, newRobotDir, goalFlag);
        int damage = getDamage();
        int movesToFlag = 0;
        ArrayList<ArrayList<List<Object>>> path = new ArrayList<>();
        ArrayList<ProgramCard> chosenCards = new ArrayList<>();
        int[] totalScore = {goalFlag, movesToFlag, score};
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
                     if (isCardTypeChecked(checkedCards, card)) {
                         continue;
                     }
                     checkedCards.add(card);
                     newFrontier.add(createNode(currentPos, currentDir, card, currentTotalScore,
                             currentChosenCards, currentAvailableCards, goalFlag, currentDamage));
                 }

                 for (List<Object> objects : newFrontier) {
                     ArrayList<ProgramCard> cardsInFrontier = (ArrayList) objects.get(3);
                     int[] nodeTotalScore = (int[]) objects.get(2);
                     int nodeScore = nodeTotalScore[2];
                     if (nodeScore < bestNodeScore) {
                         bestNodeScore = score;
                     }
                     if (cardsInFrontier.size() == cardsToPick && isCurrentBetter(bestFinalTotalScore, nodeTotalScore)) {
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
     * Creates a node for a card that is added to the frontier
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
        int phaseNr = cardsChosen.size() + 1;
        List<Object> finalPosAndDir = getPosFromCardMove(card, pos, dir, phaseNr);
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
        if (isCardUseless(card, pos, dir, damage, phaseNr)) {
            score = 100;
        }

        int[] newTotalScore = {goalFlag, moves , score};
        return Arrays.asList(finalPos, finalDir, newTotalScore, nodeChosenCards, nodeAvailableCards, newDamage);
    }













    /*
    All methods below are just simple helper methods.
     */


    /**
     * Checks if a CardType is already in a list of ProgramCards.
     * @param checkedCards the list of ProgramCards to check
     * @param card the ProgramCard with a CardType to check
     * @return true if it is already in the list, false otherwise
     */
    private boolean isCardTypeChecked(ArrayList<ProgramCard> checkedCards, ProgramCard card){
        for (ProgramCard checkedCard : checkedCards) {
            if (checkedCard.getCommand().equals(card.getCommand())) return true;
        }
        return false;
    }

    /**
     * Chechs if a ProgramCard is already in a list of ProgramCards.
     * @param checkedCards the list of ProgramCards to check
     * @param card the ProgramCard to check
     * @return true if it is already in the list, false otherwise
     */
    private boolean isProgramCardChecked(ArrayList<ProgramCard> checkedCards, ProgramCard card){
        for (ProgramCard checkedCard : checkedCards) {
            if (checkedCard.equals(card)) return true;
        }
        return false;
    }

    /**
     * Checks if card will be useless to pick in a phase.
     * A card is useless if it will result in death or make no change in position and direction.
     * @param card the ProgramCard to check
     * @param pos the current position
     * @param dir the current direction
     * @param damage the current damage of the player
     * @param phaseNr the phasenr of the phase we are checking for the card to be useless in
     * @return true if the card is useless, false otherwise.
     */
    private boolean isCardUseless(ProgramCard card, Position pos, Direction dir, int damage, int phaseNr){
        List<Object> finalPosAndDir = getPosFromCardMove(card, pos, dir, phaseNr);
        Position newPos = (Position) finalPosAndDir.get(0);
        Direction newDir = (Direction) finalPosAndDir.get(1);
        if(pos.equals(newPos) && dir.equals(newDir)){ return true;}
        if(damage + getLaserDamage(newPos) >= 10){
            System.out.println( card.toString() + " is useless at " + pos + ", new pos: " + newPos + " damage: " + damage + " laser damage: " + getLaserDamage(newPos));
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
            if (!isProgramCardChecked(chosenCards, programCard)) {
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
     * Find the final position and direction at the end of the phase (after conveyorbelts, pushers, and cogs move)
     *
     * @param pos             the position after a card move
     * @param dir             the direction after a card move
     * @param phaseNr         the phaseNr of the phase we are checking the cardmove in
     * @param expressBeltMove always false if calling this method from outside of this method
     * @return the final position at the end of the phase
     */
    private List<Object> findFinalPosAndDir(Position pos, Direction dir, int phaseNr, boolean expressBeltMove) {
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
                findFinalPosAndDir(finalPos, finalDir, phaseNr, true);
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
        if(!logicGrid.positionIsFree(finalPos, 7)){
            PusherPiece pusherPiece = logicGrid.getPieceType(finalPos, PusherPiece.class);
            if(pusherPiece.isActiveWhenOddPhase() && phaseNr % 2 != 0){
                finalPos = finalPos.getPositionIn(pusherPiece.getPushingDir());
            }
            if(!pusherPiece.isActiveWhenOddPhase() && phaseNr % 2 == 0){
                finalPos = finalPos.getPositionIn(pusherPiece.getPushingDir());
            }
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
     * @param phaseNr the phaseNr of the phase we are checking the card in
     * @return the final position and direction.
     */
    private List<Object> getPosFromCardMove(ProgramCard card, Position pos, Direction dir, int phaseNr) {
        int moves = Math.abs(card.getMovement());
        Position newPos = pos;
        Direction moveDir = dir;
        if(card.getCommand().equals(CardType.BACKUP)) moveDir = dir.getOppositeDirection();

        for (int i = 0; i < moves; i++) {
            if (logicGrid.isDeadMove(newPos.getPositionIn(moveDir))) {
                return findFinalPosAndDir(newPos.getPositionIn(moveDir), dir, phaseNr,false);
            }
            if(!isLegalMoveInDirection(newPos, moveDir)){
                return findFinalPosAndDir(newPos, dir, phaseNr, false);
            }
            newPos = newPos.getPositionIn(moveDir);
        }
        Direction newDir = dir.getCardTurnDirection(card.getCommand());
        return findFinalPosAndDir(newPos, newDir, phaseNr, false);
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


    /**
     * Gets the laser damage from a position
     * @param pos the position to check
     * @return 0 if there is no laser on the position, 1 for single laser, 2 for double laser.
     */
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

    /**
     * Evaluates powerdown if the AI has 5 or more damage and is not standing on a laser
     * The percentage chance to powerdown increases with more damage (e.g 7 damage = 70% chance to powerdown).
     */
    private void evaluatePowerDown(){
        if (getDamage() < 5 || logicGrid.positionIsFree(getPos(), 8) || logicGrid.positionIsFree(getPos(), 9))return;
        int random = (int) (Math.random()*10);
        if(random < getDamage()){
            doPowerDown();
        }
    }

    /**
     * Determines what position to respawn in
     * Easy difficulty chooses a random position
     * Medium, Hard and Expert uses the getScore method to get best position to respawn in.
     * In this case medium and hard will pick the same position while expert may choose a better position.
     * @param positions a list of possible positions to respawn in.
     * @return the desired position to respawn in.
     */
    public Position chooseRespawnPos(ArrayList<Position> positions){
        if(difficulty.equals(Difficulty.EASY)){
            int random  = (int) (Math.random() * positions.size());
            return (positions.get(random));
        }
        Position bestPos = positions.get(0);
        int bestScore = 100;
        for (int i = 1; i < positions.size() ; i++) {
            Position pos = positions.get(i);
             chooseRespawnDir(pos);
             int score = getScore(pos, chooseRespawnDir(pos), nextGoalFlag);
             if( score < bestScore){
                 bestScore = score;
                 bestPos = pos;
             }
        }
        return bestPos;
    }

    /**
     * Determines what direction to respawn in
     * Easy difficulty chooses a random direction
     * Medium, Hard and Expert uses the getScore method to get best direction to respawn in.
     * In this case medium and hard will pick the same direction while expert may choose a better direction.
     * @param pos the respawn position
     * @return the desired direction to respawn in.
     */
    public Direction chooseRespawnDir(Position pos){
        int random = (int) (Math.random()*4);
        if(difficulty.equals(Difficulty.EASY)){
            return Direction.values()[random];
        }
        Direction bestDir = getPlayerPiece().getDir();
        int bestScore = 100;
        for(Direction dir : Direction.values()){
            Position posInFront = pos.getPositionIn(dir);
            if(logicGrid.isDeadMove(posInFront)){
                continue;
            }
            int score = getScore(posInFront, dir, nextGoalFlag);
            if(score < bestScore){
                bestScore = score;
                bestDir = dir;
            }
        }
        return bestDir;
    }

    /**
     * Creates a test player if the game map is "ai_test_map" with a predetermined playerhand
     */
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

    /**
     * Checks if the current game map is a certain test map.
     * If it is then powerdown the AI immediately to make testing in those maps easier.
     */
    private void checkIfTestMap(){
        String mapName = game.getMapName();
        String cogMap = "cogs_test_map";
        String conveyorbeltMap = "conveyorbelt_test_map";
        String respawnMap = "respawn_test_map";
        String laserMap = "laser_test_map";
        if(mapName.equals(conveyorbeltMap) || mapName.equals(cogMap)
                || mapName.equals(respawnMap) || mapName.equals(laserMap)){
            doPowerDown();
        }
    }
    
}

