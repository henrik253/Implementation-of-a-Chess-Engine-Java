package ai.DeeperBlue.NormalSearchTree.Nodes;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.NormalSearchTree.DeeperBlueTree;
import ai.Validation.BitboardValidation.BitboardMove;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeeperBlueMaxNode extends DeeperBlueNode{


    public DeeperBlueMaxNode(int[][] board, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo, boolean addLeavesToBuffer) {
        super(board, currentDepth, parent, tree, moveLeadingTo, addLeavesToBuffer);
        this.isRoot = false;
        this.value = Integer.MIN_VALUE;
        this.alpha = Integer.MIN_VALUE;
        this.maxNode = true;

    }
    public DeeperBlueMaxNode(int[][] board, int currentDepth, DeeperBlueTree tree, boolean addLeavesToBuffer) {
        super(board, currentDepth, tree, addLeavesToBuffer);
        this.isRoot = true;
        this.value = Integer.MIN_VALUE;
        this.alpha = Integer.MIN_VALUE;
        this.maxNode = true;
    }

    @Override
    public void expand() throws DeeperBlueException {

        if(this.tree.agent.maxDepthAlphaBeta - currentDepth >= 0) {
            this.fillChildrenWithMinNodes();
            Collections.sort(this.children);// sorts with the sorting values, not the board values!!!!!!!!!!!
            ArrayList<DeeperBlueNode> childrenToRemove = new ArrayList<>();
            ArrayList<DeeperBlueNode> childrenToAdd = new ArrayList<>();
            for(DeeperBlueNode child : this.children){
                this.tree.agent.nodesSearched++;
                child.beta = this.beta;
                child.expand();
                if(!child.children.isEmpty()){
                    child.expanded = true;
                    this.value = Math.max(this.value, child.value);
                    this.alpha = Math.max(this.alpha, child.value);
                    if(!this.isRoot && this.alpha >= parent.beta){
                        //System.out.println("Alpha pruning!");
                        break;
                    }
                }else{//Human player is checkmated

                    childrenToAdd.add(
                            new DeeperBlueExtensionNode(
                                    flipBoardHorizontallyAndFLipPlayer(child.intBoard) ,  currentDepth + 1, this, this.tree, child.moveLeadingTo, this.addLeavesToBuffer
                            )
                    );
                    childrenToRemove.add(child);
                }

            }
            int boardHash = Arrays.deepHashCode(intBoard);
            this.tree.agent.valueBuffer.put(boardHash, value);
            for(DeeperBlueNode child : childrenToRemove){
                this.children.remove(child);
           }
            this.children.addAll(childrenToAdd);
        }else{
            this.fillChildrenWithExtensionNodes();
            for(DeeperBlueNode child : children){
                this.value = Math.max(this.value, child.value);
                this.alpha = Math.max(this.alpha, child.value);
            }
        }
        if(this.children.isEmpty()){//this bot is checkmated
            this.value = -100000;
        }
    }

    private void fillChildrenWithMinNodes() {

        List<Integer> validMoves = this.tree.agent.translator.getValidMoves(intBoard, -1);
        Bitboard boardAfterMove;
        int[] currentMoveCoordinates;
        for(Integer moveInt : validMoves){
            currentMoveCoordinates = this.tree.agent.translator.intToCoordinates(moveInt);
            int[] currentMove = new int[]{
                    currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                    currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
            };
            BitboardMove move = new BitboardMove(currentMove[0], currentMove[1], Math.abs(this.intBoard[currentMove[0]/8][currentMove[0]%8]));
            boardAfterMove = new Bitboard(this.bitBoard);
            move.applyMoveToFlippedBoard(boardAfterMove);
            int[][] newIntBoard = boardAfterMove.toIntBoard();
            this.children.add(
                    new DeeperBlueMinNode(
                            flipBoardHorizontallyAndFLipPlayer(newIntBoard) ,  currentDepth + 1, this, this.tree, new int[]{
                            currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                            currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
                    }, this.addLeavesToBuffer
                    )
            );
        }

    }

    private void fillChildrenWithExtensionNodes() {

        List<Integer> validMoves = this.tree.agent.translator.getValidMoves(intBoard, -1);
        Bitboard boardAfterMove;
        int[] currentMoveCoordinates;
        for(Integer moveInt : validMoves){
            currentMoveCoordinates = this.tree.agent.translator.intToCoordinates(moveInt);
            int[] currentMove = new int[]{
                    currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                    currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
            };
            BitboardMove move = new BitboardMove(currentMove[0], currentMove[1], Math.abs(this.intBoard[currentMove[0]/8][currentMove[0]%8]));
            boardAfterMove = new Bitboard(this.bitBoard);
            move.applyMove(boardAfterMove);
            int[][] newIntBoard = boardAfterMove.toIntBoard();
            this.children.add(
                    new DeeperBlueExtensionNode(
                            flipBoardHorizontallyAndFLipPlayer(newIntBoard) , currentDepth + 1,this, this.tree,new int[]{
                            currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                            currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
                    }, this.addLeavesToBuffer
                    )
            );
        }

    }

    @Override
    public int compareTo(DeeperBlueNode o) {
        return Float.compare(this.sortingValue, o.sortingValue);
    }


}
