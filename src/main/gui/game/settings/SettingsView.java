package main.gui.game.settings;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.Settings;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;
import main.gui.game.settings.settingsViewComponents.BotSelectionView;

public class SettingsView extends Pane {

	private SettingsPresenter settingsPresenter;
	private Settings settings;

	private Pane gameContent;
	private Pane noGameContent;

	private BotSelectionView botSelectionView;

	public void init() { // init() is called when all Comps. are connected
		this.setTranslateX(settings.settingsViewTranslateX.get()); 
		this.setTranslateY(settings.settingsViewTranslateY.get());
		this.setPrefWidth(settings.settingsViewPrefWidth.get());
		this.setPrefHeight(settings.settingsViewPrefHeight.get());
		this.setPrefHeight(settings.WINDOW_HEIGHT);
		this.setId("SettingsView");
		this.botSelectionView = new BotSelectionView(this);

		initGameContent();

		// inGameContent = new InGamePane();
		noGameContent = new NoGamePane();

		this.getChildren().add(botSelectionView);

	}

	private void initGameContent() {
		gameContent = new Pane();
		this.getChildren().add(gameContent);
	}

	public void setInGameContent() { // TODO CHANGE IN AND NO GAME TO Method if(Game.isRunning) - Settings haben
										// nichts damit zu tun, ob ein Spiel läuft oder nicht

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

	public BotRepresentation getSelectedBot() {
		return botSelectionView.getSelected();
	}

//	public class InGamePane extends VBox {
//
//		private Button remiButton;
//		private Button surrenderButton;
//		private Button pauseGameButton;
//
//		private Pane pauseGameContainer;
//		private Pane gameActionsContainer;
//		private Insets containerPadding;
//
//		public InGamePane() {
//			this.setPrefWidth(settings.settingsViewInGamePrefWidth.get());
//			this.setPrefHeight(settings.settingsViewPrefHeight.get() / 5); // TODO: Remove Magical Numbers 5 And 2
//			this.setTranslateX(settings.settingsViewInGameWidthOffset.get() / 2);
//			this.setTranslateY(settings.settingsViewPrefHeight.get() / 2);
//			this.setId("InGamePane");
//
//			pauseGameContainer = new HBox();
//			gameActionsContainer = new HBox();
//			containerPadding = new Insets(10, 0, 10, 0);
//
//			remiButton = new Button("Remi");
//			surrenderButton = new Button("Surrender");
//			pauseGameButton = new Button("Pause Game");
//			
//			styleElements();
//			initListeners();
//			
//			pauseGameContainer.getChildren().add(pauseGameButton);
//			gameActionsContainer.getChildren().addAll(surrenderButton, remiButton);
//
//			this.getChildren().add(pauseGameContainer);
//			this.getChildren().add(gameActionsContainer);
//		}
//
//		private void styleElements() {
//			remiButton.setId("remiButton");
//			surrenderButton.setId("surrenderButton");
//			pauseGameButton.setId("pauseGameButton");
//			pauseGameContainer.setPadding(containerPadding);
//			gameActionsContainer.setPadding(containerPadding);
//
//			pauseGameButton.setPrefWidth(this.getPrefWidth());
//
//			surrenderButton.setPrefWidth(this.getPrefWidth() / 2);
//			remiButton.setPrefWidth(this.getPrefWidth() / 2);
//		}
//		
//		private void initListeners() {
//			pauseGameButton.setOnAction( e -> settingsPresenter.pauseGameButtonClicked());
//		}
//		
//		
//
//	}

	private class NoGamePane extends HBox {

	}

	public void botSelectedPressed(BotRepresentation source) {
		settingsPresenter.botSelected(source);
	}

}
