package main.model.pieces;

import java.util.LinkedList;
import java.util.List;

import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.Move;
import utils.Vector2D;

public class King extends Piece {

	public final int value = 300;

	private final Vector2D[] attackDirections = { new Vector2D(1, 0), new Vector2D(-1, 0), new Vector2D(0, 1),
			new Vector2D(0, -1), new Vector2D(1, 1), new Vector2D(-1, -1), new Vector2D(-1, 1), new Vector2D(1, -1) };

	private final Vector2D[] castlingDirections = { new Vector2D(2, 0), new Vector2D(-2, 0) };

	public King(ChessPieceColor color, int row, int column) {
		super(ChessPieceName.KING, color, row, column);

	}

	@Override
	public boolean isValidMove(Vector2D position) {
		boolean validMovement = super.isValidMove(position);

		return validMovement || isValidCastle(position);
	}

	// calculateAttackablePositions returns squares where king would be in check ,
	// moveValidation checks that this wont happen, so we dont have to do this in
	// this class
	// TODO calculateAttackablePositions or moveablePos the castling
	@Override
	public List<List<Vector2D>> calculateAttackablePositions() {
		List<List<Vector2D>> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		for (Vector2D direction : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = position.clone();

			possiblePosition.plus(direction); // currentPosition should not be included

			if (!outOfBounds(possiblePosition)) {
				Piece piece = board.getPiece(possiblePosition);

				// pieces were king would be in check will be shown
//				if (piece != null && piece.getColor() == color) // cant step on ally piece
//					continue;

				movesInDirection.add(possiblePosition.clone()); // add to linked list
				possiblePosition.plus(direction); // addition

			}
			moves.add(movesInDirection);
		}
		this.attackableSquares = moves;

		return moves;
	}

	@Override
	public List<List<Vector2D>> calculateMoveablePositions() {
		List<List<Vector2D>> moves = new LinkedList<>();
		int[][] attackedSquaresEnemy = board.calcAttackedSquaresBy(color.getOpponentColor());

		if (outOfBounds(position)) // base case
			return moves;

		for (Vector2D direction : attackDirections) {
			List<Vector2D> movesInDirection = new LinkedList<>();
			Vector2D possiblePosition = position.clone();

			possiblePosition.plus(direction); // currentPosition should not be included

			if (!outOfBounds(possiblePosition)) {
				Piece piece = board.getPiece(possiblePosition);

				// pieces were king would be in check will be shown
				if ((piece != null && piece.getColor() == color)
						|| attackedSquaresEnemy[possiblePosition.getY()][possiblePosition.getX()] > 0) // cant step on
																										// ally piece
					continue;

				movesInDirection.add(possiblePosition.clone()); // add to linked list
				possiblePosition.plus(direction); // addition

			}
			moves.add(movesInDirection);
		}

		List<Vector2D> castlingMoves = new LinkedList<>();
		Vector2D castlingPos1 = Vector2D.plus(position, castlingDirections[0]);
		Vector2D castlingPos2 = Vector2D.plus(position, castlingDirections[1]);

		if (!outOfBounds(castlingPos1) && isValidCastle(castlingPos1)) {
			castlingMoves.add(castlingPos1);
		}

		if (!outOfBounds(castlingPos1) && isValidCastle(castlingPos2)) {
			castlingMoves.add(castlingPos2);
		}

		moves.add(castlingMoves);

		this.attackableSquares = moves; // TODO correct?
		return moves;
	}

	@Override
	public void executeMove(Vector2D oldPos, Vector2D newPos) {
		if (isValidCastle(newPos)) {
			executeCastling(oldPos, newPos);
		} else {
			super.executeMove(oldPos, newPos);
		}
	}

	// Valid Castle if 1. Rook & King didnt move 2. no Pieces between 3. no check
	// between
	public boolean isValidCastle(Vector2D pos) {
		if (!firstMove)
			return false;
		try {
			int[][] aS = board.calcAttackedSquaresBy(color.getOpponentColor());
			Piece[][] b = board.getBoard();
			boolean kingSideCastle = pos.getX() > this.getPosition().getX();
			int rookCol = kingSideCastle ? this.board.getBoard().length - 1 : 0; // either left corner or right corner
			Vector2D rookPos = new Vector2D(rookCol, this.position.getY()); // on the same row
			Piece rook = board.getPiece(rookPos);

			if (rook == null)
				return false;

			boolean inCheck = false;
			boolean pieceOnSquare = false;
	
			if (kingSideCastle) {
				int x0 = this.position.getX();
				int x1 = this.position.getX() + 1, x2 = this.position.getX() + 2;
				int y0 = this.position.getY();
				inCheck = aS[y0][x0] > 0 || aS[y0][x1] > 0 || aS[y0][x2] > 0;
				pieceOnSquare = b[y0][x1] != null || b[y0][x2] != null; // BETWEEN K & R
			} else { // left Side
				int x0 = this.position.getX();
				int x1 = this.position.getX() - 1, x2 = this.position.getX() - 2, x3 = this.position.getX() - 3;
				int y0 = this.position.getY();
	
			
				inCheck = aS[y0][x0] > 0 || aS[y0][x1] > 0 || aS[y0][x2] > 0; // only the squares where king would move through in check
				pieceOnSquare = b[y0][x1] != null || b[y0][x2] != null || b[y0][x3] != null; // BETWEEN K & R
			}
			boolean validMove = ((int) Math.abs((((double) (position.getX() - pos.getX())))) == 2); 

			
			return validMove && rook.firstMove && firstMove
					&& !inCheck && !pieceOnSquare;
			
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Castling Error in King Class");
		}
		return false;
	}

	private void executeCastling(Vector2D oldPos, Vector2D newPos) { // TODO test Castling
		boolean kingSideCastle = newPos.getX() > this.getPosition().getX();
		Vector2D rookDirection = new Vector2D(kingSideCastle ? -2 : 3, 0);
		
		super.executeMove(oldPos, newPos);
	
		
		Piece[][] board = this.board.getBoard();
		// only rook needs to be moved
		int rookCol = kingSideCastle ? this.board.getBoard().length - 1 : 0;
		Vector2D rookPos = new Vector2D(rookCol, this.position.getY()); // on the same row
		Rook rook = (Rook) this.board.getPiece(rookPos);

		board[rook.getPosition().getY()][rook.getPosition().getX()] = null;
		Vector2D newRookPos = Vector2D.plus(rookPos, rookDirection);
		rook.setPosition(newRookPos);
		board[newRookPos.getY()][newRookPos.getX()] = rook;
		
		Move currentMove = this.board.getCurrentMove();
		currentMove.setCastlingMove(true);
		currentMove.setRook(rook);
		currentMove.setOldRookPos(rookPos);
		currentMove.setNewRookPos(newRookPos);
	}

	@Override
	public void setPosition(Vector2D position) {
		if (this.position == null) {
			this.position = position;
			return;
		}
		this.position = position;

		if (firstMove)
			firstMove = false;
	}

	@Override
	public Piece clone() {
		King king = new King(color, position.getY(), position.getX());
		king.setFirstMove(firstMove);
		return king;
	}

	@Override
	public int getValue() {
		return value;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

}
