package main.model.chessPieces;

public class SimplePiece {

	private ChessPieceName name;
	private ChessPieceColor color;

	public SimplePiece(ChessPieceName name, ChessPieceColor color) {
		this.name = name;
		this.color = color;
	}

	public ChessPieceName getName() {
		return name;
	}

	public ChessPieceColor getColor() {
		return color;
	}
	
	public String toString() {
		return name+"_"+color;
	}

}
