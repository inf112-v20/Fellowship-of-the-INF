package inf112.skeleton.app.grid;

import inf112.skeleton.app.cards.CardType;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    /**
     * @return the direction opposite to itself
     */
    public Direction getOppositeDirection() {
        switch(this) {
            case SOUTH: return Direction.NORTH;
            case NORTH: return Direction.SOUTH;
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
            default: throw new IllegalArgumentException("No such direction");
        }
    }

    /**
     * @return the direction to the right of itself
     */
    public Direction getRightTurnDirection() {
        switch(this) {
            case SOUTH: return Direction.WEST;
            case NORTH: return Direction.EAST;
            case EAST: return Direction.SOUTH;
            case WEST: return Direction.NORTH;
            default: throw new IllegalArgumentException("No such direction");
        }

    }

    /**
     * @return the direction to the left of itself
     */
    public Direction getLeftTurnDirection() {
        switch(this) {
            case SOUTH: return Direction.EAST;
            case NORTH: return Direction.WEST;
            case EAST: return Direction.NORTH;
            case WEST: return Direction.SOUTH;
            default: throw new IllegalArgumentException("No such direction");
        }
    }

    /**
     * @return the new direction from using a rotation card
     */
    public Direction getCardTurnDirection(CardType cardType) {
        switch(cardType) {
            case ROTATELEFT: return this.getLeftTurnDirection();
            case ROTATERIGHT: return this.getRightTurnDirection();
            case UTURN: return this.getOppositeDirection();
            default: return this;
        }
    }

}



