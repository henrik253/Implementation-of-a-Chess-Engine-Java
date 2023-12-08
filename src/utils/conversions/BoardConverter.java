package utils.conversions;

import main.model.pieces.Bishop;
import main.model.pieces.King;
import main.model.pieces.Knight;
import main.model.pieces.Pawn;
import main.model.pieces.Piece;
import main.model.pieces.Queen;
import main.model.pieces.Rook;
import utils.ChessPieceName;
import utils.SimplePiece;

// this class allows conversions between different Boards
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
