package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public class Queen extends Piece {

	private Vector2D[] directions = { new Vector2D(1, 0), new Vector2D(-1, 0), new Vector2D(0, 1), new Vector2D(0, -1),
			new Vector2D(1, 1), new Vector2D(-1, -1), new Vector2D(-1, 1), new Vector2D(1, -1) };

	public Queen(ChessPieceColor color, int column, int row) {
		super(ChessPieceName.QUEEN, color, column, row);
		this.possiblePositions = calculatePossiblePositions(this.position);
	}

	@Override
	protected List<Vector2D> calculatePossiblePositions(Vector2D position) {

		List<Vector2D> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : directions) {

			Vector2D possiblePosition = position.clone();

			possiblePosition.add(direction); // currentPosition should not be included

			while (!outOfBounds(possiblePosition)) {
				moves.add(possiblePosition.clone());
				possiblePosition.add(direction);
			}
		}

		return moves;
	}

}
