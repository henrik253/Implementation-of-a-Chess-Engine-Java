package ai.MCTSAgent;

import ai.MCTSAgent.Logic.LogicTranslator;
import ai.MCTSAgent.MonteCarloTree.MonteCarloTree;
import ai.MCTSAgent.NeuralNetsAndEvaluators.*;
import ai.MCTSAgent.NeuralNetsAndEvaluators.MPolicyNetwork.MPolicyNetwork;
import ai.MCTSAgent.Validation.Bitboards.BitMaskArr;
import main.model.ChessBot;
import main.model.Move;
import main.model.Vector2D;
import main.model.chessPieces.concretePieces.Piece;

import java.io.IOException;
import java.util.Arrays;

public class MCTSAgent implements ChessBot {
    //logic specific data
    int[] moveMemory;
    private IPolicyNetWork policyNet;
    private IValueNetWork valueNet;
    MonteCarloTree tree;
    public int player;
    public BitMaskArr arr;
    public int[][] currentBoard;
    public MCTSAgent(float c, int simulations, int player) {
        moveMemory = new int[4];// used to stop repetition
        Arrays.fill(moveMemory, -1);
        this.tree = new MonteCarloTree(this, new LogicTranslator(), c, simulations);
        this.player = player;
        this.arr = new BitMaskArr();
    }
    static MCTSAgent getTestBot(int player){
        MCTSAgent testBot = new MCTSAgent(2, 200, player);
        testBot.initRandom();
        testBot.addMathematicalPolicyNet();
        try {
            testBot.addActualValueNet();
        } catch (IOException e) {
            System.out.println("Cant open Value net");
        }
        return testBot;
    }

    public void initRandom() {
        this.policyNet = new RPolicyNetwork();
        this.valueNet = new RValueNetWork();
    }
    public void addActualValueNet() throws IOException {
        this.valueNet = new ValueNetwork("./resources/TrainedValueNetForRunning.zip", false);
    }
    public void addMathematicalPolicyNet(){
        this.policyNet = new MPolicyNetwork(this.arr, this);
    }
    public int[] getNextMove(int[][] board){
        float[] monteCarloValues = tree.search(board);
        int bestMoveIndex = 0;
        float bestMoveValue= monteCarloValues[0];
        for(int i = 0; i < monteCarloValues.length; i++){
            if(monteCarloValues[i] > bestMoveValue){
                bestMoveValue = monteCarloValues[i];
                bestMoveIndex = i;
            }
        }
        return getLogic().intToCoordinates(bestMoveIndex);
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





    private boolean notInMoveMemory(float monteCarloValue) {
        for(int i : moveMemory){
            if(i == monteCarloValue){
                return false;
            }
        }
        return true;
    }
    private int[][] flipBoardHorizontallyAndFLipPlayer(int[][] board) {
        int[][] result = new int[8][8];
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                result[7-row][col] = board[row][col] * -1;
            }
        }
        return result;
    }
    private int[] flipCoordinates(int[] ints) {
        return new int[]{ints[0], 7 - ints[1], ints[2], 7 - ints[3]};
    }

    @Override
    public Move makeMove(Piece[][] board) {
        System.out.println("tick");
        int[][] intBoard = getLogic().translateBoard(board);
        this.currentBoard = intBoard;
        if(player == -1){
            this.currentBoard = flipBoardHorizontallyAndFLipPlayer(intBoard);
        }
        float[] monteCarloValues = tree.search(intBoard);
        int bestMoveIndex = 0;
        float bestMoveValue = monteCarloValues[0];
        for(int i = 0; i < monteCarloValues.length; i++){
            if(monteCarloValues[i] > bestMoveValue && notInMoveMemory(i)){
                bestMoveValue = monteCarloValues[i];
                bestMoveIndex = i;
            }
        }
        int[] coordinates;
        if(player == -1){
            coordinates = flipCoordinates(getLogic().intToCoordinates(bestMoveIndex));
        }else{
            coordinates = getLogic().intToCoordinates(bestMoveIndex);
        }
        return new Move(new Vector2D(coordinates[1], coordinates[0]), new Vector2D(coordinates[3],coordinates[2]));

    }
}
