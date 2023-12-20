package main.model.chessbots;

import ai.DeeperBlue.DeeperBlueAgent;
import ai.DeeperBlue.DeeperBlueException;
import main.model.gameLogic.BoardRepresentation;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class DeeperBlueBot implements ChessBot {
    DeeperBlueAgent agent;
    Move lastMove;
    public DeeperBlueBot(){
        lastMove = new Move(new Vector2D(0, 0), new Vector2D(0,0));
        this.agent = new DeeperBlueAgent(-1, 12, 7, 3000, 800);
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
            System.out.println(new BoardRepresentation(board));
            long start = System.currentTimeMillis();
            int[] moveSquares = this.agent.makeMove(this.agent.translator.translateBoard(board), this.agent.player);
            int[] coordinates = new int[]{moveSquares[0]%8, moveSquares[0]/8, moveSquares[1]%8, moveSquares[1]/8};

            lastMove = new Move(new Vector2D(coordinates[0], coordinates[1]), new Vector2D(coordinates[2],coordinates[3]));
            lastMove = this.agent.player == 1 ? new Move(lastMove.from().getInverted(7), lastMove.to().getInverted(7)) : lastMove;
            long end = System.currentTimeMillis();
            System.out.println("Time: " + ((end - start)) + "ms");
            return lastMove;
        } catch (DeeperBlueException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Move getLastMove() {

        return this.lastMove;

    }

    @Override
    public void setDepthOrMillis(int depth) {
        this.agent.iterativeDeepeningMillis = depth;
    }

    @Override
    public int getDepthOrMillis() {
        return this.agent.iterativeDeepeningMillis;
    }
}
