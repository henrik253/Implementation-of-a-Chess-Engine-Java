package main.model.Chessbots;

import ai.MCTSAgent.MCTSAgent;
import main.model.ChessBot;
import main.model.Move;
import main.model.chessPieces.concretePieces.Piece;

public class MCTSBot implements ChessBot {
    MCTSAgent agent;
    public MCTSBot(){
        this.agent = new MCTSAgent(2, 200, 1);
        this.agent.initRandom();
        this.agent.addMathematicalPolicyNet();
    }
    @Override
    public Move makeMove(Piece[][] board) {
        return this.agent.makeMove(board);
    }
}
