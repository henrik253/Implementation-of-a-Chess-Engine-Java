package main.model.chessPieces;

public enum ChessPieceColor {
	BLACK,WHITE;
	
	public boolean isWhite() {
		return this == ChessPieceColor.WHITE;
	}
	
	public boolean isBlack() {
		return this == ChessPieceColor.BLACK;
	}
}
