package utils;

import main.model.chessPieces.concretePieces.Piece;

public final class Move {

	private final Vector2D oldPos;
	private final Vector2D newPos;

	private Piece movedPiece;

	private Piece capturedPiece;

	private Piece transformed;

	private boolean movedPieceFirstMove = false;

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
		return oldPos.toString() + " " + newPos.toString() + " " + capturedPiece;
	}

}
