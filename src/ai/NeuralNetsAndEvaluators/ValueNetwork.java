package ai.NeuralNetsAndEvaluators;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;

public class ValueNetwork implements IValueNetWork{
    MultiLayerNetwork network;
    public ValueNetwork(String path, boolean forFurtherTraining) throws IOException {
        if(forFurtherTraining){
            setModelForFurtherTraining(path);
        }else{
            setModelForPlaying(path);
        }
    }

    private void setModelForPlaying(String path) throws IOException {
        this.network = ModelSerializer.restoreMultiLayerNetwork(path, false);
        this.network.init();
    }

    private void setModelForFurtherTraining(String path) {
        //todo
    }

    @Override
    public float getValue(int[][] board) {
        INDArray converted = Nd4j.create(2,board.length * board[0].length);
        int index = 0;
        for (int[] ints : board) {
            for (int anInt : ints) {
                converted.putScalar(index, ((float)anInt)/6);
                index++;
            }
        }
        INDArray tempResult = this.network.output(converted, false);
        return tempResult.getFloat(0, 1);
    }
}
