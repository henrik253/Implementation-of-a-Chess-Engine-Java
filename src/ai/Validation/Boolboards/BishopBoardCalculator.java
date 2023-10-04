package ai.Validation.Boolboards;

public class BishopBoardCalculator extends BoolBoardCalculator{
    @Override
    boolean[][] calculateBoard(int row, int col) {
        boolean[][] result = new boolean[8][8];
        //leftUpperArm
        int
                ccol = col,
                crow = row;
        while(ccol >= 0 && crow >= 0){
            result[ccol][crow] = true;
            ccol--;
            crow--;
        }
        //leftLowerArm
        ccol = col;
        crow = row;
        while(ccol >= 0 && crow < 8){
            result[ccol][crow] = true;
            ccol--;
            crow++;
        }
        //rightLowerArm
        ccol = col;
        crow = row;
        while(ccol < 8 && crow < 8){
            result[ccol][crow] = true;
            ccol++;
            crow++;
        }
        //rightUpperArm
        ccol = col;
        crow = row;
        while(ccol < 8 && crow >= 0){
            result[ccol][crow] = true;
            ccol++;
            crow--;
        }
        result[col][row] = false;
        return result;
    }
}
