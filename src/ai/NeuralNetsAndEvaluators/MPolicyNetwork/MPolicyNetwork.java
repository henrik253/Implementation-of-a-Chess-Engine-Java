package ai.NeuralNetsAndEvaluators.MPolicyNetwork;


import ai.DeeperBlue.Evaluation.PieceValueCalculator;
import ai.MCTSAgent.MCTSAgent;
import ai.NeuralNetsAndEvaluators.IPolicyNetWork;
import ai.Util.Util;
import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;


public class MPolicyNetwork  implements IPolicyNetWork {
    static final int[] PIECE_VALUES = new int[]{0, 0, 450, 350, 900, 250, 100};
    static final float PIECE_ATTACKED_WEIGHT = 0.5f;
    static final int ENDGAME_PIECE_NUM = 5;
    static final int ENDGAME_KINGDST_WEIGHT = 10;
    static final int ENEMY_CHECK_BONUS_VALUE = 400;
    static final float PIECE_IN_DANGER_AFTER_MOVE_WEIGHT = 0.2f;
    final BitboardMoveValidation validation;
    final MCTSAgent ai;
    final PieceValueCalculator pieceValueCalculator;

    public MPolicyNetwork(BitMaskArr arr, MCTSAgent ai){
        this.validation = new BitboardMoveValidation(ai.arr, 1);
        this.ai = ai;
        pieceValueCalculator = new PieceValueCalculator();
    }


    public int coordinatesToInt(int sx, int sy, int dx, int dy){
        return sx * (8*8*8)+ sy * (8*8) + dx * 8 + dy;
    }


    @Override
    public float[] getPolicy(int[][] board, int player) {
        int[] evaluationResult = new int[4096];
        float[] policy = new float[4096];
        ArrayList<int[]> validMoves = this.validation.getValidMoves(board,player);
        Bitboard bitboard = new Bitboard(board, this.ai.arr);
        boolean[][] attackableSquaresPlayer = calcAttackedSquaresArray(board, player);
        boolean[][] attackableSquaresEnemy = calcAttackedSquaresArray(board, player *= -1);
        if(player == -1){
            bitboard.flipPlayer();
        }
        Bitboard afterMove;
        for(int[] move : validMoves){
            afterMove = bitboard.simulateMove(move);
            if(player == -1){
                afterMove.flipPlayer();
            }
            evaluationResult[coordinatesToInt(move[0]%8, move[0]/8, move[1]%8, move[1]/8)] =
                    //evaluateDynamic(move, bitboard.toIntBoard(), afterMove.toIntBoard(), attackableSquaresPlayer, attackableSquaresEnemy, player) +
                            evaluateStatic(afterMove.toIntBoard(), player);
        }
        int sum = 0;
        for(int i : evaluationResult){
            sum += i;
        }
        for(int i = 0; i < evaluationResult.length; i++){
            policy[i] = (float) evaluationResult[i] /sum;
        }
        return policy;
    }



    //TODO-------------------------------------------------------------------------------------------------------
    private int evaluateDynamic(int[] move, int[][] beforeMove, int[][] afterMove, boolean[][] attackableSquaresPlayerBefore, boolean[][] attackableSquaresEnemyBefore, int player) {
        int[][] boardBefore = beforeMove;
        int[][] boardAfter = afterMove;
        if(player == -1){
            boardBefore = Util.flipBoardHorizontallyAndFLipPlayer(beforeMove);
            boardAfter = Util.flipBoardHorizontallyAndFLipPlayer(afterMove);
        }
        int result = 0;
        boolean[][] attackableSquaresPlayerAfter = calcAttackedSquaresArray(boardAfter, 1);
        int startRow = move[0]/8;
        int startCol = move[0]%8;
        int dstRow = move[1]/8;
        int dstCol = move[1]%8;
        int pieceType = beforeMove[startRow][startCol];

        return 0;
    }




    public int evaluateStatic(int[][] board, int player){
        if(player == -1){
            board = Util.flipBoardHorizontallyAndFLipPlayer(board);
        }
        int result = 0;

        result += countWhitePieces(board);
        result -= countBlackPieces(board);
        result += encourageKingInEndGames(board);
        result += calcAttackedSquares(board, player);
        result -= calcAttackedSquares(Util.flipBoardHorizontallyAndFLipPlayer(board), player);
        result += attackedKingSquares(board, player);
        result -= attackedKingSquares(Util.flipBoardHorizontallyAndFLipPlayer(board), player);
        return result;
    }

    private int attackedKingSquares(int[][] board, int player) {
        int result = 0;
        ArrayList<int[]> validMoves = validation.getValidMoves(board, player);
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

    private int calcAttackedSquares(int[][] board, int player) {
        int result = 0;
        ArrayList<int[]> validMoves = validation.getValidMoves(board, player);
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
    private boolean[][] calcAttackedSquaresArray(int[][] board, int player) {
        boolean[][] result = new boolean[8][8];
        ArrayList<int[]> validMoves = validation.getValidMoves(board, player);
        for(int[] move : validMoves){
            result[move[1]/8][move[1]%8] = true;
        }
        return result;
    }

    private int encourageKingInEndGames(int[][] board) {
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

    private int manhattanDistance(int sRow, int sCol, int dRow, int dCol) {
        return Math.abs(sRow) - Math.abs(dRow) + Math.abs(sCol) - Math.abs(dCol);
    }

    private int countWhitePieces(int[][] board) {
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
    private int countBlackPieces(int[][] board) {
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
    private int pieceInMoreOrLessDangerAfterMove(int startRow, int startCol, int dstRow, int dstCol, boolean[][] attackableSquaresPlayerBefore, boolean[][] attackableSquaresPlayerAfter, int pieceType) {
        if(!attackableSquaresPlayerBefore[startRow][startCol] && attackableSquaresPlayerAfter[dstRow][dstCol]){//if its only attackable after the move
            return (int)((PIECE_VALUES[pieceType] * PIECE_IN_DANGER_AFTER_MOVE_WEIGHT) * -1);
        }else if(attackableSquaresPlayerBefore[startRow][startCol] && !attackableSquaresPlayerAfter[dstRow][dstCol]){//if it isnt attackable after the move but it was before
            return (int)(PIECE_VALUES[pieceType] * PIECE_IN_DANGER_AFTER_MOVE_WEIGHT);
        }else{
            return 0;
        }
    }
}
