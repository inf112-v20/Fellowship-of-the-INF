package inf112.skeleton.app.player;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.cards.CardType;
import inf112.skeleton.app.game_logic.Game;
import inf112.skeleton.app.game_logic.Move;
import inf112.skeleton.app.game_logic.MovesToExecuteSimultaneously;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;
import java.util.*;

/**
 * Class representing a player.
 */
public class Player {

    private boolean isDead;
    private boolean powerDownMode;
    private LogicGrid logicGrid;
    private ArrayList<BoardPiece>[][] pieceGrid;
    private PlayerPiece playerPiece;
    private ArrayList<ProgramCard> playerHandDeck;
    private ProgramCard[] selectedCards;
    private ArrayList<ProgramCard> lockedCards;
    private ArrayList<Position> laserPath;
    private Position newRobotPos;
    private Direction newRobotDir;
    private int flagsVisitedInRound = 0;
    private Position nextGoalPos;

    private Position spawnPoint;
    private int damage;
    private int playerNumber;
    private int lives = 3;
    private int checkpointsVisited;
    private BoardPiece currentBoardPiece;
    private Direction latestMoveDirection;
    private boolean conveyorBeltMove = false;
    private boolean hasBeenMovedThisPhase = false;
    private Position oldLaserPos;
    private Game game;

    public Player(int playerNumber, Game game) {
        this.playerNumber = playerNumber;
        this.game = game;
        this.logicGrid = game.getLogicGrid();
        this.pieceGrid = logicGrid.getGrid();
        this.damage = 0;
        this.checkpointsVisited = 0;
        GameDeck gameDeck = game.getGameDeck();
        this.playerHandDeck = game.getGameDeck().drawHand(new ArrayList<ProgramCard>(), getDamage());
        this.selectedCards = new ProgramCard[5];
        this.lockedCards = new ArrayList<>();
        this.laserPath = new ArrayList<>();

        //Find the spawn point of the player, and set spawnPoint position to the first spawn
        findFirstSpawnPoint();

        this.playerPiece = new PlayerPiece(spawnPoint, 200, Direction.NORTH, this);
        this.isDead = false;
        this.powerDownMode = false;
        this.oldLaserPos = new Position(-1,0);
    }


    /**
     * Getter for position
     *
     * @return position of player
     */
    public Position getPos() {
        return playerPiece.getPos();
    }

    public void setPos(int x, int y) {
        playerPiece.setPos(new Position(x, y));
    }

    private void setPos(Position positionIn) {
        playerPiece.setPos(positionIn);
    }

    /**
     * Getter for player
     *
     * @return player
     */
    public TiledMapTileLayer.Cell getPlayerCell() {
        return playerPiece.getCurrentCell();
    }

    /**
     * Tries to move the player in a new direction
     *
     * @param newDirection new direction to move the player
     * @param moves list that moves created can be added to
     */
    public void tryToGo(Direction newDirection, MovesToExecuteSimultaneously moves) {
        Move move = new Move(this);
        Position pos = playerPiece.getPos();
        int newX = playerPiece.getPos().getX();
        int newY = playerPiece.getPos().getY();

        if (isLegalMove(pos.getX(), pos.getY(), newDirection)) {
            switch (newDirection) {
                case NORTH:
                    newY += 1;
                    break;
                case SOUTH:
                    newY -= 1;
                    break;
                case WEST:
                   newX -= 1;
                    break;
                case EAST:
                    newX += 1;
                    break;
            }
        }
        //check if the move kills the player, if so lose a life
        if (isDeadMove(newX, newY)) {
            currentBoardPiece = pieceGrid[spawnPoint.getX()][spawnPoint.getY()].get(0);
            latestMoveDirection = newDirection;
            loseLife();
        }

        //if position has changed and player isn't dead, update logic grid
        if ((newY != pos.getY() || newX != pos.getX()) && !isDead()) {
            //if the move results in pushing robots, add the resulting moves to the moves list
            addMovesForPushedRobots(this.getPlayerPiece(), newDirection, moves);
            setCurrentBoardPiece(newX, newY); //update currentBoardPiece
            setPos(newX, newY);
            move.updateMove(this);
            moves.add(move);
            latestMoveDirection = newDirection;
            this.conveyorBeltMove = false;
        }

        //If the player still have lives left, respawn it, but set it in shutdown mode
        else if  (lives >= 0 && isDead()) {
            respawnPlayer();
            setPowerDownMode(true);
        }

        //Handle what happens if the player runs out of lives
        else if (lives < 0 && isDead()) {
            respawnPlayer();
            setPowerDownMode(true);
            isDead = true;
        }
    }


    /**
     * If there is a player to be pushed, call tryToGo for the player laying there
     * TryToGo will call addMovesForPushedRobots, and add moves to the list, in order.
     *
     * @param playerPiece playerPiece that is pushing other players
     * @param dir         direction being pushed
     * @param moves       list of moves
     */
    private void addMovesForPushedRobots(PlayerPiece playerPiece, Direction dir, MovesToExecuteSimultaneously moves) {
        PlayerPiece possiblePlayerPiece = getPlayerPieceToPush(playerPiece, dir);
        if (possiblePlayerPiece != null) { //if there is a player to push, push it
            Player playertoPush = possiblePlayerPiece.getPlayer();
            playertoPush.tryToGo(dir, moves);
        }
    }

    /**
     * Finds a player piece that is pushed by a move in direction dir
     *
     * @param playerPiece playerPiece doing the pushing
     * @param dir         direction in which a robot may be pushed
     * @return null if there is no player piece to push, otherwise return player piece to push
     */
    private PlayerPiece getPlayerPieceToPush(PlayerPiece playerPiece, Direction dir) {
        Position targetPosition = playerPiece.getPos().getPositionIn(dir);
        if (logicGrid.isInBounds(targetPosition))
            return logicGrid.getPieceType(targetPosition, PlayerPiece.class);
        return null; //return null if target position is not within grid
    }

    /**
     * Updates currentBoardPiece based on what type of tile the player is standing on.
     *
     * @param newX, newY new position for Player
     */
    public void setCurrentBoardPiece(int newX, int newY) {
        BoardPiece currPiece;
        for (int i = 0; i < pieceGrid[newX][newY].size(); i++) {
            currPiece = pieceGrid[newX][newY].get(i);
            if (currPiece instanceof ExpressBeltPiece) {
                currentBoardPiece = currPiece;
            } else if (currPiece instanceof ConveyorBeltPiece) {
                currentBoardPiece = currPiece;
            } else if (currPiece instanceof CogPiece) {
                currentBoardPiece = currPiece;
            } else if (currPiece instanceof PusherPiece) {
                currentBoardPiece = currPiece;
            } else if (currPiece instanceof AbyssPiece) {
                currentBoardPiece = currPiece;
            } else if (currPiece instanceof FlagPiece) {
                currentBoardPiece = currPiece;
            } else if (currPiece instanceof FloorPiece) {
                currentBoardPiece = currPiece;
            }
        }
    }

    /**
     * Method for checking if a move-1-forward move is applicable.
     *
     * @param x-coordinate current x coordinate
     * @param y-coordinate current y coordinate
     * @param dir          current direction
     * @return whether moving one tile in a given direction is legal
     */
    private boolean isLegalMove(int x, int y, Direction dir) {
        if (isDead()) {
            return false;
        }
        switch (dir) {
            case EAST:
                if (isLegalMoveInDirection(x + 1, y, new Position(x, y), dir)) {
                    return true;
                }
                break;
            case WEST:
                if (isLegalMoveInDirection(x - 1, y, new Position(x, y), dir)) {
                    return true;
                }
                break;
            case NORTH:
                if (isLegalMoveInDirection(x, y + 1, new Position(x, y), dir)) {
                    return true;
                }
                break;
            case SOUTH:
                if (isLegalMoveInDirection(x, y - 1, new Position(x, y), dir)) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Checks if a move in a certain direction is legal.
     * The robot must be allowed to exit it's current cell.
     * The robot must be allowed to enter the destination cell.
     * There must not be a robot that cannot be pushed in the way.
     *
     * @param newX            x-coordinate of new position
     * @param newY            y-coordinate of new position
     * @param currentPosition position you are moving from
     * @param dir             direction we are checking if a move is legal in
     * @return true if move is legal
     */
    public boolean isLegalMoveInDirection(int newX, int newY, Position currentPosition, Direction dir) {
        Position positionInFront = new Position(newX, newY);
        if (logicGrid.canLeavePosition(currentPosition, dir) && logicGrid.canEnterNewPosition(positionInFront, dir)) {
            //if the position in front is in bounds, check if there is a player there that can be pushed
            if (logicGrid.isInBounds(positionInFront)) {
                //If there is a player in the way of where you want to move, check that you can push that player
                PlayerPiece possiblePlayer = logicGrid.getPieceType(positionInFront, PlayerPiece.class);
                if (possiblePlayer != null) {
                    return isLegalMove(positionInFront.getX(), positionInFront.getY(), dir);
                } else return true;
            } else return true;
        }
        return false;
    }


    /**
     * Method for checking if a move results in death
     *
     * @param x x-position after move
     * @param y y-position after move
     * @return whether the move results in death
     */
    private boolean isDeadMove(int x, int y) {
        BoardPiece currPiece;
        //if move is within bounds, check if move is to AbyssPiece
        if (logicGrid.isInBounds(new Position(x, y))) {
            for (int i = 0; i < pieceGrid[x][y].size(); i++) {
                currPiece = pieceGrid[x][y].get(i);
                if (currPiece instanceof AbyssPiece) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * Player executes the move on the given card
     *
     * @param programCard to convert to player move
     * @param moves       list of moves to add move objects to
     */
    public void executeCardAction(ProgramCard programCard, MovesToExecuteSimultaneously moves) {
        Move rotateMove = new Move(this); //initiate possible rotateMove to be done
        switch (programCard.getCommand()) {
            case MOVE1:
                tryToGo(getPlayerPiece().getDir(), moves);
                break;
            case MOVE2:
                tryToGo(getPlayerPiece().getDir(), moves);
                tryToGo(getPlayerPiece().getDir(), moves);
                break;
            case MOVE3:
                tryToGo(getPlayerPiece().getDir(), moves);
                tryToGo(getPlayerPiece().getDir(), moves);
                tryToGo(getPlayerPiece().getDir(), moves);
                break;
            case UTURN:
                turnPlayerAround();
                break;
            case BACKUP:
                tryToGo(getPlayerPiece().getDir().getOppositeDirection(), moves);
                break;
            case ROTATELEFT:
                turnPlayerLeft();
                break;
            case ROTATERIGHT:
                turnPlayerRight();
                break;
            default:
                break;
        }
        rotateMove.updateMove(this); //complete rotateMove object
        moves.add(rotateMove);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean isDead() {
        return isDead;
    }

    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    public void turnPlayerAround() {
        playerPiece.turnAround();
    }

    public void turnPlayerLeft() {
        playerPiece.turnLeft();
    }

    public void turnPlayerRight() {
        playerPiece.turnRight();
    }

    public ArrayList<ProgramCard> getPlayerHandDeck() {
        return playerHandDeck;
    }

    public ProgramCard[] getSelectedCards() {
        return selectedCards;
    }

    public void setPlayerHandDeck(ArrayList<ProgramCard> playerHandDeck) {
        this.playerHandDeck = playerHandDeck;
    }

    public void setSelectedCards(ProgramCard[] selectedCards) {
        this.selectedCards = selectedCards;
    }



    @Override
    public String toString() {
        return "Player{" +
                "playerNumber=" + playerNumber +
                '}';
    }

    public Position getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(int x, int y) {
        spawnPoint = new Position(x, y);
    }

    /**
     * Put the player back to it's respawn position and update all maps
     */
    public void respawnPlayer() {
        isDead = false;
        playerPiece.showAlivePlayer();
        playerPiece.setPos(new Position(spawnPoint.getX(), spawnPoint.getY()));
        logicGrid.movePlayerToNewPosition(playerPiece.getPos(), new Position(spawnPoint.getX(), spawnPoint.getY()));
        setPos(spawnPoint.getX(), spawnPoint.getY());
    }

    public void setPowerDownMode(boolean a){
        powerDownMode = a;
    }

    public boolean isPowerDownMode() {
        return powerDownMode;
    }

    /**
     * Damages a player a given amount
     * A player loses a life if its damage is 10 or higher
     * Starts locking a players selected cards (from right to left)
     * when a players current damage is 5 or higher.
     *
     * @param amountOfDamage the number of damage the player takes
     */
    public void takeDamage(int amountOfDamage) {
        int oldDamage = damage;
        damage += amountOfDamage;
        if (damage >= 10) {
            lives--;
            damage = 10;
        }
        if (damage >= 5 && damage <= 9) {
            int number = damage - oldDamage;
            if(oldDamage < 5){number = damage - 4;}
            if(number > 5-lockedCards.size()){number = 5-lockedCards.size();}
            for (int i = 0; i < number; i++) {
                lockedCards.add(0, selectedCards[(8-(damage-number))-i]);
                System.out.println("Locking card" + selectedCards[(8-(damage-number))-i]);
                playerHandDeck.remove(lockedCards.get(0));
            }
        }
    }

    /**
     * Heals a player a given amount.
     * Will unlock a players selected cards (from left to right)
     * if the player already have locked cards.
     *
     * @param amountOfRepairs the number of damage to remove from the player
     */
    public void repairDamage(int amountOfRepairs) {
        damage -= amountOfRepairs;
        if (damage < 0) {
            damage = 0;
        }
        int numberOfCardsToUnlock = lockedCards.size() - (damage - 4);
        if (numberOfCardsToUnlock > 0 && lockedCards.size() > 0) {
            for (int i = 0; i < numberOfCardsToUnlock; i++) {
                playerHandDeck.add(lockedCards.get(0));
                lockedCards.remove(0);
            }
        }
    }

    public int getDamage() {
        return damage;
    }

    /**
     * Remove a life from the life counter, and turn the player cell into a dead player cell
     */
    public void loseLife() {
        lives--;
        isDead = true;
        playerPiece.showDeadPlayer();
    }

    /**
     * Get the spawn point position from a list of spawn points
     */
    public void findFirstSpawnPoint() {
        ArrayList<Position> spawns = logicGrid.getSpawnPointPositions();
        spawnPoint = new Position(spawns.get(playerNumber - 1).getX(), spawns.get(playerNumber - 1).getY());
    }

    /**
     * Method for testing.
     */
    public void gainLife() {
        lives++;
    }

    public int getLives() {
        return lives;
    }

    public void visitedCheckpoint() {
        checkpointsVisited++;
    }

    /**
     * Method for testing.
     */
    public void removeCheckpoint() {
        checkpointsVisited--;
    }

    public int getCheckpointsVisited() {
        return checkpointsVisited;
    }

    /**
     * Checks if a player is currently on a ConveyorBeltPiece
     *
     * @return true if a player is on a conveyorBelt, false otherwise.
     */
    public boolean isOnConveyorBelt() {
        return (currentBoardPiece instanceof ConveyorBeltPiece);
    }

    /**
     * Checks if a player is currently on an ExpressBeltPiece
     *
     * @return true if a player is on an ExpressBelt, false otherwise.
     */
    public boolean isOnExpressBelt() {
        return (currentBoardPiece instanceof ExpressBeltPiece);
    }

    /**
     * Checks if a player is currently on a CogPiece
     *
     * @return true if a player is on a cog, false otherwise.
     */
    public boolean isOnCog() {
        return currentBoardPiece instanceof CogPiece;
    }


    public ArrayList<ProgramCard> getLockedCards() {
        return lockedCards;
    }

    /**
     * Removes all cards from the players selected cards
     */
    public void removeSelectedCards() {
        Arrays.fill(selectedCards, null);
    }

    public BoardPiece getCurrentBoardPiece() {
        return currentBoardPiece;
    }

    /**
     * Gets the last playermove direction
     *
     * @return the direction of the last movement of the player (movement can be from cards or map objects)
     */
    public Direction latestMoveDirection() {
        return latestMoveDirection;
    }

    /**
     * @param move true if the last movement of the player was by a conveyorbelt/expressbelt, false otherwise
     */
    public void setConveyorBeltMove(boolean move) {
        conveyorBeltMove = move;
    }

    /**
     * @return true if the last last movement of the player was by a conveyorbelt/expressbelt, false otherwise
     */
    public boolean isLatestMoveConveyorBelt() {
        return conveyorBeltMove;
    }

    public void setHasBeenMovedThisPhase(boolean bool) {
        hasBeenMovedThisPhase = bool;
    }

    public Boolean hasBeenMovedThisPhase() {
        return hasBeenMovedThisPhase;
    }

    public ArrayList<Position> getLaserPath(){
        return laserPath;
    }



    public Position getOldLaserPos(){
        return oldLaserPos;
    }

    public void setOldLaserPos(Position pos){
        oldLaserPos = pos;
    }

    public void shootLaser(){
        Direction laserDir = playerPiece.getDir();
        Position laserPos = getPos();
        for (int i = 0; i < logicGrid.getHeight(); i++) {
            //System.out.println(player.toString() + " laser is at " + laserPos);
            if (!logicGrid.isInBounds(laserPos)) { return; }
            if(!logicGrid.positionIsFree(laserPos, 12) && !laserPos.equals(getPos())){
                if(game.getPlayerAt(laserPos) == null){System.out.println("Error: " + toString() + " Couldn't shoot player"); return;}
                game.getPlayerAt(laserPos).takeDamage(1);
                //System.out.println(player.toString() + " hit " + game.getPlayerAt(laserPos).toString());
                laserPath.add(laserPos);
                return;
            }
            if(!logicGrid.canLeavePosition(laserPos, laserDir)||
                    !logicGrid.canEnterNewPosition(laserPos.getPositionIn(laserDir), laserDir)){return;}
            if(i == 0){setOldLaserPos(laserPos);}
            laserPos = laserPos.getPositionIn(laserDir);
            laserPath.add(laserPos);
        }

    }

    /**
     * Pick cards for computer player
     */
    public void pickCards() {
        if(playerHandDeck.isEmpty()){ return;}
        newRobotPos = new Position(getPos().getX(), getPos().getY());
        newRobotDir = playerPiece.getDir();
        for (int i = 0; i < 5 - lockedCards.size() ; i++) {
            selectedCards[i] = chooseCard();
        }
        flagsVisitedInRound = 0;
    }

    /**
     * Picks the best available card in hand
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
        for (int i = 0; i < playerHandDeck.size(); i++) {
            if(!isCardAlreadyChosen(playerHandDeck.get(i))){
                randomCard = playerHandDeck.get(i);
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
    private boolean isCardAlreadyChosen(ProgramCard card){
        for (int i = 0; i < selectedCards.length ; i++) {
            if(selectedCards[i] == null){continue;}
            if(selectedCards[i].equals(card)){return true;}
        }
        return false;
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
            System.out.println("Pos: " + finalPos + ", Dir: " + finalDir + ", Movescore: " + moveScore);
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
     * @param dir the new dir if card is rotation card
     * @return the new position from using the card given
     */
    private List<Object> getPosFromCardMove(int cardMove, Direction dir){
        Position newPos = newRobotPos;
        Direction newDir = dir;
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
            if (card.getCommand() == cardType && !isCardAlreadyChosen(card)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Gets cardType from an id
     * @param id = movement for movecard, = rotations array position + 1 in getBestCardsOrdered()
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