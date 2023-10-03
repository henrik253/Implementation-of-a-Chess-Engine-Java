package ai.NeuralNetsAndEvaluators;

public interface IPolicyNetWork {

    float[] getPolicy(int[][] board, int player);
}
