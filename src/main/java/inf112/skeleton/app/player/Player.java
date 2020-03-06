package inf112.skeleton.app.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Map;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;

import java.util.ArrayList;

/**
 * Class representing a player.
 */
public class Player {

    private boolean isDead;
    private Map map;
    private ArrayList<BoardPiece>[][] pieceGrid;
    private PlayerPiece playerPiece;
    private Game game;
    private ArrayList<ProgramCard> playerHandDeck;
    private ArrayList<ProgramCard> selectedCards;
    private ArrayList<ProgramCard> lockedCards;
    private int damage;
    private int playerNumber;
    private int lifes = 3;
    private int checkpointsVisited;
    private BoardPiece currentBoardPiece;




    public Player(int playerNumber, Game game) {
        this.playerNumber = playerNumber;
        this.map = game.getMap();
        this.pieceGrid = map.getGrid();
        this.game = game;
        this.damage = 0;
        GameDeck gameDeck = game.getGameDeck();
        this.playerHandDeck = game.getGameDeck().drawHand(new ArrayList<ProgramCard>(), getDamage());
        this.selectedCards = new ArrayList<>();
        this.lockedCards = new ArrayList<>();
        this.lockedCards = new ArrayList<>();
        //player is player is placed at bottom of board in position of player number
        int playerStartPositionX = (playerNumber-1)*2;
        int playerStartPositionY = 0;
        this.playerPiece = new PlayerPiece(new Position(playerStartPositionX, playerStartPositionY), 200, Direction.NORTH, playerNumber);
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
        //check if the move kills the player
        if (isDeadMove(newX, newY)) {
            playerPiece.showDeadPlayer();
            isDead = true;
        }

        //if position has changed and player isn't dead, update logic grid
        if ((newY != pos.getY() || newX != pos.getX()) && !isDead()) {
            playerPiece.setPos(new Position(newX, newY));
            map.movePlayerToNewPosition(pos, new Position(newX, newY));
            BoardPiece currPiece;
            for (int i = 0; i < pieceGrid[newX][newY].size(); i++) {
                currPiece = pieceGrid[newX][newY].get(i);
                if(currPiece instanceof ExpressBeltPiece){currentBoardPiece = currPiece;}
                else if(currPiece instanceof ConveyorBeltPiece){currentBoardPiece = currPiece;}
                else if(currPiece instanceof CogPiece){currentBoardPiece = currPiece;}
                else if(currPiece instanceof PusherPiece){currentBoardPiece = currPiece;}
                else if(currPiece instanceof FlagPiece){currentBoardPiece = currPiece;}
                else if(currPiece instanceof AbyssPiece){currentBoardPiece = currPiece;}
                else if(currPiece instanceof SpawnPointPiece){currentBoardPiece = currPiece;}
                else if(currPiece instanceof FloorPiece){currentBoardPiece = currPiece;}
            }
            System.out.println(getPlayerPiece().getDir());
        }
        setPos(newX, newY);
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
        int MAP_WIDTH = game.getMap().getWidth();
        int MAP_HEIGHT = game.getMap().getHeight();
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

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean isDead() {
        return isDead;
    }

    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    public void turnPlayerAround() { playerPiece.turnAround(); }

    public void turnPlayerLeft() { playerPiece.turnLeft(); }

    public void turnPlayerRight() { playerPiece.turnRight(); }

    public ArrayList<ProgramCard> getPlayerHandDeck() {
        return playerHandDeck;
    }

    public ArrayList<ProgramCard> getSelectedCards() {
        return selectedCards;
    }

    public void setPlayerHandDeck(ArrayList<ProgramCard> playerHandDeck) {
        this.playerHandDeck = playerHandDeck;
    }

    public void setSelectedCards(ArrayList<ProgramCard> selectedCards) {
        this.selectedCards = selectedCards;
    }

    /**
     * Sets the first five cards in the given hand of nine cards, to be the chosen five cards in a round
     */
   public void pickFirstFiveCards() {
        int NUMBER_OF_CARDS_TO_CHOOSE = 5;
        ArrayList<ProgramCard> firstFiveCards = new ArrayList<>();
        if (playerHandDeck.size() >= NUMBER_OF_CARDS_TO_CHOOSE) {
            for (int i = 0; i < NUMBER_OF_CARDS_TO_CHOOSE; i++) {
                firstFiveCards.add(playerHandDeck.get(i));
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

    public void takeDamage(int amountOfDamage){
       damage += amountOfDamage;
        if(damage >= 10){
            lifes--;
            damage = 10;
        }
       System.out.println("Damage: " + damage);

        if(damage >= 5 && damage <= 9){
           for (int i = damage-amountOfDamage-4; i < damage-4; i++) {
               lockedCards.add(0, selectedCards.get(4-i));
               playerHandDeck.remove(lockedCards.get(0));
               System.out.println("Locking card: " + lockedCards.get(0).toString());
           }
       }
        System.out.println("Number of cards in playerhand: " + playerHandDeck.size());
        System.out.println("Number of locked cards: " + lockedCards.size());
   }

    public void repairDamage(int amountOfRepairs){
       damage -= amountOfRepairs;
        if(damage < 0){
            damage = 0;
        }
       System.out.println("Damage: " + damage);

       int numberOfCardsToUnlock =  lockedCards.size()-(damage - 4);
       if (numberOfCardsToUnlock > 0 && lockedCards.size() > 0) {
           for (int i = 0; i < numberOfCardsToUnlock; i++) {
               System.out.println("Unlocking card: " + lockedCards.get(0).toString());
               playerHandDeck.add(lockedCards.get(0));
               lockedCards.remove(0);
            }
        }
       System.out.println("Number of cards in playerhand: " + playerHandDeck.size());
       System.out.println("Number of locked cards: " + lockedCards.size());
   }

    public int getDamage(){return damage;}

    public void loseLife(){lifes--; System.out.println("Lifes: " + lifes);}

    //TODO remove this later since it is not possible to gain a life. This is for testing purposes only.
    public void gainLife(){lifes++; System.out.println("Lifes: " + lifes);}

    public int getLifes(){return lifes;}

    public void visitedCheckpoint(){checkpointsVisited++; System.out.println("Checkpoints: " + checkpointsVisited);}

    //TODO remove this later since it is not possible to undo a checkpoint/flag. This is for testing purposes only.
    public void removeCheckpoint(){checkpointsVisited--; System.out.println("Checkpoints: " + checkpointsVisited);}

    public int getCheckpointsVisited(){return checkpointsVisited;}


    public boolean isOnConveyorBelt(){
        if (currentBoardPiece instanceof ConveyorBeltPiece) {
            System.out.println("Standing on Conveyorbelt");
            return true;
        }
        return false;
    }

    public boolean isOnExpressBelt(){
        if (currentBoardPiece instanceof ExpressBeltPiece) {
            System.out.println("Standing on Expressbelt");
            return true;
        }
        return false;
    }

    public boolean isOnCog(){
       if(currentBoardPiece instanceof CogPiece){
           String rotation;
           if(((CogPiece) currentBoardPiece).isRotateClockwise()){rotation = "Clockwise cog";}
           else{rotation = "Counterclockwise cog";}
           System.out.println("Standing on " + rotation);
           return true;
       }
       return false;
    }

    public BoardPiece getCurrentBoardPiece(){return currentBoardPiece;}

}