package main.model.gameLogic;

import main.model.Model;
import main.model.Move;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.*;
import main.model.gameStates.GameOverReason;
import main.model.gameStates.GameState;
import main.model.gameStates.State;

import java.util.List;
import java.util.stream.Stream;

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

	public boolean makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece movedPiece = board.getPiece(oldPos);

		if (movedPiece == null || !playerOnMove(movedPiece) || !movedPiece.isValidMove(newPos)) {
			return false;
		}

		if (isMovingPiecePinned(oldPos, newPos) || kingInCheck(onMove)) {
			return false;
		}

		// Successfull Move if code comes here

		if (enemyInCheck()) {
			checkForCheckMate(movedPiece);
		}

		setNextPlayerOnMove(); // TODO in other class

		return true;
	}

	private boolean kingInCheck(ChessPieceColor color) {
		King king = color.isWhite() ? board.getWhiteKing() : board.getBlackKing();
		int[][] attackedSquares = color.isWhite() ? board.getAttackedSquaresByBlack()
				: board.getAttackedSquaresByWhite();

		return inCheck(attackedSquares, king);
	}

	private void checkForCheckMate(Piece piece) {
		if (isCheckMate(onMove.isWhite() ? board.getBlackKing() : board.getWhiteKing(), piece)) {
			initiateCheckMate(); // TODO mehr Trennung zwischen Model und Presenter
		}
	}

	private void initiateCheckMate() {
		State.gameState = GameState.GAME_OVER;
		State.gameOverReason = onMove.isWhite() ? GameOverReason.WHITE_WON : GameOverReason.BLACK_WON;
		System.out.println("CHECK MATE ! ");
	}

	private boolean isMovingPiecePinned(Vector2D oldPos, Vector2D newPos) {
		ChessPieceColor pieceColor = board.getPiece(oldPos).getColor();

		board.makeMove(oldPos, newPos);

		final int[][] attackedSquaresByEnemy = board.calcAttackedSquaresBy(onMove.getOpponentColor());

		King king = pieceColor.isWhite() ? board.getWhiteKing() : board.getBlackKing();

		if (inCheck(attackedSquaresByEnemy, king)) { // checks if own side would be in check if it does the move
			board.undoLastMove();
			return true;
		}

		board.calcAttackedSquaresBy(pieceColor); // neccessary if we want to proof if we check the enemy side

		this.board.setAttackedSquaresByWhite(board.getAttackedSquaresByWhite());
		this.board.setAttackedSquaresByBlack(board.getAttackedSquaresByBlack());

		return false;
	}

	private boolean inCheck(int[][] attackedSquares, King king) {// TODO Game Logic???
		return attackedSquares[king.getPosition().getY()][king.getPosition().getX()] > 0;
	}

//	public boolean isSpecialMove(Piece piece, Vector2D newPos) {
//
//		if (piece instanceof Pawn) {
//			Pawn pawn = (Pawn) piece;
//			if (isPawnAttack(pawn, newPos) || isEnPassantMove(pawn, newPos))
//				return true;
//		}
//
//		if (piece instanceof King) {
//			King king = (King) piece;
//			if (isCastleMove(king, newPos)) {
//				return true;
//			}
//		}
//
//		return false;
//	}

//	public boolean isPawnAttack(Pawn pawn, Vector2D newPos) {
//		Piece p = this.board.getPiece(newPos);
//		return pawn.isValidAttack(newPos) && p != null && p.getColor() != onMove;
//
//	}

//	public boolean isEnPassantMove(Pawn pawn, Vector2D newPos) { // Game Logic???
//		Piece p = this.board.getPiece(newPos);
//		Vector2D direction = new Vector2D(0, onMove.isWhite() ? 1 : -1);
//		Vector2D attackedPieceCoords = Vector2D.add(newPos, direction);
//		if (attackedPieceCoords.getX() >= 0 && attackedPieceCoords.getY() >= 0 && attackedPieceCoords.getX() < 8
//				&& attackedPieceCoords.getY() < 8) {
//			Piece attackedPiece = board.getPiece(attackedPieceCoords);
//			return pawn.isValidAttack(newPos) && p == null && attackedPiece instanceof Pawn
//					&& ((Pawn) attackedPiece).getColor() != onMove && lastMoveIsDoublePawnMove();
//		}
//		return false;
//	}

//	private boolean lastMoveIsDoublePawnMove() { // Game Logic???
//		Piece piece = board.getPiece(lastMove.getNewPos());
//		int yLength = (int) Math.abs(lastMove.getOldPos().getY() - lastMove.getNewPos().getY());
//		return piece instanceof Pawn && !(((Pawn) piece).isFirstMove()) && yLength == 2;
//	}

//	public boolean isCastleMove(King king, Vector2D newPos) { // Game Logic???
//		if (!king.isFirstMove())
//			return false;
//
//		if (!king.isValidCastle(newPos))
//			return false;
//
//		// left or right side castle
//		boolean isRightSideCastle = newPos.getX() - king.getPosition().getX() > 0;
//
//		int xPosRook = isRightSideCastle ? this.board.getBoard().length - 1 : 0;
//		Vector2D rookPos = new Vector2D(xPosRook, king.getPosition().getY());
//
//		if (board.countFiguresBetween(king.getPosition(), rookPos) > 0) {
//			return false;
//		}
//
//		if (board.isCheckBetween(king.getPosition(), rookPos)) {
//			return false;
//		}
//
//		Piece rook = board.getPiece(rookPos);
//
//		if (!(rook instanceof Rook && ((Rook) rook).isFirstMove())) {
//			return false;
//		}
//
//		return true;
//	}

//	public boolean tryCastling(Vector2D oldPos, Vector2D newPos) { // Game Logic???
//		Piece piece = board.getPiece(oldPos);
//		if (!(piece instanceof King))
//			return false;
//
//		King king = (King) piece;
//
//		if (!king.isFirstMove())
//			return false;
//
//		if (!king.isValidCastle(newPos))
//			return false;
//
//		// left or right side castle
//		boolean isRightSideCastle = newPos.getX() - king.getPosition().getX() > 0;
//
//		int xPos = isRightSideCastle ? this.board.getBoard().length - 1 : 0;
//		Vector2D rookPos = new Vector2D(xPos, king.getPosition().getY());
//
//		if (board.countFiguresBetween(king.getPosition(), rookPos) > 0) {
//			return false;
//		}
//
//		if (board.isCheckBetween(king.getPosition(), rookPos)) {
//			return false;
//		}
//
//		Piece rook = board.getPiece(rookPos);
//
//		if (rook instanceof Rook && ((Rook) rook).isFirstMove()) {
//			Vector2D direction = new Vector2D(isRightSideCastle ? -2 : 3, 0);
//			makeMove(rookPos, Vector2D.add(rookPos, direction));
//			return true;
//		}
//
//		return false;
//	}

	public boolean isCheckMate(King king, Piece movedPiece) {// TODO Game Logic???
		return !allyPieceStopsCheck(king, movedPiece) && !kingCanMove(king);
	}

	private boolean allyPieceStopsCheck(King king, Piece movedPiece) { // TODO use attackedSquares by Enemy ? King?
		List<Piece> enemyPieces = onMove.isWhite() ? board.getBlackPieces() : board.getWhitePieces();
		List<List<Vector2D>> attackingSquares = movedPiece.calculateAttackablePositions(movedPiece.getPosition());

		// movedPiece.getPosition() returns the updated position of the moved piece
		for (List<Vector2D> movesInDirection : attackingSquares) {
			if (movesInDirection.contains(king.getPosition())) { // direction the piece is checking the king
				for (Piece enemyPiece : enemyPieces) {
					List<Vector2D> enemyPieceMoves = enemyPiece.calculateAttackablePositions(enemyPiece.getPosition())
							.stream().flatMap(list -> list.stream()).toList();

					// Blocking the piece(s) delivering check || Capturing the checking piece.
					for (Vector2D enemyPieceMove : enemyPieceMoves) {
						if (canBeBlocked(movesInDirection, enemyPieceMove, enemyPiece, king)
								|| canBeCaptured(enemyPieceMove, enemyPiece, movedPiece)) {
							// the piece that can block or capture needs to checked if its pinned
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

	private boolean kingCanMove(King king) {
		King k = onMove.isWhite() ? board.getBlackKing() : board.getWhiteKing();
		int[][] attackedSquares = onMove.isWhite() ? board.getAttackedSquaresByWhite()
				: board.getAttackedSquaresByBlack();

		List<List<Vector2D>> moveablePositions = k.getAttackableSquares();

		// 1. Moving your king to a non-attacked square
		for (List<Vector2D> movesInDirection : moveablePositions) {
			for (Vector2D move : movesInDirection) {
				Piece piece = board.getPiece(move);
				System.out.print(move + " ");
				if (attackedSquares[move.getY()][move.getX()] == 0 && !isAllyPiece(piece, move)) {
					System.out.println("king Can move true");
					return true; // King can move on that square
				}
			}
		}
		System.out.println("king Can move false");
		return false;
	}

//	private boolean noPiecesBetween(Piece piece, Vector2D newPos) {
//		return this.board.countFiguresBetween(piece.getPosition(), newPos) == 0 || piece instanceof Knight;
//	}

	private boolean playerOnMove(Piece piece) {
		return onMove == piece.getColor();
	}

	private void setNextPlayerOnMove() {
		onMove = onMove.isWhite() ? ChessPieceColor.BLACK : ChessPieceColor.WHITE;
	}

	private boolean noPieceOn(Vector2D pos) {
		return board.getPiece(pos) == null;
	}

	private boolean isPiece(Vector2D pos) {
		return !noPieceOn(pos);
	}

	private boolean isEnemyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() != piece.getColor();
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
		Vector2D pos = onMove.isWhite() ? board.getBlackKing().getPosition() : board.getWhiteKing().getPosition();

		return attackedSquares[pos.getY()][pos.getX()] > 0;
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
