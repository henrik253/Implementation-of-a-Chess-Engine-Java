package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.Vector2D;

public class Knight extends Piece {

	public final int value = 30;

	protected Vector2D[] attackDirections = { new Vector2D(2, 1), new Vector2D(1, 2), new Vector2D(-2, 1),
			new Vector2D(-1, 2), new Vector2D(-2, -1), new Vector2D(-1, -2), new Vector2D(1, -2), new Vector2D(2, -1) };

	public Knight(ChessPieceColor color, int row, int column) {
		super(ChessPieceName.KNIGHT, color, row, column);
	}

	@Override
	public List<List<Vector2D>> calculateAttackablePositions() {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = this.position.clone();

			possiblePosition.plus(direction);

			if (!outOfBounds(possiblePosition)) {
				movesInDirection.add(possiblePosition.clone());
			}
			moves.add(movesInDirection);
		}
		this.attackableSquares = moves;
		return moves;
	}

	@Override
	public List<List<Vector2D>> calculateMoveablePositions() {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = this.position.clone();

			possiblePosition.plus(direction);

			if (!outOfBounds(possiblePosition)) {
				movesInDirection.add(possiblePosition.clone());
			}
			moves.add(movesInDirection);
		}
		this.attackableSquares = moves;
		return moves;
	}

	@Override
	public Piece clone() {
		return new Knight(color, position.getY(), position.getX());
	}

}
