package ai.AlphaZeroDotFive.NeuralNets;

import java.util.Random;

public class RPolicyNetwork implements IPolicyNetWork {
    Random rand;
    public RPolicyNetwork(){
        rand = new Random();
    }
    @Override
    public float[] getPolicy(int[][] board) {
        float[] result = new float[board.length * board[0].length * board.length * board[0].length];
        for(int i = 0; i < result.length; i++){
            result[i] = Math.abs(this.rand.nextFloat());

        }
        return result;
    }
}
