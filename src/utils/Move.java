package utils;

import main.model.pieces.Piece;
import main.model.pieces.Rook;

public final class Move {

	private final Vector2D oldPos;
	private final Vector2D newPos;

	private Piece movedPiece;

	private Piece capturedPiece;

	private Piece transformed;
	
	private Vector2D oldRookPos;
	private Vector2D newRookPos;
	private Rook rook;
	
	private boolean movedPieceFirstMove = false;
	public Vector2D getOldRookPos() {
		return oldRookPos;
	}

	public void setOldRookPos(Vector2D oldRookPos) {
		this.oldRookPos = oldRookPos;
	}

	public Vector2D getNewRookPos() {
		return newRookPos;
	}

	public void setNewRookPos(Vector2D newRookPos) {
		this.newRookPos = newRookPos;
	}

	public Rook getRook() {
		return rook;
	}

	public void setRook(Rook rook) {
		this.rook = rook;
	}

	public boolean isCastlingMove() {
		return castlingMove;
	}

	public void setCastlingMove(boolean castlingMove) {
		this.castlingMove = castlingMove;
	}

	public boolean isMovedPieceFirstMove() {
		return movedPieceFirstMove;
	}

	private boolean castlingMove = false;

	public Move(Vector2D oldPos, Vector2D newPos) {
		this.oldPos = oldPos;
		this.newPos = newPos;
	}

	public boolean pieceGotCaptured() {
		return capturedPiece != null;
	}

	public void setCapturedPiece(Piece capturedPiece) {
		this.capturedPiece = capturedPiece;
	}

	public Piece getCapturedPiece() {
		return capturedPiece;
	}

	public void setTransformedPiece(Piece piece) {
		transformed = piece;
	}

	public Piece getTransformed() {
		return transformed;
	}

	public boolean pieceTransformed() {
		return transformed != null;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public void setMovedPiece(Piece p) {
		movedPiece = p;
	}

	public boolean wasFirstMoveOfMovedPiece() {
		return movedPieceFirstMove;
	}

	public void setMovedPieceFirstMove(boolean movedPieceFirstMove) {
		this.movedPieceFirstMove = movedPieceFirstMove;
	}

	public Vector2D getOldPos() {
		return oldPos;
	}

	public Vector2D getNewPos() {
		return newPos;
	}

	@Override
	public String toString() {
		return oldPos.toString() + " -> " + newPos.toString();
	}

}
