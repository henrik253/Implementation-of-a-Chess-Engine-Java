package ai.AlphaZeroDotFive.NeuralNets;

import java.util.Random;

public class RValueNetWork implements IValueNetWork {
    Random rand;
    public RValueNetWork(){
        rand = new Random();
    }
    @Override
    public float getValue(int[][] board) {
        return this.rand.nextFloat();
    }
}
