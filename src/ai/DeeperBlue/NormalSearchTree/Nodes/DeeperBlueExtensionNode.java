package ai.DeeperBlue.NormalSearchTree.Nodes;

import ai.DeeperBlue.Evaluation.BoardEvaluator;
import ai.DeeperBlue.NormalSearchTree.DeeperBlueTree;
import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;

import java.util.ArrayList;

public class DeeperBlueExtensionNode extends DeeperBlueNode{
    public final boolean checkMated;

    public boolean interesting;
    static final float WEIGHT_BOARD_VALUE = 1.0f;
    static final float WEIGHT_MOVE_VALUE = 8.0f;
    static final BitboardMoveValidation validation = new BitboardMoveValidation(new BitMaskArr(),0);
    public final ArrayList<int[]> maybeValidMovesPlayer;
    public final ArrayList<int[]> maybeValidMovesEnemy;
    public int[] extensionInterestValues;

    public DeeperBlueExtensionNode(int[][] intBoard, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo, boolean addLeavesToBuffer) {
        super(intBoard, currentDepth, parent, tree, moveLeadingTo, addLeavesToBuffer);
        this.maybeValidMovesPlayer = validation.getValidMoves(intBoard, 1);
        this.maybeValidMovesEnemy = validation.getValidMoves(intBoard, -1);
        this.checkMated = this.maybeValidMovesPlayer.isEmpty();

        currentHighestInterestValue = 0;
        this.value = 0;
        if(this.tree.agent.useMoreComplexEvaluation){
            this.value += WEIGHT_BOARD_VALUE * BoardEvaluator.evaluate(intBoard, bitBoard);
        }else{
            this.value += WEIGHT_BOARD_VALUE * BoardEvaluator.evaluateSimple(intBoard);
        }

        if(this.checkMated){
            this.value = 10000;
        }
        if(this.tree.agent.useExtensions){
            this.extensionInterestValues = new int[this.tree.agent.extensions.length];
        }
        interesting = false;



        this.tree.agent.leafNodes.add((this));

    }

    @Override
    public void expand() {

    }

    @Override
    public int compareTo(DeeperBlueNode o) {// This one sorts them by the maximum interest
        return Float.compare(this.currentHighestInterestValue, o.currentHighestInterestValue);
    }
}
