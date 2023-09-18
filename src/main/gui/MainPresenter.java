package main.gui;

import ai.AlphaZeroDotFive.AlphaZeroDotFiveAgent;
import main.Settings;
import main.gui.game.board.GamePresenter;
import main.gui.game.gameOver.GameOverPresenter;
import main.gui.game.gameStart.GameStartPresenter;
import main.gui.game.settings.SettingsPresenter;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;
import main.model.Model;
import main.model.Vector2D;
import main.model.chessPieces.SimplePiece;
import main.model.convertions.BoardConverter;
import main.model.convertions.FENConverter;
import main.model.gameStates.State;

import java.io.IOException;

public class MainPresenter extends Presenter {

	private SettingsPresenter settingsPresenter;
	private Settings settings;
	private GamePresenter gamePresenter;
	private GameStartPresenter gameStartPresenter;
	private GameOverPresenter gameOverPresenter;
	private MainView mainView;

	private Model model;

	public boolean moveRequest(Vector2D oldPos, Vector2D newPos) {

		boolean validMove = model.movePiece(oldPos, newPos); // after model.moveRequest

//		if(validMove) // TODO AI
//		{
//			AlphaZeroDotFiveAgent ai = new AlphaZeroDotFiveAgent(2, 500, -1);
//			ai.initRandom();
//			try {
//				ai.addActualValueNet();
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//			int[] coords = ai.getNextMove(ai.getLogic().translateBoard(model.getBoardRepresentation().getBoard()));
//			model.movePiece(coords[0], coords[1], coords[2], coords[3]);
//
//		}
		checkStates();

		return validMove;
	}

	public SimplePiece[][] requestBotMove() {
		SimplePiece[][] simpleBoard = BoardConverter.convertToSimple(model.makeBotMove());
		checkStates();
		return simpleBoard;
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
		System.out.println("checkGameOverStates");
		gameOverPresenter.setDisableView(false);
		if (State.gameOverReason.isNone()) {

		}

		if (State.gameOverReason.isBlackWon()) {

		}

		if (State.gameOverReason.isWhiteWon()) {

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
	}

	public void loadBoard(String fen) {
		gamePresenter.setBoard(FENConverter.convertSimplePieceBoard(fen));
	}

	private void startModel() {
		model.startGame();
	}

	public void surrenderGame() {
	}

	public void endGame() {
		gameOverPresenter.setDisableView(false);
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
		gameStartPresenter.setDisableView(true);
		gameStartPresenter.pauseGame();
	}

	public SimplePiece[][] getGameBoard() {
		return BoardConverter.convertToSimple(model.getBoard());
	}

	public void botSelected(BotRepresentation source) {
		gameStartPresenter.botSelected(source);
		gamePresenter.userPlaysAs(source.getSelectedColor());
	}

	public void playAgainButtonPressed() {

	}

}
