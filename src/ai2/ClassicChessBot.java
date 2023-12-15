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
		depth = 3;
		color = ChessPieceColor.BLACK; // by default Black
	}

	@Override
	public Move makeMove(Piece[][] board) {

		BoardRepresentation boardR = new BoardRepresentation(board);

		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(boardR, color);

		Vector2D oldPos = null, newPos = null;

//		if (openingBook.hasNextMove()) {
//			try {
//				move = openingBook.getNextMove(board);
//
//				if (move.from() == null || move.to() == null) {
//					throw new IllegalArgumentException();
//				}
//				return move;
//			} catch (Exception e) {
//			}
//		}

		for (Entry<Piece, Vector2D[]> pMoves : moves.entrySet()) {
			System.out.println(pMoves.getKey());
			for (Vector2D move : pMoves.getValue()) {
				System.out.println(move);
				if (color.isWhite()) {
					float bestValue = Float.MIN_VALUE;
					float val = MiniMax.max(boardR, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, color);
					System.out.print(" " + val + "\n");
					if (val >= bestValue) {
						bestValue = val;
						oldPos = pMoves.getKey().getPosition().clone();
						newPos = move.clone();
					}

				} else {
					float bestValue = Float.MAX_VALUE;
					float val = MiniMax.min(boardR, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, color);

					if (val <= bestValue) {
						bestValue = val;
						oldPos = pMoves.getKey().getPosition().clone();
						newPos = move.clone();
					}

				}
			}
		}
		if (oldPos == null || newPos == null) {
			throw new NoSuchElementException(" ClassicChessBot couldnt find move");
		}

		move = new Move(oldPos, newPos);
		return move;
	}

	@Override
	public Move getLastMove() {
		return move;
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
