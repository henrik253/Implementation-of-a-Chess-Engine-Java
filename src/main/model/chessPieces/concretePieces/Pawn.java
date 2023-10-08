package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.Move;
import utils.Vector2D;

public class Pawn extends Piece {

	public final int value = 10;

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

		Move lastMove = board.getLastMove();
		if (isDoublePawnMove(lastMove) && isEnemyPawnNextTo(newPosition, lastMove)) { // TODO is this right?
			return true;
		}

		return false;
	}

	private boolean isEnemyPawnNextTo(Vector2D pos, Move lastMove) {
		Piece enemyPiece = board.getPiece(lastMove.getNewPos());

		if (enemyPiece == null || !(enemyPiece instanceof Pawn))
			return false;

		Vector2D right = new Vector2D(1, 0);
		Vector2D left = new Vector2D(-1, 0);

		// check left/right
		return Vector2D.plus(position, right).equals(enemyPiece.getPosition())
				|| (Vector2D.plus(position, left).equals(enemyPiece.getPosition()));
	}

	private boolean isDoublePawnMove(Move move) {
		Piece lastMovedPiece = board.getPiece(move.getNewPos());
		return lastMovedPiece instanceof Pawn && Math.abs(move.getNewPos().getY() - move.getOldPos().getY()) == 2;
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
			transformPawn();
		}
	}

	private void executeEnPassant(Vector2D oldPos, Vector2D newPos) { // UNSECURE !
		super.executeMove(oldPos, newPos);
		Piece attackedPiece = board.getPiece(board.getCurrentMove().getNewPos());

		Vector2D attackedPos = attackedPiece.getPosition();
		Piece[][] board = this.board.getBoard();
		board[attackedPos.getY()][attackedPos.getX()] = null;
		this.board.removePiece(attackedPiece.getPosition());
	}

	private boolean isPawnOnEndline() {
		return position.getY() == endline;
	}

	private void transformPawn() { // auto Queen for the first
		board.replacePiece(this, new Queen(this.color, this.position));
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
	public Piece clone() {
		Pawn pawn = new Pawn(color, position.getY(), position.getX());
		pawn.setFirstMove(firstMove);
		return pawn;
	}
}
