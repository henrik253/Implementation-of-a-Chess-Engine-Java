package main.gui.game.gameOver;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.Settings;
import main.gui.game.Overlay;

public class GameOverView extends Pane {

	private GameOverPresenter gameOverPresenter;
	private Settings settings;
	private Overlay overlay;

	private Pane contentBox;
	private Button playAgainButton;

	public void init() {
		initContentBox();
		initPlayButton();

		style();
	}

	private void style() {
		this.getChildren().add(overlay);
		this.getChildren().add(contentBox);

		this.contentBox.getChildren().addAll(playAgainButton);
	}

	private void initContentBox() {
		contentBox = new Pane();
		contentBox.setPrefHeight(settings.boardHeight.get() / 4 + settings.contentBoxHeightLayoutOffset.get());
		contentBox.setPrefWidth(settings.boardWidth.get() / 2 + settings.contentBoxWidthLayoutOffset.get());
		contentBox.setTranslateX((settings.boardWidth.get() / 2) - (contentBox.getPrefWidth() / 2));
		contentBox.setTranslateY((settings.boardHeight.get() / 2) - (contentBox.getPrefHeight() / 2));
		contentBox.setId("contentBox");
	}

	private void initPlayButton() {
		playAgainButton = new Button(settings.playButtonTextStart.get());
		playAgainButton.setPrefHeight(contentBox.getPrefHeight() / 5);
		playAgainButton.setPrefWidth((contentBox.getPrefWidth() / 5) * 4);
		playAgainButton.setTranslateX((contentBox.getPrefWidth() - playAgainButton.getPrefWidth()) / 2);
		playAgainButton.setTranslateY((contentBox.getPrefHeight() / 4) * 3);
		playAgainButton.setOnMousePressed(event -> gameOverPresenter.playAgainButtonPressed());
	}

	public Overlay getOverlay() {
		return overlay;
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public GameOverPresenter getGameOverPresenter() {
		return gameOverPresenter;
	}

	public void setGameOverPresenter(GameOverPresenter gameOverPresenter) {
		this.gameOverPresenter = gameOverPresenter;
	}

}
