package main.model.chessPieces.concretePieces;

import java.util.LinkedList;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;

public class Pawn extends Piece {

	private Vector2D[] directions = { new Vector2D(0, 1) };
	private boolean firstMove = true;

	public Pawn(ChessPieceColor color, int column, int row) {
		super(ChessPieceName.PAWN, color, column, row);

		if (color == ChessPieceColor.BLACK) {
			directions[0] = new Vector2D(0, -1);
		}
	}

	@Override
	protected List<Vector2D> calculatePossiblePositions(Vector2D position) {
		List<Vector2D> moves = new LinkedList<>();

		if (outOfBounds(position))
			return moves;

		Vector2D direction = directions[0]; 
		
		Vector2D possiblePosition = position.clone();
		
		possiblePosition.add(direction);
		moves.add(possiblePosition);

		if (firstMove) {
			possiblePosition = possiblePosition.clone();
			possiblePosition.add(direction);
			moves.add(possiblePosition);
		}

		return moves;
	}
	
	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

}
