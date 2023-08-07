package BitboardValidation.RayCasters;

import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;

public abstract class RayCaster {
    public Bitboard bitboard;
    MovementBitBoardGenerator generator;
    public RayCaster(Bitboard bitBoard, MovementBitBoardGenerator generator){
        this.bitboard = bitBoard;
        this.generator = generator;
    }
    public abstract ULong castSomeRays(int row, int col);
    boolean enemyObstacle(int row, int col) {
        return this.bitboard.enemyPieces.combinedPieces.at(row, col);
    }
    boolean playerObstacle(int row, int col) {
        return this.bitboard.playerPieces.combinedPieces.at(row, col);
    }
    boolean validCoordinates(int row, int rowO, int col, int colO) {
        return
                row + rowO < 8 &&
                        col + colO < 8 &&
                        row + rowO >= 0 &&
                        col + colO >= 0;
    }
    ULong andPositions(int row, int col, ULong[] movementBoards) {
        ULong result = new ULong(this.bitboard.playerPieces.combinedPieces.content, this.bitboard.playerPieces.combinedPieces.bitmasks);
        result.not();
        result.and(movementBoards[row * 8 + col]);
        return result;
    }
    ULong shootDiagonalRays(int row, int col) {
        ULong result = new ULong(this.bitboard.playerPieces.combinedPieces.bitmasks);
        //right upper arm
        int colO = 1;
        int rowO = 1;
        while(validCoordinates(row, rowO, col, colO)){
            if(enemyObstacle(row + rowO, col + colO)){
                result.set(row + rowO, col + colO);
                break;
            }
            if(playerObstacle(row + rowO, col + colO)){
                break;
            }
            result.set(row + rowO, col + colO);
            colO++;
            rowO++;
        }
        //left upper arm
        colO = -1;
        rowO = 1;
        while(validCoordinates(row, rowO, col, colO)){
            if(enemyObstacle(row + rowO, col + colO)){
                result.set(row + rowO, col + colO);
                break;
            }
            if(playerObstacle(row + rowO, col + colO)){
                break;
            }
            result.set(row + rowO, col + colO);
            colO--;
            rowO++;
        }
        //left bottom arm
        colO = -1;
        rowO = -1;
        while(validCoordinates(row, rowO, col, colO)){
            if(enemyObstacle(row + rowO, col + colO)){
                result.set(row + rowO, col + colO);
                break;
            }
            if(playerObstacle(row + rowO, col + colO)){
                break;
            }
            result.set(row + rowO, col + colO);
            colO--;
            rowO--;
        }
        //right bottom arm
        colO = 1;
        rowO = -1;
        while(validCoordinates(row, rowO, col, colO)){
            if(enemyObstacle(row + rowO, col + colO)){
                result.set(row + rowO, col + colO);
                break;
            }
            if(playerObstacle(row + rowO, col + colO)){
                break;
            }
            result.set(row + rowO, col + colO);
            colO++;
            rowO--;
        }
        return result;
    }
    ULong shootVerticalRays(int row, int col) {
        ULong result = new ULong(this.bitboard.playerPieces.combinedPieces.bitmasks);
        int rowO = 0;
        int colO = 1;
        //right arm
        while(validCoordinates(row, rowO, col, colO)){
            if(enemyObstacle(row, col + colO)){
                result.set(row, col + colO);
                break;
            }
            if(playerObstacle(row, col + colO)){
                break;
            }
            result.set(row, col + colO);
            colO++;
        }
        colO = -1;

        //left arm
        while(validCoordinates(row, rowO, col, colO)){
            if(enemyObstacle(row, col + colO)){
                result.set(row, col + colO);
                break;
            }
            if(playerObstacle(row, col + colO)){
                break;
            }
            result.set(row, col + colO);
            colO--;
        }
        rowO = 1;
        colO = 0;
        //bottom arm
        while(validCoordinates(row, rowO, col, colO)){
            if(enemyObstacle(row + rowO, col)){
                result.set(row + rowO, col);
                break;
            }
            if(playerObstacle(row + rowO, col)){
                break;
            }
            result.set(row + rowO, col);
            rowO++;
        }
        rowO = -1;
        //bottom arm
        while(validCoordinates(row, rowO, col, colO)){
            if(enemyObstacle(row + rowO, col)){
                result.set(row + rowO, col);
                break;
            }
            if(playerObstacle(row + rowO, col)){
                break;
            }
            result.set(row + rowO, col);
            rowO--;
        }
        return result;
    }
}
