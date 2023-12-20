package main.gui.game.sidebar;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.Settings;
import main.gui.game.sidebar.botrepresentations.BotRepresentation;
import main.gui.game.sidebar.botrepresentations.BotSelectionView;

public class SettingsView extends Pane {

	private SettingsPresenter settingsPresenter;
	private Settings settings;

	private Pane gameContent;

	private BotSelectionView botSelectionView;

	private InsertFENView insertBoardView;

	public void init() { // init() is called when all Comps. are connected
		this.setTranslateX(settings.settingsViewTranslateX.get());
		this.setTranslateY(settings.settingsViewTranslateY.get());
		this.setPrefWidth(settings.settingsViewPrefWidth.get());
		this.setPrefHeight(settings.settingsViewPrefHeight.get());
		this.setPrefHeight(settings.WINDOW_HEIGHT);
		this.setId("SettingsView");
		this.botSelectionView = new BotSelectionView(this);
		this.insertBoardView = new InsertFENView(this, getPrefHeight(), getPrefWidth(),settings);

		initGameContent();

		// inGameContent = new InGamePane();

		this.getChildren().addAll(botSelectionView, insertBoardView);

	}

	public void toggleSelectSurrenderButton(boolean inGame) {
		Platform.runLater( () -> 	botSelectionView.toggleSelectSurrenderButton(inGame));
	
	}

	private void initGameContent() {
		gameContent = new Pane();
		this.getChildren().add(gameContent);
	}

	public void saveFENString(String fen) {
		settingsPresenter.loadBoard(fen);
		settings.selectedFEN.set(fen);
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

	public void botSelectedPressed(BotRepresentation source) {
		settingsPresenter.botSelected(source);
	}

	public void surrenderButtonPressed() {
		settingsPresenter.surrenderButtonPressed();
	}

	public void setDisableColorSelect(boolean disable) {
		botSelectionView.setDisableColorSelect(disable);
	}

	public void setVisibleBotSelectButtons(boolean disable) {
		botSelectionView.setVisibleBotSelectButtons(disable);
	}

	public void setVisibleInsertFENView(boolean visible) {
		insertBoardView.setVisible(visible);
	}

	public void bot1SliderMillisChanged(int millis) {
		settingsPresenter.bot1SliderMillisChanged(millis);
	}

	public void bot2SliderDepthChanged(int depth) {
		settingsPresenter.bot2SliderDepthChanged(depth);
		
	}

	public void disableSlider(boolean b) {
		botSelectionView.disableSlider(b);	
	}

}
