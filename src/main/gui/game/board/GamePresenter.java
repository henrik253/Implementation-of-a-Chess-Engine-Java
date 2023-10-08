package main.gui.game.board;

import java.util.List;

import javafx.application.Platform;
import main.Settings;
import main.gui.MainPresenter;
import utils.ChessPieceColor;
import utils.Move;
import utils.SimplePiece;
import utils.Vector2D;

public class GamePresenter {
	private GameView gameView;
	private MainPresenter mainPresenter;
	private Settings settings;

	private Vector2D markedSquareOld;
	private Vector2D markedSquareNew;

	private Vector2D lastMarkedKingPos;

	private List<Vector2D> moveablePositions;
	private Vector2D piecePosition; 
	
	public void setBoard(SimplePiece[][] simplePieceBoard) {
		gameView.initSimplePieceBoard(simplePieceBoard);
	}

	public boolean moveRequest(Vector2D oldPos, Vector2D newPos) {
		boolean isValidMove = mainPresenter.moveRequest(oldPos, newPos);

		if (isValidMove) {
			gameView.loadSimpleBoard(mainPresenter.getGameBoard());
			markCheckedKing();
			markSquaresPieceMoved(oldPos, newPos);
		}

		return isValidMove;
	}

	public void userMoveSucceeded() {
		new Thread(() -> {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			Platform.runLater(() -> {
				if (mainPresenter.gameIsRunning()) {
					gameView.loadSimpleBoard(mainPresenter.requestBotMove());
					Move botMove = mainPresenter.getLastBotMove();
					markCheckedKing();
					markSquaresPieceMoved(botMove.getOldPos(), botMove.getNewPos());
				}
			});
		}).start();
	}

	public void startGame() {
		if (markedSquareOld != null && markedSquareNew != null) {
			gameView.unmarkSquare(markedSquareOld);
			gameView.unmarkSquare(markedSquareNew);
		}

		if (lastMarkedKingPos != null) {
			gameView.unmarkSquare(lastMarkedKingPos);
		}
	}

	private void markCheckedKing() {
		if (mainPresenter.kingInCheck()) {
			lastMarkedKingPos = mainPresenter.getKingCheckedPos();
			gameView.markSquare(lastMarkedKingPos, settings.kingCheckMarked.get());
			System.out.println("mainPresenter.kingInCheck() ");
		} else if (lastMarkedKingPos != null) {
			gameView.unmarkSquare(lastMarkedKingPos);
		}
	}

	private void markSquaresPieceMoved(Vector2D oldPos, Vector2D newPos) {
		if (markedSquareOld != null && markedSquareNew != null) {
			gameView.unmarkSquare(markedSquareOld);
			gameView.unmarkSquare(markedSquareNew);
		}

		gameView.markSquare(oldPos, settings.markedColorBright.get());
		gameView.markSquare(newPos, settings.markedColorDark.get());

		markedSquareOld = oldPos;
		markedSquareNew = newPos;
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

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public void markMoveableSquares(Vector2D pos) {
		piecePosition = pos;
		moveablePositions = mainPresenter.getMoveablePositions(pos);
		gameView.markSquare(pos,settings.markedColorBright.get());
		moveablePositions.forEach(position -> gameView.markSquare(position, settings.moveablePosMarked.get()));
	}

	public void unMarkMoveableSquares() {
		if (moveablePositions == null)
			return;
		gameView.unmarkSquare(piecePosition);
		moveablePositions.forEach(position -> gameView.unmarkSquare(position));
	}

}
