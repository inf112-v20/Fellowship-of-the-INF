package inf112.skeleton.app.player;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.game_logic.Game;
import inf112.skeleton.app.game_logic.Move;
import inf112.skeleton.app.game_logic.MovesToExecuteSimultaneously;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.deck.GameDeck;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;

import java.util.ArrayList;
import java.util.Arrays;

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

    private Position spawnPoint;
    private int damage;
    private int playerNumber;
    private int lives = 3;
    private int checkpointsVisited;
    private BoardPiece currentBoardPiece;
    private Direction latestMoveDirection;
    private boolean conveyorBeltMove = false;
    private boolean hasBeenMovedThisPhase = false;
    private boolean isOnLaser = false;

    public Player(int playerNumber, Game game) {
        this.playerNumber = playerNumber;
        this.logicGrid = game.getLogicGrid();
        this.pieceGrid = logicGrid.getGrid();
        this.damage = 0;
        this.checkpointsVisited = 0;
        GameDeck gameDeck = game.getGameDeck();
        this.playerHandDeck = game.getGameDeck().drawHand(new ArrayList<ProgramCard>(), getDamage());
        this.selectedCards = new ProgramCard[5];
        this.lockedCards = new ArrayList<>();


        //Find the spawn point of the player, and set spawnPoint position to the first spawn
        findFirstSpawnPoint();

        this.playerPiece = new PlayerPiece(spawnPoint, 200, Direction.NORTH, this);
        this.isDead = false;
        this.powerDownMode = false;
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
     */
    public void tryToGo(Direction newDirection, MovesToExecuteSimultaneously moves) {
        Move move = new Move(this);
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
            currentBoardPiece = pieceGrid[spawnPoint.getX()][spawnPoint.getY()].get(0);
            latestMoveDirection = newDirection;
            this.conveyorBeltMove = false;
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
        else if (lives >= 0 && isDead()) {
            respawnPlayer();
            setPowerDownMode(true);
        }

        //TODO Add what happens when a player runs out of lives? @Johanna
        //Handle what happens if the player runs out of lives
        else {
            respawnPlayer();
            setPowerDownMode(true);
            isDead = true;
        }
    }

    /**
     * This class is identical to the other tryToGo, but does not handle a moves object.
     * This is used when tryToGo is used on a conveyor belt, as conveyor belts deal with collision differently.
     *
     * @param newDirection new direction to move the player
     */
    public void tryToGo(Direction newDirection) {
        //TODO: duplicate code, refactor
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
            currentBoardPiece = pieceGrid[spawnPoint.getX()][spawnPoint.getY()].get(0);
            latestMoveDirection = newDirection;
            this.conveyorBeltMove = false;
            loseLife();

            //game.getLogicGrid().removePlayerFromMap(playerPiece.getPos());
            //return;
        }

        //if position has changed and player isn't dead, update logic grid
        if ((newY != pos.getY() || newX != pos.getX()) && !isDead()) {
            setCurrentBoardPiece(newX, newY); //update currentBoardPiece
            setPos(newX, newY);
            latestMoveDirection = newDirection;
            this.conveyorBeltMove = false;
        }

        //If the player still have lives left, respawn it, but set it in shutdown mode
        else if (lives >= 0 && isDead()) {
            respawnPlayer();
            setPowerDownMode(true);
        }


        //TODO Add what happens when a player runs out of lives? @Johanna
        //Handle what happens if the player runs out of lives
        else {
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
     * Does not check if move is legal, moves player in direction regardless
     * This method is only used when moving a player on a conveyor belt, as it removes the aspect of pushing
     * TODO: find a better solution for moving robots on a conveyor belt
     *
     * @param newDirection
     */
    public void moveOnConveyorBelt(Direction newDirection) {
        Position pos = playerPiece.getPos();
        //set position of player to be one cell further down the conveyorbelt
        setPos(pos.getPositionIn(newDirection));
        int newX = pos.getX();
        int newY = pos.getY();
        setCurrentBoardPiece(newX, newY); //set the new current board piece
        //check if the move kills the player, if so lose a life
        if (isDeadMove(newX, newY)) {
            loseLife();
        }
    }

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
            } else if (currPiece instanceof FloorPiece) {
                currentBoardPiece = currPiece;
            } else if (currPiece instanceof FlagPiece) {
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
     * Checks if a move in a certian direction is legal.
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
                //TODO error handling as default maybe?
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

    /**
     * Sets the first five cards in the given hand of nine cards, to be the chosen five cards in a round
     */
    public void pickFirstFiveCards() {
        for (int i = 0; i < 5 - lockedCards.size() ; i++) {
            selectedCards[i] = playerHandDeck.get(i);
        }
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
        damage += amountOfDamage;
        if (damage >= 10) {
            lives--;
            damage = 10;
        }
        if (damage >= 5 && damage <= 9) {
            for (int i = damage - amountOfDamage - 4; i < damage - 4; i++) {
                lockedCards.add(0, selectedCards[4 - i]);
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

    //TODO remove this later since it is not possible to gain a life. This is for testing purposes only.
    public void gainLife() {
        lives++;
    }

    public int getLives() {
        return lives;
    }

    public void visitedCheckpoint() {
        checkpointsVisited++;
    }

    //TODO remove this later since it is not possible to undo a checkpoint/flag. This is for testing purposes only.
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
        return (currentBoardPiece instanceof ConveyorBeltPiece); //TODO @Erlend these methods are unnecessary, as they are only one line
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

}