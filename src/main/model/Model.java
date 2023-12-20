package main.model;

import java.util.LinkedList;
import java.util.List;

import main.Settings;
import main.model.chessbots.ChessBot;
import main.model.chessbots.ClassicBot;
import main.model.chessbots.DeeperBlueBot;
import main.model.chessbots.RandomChessBot;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;
import main.model.pieces.Piece;
import utils.ChessMove;
import utils.ChessPieceColor;
import utils.GameOverReason;
import utils.GameState;
import utils.InCheck;
import utils.Move;
import utils.State;
import utils.Vector2D;
import utils.conversions.FENConverter;

public class Model {

	private Settings settings;
	private BoardRepresentation boardRepresentation;
	private MoveValidation moveValidation;

	private GameStatistic gameStatistic;

	private ChessBot selectedChessBot;
	public final ChessBot bot1 = new DeeperBlueBot();
	public final ChessBot bot2 = new ClassicBot();

	public Model() {
		selectedChessBot = bot1;
		selectedChessBot.setColor(ChessPieceColor.BLACK); // By default black
		gameStatistic = new GameStatistic();
	}

	// GAME START
	public void startGame() {
		State.gameState = GameState.IN_GAME;
		State.gameOverReason = GameOverReason.NONE;
		State.inCheck = InCheck.OFF;
		this.boardRepresentation = new BoardRepresentation(
				FENConverter.convertToPieceBoard(settings.selectedFEN.get()));
		moveValidation.setBoard(boardRepresentation);
		this.moveValidation.setOnMove(ChessPieceColor.WHITE);
	}

	public void endGame() {
		State.gameState = GameState.GAME_OVER;
	}

	public boolean movePiece(Vector2D oldPos, Vector2D newPos) {
		boolean success;
		try {
			success = moveValidation.makeMove(oldPos, newPos);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		updateState(success, new Move(oldPos, newPos));
		enterGameResult();
		return success;
	}

	public boolean makeBotMove() {
		BoardRepresentation boardForBot = boardRepresentation.clone();
		Move move = selectedChessBot.makeMove(boardForBot.getBoard());
		boolean success = moveValidation.makeMove(move.from(), move.to());
		updateState(success, move);
		enterGameResult();
		return success;
	}

	private void updateState(boolean success, Move move) { // when surrendered this wont be called
		State.chessMove = success ? ChessMove.VALID : ChessMove.NOT_VALID;
		gameStatistic.enterMove(move, selectedChessBot.toString(), selectedChessBot.getColor());
	}

	private void enterGameResult() {
		if (State.gameState.isGameOver()) {
			if (State.gameOverReason == GameOverReason.DRAW) {
				gameStatistic.enterDraw(selectedChessBot.toString());
			} else {
				gameStatistic.enterGameWinner(selectedChessBot.toString(), selectedChessBot.getColor());
			}
		}
	}

	public boolean inCheck() {
		return State.inCheck == InCheck.ON;
	}

	public Vector2D getKingCheckedPos() {
		return boardRepresentation.getKing(State.lastInCheck).getPosition();
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

	public Move getLastBotMove() {
		return selectedChessBot.getLastMove();
	}

	public boolean gameIsRunning() {
		return State.gameState == GameState.IN_GAME;
	}

	public void playerSurrendersGame() {
		State.gameState = GameState.GAME_OVER;
		State.gameOverReason = moveValidation.getOnMove().isWhite() ? GameOverReason.BLACK_WON
				: GameOverReason.WHITE_WON;
		enterGameResult();
	}

	public List<Vector2D> getMoveablePositions(Vector2D pos) {
		try {
			return boardRepresentation.getPiece(pos).calculateMoveablePositions().stream().flatMap(s -> s.stream())
					.toList();
		} catch (Exception e) {
			System.err.println("Tryed to access position: " + pos + " on board:");
			System.err.println(boardRepresentation);
		}
		return new LinkedList<>();
	}

	public ChessBot getSelectedChessBot() {
		return selectedChessBot;
	}

	public void setSelectedChessBot(ChessBot selectedChessBot) {
		this.selectedChessBot = selectedChessBot;
	}

	public void setColorForSelectedChessBot(ChessPieceColor color) {
		selectedChessBot.setColor(color);
	}

	public void setMillisForBot1(int millis) {
		bot1.setDepthOrMillis(millis);
	}

	public void setDepthForBot2(int depth) {
		bot2.setDepthOrMillis(depth);
	}

}
