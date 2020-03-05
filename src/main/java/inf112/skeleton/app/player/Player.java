package inf112.skeleton.app.player;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.Map;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.AbyssPiece;
import inf112.skeleton.app.grid_objects.BoardPiece;
import inf112.skeleton.app.grid_objects.PlayerPiece;
import inf112.skeleton.app.grid_objects.WallPiece;

import java.util.ArrayList;

/**
 * Class representing a player.
 */
public class Player {

    private Position pos; //position of player
    private Position spawnPoint; //Current spawn point
    private TiledMapTileLayer.Cell currentCell; //Cell for the current state of the player
    private TiledMapTileLayer.Cell playerCell; //cell for normal player
    private TiledMapTileLayer.Cell deadPlayerCell; //cell for dead player looks
    private TiledMapTileLayer.Cell wonPlayerCell; //cell for player who has won looks
    private int MAP_WIDTH;
    private int MAP_HEIGHT;
    private Map map;
    private ArrayList<BoardPiece>[][] pieceGrid;
    private PlayerPiece playerPiece;
    private Game game;
    private ArrayList<ProgramCard> playerHandDeck;
    private ArrayList<ProgramCard> selectedCards;
    private int damage;
    private int playerNumber;
    private int lives = 3;
    private int checkpointsVisited;

    public Player(int playerNumber, Game game) {
        this.playerNumber = playerNumber;
        this.map = game.getMap();
        this.pieceGrid = map.getGrid();
        this.game = game;
        this.damage = 0;
        GameDeck gameDeck = game.getGameDeck();
        this.playerHandDeck = game.getGameDeck().drawHand(new ArrayList<ProgramCard>(), getDamage());
        this.selectedCards = new ArrayList<>();

        //player is player is placed at bottom of board in position of player number
        int playerStartPositionX = (playerNumber-1)*2;
        int playerStartPositionY = 0;

        this.spawnPoint = new Position(playerStartPositionX, playerStartPositionY);

        this.playerPiece = new PlayerPiece(spawnPoint, 200, Direction.NORTH);
        pos = new Position(spawnPoint.getX(), spawnPoint.getY());

        MAP_WIDTH = game.getMap().getWidth();
        MAP_HEIGHT = game.getMap().getHeight();

        playerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 0)));
        deadPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 1)));
        wonPlayerCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(TextureMaker.getPlayerTextureRegion(playerNumber, 2)));
        currentCell = playerCell; //appearance starts as normal player
    }


    /**
     * Getter for position
     * @return position of player
     */
    public Position getPos() {
        return pos;
    }

    /**
     * Setter for position of player
     *
     * @param x new x position
     * @param y new y position
     */
    public void setPos(int x, int y) {
        pos.setX(x);
        pos.setY(y);
    }

    /**
     * Getter for player
     * @return player
     */
    public TiledMapTileLayer.Cell getPlayerCell() {
        return currentCell;
    }

    public TiledMapTileLayer.Cell getStandardPlayerCell() {
        return playerCell;
    }

    /**
     * Tries to move the player in a new direction
     *
     * @param newDirection new direction to move the player
     */
    public void tryToGo(Direction newDirection) {
        int newX = pos.getX();
        int newY = pos.getY();
        playerPiece.setDir(newDirection);
        switch (newDirection) {
            case NORTH:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newY += 1;
                if (isDeadMove(pos.getX(), newY)) loseLife();
                break;
            case SOUTH:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newY -= 1;
                if (isDeadMove(pos.getX(), newY)) loseLife();
                break;
            case WEST:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newX -= 1;
                if (isDeadMove(newX, pos.getY())) loseLife();
                break;
            case EAST:
                if (isLegalMove(pos.getX(), pos.getY(), newDirection)) newX += 1;
                if (isDeadMove(newX, pos.getY())) loseLife();
                break;
        }

        //if position has changed and player isn't dead, update logic grid
        if ((newY != pos.getY() || newX != pos.getX()) && !isDead()) {
            playerPiece.setPos(new Position(newX, newY));
            map.movePlayerToNewPosition(pos, new Position(newX, newY));
            setPos(newX, newY);
        }
        //If the player still have lives left, respawn it
        else if (isDead() && lives>=0) {
            respawnPlayer();
        }
        //Handle what happens if the player runs out of lives
        else {
            setPos(newX, newY);
        }

        /* UNCOMMENT TO SEE PRINTOUT OF PIECES IN CELL
        if (newY != pos.getY() || newX != pos.getX() && !isDead()) {
            System.out.println("OLD: " + pos.getX() + "," + pos.getY());

            ArrayList<BoardPiece> array = pieceGrid[pos.getX()][pos.getY()];
            for (int i = 0; i < array.size(); i++) {
                BoardPiece p = array.get(i);l
                System.out.println(p);
            }

            System.out.println("NEW: " + newX + "," + newY);
            array = pieceGrid[newX][newY];
            for (int i = 0; i < array.size(); i++) {
                BoardPiece p = array.get(i);
                System.out.println(p);
            }
        }*/
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
                    if (!isLegalMoveInDirection(x+1, y, currPiece, dir, i)) {
                        return false;
                    }
                    break;
                case WEST:
                    if (!isLegalMoveInDirection(x-1, y, currPiece, dir, i)) {
                        return false;
                    }
                    break;
                case NORTH:
                    if (!isLegalMoveInDirection(x, y+1, currPiece, dir, i)) {
                        return false;
                    }
                    break;
                case SOUTH:
                    if (!isLegalMoveInDirection(x, y-1, currPiece, dir, i)) {
                        return false;
                    }
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
        BoardPiece currPiece;
        if (x > MAP_WIDTH - 1 || x < 0 || y < 0 || y > MAP_WIDTH - 1) {
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
                turnPlayerAround();
                tryToGo(getPlayerPiece().getDir());
                turnPlayerAround();
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
        return currentCell.equals(deadPlayerCell);
    }

    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    /*
    This now rotates the cell also which is the image of the player.
    A cells rotation is an integer between 0 and 3, where 0 = North, 1 = West, 2 = South, 3 = East.
    This does not affect the playerPiece direction in any way.
     */

    public void turnPlayerAround() {
        playerPiece.turnPieceInOppositeDirection();
        int newDir = currentCell.getRotation() + 2;
        if (newDir > 3){ newDir -= 4;}
        currentCell.setRotation(newDir);
    }

    public void turnPlayerLeft() {
        playerPiece.rotatePieceLeft();
        int newDir = currentCell.getRotation() + 1;
        if (newDir > 3){ newDir -= 4;}
        currentCell.setRotation(newDir);
    }

    public void turnPlayerRight() {
        playerPiece.rotatePieceRight();
        int newDir = currentCell.getRotation() + 3;
        if (newDir > 3){ newDir -= 4;}
        currentCell.setRotation(newDir);
    }

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
    public Position getSpawnPoint() { return spawnPoint; }

    public void setSpawnPoint(int x, int y) { spawnPoint = new Position(x, y); }

    public void respawnPlayer() {
        currentCell = playerCell;
        setPos(spawnPoint.getX(), spawnPoint.getY());
        playerPiece.setPos(new Position(spawnPoint.getX(), spawnPoint.getY()));
        map.movePlayerToNewPosition(pos, new Position(spawnPoint.getX(), spawnPoint.getX()));
    }

    public void takeDamage(int amountOfDamage){ damage += amountOfDamage; System.out.println("Damage: " + damage); }

    public void repairDamage(int amountOfRepairs){damage -= amountOfRepairs; System.out.println("Damage: " + damage);}

    public int getDamage(){return damage;}

    public void loseLife(){
        lives--;
        currentCell = deadPlayerCell;
        System.out.println("Lives: " + lives);
    }

    //TODO remove this later since it is not possible to gain a life. This is for testing purposes only.
    public void gainLife(){lives++; System.out.println("Lives: " + lives);}

    public int getLives(){return lives;}

    public void visitedCheckpoint(){checkpointsVisited++; System.out.println("Checkpoints: " + checkpointsVisited);}

    //TODO remove this later since it is not possible to undo a checkpoint/flag. This is for testing purposes only.
    public void removeCheckpoint(){checkpointsVisited--; System.out.println("Checkpoints: " + checkpointsVisited);}

    public int getCheckpointsVisited(){return checkpointsVisited;}


}