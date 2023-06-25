package ai.AlphaZeroDotFive.MonteCarloTree;

import ai.AlphaZeroDotFive.AlphaZeroDotFiveAgent;
import ai.AlphaZeroDotFive.Logic.LogicTranslator;

public class MonteCarloTree {
    private final LogicTranslator logic;
    private final float c;
    private final int simulations;
    AlphaZeroDotFiveAgent ai;

    public MonteCarloTree(AlphaZeroDotFiveAgent ai, LogicTranslator logic, float c, int simulations){
        this.logic = logic;
        this.c = c;
        this.simulations = simulations;
        this.ai = ai;

    }
    public float[] search(int[][] board){
        Node root = new Node(getLogic(), this, board, this.c, this.simulations);
        for (int i = 0; i < this.simulations; i++) {
            runSimulation(root);
        }
        return childVisitCountsToProbabilities(root);
    }

    private float[] childVisitCountsToProbabilities(Node root) {
        float[] probabilities = new float[this.getLogic().moveSize];
        float sum = 0.f;
        for(Node child : root.children){
            probabilities[child.moveLeadingTo] = child.visitCount;
        }
        for(float prob : probabilities){
            sum += prob;
        }
        for(int i = 0; i < probabilities.length; i++){
            probabilities[i] /= sum;
        }
        return probabilities;
    }

    private void runSimulation(Node root) {
        Node node;
        node = root;
        //select the best leaf-node
        while(node.alreadyExpanded()){
            node = node.selectBestChild();
        }
        float value = -1;
        //if the game hasnt ended, expand node before backtracking
        if(!getLogic().endingMove(node.board, node.moveLeadingTo)){
            //run policy and value network
            float[] policy = this.ai.getPolicyNet().getPolicy(node.board, getLogic().getValidMoves(node.board));
            value = this.ai.getValueNet().getValue(node.board);//for backtracking to root
            //expand the node with new policy
            node.expand(policy);
        }
        node.backtrackToRoot(value);
    }

    public LogicTranslator getLogic() {
        return logic;
    }
}
