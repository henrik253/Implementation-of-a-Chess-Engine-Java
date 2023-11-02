package ai.NeuralNetsAndEvaluators;

import java.io.IOException;

public class ValueNetwork implements IValueNetWork{
    //MultiLayerNetwork network;
    public ValueNetwork(String path, boolean forFurtherTraining) throws IOException {
        if(forFurtherTraining){
            setModelForFurtherTraining(path);
        }else{
            setModelForPlaying(path);
        }
    }

    private void setModelForPlaying(String path){
    }

    private void setModelForFurtherTraining(String path) {
        //todo
    }

    @Override
    public float getValue(int[][] board) {

        return 0.0f;
    }
}
