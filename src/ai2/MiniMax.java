package ai2;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import ai2.boardEvaluation.Evaluate;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.Check;
import main.model.gameLogic.MoveValidation;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Vector2D;

public class MiniMax {

	private static final float INFINITY = Float.MAX_VALUE;
	private static final float NEGATIVE_INFINITY = Float.MIN_VALUE;

	private static final ChessPieceColor WHITE = ChessPieceColor.WHITE;
	private static final ChessPieceColor BLACK = ChessPieceColor.BLACK;

	public static float max(BoardRepresentation boardR, int depth, float alpha,
			float beta) {
		if (depth == 0 || gameOver(boardR, WHITE))
			return evaluate(boardR, WHITE);

		float maxValue = NEGATIVE_INFINITY;
		System.out.println(boardR);
		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(boardR, WHITE);

		for (Entry<Piece, Vector2D[]> move : moves.entrySet()) {
			for (Vector2D newPos : move.getValue()) {
				Piece p = move.getKey();
				boardR.makeMove(p.getPosition(), newPos);

				float value = -min(boardR, depth - 1, alpha, beta);

				boardR.undoLastMove();

				maxValue = Math.max(maxValue, value);
				alpha = Math.max(maxValue, beta);

				if (maxValue >= beta)
					break;
			}
		}

		return maxValue;
	}

	public static float min(BoardRepresentation boardR, int depth, float alpha,
			float beta) {
		if (depth == 0 || gameOver(boardR, BLACK))
			return evaluate(boardR, BLACK);

		float minValue = INFINITY;

		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(boardR, BLACK);

		for (Entry<Piece, Vector2D[]> move : moves.entrySet()) {
			for (Vector2D newPos : move.getValue()) {

				boardR.makeMove(move.getKey().getPosition(), newPos);
				float value = max(boardR, depth - 1, alpha, beta);
				boardR.undoLastMove();

				minValue = Math.min(minValue, value);

				beta = Math.min(minValue, beta);

				if (minValue <= alpha)
					break;

			}
		}

		return minValue;
	}

	private static float evaluate(BoardRepresentation boardR,
			ChessPieceColor color) {
		return Evaluate.evaluate(boardR, color);
	}

	private static boolean gameOver(BoardRepresentation boardR,
			ChessPieceColor color) {
		return Check.kingInCheck(boardR, color) && Check.isMate(boardR, color);
	}
}
