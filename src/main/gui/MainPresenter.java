package main.gui;

import main.gui.game.board.GamePresenter;
import main.gui.game.gameStart.GameStartPresenter;
import main.gui.game.settings.Settings;
import main.gui.game.settings.SettingsPresenter;
import main.model.Model;
import main.model.gameStates.State;
import main.model.start.FENConverter;

public class MainPresenter extends Presenter {

	private SettingsPresenter settingsPresenter;
	private Settings settings;
	private GamePresenter gamePresenter;
	private GameStartPresenter gameStartPresenter;
	private MainView mainView;

	private Model model;

	public boolean moveRequest(int oldX, int oldY, int newX, int newY) {

	
		boolean validMove = State.chessMove.isValid(); // after model.moveRequest

		return validMove;
	}

	public void startGame() {
		gameStartPresenter.setDisableView(false);
		loadBoard(settings.selectedFEN.get());
		settingsPresenter.setInGameContent();
	}

	public void surrenderGame() {
	}

	public void endGame() {

	}

	public void loadBoard(String fen) {
		gamePresenter.setBoard(FENConverter.convert(fen));
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
}
