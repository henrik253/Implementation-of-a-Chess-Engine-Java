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
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValidMove(Vector2D position) {
		boolean validMovement = super.isValidMove(position);

		return validMovement;
	}

	public boolean isValidCastle(Vector2D position) {
		if (!firstMove)
			return false;

		return (int) Math.abs((((double) (position.getX() - this.position.getX())))) == 2;
	}

	@Override
	public List<List<Vector2D>> calculateAttackablePositions(Vector2D position) {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = position.clone();

			possiblePosition.add(direction); // currentPosition should not be included

			if (!outOfBounds(possiblePosition)) {
				movesInDirection.add(possiblePosition.clone());
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
			possiblePosition.add(direction);
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
