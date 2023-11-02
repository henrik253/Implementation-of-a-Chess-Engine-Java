package ai.NeuralNetsAndEvaluators;

import java.security.SecureRandom;

public class RPolicyNetwork implements IPolicyNetWork {
    final SecureRandom rand;
    public RPolicyNetwork(){
        rand = new SecureRandom();
    }
    @Override
    public float[] getPolicy(int[][] board, int player) {
        float[] result = new float[board.length * board[0].length * board.length * board[0].length];
        for(int i = 0; i < result.length; i++){
            result[i] = Math.abs(this.rand.nextFloat());

        }
        return result;
    }
}
