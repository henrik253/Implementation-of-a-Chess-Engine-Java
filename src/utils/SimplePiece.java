package utils;

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
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof SimplePiece) {
			SimplePiece p = (SimplePiece) o; 
			return p.name.equals(this.name) && p.color.equals(this.color);
		}
		return false; 
	}
	
	public ChessPieceColor getColor() {
		return color;
	}

	public String toString() {
		return name + "_" + color;
	}

}
