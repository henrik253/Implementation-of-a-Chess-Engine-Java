package main.model.gameLogic;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;

public class MoveValidation {

	private ChessPieceColor onMove;
	private BoardRepresentation board;

	private Piece whiteKing;
	private Piece blackKing;

	public MoveValidation(ChessPieceColor startingColor) {
		this.onMove = startingColor;
	}

	public MoveValidation() {
		this(ChessPieceColor.WHITE);
	}

	public void init() {
		whiteKing = board.getKing(ChessPieceColor.WHITE);
		blackKing = board.getKing(ChessPieceColor.BLACK);
	}

	public boolean makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece currentPiece = board.getPiece(oldPos);
		boolean movesSucces = false;

		if (!isOnMove(currentPiece))
			return false;

		if (currentPiece.isValidMove(newPos)) {

			if (noPiece(newPos)) {
				movesSucces = tryMove(oldPos, newPos);
			} else if (enemyPiece(currentPiece, newPos)) {
				movesSucces = tryMove(oldPos, newPos);
			} else if (allyPiece(currentPiece, newPos)) {
				movesSucces = false;
			}
		}

		if (movesSucces == true)
			toggleOnMove();

		return movesSucces;
	}

	private boolean isOnMove(Piece piece) {
		return onMove == piece.getColor();
	}

	private void toggleOnMove() {
		onMove = onMove.isWhite() ? ChessPieceColor.BLACK : ChessPieceColor.WHITE;
	}

	private boolean noPiece(Vector2D pos) {
		return board.getPiece(pos) == null;
	}

	private boolean enemyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() != piece.getColor();
	}

	private boolean allyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() == piece.getColor();
	}

	private boolean tryMove(Vector2D oldPos, Vector2D newPos) {
		BoardRepresentation boardClone = this.board.clone();
		boardClone.makeMove(oldPos, newPos);
		boardClone.calcAttackedSquares();
		int[][] attackedSqaures;
		boolean moveSucces = true;

		if (onMove.isWhite()) {
			attackedSqaures = boardClone.getAttackedSquaresByBlack();
			if (attackedSqaures[whiteKing.getPosition().getY()][whiteKing.getPosition().getX()] > 0) {
				System.out.println("WHITE IN CHECK");
				moveSucces = false;
			}
		} else {
			attackedSqaures = boardClone.getAttackedSquaresByWhite();
			if (attackedSqaures[blackKing.getPosition().getY()][blackKing.getPosition().getX()] > 0) {
				System.out.println("BLACK IN CHECK!");
				moveSucces = false;
			}
		}
		
		if (moveSucces) {
			this.board.setBoard(boardClone.getBoard());
			this.board.calcAttackedSquares();
			System.out.println("Check : " + enemyInCheck());
			
		}

		return moveSucces;
	}
	
	public boolean enemyInCheck() {
		int[][] attackedSquares = onMove.isWhite() ?board.getAttackedSquaresByWhite(): board.getAttackedSquaresByBlack();
		Vector2D pos = onMove.isWhite() ? blackKing.getPosition() : whiteKing.getPosition();
		return attackedSquares[pos.getY()][pos.getX()] > 0;
	}

	public ChessPieceColor getOnMove() {
		return onMove;
	}

	public void setOnMove(ChessPieceColor onMove) {
		this.onMove = onMove;
	}

	public BoardRepresentation getBoard() {
		return board;
	}

	public void setBoard(BoardRepresentation board) {
		this.board = board;
		init();
	}

}
