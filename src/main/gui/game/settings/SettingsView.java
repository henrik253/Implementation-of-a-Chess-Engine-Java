package main.gui.game.settings;

import javafx.scene.layout.Pane;
import main.gui.Component;


public class SettingsView extends Pane implements Component{

	private SettingsPresenter settingsPresenter;
	
	public void init() {
		
	}
	
	public SettingsPresenter getSettingsPresenter() {
		return settingsPresenter;
	}

	public void setSettingsPresenter(SettingsPresenter settingsPresenter) {
		this.settingsPresenter = settingsPresenter;
	}
	
	
}
