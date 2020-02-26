package inf112.skeleton.app.Grid;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public Direction getOppositeDirection() {
        switch(this) {
            case SOUTH: return Direction.NORTH;
            case NORTH: return Direction.SOUTH;
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
            default: return null; //TODO deal with this
        }
    }

}



