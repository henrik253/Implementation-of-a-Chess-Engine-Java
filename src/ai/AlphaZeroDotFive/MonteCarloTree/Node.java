package ai.AlphaZeroDotFive.MonteCarloTree;

import ai.AlphaZeroDotFive.Logic.LogicTranslator;

import java.util.ArrayList;
import java.util.Arrays;

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
    public Node(MonteCarloTree tree, int[][] board, float c, int simulations, Node parent, int moveLeadingTo, float prior, int player){
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
        boolean[] validMoves = this.tree.getLogic().getValidMoves(this.board, this.player);
        for(int move = 0; move < policy.length; move++){
            if(validMoves[move]){

                int[][]newBoard = this.tree.getLogic().getBoardBuffer()[move];
                Node child = new Node(
                        this.tree,
                        newBoard,
                        this.c,
                        this.simulations,
                        this,
                        move,
                        policy[move],
                        this.player * -1
                );
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
