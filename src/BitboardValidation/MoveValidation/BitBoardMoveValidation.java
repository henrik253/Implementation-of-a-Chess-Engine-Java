package BitboardValidation.MoveValidation;

import BitboardValidation.BitMaskCorrection.*;
import BitboardValidation.BitMaskCorrection.BitMaskCorrection;
import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;
import BitboardValidation.RayCasters.*;

import java.util.ArrayList;

public class BitBoardMoveValidation {

    public Bitboard board;
    public Bitboard flippedBoard;
    MovementBitBoardGenerator generator;
    public RayCaster[] rayCasters;
    public BitMaskCorrection[] corrections;



    public BitBoardMoveValidation(int[][] board){
        this.generator = new MovementBitBoardGenerator();
        this.generator.calculateMovementBoards();
        this.board = new Bitboard(board, this.generator.bitMaskArr);
        this.flippedBoard = new Bitboard(this.board);
        this.flippedBoard.flipPlayer();
        this.rayCasters = new RayCaster[]{
                new KingRayCaster(this.board, this.generator),
                new RookRayCaster(this.board, this.generator),
                new BishopRayCaster(this.board, this.generator),
                new QueenRayCaster(this.board, this.generator),
                new KnightRayCaster(this.board, this.generator),
                new PawnFacingUpRayCaster(this.board, this.generator),//needs to switch when player switches
        };
        this.corrections = new BitMaskCorrection[]{
                new KingCorrection(this.board, this.generator),
                new RookCorrection(this.board, this.generator),
                new BishopCorrection(this.board, this.generator),
                new QueenCorrection(this.board, this.generator),
                new KnightCorrection(this.board, this.generator),
                new PawnUpCorrection(this.board, this.generator),//needs to switch when player switches
        };
    }

    public int coordinatesToInt(int sx, int sy, int dx, int dy){
        return sx * (8*8*8)+ sy * (8*8) + dx * 8 + dy;
    }

    public ArrayList<Integer> getValidMoves(){
        ArrayList<Integer> result = new ArrayList<>();
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                int pieceType = playerPieceAt(row, col);
                if(pieceType != -1){
                    appendValidMoves(row, col, result, pieceType);
                }
            }
        }
        return result;
    }

    public boolean isValidMove(int row, int col, int rowD, int colD){
        int pieceType = playerPieceAt(row, col);
        if(pieceType != -1){
            ULong validDestinations = this.corrections[pieceType].correct(this.rayCasters[pieceType].castSomeRays(row, col), row, col);
            if(validDestinations.at(rowD, colD)){
                prepareRayCastersAndCorrections();
                if(!inCheckAfterMove(row, col, rowD, colD, pieceType)){
                    restoreRayCastersAndCorrections();
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private void appendValidMoves(int row, int col, ArrayList<Integer> result, int pieceType) {
        ULong resultMask = this.corrections[pieceType].correct(
                this.rayCasters[pieceType].castSomeRays(row, col),
                row,
                col
        );
        prepareRayCastersAndCorrections();
        for(int rowD = 0; rowD < 8; rowD++){
            for (int colD = 0; colD < 8; colD++) {
                if(resultMask.at(rowD, colD)){
                    if(!inCheckAfterMove(row, col, rowD, colD, pieceType)){
                        result.add(coordinatesToInt(col, row, colD, rowD));
                    }
                }
            }
        }
        restoreRayCastersAndCorrections();
    }

    private void prepareRayCastersAndCorrections() {
        if(this.board.player == 1){
            this.rayCasters[5] = new PawnFacingDownRayCaster(this.board, this.generator);
            this.corrections[5] = new PawnDownCorrection(this.board, this.generator);
        }else{
            this.rayCasters[5] = new PawnFacingUpRayCaster(this.board, this.generator);
            this.corrections[5] = new PawnUpCorrection(this.board, this.generator);
        }
    }

    private void restoreRayCastersAndCorrections() {
        if(this.board.player == 1){
            this.rayCasters[5] = new PawnFacingUpRayCaster(this.board, this.generator);
            this.corrections[5] = new PawnUpCorrection(this.board, this.generator);

        }else{
            this.rayCasters[5] = new PawnFacingDownRayCaster(this.board, this.generator);
            this.corrections[5] = new PawnDownCorrection(this.board, this.generator);
        }
        for(int i = 0; i < this.rayCasters.length; i++){
            this.rayCasters[i].bitboard = this.board;
            this.corrections[i].board = this.board;
        }
    }



    private boolean inCheckAfterMove(int row, int col, int rowD, int colD, int pieceType) {
        Bitboard afterMove = new Bitboard(this.flippedBoard);
        //reset player piece
        afterMove.enemyPieces.pieces[pieceType].unset(row, col);
        afterMove.enemyPieces.combinedPieces.unset(row, col);
        afterMove.enemyPieces.pieces[pieceType].set(rowD, colD);
        afterMove.enemyPieces.combinedPieces.set(rowD, colD);
        if(afterMove.playerPieces.combinedPieces.at(rowD, colD)){
            afterMove.playerPieces.pieces[pieceType].unset(row, col);
            afterMove.playerPieces.combinedPieces.unset(row, col);
        }
        ULong attackableSquares = new ULong(afterMove.enemyPieces.combinedPieces.bitmasks);
        for(int i = 0; i < this.rayCasters.length; i++){
            this.rayCasters[i].bitboard = afterMove;
            this.corrections[i].board = afterMove;
            attackableSquares.or(this.corrections[i].correct(this.rayCasters[i].castSomeRays(row, col), row, col));
        }
        int[] kingPosition = this.board.getKingPosition();
        return attackableSquares.at(kingPosition[0], kingPosition[1]);
    }

    public void switchPlayer(){
        if(this.board.player == 1){
            this.rayCasters[5] = new PawnFacingDownRayCaster(this.board, this.generator);
            this.corrections[5] = new PawnDownCorrection(this.board, this.generator);
            this.board.flipPlayer();
        }else{
            this.rayCasters[5] = new PawnFacingUpRayCaster(this.board, this.generator);
            this.corrections[5] = new PawnUpCorrection(this.board, this.generator);
            this.board.flipPlayer();
        }
    }


    private int playerPieceAt(int row, int col) {
        for(int i = 0; i < this.board.playerPieces.pieces.length; i++){
            if(this.board.playerPieces.pieces[i].at(row, col)){
                return i;
            }
        }
        return -1;
    }
}
