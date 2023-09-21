package ai.MCTSAgent.Validation.Boolboards;

public class QueenBoardCalculator extends BoolBoardCalculator{
    RookBoardCalculator rookCalc;
    BishopBoardCalculator bishopCalc;
    public QueenBoardCalculator(){
        this.bishopCalc = new BishopBoardCalculator();
        this.bishopCalc.calcAllBoards();
        this.rookCalc = new RookBoardCalculator();
        this.rookCalc.calcAllBoards();
        this.allBoards2Dim = new boolean[64][8][8];
        this.allBoards1Dim = new boolean[64][64];
    }
    @Override
    boolean[][] calculateBoard(int row, int col) {
        return BoolBoardArithmetic.or(rookCalc.allBoards2Dim[col + row * 8], bishopCalc.allBoards2Dim[col + row * 8]);

    }
}
