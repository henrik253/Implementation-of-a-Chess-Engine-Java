package main.gui.game.gameOver;

import main.gui.MainPresenter;
import main.gui.game.board.GamePresenter;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;
import main.model.GameStatistic;
import utils.State;

public class GameOverPresenter {

	private GameOverView gameOverView;
	private MainPresenter mainPresenter;
	private BotRepresentation selectedBot;

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

	public void setEnableView(boolean enable) {
		gameOverView.setDisable(!enable);
		gameOverView.enable(enable);
	}

	public void botSelected(BotRepresentation source) {
		if (State.gameState.noGame()) {
			gameOverView.drawSelectedBot(source);
			selectedBot = source;
		}

	}

	public void gameOver() {
		gameOverView.drawScore(mainPresenter.getUserWins(), mainPresenter.getBotWins());
		
	}

}
