package utils;

public enum GameOverReason {
	BLACK_WON, WHITE_WON, DRAW, NONE;

	public boolean isBlackWon() {
		return this == BLACK_WON;
	}

	public boolean isWhiteWon() {
		return this == WHITE_WON;
	}

	public boolean isDraw() {
		return this == DRAW;
	}

	public boolean isNone() {
		return this == NONE;
	}
}
