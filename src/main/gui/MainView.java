package main.gui;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.gui.game.board.GameView;
import main.gui.game.gameOver.GameOverView;
import main.gui.game.gameStart.GameStartView;
import main.gui.game.settings.Settings;
import main.gui.game.settings.SettingsView;

public class MainView extends AnchorPane {

	private MainPresenter mainPresenter;

	private Settings settings;
	private SettingsView settingsView;
	private GameView gameView;
	private GameOverView gameOverView;
	private GameStartView gameStartView;

	public void init() {
		this.getChildren().add(gameView);
		this.getChildren().add(settingsView);
		this.getChildren().add(gameStartView);
		this.getChildren().add(gameOverView);
		
		this.gameOverView.setVisible(false);
		
	}

	public void drawGameStart() {

	}

	public void drawGame() {
	}

	public void drawGameOver() {
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public MainPresenter getMainPresenter() {
		return mainPresenter;
	}

	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

	public SettingsView getSettingsView() {
		return settingsView;
	}

	public void setSettingsView(SettingsView settingsView) {
		this.settingsView = settingsView;
	}

	public GameView getGameView() {
		return gameView;
	}

	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	public GameOverView getGameOverView() {
		return gameOverView;
	}

	public void setGameOverView(GameOverView gameOverView) {
		this.gameOverView = gameOverView;
	}

	public GameStartView getGameStartView() {
		return gameStartView;
	}

	public void setGameStartView(GameStartView gameStartView) {
		this.gameStartView = gameStartView;
	}

}
