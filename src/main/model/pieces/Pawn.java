package main.model.pieces;

import java.util.LinkedList;
import java.util.List;

import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.Move;
import utils.Vector2D;

public class Pawn extends Piece {

	public final int value = 1;

	private static final int WHITE_ENDLINE = 0;
	private static final int BLACK_ENDLINE = 7;

	private Vector2D[] directions = { new Vector2D(0, -1) };
	private Vector2D[] attackDirections = { new Vector2D(1, -1), new Vector2D(-1, -1) };
	private int endline = WHITE_ENDLINE;

	public Pawn(ChessPieceColor color, int row, int column) {
		super(ChessPieceName.PAWN, color, row, column);

		if (color == ChessPieceColor.BLACK) {
			directions[0] = new Vector2D(0, 1);
			attackDirections[0] = new Vector2D(1, 1);
			attackDirections[1] = new Vector2D(-1, 1);
			endline = BLACK_ENDLINE;
		}

	}

	@Override
	public boolean isValidMove(Vector2D newPosition) {
		return validPawnMove(newPosition) || isValidAttack(newPosition) || enPassant(newPosition);
	}

	private boolean enPassant(Vector2D newPosition) { // TODO how is enPassant executed?

		if (board.getMoveHistory().size() < 2) // At least one Move has been executed and one current move
			return false;

		Move lastMove = board.getLastMove(); // is correct
		if (isDoublePawnMove(lastMove) && isEnemyPawnNextTo(newPosition, lastMove)
				&& isAttackOnSameColumnAsDoubleMovedPawn(newPosition, lastMove)) { // TODO is this right?
			return true;
		}

		return false;
	}

	private boolean isEnemyPawnNextTo(Vector2D movingPos, Move lastMove) {
		Piece enemyPiece = board.getPiece(lastMove.to());

		if (enemyPiece == null || !(enemyPiece instanceof Pawn))
			return false;

		Vector2D right = new Vector2D(1, 0);
		Vector2D left = new Vector2D(-1, 0);

		// check left/right
		return (Vector2D.plus(position, right).equals(enemyPiece.getPosition())
				|| Vector2D.plus(position, left).equals(enemyPiece.getPosition()));
	}

	private boolean isDoublePawnMove(Move move) {
		Piece lastMovedPiece = board.getPiece(move.to());
		return lastMovedPiece instanceof Pawn && Math.abs(move.to().getY() - move.from().getY()) == 2;
	}

	private boolean isAttackOnSameColumnAsDoubleMovedPawn(Vector2D movingPosition, Move lastMove) {
		Piece doubleMovedPawn = board.getPiece(lastMove.to());
		return doubleMovedPawn.getPosition().getX() == movingPosition.getX();
	}

	private boolean validPawnMove(Vector2D newPosition) {
		if (position.getX() != newPosition.getX() || board.isPieceOn(newPosition))
			return false;

		if (Vector2D.plus(position, directions[0]).equals(newPosition))
			return true;

		if (firstMove) {
			if (Vector2D.plus(Vector2D.plus(position, directions[0]), directions[0]).equals(newPosition)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<List<Vector2D>> calculateAttackablePositions() {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D attackDirection : attackDirections) {

			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = this.position.clone();
			possiblePosition.plus(attackDirection);

			if (!outOfBounds(possiblePosition)
					&& (board.isEnemyPieceOn(possiblePosition, color) || enPassant(possiblePosition))) {
				movesInDirection.add(possiblePosition.clone());
			}

			moves.add(movesInDirection);
		}
		this.attackableSquares = moves;
		return moves;
	}

	@Override
	public List<List<Vector2D>> calculateAttackableAndMoveablePositions() {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D attackDirection : attackDirections) {

			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = this.position.clone();
			possiblePosition.plus(attackDirection);

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
		// Pawn is the only piece that has a different attack then move!
		List<List<Vector2D>> moves = new LinkedList<>();
		Vector2D direction = directions[0];
		List<Vector2D> movesInDirection = new LinkedList<>();
		Vector2D possiblePosition = this.position.clone();
		for (int i = 0; i < (firstMove ? 2 : 1); i++) {
			possiblePosition.plus(direction);

			if (!outOfBounds(possiblePosition)) {
				if (board.isNoPieceOn(possiblePosition))
					movesInDirection.add(possiblePosition.clone());
				else
					break;
			}
		}
		moves.add(movesInDirection);

		moves.addAll(calculateAttackablePositions());

		return moves;
	}

	public boolean isValidAttack(Vector2D position) {

		for (List<Vector2D> movesInDirection : this.calculateAttackablePositions()) {
			for (Vector2D move : movesInDirection) {
				if (move.equals(position))
					return true;
			}
		}
		return false;
	}

	@Override
	public void executeMove(Vector2D oldPos, Vector2D newPos) {

		if (enPassant(newPos)) {
			executeEnPassant(oldPos, newPos);
		} else
			super.executeMove(oldPos, newPos); // normal behaviour !

		if (isPawnOnEndline()) {
			promotePawn();
		}
	}

	public void executeMove(Vector2D oldPos, Vector2D newPos, ChessPieceName promoting) {

		if (enPassant(newPos)) {
			executeEnPassant(oldPos, newPos);
		} else
			super.executeMove(oldPos, newPos); // normal behaviour !

		if (isPawnOnEndline()) {
			promotePawn(promoting);
		}
	}

	private void executeEnPassant(Vector2D oldPos, Vector2D newPos) { // UNSECURE !
		super.executeMove(oldPos, newPos);
		Piece doubleMovedPawn = board.getPiece(board.getLastMove().to()); // the double moved Pawn

		Piece[][] board = this.board.getBoard();
		board[doubleMovedPawn.getPosition().getY()][doubleMovedPawn.getPosition().getX()] = null;
		this.board.removePiece(doubleMovedPawn.getPosition());
	}

	private boolean isPawnOnEndline() {
		return position.getY() == endline;
	}

	private void promotePawn() { // auto Queen for the first
		board.promotePiece(this, new Queen(this.color, this.position));
	}

	private void promotePawn(ChessPieceName promotingPiece) { // auto Queen for the first
		switch (promotingPiece) {
		case KNIGHT:
			board.promotePiece(this, new Knight(this.color, this.position));
			break;
		case QUEEN:
			board.promotePiece(this, new Queen(this.color, this.position));
			break;
		case BISHOP:
			board.promotePiece(this, new Bishop(this.color, this.position));
			break;
		case ROOK:
			board.promotePiece(this, new Rook(this.color, this.position));
			break;
		}

	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

	@Override
	public void setPosition(Vector2D position) {
		if (this.position == null) {
			this.position = position;
			return;
		}
		this.position = position;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public Piece clone() {
		Pawn pawn = new Pawn(color, position.getY(), position.getX());
		pawn.setFirstMove(firstMove);
		return pawn;
	}
}
