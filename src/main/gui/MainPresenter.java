package main.gui;

import java.util.List;

import main.Settings;
import main.gui.game.board.GamePresenter;
import main.gui.game.gameOver.GameOverPresenter;
import main.gui.game.gameStart.GameStartPresenter;
import main.gui.game.settings.SettingsPresenter;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;
import main.model.Model;
import main.model.Move;
import main.model.Vector2D;
import main.model.chessPieces.SimplePiece;
import main.model.convertions.BoardConverter;
import main.model.convertions.FENConverter;
import main.model.gameStates.GameState;
import main.model.gameStates.State;

public class MainPresenter  {

	private SettingsPresenter settingsPresenter;
	private Settings settings;
	private GamePresenter gamePresenter;
	private GameStartPresenter gameStartPresenter;
	private GameOverPresenter gameOverPresenter;
	private MainView mainView;

	private Model model;

	public boolean moveRequest(Vector2D oldPos, Vector2D newPos) {
		boolean validMove = model.movePiece(oldPos, newPos); // after model.moveRequest

		checkGameStates();

		return validMove;
	}

	public SimplePiece[][] requestBotMove() {

		// TODO TEMP

		while (State.gameState.inGame()) {
			if (model.makeBotMove()) {
				break;
			}
		}
		checkGameStates();
		return BoardConverter.convertToSimple(model.getBoard());

//		model.makeBotMove();
//		SimplePiece[][] simpleBoard = BoardConverter.convertToSimple(model.getBoard());
//		return BoardConverter.convertToSimple(model.getBoard()); // no move
		// TODO TEMP
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
	}

	public void startGame() {
		startGameViews();
		startBoard();
		startModel();
	}

	private void startGameViews() {
		gameStartPresenter.setEnableView(false);
		settingsPresenter.setInGameContent(); // PauseGame Button etc...
	} // specific gui content for the game

	private void startBoard() {
		loadBoard(settings.selectedFEN.get());
		// Dis- and enable listeners
//		ChessPieceColor playerColor = settingsPresenter.getSelectedBot().getUserColor();
//		gamePresenter.setPieceListenerDisabled(playerColor, false);
//		gamePresenter.setPieceListenerDisabled(playerColor.getOpponentColor(), true);
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
		gameOverPresenter.setEnableView(false);
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
		gameStartPresenter.setEnableView(true);
		gameOverPresenter.setEnableView(false);
	}

}
