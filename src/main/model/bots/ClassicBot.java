package main.model.bots;

import chessAI.ClassicChessBot;
import main.model.chessPieces.concretePieces.Piece;
import utils.ChessPieceColor;
import utils.Move;

public class ClassicBot implements ChessBot {

	private ChessBot bot = new ClassicChessBot();

	@Override
	public ChessPieceColor getColor() {
		return bot.getColor();
	}

	@Override
	public void setColor(ChessPieceColor color) {
		bot.setColor(color);
	}

	@Override
	public Move makeMove(Piece[][] board) {
		return bot.makeMove(board);
	}

	@Override
	public Move getLastMove() {
		return bot.getLastMove();
	}

}
