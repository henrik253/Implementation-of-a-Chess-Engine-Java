package utils;

import java.util.Arrays;

public class SimpleBoard {
	private final SimplePiece[][] board;

	public SimpleBoard(SimplePiece[][] board) {
		this.board = board;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SimpleBoard) {
			SimpleBoard b = (SimpleBoard) o;
		}
		return false;
	}

	public boolean equals(SimpleBoard b) {
		return Arrays.deepEquals(board, b.board); 
	}

	public SimplePiece[][] getBoard() {
		return board;
	}

}
