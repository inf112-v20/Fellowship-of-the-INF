package inf112.skeleton.app.GridObjects;

import inf112.skeleton.app.Grid.Direction;
import inf112.skeleton.app.Grid.Position;

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

    /**
     * Creates BoardPiece to put in PieceGrid
     * @param id
     * @return BoardPiece correspinging to the id
     */
    public BoardPiece generate(int id) {
        switch (id) {
            case (5):
                return new FloorPiece(pos, id);
            case (15):
                return new RepairPiece(pos, id);
            case (7):
                return new OpCardPiece(pos, id);
            case (6):
                return new AbyssPiece(pos, id);
            case (49):
                return new ConveyorBeltPiece(pos, id, Direction.NORTH, false, false);
            case (50):
                return new ConveyorBeltPiece(pos, id, Direction.SOUTH, false, false);
            case (51):
                return new ConveyorBeltPiece(pos, id, Direction.WEST, false, false);
            case (52):
                return new ConveyorBeltPiece(pos, id, Direction.EAST, false, false);
            case (33):
                return new ConveyorBeltPiece(pos, id, Direction.WEST, true, false);
            case (34):
                return new ConveyorBeltPiece(pos, id, Direction.NORTH, true, false);
            case (35):
                return new ConveyorBeltPiece(pos, id, Direction.NORTH, true, true);
            case (36):
                return new ConveyorBeltPiece(pos, id, Direction.EAST, true, true);
            case (41):
                return new ConveyorBeltPiece(pos, id, Direction.SOUTH, true, false);
            case (42):
                return new ConveyorBeltPiece(pos, id, Direction.EAST, true, false);
            case (43):
                return new ConveyorBeltPiece(pos, id, Direction.WEST, true, true);
            case (44):
                return new ConveyorBeltPiece(pos, id, Direction.SOUTH, true, true);
            case (13):
                return new ExpressBeltPiece(pos, id, Direction.NORTH, false, false);
            case (14):
                return new ExpressBeltPiece(pos, id, Direction.EAST, false, false);
            case (21):
                return new ExpressBeltPiece(pos, id, Direction.SOUTH, false, false);
            case (22):
                return new ExpressBeltPiece(pos, id, Direction.WEST, false, false);
            case (25):
                return new ExpressBeltPiece(pos, id, Direction.SOUTH, true, false);
            case (26):
                return new ExpressBeltPiece(pos, id, Direction.EAST, true, false);
            case (27):
                return new ExpressBeltPiece(pos, id, Direction.WEST, true, true);
            case (28):
                return new ExpressBeltPiece(pos, id, Direction.SOUTH, true, true);
            case (17):
                return new ExpressBeltPiece(pos, id, Direction.WEST, true, false);
            case (18):
                return new ExpressBeltPiece(pos, id, Direction.NORTH, true, false);
            case (19):
                return new ExpressBeltPiece(pos, id, Direction.NORTH, true, true);
            case (20):
                return new ExpressBeltPiece(pos, id, Direction.EAST, true, true);
            case (53):
                return new CogPiece(pos, id, false);
            case (54):
                return new CogPiece(pos, id, true);
            case (1):
                return new PusherPiece(pos, id, Direction.SOUTH, false);
            case (2):
                return new PusherPiece(pos, id, Direction.WEST, false);
            case (3):
                return new PusherPiece(pos, id, Direction.NORTH, false);
            case (4):
                return new PusherPiece(pos, id, Direction.EAST, false);
            case (9):
                return new PusherPiece(pos, id, Direction.SOUTH, true);
            case (10):
                return new PusherPiece(pos, id, Direction.WEST, true);
            case (11):
                return new PusherPiece(pos, id, Direction.NORTH, true);
            case (12):
                return new PusherPiece(pos, id, Direction.EAST, true);
            case (47):
                return new LazerPiece(pos, id, Direction.NORTH, false, false);
            case (39):
                return new LazerPiece(pos, id, Direction.WEST, false, false);
            case (40):
                return new LazerPiece(pos, id, Direction.WEST, false, true);
            case (103):
                return new LazerPiece(pos, id, Direction.WEST, true, false);
            case (102):
                return new LazerPiece(pos, id, Direction.NORTH, true, false);
            case (101):
                return new LazerPiece(pos, id, Direction.NORTH, true, true);
            case (37):
                return new LazerSourcePiece(pos, id, Direction.NORTH, false);
            case (38):
                return new LazerSourcePiece(pos, id, Direction.EAST, false);
            case (45):
                return new LazerSourcePiece(pos, id, Direction.SOUTH, false);
            case (46):
                return new LazerSourcePiece(pos, id, Direction.WEST, false);
            case (88):
                return new LazerSourcePiece(pos, id, Direction.NORTH, true);
            case (93):
                return new LazerSourcePiece(pos, id, Direction.EAST, true);
            case (94):
                return new LazerSourcePiece(pos, id, Direction.SOUTH, true);
            case (95):
                return new LazerSourcePiece(pos, id, Direction.WEST, true);
            case (31):
                return new WallPiece(pos, id, Direction.NORTH);
            case (30):
                return new WallPiece(pos, id, Direction.WEST);
            case (323):
                return new WallPiece(pos, id, Direction.EAST);
            case (29):
                return new WallPiece(pos, id, Direction.SOUTH);
            case (55):
                return new FlagPiece(pos, id, 1);
            case (63):
                return new FlagPiece(pos, id, 2);
            case (71):
                return new FlagPiece(pos, id, 3);
            case (79):
                return new FlagPiece(pos, id, 4);
            //TODO: find id of player
            default:
                return new NullPiece(pos, id);
        }
    }
}
