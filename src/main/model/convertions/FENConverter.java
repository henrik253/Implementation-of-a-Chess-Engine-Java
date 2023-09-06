package main.model.convertions;

import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.ChessPieceName;
import main.model.chessPieces.SimplePiece;
import main.model.chessPieces.concretePieces.Bishop;
import main.model.chessPieces.concretePieces.King;
import main.model.chessPieces.concretePieces.Knight;
import main.model.chessPieces.concretePieces.Pawn;
import main.model.chessPieces.concretePieces.Piece;
import main.model.chessPieces.concretePieces.Queen;
import main.model.chessPieces.concretePieces.Rook;

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
	
	public static Piece[][] convertPieceBoard(String fen) {
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
					board[row][++column] = buildPiece(c,row,column);
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

	public static Piece buildPiece(char c,int row,int column) {
		ChessPieceColor color = Character.isUpperCase(c) ? ChessPieceColor.WHITE : ChessPieceColor.BLACK;
		c = Character.toLowerCase(c);
		return switch (c) {
		case 'p' -> new Pawn(color,row,column);
		case 'r' -> new Rook(color,row,column);
		case 'b' -> new Bishop(color,row,column);
		case 'n' -> new Knight(color,row,column);
		case 'q' -> new Queen(color,row,column);
		case 'k' -> new King(color,row,column);
		default -> throw new IllegalArgumentException();
		};

	}

	

}
