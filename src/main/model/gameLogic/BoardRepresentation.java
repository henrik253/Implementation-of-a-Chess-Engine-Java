package main.model.gameLogic;

import java.util.Arrays;
import java.util.List;

import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.King;
import main.model.chessPieces.concretePieces.Piece;

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

	public void calcAttackedSquares() {
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
					attackedSquares[y][x] += 1;
					break;
				} else if (attackedSquares[y][x] == 0) { // no attack on this square
					attackedSquares[y][x] += 1;
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

		board[newPos.getY()][newPos.getX()] = piece;
		board[oldPos.getY()][oldPos.getX()] = null;

		piece.setPosition(newPos);
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
		int pos = 0;
		for (Piece[] p : board) {
			result[pos++] = p.clone();
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
