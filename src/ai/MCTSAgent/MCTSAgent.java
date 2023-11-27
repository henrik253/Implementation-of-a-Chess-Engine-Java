package ai.MCTSAgent;

import ai.Util.Util;
import ai.MCTSAgent.MonteCarloTree.MonteCarloTree;
import ai.NeuralNetsAndEvaluators.*;
import ai.NeuralNetsAndEvaluators.MPolicyNetwork.MPolicyNetwork;
import ai.Validation.Bitboards.BitMaskArr;
import main.model.chessbots.ChessBot;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

import java.io.IOException;
import java.util.Arrays;

public class MCTSAgent implements ChessBot {
    //logic specific data
    final int[] moveMemory;
    private IPolicyNetWork policyNet;
    private IValueNetWork valueNet;
    final MonteCarloTree tree;
    public int player;
    public final BitMaskArr arr;
    public int[][] currentBoard;
    public MCTSAgent(float c, int simulations, int player) {
        moveMemory = new int[4];// used to stop repetition
        Arrays.fill(moveMemory, -1);
        this.tree = new MonteCarloTree(this, new Util(), c, simulations);
        this.player = player;
        this.arr = new BitMaskArr();
    }


    public void initRandom() {
        this.policyNet = new RPolicyNetwork();
        this.valueNet = new RValueNetWork();
    }
    public void addActualValueNet() throws IOException {
        this.valueNet = new ValueNetwork("./resources/ForRunning.zip", false);
    }
    public void addMathematicalPolicyNet(){
        this.policyNet = new MPolicyNetwork(this.arr, this);
    }
    public int[] getNextMove(int[][] board){
        float[] monteCarloValues = tree.searchWithTimeConstraint(board, 4000);
        int bestMoveIndex = 0;
        float bestMoveValue= monteCarloValues[0];
        for(int i = 0; i < monteCarloValues.length; i++){
            if(monteCarloValues[i] > bestMoveValue){
                bestMoveValue = monteCarloValues[i];
                bestMoveIndex = i;
            }
        }
        return Util.intToCoordinates(bestMoveIndex);
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

    private int[] flipCoordinates(int[] ints) {
        return new int[]{ints[0], 7 - ints[1], ints[2], 7 - ints[3]};
    }

    @Override
    public ChessPieceColor getColor() {
        return this.player == 1 ? ChessPieceColor.WHITE : ChessPieceColor.BLACK;
    }

    @Override
    public void setColor(ChessPieceColor color) {
        if(color == ChessPieceColor.WHITE){
            this.player = 1;
        }else{
            this.player = -1;
        }
    }


    @Override
    public Move makeMove(Piece[][] board) {
        System.out.println("tick");
        int[][] intBoard = Util.translateBoard(board);
        this.currentBoard = intBoard;
        if(player == -1){
            this.currentBoard = Util.flipBoardHorizontallyAndFLipPlayer(intBoard);
        }
        float[] monteCarloValues = tree.searchWithTimeConstraint(currentBoard, 5000);
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
            coordinates = flipCoordinates(Util.intToCoordinates(bestMoveIndex));
        }else{
            coordinates = Util.intToCoordinates(bestMoveIndex);
        }
        System.out.println(coordinates[0] +" "+ coordinates[1] + " " + coordinates[2] +" "+ coordinates[3]);
        return new Move(new Vector2D(coordinates[0], coordinates[1]), new Vector2D(coordinates[2],coordinates[3]));

    }

    @Override
    public Move getLastMove() {
        return null;
    }
}
