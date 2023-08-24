package main.gui;

import ai.AlphaZeroDotFive.AlphaZeroDotFiveAgent;
import main.Settings;
import main.gui.game.board.GamePresenter;
import main.gui.game.gameStart.GameStartPresenter;
import main.gui.game.settings.SettingsPresenter;
import main.model.Model;
import main.model.chessPieces.SimplePiece;
import main.model.gameStates.State;
import main.model.start.BoardConverter;
import main.model.start.FENConverter;

import java.io.IOException;

public class MainPresenter extends Presenter {

	private SettingsPresenter settingsPresenter;
	private Settings settings;
	private GamePresenter gamePresenter;
	private GameStartPresenter gameStartPresenter;
	private MainView mainView;

	private Model model;

	public boolean moveRequest(int oldX, int oldY, int newX, int newY) {

		boolean validMove = model.movePiece(oldX, oldY, newX, newY); // after model.moveRequest

		if(validMove) // TODO AI
		{
			AlphaZeroDotFiveAgent ai = new AlphaZeroDotFiveAgent(2, 500, -1);
			ai.initRandom();
			try {
				ai.addActualValueNet();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			int[] coords = ai.getNextMove(ai.getLogic().translateBoard(model.getBoardRepresentation().getBoard()));
			model.movePiece(coords[0], coords[1], coords[2], coords[3]);

		}

		checkStates();

		return validMove;
	}

	public void checkStates() {

		if (State.gameState.inGame()) {
			return; // Game can continue normally
		}

		if (State.gameState.isGameOver()) {
			checkGameOverStates();
		}
	}

	private void checkGameOverStates() {
		if (State.gameOverReason.isNone()) {

		}

		if (State.gameOverReason.isBlackWon()) {
			// show winning Screen for Black
		}

		if (State.gameOverReason.isWhiteWon()) {
			// show winning Screen for White
		}

		if (State.gameOverReason.isDraw()) {
			// show Draw Screen
		}
	}

	public void startGame() {
		startGameViews();
		startBoard();
		startModel();
	}

	private void startGameViews() {
		gameStartPresenter.setDisableView(false);
		settingsPresenter.setInGameContent(); // PauseGame Button etc...
	} // specific gui content for the game

	private void startBoard() {
		loadBoard(settings.selectedFEN.get());
	} // load the board

	private void startModel() {
		model.startGame();
	}

	public void surrenderGame() {
	}

	public void endGame() {

	}

	public void loadBoard(String fen) {
		gamePresenter.setBoard(FENConverter.convertSimplePieceBoard(fen));
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
		gameStartPresenter.setDisableView(true);
		gameStartPresenter.pauseGame();
	}

	public SimplePiece[][] getGameBoard() {
		return BoardConverter.convertToSimple(model.getBoard());

	}

}
