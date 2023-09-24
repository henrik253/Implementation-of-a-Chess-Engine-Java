package main.gui.game.gameStart;

import main.Settings;
import main.gui.MainPresenter;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;

public class GameStartPresenter {

	private GameStartView gameStartView;
	private MainPresenter mainPresenter;
	private Settings settings;

	public void playButtonPressed() {
		mainPresenter.startGame();
	}

	public void pauseGame() {
		gameStartView.setPlayButtonText(settings.playButtonTextContinue.get());
	}

	public void setEnableView(boolean disable) {
		gameStartView.setVisible(disable);
	}

	public GameStartView getGameStartView() {
		return gameStartView;
	}

	public void setGameStartView(GameStartView gameStartView) {
		this.gameStartView = gameStartView;
	}

	public MainPresenter getMainPresenter() {
		return mainPresenter;
	}

	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public void botSelected(BotRepresentation source) {
		gameStartView.drawSelectedBot(source);
	}

}
