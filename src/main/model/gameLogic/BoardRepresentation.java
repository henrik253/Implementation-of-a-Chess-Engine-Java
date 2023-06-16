package main.model.gameLogic;

import java.util.Arrays;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.King;
import main.model.chessPieces.concretePieces.Piece;
import main.model.chessPieces.concretePieces.Rook;

public class BoardRepresentation {

	private Piece[][] board;
	private int[][] attackedSquaresByWhite;
	private int[][] attackedSquaresByBlack;

	public BoardRepresentation(Piece[][] board) {
		this.board = board;
		calcAttackedSquares();

	}

	public BoardRepresentation(int length) {
		this.board = new Piece[length][length];
		calcAttackedSquares();
	}

	public void calcAttackedSquares() { // inefficient solution
		int[][] attackedSquaresByWhite = new int[board.length][board.length];
		int[][] attackedSquaresByBlack = new int[board.length][board.length];

		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				Piece piece = board[row][column];
				if (piece == null)
					continue;

				if (piece.getColor().isWhite()) {
					markAttackedSquares(piece, attackedSquaresByWhite);
				} else if (piece.getColor().isBlack()) {
					markAttackedSquares(piece, attackedSquaresByBlack);
				}
			}
		}
		this.attackedSquaresByWhite = attackedSquaresByWhite;
		this.attackedSquaresByBlack = attackedSquaresByBlack;
	}

	private void markAttackedSquares(Piece piece, int[][] attackedSquares) {
		List<List<Vector2D>> moves = piece.calculateAttackablePositions(piece.getPosition());
		for (List<Vector2D> movesInDirection : moves) {
			for (Vector2D move : movesInDirection) {
				// Vector2D is alway (x,y) tuple. x in this case are the columns and y the rows
				int y = move.getY(), x = move.getX();
				if (board[y][x] instanceof Piece) {
					attackedSquares[y][x]++;
					break;
				} else if (attackedSquares[y][x] >= 0) { // no attack on this square
					attackedSquares[y][x]++;
				}
			}

		}
	}

	public Piece getKing(ChessPieceColor color) {
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				Piece piece = board[row][column];
				if (piece instanceof King) {
					if (piece.getColor() == color) {
						return piece;
					}
				}

			}
		}
		return null;
	}

	public void makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece piece = board[oldPos.getY()][oldPos.getX()];

		checkSpecialMoves(oldPos, newPos);

		board[newPos.getY()][newPos.getX()] = piece;
		board[oldPos.getY()][oldPos.getX()] = null;

		piece.setPosition(newPos);
	}

	public boolean checkSpecialMoves(Vector2D oldPos, Vector2D newPos) {
		boolean castling = tryCastling(oldPos, newPos);

		if (castling) {
			return true;
		}

		return false;
	}

	public boolean tryCastling(Vector2D oldPos, Vector2D newPos) {
		Piece piece = getPiece(oldPos);
		if (!(piece instanceof King))
			return false;

		King king = (King) piece;

		if (!king.isFirstMove())
			return false;

		if (!king.isValidCastle(newPos))
			return false;

		// left or right side castle
		boolean isRightSideCastle = newPos.getX() - king.getPosition().getX() > 0;

		int xPos = isRightSideCastle ? board.length - 1 : 0;
		Vector2D rookPos = new Vector2D(xPos, king.getPosition().getY());

		if (countFiguresBetween(king.getPosition(), rookPos) > 0) {
			return false;
		}

		if (checkInCastlingMove(king.getPosition(), rookPos)) {
			return false;
		}

		Piece rook = getPiece(rookPos);

		if (rook instanceof Rook && ((Rook) rook).isFirstMove()) {
			Vector2D direction = new Vector2D(isRightSideCastle ? -2 : 3, 0);
			makeMove(rookPos, Vector2D.add(rookPos, direction));
			return true;
		}

		return false;
	}

	public boolean checkInCastlingMove(Vector2D kingPos, Vector2D rookPos) {
		int x = kingPos.getX() - rookPos.getX();
		int y = kingPos.getY() - rookPos.getY();
		int length = Math.max(Math.abs(x), Math.abs(y));

		int countX = kingPos.getX();
		int countY = kingPos.getY();

		int incX = (x > 0 ? 1 : -1);
		int incY = (y > 0 ? 1 : -1);

		incX = x != 0 ? incX : 0;
		incY = y != 0 ? incY : 0;

		int[][] attackedSquares = getPiece(kingPos).getColor().isWhite() ? attackedSquaresByBlack
				: attackedSquaresByWhite;

		for (int step = 0; step < length - 1; step++) {
			countX -= incX;
			countY -= incY;
			if (attackedSquares[countY][countX] > 0) {
				return true;
			}
		}
		return false;

	}

	public int countFiguresBetween(Vector2D pos1, Vector2D pos2) {
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
			if (this.board[countY][countX] != null) {
				count++;
			}
		}
		return count;
	}

	public Piece getPiece(Vector2D position) {
		return board[position.getY()][position.getX()];
	}

	public Piece[][] getBoard() {
		return board;
	}

	@Override
	public BoardRepresentation clone() { // !!!!!!!!!!!!!!

		BoardRepresentation clone = new BoardRepresentation(this.getBoardClone());

		return clone;
	}

	public Piece[][] getBoardClone() {
		Piece[][] result = new Piece[board.length][board.length];
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				Piece p = this.board[row][column];
				if (p != null)
					result[row][column] = p.clone();
			}
		}

		return result;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	public int[][] getAttackedSquaresByWhite() {
		return attackedSquaresByWhite;
	}

	public void setAttackedSquaresByWhite(int[][] attackedSquaresByWhite) {
		this.attackedSquaresByWhite = attackedSquaresByWhite;
	}

	public int[][] getAttackedSquaresByBlack() {
		return attackedSquaresByBlack;
	}

	public void setAttackedSquaresByBlack(int[][] attackedSquaresByBlack) {
		this.attackedSquaresByBlack = attackedSquaresByBlack;
	}

	public String toBoardString() {
		String result = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {

				if (board[i][j] instanceof Piece) {
					result += board[i][j].toString().charAt(0);
				} else
					result += " ";

				result += " | ";
			}
			result += "\n";
			result += "_".repeat(40);
			result += "\n";
		}

		return result;
	}

	public String toString() {
		String resultWhite = "";
		String resultBlack = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				resultWhite += attackedSquaresByWhite[i][j];

				resultBlack += attackedSquaresByBlack[i][j];

				resultWhite += " | ";
				resultBlack += " | ";
			}
			resultWhite += "\n";
			resultWhite += "_".repeat(40);
			resultWhite += "\n";

			resultBlack += "\n";
			resultBlack += "_".repeat(40);
			resultBlack += "\n";
		}

		return resultWhite + " \n" + "#".repeat(50) + "\n \n" + resultBlack;
	}

}
