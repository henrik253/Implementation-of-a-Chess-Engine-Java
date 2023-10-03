package ai.DeeperBlue.Evaluation;

import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;

import java.util.ArrayList;

public class PieceValueCalculator {//gets unflipped board
    static float[] PIECE_BONUS_WEIGHT = {0, 1, 0.8f, 1, 1, 1, 2};
    static int[] PIECE_VALUES = {0, 5000, 450, 350, 900, 250, 100};
    static float PIECE_GUARDING_WEIGHT = 0.1f;
    static float PIECE_ATTACKING_WEIGHT = 0.1f;
    static int ENDGAME_NUM_OF_PIECES = 5;
    static int
            KING = 1,
            ROOK = 2,
            BISHOP = 3,
            QUEEN = 4,
            KNIGHT = 5,
            PAWN = 6;


    static float[][][] MIDGAME_BONUSES = new float[][][]{
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
    BitboardMoveValidation validation;
    public PieceValueCalculator(){
        this.validation = new BitboardMoveValidation(new BitMaskArr(), 0);
    }
    private int oneDimLerp(int input, int high, int low){//y between 2 and 32
        int increment = (high - low)/30;
        return low + input * increment;
    }
    public int getValue(int row, int col, int[][] board, int player){
        int[][] newBoard;
        int newRow;
        if(player == -1){
            newRow = 7-row;
            newBoard = flipBoardHorizontallyAndFLipPlayer(board);
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
            case 2 -> getRookValue(newRow , col);
            case 3 -> getBishopValue(newRow , col);
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
            return knightPushKing(row, col, board);
        }
        // if it isnt an actual endgame but the opponent hasn't way less Pieces push allPiecesEqual
        else if(numOfEnemyPieces * 1.5 < numOfPLayerPieces){
            knightPushAllPiecesEqual(row, col, board);

        }else{// try to control the middle territory by pushing pieces in the middle more
            return (int) (PIECE_VALUES[KNIGHT] * PIECE_BONUS_WEIGHT[KNIGHT] * MIDGAME_BONUSES[KNIGHT][row][col]);
        }


        return PIECE_VALUES[KNIGHT];
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

    private int getBishopValue(int row, int col) {
        return (int) (PIECE_VALUES[BISHOP] * MIDGAME_BONUSES[QUEEN][row][col]);
    }

    private int getRookValue(int row, int col) {
        return (int) (PIECE_VALUES[ROOK] * MIDGAME_BONUSES[QUEEN][row][col] * PIECE_BONUS_WEIGHT[ROOK]);
    }

    private int getKingValue(int row, int col) {
        return (int) (PIECE_VALUES[KING] * MIDGAME_BONUSES[QUEEN][row][col] * PIECE_BONUS_WEIGHT[QUEEN]);
    }

    //-----------------------------------Helper Methods---------------------------------------

    private int[][] flipBoardHorizontallyAndFLipPlayer(int[][] board) {
        int[][] result = new int[8][8];
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                result[7-row][col] = board[row][col] * -1;
            }
        }
        return result;
    }
    private int manhattanDistance(int sRow, int sCol, int dRow, int dCol) {
        return Math.abs(sRow) - Math.abs(dRow) + Math.abs(sCol) - Math.abs(dCol);
    }
    private int knightPushKing(int row, int col, int[][] board) {
        float[] distanceToKingEndGameBonus = new float[]{1.0f, 1.0f, 1.0f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f};
        int result = PIECE_VALUES[KNIGHT];
        result *= distanceToKingEndGameBonus[manhattanDistance(row, col, enemyKingPosition(board)[0], enemyKingPosition(board)[1])];
        return result;
    }
    private void knightControlMiddle(int row, int col, int[][] board) {
        float[] rowWeightsStartAndMiddle = new float[]{0.8f, 1.1f, 1.1f, 1.2f, 1.2f, 1.1f, 1.1f, 0.8f};
        int result = PIECE_VALUES[KNIGHT];
        ArrayList<int[]> validMovesForKnight = validation.getValidMovesForPiece(board, 1, row * 8 + col);
        for(int[] move : validMovesForKnight){
            if(board[move[1]/8][move[0]%8] < 0){
                result += (int) ((PIECE_ATTACKING_WEIGHT * PIECE_VALUES[Math.abs(board[move[1]/8][move[1]%8])]) * rowWeightsStartAndMiddle[Math.abs(board[move[1]/8][move[1]%8])]);
            }
        }
        result *= (int) rowWeightsStartAndMiddle[row];
    }
    private void knightPushAllPiecesEqual(int row, int col, int[][] board) {
        int result = PIECE_VALUES[KNIGHT];
        ArrayList<int[]> validMovesForKnight = validation.getValidMovesForPiece(board, 1, row * 8 + col);
        for(int[] move : validMovesForKnight){
            if(board[move[1]/8][move[0]%8] < 0){
                result += (PIECE_ATTACKING_WEIGHT * PIECE_VALUES[Math.abs(board[move[1]/8][move[1]%8])]) *1.2f;
            }
        }
    }
}
