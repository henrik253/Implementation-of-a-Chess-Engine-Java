package ai.DeeperBlue.Tree.Nodes;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.Tree.DeeperBlueTree;
import ai.Validation.Bitboards.Bitboard;

import java.util.Collections;
import java.util.List;

public class DeeperBlueMaxNode extends DeeperBlueNode{


    public DeeperBlueMaxNode(int[][] board, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo) {
        super(board, currentDepth, parent, tree, moveLeadingTo);
        this.isRoot = false;
        this.value = Integer.MIN_VALUE;
        this.alpha = Integer.MIN_VALUE;

    }
    public DeeperBlueMaxNode(int[][] board, int currentDepth, DeeperBlueTree tree) {
        super(board, currentDepth, tree);
        this.isRoot = true;
        this.value = Integer.MIN_VALUE;
        this.alpha = Integer.MIN_VALUE;
    }

    @Override
    public void expand() throws DeeperBlueException {

        if(this.tree.agent.maxDepthAlphaBeta - currentDepth >= 0) {
            this.fillChildrenWithMinNodes();
            Collections.sort(this.children);// sorts with the sorting nodes, not the values!!!!!!!!!!!
            for(DeeperBlueNode child : this.children){
                this.tree.agent.nodesSearched++;
                child.beta = this.beta;
                child.expand();
                child.expanded = true;
                this.value = Math.max(this.value, child.value);
                this.alpha = Math.max(this.alpha, child.value);
                if(!this.isRoot){
                    //System.out.println("Depth: " + this.currentDepth  +" alpha: " + this.alpha + ", parent Beta: " + parent.beta );
                }
                if(!this.isRoot && this.alpha >= parent.beta){
                    //System.out.println("Alpha pruning!");
                    break;
                }
            }
        }else{
            throw new DeeperBlueException("Depth is not divisible by 2");
        }

    }

    private void fillChildrenWithMinNodes() {
        List<Integer> validMoves = this.tree.agent.translator.getValidMoves(intBoard, -1);
        Bitboard boardAfterMove;
        int[] currentMoveCoordinates;
        for(Integer moveInt : validMoves){
            currentMoveCoordinates = this.tree.agent.translator.intToCoordinates(moveInt);
            boardAfterMove = this.bitBoard.simulateMove(
                    new int[]{
                            currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                            currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
                    }
            );
            this.children.add(
                    new DeeperBlueMinNode(
                            boardAfterMove.toIntBoard(),  currentDepth + 1, this, this.tree, new int[]{
                            currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                            currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
                    }
                    )
            );
        }
    }

    // TODO
    public int[] getMoveWithBestValue() {
        return new int[10];
    }

    @Override
    public int compareTo(DeeperBlueNode o) {
        return Float.compare(this.sortingValue, o.sortingValue);
    }
}
