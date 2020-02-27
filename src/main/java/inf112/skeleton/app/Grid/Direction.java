package inf112.skeleton.app.Grid;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public Direction getOppositeDirection() {
        switch(this) {
            case SOUTH: return Direction.NORTH;
            case NORTH: return Direction.SOUTH;
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
            default: return null; //no other possible directions
        }
    }

    public Direction getRightTurnDirection() {
        switch(this) {
            case SOUTH: return Direction.WEST;
            case NORTH: return Direction.EAST;
            case EAST: return Direction.SOUTH;
            case WEST: return Direction.NORTH;
            default: return null;
        }
    }

    public Direction getLeftTurnDirection() {
        switch(this) {
            case SOUTH: return Direction.EAST;
            case NORTH: return Direction.WEST;
            case EAST: return Direction.NORTH;
            case WEST: return Direction.SOUTH;
            default: return null;
        }
    }

}



