package main.gui.game.board;

import main.gui.MainPresenter;
import main.gui.Presenter;
import main.model.Vector2D;
import main.model.chessPieces.SimplePiece;

public class GamePresenter extends Presenter {
	private GameView gameView;
	private MainPresenter mainPresenter;

	public void setBoard(SimplePiece[][] simplePieceBoard) {
		gameView.initSimplePieceBoard(simplePieceBoard);
	}

	public boolean moveRequest(int oldX, int oldY, int newX, int newY) {
		boolean succeedMove = mainPresenter.moveRequest(oldX, oldY, newX, newY);

		if (succeedMove) {
			gameView.loadSimpleBoard(mainPresenter.getGameBoard());

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			gameView.loadSimpleBoard(mainPresenter.requestBotMove());
		}

		return succeedMove;
	}

	public GameView getGameView() {
		return gameView;
	}

	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	public MainPresenter getMainPresenter() {
		return mainPresenter;
	}

	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

//	public void removePieceFromBoard(Vector2D pos) {
//		gameView.removePieceFromBoard(pos);
//		
//	}

}
