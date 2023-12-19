package main.gui;

import java.util.List;

import main.Settings;
import main.gui.game.board.presenter.GamePresenter;
import main.gui.game.gameOver.GameOverPresenter;
import main.gui.game.gameStart.GameStartPresenter;
import main.gui.game.settings.SettingsPresenter;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;
import main.model.Model;
import main.model.gameLogic.BoardRepresentation;
import utils.ChessPieceColor;
import utils.GameState;
import utils.Move;
import utils.SimplePiece;
import utils.State;
import utils.Vector2D;
import utils.conversions.BoardConverter;
import utils.conversions.FENConverter;

public class MainPresenter {

	private SettingsPresenter settingsPresenter;
	private Settings settings;
	private GamePresenter gamePresenter;
	private GameStartPresenter gameStartPresenter;
	private GameOverPresenter gameOverPresenter;
	private MainView mainView;

	private Model model;

	public boolean moveRequest(Vector2D oldPos, Vector2D newPos) {
		boolean validMove;
		try {
			validMove = model.movePiece(oldPos, newPos); // after model.moveRequest
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(model.getBoardRepresentation());
			throw e;
		}
		checkGameStates();
		return validMove;
	}

	public SimplePiece[][] requestBotMove() {
		int c = 0;
		while (State.gameState.inGame()) {
			try {
				if (model.makeBotMove()) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(model.getBoardRepresentation());
				initGameOver();
			}
			if (++c >= 1) {
				System.err.print("\n Bot couldnt find a Move after " + c + " attempts.");
				break;
			}
		}

		checkGameStates();
		return BoardConverter.convertToSimple(model.getBoard());
	}

	public void checkGameStates() {
		if (State.gameState.inGame()) {
			return; // Game can continue normally
		}

		if (State.gameState.isGameOver()) {
			initGameOver();
		}
	}

	private void initGameOver() {
		gameOverPresenter.gameOver();
		gameOverPresenter.setEnableView(true);
		settingsPresenter.setNoGameContent();
	}

	public void surrenderGame() {
		model.playerSurrendersGame();
		try {
			gamePresenter.stopChessBotCalculation();
		} catch (InterruptedException e) {
			System.err.println(
					"Interrupted Exception thrown because thread running the ChessCalculation was stopped during run");
		}
		initGameOver();
	}

	public void startGame() {
		startModel();
		startGameViews();
		startBoard();

		if (model.getSelectedChessBot() != null) { // user plays against bot
			if (model.getSelectedChessBot().getColor().isWhite()) {
				gamePresenter.userMoveSucceeded();
			}
		}
	}

	private void startGameViews() {
		gameStartPresenter.setEnableView(false);
		settingsPresenter.setInGameContent(); // PauseGame Button etc...
	} // specific gui content for the game

	private void startBoard() {
		loadBoard(settings.selectedFEN.get());
		// Dis- and enable listeners
		ChessPieceColor playerColor = settingsPresenter.getSelectedBot().getUserColor();
		gamePresenter.setPieceListenerDisabled(playerColor, false);
		gamePresenter.setPieceListenerDisabled(playerColor.getOpponentColor(), true);
	}

	public void loadBoard(String fen) {
		gamePresenter.setBoard(FENConverter.convertSimplePieceBoard(fen));
		gamePresenter.startGame();
	}

	private void startModel() {
		model.startGame();
	}

	public MainView getMainView() {
		return mainView;
	}

	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}

	public SettingsPresenter getSettingsPresenter() {
		return settingsPresenter;
	}

	public void setSettingsPresenter(SettingsPresenter settingsPresenter) {
		this.settingsPresenter = settingsPresenter;
	}

	public GamePresenter getGamePresenter() {
		return gamePresenter;
	}

	public void setGamePresenter(GamePresenter gamePresenter) {
		this.gamePresenter = gamePresenter;
	}

	public GameStartPresenter getGameStartPresenter() {
		return gameStartPresenter;
	}

	public void setGameStartPresenter(GameStartPresenter gameStartPresenter) {
		this.gameStartPresenter = gameStartPresenter;
	}

	public GameOverPresenter getGameOverPresenter() {
		return gameOverPresenter;
	}

	public void setGameOverPresenter(GameOverPresenter gameOverPresenter) {
		this.gameOverPresenter = gameOverPresenter;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void pauseGame() {
		gameStartPresenter.setEnableView(true);
		gameStartPresenter.pauseGame();
	}

	public SimplePiece[][] getGameBoard() {
		return BoardConverter.convertToSimple(model.getBoard());
	}

	public void botSelected(BotRepresentation source) {
		gameStartPresenter.botSelected(source);
		gameOverPresenter.botSelected(source);
		gamePresenter.userPlaysAs(source.getUserColor());

		if (model != null) {
			model.getSelectedChessBot().setColor(source.getUserColor().getOpponentColor());
			model.setSelectedChessBot(source.getName().equals("bot1") ? model.bot1 : model.bot2);
			model.setColorForSelectedChessBot(source.getUserColor().getOpponentColor());
		}
	}

	public int getRoundCount() {
		return model.getRoundCount();
	}

	public List<Move> getMoveHistory() {
		return model.getMoveHistory();
	}

	public int getBotWins() {
		return model.getBotWins();
	}

	public int getUserWins() {
		return model.getUserWins();
	}

	public int getTotalUserWins() {
		return model.getTotalUserWins();
	}

	public int getTotalBotWins() {
		return model.getTotalBotWins();
	}

	public void playAgainButtonPressed() {
		State.gameState = GameState.NO_GAME;
		gameStartPresenter.setEnableView(true);
		gameOverPresenter.setEnableView(false);
	}

	public Move getLastBotMove() {
		return model.getLastBotMove();
	}

	public boolean kingInCheck() {
		return model.inCheck();
	}

	public Vector2D getKingCheckedPos() {
		return model.getKingCheckedPos();
	}

	public boolean gameIsRunning() {
		return model.gameIsRunning();
	}

	public List<Vector2D> getMoveablePositions(Vector2D pos) {
		return model.getMoveablePositions(pos);
	}

	public void bot1SliderMillisChanged(int millis) {
		model.setMillisForBot1(millis);
	}

	public void bot2SliderDepthChanged(int depth) {
		model.setDepthForBot2(depth);
	}

}
