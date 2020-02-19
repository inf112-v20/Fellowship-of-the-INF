package inf112.skeleton.app.GridObjects;

/**
 * Generates the appropriate BoardPiece based on input id
 * The id is retrieved from the cell in a layer.
 */
public class BoardPieceGenerator {
    //position of piece to be generated
    private Position pos;

    public BoardPieceGenerator(int x, int y) {
        pos = new Position(x, y);
    }

    public BoardPiece generate(int id) {
        switch (id) {
            case (0):
                return null;
            case (5):
                return new FloorPiece(pos, id);
            case (15):
                return new RepairPiece(pos, id);
            case (7):
                return new OpCardPiece(pos, id);
            case (6):
                return new AbyssPiece(pos, id);
            /**
             * TODO: resolve problem of many different types of conveyor belts
             * See conveyorbeltLayer
             */
            case (49):
                return new ConveyorBeltPiece(pos, id);
            case (13):
                return new ExpressBeltPiece(pos, id);
            /**
             * TODO: resolve problem of having clockwise and counterclockwise cogs
             */
            case (53):
            case (54):
                return new CogPiece(pos, id);
            /**
             * TODO: find id of pusher
             */
            case (-1):
                return new PusherPiece(pos, id);
            /**
             * TODO: resolve problem of having horizontal and vertical lazers
             * Id 53 and 54
             */
            case (47):
                return new LazerPiece(pos, id);
            case (37):
                return new LazerSourcePiece(pos, id);
            case (31):
                return new WallPiece(pos, id, Direction.NORTH);
            case (30):
                return new WallPiece(pos, id, Direction.WEST);
            case (323):
                return new WallPiece(pos, id, Direction.EAST);
            case (29):
                return new WallPiece(pos, id, Direction.SOUTH);
            case (55):
            case (63):
            case (71):
            case (79):
                return new FlagPiece(pos, id);
            //TODO: find id of player
            default:
                return new NullPiece(pos, id);
        }
    }
}
