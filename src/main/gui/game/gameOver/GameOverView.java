package main.gui.game.gameOver;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.Settings;
import main.gui.game.Overlay;

public class GameOverView extends Overlay {

	private GameOverPresenter gameOverPresenter;
	private Settings settings;
	private Overlay overlay;

	private Pane contentBox;
	private Button playButton;

	private void initContentBox() {
		contentBox = new Pane();
		contentBox.setPrefHeight(settings.boardHeight.get() / 4 + settings.contentBoxHeightLayoutOffset.get());
		contentBox.setPrefWidth(settings.boardWidth.get() / 2 + settings.contentBoxWidthLayoutOffset.get());
		contentBox.setTranslateX((settings.boardWidth.get() / 2) - (contentBox.getPrefWidth() / 2));
		contentBox.setTranslateY((settings.boardHeight.get() / 2) - (contentBox.getPrefHeight() / 2));
		contentBox.setId("contentBox");
	}

	private void initPlayButton() {
		playButton = new Button(settings.playButtonTextStart.get());
		playButton.setPrefHeight(contentBox.getPrefHeight() / 5);
		playButton.setPrefWidth((contentBox.getPrefWidth() / 5) * 4);
		playButton.setTranslateX((contentBox.getPrefWidth() - playButton.getPrefWidth()) / 2);
		playButton.setTranslateY((contentBox.getPrefHeight() / 4) * 3);
		playButton.setOnMousePressed(this::handle);
	}

	public Overlay getOverlay() {
		return overlay;
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}

	public GameOverPresenter getGameOverPresenter() {
		return gameOverPresenter;
	}

	public void setGameOverPresenter(GameOverPresenter gameOverPresenter) {
		this.gameOverPresenter = gameOverPresenter;
	}

}
