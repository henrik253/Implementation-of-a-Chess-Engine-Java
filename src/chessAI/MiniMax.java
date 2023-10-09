package chessAI;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import chessAI.logic.boardEvaluation.Evaluate;
import main.model.chessPieces.concretePieces.Piece;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;
import utils.ChessPieceColor;
import utils.Vector2D;

public class MiniMax {

	private static final int INFINITY = Integer.MAX_VALUE;
	private static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;

	private static MoveValidation validation = new MoveValidation();

	private static final ChessPieceColor WHITE = ChessPieceColor.WHITE;
	private static final ChessPieceColor BLACK = ChessPieceColor.BLACK;

	public static int max(BoardRepresentation boardR, int depth, int alpha, int beta) {
		if (depth == 0 || gameOver(boardR, WHITE))
			return evaluate(boardR, WHITE);

		int maxValue = NEGATIVE_INFINITY;

		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(boardR, WHITE);

		for (Entry<Piece, Vector2D[]> move : moves.entrySet()) {
			for (Vector2D newPos : move.getValue()) {

				boardR.makeMove(move.getKey().getPosition(), newPos);
				int value = -min(boardR, depth - 1, alpha, beta);
				boardR.undoLastMove();

				maxValue = Math.max(maxValue, value);
				alpha = Math.max(maxValue, beta);

				if (maxValue >= beta)
					break;
			}
		}

		return maxValue;
	}

	public static int min(BoardRepresentation boardR, int depth, int alpha, int beta) {
		if (depth == 0 || gameOver(boardR, BLACK))
			return evaluate(boardR, BLACK);

		int minValue = INFINITY;

		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(boardR, BLACK);

		for (Entry<Piece, Vector2D[]> move : moves.entrySet()) {
			for (Vector2D newPos : move.getValue()) {

				boardR.makeMove(move.getKey().getPosition(), newPos);
				int value = max(boardR, depth - 1, alpha, beta);
				boardR.undoLastMove();

				minValue = Math.min(minValue, value);

				beta = Math.min(minValue, beta);

				if (minValue <= alpha)
					break;

			}
		}
	
		return minValue;
	}

	private static int evaluate(BoardRepresentation boardR, ChessPieceColor color) {
		return Evaluate.calc(boardR, color) * 4;
	}

	private static boolean gameOver(BoardRepresentation boardR, ChessPieceColor color) {
		validation.setBoard(boardR);
		boardR.calcAttackedSquaresBy(color);
		validation.setOnMove(color.getOpponentColor());

		if (validation.enemyInCheck()) {
			if (validation.isCheckMate(boardR.getKing(color), boardR.getPiece(boardR.getLastMove().getNewPos()))) {
				return true;
			}

		}

		return false;
	}
}
