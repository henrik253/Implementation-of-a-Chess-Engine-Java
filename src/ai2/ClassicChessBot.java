package ai2;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import ai2.opening.OpeningBook;
import main.model.chessbots.ChessBot;
import main.model.gameLogic.BoardRepresentation;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class ClassicChessBot implements ChessBot {

	private int depth;
	private ChessPieceColor color;
	private Move move;

	private OpeningBook openingBook = OpeningBook.openingBook;

	public ClassicChessBot() {
		depth = 4;
		color = ChessPieceColor.BLACK; // by default Black
	}

	@Override
	public Move makeMove(Piece[][] board) {
		BoardRepresentation boardR = new BoardRepresentation(board);
		if (openingBook.isInit()) {
			try {
				move = openingBook.getNextMove(board);

				if (move.from() == null || move.to() == null) {
					throw new IllegalArgumentException();
				}
				return move;
			} catch (Exception e) {
			}
		}
		move = MiniMax.miniMaxRootParallel(boardR, color, depth);
		return move;
	}

	@Override
	public Move getLastMove() {
		return move;
	}

	@Override
	public void setDepthOrMillis(int depth) {

	}

	@Override
	public int getDepthOrMillis() {
		return 0;
	}

	@Override
	public ChessPieceColor getColor() {
		return color;
	}

	@Override
	public void setColor(ChessPieceColor color) {
		this.color = color;
	}
}
