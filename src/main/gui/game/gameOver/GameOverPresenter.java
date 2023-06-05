package main.gui.game.gameOver;

import main.gui.Presenter;
import main.gui.game.board.GamePresenter;

public class GameOverPresenter extends Presenter {

	private GameOverView gameOverView;
	private GamePresenter gamePresenter;

	public GameOverView getGameOverView() {
		return gameOverView;
	}

	public void setGameOverView(GameOverView gameOverView) {
		this.gameOverView = gameOverView;
	}

	public GamePresenter getGamePresenter() {
		return gamePresenter;
	}

	public void setGamePresenter(GamePresenter gamePresenter) {
		this.gamePresenter = gamePresenter;
	}

}
