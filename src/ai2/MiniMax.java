package ai2;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.*;

import ai2.boardEvaluation.Evaluate;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.Check;
import main.model.gameLogic.Remis;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class MiniMax {

	private static final ChessPieceColor WHITE = ChessPieceColor.WHITE;
	private static final ChessPieceColor BLACK = ChessPieceColor.BLACK;
	private static final int MAX = 10000000;
	private static final int MIN = -MAX;
	private static final int POOL_SIZE = 12;

	private static int maxDepth;

	public static Move miniMaxRoot(BoardRepresentation board, ChessPieceColor onMove, int depth) {
		maxDepth = depth;

		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(board, onMove);
		Vector2D from = null, to = null;

		int bestValue = MIN;
		for (Map.Entry<Piece, Vector2D[]> pieceWithMoves : moves.entrySet()) {
			Vector2D piecePos = pieceWithMoves.getKey().getPosition();

			for (Vector2D moveOfPiece : pieceWithMoves.getValue()) {

				board.makeMove(piecePos, moveOfPiece);
				int evaluated = miniMax(board, false, onMove.getOpponentColor(), depth, MIN, MAX);

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

	private static class MoveValuePair {
		public final Move move;
		public final int value;

		public MoveValuePair(Move move, int value) {
			this.move = move;
			this.value = value;
		}

	}

	public static Move miniMaxRootParallel(BoardRepresentation board, ChessPieceColor onMove, int depth) {
		maxDepth = depth;
		ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);
		List<Future<MoveValuePair>> results = new LinkedList<>();

		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(board, onMove);

		for (Map.Entry<Piece, Vector2D[]> pieceWithMoves : moves.entrySet()) {

			Vector2D piecePos = pieceWithMoves.getKey().getPosition();
			for (Vector2D moveOfPiece : pieceWithMoves.getValue()) {
				BoardRepresentation boardClone = board.clone();

				Future<MoveValuePair> pair = executorService.submit(() -> {

					boardClone.makeMove(piecePos.clone(), moveOfPiece.clone());

					int evaluated = miniMax(boardClone, false, onMove.getOpponentColor(), depth, MIN, MAX);

					boardClone.undoLastMove();

					System.out.println(pieceWithMoves.getKey() + " -> " + moveOfPiece + " evaluated: " + evaluated);
					return new MoveValuePair(new Move(pieceWithMoves.getKey().getPosition(), moveOfPiece), evaluated);
				});
				results.add(pair);
			}
		}

		Move bestMove = null;
		int best = MIN;

		for (Future<MoveValuePair> future : results) {
			MoveValuePair pair = null;
			try {
				pair = future.get();
				if (onMove.isWhite()) {
					if (pair.value >= best) { //
						bestMove = pair.move;
						best = pair.value;
					}
				}
			} catch (InterruptedException e) {
				executorService.shutdown();
				System.err.println("ClassicChessBot interrupted in MiniMaxParallel");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		System.out.println("bestMove: " + bestMove);

		executorService.shutdown();

		if (bestMove == null)
			throw new NoSuchElementException("couldnt find a move in MinimaxRootParallel");

		return bestMove;
	}

	public static int miniMax(BoardRepresentation board, boolean maximazingPlayer, ChessPieceColor player, int depth,
			int alpha, int beta) {

		if (depth == 0) {
			return Evaluate.evaluate(board); // // negative values for black side and
		}

		Map<Piece, Vector2D[]> moves = MoveGeneration.getMoves(board, player);

		if (moves.size() == 0) {
			if (gameOver(board, player)) {
				int currentDepth = maxDepth - depth;
				return maximazingPlayer ? MIN + currentDepth : MAX - currentDepth;
			}

			// otherwise Remis
			return 0;
		}
		// minus the currentDepth, a mate at a low depth is better rated then a mate in
		// a high depth

		if (maximazingPlayer) { // maximazing player wants positive values as result if he s good, if the
								// maximazing player is black
								// we need to negate the values
			int maxValue = MIN;
			for (Map.Entry<Piece, Vector2D[]> pieceWithMoves : moves.entrySet()) {
				for (Vector2D moveOfPiece : pieceWithMoves.getValue()) {
					board.makeMove(pieceWithMoves.getKey().getPosition(), moveOfPiece);
					int evaluation = miniMax(board, false, player.getOpponentColor(), depth - 1, alpha, beta);

					if (player.isBlack()) {
						evaluation = -evaluation;
					}

					maxValue = Math.max(maxValue, evaluation);
					alpha = (int) Math.max(alpha, maxValue);
					board.undoLastMove();

					if (beta <= alpha) {
						return maxValue;
					}
				}
			}
			return maxValue;
		} else {
			int minValue = MAX;

			for (Map.Entry<Piece, Vector2D[]> pieceWithMoves : moves.entrySet()) {
				for (Vector2D moveOfPiece : pieceWithMoves.getValue()) {

					board.makeMove(pieceWithMoves.getKey().getPosition(), moveOfPiece);
					int evaluation = miniMax(board, true, player.getOpponentColor(), depth - 1, alpha, beta);
					board.undoLastMove();

					if (player.isWhite()) {
						evaluation = -evaluation;
					}

					minValue = Math.min(evaluation, minValue);
					beta = Math.min(beta, minValue);

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
