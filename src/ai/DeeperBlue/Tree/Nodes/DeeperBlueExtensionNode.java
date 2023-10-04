package ai.DeeperBlue.Tree.Nodes;

import ai.DeeperBlue.Evaluation.StaticEvaluator;
import ai.DeeperBlue.Tree.DeeperBlueTree;

public class DeeperBlueExtensionNode extends DeeperBlueNode{
    int extensionValue;
    public DeeperBlueExtensionNode(int[][] intBoard, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo) {
        super(intBoard, currentDepth, parent, tree, moveLeadingTo);
        this.value = StaticEvaluator.evaluate(intBoard, this.bitBoard);
        this.tree.agent.leafNodes.add((this));
    }

    @Override
    void expand() {

    }

    @Override
    public int compareTo(DeeperBlueNode o) {// This one sorts them by they're actual value!!!
        return Float.compare(this.value, o.value);
    }
}
