package ai.NeuralNetsAndEvaluators;

import java.util.Arrays;

public class ConstantPolicyNet implements IPolicyNetWork{
    @Override
    public float[] getPolicy(int[][] board, int player) {
        float[] result = new float[4096];
        Arrays.fill(result, 0.5f);
        return result;
    }
}
