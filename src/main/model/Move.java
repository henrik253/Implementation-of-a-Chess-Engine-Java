package main.model;

import main.model.chessPieces.concretePieces.Piece;

public final class Move {

	private final Vector2D oldPos;
	private final Vector2D newPos;

	private Piece capturedPiece;

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
