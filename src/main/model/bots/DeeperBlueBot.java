package main.model.bots;

import ai.DeeperBlue.DeeperBlueAgent;
import ai.DeeperBlue.DeeperBlueException;

import main.model.bots.ChessBot;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;
import utils.Move;
import utils.Vector2D;

import java.io.IOException;

public class DeeperBlueBot implements ChessBot {
    DeeperBlueAgent agent;
    Move lastMove;
    public DeeperBlueBot(){
        lastMove = new Move(new Vector2D(0, 0), new Vector2D(0,0));
        this.agent = new DeeperBlueAgent(-1, 9);
        try {
            agent.addNeuralNetForSorting();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        try {
            int[] moveSquares = this.agent.makeMove(this.agent.translator.translateBoard(board));
            int[] coordinates = new int[]{moveSquares[0]%8, moveSquares[0]/8, moveSquares[1]%8, moveSquares[1]/8};
            lastMove = new Move(new Vector2D(coordinates[0], coordinates[1]), new Vector2D(coordinates[2],coordinates[3]));
            return lastMove;
        } catch (DeeperBlueException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Move getLastMove() {

        return this.lastMove;

    }
}
