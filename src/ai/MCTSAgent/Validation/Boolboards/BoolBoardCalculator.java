package ai.MCTSAgent.Validation.Boolboards;

public abstract class BoolBoardCalculator {
    public boolean[][][] allBoards2Dim;
    public boolean[][] allBoards1Dim;

    BoolBoardCalculator(){
        this.allBoards2Dim = new boolean[64][8][8];
        this.allBoards1Dim = new boolean[64][64];
    }

    abstract boolean[][] calculateBoard(int row, int col);
    public void calcAllBoards(){
        int index = 0;
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                allBoards2Dim[index] = calculateBoard(row, col);
                allBoards1Dim[index] = toOneDim(allBoards2Dim[index]);
                index++;
            }
        }
    }

    boolean[] toOneDim(boolean[][] input){
        boolean[] result = new boolean[64];
        int index = 0;
        for(int col = 0; col < 8; col ++){
            for(int row = 0; row < 8; row++){
                result[index] = input[row][col];
                index++;
            }
        }
        return result;
    }
}
