package main.gui.game.settings;

import main.Settings;
import main.gui.MainPresenter;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;

public class SettingsPresenter {
	private Settings settings;
	private SettingsView settingsView;
	private MainPresenter mainPresenter;

	public void setInGameContent() {
		settingsView.toggleSelectSurrenderButton(true);
		settingsView.setDisableColorSelect(true);
		settingsView.setVisibleBotSelectButtons(false);
		settingsView.setVisibleInsertFENView(false);
	}

	public void setNoGameContent() {
		settingsView.toggleSelectSurrenderButton(false);
		settingsView.setDisableColorSelect(false);
		settingsView.setVisibleBotSelectButtons(true);
		settingsView.setVisibleInsertFENView(true);
	}

	public void loadBoard(String fen) {
		mainPresenter.loadBoard(fen);
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

	public void botSelected(BotRepresentation source) {
		mainPresenter.botSelected(source);
	}

	public BotRepresentation getSelectedBot() {
		return settingsView.getSelectedBot();
	}

	public void surrenderButtonPressed() {
		mainPresenter.surrenderGame();
	}

}
