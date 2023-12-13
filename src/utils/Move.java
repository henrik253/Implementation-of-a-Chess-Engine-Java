package utils;

import main.model.pieces.Piece;
import main.model.pieces.Rook;

public final class Move {

	public Piece getPromotingPawn() {
		return promoting;
	}

	private final Vector2D oldPos;
	private final Vector2D newPos;

	private Piece movedPiece;

	private Piece capturedPiece;

	private Piece promoting;
	private ChessPieceName promotingPiece;

	private boolean castlingMove = false;

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

	// can either be used to say that a piece will promote or to set a piece that is
	// promoted like queen
	public void setPromotingPiece(Piece pawn, ChessPieceName promotingPiece) {
		promoting = pawn;
		this.promotingPiece = promotingPiece;
	}

	public ChessPieceName getPromotingPiece() {
		return promotingPiece;
	}

	public Piece getPromoting() {
		return promoting;
	}

	public boolean pawnWillPromote() {
		return promoting != null;
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

		return (oldPos == null ? "null" : oldPos.toString()) + " -> " + (newPos == null ? "null" : newPos.toString());
	}

}
