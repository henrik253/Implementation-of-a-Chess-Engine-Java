package main.gui.game.board;

import javafx.application.Platform;
import main.gui.MainPresenter;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.SimplePiece;

public class GamePresenter  {
	private GameView gameView;
	private MainPresenter mainPresenter;

	public void setBoard(SimplePiece[][] simplePieceBoard) {
		gameView.initSimplePieceBoard(simplePieceBoard);
	}

	public boolean moveRequest(Vector2D oldPos, Vector2D newPos) {
		boolean isValidMove = mainPresenter.moveRequest(oldPos, newPos);

		if (isValidMove) {
			gameView.loadSimpleBoard(mainPresenter.getGameBoard());
		}

		return isValidMove;
	}

	public void requestBotMove() {
		new Thread(() -> {
			try {
				Thread.sleep(200); // Simulate some heavy computation
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			// Update UI on JavaFX Application Thread
			Platform.runLater(() -> {
				gameView.loadSimpleBoard(mainPresenter.requestBotMove());
			});
		}).start();
	}

	public void setPieceListenerDisabled(ChessPieceColor color, boolean disabled) {
		gameView.setPieceListenerDisabled(color, disabled);
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

	public void userPlaysAs(ChessPieceColor selectedColor) {
		gameView.userPlaysAs(selectedColor);
	}

}
