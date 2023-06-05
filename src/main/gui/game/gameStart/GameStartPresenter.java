package main.gui.game.gameStart;

import main.gui.MainPresenter;
import main.gui.Presenter;

public class GameStartPresenter extends Presenter {

	private GameStartView gameStartView;
	private MainPresenter mainPresenter;

	public void playButtonPressed() {
		mainPresenter.startGame();
	}
	
	public void setDisableView(boolean disable) {
		gameStartView.setVisible(disable);
	}

	
	public GameStartView getGameStartView() {
		return gameStartView;
	}

	public void setGameStartView(GameStartView gameStartView) {
		this.gameStartView = gameStartView;
	}

	public MainPresenter getMainPresenter() {
		return mainPresenter;
	}

	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

}
