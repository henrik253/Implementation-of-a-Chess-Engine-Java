package BitboardValidation.Boolboards;

public class KingBoardCalculator extends BoolBoardCalculator{

    @Override
    boolean[][] calculateBoard(int row, int col) {
        boolean[][] result = new boolean[8][8];
        for(int rowO = -1; rowO < 2; rowO++){
            for(int colO = -1; colO < 2; colO++){
                if(
                        row + rowO >= 0 &&
                        row + rowO < 8 &&
                        col + colO >= 0 &&
                        col + colO < 8
                ){
                    result[colO + col][rowO + row] = true;
                }
            }
        }
        //allow castling
        if(row == 0 && col == 4){
            result[0][6] = true;
            result[0][1] = true;
        }else if(row == 7 && col == 4){
            result[7][6] = true;
            result[7][1] = true;
        }
        result[col][row] = false;
        return result;
    }

}
