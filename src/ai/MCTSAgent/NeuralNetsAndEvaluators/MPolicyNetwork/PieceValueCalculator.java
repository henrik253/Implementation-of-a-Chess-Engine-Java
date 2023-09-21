package ai.MCTSAgent.NeuralNetsAndEvaluators.MPolicyNetwork;

public class PieceValueCalculator {//gets unflipped board
    static int[] PIECE_BONUS_WEIGHT = {0, 1, 1, 1, 1, 1, 2};
    static int[] PIECE_VALUES = {0, 5000, 450, 350, 900, 250, 100};
    static int
            KING = 1,
            ROOK = 2,
            BISHOP = 3,
            QUEEN = 4,
            KNIGHT = 5,
            PAWN = 6;

    private int oneDimLerp(int input, int high, int low){//y between 2 and 32
        int increment = (high - low)/30;
        return low + input * increment;
    }
    public int getValue(int row, int col, int[][] board, int player){
        int[][] newBoard;
        if(player == -1){
            newBoard = flipBoardHorizontallyAndFLipPlayer(board);
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
