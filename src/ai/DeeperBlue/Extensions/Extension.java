package ai.DeeperBlue.Extensions;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueExtensionNode;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueMaxNode;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueNode;
import ai.Util.Util;
import ai.Validation.BitboardValidation.BitboardMove;
import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Extension {
    public static final int POSSIBLE_CHECKMATE = 1;
    public static final int POSSIBLE_PAWN_PROMOTION = 2;
    public static final int QUIESCENCE_SEARCH = 3;
    public static final BitboardMoveValidation validation = new BitboardMoveValidation(new BitMaskArr(), 0);
    protected int id = -1;
    public abstract void expand(DeeperBlueExtensionNode e) throws DeeperBlueException;
    public abstract int interest(DeeperBlueExtensionNode e);
    protected void fillChildrenWithMaxNodes(DeeperBlueNode toExpand) {

        List<Integer> validMoves = Util.getValidMoves(toExpand.intBoard, -1);
        Bitboard boardAfterMove;
        int[] currentMoveCoordinates;
        for(Integer moveInt : validMoves){
            currentMoveCoordinates = Util.intToCoordinates(moveInt);
            int[] currentMove = new int[]{
                    currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                    currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
            };
            BitboardMove move = new BitboardMove(currentMove[0], currentMove[1], Math.abs(toExpand.intBoard[currentMove[0]/8][currentMove[0]%8]));
            boardAfterMove = new Bitboard(toExpand.bitBoard);
            move.applyMove(boardAfterMove);
            int[][] newIntBoard = boardAfterMove.toIntBoard();
            toExpand.children.add(
                    new DeeperBlueMaxNode(
                            Util.flipBoardHorizontallyAndFLipPlayer(newIntBoard),  0, toExpand, toExpand.tree,new int[]{
                            currentMoveCoordinates[1] * 8 + currentMoveCoordinates[0],
                            currentMoveCoordinates[3] * 8 + currentMoveCoordinates[2]
                    }, false
                    )
            );
        }
    }
    protected void expandChildren(DeeperBlueExtensionNode leafNode) throws DeeperBlueException {
        this.fillChildrenWithMaxNodes(leafNode);
        Collections.sort(leafNode.children);// sorts with the sorting nodes, not the values!!!!!!!!!!!
        for(DeeperBlueNode child : leafNode.children){
            child.alpha = leafNode.alpha;
            child.expand();
            child.expanded = true;
            leafNode.value = Math.min(leafNode.value, child.value);
            leafNode.beta = Math.min(leafNode.beta, child.value);
            if(leafNode.beta <= leafNode.parent.alpha){
                break;
            }
        }
    }
    protected ArrayList<int[][]> getChildBoards(int[][] intBoard, ArrayList<int[]> validMoves) {
        ArrayList<int[][]> result = new ArrayList<>();
        Bitboard before = new Bitboard(intBoard, validation.arr);
        for(int[] move : validMoves){
            result.add(before.simulateMove(move).toIntBoard());
        }
        return result;
    }
}
