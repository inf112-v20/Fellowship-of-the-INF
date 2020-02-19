package inf112.skeleton.app.GridObjects;

public class BoardPieceGenerator {

    public BoardPieceGenerator() {

    }


    public BoardPiece generate(int x, int y, int id) {
        Position pos = new Position(x, y);


        switch (id) {
            case (0):
                return null;
            break;
            case (5):
                return new WallPiece(pos, id);
            break;
            case (15):
                return new RepairPiece(pos, id);
            break;
            case (7):
                return new OpCardPiece(pos, id);
            break;
            case (6):
                return new AbyssPiece(pos, id);
            break;
            /**
             * TODO: resolve problem of many different types of conveyor belts
             * See conveyorbeltLayer
             */
            case (49):
                return new ConveyorBeltPiece(pos, id);
            break;
            case (13):
                return new ExpressBeltPiece(pos, id);
            break;
            /**
             * TODO: resolve problem of having clockwise and counterclockwise cogs
             * Id 53 and 54
             */
            case (53):
                return null;
            break;
            /**
             * TODO: resolve problem of having horizontal and vertical lazers
             * Id 53 and 54
             */
            case (47):
                return new LazerPiece();
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;
            case (0):
                return null;
            break;


        }

    }
