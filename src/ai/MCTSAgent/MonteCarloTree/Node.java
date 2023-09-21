package ai.MCTSAgent.MonteCarloTree;

import java.util.ArrayList;
import java.util.List;

import ai.MCTSAgent.Validation.Bitboards.Bitboard;

public class Node {
    MonteCarloTree tree;
    float c;
    int simulations;
    Node parent;
    int moveLeadingTo;
    int[][] board;
    ArrayList<Node> children;
    float valueSum;
    int visitCount;
    float prior;
    int player;
    int depth;
    private Bitboard bitboard;

    public Node(MonteCarloTree tree, int[][] board, float c, int simulations, Node parent, int moveLeadingTo, float prior, int player, int depth){
        this.tree = tree;
        this.c = c;
        this.simulations = simulations;
        this.parent = parent;
        this.moveLeadingTo = moveLeadingTo;
        this.board = board;
        this.children = new ArrayList<>();
        this.valueSum = 0.f;
        this.visitCount = 0;
        this.prior = prior;
        this.player = player;
        this.depth = depth;
        if(this.player == 1){
            this.bitboard = new Bitboard(board, this.tree.ai.arr);
        }else{
            this.bitboard = new Bitboard(flipIntBoard(board), this.tree.ai.arr);
            this.bitboard.flipPlayer();
        }

    }

    private int[][] flipIntBoard(int[][] board) {
        int[][] result = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result[i][j] = board[i][j] * -1;
            }
        }
        return result;
    }

    public Node(MonteCarloTree tree, int[][] board, float c, int simulations, int player){
        this.tree = tree;
        this.c = c;
        this.simulations = simulations;
        this.board = board;
        this.children = new ArrayList<>();
        this.valueSum = 0.f;
        this.visitCount = 0;
        this.moveLeadingTo = 0;
        this.prior = 0.f;
        this.player = player;
        this.depth = 0;
        if(this.player == 1){
            this.bitboard = new Bitboard(board, this.tree.ai.arr);
        }else{
            this.bitboard = new Bitboard(flipIntBoard(board), this.tree.ai.arr);
            this.bitboard.flipPlayer();
        }
    }

    boolean alreadyExpanded(){
        return this.children.size() > 0;
    }

    Node selectBestChild(){
        Node bestChild = this.children.get(0);
        float bestUcb = this.calcUcb(bestChild);
        for(Node child : this.children){
            if(this.calcUcb(child) > bestUcb){
                bestChild = child;
                bestUcb = this.calcUcb(child);
            }
        }
        return bestChild;
    }

    private float calcUcb(Node child) {
        if(child.visitCount == 0){
            return (float) (this.c * (Math.sqrt(this.visitCount) / (child.visitCount+1)) * child.prior);
        }else{
            return (float) ((1 - ((child.valueSum / child.visitCount) + 1) / 2)
                    + this.c * (Math.sqrt(this.visitCount) / (child.visitCount+1)) * child.prior);
        }
    }

    public void expand(float[] policy) {
        List<Integer> validMoves = this.tree.getLogic().getValidMoves(this.board, this.player);

        for(int move : validMoves) {
            int[][] newBoard = calculateNewBoard(this.bitboard, this.player, move);
            Node child = new Node(
                    this.tree,
                    newBoard,
                    this.c,
                    this.simulations,
                    this,
                    move,
                    policy[move],
                    this.player * -1,
                    this.depth + 1
            );
            this.children.add(child);
        }
    }

    private int[][] calculateNewBoard(Bitboard bitboard, int player, int move) {
        int[] coordinates = this.tree.ai.getLogic().intToCoordinates(move);
        int start = coordinates[1] * 8 + coordinates[0];
        int destination = coordinates[3] * 8 + coordinates[2];
        Bitboard afterMove = bitboard.simulateMove(new int[]{start, destination});
        return afterMove.toIntBoard();
    }


    public void backtrackToRoot(float value) {
        this.visitCount++;
        this.valueSum += value;
        if(parent != null){
            parent.backtrackToRoot(value * -1);
        }
    }

}
