package inf112.skeleton.app.player;

import com.badlogic.gdx.Gdx;
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
    private ArrayList<Position> respawnPositions = new ArrayList<>();

    private Position spawnPoint;
    private BoardPiece currentBoardPiece;
    private Direction latestMoveDirection;
    private boolean conveyorBeltMove = false;
    private boolean hasBeenMovedThisPhase = false;
    private boolean hasBeenPushedThisPhase = false;
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
    private Player pusherStarter = null;

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

    /*
    Getters
     */

    public Position getPos() { return this.playerPiece.getPos(); }

    public TiledMapTileLayer.Cell getPlayerCell() { return this.playerPiece.getCurrentCell(); }

    public Position getSpawnPoint() {
        return this.spawnPoint;
    }

    public boolean isPowerDownMode() {
        return this.powerDownMode;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public PlayerPiece getPlayerPiece() {
        return this.playerPiece;
    }

    public ArrayList<ProgramCard> getPlayerHandDeck() {
        return this.playerHandDeck;
    }

    public ProgramCard[] getSelectedCards() {
        return this.selectedCards;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getLives() {
        return this.lives;
    }

    public boolean hasBeenPushedThisPhase() { return this.hasBeenPushedThisPhase; }

    public boolean isPermanentlyDead() {
        return this.isPermanentlyDead;
    }

    private Player getPusherStarter(){
        return this.pusherStarter;
    }

    public boolean isKeyInput() { return this.keyInput; }

    public int getCheckpointsVisited() {
        return this.checkpointsVisited;
    }

    public ArrayList<ProgramCard> getLockedCards() {
        return this.lockedCards;
    }

    public BoardPiece getCurrentBoardPiece() {
        return this.currentBoardPiece;
    }

    public Direction latestMoveDirection() {
        return this.latestMoveDirection;
    }

    public boolean isLatestMoveConveyorBelt() {
        return this.conveyorBeltMove;
    }

    public Boolean hasBeenMovedThisPhase() {
        return this.hasBeenMovedThisPhase;
    }

    public ArrayList<Position> getLaserPath() {
        return this.laserPath;
    }

    public Position getOldLaserPos() {
        return this.oldLaserPos;
    }

    public boolean hasLockedIn(){
        return this.hasLockedIn;
    }

    public Position getLastPosAlive(){
        return this.lastPosAlive;
    }

    public ArrayList<Position> getRespawnPositions(){
        return this.respawnPositions;
    }

    public Position getDeadPosition(){
        return this.deadPosition;
    }


    /*
    Setters
     */

    public void setPos(Position positionIn) {
        this.playerPiece.setPos(positionIn);
    }

    public void setSpawnPoint(Position pos) {
        this.spawnPoint = pos;
    }

    public void setPowerDownMode(boolean a) {
        this.powerDownMode = a;
    }

    public void setPlayerHandDeck(ArrayList<ProgramCard> playerHandDeck) {
        this.playerHandDeck = playerHandDeck;
    }

    public void setIsDead(boolean bool){
       this.isDead = bool;
    }

    private void setPusherStarter(Player pusherStarter){
        this.pusherStarter = pusherStarter;
    }

    public void setHasBeenPushedThisPhase(boolean hasBeenPushed) {
        this.hasBeenPushedThisPhase = hasBeenPushed;
    }

    public void setConveyorBeltMove(boolean move) {
        this.conveyorBeltMove = move;
    }

    public void setHasBeenMovedThisPhase(boolean bool) {
        this.hasBeenMovedThisPhase = bool;
    }

    public void setOldLaserPos(Position pos) { this.oldLaserPos = pos; }

    public void setLockedIn(boolean bool){
        this.hasLockedIn = bool;
    }

    public void setKeyInput(boolean bool){
        this.keyInput = bool;
    }


    @Override
    public String toString() {
        return "Player{" +
                "playerNumber=" + playerNumber +
                '}';
    }


    /**
     * Tries to move the player in a new direction
     *
     * @param newDirection new direction to move the player
     * @param moves        list that moves created can be added to
     */
    public void tryToGo(Direction newDirection, MovesToExecuteSimultaneously moves) {
        Position oldPosition = getPos();
        //if the move is illegal, the robot does not move
        if (!isLegalMoveInDirection(oldPosition, newDirection)) return;
        Position newPosition = oldPosition.getPositionIn(newDirection);
        Move move = new Move(this);

        //check if the move kills the player, if so lose a life
        if (logicGrid.isDeadMove(newPosition)) {
            latestMoveDirection = newDirection;
            loseLife();
            move.updateMove();
            moves.add(move);
        }
        //if the player isn't dead, update logic grid
        if (!isDead()) {
            //if the move results in pushing robots, add the resulting moves to the moves list
            addMovesForPushedRobots(this.getPlayerPiece(), newDirection, moves);
            setCurrentBoardPiece(newPosition.getX(), newPosition.getY()); //update currentBoardPiece
            setPos(newPosition);
            move.updateMove();
            moves.add(move);
            latestMoveDirection = newDirection;
            this.conveyorBeltMove = false;
        }

        checkForRespawnAfterKeyBoardInput(moves); //checks if respawn should be called
    }


    /**
     *According to the Roborally rules respawns only happen at round end, so this code is only useful for
     * immediately respawning a player after using keyboard inputs to move the player.
     *Currently players are respawned at round end in finishRound() in Round.java.
     * Anyways, it should work as intended using keyboard inputs also.
     * @param moves list of moves to update
     */
    public void checkForRespawnAfterKeyBoardInput(MovesToExecuteSimultaneously moves) {
        if(!this.isPushed && keyInput) {
            movesLeft--;
            if (movesLeft == 0) {
                for (int i = 0; i < playersPushed.size(); i++) {
                    System.out.println(playersPushed);
                    Player pushedPlayer = playersPushed.get(i);
                    pushedPlayer.isPushed = false;
                    if (pushedPlayer.isDead() && game.getRound().getPhaseNr() == 0) {
                        pushedPlayer.checkForRespawn(moves);
                        pushedPlayer.playersPushed.clear();
                    }
                }
                playersPushed.clear();
                if (isDead && game.getRound().getPhaseNr() == 0) {
                    checkForRespawn(moves);
                }
                movesLeft = 1;
            }
            pusherStarter = null;
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
            if(!isPushed){
                setPusherStarter(this);
            }
            Player pusherStarter = getPusherStarter();
            playertoPush.setPusherStarter(pusherStarter);
            pusherStarter.playersPushed.add(0, playertoPush);
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
        if (lives > 0 && isDead()) {
            respawnPlayer(moves);
        }
    }

    /**
     *Put the player back to it's respawn position, update boardPiece and moves
     * @param moves list to update
     */
    public void respawnPlayer(MovesToExecuteSimultaneously moves) {
        game.performMoves(moves);
        Move respawnMove = new Move(this);
        System.out.println("Finding spawn positions for " + toString());
        respawnPositions = logicGrid.getValidSpawnPointPosition(spawnPoint);
        if(this instanceof AIPlayer) {
            AIPlayer aiPlayer = (AIPlayer) this;
            Position newPos = aiPlayer.chooseRespawnPos(respawnPositions);
            System.out.println("Respawning " + toString() + " at " + newPos);
            playerPiece.setPos(newPos);
            Direction newDir = aiPlayer.chooseRespawnDir(newPos);
            getPlayerPiece().setDir(newDir);
        }
        else{
            playerPiece.showAlivePlayer();
            if(keyInput){
                game.getDeadPlayers().clear();
                game.setChoosingRespawn(true);
            }
            return;
        }
        respawnMove.updateMove();
        moves.add(respawnMove);
        isDead = false;
        playerPiece.showAlivePlayer();

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
     * Player executes the move on the given card
     *
     * @param programCard to convert to player move
     * @param moves       list of moves to add move objects to
     */
    public void executeCardAction(ProgramCard programCard, MovesToExecuteSimultaneously moves) {
        keyInput = false;
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
                if (!isDead) playerPiece.turnAround(moves);
                break;
            case BACKUP:
                tryToGo(getPlayerPiece().getDir().getOppositeDirection(), moves);
                break;
            case ROTATELEFT:
                if (!isDead) playerPiece.turnLeft(moves);
                break;
            case ROTATERIGHT:
                if (!isDead) playerPiece.turnRight(moves);
                break;
            default:
                break;
        }
    }



    public void addCardsToSelectedCards(ArrayList<ProgramCard> cards){
        for (int i = 0; i < cards.size() ; i++) {
            selectedCards[i] = cards.get(i);
        }

        //TODO Remove. Temporary fix to stop nullpointers
        for (ProgramCard selectedCard : selectedCards) {
            if (selectedCard == null) {
                System.out.println("Couldn't find enough cards for AI Player using their preferred card choosing method\n" +
                        "Picking random cards to fill their slots instead");
                pickRandomCards();
            }
        }

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
        if(amountOfDamage <= 0){
            return;
        }
        this.damage++;
        if(damage >= 10){
            loseLife();
            MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
            Move deadMove = new Move(playerPiece, lastPosAlive, deadPosition, playerPiece.getDir(), playerPiece.getDir());
            moves.add(deadMove);
            game.executeMoves(moves);
            return;
        }
        if(damage >= 5){
            ProgramCard cardToLock = selectedCards[9-damage];
            lockedCards.add(cardToLock);
            //System.out.println("Locking card " + cardToLock);
            playerHandDeck.remove(cardToLock);
        }
        takeDamage(amountOfDamage-1);

    }

    /**
     * Method to be used when the player decides to enter power down mode
     */
    public void doPowerDown() {
        setPowerDownMode(true);
        repairDamage(damage);
        setLockedIn(true);
    }

    /**
     * Heals a player a given amount.
     * Will unlock a players selected cards (from left to right)
     * if the player already have locked cards.
     *
     * @param amountOfRepairs the number of damage to remove from the player
     */
    public void repairDamage(int amountOfRepairs) {
        if(damage <= 0 || amountOfRepairs <= 0)return;
        if(damage >= 5){
            ProgramCard cardToUnlock = selectedCards[9-damage];
            if(cardToUnlock != null){
                lockedCards.remove(cardToUnlock);
                playerHandDeck.add(cardToUnlock);
            }
        }
        damage--;
        repairDamage(amountOfRepairs-1);
    }

    /**
     * Remove a life from the life counter, and turn the player cell into a dead player cell
     */
    public void loseLife() {
        lives--;
        isDead = true;
        playerPiece.showDeadPlayer();
        lastPosAlive = getPos();
        setPos(deadPosition);
        playerHandDeck.addAll(lockedCards);
        lockedCards.clear();
        if (lives == 0) {
            System.out.println(toString() + " is dead and out of the game");
            MovesToExecuteSimultaneously moves = new MovesToExecuteSimultaneously();
            Move permaDeadMove = new Move(playerPiece, lastPosAlive, deadPosition, playerPiece.getDir(), playerPiece.getDir());
            moves.add(permaDeadMove);
            setPowerDownMode(true);
            isPermanentlyDead = true;
            game.getGameDeck().moveAll(game.getGameDeck().getDiscardDeck(), playerHandDeck);
            game.executeMoves(moves);
            return;
        }
        game.getDeadPlayers().add(this);
        damage = 2;
    }

    /**
     * Get the spawn point position from a list of spawn points
     */
    public void findFirstSpawnPoint() {
        ArrayList<Position> spawns = logicGrid.getSpawnPointPositions();
        this.spawnPoint = new Position(spawns.get(playerNumber - 1).getX(), spawns.get(playerNumber - 1).getY());
    }

    /**
     * Method for testing.
     */
    public void gainLife() { lives++; }

    public void visitedCheckpoint() { checkpointsVisited++; }

    /**
     * Method for testing.
     */
    public void removeCheckpoint() { checkpointsVisited--; }

    public void removeSelectedCards() { Arrays.fill(selectedCards, null); }


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
        ArrayList<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < playerHandDeck.size(); i++) {
            if(isCardAvailable(playerHandDeck.get(i))){
                randomNumbers.add(i);
            }
        }
        Collections.shuffle(randomNumbers);
        int missingCards = cardsMissing();
        //System.out.println(toString() + " damage: " + damage + " cards missing: " + missingCards + " random numbers size: "
          //      + randomNumbers.size() + " player hand deck size: " + playerHandDeck.size());
        for (int i = 0; i < missingCards; i++) {
            int randomNumber = randomNumbers.get(i);
            ProgramCard card = playerHandDeck.get(randomNumber);
            for (int j = 0; j < selectedCards.length; j++) {
                if(selectedCards[j] == null){
                    selectedCards[j] = card;
                    break;
                }
            }
        }
        setLockedIn(true);
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

    private int cardsMissing(){
        int counter = 0;
        for (ProgramCard selectedCard : selectedCards) {
            if (selectedCard == null) {
                counter++;
            }
        }
        return counter;
    }

}