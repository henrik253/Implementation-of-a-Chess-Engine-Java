package main.gui;

import main.gui.game.board.GamePresenter;
import main.gui.game.gameStart.GameStartPresenter;
import main.gui.game.settings.SettingsPresenter;
import main.model.start.FENConverter;

public class MainPresenter extends Presenter {

	private SettingsPresenter settingsPresenter;
	private GamePresenter gamePresenter;
	private GameStartPresenter gameStartPresenter;

	private MainView mainView;

	public void startGame() {
		gameStartPresenter.setDisableView(false);
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
}
