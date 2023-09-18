package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public class King extends Piece {

	private boolean firstMove = true;

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

	public boolean isValidCastle(Vector2D position) {
		if (!firstMove)
			return false;

		return (int) Math.abs((((double) (position.getX() - this.position.getX())))) == 2;
	}

	// calculateAttackablePositions shows squares where king would be in check ,
	// moveValidation checks that this wont happen
	@Override
	public List<List<Vector2D>> calculateAttackablePositions(Vector2D position) {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = position.clone();

			possiblePosition.plus(direction); // currentPosition should not be included

			if (!outOfBounds(possiblePosition)) {
				Piece piece = board.getPiece(possiblePosition);

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

	public List<Vector2D> calculateMoveablePositions() {
		List<Vector2D> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : attackDirections) {
			Vector2D possiblePosition = position.clone();
			possiblePosition.plus(direction);
			if (!outOfBounds(possiblePosition)) {
				moves.add(possiblePosition.clone());
			}
		}
		return moves;
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
