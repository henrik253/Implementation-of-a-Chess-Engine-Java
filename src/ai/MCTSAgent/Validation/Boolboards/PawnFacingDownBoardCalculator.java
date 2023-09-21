package ai.MCTSAgent.Validation.Boolboards;

public class PawnFacingDownBoardCalculator extends BoolBoardCalculator{
    @Override
    boolean[][] calculateBoard(int row, int col) {
        boolean[][] result = new boolean[8][8];
        if(row == 1){
            result[col][row + 2] = true;
        }
        if(row + 1 < 8){
            for( int i = -1; i < 2; i++){
                if(col + i < 8 && col + i >= 0) {
                    result[col + i][row + 1] = true;
                }
            }
        }
        return result;
    }
}
