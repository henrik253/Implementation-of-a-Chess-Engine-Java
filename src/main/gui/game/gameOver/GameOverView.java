package main.gui.game.gameOver;

import main.gui.game.Overlay;

public class GameOverView extends Overlay {

	private GameOverPresenter gameOverPresenter;

	private Overlay overlay;
	
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
