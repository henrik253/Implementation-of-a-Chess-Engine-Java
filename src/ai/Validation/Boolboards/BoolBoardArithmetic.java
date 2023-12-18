package ai.Validation.Boolboards;

public class BoolBoardArithmetic {
    static boolean[][] and(boolean[][] a, boolean[][] b){
        boolean[][] result = new boolean[a.length][a[0].length];
        for(int col = 0; col < a.length; col++){
            for(int row = 0; row < a.length; row++){
                result[col][row] = a[col][row] && b[col][row];
            }
        }
        return result;
    }

    public static boolean[][] or(boolean[][] a, boolean[][] b) {
        boolean[][] result = new boolean[a.length][a[0].length];
        for(int col = 0; col < a.length; col++){
            for(int row = 0; row < a.length; row++){
                result[col][row] = a[col][row] || b[col][row];
            }
        }
        return result;
    }
}
