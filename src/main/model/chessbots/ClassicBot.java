package main.model.chessbots;

import ai2.ClassicChessBot;
import main.model.pieces.Piece;
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

	@Override
	public void setDepthOrMillis(int depth) {
		bot.setDepthOrMillis(depth);
	}

	@Override
	public int getDepthOrMillis() {
		return bot.getDepthOrMillis();
	}

}
