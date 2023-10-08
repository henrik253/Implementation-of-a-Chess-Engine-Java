package chessAI;

import java.util.Map;
import java.util.Map.Entry;

import main.model.bots.ChessBot;
import main.model.chessPieces.concretePieces.Piece;
import main.model.gameLogic.BoardRepresentation;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class ClassicChessBot implements ChessBot {

	private int depth;
	private ChessPieceColor color;
	private Move move;

	public ClassicChessBot() {
		depth = 8;
		color = ChessPieceColor.BLACK; // by default Black
	}

	@Override
	public Move makeMove(Piece[][] board) {

		BoardRepresentation boardR = new BoardRepresentation(board);
		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(boardR, color);

		Vector2D oldPos = null, newPos = null;
		Move bestMove = new Move(oldPos, newPos);

		for (Entry<Piece, Vector2D[]> pMoves : moves.entrySet()) {
			for (Vector2D move : pMoves.getValue()) {

				int bestValue = 0;
				if (color.isWhite()) {
					int val = MiniMax.max(boardR, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

					if (val > bestValue) {
						bestValue = val;
						oldPos = pMoves.getKey().getPosition().clone();
						newPos = move.clone();
					}

				} else {

					int val = MiniMax.min(boardR, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
					if (val < bestValue) {
						bestValue = val;
						oldPos = pMoves.getKey().getPosition().clone();
						newPos = move.clone();
					}

				}
			}
		}

		return bestMove;
	}

	@Override
	public Move getLastMove() {

		return null;
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
