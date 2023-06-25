package ai.AlphaZeroDotFive.NeuralNets;

import java.util.Random;

public class RPolicyNetwork implements IPolicyNetWork {
    Random rand;
    public RPolicyNetwork(){
        rand = new Random();
    }
    @Override
    public float[] getPolicy(int[][] board, boolean[] validMoves) {
        float[] result = new float[board.length * board[0].length];
        for(int i = 0; i < result.length; i++){
            if(validMoves[i]){
                result[i] = this.rand.nextFloat();
            }else{
                result[i] = 0.f;
            }
        }
        return result;
    }
}
