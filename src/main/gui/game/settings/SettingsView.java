package main.gui.game.settings;

import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.gui.Component;


public class SettingsView extends Pane implements Component{

	private SettingsPresenter settingsPresenter;
	private Settings settings; 
	

	public void init() {
		this.setTranslateX(settings.settingsViewTranslateX.get());
		this.setTranslateY(settings.settingsViewTranslateY.get());
		this.setPrefWidth(settings.settingsViewPrefWidth.get());
		this.setPrefHeight(settings.settingsViewPrefHeight.get());
		this.setPrefHeight(settings.WINDOW_HEIGHT);
		
	}
	
	public SettingsPresenter getSettingsPresenter() {
		return settingsPresenter;
	}

	public void setSettingsPresenter(SettingsPresenter settingsPresenter) {
		this.settingsPresenter = settingsPresenter;
	}
	

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	
}
