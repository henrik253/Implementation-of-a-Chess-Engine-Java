package main.model.chessPieces.concretePieces;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;
import main.model.gameLogic.BoardRepresentation;

import java.util.LinkedList;
import java.util.List;

public abstract class Piece {

	protected BoardRepresentation board;

	protected ChessPieceName name;
	protected ChessPieceColor color;

	protected Vector2D position;
	protected List<List<Vector2D>> attackableSquares;
	protected final int length = 8;

	protected Vector2D[] attackDirections;

	public Piece(ChessPieceName name, ChessPieceColor color, int row, int column) {
		this.name = name;
		this.color = color;
		this.position = new Vector2D(column, row);
		this.attackableSquares = new LinkedList<>();
	}

	public boolean isValidMove(Vector2D newPosition) {
		for (List<Vector2D> positionsInDirection : attackableSquares) {
			for (Vector2D position : positionsInDirection) {
				if (newPosition.equals(position))
					return true;
			}
		}

		return false;
	}

	public void executeMove(Vector2D oldPos, Vector2D newPos) {
		Piece[][] board = this.board.getBoard();
		board[oldPos.getY()][oldPos.getX()] = null;
		board[newPos.getY()][newPos.getX()] = this;

		this.setPosition(newPos);
	}

	public abstract List<List<Vector2D>> calculateAttackablePositions(Vector2D position);

	protected boolean outOfBounds(Vector2D position) {
		return position.getX() < 0 || position.getX() >= length || position.getY() < 0 || position.getY() >= length;
	}

	public boolean noPiece() {
		return false;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public List<List<Vector2D>> getAttackableSquares() {
		return attackableSquares;
	}

	public List<List<Vector2D>> getMoveablePositions() {
		return this.getAttackableSquares();
	}

	public void setAttackableSquares(List<List<Vector2D>> attackableSquares) {
		this.attackableSquares = attackableSquares;
	}

	public Vector2D[] getAttackDirections() {
		return attackDirections;
	}

	public void setAttackDirections(Vector2D[] attackDirections) {
		this.attackDirections = attackDirections;
	}

	public String toString() {
		return "( " + this.name + " " + this.color + " " + position.toString() + " )";
	}

	public ChessPieceColor getColor() {
		return color;
	}

	public void setColor(ChessPieceColor color) {
		this.color = color;
	}

	public abstract Piece clone();

	public ChessPieceName getName() {
		return this.name;
	}

	public BoardRepresentation getBoard() {
		return board;
	}

	public void setBoard(BoardRepresentation board) {
		this.board = board;
	}

}
