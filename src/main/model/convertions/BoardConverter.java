package main.model.convertions;

import main.model.chessPieces.concretePieces.Bishop;
import main.model.chessPieces.concretePieces.King;
import main.model.chessPieces.concretePieces.Knight;
import main.model.chessPieces.concretePieces.Pawn;
import main.model.chessPieces.concretePieces.Piece;
import main.model.chessPieces.concretePieces.Queen;
import main.model.chessPieces.concretePieces.Rook;
import utils.ChessPieceName;
import utils.SimplePiece;

public class BoardConverter {

	public static SimplePiece[][] convertToSimple(Piece[][] board) {
		SimplePiece[][] simpleBoard = new SimplePiece[board.length][board.length];
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				if (board[row][column] != null) {
					simpleBoard[row][column] = getSimplePiece(board[row][column]);
				}
			}
		}

		return simpleBoard;
	}

	public static SimplePiece getSimplePiece(Piece piece) {

		if (piece instanceof Pawn) {
			return new SimplePiece(ChessPieceName.PAWN, piece.getColor());
		}
		if (piece instanceof Rook) {
			return new SimplePiece(ChessPieceName.ROOK, piece.getColor());
		}
		if (piece instanceof Knight) {
			return new SimplePiece(ChessPieceName.KNIGHT, piece.getColor());
		}
		if (piece instanceof Bishop) {
			return new SimplePiece(ChessPieceName.BISHOP, piece.getColor());
		}
		if (piece instanceof Queen) {
			return new SimplePiece(ChessPieceName.QUEEN, piece.getColor());
		}
		if (piece instanceof King) {
			return new SimplePiece(ChessPieceName.KING, piece.getColor());
		}

		return null;
	}
}
