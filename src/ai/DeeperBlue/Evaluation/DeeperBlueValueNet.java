package ai.DeeperBlue.Evaluation;

import ai.Validation.Bitboards.Bitboard;
/*import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
*/
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;

public class DeeperBlueValueNet {
    public HashMap<Long, Float> positions;
    //MultiLayerNetwork network;
    int maxBufferSize = 10000;
    SecureRandom rand;
    public DeeperBlueValueNet(String path) throws IOException {
        this.positions = new HashMap<>();
        //this.network = ModelSerializer.restoreMultiLayerNetwork(path, false);
        //this.network.init();
        this.rand = new SecureRandom();
    }
    public float getValue(int[][] board, Bitboard bitboard) {
        long bitboardHash = bitboard.getSimpleHash();
        if(this.positions.containsKey(bitboardHash)){
            return this.positions.get(bitboardHash);
        }else{
            //INDArray converted = Nd4j.create(2,board.length * board[0].length);
            int index = 0;
            for (int[] ints : board) {
                for (int anInt : ints) {
                    //converted.putScalar(index, ((float)anInt)/6);
                    index++;
                }
            }
            //float result = this.network.output(converted, false).getFloat(0, 1);
            if(this.positions.size() >= maxBufferSize){
                //this.positions.put(bitboardHash, result);
                this.removeFirstElement();
            }else{
                //this.positions.put(bitboardHash, result);
            }


            //return result;
            return 0;
        }

    }

    private void removeFirstElement() {
        this.positions.remove(this.positions.entrySet().iterator().next().getKey());
    }
}
