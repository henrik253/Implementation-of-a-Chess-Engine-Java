package ai.DeeperBlue.Evaluation;

import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;

public class StaticEvaluator {
    static final int[] PIECE_VALUES = new int[]{0, 0, 450, 350, 900, 250, 100};
    static final float PIECE_ATTACKED_WEIGHT = 0.5f;
    static final int ENDGAME_PIECE_NUM = 5;
    static final int ENDGAME_KINGDST_WEIGHT = 10;
    static final int ENEMY_CHECK_BONUS_VALUE = 400;
    static final float PIECE_IN_DANGER_AFTER_MOVE_WEIGHT = 0.2f;

    static BitboardMoveValidation validation = new BitboardMoveValidation(new BitMaskArr(),0);
    static PieceValueCalculator pieceValueCalculator = new PieceValueCalculator();

    public static int evaluate(int[][] board, Bitboard bitboard){
        int result = 0;
        ArrayList<int[]> validMoves = validation.getValidMoves(board, 1);
        ArrayList<int[]> validMovesEnemy = validation.getValidMoves(board, -1);
        result += countWhitePieces(board);
        result -= countBlackPieces(board);
        result += encourageKingInEndGames(board);
        result += calcAttackedSquares(board, validMoves);
        result -= calcAttackedSquares(flipBoardHorizontallyAndFLipPlayer(board), validMovesEnemy);
        result += attackedKingSquares(board, validMoves);
        result -= attackedKingSquares(flipBoardHorizontallyAndFLipPlayer(board), validMovesEnemy);
        return result;
    }
    private static int attackedKingSquares(int[][] board, ArrayList<int[]> validMoves) {
        int result = 0;

        int kingRow = -1;
        int kingCol = -1;
        //search the enemy king
        boolean loop = true;
        for (int row = 0; row < 8 && loop; row++) {
            for(int col = 0; col < 8 && loop; col++){
                if(board[row][col] == -1){
                    kingRow = row;
                    kingCol = col;
                    loop = false;
                }
            }
        }
        int kingSquare = kingRow * 8 + kingCol;
        for(int[] move : validMoves){
            if(move[1] == kingSquare){//if the piece is checking the king
                result += ENEMY_CHECK_BONUS_VALUE;
            }else if(manhattanDistance(kingRow, kingCol, move[1]/8, move[1]%8) == 1) {//if the piece is blocking the movement of the king
                result += ENEMY_CHECK_BONUS_VALUE/4;
            }


        }
        return result;
    }

    private static int calcAttackedSquares(int[][] board, ArrayList<int[]> validMoves) {
        int result = 0;
        int[][] attackableSquares = new int[8][8];
        for(int[] move : validMoves){
            attackableSquares[move[1]/8][move[1]%8]++;
        }
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col ++){
                result += attackableSquares[row][col] * (PIECE_VALUES[Math.abs(board[row][col])] * PIECE_ATTACKED_WEIGHT);
            }
        }
        return result;
    }

    public static float countPieces(int[][] board) {
        return countWhitePieces(board) - countBlackPieces(board);
    }

    private boolean[][] calcAttackedSquaresArray(int[][] board, int player) {
        boolean[][] result = new boolean[8][8];
        ArrayList<int[]> validMoves = validation.getValidMoves(board, player);
        for(int[] move : validMoves){
            result[move[1]/8][move[1]%8] = true;
        }
        return result;
    }

    private static int encourageKingInEndGames(int[][] board) {
        //countNumberOfPieces
        int numberOfPieces = 0;
        int result = 0;
        for(int[] row : board){
            for(int square : row){
                if(square != 0){
                    numberOfPieces++;
                }
            }
        }
        //calculate Reward for a king getting closer to the opponents king
        //if the number of pieces is less or equal than ENDGAME_PIECE_NUM
        //else return 0
        if(numberOfPieces <= ENDGAME_PIECE_NUM){
            //search for the kings
            int whiteKingRow = -1, whiteKingCol = -1, blackKingRow = -1, blackKingCol = -1;
            boolean whiteKingFound = false;
            boolean blackKingFound = false;
            boolean loop = true;
            for(int row = 0; row < 8 && loop; row++){
                for (int col = 0; col < 8 && loop; col++) {
                    if(board[row][col] == 1){
                        whiteKingRow = row;
                        whiteKingCol = col;
                        whiteKingFound = true;
                    }
                    if(board[row][col] == -1){
                        blackKingRow = row;
                        blackKingCol = col;
                        blackKingFound = true;
                    }
                    if(blackKingFound && whiteKingFound){
                        loop = false;
                    }
                }
            }
            return ENDGAME_KINGDST_WEIGHT * (8 - manhattanDistance(whiteKingRow, whiteKingCol, blackKingRow, blackKingCol));

        }else{
            return 0;
        }
    }

    private static int manhattanDistance(int sRow, int sCol, int dRow, int dCol) {
        return Math.abs(sRow) - Math.abs(dRow) + Math.abs(sCol) - Math.abs(dCol);
    }

    private static int countWhitePieces(int[][] board) {
        int result = 0;
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                if(board[row][col] > 0){
                    result += pieceValueCalculator.getValue(row, col, board, 1);
                }
            }
        }
        return result;
    }
    private static int countBlackPieces(int[][] board) {
        int result = 0;
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                if(board[row][col] < 0){
                    result += pieceValueCalculator.getValue(row, col, board, -1);
                }
            }
        }
        return result;
    }

    private static int[][] flipBoardHorizontallyAndFLipPlayer(int[][] board) {
        int[][] result = new int[8][8];
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                result[7-row][col] = board[row][col] * -1;
            }
        }
        return result;
    }
}
