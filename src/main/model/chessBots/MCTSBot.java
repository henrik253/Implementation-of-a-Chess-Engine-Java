package main.model.chessBots;

import ai.MCTSAgent.MCTSAgent;
import main.model.ChessBot;
import main.model.Move;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;

import java.io.IOException;

public class MCTSBot implements ChessBot {
    MCTSAgent agent;
    public MCTSBot(){
        this.agent = new MCTSAgent(2, 200, -1);
        this.agent.initRandom();
        try {
            this.agent.addActualValueNet();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.agent.addMathematicalPolicyNet();
    }

    @Override
    public ChessPieceColor getColor() {
        return this.agent.player == 1 ? ChessPieceColor.WHITE : ChessPieceColor.BLACK;
    }

    @Override
    public void setColor(ChessPieceColor color) {
        if(color.isWhite()){
            this.agent.player = 1;
        }else{
            this.agent.player = -1;
        }
    }

    @Override
    public Move makeMove(Piece[][] board) {

        return this.agent.makeMove(board);
    }
}
