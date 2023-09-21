package ai.MCTSAgent.NeuralNetsAndEvaluators;

public interface IPolicyNetWork {

    float[] getPolicy(int[][] board, int player);
}
