package ai.AlphaZeroDotFive;

import ai.AlphaZeroDotFive.Logic.LogicTranslator;
import ai.AlphaZeroDotFive.MonteCarloTree.MonteCarloTree;
import ai.AlphaZeroDotFive.NeuralNets.IPolicyNetWork;
import ai.AlphaZeroDotFive.NeuralNets.IValueNetWork;
import ai.AlphaZeroDotFive.NeuralNets.RPolicyNetwork;
import ai.AlphaZeroDotFive.NeuralNets.RValueNetWork;

public class AlphaZeroDotFiveAgent {
    //logic specific data

    private IPolicyNetWork policyNet;
    private IValueNetWork valueNet;
    MonteCarloTree tree;
    public AlphaZeroDotFiveAgent(float c, int simulations) {

        this.tree = new MonteCarloTree(this, new LogicTranslator(), c, simulations);
    }

    public void initRandom() {
        this.policyNet = new RPolicyNetwork();
        this.valueNet = new RValueNetWork();
    }
    public int getNextMove(int[][] board){
        int[][] monteBoard = new int[board.length][board[0].length];
        for(int i = 0; i < monteBoard.length; i++){
            for(int j = 0; j < monteBoard[0].length; j++){
                monteBoard[i][j] = -1 * board[i][j];
            }
        }
        float[] monteCarloValues = tree.search(board);
        int bestMoveIndex = 0;
        float bestMoveValue= monteCarloValues[0];
        for(int i = 0; i < monteCarloValues.length; i++){
            if(monteCarloValues[i] > bestMoveValue){
                bestMoveValue = monteCarloValues[i];
                bestMoveIndex = i;
            }
        }
        return bestMoveIndex;
    }
    public LogicTranslator getLogic(){
        return this.tree.getLogic();
    }

    public IPolicyNetWork getPolicyNet() {
        return policyNet;
    }

    public IValueNetWork getValueNet() {
        return valueNet;
    }
}
