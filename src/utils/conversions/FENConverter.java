package utils.conversions;

import main.model.pieces.Bishop;
import main.model.pieces.King;
import main.model.pieces.Knight;
import main.model.pieces.Pawn;
import main.model.pieces.Piece;
import main.model.pieces.Queen;
import main.model.pieces.Rook;
import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.SimplePiece;
import utils.Vector2D;

public class FENConverter { // Converting a FEN String to

	private static final int ROWS = 8;
	private static final int COLUMNS = 8;

	public static SimplePiece[][] convertSimplePieceBoard(String fen) {
		SimplePiece[][] board = new SimplePiece[ROWS][COLUMNS];

		int row = 0;
		int column = -1;
		String[] parts = fen.split(" ");
		String firstPart = parts[0];

		try {
			for (int i = 0; i < firstPart.length(); i++) {
				char c = firstPart.charAt(i);

				if (c == '/') {
					column = -1;
					row++;
					continue;
				}
				if (Character.isLetter(c)) {
					board[row][++column] = buildSimplePiece(c);
					continue;
				}
				if (Character.getNumericValue(c) != -1) {
					column += Character.getNumericValue(c);
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("not a valid FEN-String");
		}

		return board;
	}

	private static SimplePiece buildSimplePiece(char c) {
		ChessPieceColor color = Character.isUpperCase(c) ? ChessPieceColor.WHITE : ChessPieceColor.BLACK;
		c = Character.toLowerCase(c);
		return switch (c) {
		case 'p' -> new SimplePiece(ChessPieceName.PAWN, color);
		case 'r' -> new SimplePiece(ChessPieceName.ROOK, color);
		case 'b' -> new SimplePiece(ChessPieceName.BISHOP, color);
		case 'n' -> new SimplePiece(ChessPieceName.KNIGHT, color);
		case 'q' -> new SimplePiece(ChessPieceName.QUEEN, color);
		case 'k' -> new SimplePiece(ChessPieceName.KING, color);
		default -> throw new IllegalArgumentException();
		};

	}

	public static Piece[][] convertToPieceBoard(String fen) {
		Piece[][] board = new Piece[ROWS][COLUMNS];

		int row = 0;
		int column = -1;
		String[] parts = fen.split(" ");
		String firstPart = parts[0];

		try {
			for (int i = 0; i < firstPart.length(); i++) {
				char c = firstPart.charAt(i);

				if (c == '/') {
					column = -1;
					row++;
					continue;
				}
				if (Character.isLetter(c)) {
					column++;
					Piece p = buildPiece(c, row, column);
					board[row][column] = setSettingsForPiece(p);

					continue;
				}
				if (Character.getNumericValue(c) != -1) {
					column += Character.getNumericValue(c);
				}
			}
		} catch (Exception e) {
			throw e;
		}

		return board;
	}

	private static Piece buildPiece(char c, int row, int column) {
		ChessPieceColor color = Character.isUpperCase(c) ? ChessPieceColor.WHITE : ChessPieceColor.BLACK;
		c = Character.toLowerCase(c);
		return switch (c) {
		case 'p' -> new Pawn(color, row, column);
		case 'r' -> new Rook(color, row, column);
		case 'b' -> new Bishop(color, row, column);
		case 'n' -> new Knight(color, row, column);
		case 'q' -> new Queen(color, row, column);
		case 'k' -> new King(color, row, column);
		default -> throw new IllegalArgumentException(c + " at " + " row " + row + " & column " + column);
		};

	}
	 // only the necessary settings for double Pawn move, Castling rights, ... 
	private static Piece setSettingsForPiece(Piece p) {
		if (p instanceof Pawn) {
			int y = p.getPosition().getY();
			if (y == 6 || y == 1) {
				p.setFirstMove(true);
			} else
				p.setFirstMove(false);
		} else if (p instanceof Knight) {
			Vector2D whiteKingPos = new Vector2D(4, 7), blackKingPos = new Vector2D(4, 0);
			if (p.getColor().isWhite() && !p.getPosition().equals(whiteKingPos)) {
				p.setFirstMove(false);
			} else if (!p.getPosition().equals(blackKingPos)) {
				p.setFirstMove(false);
			}

		}

		return p;
	}

}
