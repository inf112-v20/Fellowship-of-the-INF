package inf112.skeleton.app.player;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.skeleton.app.game_logic.Game;
import inf112.skeleton.app.game_logic.Move;
import inf112.skeleton.app.game_logic.MovesToExecuteSimultaneously;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.grid.Direction;
import inf112.skeleton.app.grid.LogicGrid;
import inf112.skeleton.app.grid.Position;
import inf112.skeleton.app.grid_objects.*;

import java.util.*;

/**
 * Class representing a player.
 */
public class Player {
    private LogicGrid logicGrid;
    private ArrayList<BoardPiece>[][] pieceGrid;
    private PlayerPiece playerPiece;
    private ArrayList<ProgramCard> playerHandDeck;
    private ProgramCard[] selectedCards = new ProgramCard[5];
    private ArrayList<ProgramCard> lockedCards = new ArrayList<>();
    private ArrayList<Position> laserPath = new ArrayList<>();
    private ArrayList<Player> playersPushed = new ArrayList<>();

    private Position spawnPoint;
    private BoardPiece currentBoardPiece;
    private Direction latestMoveDirection;
    private boolean conveyorBeltMove = false;
    private boolean hasBeenMovedThisPhase = false;
    private Position oldLaserPos;
    private Game game;
    private boolean isDead = false;
    private boolean isPermanentlyDead = false;
    private boolean powerDownMode = false;
    private int damage = 0;
    private int playerNumber;
    private int lives = 3;
    private int checkpointsVisited = 0;
    private boolean hasLockedIn = false;
    private boolean isPushed = false;
    private Position lastPosAlive;
    private Position deadPosition;
    private int movesLeft = 1;
    private boolean keyInput = false;

    public Player(int playerNumber, Game game) {
        this.playerNumber = playerNumber;
        this.game = game;
        this.logicGrid = game.getLogicGrid();
        this.pieceGrid = logicGrid.getGrid();
        this.playerHandDeck = game.getGameDeck().drawHand(new ArrayList<ProgramCard>(), getDamage());

        //Find the spawn point of the player, and set spawnPoint position to the first spawn
        findFirstSpawnPoint();
        this.playerPiece = new PlayerPiece(spawnPoint, 200, Direction.NORTH, this);
        this.oldLaserPos = new Position(-1, 0);
        this.deadPosition = new Position(playerNumber*100, playerNumber*100);
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
     * @param moves        list that moves created can be added to
     */
    public void tryToGo(Direction newDirection, MovesToExecuteSimultaneously moves) {

        Move move = new Move(this);
        Position oldPosition = playerPiece.getPos();
        Position newPosition = oldPosition.getPositionIn(newDirection);

        //check if the move kills the player, if so lose a life
        if (isLegalMoveInDirection(oldPosition, newDirection) && isDeadMove(newPosition)) {
            latestMoveDirection = newDirection;
            loseLife();
            lastPosAlive = oldPosition;
            setPos(deadPosition);
            move.updateMove();
            moves.add(move);
            game.getDeadPlayers().add(this);
        }
        //if move is legal and player isn't dead, update logic grid
        if (isLegalMoveInDirection(oldPosition, newDirection) && !isDead()) {
            //if the move results in pushing robots, add the resulting moves to the moves list
            addMovesForPushedRobots(this.getPlayerPiece(), newDirection, moves);
            setCurrentBoardPiece(newPosition.getX(), newPosition.getY()); //update currentBoardPiece
            setPos(newPosition);
            move.updateMove();
            moves.add(move);
            latestMoveDirection = newDirection;
            this.conveyorBeltMove = false;
        }

        //TODO: read comment:
        /*
         * Respawns only happen at round end, so this code is only useful for immediately respawning a player
         * after using keyboard inputs to move the player. If this code is implemented, then moves from cards that
         * kill a player during a phase will immediately respawn the player after the card move. Perhaps this code
         * can be removed then? Currently players are respawned at round end in finishRound() in Round.java.
         * Anyways, it should work as intended using keyboard inputs also.
         */
        if(!this.isPushed && keyInput) {
            movesLeft--;
            if (movesLeft == 0) {
                for (int i = 0; i < playersPushed.size(); i++) {
                    Player pushedPlayer = playersPushed.get(i);
                    pushedPlayer.isPushed = false;
                    if (pushedPlayer.isDead()) {
                        pushedPlayer.checkForRespawn(moves);
                        pushedPlayer.playersPushed.clear();
                    }
                }
                playersPushed.clear();
                if (isDead) {
                    checkForRespawn(moves);
                }
                movesLeft = 1;
            }
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
            if (playertoPush.getPlayerNumber() == playerNumber) {
                System.out.println("Error: Player " + playerNumber + " can't push itself.");
                return;
            }
            playertoPush.isPushed = true;
            playersPushed.add(0, playertoPush);
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
            if(game.getPlayerAt(targetPosition) != null) {
                return game.getPlayerAt(targetPosition).getPlayerPiece();
            }
        return null; //return null if target position is not within grid
    }


    /**
     * Check if a player needs to be respawned
     * @param moves list to add respawn move to
     */
    public void checkForRespawn(MovesToExecuteSimultaneously moves) {
        //If the player still have lives left, respawn it, but set it in shutdown mode
        if (lives >= 0 && isDead()) {
            respawnPlayer(moves);
            setPowerDownMode(true);
        }

        //Handle what happens if the player runs out of lives
        else if (lives < 0 && isDead()) {
            Move permaDeadMove = new Move(playerPiece, lastPosAlive, deadPosition, playerPiece.getDir(), playerPiece.getDir());
            moves.add(permaDeadMove);
            setPos(deadPosition);
            setPowerDownMode(true);
            isDead = true;
            isPermanentlyDead = true;
        }
    }

    public void setSpawnPoint(int x, int y) {
        spawnPoint = new Position(x, y);
    }

    /**
     *Put the player back to it's respawn position, update boardPiece and moves
     * @param moves list to update
     */
    public void respawnPlayer(MovesToExecuteSimultaneously moves) {
        game.performMoves(moves);
        Move respawnMove = new Move(this);
        Position validSpawnPointPosition = logicGrid.getValidSpawnPointPosition(this, spawnPoint);
        setPos(validSpawnPointPosition);
        respawnMove.updateMove();
        moves.add(respawnMove);
        isDead = false;
        playerPiece.showAlivePlayer();
        if(keyInput){game.getDeadPlayers().clear();}
    }

    public void setPowerDownMode(boolean a) {
        powerDownMode = a;
    }

    public boolean isPowerDownMode() {
        return powerDownMode;
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
     * Checks if a move in a certain direction is legal.
     * The robot must be allowed to exit it's current cell.
     * The robot must be allowed to enter the destination cell.
     * There must not be a robot that cannot be pushed in the way.
     *
     * @param currentPosition position you are moving from
     * @param dir             direction we are checking if a move is legal in
     * @return true if move is legal
     */
    public boolean isLegalMoveInDirection(Position currentPosition, Direction dir) {
        if (!logicGrid.isInBounds(currentPosition)) {
            return false;
        }
        Position positionInFront = currentPosition.getPositionIn(dir);
        if (logicGrid.canLeavePosition(currentPosition, dir) && logicGrid.canEnterNewPosition(positionInFront, dir)) {
            //if the position in front is in bounds, check if there is a player there that can be pushed
            if (logicGrid.isInBounds(positionInFront)) {
                //If there is a player in the way of where you want to move, check that you can push that player
                PlayerPiece possiblePlayer = logicGrid.getPieceType(positionInFront, PlayerPiece.class);
                if (possiblePlayer != null) {
                    return isLegalMoveInDirection(positionInFront, dir);
                } else return true;
            } else return true;
        }
        return false;
    }

    /**
     * Method for checking if a move results in death
     *
     * @param position to check
     * @return whether the move results in death
     */
    public boolean isDeadMove(Position position) {
        int x = position.getX();
        int y = position.getY();
        BoardPiece currPiece;
        //if move is within bounds, check if move is to AbyssPiece
        if (logicGrid.isInBounds(position)) {
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
        keyInput = false;
        //Move rotateMove = new Move(this);
        movesLeft = programCard.getMovement();
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
                turnPlayerAround(moves);
                break;
            case BACKUP:
                tryToGo(getPlayerPiece().getDir().getOppositeDirection(), moves);
                break;
            case ROTATELEFT:
                turnPlayerLeft(moves);
                break;
            case ROTATERIGHT:
                turnPlayerRight(moves);
                break;
            default:
                break;
        }
        //rotateMove.updateMove(); //complete rotateMove object
        //moves.add(rotateMove);
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

    public void turnPlayerAround(MovesToExecuteSimultaneously moves) {
        if (!isDead()) {
            Move move = new Move(this);
            playerPiece.turnAround();
            move.updateMove();
            moves.add(move);
        }
    }

    /**
     * If the player is not dead, player is turned around
     * @param moves list to update
     */
    public void turnPlayerLeft(MovesToExecuteSimultaneously moves) {
        if (!isDead())  {
            Move move = new Move(this);
            playerPiece.turnLeft();
            move.updateMove();
            moves.add(move);
        }
    }

    public void turnPlayerRight(MovesToExecuteSimultaneously moves) {
        if (!isDead()) {
            Move move = new Move(this);
            playerPiece.turnRight();
            move.updateMove();
            moves.add(move);
        }
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


    /** TODO remove? can be useful for testing maybe
     * Picks the first cards in the hand so that selectedcards has 5 cards.

    public void pickFirstFiveCards() {
        for (int i = 0; i < 5 - lockedCards.size(); i++) {
            if (playerHandDeck.size() == 0) {
                continue;
            }
            selectedCards[i] = playerHandDeck.get(i);
        }
    }
     */

    @Override
    public String toString() {
        return "Player{" +
                "playerNumber=" + playerNumber +
                '}';
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
            int number = amountOfDamage;
            if (damage - number < 5) {
                number = damage - 4;
            }
            if (number > 5 - lockedCards.size()) {
                number = 5 - lockedCards.size();
            }
            for (int i = 0; i < number; i++) {
                lockedCards.add(0, selectedCards[(8 - (damage - number)) - i]);
                System.out.println("Locking card" + selectedCards[(8 - (damage - number)) - i]);
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

    public void visitedCheckpoint() { checkpointsVisited++; }

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

    public ArrayList<Position> getLaserPath() { return laserPath; }

    public Position getOldLaserPos() { return oldLaserPos; }

    public void setOldLaserPos(Position pos) { oldLaserPos = pos; }

    public void lockedIn(){ hasLockedIn = true; }

    public boolean hasLockedIn(){ return hasLockedIn; }

    public Position getLastPosAlive(){return  lastPosAlive;}

    public void setKeyInput(boolean bool){ keyInput = bool; }

    /**
     * Shoot laser in the direction which the robot is pointing.
     * Lasers stops at walls and players, and will damage any player hit
     */
    public void shootLaser() {
        Direction laserDir = playerPiece.getDir();
        Position laserPos = getPos();
        for (int i = 0; i < logicGrid.getHeight(); i++) {
            //System.out.println(player.toString() + " laser is at " + laserPos);
            if (!logicGrid.isInBounds(laserPos)) {
                return;
            }
            if (!logicGrid.positionIsFree(laserPos, 12) && !laserPos.equals(getPos())) {
                if (game.getPlayerAt(laserPos) == null) {
                    System.out.println("Error: " + toString() + " Couldn't shoot player");
                    return;
                }
                game.getPlayerAt(laserPos).takeDamage(1);
                //System.out.println(player.toString() + " hit " + game.getPlayerAt(laserPos).toString());
                laserPath.add(laserPos);
                return;
            }
            if (!logicGrid.canLeavePosition(laserPos, laserDir) ||
                    !logicGrid.canEnterNewPosition(laserPos.getPositionIn(laserDir), laserDir)) {
                return;
            }
            if (i == 0) {
                setOldLaserPos(laserPos);
            }
            laserPos = laserPos.getPositionIn(laserDir);
            laserPath.add(laserPos);
        }

    }

    /**
     * Picks random cards for the remaining open slots in the register
     */
    public void pickRandomCards(){
        if (playerHandDeck.isEmpty()) { return; }
        ArrayList<Integer> numbers = new ArrayList<>();
        boolean hasSelectedFiveCards = false;
        while (!hasSelectedFiveCards){
            if(numbers.size() == playerHandDeck.size()){
                System.out.println("Error: Couldn't choose enough random cards");
                return;
            }
            int randomNumber = (int)(Math.random()*playerHandDeck.size());
            if(numbers.contains(randomNumber)){continue;}
            ProgramCard card = playerHandDeck.get(randomNumber);
            if(isCardAvailable(card)){
                for (int i = 0; i < selectedCards.length; i++) {
                    if(selectedCards[i] == null){
                        selectedCards[i] = card;
                        break;
                    }
                    if(i == 4){hasSelectedFiveCards = true;}
                }
            }
            else{numbers.add(randomNumber);}
        }
        lockedIn();
    }

    /**
     * Checks if a card is available in hand
     *
     * @param card the card to check
     * @return true if that type of card is available in hand, false otherwise
     */
    public boolean isCardAvailable(ProgramCard card) {
        for (ProgramCard selectedCard : getSelectedCards()) {
            if (selectedCard == null) {
                continue;
            }
            if (selectedCard.equals(card)) {
                return false;
            }
        }
        return true;
    }

}