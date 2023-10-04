package main.model.chessBots;

import ai.DeeperBlue.DeeperBlueAgent;
import ai.DeeperBlue.DeeperBlueException;
import main.model.ChessBot;
import main.model.Move;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;

import java.io.IOException;

public class DeeperBlueBot implements ChessBot {
    DeeperBlueAgent agent;
    public DeeperBlueBot(){
        this.agent = new DeeperBlueAgent(-1, 6);
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
            return new Move(new Vector2D(coordinates[0], coordinates[1]), new Vector2D(coordinates[2],coordinates[3]));
        } catch (DeeperBlueException e) {
            throw new RuntimeException(e);
        }

    }
}
