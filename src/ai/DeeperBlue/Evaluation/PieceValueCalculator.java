package ai.DeeperBlue.Evaluation;

import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;

import java.util.ArrayList;

public class PieceValueCalculator {//gets unflipped board
    static final float[] PIECE_BONUS_WEIGHT = {0, 1, 0.8f, 1, 1, 1, 2};
    static final int[] PIECE_VALUES = {0, 5000, 450, 350, 900, 250, 100};
    static final float PIECE_ATTACKING_WEIGHT = 0.1f;
    static final int ENDGAME_NUM_OF_PIECES = 5;
    static final int
            KING = 1;
    static final int ROOK = 2;
    static int BISHOP = 3;
    static final int QUEEN = 4;
    static final int KNIGHT = 5;
    static final int PAWN = 6;


    static final float[][][] MIDGAME_BONUSES = new float[][][]{
        //KING
        {
                { 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f},
                { 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f},
                { 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f},
                { 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f},
                { 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f},
                { 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f},
                { 0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0.9f},
                { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        },
        //ROOK
        {
                { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
                { 1.1f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.1f},
                { 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f},
                { 1.0f, 1.0f, 1.0f, 1.1f, 1.1f, 1.0f, 1.0f, 1.0f}
        },
        //BISHOP
        {
                { 0.8f, 0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0.8f},
                { 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.1f, 1.2f, 1.2f, 1.1f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.1f, 1.2f, 1.2f, 1.1f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.2f, 1.2f, 1.2f, 1.2f, 1.0f, 0.8f},
                { 0.8f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 0.8f},
                { 0.8f, 1.1f, 1.0f, 1.0f, 1.0f, 1.0f, 1.1f, 0.8f},
                { 0.8f, 0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0.8f},
        },
        //QUEEN
        {
                { 0.7f, 0.8f, 0.8f, 1.0f, 1.0f, 0.8f, 0.8f, 0.7f},
                { 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.1f, 1.1f, 1.1f, 1.1f, 1.0f, 0.8f},
                { 0.9f, 1.0f, 1.1f, 1.1f, 1.1f, 1.1f, 1.0f, 0.9f},
                { 0.9f, 1.0f, 1.1f, 1.1f, 1.1f, 1.1f, 1.0f, 0.9f},
                { 0.8f, 1.0f, 1.1f, 1.1f, 1.1f, 1.1f, 1.0f, 0.8f},
                { 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f},
                { 0.7f, 0.8f, 0.8f, 1.0f, 1.0f, 0.8f, 0.8f, 0.7f}
        },
        //KNIGHT
        {
                { 0.5f, 0.6f, 0.7f, 0.7f, 0.7f, 0.7f, 0.6f, 0.5f},
                { 0.6f, 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f, 0.6f},
                { 0.7f, 1.0f, 1.1f, 1.2f, 1.2f, 1.1f, 1.0f, 0.7f},
                { 0.7f, 1.1f, 1.2f, 1.3f, 1.3f, 1.2f, 1.1f, 0.7f},
                { 0.7f, 1.0f, 1.2f, 1.3f, 1.3f, 1.2f, 1.0f, 0.7f},
                { 0.7f, 1.1f, 1.1f, 1.2f, 1.2f, 1.1f, 1.1f, 0.7f},
                { 0.6f, 0.8f, 1.0f, 1.1f, 1.1f, 1.0f, 0.8f, 0.6f},
                { 0.5f, 0.6f, 0.7f, 0.7f, 0.7f, 0.7f, 0.6f, 0.5f}
        },
        //PAWN
        {
                { 1.0f,   1.0f,  1.0f,  1.0f,  1.0f,  1.0f,  1.0f,  1.0f},
                { 1.5f,   1.5f,  1.5f,  1.5f,  1.5f,  1.5f,  1.5f,  1.5f},
                { 1.1f,   1.2f,  1.2f,  1.3f,  1.3f,  1.2f,  1.1f,  1.1f},
                {1.05f,  1.05f,  1.1f, 1.25f, 1.25f,  1.1f, 1.05f, 1.05f},
                { 1.0f,   1.0f,  1.0f,  1.2f,  1.2f,  1.0f,  1.0f,  1.0f},
                { 1.05f, 0.95f,  0.9f,  1.0f,  1.0f,  0.9f, 0.95f, 1.05f},
                { 1.05f,  1.1f,  1.1f,  0.8f,  0.8f,  1.1f,  1.1f, 1.05f},
                {  1.0f,  1.0f,  1.0f,  1.0f,  1.0f,  1.0f,  1.0f,  1.0f}
        },

    };
    final BitboardMoveValidation validation;
    public PieceValueCalculator(){
        this.validation = new BitboardMoveValidation(new BitMaskArr(), 0);
    }

    public int getValue(int row, int col, int[][] board, int player){
        int[][] newBoard;
        int newRow;
        if(player == -1){
            newRow = 7-row;
            newBoard =  ai.Util.Util.flipBoardHorizontallyAndFLipPlayer(board);
        }else{
            newBoard = board;
            newRow = row;
        }
        int numOfEnemyPieces = 0;
        int numOfPlayerPieces = 0;

        for(int[] r : newBoard){
            for(int square : r){
                if(square < 0) {
                    numOfEnemyPieces++;
                }
                if(square > 0){
                    numOfPlayerPieces++;
                }
            }
        }
        int pieceType = newBoard[newRow ][col];
        return switch (pieceType) {
            case 1 -> getKingValue(newRow , col);
            case 2 -> getRookValue(newRow , col, numOfPlayerPieces, numOfEnemyPieces, newBoard);
            case 3 -> getBishopValue(newRow , col, numOfEnemyPieces, newBoard);
            case 4 -> getQueenValue(newRow , col);
            case 5 -> getKnightValue(newRow , col,numOfPlayerPieces, numOfEnemyPieces, newBoard);
            case 6 -> getPawnValue(newRow , col);
            default -> 0;
        };
    }

    private int getPawnValue(int row, int col) {
        return (int) (PIECE_VALUES[PAWN] + (8 - row) * PIECE_BONUS_WEIGHT[PAWN]);
    }

    private int getKnightValue(int row, int col, int numOfPLayerPieces, int numOfEnemyPieces, int[][] board) {

        if(numOfEnemyPieces + numOfEnemyPieces <= ENDGAME_NUM_OF_PIECES){// if it is an actual endgame push king
            return knightPushKing();
        }
        // if it isnt an actual endgame but the opponent hasn't way less Pieces push allPiecesEqual
        else if(numOfEnemyPieces * 1.5 < numOfPLayerPieces){
            return knightPushAllPiecesEqual(row, col, board);

        }else{// try to control the middle territory by pushing pieces in the middle more
            return (int) (PIECE_VALUES[KNIGHT] * PIECE_BONUS_WEIGHT[KNIGHT] * MIDGAME_BONUSES[KNIGHT][row][col]);
        }

    }

    private int getBishopValue(int row, int col, int numOfEnemyPieces, int[][] board) {
        ArrayList<int[]> validMoves = this.validation.getValidMovesForPiece(board, 1, row * 8 + col);
        if(numOfEnemyPieces + numOfEnemyPieces <= ENDGAME_NUM_OF_PIECES){// if it is an actual endgame push king
            return bishopPushKing(board, validMoves);
        }else{// try to control the middle territory by pushing pieces in the middle more
            return (int) (PIECE_VALUES[KNIGHT] * PIECE_BONUS_WEIGHT[KNIGHT] * MIDGAME_BONUSES[KNIGHT][row][col]);
        }

    }
    private int bishopPushKing(int[][] board, ArrayList<int[]> validMoves) {
        int result = PIECE_VALUES[ROOK];
        int[] kingPosition = enemyKingPosition(board);
        for(int[] move : validMoves){
            if(move[1] == kingPosition[0] * 8 + kingPosition[1]){//checking enemyKing
                result*= 2.0f;
                break;
            }
            if(restrictingKingsMovement(move[1], kingPosition)){
                result*= 1.5f;
                break;
            }
        }
        return result;
    }



    private int[] enemyKingPosition(int[][] board) {
        for(int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++) {
                if(board[i][j] == -1){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    private int getQueenValue(int row, int col) {
        return (int) (PIECE_VALUES[QUEEN] * MIDGAME_BONUSES[QUEEN][row][col]);
    }



    private int getRookValue(int row, int col, int numOfPlayerPieces, int numOfEnemyPieces, int[][] board) {
        ArrayList<int[]> validMoves = this.validation.getValidMovesForPiece(board, 1, row * 8 + col);
        if(numOfEnemyPieces + numOfEnemyPieces <= ENDGAME_NUM_OF_PIECES){// if it is an actual endgame push king
            return rookPushKing(row, col, board, validMoves);
        }
        // if it isnt an actual endgame but the opponent hasn't way less Pieces push allPiecesEqual
        else if(numOfEnemyPieces * 1.5 < numOfPlayerPieces){
            return rookPushAllPiecesEqual(row, col, board, validMoves);

        }else{// try to control the middle territory by pushing pieces in the middle more
            return (int) (PIECE_VALUES[ROOK] * PIECE_BONUS_WEIGHT[ROOK] * MIDGAME_BONUSES[ROOK][row][col]);
        }
    }

    private int getKingValue(int row, int col) {
        return (int) (PIECE_VALUES[KING] * MIDGAME_BONUSES[QUEEN][row][col] * PIECE_BONUS_WEIGHT[QUEEN]);
    }

    //-----------------------------------Helper Methods---------------------------------------



    private int knightPushKing() {
        return PIECE_VALUES[KNIGHT];
    }
    private int rookPushKing(int row, int col, int[][] board, ArrayList<int[]> validMoves) {
        int result = PIECE_VALUES[ROOK];
        int[] kingPosition = enemyKingPosition(board);
        for(int[] move : validMoves){
            if(move[1] == kingPosition[0] * 8 + kingPosition[1]){//checking enemyKing
                result*= 2.0f;
                break;
            }
            if(restrictingKingsMovement(move[1], kingPosition)){
                result*= 1.5f;
                break;
            }
        }
        return result;
    }




    private int knightPushAllPiecesEqual(int row, int col, int[][] board) {
        int result = PIECE_VALUES[KNIGHT];
        ArrayList<int[]> validMovesForKnight = validation.getValidMovesForPiece(board, 1, row * 8 + col);
        for(int[] move : validMovesForKnight){
            if(board[move[1]/8][move[0]%8] < 0){
                result += (int) (PIECE_ATTACKING_WEIGHT * PIECE_VALUES[Math.abs(board[move[1]/8][move[1]%8])]);
            }
        }
        return result;
    }
    private int rookPushAllPiecesEqual(int row, int col, int[][] board, ArrayList<int[]> validMoves) {
        int result = PIECE_VALUES[ROOK];
        for(int[] move : validMoves){
            if(board[move[1]/8][move[0]%8] < 0){
                result += (int) (PIECE_ATTACKING_WEIGHT * PIECE_VALUES[Math.abs(board[move[1]/8][move[1]%8])]);
            }
        }
        return result;
    }
    private boolean restrictingKingsMovement(int square, int[] kingPosition) {
        int row = square/8;
        int col = square%8;
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if(
                        kingPosition[0] + i >= 0 &&
                                kingPosition[1] + j >= 0 &&
                                kingPosition[0]+ i < 8 &&
                                kingPosition[1] + j < 8 &&
                                kingPosition[0] + i * 8 + kingPosition[1] + j == square)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
