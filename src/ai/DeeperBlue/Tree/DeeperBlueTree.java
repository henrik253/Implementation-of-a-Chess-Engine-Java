package ai.DeeperBlue.Tree;

import ai.DeeperBlue.DeeperBlueAgent;
import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.Tree.Nodes.DeeperBlueMaxNode;
import ai.DeeperBlue.Tree.Nodes.DeeperBlueNode;

public class DeeperBlueTree {
    public DeeperBlueMaxNode root;
    public DeeperBlueAgent agent;
    public DeeperBlueTree(DeeperBlueAgent agent){
        this.agent = agent;
    }

    public void search(int[][] board, int currentDepth) throws DeeperBlueException {
        this.root = new DeeperBlueMaxNode(board, 0, this, true);
        this.root.expand();
    }

    public int[] getMoveWithBestValue() {
        DeeperBlueNode bestChild = this.root.children.get(0);
        float bestValue = bestChild.value;
        for(DeeperBlueNode child : this.root.children){
            if(child.value > bestValue){
                bestValue = child.value;
                bestChild = child;
            }
        }
        return bestChild.moveLeadingTo;
    }

    public void removeChild(int[] bestMove) {
        DeeperBlueNode childToRemove = root.children.get(0);
        boolean childToRemoveSet = false;
        for(DeeperBlueNode child : this.root.children){
            if(child.moveLeadingTo[0] == bestMove[0] && child.moveLeadingTo[1] == bestMove[1]){
                childToRemove = child;
                childToRemoveSet = true;
                break;
            }
        }
        if(childToRemoveSet){
            root.children.remove(childToRemove);
        }
    }
}
