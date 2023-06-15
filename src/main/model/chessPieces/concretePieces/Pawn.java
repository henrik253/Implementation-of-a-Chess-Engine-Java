package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public class Pawn extends Piece {

	private Vector2D[] directions = { new Vector2D(0, -1) };
	private Vector2D[] attackDirections = { new Vector2D(0, -1), new Vector2D(-1, -1) };
	private boolean firstMove = true;

	public Pawn(ChessPieceColor color, int row, int column) {
		super(ChessPieceName.PAWN, color, row, column);

		if (color == ChessPieceColor.BLACK) {
			directions[0] = new Vector2D(0, 1);
			attackDirections[0] = new Vector2D(1, 1);
			attackDirections[1] = new Vector2D(-1, 1);
		}

	}

	@Override
	public boolean isValidMove(Vector2D newPosition) {

		return validPawnMove(newPosition) || enPassant(newPosition);
	}

	private boolean enPassant(Vector2D newPosition) {
		return false;
	}

	private boolean validPawnMove(Vector2D newPosition) {
		if (position.getX() != newPosition.getX())
			return false;

		if (Vector2D.add(position, directions[0]).equals(newPosition))
			return true;

		if (firstMove) {
			if (Vector2D.add(Vector2D.add(position, directions[0]), directions[0]).equals(newPosition)) {
				firstMove = !firstMove;
				return true;
			}
		}
		return false;
	}

	@Override
	public List<List<Vector2D>> calculateAttackablePositions(Vector2D position) {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D attackDirection : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = position.clone();
			possiblePosition.add(attackDirection);

			if (!outOfBounds(possiblePosition)) {
				movesInDirection.add(possiblePosition.clone());
			}

			moves.add(movesInDirection);
		}
		this.attackableSquares = moves;
		return moves;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

}
