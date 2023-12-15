package ai2;

import java.util.Map;
import java.util.NoSuchElementException;

import ai2.boardEvaluation.Evaluate;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.Check;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class MiniMax {

	private static final ChessPieceColor WHITE = ChessPieceColor.WHITE;
	private static final ChessPieceColor BLACK = ChessPieceColor.BLACK;
	private static final float MAX = 1000000.0f;
	
	private static  int maxDepth; 
	
	public static Move miniMaxRoot(BoardRepresentation board, ChessPieceColor onMove, int depth) {
		maxDepth = depth;
		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(board, onMove);
		Vector2D from = null, to = null;

		float bestValue = Float.NEGATIVE_INFINITY;
		for (Map.Entry<Piece, Vector2D[]> pieceWithMoves : moves.entrySet()) {

			Vector2D piecePos = pieceWithMoves.getKey().getPosition();

			for (Vector2D moveOfPiece : pieceWithMoves.getValue()) {

				board.makeMove(piecePos, moveOfPiece);
				float evaluated = miniMax(board, onMove.getOpponentColor(), false, depth, Float.NEGATIVE_INFINITY,
						Float.POSITIVE_INFINITY);

				board.undoLastMove();

				if (evaluated >= bestValue) { //
					from = piecePos;
					to = moveOfPiece;
					bestValue = evaluated;
				}
				System.out.println(pieceWithMoves.getKey() + " -> " + moveOfPiece + " evaluated: " + evaluated);
			}
		}

		if (from == null || to == null)
			throw new NoSuchElementException("couldnt find a move in MinimaxRoot");

		return new Move(from, to);
	}

	public static float miniMax(BoardRepresentation board, ChessPieceColor player, boolean maximazingPlayer, int depth,
			float alpha, float beta) {

		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(board, player);
		if (depth == 0 ) {
			return Evaluate.evaluate(board, player,maximazingPlayer, maxDepth - depth);
		}
		
		if(gameOver(board,player)) {
			return maximazingPlayer ? -(MAX + depth) : MAX + depth; // if it would be the maximazing Player its bad
		}
		
		if (maximazingPlayer) {

			float maxValue = Float.NEGATIVE_INFINITY;
			for (Map.Entry<Piece, Vector2D[]> pieceWithMoves : moves.entrySet()) {
				for (Vector2D moveOfPiece : pieceWithMoves.getValue()) {
					board.makeMove(pieceWithMoves.getKey().getPosition(), moveOfPiece);
					float evaluation = miniMax(board, player.getOpponentColor(), false, depth - 1, alpha, beta);
					maxValue = Math.max(maxValue, evaluation);
					alpha = Math.max(alpha, maxValue);
					board.undoLastMove();

					if (beta <= alpha) {
						return maxValue;
					}
				}
			}
			return maxValue;

		} else {

			float minValue = Float.POSITIVE_INFINITY;

			for (Map.Entry<Piece, Vector2D[]> pieceWithMoves : moves.entrySet()) {
				for (Vector2D moveOfPiece : pieceWithMoves.getValue()) {
					board.makeMove(pieceWithMoves.getKey().getPosition(), moveOfPiece);

					float evaluation = miniMax(board, player.getOpponentColor(), true, depth - 1, alpha, beta);
					minValue = Math.min(evaluation, minValue);
					beta = Math.min(beta, minValue);
					board.undoLastMove();
					if (beta <= alpha) {
						return minValue;
					}

				}
			}
			return minValue;
		}
	}

	private static boolean gameOver(BoardRepresentation boardR, ChessPieceColor color) {
		return Check.kingInCheck(boardR, color) && Check.isMate(boardR, color);
	}
}
