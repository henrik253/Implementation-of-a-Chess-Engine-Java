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

	private King whiteKing;
	private King blackKing;

	private Move lastMove;

	public MoveValidation(ChessPieceColor startingColor) {

		this.onMove = startingColor;
		this.lastMove = new Move(new Vector2D(0, 0), new Vector2D(0, 0));
	}

	public MoveValidation() {
		this(ChessPieceColor.WHITE);
	}

	public void initKingReferences() {
		whiteKing = (King) board.getKing(ChessPieceColor.WHITE);
		blackKing = (King) board.getKing(ChessPieceColor.BLACK);

	}

	public boolean makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece piece = board.getPiece(oldPos);

		if (piece == null) {
			return false;
		}

		boolean moveSucceed = false;

		if (!isOnMove(piece))
			return false;

		if (!piece.isValidMove(newPos) && !isSpecialMove(piece, newPos))
			return false;

		if (!noPiecesBetween(piece, newPos))
			return false;

		if (isPawnMovingOnEnemyPiece(piece, newPos))
			return false;

		if (noPieceOn(newPos)) {
			moveSucceed = tryMove(oldPos, newPos);
		} else if (isEnemyPiece(piece, newPos)) {
			if (tryMove(oldPos, newPos)) {
				moveSucceed = true;
			}

		} else if (isAllyPiece(piece, newPos)) {
			moveSucceed = false;
		}

		if (moveSucceed) {

			piece.setPosition(newPos);

			if (enemyInCheck()) {
				initiateCheck();
				if (isCheckMate(onMove.isWhite() ? this.blackKing : this.whiteKing, piece)) {
					initiateCheckMate();
				}
			}
			setNextPlayerOnMove();
			lastMove = new Move(oldPos, newPos);
		}

		return moveSucceed;
	}

	private void initiateCheck() {

	}

	private void initiateCheckMate() {
		State.gameState = GameState.GAME_OVER;
		State.gameOverReason = onMove.isWhite() ? GameOverReason.WHITE_WON : GameOverReason.BLACK_WON;
	}

	private boolean isPawnMovingOnEnemyPiece(Piece piece, Vector2D newPos) {
		return piece instanceof Pawn && ((Pawn) piece).isValidMove(newPos) && board.getPiece(newPos) != null;
	}

	private boolean tryMove(Vector2D oldPos, Vector2D newPos) {
		BoardRepresentation boardClone = this.board.clone();

		if (isSpecialMove(boardClone.getPiece(oldPos), newPos)) {
			boardClone.makeSpecialMove(oldPos, newPos);
		} else
			boardClone.makeMove(oldPos, newPos);

		boardClone.calcAttackedSquaresBy(onMove.getOpponentColor());

		int[][] attackedSquares = onMove.isWhite() ? boardClone.getAttackedSquaresByBlack()
				: boardClone.getAttackedSquaresByWhite();

		King king = onMove.isWhite() ? (King) boardClone.getKing(ChessPieceColor.WHITE)
				: (King) boardClone.getKing(ChessPieceColor.BLACK);

		if (inCheck(attackedSquares, king)) { // checks if own side would be in check if it does the move
			return false;
		}
		boardClone.calcAttackedSquaresBy(onMove); // neccessary if we want to proof if we check the enemy side

		this.board.setBoard(boardClone.getBoard());
		this.board.setAttackedSquaresByWhite(boardClone.getAttackedSquaresByWhite());
		this.board.setAttackedSquaresByBlack(boardClone.getAttackedSquaresByBlack());

		return true;
	}

	public boolean isSpecialMove(Piece piece, Vector2D newPos) {

		if (piece instanceof Pawn) {
			Pawn pawn = (Pawn) piece;
			if (isPawnAttack(pawn, newPos) || isEnPassantMove(pawn, newPos))
				return true;
		}

		if (piece instanceof King) {
			King king = (King) piece;
			if (isCastleMove(king, newPos)) {
				return true;
			}
		}

		return false;
	}

	public boolean isPawnAttack(Pawn pawn, Vector2D newPos) {
		Piece p = this.board.getPiece(newPos);
		return pawn.isValidAttack(newPos) && p != null && p.getColor() != onMove;

	}

	public boolean isEnPassantMove(Pawn pawn, Vector2D newPos) { // Game Logic???
		Piece p = this.board.getPiece(newPos);
		Vector2D direction = new Vector2D(0, onMove.isWhite() ? 1 : -1);
		Vector2D attackedPieceCoords = Vector2D.add(newPos, direction);
		if (attackedPieceCoords.getX() >= 0 && attackedPieceCoords.getY() >= 0 && attackedPieceCoords.getX() < 8
				&& attackedPieceCoords.getY() < 8) {
			Piece attackedPiece = board.getPiece(attackedPieceCoords);
			return pawn.isValidAttack(newPos) && p == null && attackedPiece instanceof Pawn
					&& ((Pawn) attackedPiece).getColor() != onMove && lastMoveIsDoublePawnMove();
		}
		return false;
	}

	private boolean lastMoveIsDoublePawnMove() { // Game Logic???
		Piece piece = board.getPiece(lastMove.getNewPos());
		int yLength = (int) Math.abs(lastMove.getOldPos().getY() - lastMove.getNewPos().getY());
		return piece instanceof Pawn && !(((Pawn) piece).isFirstMove()) && yLength == 2;
	}

	public boolean isCastleMove(King king, Vector2D newPos) { // Game Logic???
		if (!king.isFirstMove())
			return false;

		if (!king.isValidCastle(newPos))
			return false;

		// left or right side castle
		boolean isRightSideCastle = newPos.getX() - king.getPosition().getX() > 0;

		int xPosRook = isRightSideCastle ? this.board.getBoard().length - 1 : 0;
		Vector2D rookPos = new Vector2D(xPosRook, king.getPosition().getY());

		if (board.countFiguresBetween(king.getPosition(), rookPos) > 0) {
			return false;
		}

		if (board.isCheckBetween(king.getPosition(), rookPos)) {
			return false;
		}

		Piece rook = board.getPiece(rookPos);

		if (!(rook instanceof Rook && ((Rook) rook).isFirstMove())) {
			return false;
		}

		return true;
	}

	public boolean tryCastling(Vector2D oldPos, Vector2D newPos) { // Game Logic???
		Piece piece = board.getPiece(oldPos);
		if (!(piece instanceof King))
			return false;

		King king = (King) piece;

		if (!king.isFirstMove())
			return false;

		if (!king.isValidCastle(newPos))
			return false;

		// left or right side castle
		boolean isRightSideCastle = newPos.getX() - king.getPosition().getX() > 0;

		int xPos = isRightSideCastle ? this.board.getBoard().length - 1 : 0;
		Vector2D rookPos = new Vector2D(xPos, king.getPosition().getY());

		if (board.countFiguresBetween(king.getPosition(), rookPos) > 0) {
			return false;
		}

		if (board.isCheckBetween(king.getPosition(), rookPos)) {
			return false;
		}

		Piece rook = board.getPiece(rookPos);

		if (rook instanceof Rook && ((Rook) rook).isFirstMove()) {
			Vector2D direction = new Vector2D(isRightSideCastle ? -2 : 3, 0);
			makeMove(rookPos, Vector2D.add(rookPos, direction));
			return true;
		}

		return false;
	}

	private boolean inCheck(int[][] attackedSquares, King king) {// TODO Game Logic???
		return attackedSquares[king.getPosition().getY()][king.getPosition().getX()] > 0;
	}

	public boolean isCheckMate(King king, Piece movedPiece) {// TODO Game Logic???
		return !allyPieceStopsCheck(king, movedPiece) && !kingIsMoveable(king);
	}

	private boolean allyPieceStopsCheck(King king, Piece movedPiece) {
		List<Piece> enemyPieces = onMove.isWhite() ? board.getBlackPieces() : board.getWhitePieces();
		List<List<Vector2D>> attackingSquares = movedPiece.calculateAttackablePositions(movedPiece.getPosition());
		// movedPiece.getPosition() returns the updated position of the moved piece
		for (List<Vector2D> movesInDirection : attackingSquares) {
			System.out.println(movesInDirection);
			if (movesInDirection.contains(king.getPosition())) { // direction the piece is checking the king

				for (Piece enemyPiece : enemyPieces) {
					List<Vector2D> enemyPieceMoves = enemyPiece.getAttackableSquares().stream()
							.flatMap(list -> list.stream()).toList();

					// Blocking the piece(s) delivering check || Capturing the checking piece.
					for (Vector2D move : enemyPieceMoves) {
						if (movesInDirection.contains(move) || move.equals(enemyPiece.getPosition())) {
							return true;
						}

					}
				}
			}
		}

		return false;
	}

	private boolean kingIsMoveable(King king) {
		King k = onMove.isWhite() ? blackKing : whiteKing;
		int[][] attackedSquares = onMove.isWhite() ? this.board.getAttackedSquaresByWhite()
				: this.board.getAttackedSquaresByBlack();

		List<List<Vector2D>> moveablePositions = k.getAttackableSquares();

		// 1. Moving your king to a non-attacked square
		for (List<Vector2D> movesInDirection : moveablePositions) {
			for (Vector2D move : movesInDirection) {
				Piece piece = board.getPiece(move);

				if (attackedSquares[move.getY()][move.getX()] == 0 && !this.isAllyPiece(piece, move)) {
					return true; // King can move on that square
				}
			}
		}
		return false;
	}

	private boolean noPiecesBetween(Piece piece, Vector2D newPos) {
		return this.board.countFiguresBetween(piece.getPosition(), newPos) == 0 || piece instanceof Knight;
	}

	private boolean isOnMove(Piece piece) {
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
		Vector2D pos = onMove.isWhite() ? blackKing.getPosition() : whiteKing.getPosition();

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
		initKingReferences();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
