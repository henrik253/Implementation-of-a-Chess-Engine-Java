package ai.AlphaZeroDotFive.MonteCarloTree;

import ai.AlphaZeroDotFive.Logic.LogicTranslator;

import java.util.ArrayList;

public class Node {
    LogicTranslator logic;
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
    public Node(LogicTranslator logic, MonteCarloTree tree, int[][] board, float c, int simulations, Node parent, int moveLeadingTo, float prior){
        this.logic = logic;
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
    }
    public Node(LogicTranslator logic, MonteCarloTree tree, int[][] board, float c, int simulations){
        this.logic = logic;
        this.tree = tree;
        this.c = c;
        this.simulations = simulations;
        this.board = board;
        this.children = new ArrayList<>();
        this.valueSum = 0.f;
        this.visitCount = 0;
        this.moveLeadingTo = -1000;
        this.prior = 0.f;
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
        for(int move = 0; move < policy.length; move++){
            if(policy[move] > 0){
                int[][]newBoard = new int[this.board.length][this.board[0].length];
                for(int i = 0; i < this.board.length; i++){
                    newBoard[i] = this.board[i].clone();
                }
                newBoard = logic.applyMove(newBoard, move, 1);
                for (int i = 0; i < newBoard.length; i++) {
                    for(int j = 0; j < newBoard[0].length; j++){
                        newBoard[i][j] *= -1;
                    }
                }
                Node child = new Node(this.logic, this.tree, newBoard, this.c, this.simulations, this, move, policy[move]);
                this.children.add(child);
            }
        }
    }



    public void backtrackToRoot(float value) {
        this.visitCount++;
        this.valueSum += value;
        if(parent != null){
            parent.backtrackToRoot(value * -1);
        }
    }
}
