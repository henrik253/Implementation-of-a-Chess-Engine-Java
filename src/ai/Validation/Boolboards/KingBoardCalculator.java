package ai.Validation.Boolboards;

public class KingBoardCalculator extends BoolBoardCalculator{

    @Override
    boolean[][] calculateBoard(int row, int col) {
        boolean[][] result = new boolean[8][8];
        for(int rowO = -1; rowO < 2; rowO++){
            for(int colO = -1; colO < 2; colO++){
                if(
                        (rowO != 0 || colO != 0) &&
                        row + rowO >= 0 &&
                        row + rowO < 8 &&
                        col + colO >= 0 &&
                        col + colO < 8
                ){
                    result[colO + col][rowO + row] = true;
                }
            }
        }
        return result;
    }

}
