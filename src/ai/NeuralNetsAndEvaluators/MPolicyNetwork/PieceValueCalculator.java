package ai.NeuralNetsAndEvaluators.MPolicyNetwork;

public class PieceValueCalculator {//gets unflipped board
    static final int[] PIECE_BONUS_WEIGHT = {0, 1, 1, 1, 1, 1, 2};
    static final int[] PIECE_VALUES = {0, 5000, 450, 350, 900, 250, 100};
    static final int
            KING = 1;
    static final int ROOK = 2;
    static final int BISHOP = 3;
    static final int QUEEN = 4;
    static final int KNIGHT = 5;
    static final int PAWN = 6;

    public int getValue(int row, int col, int[][] board, int player){
        int[][] newBoard;
        if(player == -1){
            newBoard =  ai.Util.Util.flipBoardHorizontallyAndFLipPlayer(board);
        }else{
            newBoard = board;
        }
        int pieceType = newBoard[row][col];
        return switch (pieceType) {
            case 1 -> getKingValue(row, col);
            case 2 -> getRookValue(row, col);
            case 3 -> getBishopValue(row, col);
            case 4 -> getQueenValue(row, col);
            case 5 -> getKnightValue(row, col);
            case 6 -> getPawnValue(row, col);
            default -> 0;
        };
    }

    private int getPawnValue(int row, int col) {
        return PIECE_VALUES[PAWN] + (8 - row) * PIECE_BONUS_WEIGHT[PAWN];
    }

    private int getKnightValue(int row, int col) {
        return PIECE_VALUES[KNIGHT];
    }

    private int getQueenValue(int row, int col) {
        return PIECE_VALUES[QUEEN];
    }

    private int getBishopValue(int row, int col) {
        return PIECE_VALUES[BISHOP];
    }

    private int getRookValue(int row, int col) {
        return PIECE_VALUES[ROOK];
    }

    private int getKingValue(int row, int col) {
        return PIECE_VALUES[KING];
    }


}
