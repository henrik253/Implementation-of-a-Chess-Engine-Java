package main.model.pieces;

import main.model.gameLogic.BoardRepresentation;
import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.Vector2D;

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

	protected boolean firstMove = true;

	public int value;

	public Piece(ChessPieceName name, ChessPieceColor color, int row, int column) {
		this.name = name;
		this.color = color;
		this.position = new Vector2D(column, row);
		this.attackableSquares = new LinkedList<>();
	}

	public boolean isValidMove(Vector2D newPosition) {
		for (List<Vector2D> positionsInDirection : this.calculateMoveablePositions()) {
			for (Vector2D position : positionsInDirection) {
				if (newPosition.equals(position))
					return true;
			}
		}

		return false;
	}

	// an attack is always a move, but a move isnt always a attack. e.g. Pawn
	// movement
	public List<List<Vector2D>> calculateMoveablePositions() {
		return calculateAttackablePositions();
	}

	public void executeMove(Vector2D oldPos, Vector2D newPos) {
		if (firstMove) {
			firstMove = false;
			board.getCurrentMove().setMovedPieceFirstMove(true);
		}
		Piece[][] board = this.board.getBoard();
		board[oldPos.getY()][oldPos.getX()] = null;
		board[newPos.getY()][newPos.getX()] = this;
		this.setPosition(newPos);
	}

	public abstract List<List<Vector2D>> calculateAttackablePositions();
	
	public List<List<Vector2D>> calculateAttackableAndMoveablePositions() {
		return calculateAttackablePositions();
	}
	
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

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

	public int getValue() {
		return value;
	}

}
