package ai.MCTSAgent.MonteCarloTree;

import ai.Logic.LogicTranslator;
import ai.MCTSAgent.MCTSAgent;

public class MonteCarloTree {
    int simNum;
    private final LogicTranslator logic;
    private final float c;
    private final int simulations;
    MCTSAgent ai;
    boolean lastSimulation;

    public MonteCarloTree(MCTSAgent ai, LogicTranslator logic, float c, int simulations){
        this.simNum = 0;
        this.logic = logic;
        this.c = c;
        this.simulations = simulations;
        this.ai = ai;
        lastSimulation = false;
    }
    public float[] search(int[][] board){
        Node root = new Node(this, board, this.c, this.simulations, 1);
        for (int i = 0; i < this.simulations; i++) {
            runSimulation(root);
        }
        lastSimulation = true;
        runSimulation(root);
        return childVisitCountsToProbabilities(root);
    }
    public float[] searchWithTimeConstraint(int[][] board, int milliSeconds){
        Node root = new Node(this, board, this.c, this.simulations, 1);
        long start = System.currentTimeMillis();
        long end = 0;
        while(end-start < milliSeconds){
            runSimulation(root);
            end = System.currentTimeMillis();
        }
        lastSimulation = true;
        //System.out.println("ValueNet output for Player " + this.ai.player + ": " + root.valueSum);
        return childVisitCountsToProbabilities(root);
    }

    public float[] childVisitCountsToProbabilities(Node root) {
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
        if(!getLogic().endingMove(node.player, node.board)){
            //run policy and value network
            float[] policy = this.ai.getPolicyNet().getPolicy(node.board, node.player);
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
