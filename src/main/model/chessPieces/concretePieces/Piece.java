package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public abstract class Piece {

	protected ChessPieceName name;
	protected ChessPieceColor color;

	protected Vector2D position;
	protected List<List<Vector2D>> possiblePositions;
	protected final int length = 8;

	public Piece(ChessPieceName name, ChessPieceColor color, int row, int column) {
		this.name = name;
		this.color = color;
		this.position = new Vector2D(column, row);
		this.possiblePositions = new LinkedList<>();
	}

	public boolean isValidMove(Vector2D newPosition) {
		for (List<Vector2D> positionsInDirection : possiblePositions) {
			for (Vector2D position : positionsInDirection) {
				if (newPosition.equals(position))
					return true;
			}
		}
		return false;
	}

	public abstract List<List<Vector2D>> calculateAttackablePositions(Vector2D position);

	protected boolean outOfBounds(Vector2D position) {
		return position.getX() < 0 || position.getX() >= length || position.getY() < 0 || position.getY() >= length;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}
	
	public String toString() {
		return this.name + " " + this.color;
	}

	public ChessPieceColor getColor() {
		return color;
	}

	public void setColor(ChessPieceColor color) {
		this.color = color;
	}
}
