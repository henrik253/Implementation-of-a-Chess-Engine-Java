package BitboardValidation.Boolboards;

public class KnightBoardCalculator extends BoolBoardCalculator{
    @Override
    boolean[][] calculateBoard(int row, int col) {
        boolean[][] result = new boolean[8][8];
        int[][] directions = new int[][]{
                {-2, 1},
                {-2, -1},
                {2, 1},
                {2, -1},
                {1, 2},
                {1, -2},
                {-1, 2},
                {-1, -2}

        };
        for(int[] direction : directions){
            if(
                    row + direction[1] <  8 &&
                    row + direction[1] >= 0 &&
                    col + direction[0] <  8 &&
                    col + direction[0] >= 0
            ){
                result[col + direction[0]][row + direction[1]] = true;
            }
        }
        return result;
    }
}
