package ai.AlphaZeroDotFive.MonteCarloTree;

import java.util.ArrayList;
import java.util.List;

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
        List<Integer> validMoves = this.tree.getLogic().getValidMovesAlt(this.board, this.player);
        for(int move : validMoves) {
            int[][] newBoard = this.tree.getLogic().getBoardBuffer()[move];
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



    public void backtrackToRoot(float value) {
        this.visitCount++;
        this.valueSum += value;
        if(parent != null){
            parent.backtrackToRoot(value * -1);
        }
        if(this.tree.lastSimulation){
            int[] move = this.tree.getLogic().intToCoordinates(this.moveLeadingTo);
            System.out.println("Move leading to: " + move[0] + ", " + move[1] + " -> " + move[2] + ", " + move[3]);
            System.out.println();
            this.tree.getLogic().printBoard(this.board);
        }
    }

}
