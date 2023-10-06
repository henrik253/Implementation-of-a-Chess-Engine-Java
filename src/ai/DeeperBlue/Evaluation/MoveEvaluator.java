package ai.DeeperBlue.Evaluation;

import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;

public class MoveEvaluator {
    static final int[] PIECE_VALUES = new int[]{0, 0, 450, 350, 900, 250, 100};
    static float WEIGHT_CAPTURE_BONUS = 1.0f;
    static float WEIGHT_MORE_ATTACKABLE_AFTER_MOVE = 1.0f;
    static int
            KING = 1,
            ROOK = 2,
            BISHOP = 3,
            QUEEN = 4,
            KNIGHT = 5,
            PAWN = 6;

    static final int[][] CAPTURE_BONUSES = new int[][]{

            {    0,    0,    0,    0,    0,    0,    0},//EMPTY
            {    0, 1000,  150,  100,  400,  100,   10},//KING
            {    0, 1000,  200,  150,  500,  200,   50},//ROOK
            {    0, 1000,  300,  150,  600,  200,   50},//BISHOP
            {    0, 1000,  150,  100,  400,  100,   10},//QUEEN
            {    0, 1000,  300,  150,  500,  200,   50},//KNIGHT
            {    0, 1500,  400,  350,  800,  300,  100},//PAWN
    };
    static BitboardMoveValidation validation = new BitboardMoveValidation(new BitMaskArr(),0);
    static PieceValueCalculator pieceValueCalculator = new PieceValueCalculator();
    public static int evaluate(int[][] afterMoveInt, Bitboard afterMoveBit, int[][] beforeMoveInt, Bitboard beforeMoveBit, int[] moveLeadingTo) {

        ArrayList<int[]> validMovesBeforePlayer = validation.getValidMoves(beforeMoveInt, 1);
        ArrayList<int[]> validMovesAfterPlayer = validation.getValidMoves(afterMoveInt, 1);
        ArrayList<int[]> validMovesBeforeEnemy = validation.getValidMoves(beforeMoveInt, -1);
        ArrayList<int[]> validMovesAfterEnemy = validation.getValidMoves(afterMoveInt, -1);
        int result = 0;

        result += (int) (WEIGHT_CAPTURE_BONUS * moreAttackableAfterMovePenalty(
                        validMovesBeforeEnemy,
                        validMovesAfterEnemy,
                        afterMoveInt,
                        beforeMoveInt
                ));

        result += (int) (WEIGHT_MORE_ATTACKABLE_AFTER_MOVE * captureBonus(
                        beforeMoveInt,
                        afterMoveInt,
                        moveLeadingTo
                ));



        return result;
    }

    private static int moreAttackableAfterMovePenalty(ArrayList<int[]> validMovesBeforeEnemy, ArrayList<int[]> validMovesAfterEnemy, int[][] afterMoveInt, int[][] beforeMoveInt) {
        int attackablePieceValuesBeforeMove = 0;
        int attackablePieceValuesAfterMove = 0;
        for(int[] move : validMovesBeforeEnemy){
            if(beforeMoveInt[move[1]/8][move[1]%8] > 1){
                attackablePieceValuesBeforeMove += pieceValueCalculator.getValue(move[1]/8, move[1]%8, beforeMoveInt, 1);
            }
        }
        for(int[] move : validMovesAfterEnemy){
            if(afterMoveInt[move[1]/8][move[1]%8] > 1){
                attackablePieceValuesAfterMove += pieceValueCalculator.getValue(move[1]/8, move[1]%8, afterMoveInt, 1);
            }
        }
        return attackablePieceValuesBeforeMove - attackablePieceValuesAfterMove;
    }

    private static int captureBonus(int[][] beforeMoveInt, int[][] afterMoveInt, int[] moveLeadingTo) {
        return CAPTURE_BONUSES
        [beforeMoveInt[moveLeadingTo[0]/8][moveLeadingTo[0]%8]]
        [Math.abs(beforeMoveInt[moveLeadingTo[1]/8][moveLeadingTo[1]%8])];
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
