package main.model;

import java.util.List;

import main.ChessBotStub;
import main.Settings;
//import main.model.Chessbots.MCTSBot;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;
import main.model.convertions.FENConverter;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;
import main.model.gameStates.ChessMove;
import main.model.gameStates.GameOverReason;
import main.model.gameStates.GameState;
import main.model.gameStates.State;
import main.model.statistics.GameStatistic;

public class Model {

	private Settings settings;
	private BoardRepresentation boardRepresentation;
	private MoveValidation moveValidation;

	private ChessBot selectedChessBot = new ChessBotStub();

	private GameStatistic gameStatistic;

	public Model() {
		selectedChessBot = new ChessBotStub();
		selectedChessBot.setColor(ChessPieceColor.BLACK);
		gameStatistic = new GameStatistic();
	}

	// GAME START
	public void startGame() {
		State.gameState = GameState.IN_GAME;
		State.gameOverReason = GameOverReason.NONE;
		this.boardRepresentation = new BoardRepresentation(FENConverter.convertPieceBoard(settings.selectedFEN.get())); // <----
		moveValidation.setBoard(boardRepresentation);
		this.moveValidation.setOnMove(ChessPieceColor.WHITE);
	}

	public void endGame() {
		State.gameState = GameState.GAME_OVER;
	}

	// GAME RUNNING
	public boolean movePiece(Vector2D oldPos, Vector2D newPos) { // <----
		boolean success = moveValidation.makeMove(oldPos, newPos);
		refreshStates(success, new Move(oldPos, newPos));
		return success;
	}

	public boolean makeBotMove() {
		Move move = selectedChessBot.makeMove(boardRepresentation.getBoard().clone());
		boolean success = moveValidation.makeMove(move.getOldPos(), move.getNewPos());
		refreshStates(success, move);
		return success;
	}

	private void refreshStates(boolean success, Move move) {
		updateStateAndStatistics(success);
		updateMoveStatistic(move);
	}

	private void updateStateAndStatistics(boolean lastMoveSuccess) {
		State.chessMove = lastMoveSuccess ? ChessMove.VALID : ChessMove.NOT_VALID;

		if (State.gameState.isGameOver()) {
			gameStatistic.enterGameWinner(selectedChessBot.toString(), selectedChessBot.getColor());
		}

	}

	private void updateMoveStatistic(Move move) {
		gameStatistic.enterMove(move, selectedChessBot.toString(), selectedChessBot.getColor());
	}

	// All statistic are for the selectedChessBot
	public int getRoundCount() {
		return gameStatistic.getRoundCount(selectedChessBot.toString());
	}

	public List<Move> getMoveHistory() {
		return gameStatistic.getMoves(selectedChessBot.toString());
	}

	public int getBotWins() {
		return gameStatistic.getBotWins(selectedChessBot.toString());
	}

	public int getUserWins() {
		return gameStatistic.getUserWins(selectedChessBot.toString());
	}

	public int getTotalUserWins() {
		return gameStatistic.getTotalUserWins();
	}

	public int getTotalBotWins() {
		return gameStatistic.getTotalBotWins();
	}

	public void setChessBot(ChessBot chessBot) {
		this.selectedChessBot = chessBot;
	}

	public Piece[][] getBoard() {
		return boardRepresentation.getBoard();
	}

	public MoveValidation getMoveValidation() {
		return moveValidation;
	}

	public void setMoveValidation(MoveValidation moveValidation) {
		this.moveValidation = moveValidation;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public BoardRepresentation getBoardRepresentation() {
		return boardRepresentation;
	}

	public void setBoardRepresentation(BoardRepresentation board) {
		this.boardRepresentation = board;
	}

}
