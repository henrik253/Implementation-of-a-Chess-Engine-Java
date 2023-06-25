package ai.AlphaZeroDotFive.Logic;

public class LogicTranslator {
    int rows;
    int cols;
    public int moveSize;
    public LogicTranslator(){
        this.rows = 8;
        this.cols = 8;
        this.moveSize = 64 * 64;
    }

    public int[][] applyMove(int[][] previous, int move, int player){
        int[] xy = getRowAndColFromMove(move);
        previous[xy[0]][xy[1]] = addPiece(player, move);
        return previous;
    }
    //TODO
    private int[] getRowAndColFromMove(int move) {
        return new int[]{Math.floorDiv(move, this.cols), move%this.cols};
    }
    //TODO
    int addPiece(int player, int move){
        return player;
    }
    //TODO
    public boolean[] getValidMoves(int[][] board){
        return new boolean[board.length * board[0].length];
    }
    //TODO
    public boolean endingMove(int[][] board, int move){
        return false;
    }
}
