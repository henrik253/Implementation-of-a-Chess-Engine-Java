package ai.DeeperBlue;


import ai.DeeperBlue.Tree.DeeperBlueTree;
import ai.DeeperBlue.Tree.Nodes.DeeperBlueExtensionNode;
import ai.DeeperBlue.Workers.Workerpool;
import ai.Logic.LogicTranslator;
import ai.NeuralNetsAndEvaluators.IValueNetWork;
import ai.NeuralNetsAndEvaluators.ValueNetwork;
import ai.Validation.Bitboards.BitMaskArr;

import java.io.IOException;
import java.util.ArrayList;

public class DeeperBlueAgent{
    private final int[][] moveMemory;
    public ArrayList<DeeperBlueExtensionNode> leafNodes;
    public LogicTranslator translator;
    DeeperBlueTree tree;
    Workerpool pool;
    public BitMaskArr bitMaskArr;
    public IValueNetWork valueNet;
    public int player;
    public int maxDepthAlphaBeta; // must be a multiple by 2
    public int maxDepthExtension;// must be a multiple by 2
    boolean useExtensions;
    public int nodesSearched = 0;
    public DeeperBlueAgent(int player, int numOfExtensionWorkers, int maxDepthAlphaBeta, int maxDepthExtension){
        this.bitMaskArr = new BitMaskArr();
        this.player = player;
        this.translator = new LogicTranslator();
        this.tree = new DeeperBlueTree(this);
        this.pool = new Workerpool(numOfExtensionWorkers);
        this.leafNodes = new ArrayList<>();
        this.useExtensions = true;
        this.maxDepthAlphaBeta = maxDepthAlphaBeta;
        this.maxDepthExtension = maxDepthExtension;
        this.moveMemory = new int[10][2];
    }
    public DeeperBlueAgent(int player, int maxDepthAlphaBeta){
        this.bitMaskArr = new BitMaskArr();
        this.player = player;
        this.translator = new LogicTranslator();
        this.tree = new DeeperBlueTree(this);
        this.leafNodes = new ArrayList<>();
        this.useExtensions = false;
        this.maxDepthAlphaBeta = maxDepthAlphaBeta;
        this.moveMemory = new int[10][2];
    }

    public int[] makeMove(int[][] intBoard) throws DeeperBlueException {
        nodesSearched = 0;
        int[][] correctedBoard;
        if(this.player == 1){
            correctedBoard = translator.flipBoardHorizontallyAndFLipPlayer(intBoard);
        }else{
            correctedBoard = intBoard;
        }

        this.tree.search(correctedBoard, 0);
        int[] bestMove = this.tree.getMoveWithBestValue();
        while(this.moveInMoveMemoryTwice(bestMove)){
            this.tree.removeChild(bestMove);
            bestMove = this.tree.getMoveWithBestValue();
        }

        //shift move memory
        for(int i = moveMemory.length-2; i >= 0; i--){
            moveMemory[i] = moveMemory[i + 1];
        }
        //place new move at the end
        moveMemory[moveMemory.length-1] = bestMove;
        return returnCorrectedMove(player, bestMove);


    }

    private static int[] returnCorrectedMove(int player, int[] bestMove) {
        int startRow = bestMove[0] / 8;
        int startCol = bestMove[0] % 8;
        int destinationRow = bestMove[1] / 8;
        int destinationCol = bestMove[1] % 8;
        if(player == 1){
            return new int[]{(7 - startRow) * 8 + startCol, (7 - destinationRow) * 8 + destinationCol};

        }else{
            return new int[]{startRow * 8 + startCol, destinationRow * 8 + destinationCol};
        }
    }

    public void addNeuralNetForSorting() throws IOException {
        this.valueNet = new ValueNetwork("./resources/ForRunning.zip", false);
    }
    boolean moveInMoveMemoryTwice(int[]input){
        int occurences = 0;
        for (int[] ints : moveMemory) {
            if (input[0] == ints[0] && input[1] == ints[1]) {
                occurences++;
            }
        }
        return occurences > 1;
    }
}
