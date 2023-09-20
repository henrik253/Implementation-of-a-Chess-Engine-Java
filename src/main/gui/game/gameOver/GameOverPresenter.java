package main.gui.game.gameOver;

import main.gui.MainPresenter;
import main.gui.Presenter;
import main.gui.game.board.GamePresenter;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;
import main.model.gameStates.State;

public class GameOverPresenter extends Presenter {

	private GameOverView gameOverView;
	private MainPresenter mainPresenter;
	private BotRepresentation selectedBot;

	private GameStatistic gameStatistic = new GameStatistic();

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
		gameOverView.setVisible(!disable);
	}

	public void botSelected(BotRepresentation source) {
		if (State.gameState.noGame()) {
			gameOverView.drawSelectedBot(source);
			selectedBot = source;
		}

	}

	public void gameOver() {
		gameStatistic.gamePlayed(selectedBot);
		gameOverView.drawScore(gameStatistic.getUserWins(selectedBot), gameStatistic.getBotWins(selectedBot));
	}

}
