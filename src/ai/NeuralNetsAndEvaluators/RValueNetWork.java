package ai.NeuralNetsAndEvaluators;

import java.util.Random;

public class RValueNetWork implements IValueNetWork {
    final Random rand;
    public RValueNetWork(){
        rand = new Random();
    }
    @Override
    public float getValue(int[][] board) {
        return this.rand.nextFloat();
    }
}
