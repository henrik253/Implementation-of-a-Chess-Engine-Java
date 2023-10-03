package ai.DeeperBlue.Tree.Nodes;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.Tree.DeeperBlueTree;
import ai.Validation.Bitboards.Bitboard;

import java.util.Collections;
import java.util.List;



public class DeeperBlueMinNode extends DeeperBlueNode{




    public DeeperBlueMinNode(int[][] board, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo) {
        super(board, currentDepth, parent, tree, moveLeadingTo);
        this.bitBoard.flipPlayer();
        this.value = Integer.MAX_VALUE;
        this.beta = Integer.MAX_VALUE;
    }
    @Override
    void expand() throws DeeperBlueException {
        if(this.tree.agent.maxDepthAlphaBeta - currentDepth >= 0) {
            this.fillChildrenWithMaxNodes();
            Collections.sort(this.children);// sorts with the sorting nodes, not the values!!!!!!!!!!!
            for(DeeperBlueNode child : this.children){
                this.tree.agent.nodesSearched++;
                child.alpha = this.alpha;
                child.expand();
                child.expanded = true;
                this.value = Math.min(this.value, child.value);
                this.beta = Math.min(this.beta, child.value);
                //System.out.println("Depth: " + this.currentDepth + " Beta: " + this.beta + ", parent Alpha: " + parent.alpha );
                if(this.beta <= parent.alpha){
                    //System.out.println("Beta pruning!!!\n");
                    break;
                }
            }


        }else{
            this.fillChildrenWithExtensionNodes();
            for(DeeperBlueNode child : children){
                this.value = Math.min(this.value, child.value);
                this.beta = Math.min(this.beta, child.value);
            }
        }
    }

    private void fillChildrenWithExtensionNodes() {
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
            boardAfterMove.flipPlayer();
            this.children.add(
                    new DeeperBlueExtensionNode(
                            boardAfterMove.toIntBoard(), currentDepth + 1,this, this.tree,new int[]{
                                currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                                currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
                            }
                    )
            );
        }
    }

    private void fillChildrenWithMaxNodes() {
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
            boardAfterMove.flipPlayer();
            this.children.add(
                    new DeeperBlueMaxNode(
                        boardAfterMove.toIntBoard(),  currentDepth + 1, this, this.tree,new int[]{
                            currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                            currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
                        }
                    )
            );
        }
    }

    @Override
    public int compareTo(DeeperBlueNode o) {
        return Float.compare(this.sortingValue, o.sortingValue);
    }
}
