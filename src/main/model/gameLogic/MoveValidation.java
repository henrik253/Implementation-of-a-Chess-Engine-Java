package main.model.gameLogic;

import main.model.Model;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Knight;
import main.model.chessPieces.concretePieces.Piece;

public class MoveValidation {
	private Model model; 

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

	public void initKingReferences() {
		whiteKing = board.getKing(ChessPieceColor.WHITE);
		blackKing = board.getKing(ChessPieceColor.BLACK);
	}

	public boolean makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece currentPiece = board.getPiece(oldPos);
		boolean movesSucces = false;

		if (!isOnMove(currentPiece))
			return false;

		if (currentPiece.isValidMove(newPos) && noPiecesBetween(currentPiece, newPos)) {
			if (noPiece(newPos)) {
				movesSucces = tryMove(oldPos, newPos);
			} else if (isEnemyPiece(currentPiece, newPos)) {
				if(tryMove(oldPos, newPos)) {
					movesSucces = true; 
					pieceGotTaken(newPos);
				}
				
			} else if (isAllyPiece(currentPiece, newPos)) {
				movesSucces = false;
			}
		}

		if (movesSucces == true)
			toggleOnMove();

		return movesSucces;
	}

	private void pieceGotTaken(Vector2D newPos) {
		model.removePieceFromBoard(newPos);
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
			this.board.calcAttackedSquares(); // inefficient solution!
			System.out.println(this.board.toBoardString());
		}

		return moveSucces;
	}

	private boolean noPiecesBetween(Piece piece, Vector2D newPos) {
		if (piece instanceof Knight) // Knight jumps over pieces
			return true;

		return countFiguresBetween(piece.getPosition(), newPos) == 0;

	}

	private int countFiguresBetween(Vector2D pos1, Vector2D pos2) {
		int x = pos1.getX() - pos2.getX();
		int y = pos1.getY() - pos2.getY();
		int length = Math.max(Math.abs(x), Math.abs(y));

		int countX = pos1.getX();
		int countY = pos1.getY();

		int incX = (x > 0 ? 1 : -1);
		int incY = (y > 0 ? 1 : -1);
		incX = x != 0 ? incX : 0;
		incY = y != 0 ? incY : 0;

		int count = 0;

		for (int step = 0; step < length - 1; step++) {
			countX -= incX;
			countY -= incY;
			if (this.board.getBoard()[countY][countX] != null) {
				count++;
			}
		}
		return count;
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

	private boolean isPiece(Vector2D pos) {
		return !noPiece(pos);
	}

	private boolean isEnemyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() != piece.getColor();
	}

	private boolean isAllyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() == piece.getColor();
	}

	public boolean enemyInCheck() {
		int[][] attackedSquares = onMove.isWhite() ? board.getAttackedSquaresByWhite()
				: board.getAttackedSquaresByBlack();
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
		initKingReferences();
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
