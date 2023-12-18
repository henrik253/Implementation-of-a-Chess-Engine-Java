package ai.DeeperBlue;

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
import ai.Util.Util;
import ai.Validation.Bitboards.BitMaskArr;
import java.util.ArrayList;
import java.util.HashMap;

public class DeeperBlueAgent{
    //-----------------------------------------private vars-----------------------------------------------
    private final DeeperBlueTree tree;
    private final int[][] moveMemory;
    private final Workerpool pool;
    private final OpeningBook openingBook;
    private final int iterativeDeepeningMillis;
    private DeeperBlueState mode;
    private final ForcedCheckMateTree forcedCheckMateTree;
    //-----------------------------------------public vars------------------------------------------------
    public final ArrayList<DeeperBlueExtensionNode> leafNodes;
    public final Util translator;
    public final int
        numExtensionSearches;
    public int player;
    public int maxDepthAlphaBeta;
    public final BitMaskArr bitMaskArr;
    public final boolean useExtensions;
    public final Extension[] extensions;
    public final HashMap<Integer, Float> valueBuffer;
    //-----------------------------------------public methods---------------------------------------------
    public DeeperBlueAgent(int player, int numOfExtensionWorkers, int maxDepthAlphaBeta, int maxMillisIterativeDeepening, int maxNumOfExtensionSearches){
        this.numExtensionSearches = maxNumOfExtensionSearches;
        this.iterativeDeepeningMillis = maxMillisIterativeDeepening;
        this.bitMaskArr = new BitMaskArr();
        this.player = player;
        this.translator = new Util();
        this.tree = new DeeperBlueTree(this);
        this.pool = new Workerpool(numOfExtensionWorkers, this);
        this.leafNodes = new ArrayList<>();
        this.useExtensions = true;
        this.extensions = new Extension[]{
                new PossibleCheckMateExtension(),
                new PossiblePawnPromotionExtension(),
                new QuiescenceExtension()
        };
        this.maxDepthAlphaBeta = maxDepthAlphaBeta;
        this.moveMemory = new int[10][2];
        this.openingBook = new OpeningBook();
        this.mode = DeeperBlueState.OPENING_BOOK;
        this.forcedCheckMateTree = new ForcedCheckMateTree();
        this.valueBuffer = new HashMap<>();
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
                    return getMoveWithIterativeDeepening(intBoard, player);
                }else{
                    System.out.println("Opening_book");
                    return returnCorrectedMove(-player, move);
                }
            }
            case NORMAL_SEARCH -> {
                valueBuffer.clear();
                if(foundForcedCheckMate(correctedBoard)){
                    this.mode = DeeperBlueState.FORCED_CHECKMATE;
                    System.out.println("Forced_CheckMate");
                    return getMoveWithForcedCheckMateSearch(intBoard, player);
                }
                System.out.println("Normal_search");
                return getMoveWithIterativeDeepening(intBoard, player);
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
    //-----------------------------------------private methods--------------------------------------------
    private boolean foundForcedCheckMate(int[][] correctedBoard) {
        this.forcedCheckMateTree.search(correctedBoard);
        return this.forcedCheckMateTree.foundForcedCheckMate();
    }

    private int[] getMoveWithForcedCheckMateSearch(int[][] intBoard, int player) {
        int[][] correctedBoard;
        correctedBoard = getCorrectedBoard(intBoard);

        this.forcedCheckMateTree.search(correctedBoard);
        int[] bestMove = this.forcedCheckMateTree.getMove();
        return returnCorrectedMove(player, bestMove);
    }

    private int[][] getCorrectedBoard(int[][] intBoard) {
        int[][] correctedBoard;
        if (this.player == 1) {
            correctedBoard = ai.Util.Util.flipBoardHorizontallyAndFLipPlayer(intBoard);
        } else {
            correctedBoard = intBoard;
        }
        return correctedBoard;
    }
    private int[] getMoveWithIterativeDeepening(int[][] intBoard, int player) throws DeeperBlueException, InterruptedException {
        int[][] correctedBoard;
        correctedBoard = getCorrectedBoard(intBoard);
        long start = System.currentTimeMillis();
        this.maxDepthAlphaBeta = 3;
        while(System.currentTimeMillis() - start < this.iterativeDeepeningMillis){
            this.leafNodes.clear();
            this.tree.search(correctedBoard);
            this.maxDepthAlphaBeta += 2;
        }
        System.out.println("Alpha-Beta-Depth was: " + this.maxDepthAlphaBeta);
        return runExtensionsAndGetBestMove(player);
    }

    private int[] runExtensionsAndGetBestMove(int player) throws InterruptedException {
        if(useExtensions){
            this.pool.run(this.leafNodes);
        }
        int[] bestMove = this.tree.getMoveWithBestValue();
        bestMove = changeMoveIfMoveMemoryHit(bestMove);
        return returnCorrectedMove(player, bestMove);
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


    private boolean moveInMoveMemory(int[]input){
        for (int[] ints : moveMemory) {
            if (input[0] == ints[0] && input[1] == ints[1]) {
                return true;
            }
        }
        return false;
    }


}
