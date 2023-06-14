package main.gui.game.settings;

import main.Settings;
import main.gui.MainPresenter;

public class SettingsPresenter {
	private Settings settings;
	private SettingsView settingsView;
	private MainPresenter mainPresenter;

	public void setInGameContent() {
		settingsView.inGame();
	}

	public void setNoGameContent() {
		settingsView.noGame();
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public SettingsView getSettingsView() {
		return settingsView;
	}

	public void setSettingsView(SettingsView settingsView) {
		this.settingsView = settingsView;
	}

	public MainPresenter getMainPresenter() {
		return mainPresenter;
	}

	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

	public void pauseGameButtonClicked() {
		mainPresenter.pauseGame();
	}
	
	public void setDisableView(boolean disable) {
		settingsView.setDisable(disable);
	}

}
