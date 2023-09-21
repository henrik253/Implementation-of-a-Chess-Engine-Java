package ai.MCTSAgent.Validation.Boolboards;

public class RookBoardCalculator extends BoolBoardCalculator{

    @Override
    boolean[][] calculateBoard(int x, int y) {
        boolean[][] result = new boolean[8][8];
        for(int col = 0; col < 8; col++){
            for(int row = 0; row < 8; row++){
                if(col == x || row == y){
                    result[row][col] = true;
                }
            }
        }
        result[y][x] = false;
        return result;
    }
}
