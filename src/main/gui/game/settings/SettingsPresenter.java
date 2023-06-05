package main.gui.game.settings;

public class SettingsPresenter {
	private Settings settings;
	private SettingsView settingsView;

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

}
