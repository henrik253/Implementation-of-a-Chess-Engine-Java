package ai.DeeperBlue.Evaluation;

import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;

public class BoardEvaluator {
    static final float[] PIECE_VALUES = new float[]{0.f, 250.f, 22.5f, 17.5f, 45.f, 12.5f, 5.f};
    static float[][][] PIECE_VALUE_BONUSES = new float[][][]{
            {//King
                    {-4.0f, -3.5f, -3.5f, -3.5f, -3.5f, -3.5f, -3.5f, -4.0f},
                    {-3.0f, -3.0f, -3.0f, -3.0f, -3.0f, -3.0f, -3.0f, -3.0f},
                    {-2.0f, -2.5f, -2.5f, -3.0f, -3.0f, -2.5f, -2.5f, -2.0f},
                    {-1.5f, -2.0f, -2.0f, -2.5f, -2.5f, -2.0f, -2.0f, -1.5f},
                    {-2.0f, -1.5f, -1.5f, -2.0f, -2.0f, -1.5f, -1.5f, -20.f},
                    {-0.5f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -0.5f},
                    {2.0f,  2.0f,  -0.25f,  -0.25f,  -0.25f,  -0.25f,  2.0f,  20.f},
                    {2.0f,  1.5f,  0.5f,   0.f,   0.f,  0.5f,  1.5f,  20.f}
            },
            {//Rook
                    {0.f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f},
                    {0.25f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f,  0.25f},
                    {-0.25f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f, -0.25f},
                    {-0.25f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f, -0.25f},
                    {-0.25f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f, -0.25f},
                    {-0.25f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f, -0.25f},
                    {-0.25f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f, -0.25f},
                    {0.f,  0.f,  0.f,  0.25f,  0.25f,  0.f,  0.f,  0.f}
            },
            {//Bishop
                    {-2.0f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-20.f},
                    {-0.5f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f,-0.5f},
                    {-0.5f,  0.f,  0.25f, 0.5f, 0.5f,  0.25f,  0.f,-0.5f},
                    {-0.5f,  0.25f,  0.25f, 0.5f, 0.5f,  0.25f,  0.25f,-0.5f},
                    {-0.5f,  0.f, 0.5f, 0.5f, 0.5f, 0.5f,  0.f,-0.5f},
                    {-0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f,-0.5f},
                    {-0.5f,  0.25f,  0.f,  0.f,  0.f,  0.f,  0.25f,-0.5f},
                    {-2.0f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,-20.f}
            },
            {//Queen
                    {-2.0f,-0.5f,-0.5f, -0.25f, -0.25f,-0.5f,-0.5f,-20.f},
                    {-0.5f,  0.f,  0.f,  0.f,  0.f,  0.f,  0.f,-0.5f},
                    {-0.5f,  0.f,  0.25f,  0.25f,  0.25f,  0.25f,  0.f,-0.5f},
                    {-0.25f,  0.f,  0.25f,  0.25f,  0.25f,  0.25f,  0.f, -0.25f},
                    {0.f,  0.f,  0.25f,  0.25f,  0.25f,  0.25f,  0.f, -0.25f},
                    {-0.5f,  0.25f,  0.25f,  0.25f,  0.25f,  0.25f,  0.f,-0.5f},
                    {-0.5f,  0.f,  0.25f,  0.f,  0.f,  0.f,  0.f,-0.5f},
                    {-2.0f,-0.5f,-0.5f, -0.25f, -0.25f,-0.5f,-0.5f,-20.f}
            },
            {//Knight
                    {-2.5f,-2.0f,-1.5f,-1.5f,-1.5f,-1.5f,-2.0f,-2.5f},
                    {-2.0f,-2.0f,  0.f,  0.f,  0.f,  0.f,-2.0f,-2.0f},
                    {-1.5f,  0.f, 0.5f, 0.75f, 0.75f, 0.5f,  0.f,-1.5f},
                    {-1.5f,  0.25f, 0.75f, 2.0f, 2.0f, 0.75f,  0.25f,-1.5f},
                    {-1.5f,  0.f, 0.75f, 2.0f, 2.0f, 0.75f,  0.f,-1.5f},
                    {-1.5f,  0.25f, 0.5f, 0.75f, 0.75f, 0.5f,  0.25f,-1.5f},
                    {-2.0f,-2.0f,  0.f,  0.25f,  0.25f,  0.f,-2.0f,-2.0f},
                    {-2.5f,-2.0f,-1.5f,-1.5f,-1.5f,-1.5f,-2.0f,-2.5f}
            },
            {//Pawn
                    {0.f,   0.f,   0.f,   0.f,   0.f,   0.f,   0.f,   0.f},
                    {2.5f,  2.5f,  2.5f,  2.5f,  2.5f,  2.5f,  2.5f,  2.5f},
                    {0.5f,  0.5f,  2.0f,  1.5f,  1.5f,  2.0f,  0.5f,  0.5f},
                    {0.25f,   0.25f,  0.5f,  20.25f,  20.25f,  0.5f,   0.25f,   0.25f},
                    { 0.f,   0.f,   0.f,  2.0f,  2.0f,   0.f,   0.f,   0.f},
                    {0.25f,  -0.25f, -0.5f,   0.f,   0.f, -0.5f,  -0.25f,   0.25f},
                    {0.25f,  0.5f,  0.5f, -2.0f, -2.0f,  0.5f,  0.5f,   0.25f},
                    {0.f,   0.f,   0.f,   0.f,   0.f,   0.f,   0.f,   0.f}
            }

    };





    public static float evaluateSimple(int[][] board) {
        int result = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(board[row][col] > 0){
                    result += PIECE_VALUES[board[row][col]];
                }else if(board[row][col] < 0){
                    result -= PIECE_VALUES[Math.abs(board[row][col])];
                }
            }
        }
        return result;
    }
    public static float evaluateSimplePlusBonus(int[][] board){
        float result = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(board[row][col] > 0){
                    result += PIECE_VALUES[board[row][col]] + PIECE_VALUE_BONUSES[board[row][col] - 1][row][col];
                }
            }
        }
        int[][] flippedBoard = flipBoardHorizontallyAndFLipPlayer(board);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(flippedBoard[row][col] > 0){
                    result -= PIECE_VALUES[flippedBoard[row][col]] + PIECE_VALUE_BONUSES[flippedBoard[row][col] - 1][row][col];
                }
            }
        }

        return result;
    }



    private static int manhattanDistance(int sRow, int sCol, int dRow, int dCol) {
        return Math.abs(sRow) - Math.abs(dRow) + Math.abs(sCol) - Math.abs(dCol);
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
