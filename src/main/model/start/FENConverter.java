package main.model.start;

import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;
import main.model.chessPieces.SimplePiece;

public class FENConverter {

	private static final int ROWS = 8;
	private static final int COLUMNS = 8;
	// small letters represent black figures

	public static SimplePiece[][] convert(String fen) {
		SimplePiece[][] board = new SimplePiece[ROWS][COLUMNS];

		int row = 0;
		int column = 0;
		String[] splitFen = fen.split(" ");
		String firstPart = splitFen[0];

		try {
			for (int i = 0; i < firstPart.length(); i++) {
				char c = firstPart.charAt(i);

				if (Character.isLetter(c))
					board[row][column] = buildSimplePiece(c);
				else {
					column += Character.getNumericValue(c);
				}

				column++;

				if (column >= COLUMNS) {
					column = 0;
					row++;
				}

			}
		} catch (Exception e) {
			throw new IllegalArgumentException("not a valid FEN-String");
		}

		return board;
	}
	
	

	public static SimplePiece buildSimplePiece(char c) {
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

}
