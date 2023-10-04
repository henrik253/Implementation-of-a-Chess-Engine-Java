package ai.DeeperBlue.Tree.Nodes;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.Evaluation.StaticEvaluator;
import ai.DeeperBlue.Tree.DeeperBlueTree;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;

public abstract class DeeperBlueNode implements Comparable<DeeperBlueNode>{
    protected boolean expanded;
    Bitboard bitBoard;
    int[][] intBoard;
    public ArrayList<DeeperBlueNode> children;
    DeeperBlueNode parent;
    DeeperBlueTree tree;
    int currentDepth;
    boolean isRoot;
    float sortingValue;
    public float value;
    float alpha;
    float beta;
    public int[] moveLeadingTo;
    protected static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;


    public DeeperBlueNode(int[][] board, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo) {
        this.bitBoard = new Bitboard(board, tree.agent.bitMaskArr);
        this.intBoard = board;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.tree = tree;
        this.currentDepth = currentDepth;
        this.isRoot = false;
        this.sortingValue = StaticEvaluator.countPieces(board);
        //this.sortingValue = this.tree.agent.valueNet.getValue(board);
        this.sortingValue = 0.5f;
        this.expanded = false;
        this.moveLeadingTo = moveLeadingTo;
        //System.out.printf(" depth: " + (currentDepth-1) + "\n");
    }

    public DeeperBlueNode(int[][] board, int currentDepth, DeeperBlueTree tree) {
        this.bitBoard = new Bitboard(board, tree.agent.bitMaskArr);
        this.intBoard = board;
        this.children = new ArrayList<>();
        isRoot = true;
        this.tree = tree;
        this.currentDepth = currentDepth;
        this.isRoot = false;
    }

    abstract void expand() throws DeeperBlueException;
}
