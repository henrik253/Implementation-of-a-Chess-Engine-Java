package main.model.gameLogic;

import java.util.List;

import main.model.pieces.King;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Vector2D;

public class Check {

	public static boolean kingInCheckPieceMoves(BoardRepresentation board, Vector2D oldPos, Vector2D newPos) {
		ChessPieceColor pieceColor = board.getPiece(oldPos).getColor();

		board.makeMove(oldPos, newPos);
		final int[][] attackedSquares = board.calcAttackedSquaresBy(pieceColor.getOpponentColor());
		King k = board.getKing(pieceColor);

		// checks if own side would be in check if it does the move
		boolean check = attackedSquares[k.getPosition().getY()][k.getPosition().getX()] > 0;

		board.undoLastMove();
		return check;
	}

	// Search for a check with the king of the desired color
	public static Piece getCheckingPiece(BoardRepresentation board, ChessPieceColor mated) {
		King k = board.getKing(mated);
		Piece checkingPiece = null;
		Piece[][] pieceBoard = board.getBoard();
		for (int i = 0; i < pieceBoard.length; i++) {
			for (int j = 0; j < pieceBoard.length; j++) {
				Piece p = pieceBoard[i][j];
				if (p != null && !p.getColor().equals(mated)) {
					List<List<Vector2D>> allMovesOfPiece = pieceBoard[i][j].calculateAttackablePositions();
					for (List<Vector2D> movesInDir : allMovesOfPiece) {
						for (Vector2D move : movesInDir) {
							if (k.getPosition().equals(move)) {
								checkingPiece = p;
								return checkingPiece;
							}
						}
					}
				}
			}
		}
		
		return null;
	}

	public static boolean isMate(BoardRepresentation board, ChessPieceColor checkedSide) {
		return isMate(board, checkedSide, getCheckingPiece(board,checkedSide));
	}

	public static boolean isMate(BoardRepresentation board, ChessPieceColor mated, Piece checkingPiece) {
		if(kingInCheck(board,mated)){
			return !kingCanMove(board, mated) && !checkCanBeStopped(board, mated, checkingPiece);
		}
		return false;
	}
	
	public static boolean kingInCheck(BoardRepresentation board,ChessPieceColor kingSide){
		Vector2D kingPos = board.getKing(kingSide).getPosition(); 
		return board.getAttackedSquares(kingSide.getOpponentColor())[kingPos.getY()][kingPos.getX()] > 0;
	}

	public static boolean kingCanMove(BoardRepresentation board, ChessPieceColor mated) {
		King k = board.getKing(mated);
		Vector2D kPos = k.getPosition();
		board.getBoard()[kPos.getY()][kPos.getX()] = null; // king could move on squares behind him that arent checked
		int[][] attackedSquares = board.calcAttackedSquaresBy(mated.getOpponentColor());
		board.getBoard()[kPos.getY()][kPos.getX()] = k;
		List<List<Vector2D>> moves = k.getMoveablePositions(); // shows squar

		for (List<Vector2D> movesInDir : moves) {
			for (Vector2D move : movesInDir) {
				if (attackedSquares[move.getY()][move.getX()] == 0)
					return true;
			}
		}
		return false;
	}

	public static boolean checkCanBeStopped(BoardRepresentation board, ChessPieceColor mated, Piece checkingPiece) {
		List<Piece> pieces = board.getPieces(mated);
		King k = board.getKing(mated);
		List<List<Vector2D>> attackingSquares = checkingPiece.calculateAttackablePositions();
		int[][] attackedSquares = board.getAttackedSquares(mated.getOpponentColor());
		
		
		//Double check cant be stopped, king has to move away
		if (attackedSquares[k.getPosition().getY()][k.getPosition().getX()] >= 2) {
			return false;
		}

		for (List<Vector2D> movesInDirection : attackingSquares) {
			if (movesInDirection.contains(k.getPosition())) { // direction the piece is checking the king
				for (Piece capturingPiece : pieces) {
					List<Vector2D> movesOfCapturingPiece = capturingPiece.calculateMoveablePositions().stream()
							.flatMap(list -> list.stream()).toList();
					// Blocking the piece(s) delivering check || Capturing the checking piece.
					for (Vector2D capturingPieceMove : movesOfCapturingPiece) {

						if (canBeBlocked(movesInDirection, capturingPieceMove, capturingPiece, k)
								|| checkingPiece.getPosition().equals(capturingPieceMove)) {
							
							// check if the blocking piece is pinned if it is we continue the search
							board.getBoard()[capturingPiece.getPosition().getY()][capturingPiece.getPosition().getX()] = null;
							board.getBoard()[capturingPieceMove.getY()][capturingPieceMove.getX()] = capturingPiece;
							int[][] attackedSquaresWithoutPiece = board
									.calcAttackedSquaresBy(capturingPiece.getColor().getOpponentColor());

							if (!(capturingPiece instanceof King)
									&& attackedSquaresWithoutPiece[k.getPosition().getY()][k.getPosition().getX()] > 0) {
								// if we get in here, the king is still in check even if the piece moved to desiredPos
								// so the piece was pinned
								board.getBoard()[capturingPiece.getPosition().getY()][capturingPiece.getPosition().getX()] = capturingPiece;
								board.getBoard()[capturingPieceMove.getY()][capturingPieceMove.getX()] = capturingPiece = null;
								continue;
							}
							board.getBoard()[capturingPiece.getPosition().getY()][capturingPiece.getPosition().getX()] = capturingPiece;
							board.getBoard()[capturingPieceMove.getY()][capturingPieceMove.getX()] = capturingPiece = null;
							
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static boolean canBeBlocked(List<Vector2D> movesInDirection, Vector2D enemyPieceMove, Piece enemyPiece,
			King enemyKing) {

		if (enemyPieceMove.equals(enemyKing.getPosition())) // kings pos in not included in can be blocked!
			return false;

		return movesInDirection.contains(enemyPieceMove) && enemyPiece != enemyKing;
	}

	// TODO

}
