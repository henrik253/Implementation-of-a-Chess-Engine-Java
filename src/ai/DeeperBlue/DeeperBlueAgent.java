package ai.DeeperBlue;

import ai.DeeperBlue.Evaluation.BoardEvaluator;
import ai.DeeperBlue.Evaluation.DeeperBlueValueNet;
import ai.DeeperBlue.Extensions.Extension;
import ai.DeeperBlue.Extensions.Implementations.PossibleCheckMateExtension;
import ai.DeeperBlue.Extensions.Implementations.PossiblePawnPromotionExtension;
import ai.DeeperBlue.Extensions.Implementations.QuiescenceExtension;
import ai.DeeperBlue.ForcedCheckMateTree.ForcedCheckMateTree;
import ai.DeeperBlue.OpeningBook.OpeningBook;
import ai.DeeperBlue.NormalSearchTree.DeeperBlueTree;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueExtensionNode;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueNode;
import ai.DeeperBlue.Workers.Workerpool;
import ai.Logic.LogicTranslator;
import ai.Validation.Bitboards.BitMaskArr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class DeeperBlueAgent{
    static final int MAX_EXTENSIONS_SINGLE_THREADED = 10;
    public int MAX_EXTENSIONS_MULTI_THREADED = 1000;

    private final int[][] moveMemory;
    public ArrayList<DeeperBlueExtensionNode> leafNodes;
    public LogicTranslator translator;
    public boolean useMoreComplexEvaluation;
    DeeperBlueTree tree;
    Workerpool pool;
    public BitMaskArr bitMaskArr;
    public DeeperBlueValueNet valueNet;
    public int player;
    public int maxDepthAlphaBeta; // must not be a multiple by 2
    public int maxDepthExtension;// must not be a multiple by 2
    public boolean useExtensions;
    public int nodesSearched = 0;
    public Extension[] extensions;
    public int numOfExtensionWorkers;
    boolean multiThreaded;
    private OpeningBook openingBook;
    boolean usingOpeningBook;
    DeeperBlueState mode;
    ForcedCheckMateTree forcedCheckMateTree;
    public DeeperBlueAgent(int player, int numOfExtensionWorkers, int maxDepthAlphaBeta, int maxDepthExtension){
        this.bitMaskArr = new BitMaskArr();
        this.player = player;
        this.translator = new LogicTranslator();
        this.tree = new DeeperBlueTree(this);
        this.pool = new Workerpool(numOfExtensionWorkers, this);
        this.leafNodes = new ArrayList<>();
        this.useExtensions = true;
        this.numOfExtensionWorkers = numOfExtensionWorkers;
        this.multiThreaded = true;
        this.extensions = new Extension[]{
                new PossibleCheckMateExtension(),
                new QuiescenceExtension()
        };
        this.maxDepthAlphaBeta = maxDepthAlphaBeta;
        this.maxDepthExtension = maxDepthExtension;
        this.moveMemory = new int[10][2];
        useMoreComplexEvaluation = false;
        this.openingBook = new OpeningBook();
        usingOpeningBook = true;
        this.mode = DeeperBlueState.OPENING_BOOK;
        this.forcedCheckMateTree = new ForcedCheckMateTree(this);
    }
    public DeeperBlueAgent(int player, int maxDepthAlphaBeta, int maxDepthExtension){
        this.bitMaskArr = new BitMaskArr();
        this.player = player;
        this.translator = new LogicTranslator();
        this.tree = new DeeperBlueTree(this);
        this.leafNodes = new ArrayList<>();
        if(maxDepthExtension == 0){
            this.useExtensions = false;
        }else{
            this.useExtensions = true;
            this.extensions = new Extension[]{
                    new PossibleCheckMateExtension(),
                    new QuiescenceExtension()
            };
        }
        this.multiThreaded = false;
        this.maxDepthAlphaBeta = maxDepthAlphaBeta;
        this.moveMemory = new int[10][2];
        useMoreComplexEvaluation = false;
        this.openingBook = new OpeningBook();
        usingOpeningBook = true;
        this.mode = DeeperBlueState.OPENING_BOOK;
        this.forcedCheckMateTree = new ForcedCheckMateTree(this);
    }

    public int[] makeMove(int[][] intBoard, int player) throws DeeperBlueException, InterruptedException {
        int[][] correctedBoard;
        correctedBoard = getCorrectedBoard(intBoard);
        switch (mode){
            case OPENING_BOOK -> {
                int[] move = this.openingBook.getMove(correctedBoard);
                if(move == null){
                    this.mode = DeeperBlueState.NORMAL_SEARCH;
                    System.out.println("Normal_search");
                    return getMoveWithNormalSearch(intBoard, player);
                }else{
                    System.out.println("Opening_book");
                    return returnCorrectedMove(-player, move);
                }
            }
            case NORMAL_SEARCH -> {
                if(foundForcedCheckMate(correctedBoard)){
                    this.mode = DeeperBlueState.FORCED_CHECKMATE;
                    System.out.println("Forced_CheckMate");
                    return getMoveWithForcedCheckMateSearch(intBoard, player);
                }
                System.out.println("Normal_search");
                return getMoveWithNormalSearch(intBoard, player);
            }
            case FORCED_CHECKMATE -> {
                System.out.println("Forced_CheckMate");
                return getMoveWithForcedCheckMateSearch(intBoard, player);
            }
            default ->{
                return new int[]{-1, -1};
            }
        }
    }

    private boolean foundForcedCheckMate(int[][] correctedBoard) {
        this.forcedCheckMateTree.search(correctedBoard);
        return this.forcedCheckMateTree.foundForcedCheckMate();
    }

    private int[] getMoveWithForcedCheckMateSearch(int[][] intBoard, int player) {
        nodesSearched = 0;
        int[][] correctedBoard;
        correctedBoard = getCorrectedBoard(intBoard);

        this.forcedCheckMateTree.search(correctedBoard);
        int[] bestMove = this.forcedCheckMateTree.getMove();
        return returnCorrectedMove(player, bestMove);
    }

    private int[][] getCorrectedBoard(int[][] intBoard) {
        int[][] correctedBoard;
        if (this.player == 1) {
            correctedBoard = translator.flipBoardHorizontallyAndFLipPlayer(intBoard);
        } else {
            correctedBoard = intBoard;
        }
        return correctedBoard;
    }

    private int[] getMoveWithNormalSearch(int[][] intBoard, int player) throws DeeperBlueException, InterruptedException {
        nodesSearched = 0;
        int[][] correctedBoard;
        correctedBoard = getCorrectedBoard(intBoard);

        this.tree.search(correctedBoard, 0);

        if(useExtensions){
            if(!multiThreaded){
                this.runExtensionsInterestSingleThread();
                this.useExtensionsSingleThread();
            }else{
                this.pool.run(this.leafNodes);
            }
        }
        int[] bestMove = this.tree.getMoveWithBestValue();
        bestMove = changeMoveIfMoveMemoryHit(bestMove);
        return returnCorrectedMove(player, bestMove);
    }

    private void useExtensionsSingleThread() throws DeeperBlueException {
        Collections.sort(this.leafNodes);
        int mostInterestingIndex = 0;
        int mostInterestingValue = -1;
        DeeperBlueExtensionNode currentLeafNode;
        for(int i = 0; i < MAX_EXTENSIONS_SINGLE_THREADED; i++){
            currentLeafNode = this.leafNodes.get(i);
            if(currentLeafNode.interesting){

                //find most interesting value
                for(int j = 0; j < currentLeafNode.extensionInterestValues.length; j++){
                    if(mostInterestingValue < currentLeafNode.extensionInterestValues[j]){
                        mostInterestingValue = currentLeafNode.extensionInterestValues[j];
                        mostInterestingIndex = j;
                    }
                }

                //run the most interesting extension
                this.extensions[mostInterestingIndex].expand(currentLeafNode);
                this.backPropagate(currentLeafNode);
            }

        }
    }

    public void backPropagate(DeeperBlueExtensionNode currentLeafNode) {
        DeeperBlueNode currentNode = currentLeafNode;
        while(!currentNode.isRoot){
            if(currentNode.parent.maxNode && currentNode.value > currentNode.parent.value){
                currentNode.parent.value = currentNode.value;
            }else if(!currentNode.parent.maxNode && currentNode.value < currentNode.parent.value){
                currentNode.parent.value = currentNode.value;
            }
            currentNode = currentNode.parent;
        }
    }

    private int[] changeMoveIfMoveMemoryHit(int[] bestMove) {
        while(this.moveInMoveMemory(bestMove) && (this.tree.root.children.size() > 1)){
            this.tree.removeChild(bestMove);
            bestMove = this.tree.getMoveWithBestValue();
        }
        System.gc();
        //shift move memory
        for(int i = 1; i < moveMemory.length; i++){
            moveMemory[i-1] = moveMemory[i];
        }
        //place new move at the end
        moveMemory[moveMemory.length-1] = bestMove;
        return bestMove;
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
        this.valueNet = new DeeperBlueValueNet("./resources/ForRunning.zip");
    }
    boolean moveInMoveMemory(int[]input){
        for (int[] ints : moveMemory) {
            if (input[0] == ints[0] && input[1] == ints[1]) {
                return true;
            }
        }
        return false;
    }
    public void runExtensionsInterestSingleThread(){
        //fill interest Values
        for(DeeperBlueExtensionNode leafNode : this.leafNodes){
            //run all Extensions interest functions
            for(int extensionIndex = 0; extensionIndex < this.extensions.length; extensionIndex++){
                leafNode.extensionInterestValues[extensionIndex] = this.extensions[extensionIndex].interest(leafNode);
            }
        }
    }

    void printMovesAndValues(){
        for(DeeperBlueNode child : tree.root.children){
            System.out.println("Move: " + child.moveLeadingTo[0] + " -> " + child.moveLeadingTo[1] + "|"
             + BoardEvaluator.evaluateSimple(child.intBoard));
        }
    }
    public void printDepthFirst(int numOfNodes){
        DeeperBlueNode currentNode = this.tree.root;

        int counter = 0;
        while(!currentNode.children.isEmpty()){
            currentNode.printNode();
            for(DeeperBlueNode child : currentNode.children){
                if(child.value == currentNode.value){
                    currentNode = child;
                    break;
                }
            }
        }
    }

    public void printMoveMemory(){
        System.out.println();
        for(int[] move : moveMemory){
            System.out.print(move[0] + " -> " + move[1] + ", ");
        }
        System.out.println();
    }
}
