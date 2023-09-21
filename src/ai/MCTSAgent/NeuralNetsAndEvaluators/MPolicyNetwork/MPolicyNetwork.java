package ai.MCTSAgent.NeuralNetsAndEvaluators.MPolicyNetwork;


import ai.MCTSAgent.MCTSAgent;
import ai.MCTSAgent.NeuralNetsAndEvaluators.IPolicyNetWork;
import ai.MCTSAgent.Validation.BitboardValidation.BitboardMoveValidation;
import ai.MCTSAgent.Validation.Bitboards.BitMaskArr;
import ai.MCTSAgent.Validation.Bitboards.Bitboard;

import java.util.ArrayList;


public class MPolicyNetwork  implements IPolicyNetWork {
    static final int[] PIECE_VALUES = new int[]{0, 0, 450, 350, 900, 250, 100};
    static final float PIECE_ATTACKED_WEIGHT = 0.5f;
    static final int ENDGAME_PIECE_NUM = 5;
    static final int ENDGAME_KINGDST_WEIGHT = 10;
    BitboardMoveValidation validation;
    MCTSAgent ai;
    PieceValueCalculator pieceValueCalculator;

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
        if(player == -1){
            bitboard.flipPlayer();
        }
        Bitboard afterMove;
        for(int[] move : validMoves){
            afterMove = bitboard.simulateMove(move);
            if(player == -1){
                afterMove.flipPlayer();
            }
            evaluationResult[coordinatesToInt(move[0]%8, move[0]/8, move[1]%8, move[1]/8)] = evaluate(afterMove.toIntBoard(), player);
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
    public int evaluate(int[][] board, int player){
        if(player == -1){
            board = flipBoardHorizontallyAndFLipPlayer(board);
        }
        int result = 0;

        result += countWhitePieces(board);
        result -= countBlackPieces(board);
        result += encourageKingInEndGames(board);
        result += calcAttackedSquares(board, player);
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

    private int manhattanDistance(int whiteKingRow, int whiteKingCol, int blackKingRow, int blackKingCol) {
        return Math.abs(whiteKingRow) - Math.abs(blackKingRow) + Math.abs(whiteKingCol) - Math.abs(blackKingCol);
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

    private int[][] flipBoardHorizontallyAndFLipPlayer(int[][] board) {
        int[][] result = new int[8][8];
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                result[7-row][col] = board[row][col] * -1;
            }
        }
        return result;
    }

}
