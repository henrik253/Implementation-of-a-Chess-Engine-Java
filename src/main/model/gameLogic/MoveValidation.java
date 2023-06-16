package main.model.gameLogic;

import java.util.List;

import main.model.Model;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.King;
import main.model.chessPieces.concretePieces.Knight;
import main.model.chessPieces.concretePieces.Piece;

public class MoveValidation {
	private Model model;

	private ChessPieceColor onMove;
	private BoardRepresentation board;

	private King whiteKing;
	private King blackKing;

	public MoveValidation(ChessPieceColor startingColor) {
		this.onMove = startingColor;
	}

	public MoveValidation() {
		this(ChessPieceColor.WHITE);
	}

	public void initKingReferences() {
		whiteKing = (King) board.getKing(ChessPieceColor.WHITE);
		blackKing = (King) board.getKing(ChessPieceColor.BLACK);
	}

	public boolean makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece currentPiece = board.getPiece(oldPos);
		boolean moveSucceed = false;

		if (!isOnMove(currentPiece))
			return false;

		if (!currentPiece.isValidMove(newPos) || !noPiecesBetween(currentPiece, newPos))
			return false;

		if (noPiece(newPos)) {
			moveSucceed = tryMove(oldPos, newPos);
		} else if (isEnemyPiece(currentPiece, newPos)) {
			if (tryMove(oldPos, newPos)) {
				moveSucceed = true;
				//pieceGotTaken(newPos);
			}

		} else if (isAllyPiece(currentPiece, newPos)) {
			moveSucceed = false;
		}

		if (moveSucceed) {
			if (enemyInCheck()) {

			}
			setNextPlayerOnMove();
		}
		return moveSucceed;
	}

	private boolean isCheckMate() {
		King k = onMove.isWhite() ? blackKing : whiteKing;
		int[][] attackableSqaures = onMove.isWhite() ? this.board.getAttackedSquaresByWhite()
				: this.board.getAttackedSquaresByBlack();
		List<Vector2D> moveablePositions = k.calculateMoveablePositions();

		for (Vector2D pos : moveablePositions) {

		}
		return false;
	}

//	private void pieceGotTaken(Vector2D newPos) {
//		model.removePieceFromBoard(newPos);
//	}

	private boolean tryMove(Vector2D oldPos, Vector2D newPos) {
		BoardRepresentation boardClone = this.board.clone();
		boardClone.makeMove(oldPos, newPos);
		this.board.checkSpecialMoves(oldPos, newPos);
		boardClone.calcAttackedSquares();

		int[][] attackedSqaures = onMove.isWhite() ? boardClone.getAttackedSquaresByBlack()
				: boardClone.getAttackedSquaresByWhite();
		King king = onMove.isWhite() ? this.whiteKing : this.blackKing;

		
		
		if (inCheck(attackedSqaures, king)) {
			return false;
		}
		System.out.println(this.board.toBoardString());
		this.board.setBoard(boardClone.getBoard());
		this.board.calcAttackedSquares(); // inefficient solution!
		
		return true;
	}

	private boolean inCheck(int[][] attackedSqaures, King king) {
		return attackedSqaures[king.getPosition().getY()][king.getPosition().getX()] > 0;
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

	private boolean noPiece(Vector2D pos) {
		return board.getPiece(pos) == null;
	}

	private boolean isPiece(Vector2D pos) {
		return !noPiece(pos);
	}

	private boolean isEnemyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() != piece.getColor();
	}

	private boolean isAllyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() == piece.getColor();
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
