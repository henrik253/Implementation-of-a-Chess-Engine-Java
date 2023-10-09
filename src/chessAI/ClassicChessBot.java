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
		depth = 4;
		color = ChessPieceColor.BLACK; // by default Black
	}

	@Override
	public Move makeMove(Piece[][] board) {

		BoardRepresentation boardR = new BoardRepresentation(board);
		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(boardR, color);

		Vector2D oldPos = null, newPos = null;

		for (Entry<Piece, Vector2D[]> pMoves : moves.entrySet()) {
			for (Vector2D move : pMoves.getValue()) {

			
				if (color.isWhite()) {
					int bestValue = Integer.MIN_VALUE;
					int val = MiniMax.max(boardR, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
					if (val > bestValue) {
						bestValue = val;
						oldPos = pMoves.getKey().getPosition().clone();
						newPos = move.clone();
					}

				} else {
					int bestValue = Integer.MAX_VALUE;
					int val = MiniMax.min(boardR, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
					
					if (val < bestValue) {
						bestValue = val;
						oldPos = pMoves.getKey().getPosition().clone();
						newPos = move.clone();
					}

				}
			}
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
