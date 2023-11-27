package main.model.gameLogic;

import main.model.Model;
import main.model.pieces.*;
import utils.ChessPieceColor;
import utils.GameOverReason;
import utils.GameState;
import utils.InCheck;
import utils.State;
import utils.Vector2D;

import java.util.LinkedList;
import java.util.List;

public class MoveValidation {
	private Model model;

	private ChessPieceColor onMove;
	private BoardRepresentation board;

	public MoveValidation(ChessPieceColor startingColor) {
		this.onMove = startingColor;
	}

	public MoveValidation() {
		this(ChessPieceColor.WHITE);
	}

	public void reStart() {
		this.onMove = ChessPieceColor.WHITE;
	}

	public boolean makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece movedPiece = board.getPiece(oldPos);
		
		if (isNoValidMove(oldPos, newPos)) {
			return false;
		}
		
		board.makeMove(oldPos, newPos);
		board.calcAttackedSquaresBy(movedPiece.getColor());

		boolean inCheck = enemyInCheck();

		if (inCheck) {
			testCheckMate(movedPiece);
		} else if (enemyInRemi()) {
			remis();
		}

		if (inCheck) {
			State.inCheck = InCheck.ON;
			State.lastInCheck = onMove.getOpponentColor();
		} else {
			State.inCheck = InCheck.OFF;
		}

		setNextPlayerOnMove();

		return true;
	}

	private boolean isNoValidMove(Vector2D oldPos, Vector2D newPos) {
		Piece movedPiece = board.getPiece(oldPos);
		if (movedPiece == null)
			return false;
		return !playerOnMove(movedPiece) || !movedPiece.isValidMove(newPos) || isAllyPiece(movedPiece, newPos)
				|| kingInCheckIfPieceMoves(oldPos, newPos);
	}

	private void testCheckMate(Piece piece) {
		if (isCheckMate(board.getKing(onMove.getOpponentColor()), piece)) {
			initiateCheckMate(); 
		}
	}

	private void initiateCheckMate() {
		State.gameState = GameState.GAME_OVER;
		State.gameOverReason = onMove.isWhite() ? GameOverReason.WHITE_WON : GameOverReason.BLACK_WON;
		System.out.println("CHECK MATE ! ");
	}

	public boolean kingInCheckIfPieceMoves(Vector2D oldPos, Vector2D newPos) {
		ChessPieceColor pieceColor = board.getPiece(oldPos).getColor();

		board.makeMove(oldPos, newPos);

		final int[][] attackedSquaresByEnemy = board.calcAttackedSquaresBy(pieceColor.getOpponentColor());

		King king = board.getKing(pieceColor);

		// checks if own side would be in check if it does the move
		boolean check = inCheck(attackedSquaresByEnemy, king);

		board.undoLastMove();
		board.calcAttackedSquaresBy(pieceColor.getOpponentColor()); // neccessary if we want to proof if we check the
																	// enemy side

		return check;
	}

	private boolean inCheck(int[][] attackedSquares, King king) {// TODO Game Logic???
		return attackedSquares[king.getPosition().getY()][king.getPosition().getX()] > 0;
	}

	public boolean isCheckMate(King king, Piece movedPiece) {// TODO Game Logic???
		return !allyPieceStopsCheck(king, movedPiece) && !kingCanMove(king);
	}

	private boolean kingCanMove(King king) {
		King k = board.getKing(onMove.getOpponentColor());
		int[][] attackedSquares = onMove.isWhite() ? board.getAttackedSquaresByWhite()
				: board.getAttackedSquaresByBlack();

		List<List<Vector2D>> moveablePositions = k.getAttackableSquares();

		// 1. Moving your king to a non-attacked square
		for (List<Vector2D> movesInDirection : moveablePositions) {
			for (Vector2D move : movesInDirection) {
				Piece piece = board.getPiece(move);
				if (attackedSquares[move.getY()][move.getX()] == 0 && !isAllyPiece(piece, move)) {
					if (!kingInCheckIfPieceMoves(k.getPosition(), move)) { // King can move on that square
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean allyPieceStopsCheck(King king, Piece movedPiece) { // TODO use attackedSquares by Enemy ? King?
		List<Piece> enemyPieces = onMove.isWhite() ? board.getBlackPieces() : board.getWhitePieces();
		List<List<Vector2D>> attackingSquares = movedPiece.calculateAttackablePositions();

		// movedPiece.getPosition() returns the updated position of the moved piece
		for (List<Vector2D> movesInDirection : attackingSquares) {
			if (movesInDirection.contains(king.getPosition())) { // direction the piece is checking the king
				for (Piece enemyPiece : enemyPieces) {
					List<Vector2D> enemyPieceMoves = enemyPiece.calculateMoveablePositions().stream()
							.flatMap(list -> list.stream()).toList();

					// Blocking the piece(s) delivering check || Capturing the checking piece.
					for (Vector2D enemyPieceMove : enemyPieceMoves) {
						if ((canBeBlocked(movesInDirection, enemyPieceMove, enemyPiece, king)
								|| canBeCaptured(enemyPieceMove, enemyPiece, movedPiece))
								&& !kingInCheckIfPieceMoves(enemyPiece.getPosition(), enemyPieceMove)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean canBeBlocked(List<Vector2D> movesInDirection, Vector2D enemyPieceMove, Piece enemyPiece,
			King enemyKing) {

		if (enemyPieceMove.equals(enemyKing.getPosition())) // kings pos in not included in can be blocked!
			return false;

		return movesInDirection.contains(enemyPieceMove) && enemyPiece != enemyKing;
	}

	private boolean canBeCaptured(Vector2D enemyPieceMove, Piece enemyPiece, Piece movedPiece) {
		int[][] attackedSquares = onMove.isWhite() ? board.getAttackedSquaresByWhite()
				: board.getAttackedSquaresByBlack();

		if (enemyPiece instanceof King) { // king can only take piece if is not in check after this
			return enemyPieceMove.equals(movedPiece.getPosition())
					&& attackedSquares[movedPiece.getPosition().getY()][movedPiece.getPosition().getX()] == 0;
		}
		return enemyPieceMove.equals(movedPiece.getPosition());
	}

	private boolean enemyInRemi() { // Fehleranfällig?
		return false;
	}

	private void remis() {
		State.gameState = GameState.GAME_OVER;
		State.gameOverReason = GameOverReason.DRAW;
	}

	private boolean playerOnMove(Piece piece) {
		return onMove == piece.getColor();
	}

	private void setNextPlayerOnMove() {
		onMove = onMove.getOpponentColor();
	}

	private boolean isAllyPiece(Piece piece, Vector2D pos) {
		Piece otherPiece = board.getPiece(pos);

		if (otherPiece == null)
			return false;

		return otherPiece.getColor() == piece.getColor();
	}

	public boolean enemyInCheck() {
		int[][] attackedSquares = onMove.isWhite() ? board.getAttackedSquaresByWhite()
				: board.getAttackedSquaresByBlack();
		Vector2D pos = board.getKing(onMove.getOpponentColor()).getPosition();
		
		
		boolean inCheck = attackedSquares[pos.getY()][pos.getX()] > 0;

		return inCheck;
	}

	public ChessPieceColor getOnMove() {
		return onMove;
	}

	public void setOnMove(ChessPieceColor onMove) {
		this.onMove = onMove;
	}

	public BoardRepresentation getBoard() {
		return board;
	}

	public void setBoard(BoardRepresentation board) {
		this.board = board;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
