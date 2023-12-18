package main.model.pieces;

import java.util.LinkedList;
import java.util.List;

import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.Vector2D;

public class Queen extends Piece {

	public final int value = 9;

	private Vector2D[] attackDirections = { new Vector2D(1, 0), new Vector2D(-1, 0), new Vector2D(0, 1),
			new Vector2D(0, -1), new Vector2D(1, 1), new Vector2D(-1, -1), new Vector2D(-1, 1), new Vector2D(1, -1) };

	public Queen(ChessPieceColor color, int row, int column) {
		super(ChessPieceName.QUEEN, color, row, column);

	}

	public Queen(ChessPieceColor color, Vector2D position) {
		this(color, position.getY(), position.getX());
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
	public List<List<Vector2D>> calculateMoveablePositions() {

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
				} else if (piece.getColor() != color) {
					movesInDirection.add(possiblePosition.clone());
					break;
				} else {
					break;
				}
			}
			moves.add(movesInDirection);
		}
		this.attackableSquares = moves;
		return moves;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public Piece clone() {
		Piece p =  new Queen(color, position.getY(), position.getX());
		p.setFirstMove(firstMove);
		return p; 
	}

}
