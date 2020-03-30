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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AIPlayer extends  Player {
    private Position newRobotPos;
    private Direction newRobotDir;
    private int flagsVisitedInRound = 0;
    private int checkpointsVisited;
    private Position nextGoalPos;
    private LogicGrid logicGrid;
    private ArrayList<ProgramCard> playerHandDeck;

    public AIPlayer(int playerNumber, Game game) {
        super(playerNumber, game);
        logicGrid = game.getLogicGrid();
        playerHandDeck = getPlayerHandDeck();
        checkpointsVisited = getCheckpointsVisited();
    }

    /**
     * Pick cards for computer player
     */
    public void pickCards() {
        if(playerHandDeck.isEmpty()){ return;}
        newRobotPos = new Position(getPos().getX(), getPos().getY());
        newRobotDir = getPlayerPiece().getDir();
        for (int i = 0; i < 5 - getLockedCards().size() ; i++) {
            getSelectedCards()[i] = chooseCard();
        }
        flagsVisitedInRound = 0;
    }

    /**
     * Checks first if a robot should move or rotate this phase
     * and picks the best available card in hand accordingly.
     * @return the best card
     */
    public ProgramCard chooseCard(){
        int goalFlag = checkpointsVisited + flagsVisitedInRound;
        if(goalFlag > logicGrid.getFlagPositions().size()-1){goalFlag = logicGrid.getFlagPositions().size()-1;}
        nextGoalPos = logicGrid.getFlagPositions().get(goalFlag);
        System.out.println("Current goal flag: " + (goalFlag+1)+ " at " + nextGoalPos);

        Position posInFront = newRobotPos.getPositionIn(newRobotDir);
        boolean shouldPickMoveCard = true;
        System.out.println("Position: " + newRobotPos + ", Direction: " + newRobotDir);

        if (!isLegalMoveInDirection(posInFront.getX(), posInFront.getY(), getPos(), newRobotDir)
                || !isPosCloserToGoal(newRobotPos, posInFront)) {
            shouldPickMoveCard = false;
            System.out.println(toString() + " should rotate");
        } else { System.out.println(toString() + " should move"); }

        ProgramCard bestCard = getBestCard(shouldPickMoveCard);
        if(bestCard == null){
            if(shouldPickMoveCard) {System.out.println("There wasn't any optimal movement cards left, choosing rotation card instead");}
            else{System.out.println("There wasn't any optimal rotation cards left, choosing movement card instead");}
            bestCard = getBestCard(!shouldPickMoveCard);
        }
        if(!(bestCard == null)){return bestCard;}

        ProgramCard randomCard = playerHandDeck.get(0);
        for (ProgramCard programCard : playerHandDeck) {
            if (isCardAvailable(programCard)) {
                randomCard = programCard;
                System.out.println(toString() + " chose " + randomCard.toString() + " as a random card");
                break;
            }
        }
        return randomCard;
    }

    /**
     * Checks if a position is closer to current goal than an other position
     * @param oldPos the old position
     * @param newPos the new position
     * @return true if the new position is closer to goal, false otherwise
     */
    private boolean isPosCloserToGoal(Position oldPos, Position newPos){
        int oldDistanceAway = getDistanceAway(nextGoalPos, oldPos);
        int newDistanceAway = getDistanceAway(nextGoalPos, newPos);
        return(newDistanceAway < oldDistanceAway);
    }

    /**
     * Check how far away two positions are
     * @param pos1 position 1
     * @param pos2 position 2
     * @return sum of difference of x pos and y pos of the two positions
     */
    private int getDistanceAway(Position pos1, Position pos2){
        return(Math.abs(pos1.getX()-pos2.getX()) + Math.abs(pos1.getY()- pos2.getY()));
    }

    /**
     * Checks if a card is available in hand
     * @param card the card to check
     * @return true if that type of card is available in hand, false otherwise
     */
    private boolean isCardAvailable(ProgramCard card){
        for (ProgramCard selectedCard : getSelectedCards()) {
            if (selectedCard == null) { continue; }
            if (selectedCard.equals(card)) { return false; }
        }
        return true;
    }

    /**
     * Find the final position and direction at the end of the phase (after conveyorbelts and cogs move)
     * @param pos the position after a card move
     * @param dir the direction after a card move
     * @param expressBeltMove always false if calling this method from outside of this method
     * @return the final position at the end of the phase
     */
    private List<Object> findFinalPosAndDir(Position pos, Direction dir, boolean expressBeltMove){
        if(!logicGrid.isInBounds(pos)) {return Arrays.asList(pos, dir);}
        Position finalPos = pos;
        Direction finalDir = dir;

        if(!logicGrid.positionIsFree(pos, 5)){
            ExpressBeltPiece expressBelt = logicGrid.getPieceType(pos, ExpressBeltPiece.class);
            if(expressBelt.isTurn()){
                if (expressBelt.isTurnRight()){ finalDir = finalDir.getRightTurnDirection();}
                else{finalDir = finalDir.getLeftTurnDirection();}
            }
            finalPos = finalPos.getPositionIn(expressBelt.getDir());
            if(!logicGrid.positionIsFree(finalPos, 5) && !expressBeltMove){
                findFinalPosAndDir(finalPos, finalDir, true);
            }
        }

        if(!logicGrid.isInBounds(finalPos)) {return Arrays.asList(finalPos, finalDir);}
        if(!logicGrid.positionIsFree(finalPos, 4) && !expressBeltMove){
            ConveyorBeltPiece conveyorBelt = logicGrid.getPieceType(finalPos, ConveyorBeltPiece.class);
            if(conveyorBelt.isTurn()){
                if (conveyorBelt.isTurnRight()){ finalDir = finalDir.getRightTurnDirection(); }
                else{finalDir = finalDir.getLeftTurnDirection();}
            }
            finalPos = finalPos.getPositionIn(conveyorBelt.getDir());
        }

        if(!logicGrid.isInBounds(finalPos)) {return Arrays.asList(finalPos, finalDir);}
        if(!logicGrid.positionIsFree(finalPos, 6)){
            CogPiece cog = logicGrid.getPieceType(finalPos, CogPiece.class);
            if(cog.isRotateClockwise()){ finalDir = finalDir.getRightTurnDirection(); }
            else{ finalDir = finalDir.getLeftTurnDirection(); }
        }
        return Arrays.asList(finalPos, finalDir);
    }

    /**
     * Finds the best card in order for this phase
     * @param moveCard true if a card is a movecard, false if its a rotationcard
     * @return sorted 2d array with best card first. [][0] = cardtype, [][1] = movescore
     */
    private int[][] getBestCardsOrdered(boolean moveCard){
        int[][] bestMovesInOrder = new int[3][2];
        int movement = 0;
        int rotation = 3;
        Direction [] rotations  = {newRobotDir.getLeftTurnDirection(), newRobotDir.getRightTurnDirection(),
                newRobotDir.getOppositeDirection(), newRobotDir};

        for (int i = 0; i < 3 ; i++) {
            if(moveCard){ movement = i+1;}
            else{rotation = i;}
            List<Object> finalPosAndDir = getPosFromCardMove(movement, rotations[rotation]);
            Position finalPos = (Position)finalPosAndDir.get(0);
            Direction finalDir = (Direction)finalPosAndDir.get(1);
            int moveScore = getMoveScore(finalPosAndDir);
            System.out.println(getType(i +1 , moveCard).toString() + " will put player at pos: " + finalPos + ", facing dir: " + finalDir + ". Movescore: " + moveScore);
            bestMovesInOrder[i][0] = moveScore;
            bestMovesInOrder[i][1] = i + 1;
        }

        java.util.Arrays.sort(bestMovesInOrder, new java.util.Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });
        return bestMovesInOrder;
    }

    /**
     * Gets the new position from using a card
     * @param cardMove the amount movement from the card (0 for rotation cards)
     * @param newDir the new dir if card is rotation card
     * @return the new position from using the card given
     */
    private List<Object> getPosFromCardMove(int cardMove, Direction newDir){
        Position newPos = newRobotPos;
        for (int i = 0; i < cardMove; i++) {
            newPos = newPos.getPositionIn(newDir);
        }
        return findFinalPosAndDir(newPos, newDir, false);
    }

    /**
     * Gets the first available card in hand of that type
     * @param cardType the type of card you want
     * @return the first card of that type in hand, null if it isn't available
     */
    private ProgramCard getCardInHand(CardType cardType){
        for (ProgramCard card : playerHandDeck) {
            if (card.getCommand() == cardType && isCardAvailable(card)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Gets cardType from an id
     * @param id = movement for move cards, or = rotations array position + 1 in getBestCardsOrdered() for rotation cards
     * @param moveCard true if its a movecard
     * @return the cardType from the given id
     */
    private CardType getType(int id, boolean moveCard){
        CardType cardType = CardType.BACKUP;
        switch (id) {
            case 1:
                cardType = CardType.MOVE1;
                if(!moveCard){ cardType = CardType.ROTATELEFT;}
                break;
            case 2:
                cardType = CardType.MOVE2;
                if(!moveCard){ cardType = CardType.ROTATERIGHT;}
                break;
            case 3:
                cardType = CardType.MOVE3;
                if(!moveCard){ cardType = CardType.UTURN;}
                break;
            default:
                break;
        }
        return cardType;
    }

    /**
     * Gets the best card for the robots current position in the round
     * @param shouldPickMoveCard true if robot should pick move card, false if it should pick a rotation card
     * @return the best available card
     */
    private ProgramCard getBestCard(boolean shouldPickMoveCard){
        int[][] bestMovesInOrder =  getBestCardsOrdered(shouldPickMoveCard);
        for (int i = 0; i < 3 ; i++) {
            int moveScore = bestMovesInOrder[i][0];
            CardType cardType = getType(bestMovesInOrder[i][1], shouldPickMoveCard);
            System.out.println("Best card is " + cardType);
            ProgramCard card = getCardInHand(cardType);
            if(card == null){System.out.println("Card is not available in hand");continue;}
            if(moveScore == 100){System.out.println(cardType + " will result in death");continue;} //100 = death move
            List<Object> finalPosAndDir = getPosFromCardMove(card.getMovement(), newRobotDir.getCardTurnDirection(cardType));
            newRobotPos = (Position)finalPosAndDir.get(0);
            newRobotDir = (Direction)finalPosAndDir.get(1);
            if(!shouldPickMoveCard && getBackUpCardMoveScore() < moveScore){
                ProgramCard backUpCard = getCardInHand(getType(0, true));
                if(!(backUpCard == null)){
                    card = backUpCard;
                    finalPosAndDir = findFinalPosAndDir(newRobotPos.getPositionIn(newRobotDir.getOppositeDirection()), newRobotDir, false);
                    newRobotPos = (Position) finalPosAndDir.get(0);
                    newRobotDir = (Direction) finalPosAndDir.get(1);
                }
            }
            System.out.println("Card chosen is " + card.toString() + "\n");
            if(nextGoalPos.equals(newRobotPos)){ System.out.println("Player is at flag " + (checkpointsVisited+1+flagsVisitedInRound)); flagsVisitedInRound++; }
            return card;
        }
        return null;
    }

    /**
     * Gets the move score from a backup card
     * @return move score from using a backup this phase
     */
    private int getBackUpCardMoveScore(){
        List<Object> finalPosAndDir = findFinalPosAndDir(newRobotPos.getPositionIn(newRobotDir.getOppositeDirection()), newRobotDir, false);
        return getMoveScore(finalPosAndDir);
    }

    /**
     * Gets the move score for a cardtype. Lower score = better score.
     * if movescore = 100, then using that cardtype will result in death.
     * Facing away from the next goal at the end of phase will result in worse score.
     * @param finalPosAndDir final position and direction at the end of the phase.
     * @return the movescore calculated for a cardtype.
     */
    private int getMoveScore(List<Object> finalPosAndDir){
        Position finalPos = (Position)finalPosAndDir.get(0);
        Direction finalDir = (Direction)finalPosAndDir.get(1);
        Position posInFront = finalPos.getPositionIn(finalDir);
        int moveScore = getDistanceAway(finalPos, nextGoalPos);
        if(!isPosCloserToGoal(finalPos, finalPos.getPositionIn(finalDir))){moveScore++;}
        if(isPosCloserToGoal(finalPos, finalPos.getPositionIn(finalDir.getOppositeDirection()))){moveScore++;}
        if(logicGrid.isInBounds(posInFront)&& logicGrid.isInBounds(finalPos)) {
            if (!isLegalMoveInDirection(posInFront.getX(), posInFront.getY(), finalPos, finalDir)) { moveScore++; }
        }
        if(isDeadMove(finalPos.getX(), finalPos.getY())){moveScore = 100;}
        return moveScore;
    }


}
