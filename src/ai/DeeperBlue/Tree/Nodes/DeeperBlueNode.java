package ai.DeeperBlue.Tree.Nodes;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.Evaluation.BoardEvaluator;
import ai.DeeperBlue.Tree.DeeperBlueTree;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;

public abstract class DeeperBlueNode implements Comparable<DeeperBlueNode>{
    protected boolean expanded;
    Bitboard bitBoard;
    public int[][] intBoard;
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
    public boolean maxNode = false;
    protected static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;
    public DeeperBlueNode(int[][] board, int currentDepth, DeeperBlueNode parent, DeeperBlueTree tree, int[] moveLeadingTo) {
        this.bitBoard = new Bitboard(board, tree.agent.bitMaskArr);
        this.intBoard = board;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.tree = tree;
        this.currentDepth = currentDepth;
        this.isRoot = false;
        //this.sortingValue = this.tree.agent.valueNet.getValue(board, this.bitBoard);
        this.sortingValue = BoardEvaluator.evaluateSimple(board);
        //this.sortingValue = 0.5f;
        this.expanded = false;
        this.moveLeadingTo = moveLeadingTo;
        //System.out.printf(" depth: " + (currentDepth-1) + "\n");
        //this.tree.agent.nodesSearched++;
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

    public void printNode(){
        System.out.println("============================================");
        if(this.maxNode){
            System.out.println("NodeType: MAX");
        }else{
            System.out.println("NodeType: MIN");
        }
        System.out.println("Depth: " + currentDepth);

        System.out.println("Value: " + this.value);
        if(!this.isRoot){
            if(!this.maxNode){
                System.out.println("Move leading to: " + this.moveLeadingTo[0] + " -> " + moveLeadingTo[1]);
            }else{
                int
                        rowStart = 7 - this.moveLeadingTo[0]/8,
                        colStart = this.moveLeadingTo[0]%8,
                        rowDst = 7 - this.moveLeadingTo[1]/8,
                        colDst = this.moveLeadingTo[1]%8;
                System.out.println("Move leading to: " + (rowStart * 8 + colStart) + " -> " + (rowDst * 8 + colDst));
            }
        }
        printBoard();
        System.out.println("============================================");
    }

    private void printBoard() {
        int[][] currentBoard;
        if(this.maxNode){
            currentBoard = this.intBoard;
        }else{
            currentBoard = flipBoardHorizontallyAndFLipPlayer(this.intBoard);
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(currentBoard[row][col] < 0){
                    System.out.print("|"+currentBoard[row][col]);
                }else if(currentBoard[row][col] >= 0){
                    System.out.print("| "+currentBoard[row][col]);
                }

            }
            System.out.print("|\n");
        }
    }

    static int[][] flipBoardHorizontallyAndFLipPlayer(int[][] board) {
        int[][] result = new int[8][8];
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                result[7-row][col] = board[row][col] * -1;
            }
        }
        return result;
    }

    abstract void expand() throws DeeperBlueException;
}
