package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public class Queen extends Piece {

	private Vector2D[] attackDirections = { new Vector2D(1, 0), new Vector2D(-1, 0), new Vector2D(0, 1),
			new Vector2D(0, -1), new Vector2D(1, 1), new Vector2D(-1, -1), new Vector2D(-1, 1), new Vector2D(1, -1) };

	public Queen(ChessPieceColor color, int row, int column) {
		super(ChessPieceName.QUEEN, color, row, column);

	}

	@Override
	public List<List<Vector2D>> calculateAttackablePositions() { // TODO param position useless

		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = this.position.clone();

			possiblePosition.plus(direction); // currentPosition should not be included

			while (!outOfBounds(possiblePosition)) {
				Piece piece = board.getPiece(possiblePosition);
				if (piece == null) {
					movesInDirection.add(possiblePosition.clone()); // add to linked list
					possiblePosition.plus(direction); // addition
				} else {
					movesInDirection.add(possiblePosition.clone());
					break;
				}
			}
			moves.add(movesInDirection);
		}
		this.attackableSquares = moves;
		return moves;
	}

	@Override
	public Piece clone() {
		return new Queen(color, position.getY(), position.getX());
	}

}
