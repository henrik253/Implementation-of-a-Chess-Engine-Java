package main.gui.game.board;

import main.gui.MainPresenter;
import main.gui.Presenter;
import main.model.chessPieces.SimplePiece;

public class GamePresenter extends Presenter {
	private GameView gameView;
	private MainPresenter mainPresenter;

	
	public void setBoard(SimplePiece[][] simplePieceBoard) {
		gameView.initSimplePieceBoard(simplePieceBoard);
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

}
