package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public class King extends Piece {

	private Vector2D[] attackDirections = { new Vector2D(1, 0), new Vector2D(-1, 0), new Vector2D(0, 1),
			new Vector2D(0, -1), new Vector2D(1, 1), new Vector2D(-1, -1), new Vector2D(-1, 1), new Vector2D(1, -1) };

	public King(ChessPieceColor color, int row, int column) {
		super(ChessPieceName.KING, color, row, column);

	}

	@Override
	public boolean isValidMove(Vector2D position) {
		boolean validMovement = super.isValidMove(position);

		return validMovement || isValidCastle(position);
	}

	// calculateAttackablePositions returns squares where king would be in check ,
	// moveValidation checks that this wont happen, so we dont have to do this in
	// this class
	// TODO calculateAttackablePositions or moveablePos the castling
	@Override
	public List<List<Vector2D>> calculateAttackablePositions() {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = position.clone();

			possiblePosition.plus(direction); // currentPosition should not be included

			if (!outOfBounds(possiblePosition)) {
				Piece piece = board.getPiece(possiblePosition);

				// pieces were king would be in check will be shown
				if (piece != null && piece.getColor() == color) // cant step on ally piece
					continue;

				movesInDirection.add(possiblePosition.clone()); // add to linked list
				possiblePosition.plus(direction); // addition

			}
			moves.add(movesInDirection);
		}
		this.attackableSquares = moves;

		return moves;
	}

	@Override
	public void executeMove(Vector2D oldPos, Vector2D newPos) {

		if (isValidCastle(newPos)) {
			executeCastling(oldPos, newPos);
		} else {
			super.executeMove(oldPos, newPos);
		}
	}

	// Valid Castle if 1. Rook & King didnt move 2. no Pieces between 3. no check
	// between
	public boolean isValidCastle(Vector2D pos) {
		if (!firstMove)
			return false;

		int[][] aS = this.color.isWhite() ? board.getAttackedSquaresByBlack() : board.getAttackedSquaresByWhite();
		Piece[][] b = board.getBoard();
		boolean isRightSideCastle = pos.getX() - this.position.getX() > 0;
		int rookCol = isRightSideCastle ? this.board.getBoard().length - 1 : 0;
		Vector2D rookPos = new Vector2D(rookCol, this.position.getY()); // on the same row

		boolean inCheck = false;
		boolean pieceOnSquare = false;

		if (isRightSideCastle) {
			int x0 = this.position.getX();
			int x1 = this.position.getX() + 1, x2 = this.position.getX() + 2;
			int y0 = this.position.getY();
			inCheck = aS[y0][x0] > 0 || aS[y0][x1] > 0 || aS[y0][x2] > 0;
			pieceOnSquare = b[y0][x1] != null || b[y0][x2] != null; // BETWEEN K & R
		} else { // left Side
			int x0 = this.position.getX();
			int x1 = this.position.getX() - 1, x2 = this.position.getX() - 2, x3 = this.position.getX() - 3;
			int y0 = this.position.getY();
			inCheck = aS[y0][x0] > 0 || aS[y0][x1] > 0 || aS[y0][x2] > 0 || aS[y0][x3] > 0;
			pieceOnSquare = b[y0][x1] != null || b[y0][x2] != null || b[y0][x3] != null; // BETWEEN K & R
		}

		return ((int) Math.abs((((double) (position.getX() - pos.getX())))) == 2) && board.getPiece(rookPos).firstMove
				&& firstMove && !inCheck && !pieceOnSquare;
	}

	private void executeCastling(Vector2D oldPos, Vector2D newPos) { // TODO test Castling
		boolean isRightSideCastle = newPos.getX() - this.position.getX() > 0;
		Vector2D rookDirection = new Vector2D(isRightSideCastle ? -2 : 3, 0);
		Piece[][] board = this.board.getBoard();

		super.executeMove(oldPos, newPos); // only rook needs to be moved

		int rookCol = isRightSideCastle ? this.board.getBoard().length - 1 : 0;
		Vector2D rookPos = new Vector2D(rookCol, this.position.getY()); // on the same row
		Rook rook = (Rook) this.board.getPiece(rookPos);

		board[rook.getPosition().getY()][rook.getPosition().getX()] = null;
		rook.setPosition(Vector2D.add(rookPos, rookDirection));
		board[rook.getPosition().getY()][rook.getPosition().getX()] = rook;
	}

	@Override
	public void setPosition(Vector2D position) {
		if (this.position == null) {
			this.position = position;
			return;
		}
		this.position = position;

		if (firstMove)
			firstMove = false;
	}

	@Override
	public Piece clone() {
		King king = new King(color, position.getY(), position.getX());
		king.setFirstMove(firstMove);
		return king;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

}
