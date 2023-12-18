package ai.DeeperBlue.NormalSearchTree.Nodes;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.Evaluation.BoardEvaluator;
import ai.DeeperBlue.NormalSearchTree.DeeperBlueTree;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class DeeperBlueNode implements Comparable<DeeperBlueNode>{

    public float currentHighestInterestValue;
    public int currentHighestExtensionId;
    public final boolean addLeavesToBuffer;
    public boolean expanded;
    public final Bitboard bitBoard;
    public final int[][] intBoard;
    public final ArrayList<DeeperBlueNode> children;
    public DeeperBlueNode parent;
    public final DeeperBlueTree tree;
    final int currentDepth;
    public boolean isRoot;
    float sortingValue;
    public float value;
    public float alpha;
    public float beta;
    public int[] moveLeadingTo;
    public boolean maxNode = false;
    public DeeperBlueNode(int[][] board, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo, boolean addLeavesToBuffer) {
        this.addLeavesToBuffer = addLeavesToBuffer;
        this.bitBoard = new Bitboard(board, tree.agent.bitMaskArr);
        this.intBoard = board;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.tree = tree;
        this.currentDepth = currentDepth;
        this.isRoot = false;
        this.sortingValue = BoardEvaluator.evaluateSimplePlusBonus(board);
        float boardHash = Arrays.deepHashCode(board);
        if(this.tree.agent.valueBuffer.containsKey(boardHash)){
            this.sortingValue = this.tree.agent.valueBuffer.get(boardHash);
        } else {
            this.sortingValue = BoardEvaluator.evaluateSimplePlusBonus(board);
        }
        this.expanded = false;
        this.moveLeadingTo = moveLeadingTo;
    }

    public DeeperBlueNode(int[][] board, int currentDepth, DeeperBlueTree tree, boolean addLeavesToBuffer) {
        this.addLeavesToBuffer = addLeavesToBuffer;
        this.bitBoard = new Bitboard(board, tree.agent.bitMaskArr);
        this.intBoard = board;
        this.children = new ArrayList<>();
        this.tree = tree;
        this.currentDepth = currentDepth;
        this.isRoot = false;
    }



    public abstract void expand() throws DeeperBlueException;
}
