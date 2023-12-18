package utils;

public enum ChessPieceColor {
	BLACK,WHITE;
	
	public boolean isWhite() {
		return this == ChessPieceColor.WHITE;
	}
	
	public boolean isBlack() {
		return this == ChessPieceColor.BLACK;
	}
	
	public ChessPieceColor getOpponentColor() {
		return this == ChessPieceColor.WHITE ? ChessPieceColor.BLACK : ChessPieceColor.WHITE; 
	}
}


