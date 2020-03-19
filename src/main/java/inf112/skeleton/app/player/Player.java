package inf112.skeleton.app.player;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;

import java.util.ArrayList;

/**
 * Class representing a player.
 */
public class Player {

    private boolean isDead;
    private LogicGrid logicGrid;
    private ArrayList<BoardPiece>[][] pieceGrid;
    private PlayerPiece playerPiece;
    private Game game;
    private ArrayList<ProgramCard> playerHandDeck;
    private ProgramCard[] selectedCards;
    private ArrayList<ProgramCard> lockedCards;

    private Position spawnPoint;
    private int damage;
    private int playerNumber;
    private int lives = 3;
    private int checkpointsVisited;
    private BoardPiece currentBoardPiece;
    private Direction latestMoveDirection;
    private boolean conveyorBeltMove = false;

    public Player(int playerNumber, Game game) {
        this.playerNumber = playerNumber;
        this.logicGrid = game.getLogicGrid();
        this.pieceGrid = logicGrid.getGrid();
        this.game = game;
        this.damage = 0;
        this.checkpointsVisited = 0;
        GameDeck gameDeck = game.getGameDeck();
        this.playerHandDeck = game.getGameDeck().drawHand(new ArrayList<ProgramCard>(), getDamage());
        this.selectedCards = new ProgramCard[5];
        this.lockedCards = new ArrayList<>();


        //Find the spawn point of the player, and set spawnPoint position to the first spawn
        findFirstSpawnPoint();

        this.playerPiece = new PlayerPiece(spawnPoint, 200, Direction.NORTH, playerNumber);
        this.isDead = false;
    }


    /**
     * Getter for position
     * @return position of player
     */
    public Position getPos() {
        return playerPiece.getPos();
    }

    public void setPos(int x, int y) { playerPiece.setPos(new Position(x, y)); }

    /**
     * Getter for player
     * @return player
     */
    public TiledMapTileLayer.Cell getPlayerCell() {
        return playerPiece.getCurrentCell();
    }

    public TiledMapTileLayer.Cell getStandardPlayerCell() { return playerPiece.getPlayerCell(); }

    /**
     * Tries to move the player in a new direction
     *
     * @param newDirection new direction to move the player
     */
    public void tryToGo(Direction newDirection) {
        Position pos = playerPiece.getPos();
        int newX = playerPiece.getPos().getX();
        int newY = playerPiece.getPos().getY();

        switch (newDirection) {
            case NORTH:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newY += 1;
                break;
            case SOUTH:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newY -= 1;
                break;
            case WEST:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newX -= 1;
                break;
            case EAST:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newX += 1;
                break;
        }
        //check if the move kills the player, if so lose a life
        if (isDeadMove(newX, newY)) {
            loseLife();
        }

        //if position has changed and player isn't dead, update logic grid
        if ((newY != pos.getY() || newX != pos.getX()) && !isDead()) {
            playerPiece.setPos(new Position(newX, newY));
            // map.movePlayerToNewPosition(pos, new Position(newX, newY));
            setCurrentBoardPiece(newX, newY);
            setPos(newX, newY);
            latestMoveDirection = newDirection;
            this.conveyorBeltMove = false;
        }
        //TODO This should probably only happen when the round is over, and we are about to start a new round
        //If the player still have lives left, respawn it
        /*
        else if (lives >= 0 && isDead()) {
            respawnPlayer();
        }
        */

        //TODO Add what happens when a player runs out of lives
        //Handle what happens if the player runs out of lives
        else {
            setPos(newX, newY);
        }
    }
    public void setCurrentBoardPiece(int newX, int newY){
        BoardPiece currPiece;
        for (int i = 0; i < pieceGrid[newX][newY].size(); i++) {
            currPiece = pieceGrid[newX][newY].get(i);
            if(currPiece instanceof ExpressBeltPiece){currentBoardPiece = currPiece;}
            else if(currPiece instanceof ConveyorBeltPiece){currentBoardPiece = currPiece;}
            else if(currPiece instanceof CogPiece){currentBoardPiece = currPiece;}
            else if(currPiece instanceof PusherPiece){currentBoardPiece = currPiece;}
            else if(currPiece instanceof AbyssPiece){currentBoardPiece = currPiece;}
            else if(currPiece instanceof FloorPiece){currentBoardPiece = currPiece;}
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
        //int xPosIn2dArray = MAP_WIDTH - 1 - x;
        BoardPiece currPiece;
        BoardPiece pieceInFront;
        if (isDead()) return false;
        for (int i = 0; i < pieceGrid[x][y].size(); i++) {
            currPiece = pieceGrid[x][y].get(i);
            switch (dir) {
                case EAST:
                    if (!isLegalMoveInDirection(x+1, y, currPiece, dir, i)) { return false; }
                    break;
                case WEST:
                    if (!isLegalMoveInDirection(x-1, y, currPiece, dir, i)) { return false; }
                    break;
                case NORTH:
                    if (!isLegalMoveInDirection(x, y+1, currPiece, dir, i)) { return false; }
                    break;
                case SOUTH:
                    if (!isLegalMoveInDirection(x, y-1, currPiece, dir, i)) { return false; }
                    break;
            }
        }
        return true;
    }

    public boolean isLegalMoveInDirection(int newX, int newY, BoardPiece currentPiece, Direction dir, int layerLevel) {
        if (isDeadMove(newX, newY)) {
            if (currentPiece instanceof WallPiece) {
                return ((WallPiece) currentPiece).canLeave(dir);
            }
        } else {
            BoardPiece pieceInFront = pieceGrid[newX][newY].get(layerLevel);
            if (currentPiece instanceof WallPiece) {
                return ((WallPiece) currentPiece).canLeave(dir);
            }
            if (pieceInFront instanceof WallPiece) {
                return ((WallPiece) pieceInFront).canGo(dir);
            }
        }
        return true;
    }

    /**
     * Method for checking if a move results in death
     *
     * @param x x-position after move
     * @param y y-position after move
     * @return whether the move results in death
     */
    private boolean isDeadMove(int x, int y) {
        int MAP_WIDTH = game.getLogicGrid().getWidth();
        int MAP_HEIGHT = game.getLogicGrid().getHeight();
        BoardPiece currPiece;
        if (x > MAP_WIDTH - 1 || x < 0 || y < 0 || y > MAP_HEIGHT - 1) {
            return true;
        } else {
            for (int i = 0; i < pieceGrid[x][y].size(); i++) {
                currPiece = pieceGrid[x][y].get(i);
                if (currPiece instanceof AbyssPiece) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Player executes the move on the given card
     * @param programCard to convert to player move
     */
    public void executeCardAction(ProgramCard programCard) {
        switch (programCard.getCommand()) {
            case MOVE1:
                tryToGo(getPlayerPiece().getDir());
                break;
            case MOVE2:
                tryToGo(getPlayerPiece().getDir());
                tryToGo(getPlayerPiece().getDir());
                break;
            case MOVE3:
                tryToGo(getPlayerPiece().getDir());
                tryToGo(getPlayerPiece().getDir());
                tryToGo(getPlayerPiece().getDir());
                break;
            case UTURN:
                turnPlayerAround();
                break;
            case BACKUP:
                tryToGo(getPlayerPiece().getDir().getOppositeDirection());
                break;
            case ROTATELEFT:
                turnPlayerLeft();
                break;
            case ROTATERIGHT:
                turnPlayerRight();
                break;
            default:
                //TODO error handling as default maybe?
                break;
        }
    }

    public int getPlayerNumber() { return playerNumber; }

    public boolean isDead() { return isDead; }

    public PlayerPiece getPlayerPiece() { return playerPiece; }

    public void turnPlayerAround() { playerPiece.turnAround(); }

    public void turnPlayerLeft() { playerPiece.turnLeft(); }

    public void turnPlayerRight() { playerPiece.turnRight(); }

    public ArrayList<ProgramCard> getPlayerHandDeck() { return playerHandDeck; }

    public ProgramCard[] getSelectedCards() { return selectedCards; }

    public void setPlayerHandDeck(ArrayList<ProgramCard> playerHandDeck) { this.playerHandDeck = playerHandDeck; }

    public void setSelectedCards(ProgramCard[] selectedCards) { this.selectedCards = selectedCards; }

    /**
     * Sets the first five cards in the given hand of nine cards, to be the chosen five cards in a round
     */
   public void pickFirstFiveCards() {
        int NUMBER_OF_CARDS_TO_CHOOSE = 5;
        ProgramCard[] firstFiveCards = new ProgramCard[NUMBER_OF_CARDS_TO_CHOOSE];
        if (playerHandDeck.size() >= NUMBER_OF_CARDS_TO_CHOOSE) {
            for (int i = 0; i < NUMBER_OF_CARDS_TO_CHOOSE; i++) {
                firstFiveCards[i] = (playerHandDeck.get(i));
            }
            selectedCards = firstFiveCards;
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerNumber=" + playerNumber +
                '}';
    }
    public Position getSpawnPoint() { return spawnPoint; }

    public void setSpawnPoint(int x, int y) { spawnPoint = new Position(x, y); }

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

    /**
     * Damages a player a given amount
     * A player loses a life if its damage is 10 or higher
     * Starts locking a players selected cards (from right to left)
     * when a players current damage is 5 or higher.
     * @param amountOfDamage the number of damage the player takes
     */
    public void takeDamage(int amountOfDamage){
       damage += amountOfDamage;
        if(damage >= 10){
            lives--;
            damage = 10;
        }
       System.out.println("Damage: " + damage);
        if(damage >= 5 && damage <= 9){
           for (int i = damage-amountOfDamage-4; i < damage-4; i++) {
               lockedCards.add(0, selectedCards[4-i]);
               playerHandDeck.remove(lockedCards.get(0));
           }
       }
   }

    /**
     * Heals a player a given amount.
     * Will unlock a players selected cards (from left to right)
     * if the player already have locked cards.
     * @param amountOfRepairs the number of damage to remove from the player
     */
    public void repairDamage(int amountOfRepairs){
       damage -= amountOfRepairs;
       if(damage < 0){ damage = 0; }
       System.out.println("Damage: " + damage);
       int numberOfCardsToUnlock =  lockedCards.size()-(damage - 4);
       if (numberOfCardsToUnlock > 0 && lockedCards.size() > 0) {
           for (int i = 0; i < numberOfCardsToUnlock; i++) {
               playerHandDeck.add(lockedCards.get(0));
               lockedCards.remove(0);
            }
        }
   }

    public int getDamage(){return damage;}

    /**
     * Remove a life from the life counter, and turn the player cell into a dead player cell
     */
    public void loseLife() {
        lives--;
        isDead = true;
        playerPiece.showDeadPlayer();
        System.out.println("Lives: " + lives);
    }

    /**
     * Get the spawn point position from a list of spawn points
     */
    public void findFirstSpawnPoint() {
        try {
            ArrayList<Position> spawns = logicGrid.getSpawnPointPositions();
            spawnPoint = new Position(spawns.get(playerNumber - 1).getX(), spawns.get(playerNumber - 1).getY());
        } catch (Exception spawnNotFound) {
            setSpawnPoint(0,0+playerNumber);
        }
    }

    //TODO remove this later since it is not possible to gain a life. This is for testing purposes only.
    public void gainLife() {
        lives++;
        System.out.println("Lives: " + lives);
    }

    public int getLives() {
        return lives;
    }

    public void visitedCheckpoint() {
        checkpointsVisited++;
        System.out.println("Checkpoints: " + checkpointsVisited);
    }

    //TODO remove this later since it is not possible to undo a checkpoint/flag. This is for testing purposes only.
    public void removeCheckpoint() {
        checkpointsVisited--;
        System.out.println("Checkpoints: " + checkpointsVisited);
    }

    public int getCheckpointsVisited() {
        return checkpointsVisited;
    }

    /**
     * Checks if a player is currently on a ConveyorBeltPiece
     * @return true if a player is on a conveyorBelt, false otherwise.
     */
    public boolean isOnConveyorBelt(){
        if (currentBoardPiece instanceof ConveyorBeltPiece) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a player is currently on an ExpressBeltPiece
     * @return true if a player is on an ExpressBelt, false otherwise.
     */
    public boolean isOnExpressBelt(){
        if (currentBoardPiece instanceof ExpressBeltPiece) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a player is currently on a CogPiece
     * @return true if a player is on a cog, false otherwise.
     */
    public boolean isOnCog(){
       if(currentBoardPiece instanceof CogPiece){
           return true;
       }
       return false;
    }


    public ArrayList<ProgramCard> getLockedCards(){return lockedCards;}

    /*
    Removes all cards from the players selected cards
     */
    public void removeSelectedCards(){
        for (int i = 0; i < selectedCards.length ; i++) {
            selectedCards[i] = null;
        }
    }

    public BoardPiece getCurrentBoardPiece(){return currentBoardPiece;}

    /**
     * Gets the last playermove direction
     * @return the direction of the last movement of the player (movement can be from cards or map objects)
     */
    public Direction latestMoveDirection(){
        return latestMoveDirection;
    }

    /**
     *
     * @param move true if the last movement of the player was by a conveyorbelt/expressbelt, false otherwise
     */
    public void setConveyorBeltMove(boolean move){
        conveyorBeltMove = move;
    }

    /**
     *
     * @return true if the last last movement of the player was by a conveyorbelt/expressbelt, false otherwise
     */
    public boolean isLatestMoveConveyorBelt(){
        return conveyorBeltMove;
    }

}