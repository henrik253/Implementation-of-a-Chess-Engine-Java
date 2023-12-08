package main.model.gameLogic;

import java.util.List;

import main.model.pieces.King;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Vector2D;

public class Check {

	public static boolean kingInCheckPieceMoves(BoardRepresentation board, Vector2D oldPos, Vector2D newPos) {
		ChessPieceColor pieceColor = board.getPiece(oldPos).getColor();

		board.makeMove(oldPos, newPos);
		final int[][] attackedSquares = board.calcAttackedSquaresBy(pieceColor.getOpponentColor());
		King k = board.getKing(pieceColor);

		// checks if own side would be in check if it does the move
		boolean check = attackedSquares[k.getPosition().getY()][k.getPosition().getX()] > 0;

		board.undoLastMove();
		return check;
	}

	// Search for a check with the king of the desired color
	public static Piece getCheckingPiece(BoardRepresentation board, ChessPieceColor mated) {
		King k = board.getKing(mated);
		Piece checkingPiece = null;
		Piece[][] pieceBoard = board.getBoard();
		for (int i = 0; i < pieceBoard.length; i++) {
			for (int j = 0; j < pieceBoard.length; j++) {
				Piece p = pieceBoard[i][j];
				if (p != null && !p.getColor().equals(mated)) {
					List<List<Vector2D>> allMovesOfPiece = pieceBoard[i][j].calculateAttackablePositions();
					for (List<Vector2D> movesInDir : allMovesOfPiece) {
						for (Vector2D move : movesInDir) {
							if (k.getPosition().equals(move)) {
								checkingPiece = p;
								return checkingPiece;
							}
						}
					}
				}
			}
		}

		return null;
	}

	public static boolean isMate(BoardRepresentation board, ChessPieceColor checkedSide) {
		return isMate(board, checkedSide, getCheckingPiece(board, checkedSide));
	}

	public static boolean isMate(BoardRepresentation board, ChessPieceColor mated, Piece checkingPiece) {
		if (kingInCheck(board, mated)) {
			return !kingCanMove(board, mated) && !checkCanBeStopped(board, mated, checkingPiece);
		}
		return false;
	}

	public static boolean kingInCheck(BoardRepresentation board, ChessPieceColor kingSide) {
		Vector2D kingPos = board.getKing(kingSide).getPosition();
		return board.calcAttackedSquaresBy(kingSide.getOpponentColor())[kingPos.getY()][kingPos.getX()] > 0;
	}

	public static boolean kingCanMove(BoardRepresentation board, ChessPieceColor mated) {
		King k = board.getKing(mated);
		Vector2D kPos = k.getPosition();
		board.getBoard()[kPos.getY()][kPos.getX()] = null; // king could move on
															// squares behind
															// him that arent
															// checked
		int[][] attackedSquares = board.calcAttackedSquaresBy(mated.getOpponentColor());
		board.getBoard()[kPos.getY()][kPos.getX()] = k;
		List<List<Vector2D>> moves = k.calculateMoveablePositions(); // shows
																		// squar

		for (List<Vector2D> movesInDir : moves) {
			for (Vector2D move : movesInDir) {
				if (attackedSquares[move.getY()][move.getX()] == 0)
					return true;
			}
		}
		return false;
	}

	public static boolean checkCanBeStopped(BoardRepresentation board, ChessPieceColor mated, Piece checkingPiece) {
		List<Piece> pieces = board.getPieces(mated);
		King k = board.getKing(mated);
		List<List<Vector2D>> attackingSquares = checkingPiece.calculateAttackablePositions();
		int[][] attackedSquares = board.calcAttackedSquaresBy(mated.getOpponentColor());

		// Double check cant be stopped, king has to move away
		if (attackedSquares[k.getPosition().getY()][k.getPosition().getX()] >= 2) {
			return false;
		}

		// getting the direction the piece is checking
		List<Vector2D> checkingDirection = null;
		for (List<Vector2D> movesInDirection : attackingSquares) {
			if (movesInDirection.contains(k.getPosition())) {
				checkingDirection = movesInDirection;
				break;
			}
		}
		
		if(checkingDirection == null) {
			return false;
		}
		
		for (Piece stoppingCheckPiece : pieces) {

			if (isPiecePinned(board, stoppingCheckPiece))
				continue;

			List<Vector2D> movesOfCapturingPiece = stoppingCheckPiece.calculateMoveablePositions().stream()
					.flatMap(list -> list.stream()).toList();
			// Blocking the piece(s) delivering check || Capturing the checking
			// piece.
			for (Vector2D capturingPieceMove : movesOfCapturingPiece) {

				if (canBeBlocked(checkingDirection, capturingPieceMove, stoppingCheckPiece, k)
						|| checkingPiece.getPosition().equals(capturingPieceMove)) {

					return true;
				}
			}
		}

		return false;
	}

	public static boolean isPiecePinned(BoardRepresentation board, Piece p) {
		if (p instanceof King) { // king itself cant be pinned
			return false;
		}
		Vector2D kingPos = board.getKing(p.getColor()).getPosition();

		int[][] attackedSquaresWithPiece = board.calcAttackedSquaresBy(p.getColor().getOpponentColor());

		Piece possiblePinnedPiece = board.getBoard()[p.getPosition().getY()][p.getPosition().getX()];
		board.getBoard()[p.getPosition().getY()][p.getPosition().getX()] = null; // removing
																					// piece
																					// from
																					// board
		int[][] attackedSquaresWithoutPiece = board.calcAttackedSquaresBy(p.getColor().getOpponentColor());
		board.getBoard()[p.getPosition().getY()][p.getPosition().getX()] = possiblePinnedPiece;

		// if piece is removed and the attackedSquare of king is the same the
		// piece was not pinned
		return attackedSquaresWithPiece[kingPos.getY()][kingPos
				.getX()] != attackedSquaresWithoutPiece[kingPos.getY()][kingPos.getX()];
	}

	private static boolean canBeBlocked(List<Vector2D> checkingDirection, Vector2D enemyPieceMove, Piece enemyPiece,
			King enemyKing) {

		if (enemyPieceMove.equals(enemyKing.getPosition()) || checkingDirection.isEmpty()) { // kings pos in not
			return false; // included in can be blocked!
		}
		return checkingDirection.contains(enemyPieceMove) && enemyPiece != enemyKing;
	}

	// TODO

}
