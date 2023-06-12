package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public abstract class Piece {

	protected ChessPieceName name;
	protected ChessPieceColor color;

	protected Vector2D position;
	protected List<Vector2D> possiblePositions;
	protected final int length = 8;

	public Piece(ChessPieceName name, ChessPieceColor color, int column, int row) {
		this.name = name;
		this.color = color;
		this.position = new Vector2D(column, row);
		this.possiblePositions = new LinkedList<>();
	}

	public boolean isValidMove(Vector2D newPosition) {
		for (Vector2D position : possiblePositions) {
			if (newPosition.equals(position))
				return true;
		}
		return false;
	}

	protected abstract List<Vector2D> calculatePossiblePositions(Vector2D position);

	protected boolean outOfBounds(Vector2D position) {
		return position.getX() < 0 || position.getX() >= length || position.getY() < 0 || position.getY() >= length;
	}
}
