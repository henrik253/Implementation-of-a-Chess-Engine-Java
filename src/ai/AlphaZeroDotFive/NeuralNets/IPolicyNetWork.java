package ai.AlphaZeroDotFive.NeuralNets;

public interface IPolicyNetWork {

    float[] getPolicy(int[][] board, boolean[] validMoves);
}
