package ai.DeeperBlue.Tree.Nodes;

import ai.DeeperBlue.Evaluation.BoardEvaluator;
import ai.DeeperBlue.Tree.DeeperBlueTree;

public class DeeperBlueExtensionNode extends DeeperBlueNode{

    static final float WEIGHT_BOARD_VALUE = 1.0f;
    static final float WEIGHT_MOVE_VALUE = 8.0f;
    public DeeperBlueExtensionNode(int[][] intBoard, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo) {
        super(intBoard, currentDepth, parent, tree, moveLeadingTo);
        this.value = 0;
        if(this.tree.agent.player == 1){
            this.value += WEIGHT_BOARD_VALUE * BoardEvaluator.evaluateSimple(intBoard);
        }else{
            this.value += WEIGHT_BOARD_VALUE * BoardEvaluator.evaluateSimple(intBoard);
        }

        //System.out.println("BoardValue" + WEIGHT_BOARD_VALUE * BoardEvaluator.evaluate(intBoard, this.bitBoard));
        //this.value += WEIGHT_MOVE_VALUE * MoveEvaluator.evaluate(this.intBoard, this.bitBoard, parent.intBoard, parent.bitBoard, parent.moveLeadingTo);
        //System.out.println("MoveValue" + WEIGHT_MOVE_VALUE * MoveEvaluator.evaluate(this.intBoard, this.bitBoard, parent.intBoard, parent.bitBoard, parent.moveLeadingTo));
        this.tree.agent.leafNodes.add((this));
        //this.maxNode = true;
    }

    @Override
    void expand() {

    }

    @Override
    public int compareTo(DeeperBlueNode o) {// This one sorts them by they're actual value!!!
        return Float.compare(this.value, o.value);
    }
}
