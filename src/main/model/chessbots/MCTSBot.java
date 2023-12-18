package main.model.chessbots;

import ai.MCTSAgent.MCTSAgent;
import main.model.chessbots.ChessBot;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;

import java.io.IOException;

public class MCTSBot implements ChessBot {
    MCTSAgent agent;
    Move lastMove;
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
        this.lastMove = this.agent.makeMove(board);
        return this.agent.makeMove(board);
    }

    @Override
    public Move getLastMove() {
        return this.lastMove;
    }
}
