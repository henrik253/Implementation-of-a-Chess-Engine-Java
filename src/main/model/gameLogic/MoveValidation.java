package main.model.gameLogic;

import main.model.Model;
import main.model.pieces.*;
import utils.ChessPieceColor;
import utils.GameOverReason;
import utils.GameState;
import utils.InCheck;
import utils.State;
import utils.Vector2D;

import java.util.LinkedList;
import java.util.List;

public class MoveValidation {
	private Model model;

	private ChessPieceColor onMove;
	private BoardRepresentation startingBoard; 
	private BoardRepresentation board;

	public MoveValidation(ChessPieceColor startingColor) {
		this.onMove = startingColor;
	}

	public MoveValidation() {
		this(ChessPieceColor.WHITE);
	}

	public void reStart() {
		this.onMove = ChessPieceColor.WHITE;
	}

	public boolean makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece movedPiece = board.getPiece(oldPos);
		
		if (isNoValidMove(oldPos, newPos)) {
			return false;
		}
		
		board.makeMove(oldPos, newPos);
		board.calcAttackedSquaresBy(movedPiece.getColor());

		boolean inCheck = enemyInCheck();

		if (inCheck) {
			testCheckMate(movedPiece);
		} else if (enemyInRemi()) {
			remis();
		}

		if (inCheck) {
			State.inCheck = InCheck.ON;
			State.lastInCheck = onMove.getOpponentColor();
		} else {
			State.inCheck = InCheck.OFF;
		}

		setNextPlayerOnMove();

		return true;
	}

	private boolean isNoValidMove(Vector2D oldPos, Vector2D newPos) {
		Piece movedPiece = board.getPiece(oldPos);
		if (movedPiece == null)
			return false;
		return !playerOnMove(movedPiece) || !movedPiece.isValidMove(newPos) || isAllyPiece(movedPiece, newPos)
				|| kingInCheckIfPieceMoves(oldPos, newPos);
	}

	private void testCheckMate(Piece piece) {
		if (Check.isMate(board, piece.getColor().getOpponentColor(), piece)) {
			initiateCheckMate(); 
		}
	}

	private void initiateCheckMate() {
		State.gameState = GameState.GAME_OVER;
		State.gameOverReason = onMove.isWhite() ? GameOverReason.WHITE_WON : GameOverReason.BLACK_WON;
		System.out.println("CHECK MATE ! ");
	}

	public boolean kingInCheckIfPieceMoves(Vector2D oldPos, Vector2D newPos) {
		ChessPieceColor pieceColor = board.getPiece(oldPos).getColor();

		board.makeMove(oldPos, newPos);

		final int[][] attackedSquaresByEnemy = board.calcAttackedSquaresBy(pieceColor.getOpponentColor());

		King king = board.getKing(pieceColor);

		// checks if own side would be in check if it does the move
		boolean check = inCheck(attackedSquaresByEnemy, king);

		board.undoLastMove();
		board.calcAttackedSquaresBy(pieceColor.getOpponentColor()); // neccessary if we want to proof if we check the
																	// enemy side

		return check;
	}

	private boolean inCheck(int[][] attackedSquares, King king) {// TODO Game Logic???
		return attackedSquares[king.getPosition().getY()][king.getPosition().getX()] > 0;
	}

	private boolean enemyInRemi() { // Fehleranfällig?
		return Remis.isRemis(board, board.getMoveHistory(), onMove.getOpponentColor(), startingBoard);
	}

	private void remis() {
		State.gameState = GameState.GAME_OVER;
		State.gameOverReason = GameOverReason.DRAW;
	}

	private boolean playerOnMove(Piece piece) {
		return onMove == piece.getColor();
	}

	private void setNextPlayerOnMove() {
		onMove = onMove.getOpponentColor();
	}

	private boolean isAllyPiece(Piece piece, Vector2D pos) {
		Piece otherPiece = board.getPiece(pos);

		if (otherPiece == null)
			return false;

		return otherPiece.getColor() == piece.getColor();
	}

	public boolean enemyInCheck() {
		int[][] attackedSquares = board.calcAttackedSquaresBy(onMove);
		Vector2D pos = board.getKing(onMove.getOpponentColor()).getPosition();
		boolean inCheck = attackedSquares[pos.getY()][pos.getX()] > 0;
		return inCheck;
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
		this.startingBoard = board.clone();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
