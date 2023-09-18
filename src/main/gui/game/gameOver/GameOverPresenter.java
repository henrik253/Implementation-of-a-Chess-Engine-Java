package main.gui.game.gameOver;

import main.gui.MainPresenter;
import main.gui.Presenter;
import main.gui.game.board.GamePresenter;

public class GameOverPresenter extends Presenter {

	private GameOverView gameOverView;
	private MainPresenter mainPresenter;

	public GameOverView getGameOverView() {
		return gameOverView;
	}

	public void setGameOverView(GameOverView gameOverView) {
		this.gameOverView = gameOverView;
	}

	public MainPresenter getMainPresenter() {
		return mainPresenter;
	}

	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

	public void playAgainButtonPressed() {
		mainPresenter.playAgainButtonPressed();
	}

	public void setDisableView(boolean disable) {
		gameOverView.setDisable(disable);
	}

}
