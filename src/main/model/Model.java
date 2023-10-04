package main.model;

import java.util.List;

import main.Settings;
import main.model.chessBots.ChessBot;
import main.model.chessBots.RandomChessBot;
//import main.model.Chessbots.MCTSBot;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;
import main.model.convertions.FENConverter;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;
import main.model.gameStates.ChessMove;
import main.model.gameStates.GameOverReason;
import main.model.gameStates.GameState;
import main.model.gameStates.InCheck;
import main.model.gameStates.State;
import main.model.statistics.GameStatistic;
import utils.Move;
import utils.Vector2D;

public class Model {

	private Settings settings;
	private BoardRepresentation boardRepresentation;
	private MoveValidation moveValidation;

	private ChessBot selectedChessBot = new RandomChessBot(); // TODO 
	
	private GameStatistic gameStatistic;

	public Model() {
		selectedChessBot = new RandomChessBot();
		selectedChessBot.setColor(ChessPieceColor.BLACK);
		gameStatistic = new GameStatistic();
	}

	// GAME START
	public void startGame() {
		State.gameState = GameState.IN_GAME;
		State.gameOverReason = GameOverReason.NONE;
		State.inCheck = InCheck.OFF;
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
		updateState(success, new Move(oldPos, newPos));
		enterGameResult();
		return success;
	}

	public boolean makeBotMove() {
		Move move = selectedChessBot.makeMove(boardRepresentation.getBoard().clone());
		boolean success = moveValidation.makeMove(move.getOldPos(), move.getNewPos());
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
		return State.lastInCheck.isWhite() ? boardRepresentation.getWhiteKing().getPosition()
				: boardRepresentation.getBlackKing().getPosition();
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
		return boardRepresentation.getPiece(pos).calculateMoveablePositions().stream()
				.flatMap( s -> s.stream()).toList();
	}

}
