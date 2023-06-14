package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public class Rook extends Piece {

	private Vector2D[] directions = { new Vector2D(1, 0), new Vector2D(-1, 0), new Vector2D(0, 1),
			new Vector2D(0, -1) };

	public Rook(ChessPieceColor color, int row, int column) {
		super(ChessPieceName.ROOK, color, row, column);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<List<Vector2D>> calculateAttackablePositions(Vector2D position) {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : directions) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = position.clone();

			possiblePosition.add(direction); // currentPosition should not be included

			while (!outOfBounds(possiblePosition)) {
				movesInDirection.add(possiblePosition.clone());
				possiblePosition.add(direction);
			}
			moves.add(movesInDirection);
		}
		this.possiblePositions = moves;
		return moves;
	}
}
