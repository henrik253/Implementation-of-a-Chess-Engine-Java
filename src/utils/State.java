package utils;

public class State {

	public static ChessMove chessMove = ChessMove.NONE;
	public static GameState gameState = GameState.NO_GAME;
//	public NotValidMoveReason notValidMoveReason = NotValidMoveReason.NONE;
	public static GameOverReason gameOverReason = GameOverReason.NONE;
	public static InCheck inCheck = InCheck.OFF;
	public static ChessPieceColor lastInCheck;
}
